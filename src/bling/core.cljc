;; TODO

;; callout

;; - Should we do callout-data as separate function, 
;;   or document the :data? option?

;; point of interest 

;; - Should take vector as the :line entry
;;     If a vector is supplied, draw a range of numbered lines if you can read
;;     ns at those lines.

;; - Should additional file-info (string), which would override the gen option
;; 


;; Adds about 12kb to a cljs bundle

(ns bling.core
  (:require [clojure.string :as string]
            ;; [bling.macros :refer [let-map keyed ?]] ;;<-- just for debugging
            [bling.macros :refer [let-map keyed]]
            #?(:cljs [goog.object])
            #?(:cljs [bling.js-env :refer [node?]])))

(declare xterm-colors-by-id)

(def ^:private ESC "\u001B[")
(def ^:private OSC "\u001B]")
(def ^:private BEL "\u0007")
(def ^:private SEP ";")

(defn hyperlink [text url]
  #?(:cljs
     url
     :clj
     (apply str
            [OSC
             "8"
             SEP
             SEP
             url
             BEL
             text
             OSC
             "8"
             SEP
             SEP
             BEL])))

(def ^:private browser-dev-console-props
  [:text-decoration-line
   :text-decoration-style
   :text-decoration-color
   :text-underline-offset
   :text-decoration-thickness
   :text-decoration
   :line-height
   :font-weight
   :font-style
   :color
   :background-color
   :border-radius
   :padding
   :padding-block
   :padding-block-start
   :padding-block-end
   :padding-inline
   :padding-inline-start
   :padding-inline-end
   :padding-bottom
   :padding-top
   :padding-right
   :padding-left
   :margin
   :margin-block
   :margin-block-start
   :margin-block-end
   :margin-inline
   :margin-inline-start
   :margin-inline-end
   :margin-bottom
   :margin-top
   :margin-right
   :margin-left])

(def ^:public system-colors-source
  {"system-black"   {:sgr 0}
   "system-maroon"  {:sgr 1}
   "system-green"   {:sgr 2}
   "system-olive"   {:sgr 3}
   "system-navy"    {:sgr 4}
   "system-purple"  {:sgr 5}
   "system-teal"    {:sgr 6}
   "system-silver"  {:sgr 7}
   "system-grey"    {:sgr 8}
   "system-red"     {:sgr 9}
   "system-lime"    {:sgr 10}
   "system-yellow"  {:sgr 11}
   "system-blue"    {:sgr 12}
   "system-fuchsia" {:sgr 13}
   "system-aqua"    {:sgr 14}
   "system-white"   {:sgr 15}})

(def ^:private colors-source
  (merge {"red"     {:sgr 196 :semantic "negative"}
          "orange"  {:sgr 208 :semantic "warning"}
          "yellow"  {:sgr 178}
          "olive"   {:sgr 106}
          "green"   {:sgr 40 :semantic "positive"}
          "blue"    {:sgr 39 :semantic "accent"}
          "purple"  {:sgr 141}
          "magenta" {:sgr 201}
          "gray"    {:sgr 247 :semantic "subtle"}
          "black"   {:sgr 16}
          "white"   {:sgr 231}}
         system-colors-source))


(def ^:private semantics-by-semantic-type
  {"error"    "negative"
   "warning"  "warning"
   "positive" "positive"
   "info"     "accent"
   "accent"   "accent"
   "subtle"   "subtle"
   "neutral"  "neutral"})

(defn concatv
  "Concatenate `xs` and return the result as a vector."
  [& xs]
  (into [] cat xs))


(def ^:private all-color-names
  ;; TODO - perf use reduce here?
  (into #{}
        (concatv (keys semantics-by-semantic-type)
                (vals semantics-by-semantic-type)
                (keys colors-source))))

(def ^:private color-names-by-semantic*
  (reduce-kv (fn [m color {:keys [semantic]}]
               (if semantic
                 (assoc m semantic color)
                 m))
             {}
             colors-source))

(def ^:private alert-type->label
  {"warning" "WARNING"
   "error"   "ERROR"
   "info"    "INFO"})


;; Helper functions -----------------------------------------------------------

(defn ^:public ?sgr
  "For debugging of sgr code printing.

   Prints the value with escaped sgr codes so you can read them in terminal
   emulators (otherwise text would just get colored).

   Returns the value."
  [s]
  ;; TODO - try to figure out way you can preserve the color in the output,
  ;; which would help even more for debugging.
  (println (string/replace s
                           #"\u001b\[([0-9;]*)[mK]"
                           (str "\033[38;5;231;48;5;247m"
                                "\\\\033["
                                "$1"
                                "m"
                                "\033[0;m")))
  s)

(defn ^:public !?sgr
  "Temporarily silences debugging of sgr code printing.
   Returns the value."
  [s]
  s)

(defn- maybe [x pred]
  (when (if (set? pred)
          (contains? pred x)
          (pred x))
    x))

(defn- nameable? [x]
  (or (string? x) (keyword? x) (symbol? x)))

(defn- as-str [x]
  (str (if (or (keyword? x) (symbol? x)) (name x) x)))

(defn- char-repeat [n s]
  (when (pos-int? n)
    (string/join (repeat n (or s "")))))

(defn- spaces [n] (string/join (repeat n " ")))

;; TODO - confirm we don't need this anymore and delete
(defn- readable-sgr [x]
  (let [f #(str "\\033" (subs x 1))]
    #?(:cljs (if node? (f) x)
       :clj  (f))))

(defn- ns-info-str
  [{:keys [file line column file-line-column]}]
  (if (not (string/blank? file-line-column))
    file-line-column
    (str (some-> file (str ":")) line ":" column)))

(defn- regex? [v]
  #?(:clj  (-> v type str (= "class java.util.regex.Pattern"))
     :cljs (-> v type str (= "#object[RegExp]"))))

(defn- surround-with-quotes [x]
  (str "\"" x "\""))

(defn- shortened
  "Stringifies a collection and truncates the result with ellipsis 
   so that it fits on one line."
  [v limit]
  (let [as-str                        (str v)
        regex?                        (regex? v)
        double-quotes?                (or (string? v) regex?)
        regex-pound #?(:cljs nil :clj (when regex? "#"))]
    (if (> limit (count as-str))
      (if double-quotes?
        (str regex-pound (surround-with-quotes as-str))
        as-str)
      (let [ret* (-> as-str
                     (string/split #"\n")
                     first)
            ret  (if (< limit (count ret*))
                   (let [ret (->> ret*
                                  (take limit)
                                  string/join)]
                     (str (if double-quotes?
                            (str regex-pound (surround-with-quotes ret))
                            ret)
                          (when-not double-quotes? " ")
                          "..."))
                   ret*)]
        ret))))

                                                                  
(defn- poi-text-underline-str [n str-index text-decoration-style]
  (str (string/join (repeat str-index " "))
       (string/join (repeat n
                            (case text-decoration-style
                              "wavy" "^"
                              "dashed" "-"
                              "dotted" "•"
                              "double" "═"
                              "^")))))

(def form-limit 33)

(defn poi-text-underline
  [{:keys [form form-as-str text-decoration-index text-decoration-style] :as m}]
  (if (and text-decoration-index
              (or (pos? text-decoration-index)
                  (zero? text-decoration-index))
              (coll? form)
              (< (count (as-str form)) form-limit))
    (let [form
          (into [] form)

          data
          (reverse
            (for [n (-> form count range reverse)]
              (let [v            (-> form (nth n nil))
                    value-as-str (str v)
                    len          (-> v str count)
                    sv           (-> form
                                     (subvec 0 n)
                                     str
                                     count)]
                {:strlen       len
                 :v            v
                 :value-as-str value-as-str
                 :index        n
                 :str-index    sv})))

          {:keys [strlen str-index]}
          (nth data text-decoration-index nil)]
      {:text-underline-str (poi-text-underline-str
                            strlen
                            str-index
                            text-decoration-style)})
    {:text-underline-str (poi-text-underline-str
                           (count form-as-str)
                           0
                           text-decoration-style)}))

(defn- css-stylemap->str [m]
  (reduce-kv (fn [acc k v]
               (if (and k v)
                 (str acc (as-str k) ":" (as-str v) ";")
                 acc))
             ""
             m))

(defn- x->sgr [x k]
  (when x
    (let [n (if (= k :fg) 38 48)]
      (if (int? x)
        (str n ";5;" x)
        (let [[r g b _] x
              ret (str n ";2;" r ";" g ";" b)]
          ret)))))

(defn- m->sgr
  [{fgc*  :color
    bgc*  :background-color
    :keys [font-style
           font-weight
           text-decoration
           disable-italics?
           disable-text-decoration?
           disable-font-weights?]
    :as   m}]
  (let [fgc             (x->sgr fgc* :fg)
        bgc             (x->sgr bgc* :bg)
        italic          (when (and (not disable-italics?)
                                   (contains? #{"italic" :italic} font-style))
                          "3")
        weight          (when (and (not disable-font-weights?)
                                   (contains? #{"bold" :bold} font-weight))
                          "1")

        text-decoration (when-not disable-text-decoration?
                          (cond
                            (contains? #{"underline" :underline}
                                       text-decoration)
                            "4"

                            (contains? #{"line-through" :strikethrough}
                                       text-decoration)
                            "9"))
        ret             (str "\033["
                             (string/join ";"
                                          (remove nil?
                                                  [italic
                                                   fgc
                                                   weight
                                                   bgc
                                                   text-decoration]))
                             "m")]

    ret))


;; Color-related fns  ---------------------------------------------------------
(def ^:private xterm-colors-by-id
  {0   "#000000"                                            ;; system-black
   1   "#800000"                                            ;; system-maroon
   2   "#008000"                                            ;; system-green
   3   "#808000"                                            ;; system-olive
   4   "#000080"                                            ;; system-navy
   5   "#800080"                                            ;; system-purple
   6   "#008080"                                            ;; system-teal
   7   "#c0c0c0"                                            ;; system-silver
   8   "#808080"                                            ;; system-grey
   9   "#ff0000"                                            ;; system-red
   10  "#00ff00"                                            ;; system-lime
   11  "#ffff00"                                            ;; system-yellow
   12  "#0000ff"                                            ;; system-blue
   13  "#ff00ff"                                            ;; system-fuchsia
   14  "#00ffff"                                            ;; system-aqua
   15  "#ffffff"                                            ;; system-white
   16  "#000000"                                            ;; black
   39  "#00afff"                                            ;; blue
   40  "#00d700"                                            ;; green
   106 "#87af00"                                            ;; olive
   141 "#af87ff"                                            ;; purple
   178 "#d7af00"                                            ;; yellow
   196 "#ff0000"                                            ;; red
   201 "#ff00ff"                                            ;; magenta
   208 "#ff8700"                                            ;; orange
   231 "#ffffff"                                            ;; white
   247 "#9e9e9e"                                            ;; gray
   })

(defn- assoc-hex-colors [m]
  (reduce-kv (fn [m color {:keys [sgr]}]
               (let [hex (get xterm-colors-by-id sgr nil)]
                 (assoc m color {:sgr sgr
                                 :css hex})))
             {}
             m))

(defn- reduce-colors [m1 m2]
  (reduce-kv (fn [m k color]
               (assoc m k (get m2 color)))
             {}
             m1))

(def ^:private color-codes
  (let [colors    (assoc-hex-colors colors-source)
        semantics (reduce-colors color-names-by-semantic* colors)
        callouts  (reduce-colors semantics-by-semantic-type semantics)]
    {:all              (merge colors semantics callouts)
     :colors           colors
     :semantics        semantics
     :callouts         callouts
     :colors+semantics (merge colors semantics)}))

(defn- reduce-colors-to-sgr-or-css [ctx m]
  (reduce-kv (fn [m k v]
               (assoc m k (if (map? v) (ctx v) v)))
             {}
             m))

(defn- convert-color [m k v]
  (assoc m
    k
    (if (contains? #{:background-color :color} k)
      (cond
        (nameable? v)
        (get (:all color-codes)
             (as-str v))

        (and (int? v) (<= 0 v 257))
        {:sgr v})
      v)))

(defn- et-vec? [x]
  (boolean (and (vector? x)
                (= 2 (count x))
                (-> x
                    (nth 0)
                    (maybe #(or (keyword? %)
                                (map? %)))))))


;; Formatting helper fns  -----------------------------------------------------

(defn- semantic-type [opts]
  (let [x (:colorway opts)]
    (cond
      (keyword? x) (name x)
      (string? x) x
      :else
      (some-> (when (map? x)
                (or (get x :colorway nil)
                    (get x "colorway" nil)))
              (maybe nameable?)
              name))))

(declare callout)
(declare bling)
(declare print-bling)


;; Formatting exceptions ----------------------------------------------------------

;; Stack trace preview intended for JVM clojure (no clojurescript)
;; TODO - put this through the paces and decided whether or not to expose in the
;; public API.
(defn stack-trace-preview
  "Creates a user-friendly stack-trace preview, limited to the frames which
   contain a match with the supplied regex, up to the `depth` value, if supplied.
   `depth` defaults to 7."
  [{:keys [error regex depth header]}]
  #?(:clj
     (if-let [strace (some->> (maybe error #(instance? Exception %))
                              .getStackTrace
                              seq)]
       (let [strace-len  (count strace)
             depth       (or (maybe depth pos-int?) 7)

             ;; Get a mini-strace, limited to the number of frames that will be
             ;; displayed based on `depth`
             mini-strace (->> strace
                              (take depth)
                              (mapv StackTraceElement->vec))

             ;; If regex is legit, get a list of indexes that match the regex
             ;; passed in by user. Regex will match on ns or filename where
             ;; user's their program lives. Then get the last index of a match
             ;; (within the mini-strace). If regex is not legit, use the depth.
             last-index  (if (= java.util.regex.Pattern (type regex))
                           ;; TODO - perf - use transduction here
                           (some->> mini-strace
                                    (keep-indexed
                                      (fn [i [f]]
                                        (when (re-find regex (str f))
                                          i)))
                                    seq
                                    (take depth)
                                    last)
                           (dec depth))

             ;; Get all the frames up to the last index
             trace*      (when last-index
                           (->> mini-strace (take (inc last-index))))
             len         (when trace* (count trace*))
             with-header [(or header
                              (bling [:italic "Stacktrace preview:"])) "\n"]
             trace       (some->> trace* (interpose "\n") (into with-header))
             num-dropped (when trace
                           (let [n (- (or strace-len 0) (or len 0))]
                             (some->> (maybe n pos-int?)
                                      (str "\n...+"))))

             ;; Conj num-dropped annotation to mini-strace
             trace       (some-> trace (conj num-dropped))]

         ;; Create and return multiline string
         (apply str trace))

       ;; Print a warning if option args are bad
       (callout {:type :warning}
                (bling
                  "bling.core/stack-trace-preview\n\n"
                  "Value of the "
                  [:bold :error]
                  " option should be an instance of "
                  [:bold 'java.lang.Exception.]
                  "\n\n"
                  "Value received:\n"
                  [:bold (shortened error 33)]
                  "\n\n"
                  "Type of value received:\n"
                  [:bold (str (type error))]
                  )))))

;; Race-condition-free version of clojure.core/println,
;; Maybe useful to keep around if any weird behavior arises.
#?(:clj
   (defn- safe-println [& more]
     (.write *out* (str (clojure.string/join " " more) "\n"))))


;; Shared cljs fns -------------------------------------------------------------
#?(:cljs
   (do
     (defn ^:public print-bling
           "For browser usage, sugar for the the following:
            `(.apply js/console.log js/console (goog.object/get o \"consoleArray\"))`

            Example:
            `(print-bling (bling [:bold.blue \"my blue text\"]))"
           ([o]
            (print-bling o js/console.log))
           ([o f]
            (.apply f js/console (goog.object/get o "consoleArray"))))

     (deftype
       ^{:doc
         "A js object with the the following fields:
         `tagged`
         A string with the appropriate tags for styling in browser consoles

         `css`
         An array of styles that sync with the tagged string.

         `consoleArray`
         An array by `Array.unshift`ing the tagged string onto the css array.
         This is the format that is needed for printing in a browser console, e.g.
         `(.apply js/console.log js/console (goog.object/get o \"consoleArray\"))`.

         For browser usage, sugar for the above `.apply` call is provided with
         `bling.core/print-bling`. You can use it like this:
         `(print-bling (bling [:bold.blue \"my blue text\"]))"}
       Enriched
       [tagged css consoleArray args])))

                                                 
;; PPPPPPPPPPPPPPPPP        OOOOOOOOO     IIIIIIIIII
;; P::::::::::::::::P     OO:::::::::OO   I::::::::I
;; P::::::PPPPPP:::::P  OO:::::::::::::OO I::::::::I
;; PP:::::P     P:::::PO:::::::OOO:::::::OII::::::II
;;   P::::P     P:::::PO::::::O   O::::::O  I::::I  
;;   P::::P     P:::::PO:::::O     O:::::O  I::::I  
;;   P::::PPPPPP:::::P O:::::O     O:::::O  I::::I  
;;   P:::::::::::::PP  O:::::O     O:::::O  I::::I  
;;   P::::PPPPPPPPP    O:::::O     O:::::O  I::::I  
;;   P::::P            O:::::O     O:::::O  I::::I  
;;   P::::P            O:::::O     O:::::O  I::::I  
;;   P::::P            O::::::O   O::::::O  I::::I  
;; PP::::::PP          O:::::::OOO:::::::OII::::::II
;; P::::::::P           OO:::::::::::::OO I::::::::I
;; P::::::::P             OO:::::::::OO   I::::::::I
;; PPPPPPPPPP               OOOOOOOOO     IIIIIIIIII
                                                 

;; Line and point of interest public fns  -------------------------------------
(defn- enriched-args [o]
  #?(:cljs
     (if node?
       (when o [o])
       (if (instance? Enriched o)
         (goog.object/get o "args")
         (when o [o])))
     :clj
     (when o [o])))


(defn ^:public point-of-interest
  "A namespace info diagram which identifies a specific form. This provides the
   namespace, column, and line number, and a bolded, potentially truncated,
   representation of the specific form of interest. This form representation is
   accented with a squiggly underline.
   
   The `:line`, `:column`, `:form`, and `:file` options must all be present in
   order for the namespece info diagram to be rendered. If the `:form` option is
   supplied, but any of the others are omitted, only the form will be rendered
   (with a squiggly underline and no stacktrace diagram).
   
   By default, the diagram is created with a leading and trailing newlines.
   This can be set to zero, or increased, with the `:margin-block` option.
   
| Key                | Pred                   | Description                                                  |
| :--------          | -----------------      | ------------------------------------------------------------ |
| `:file`            | `string?`              | File or namespace                                            |
| `:line`            | `integer?`             | Line number                                                  |
| `:column`          | `integer?`             | Column number                                                |
| `:form`            | `any?`                 | The form to draw attention to. Will be cast to string and truncated at 33 chars |
| `:header`          | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:body`            | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:margin-block`    | `int?`                 | Controls the number of blank lines above and below the diagram.<br/>Defaults to `1`.|
| `:type`            | #{`:error` `:warning`} | Automatically sets the `:text-decoration-color`. |
| `:text-decoration-color` | #{`keyword?` `string?`} | Controls the color of the underline. Should be one of: `:error` `:warning`, or `:neutral`.<br>Can also be any one of the pallete colors such as  `:magenta`, `:green`,  `:negative`, `:neutral`, etc. Defaults to `:neutral` |
| `:text-decoration-style` | #{`:wavy` `:solid` `:dashed` `:dotted` `:double`} | Controls the color of the underline. |
| `:text-decoration-index` | `pos-int?` | If the value of `:form` is a collection, this is the index of the item to apply text-decoration (underline). |
"

  [{:keys [line
           file
           column
           form
           header
           body
           margin-block
           text-decoration-color
           type]
    :as   opts}]
  (let [type             (some-> type as-str (maybe #{"warning" "error"}))
        file-info        (ns-info-str opts)
        gutter           (some-> line str count spaces)
        underline-color  (or (some->> type (get semantics-by-semantic-type))
                             (some-> text-decoration-color
                                     as-str
                                     (maybe all-color-names))
                             "neutral")
        form-as-str      (shortened form 33)
        underline-str    (-> opts
                             (assoc :form-as-str form-as-str)
                             poi-text-underline
                             :text-underline-str)
        bolded-form      [{:font-weight :bold} form-as-str]
        underline-styled [{:font-weight :bold
                           :color       underline-color} underline-str]
        header           (enriched-args header)
        body             (enriched-args body)
        mb*              (or (some-> margin-block (maybe pos-int?))
                             (if (some-> margin-block zero?)
                               0
                               1))
        mb               (char-repeat mb* "\n")
        diagram-char     #?(:cljs (fn [s] s) :clj #(bling [:subtle %]))   
        diagram          (cond
                           (and line column file form)
                           [mb
                            gutter (diagram-char " ┌─ ") file-info "\n"
                            gutter (diagram-char " │ ") "\n"
                            line (diagram-char " │ ") bolded-form "\n"
                            gutter (diagram-char " │ ") underline-styled
                            mb gutter]

                           form
                           [mb
                            bolded-form "\n"
                            underline-styled
                            mb]) 
        ret              (apply bling
                                (concatv header
                                         (when header ["\n"])
                                         diagram
                                         (when body ["\n"])
                                         body))]
    ret))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Enriched text public fns and helpers  --------------------------------------

(def gutter-char "█")
(def gutter-char-lower-seven-eighths "▆")

(declare ln)
(declare lns)

(defn- gutter-marquee-label
  [{:keys [padding-left
           padding-top
           margin-left 
           label 
           label-string 
           border-style]
    :as m}]
  (let [margin-left-str     (char-repeat margin-left gutter-char)
        hrz                 #(char-repeat padding-left %)
        label-lns           (-> label as-str string/split-lines)
        label-length        (some->> label-lns (mapv count) (apply max))
        label-string-lns    (some-> label-string string/split-lines)
        label-string-length (some->> label-string-lns (mapv count) (apply max))
        label-length        (or label-string-length label-length)
        bs                  border-style]
    (string/join
     (interpose
      "\n"
      (concatv
       [(bling [bs
                (str
                 (char-repeat (inc margin-left) "▄")
                 " "
                 " ┏━━"
                 (char-repeat label-length "━")
                 "━━┓")])
        (str (bling [bs
                     (str margin-left-str
                          gutter-char
                          (hrz " ")
                          "┃  ")])
             (bling [{:font-color :neutral}
                     (first label-lns)])
             (bling [bs "  ┃"]))]
       (for [ln (rest label-lns)]
         (bling margin-left-str
                [border-style (str "┃" (hrz " ") "┃  ")]
                (bling [:italic.neutral.bold ln])
                (bling [bs
                        (str (char-repeat (max 0 (- label-length (count ln)))
                                          " ")
                             "  ┃")])))
       [(bling [bs
                (str margin-left-str
                     gutter-char
                     (hrz " ")
                     "┗━━"
                     (char-repeat label-length "━")
                     "━━┛")])]
       (mapv (fn [_]
               (bling [bs
                        (str margin-left-str
                             gutter-char
                             (hrz " "))]))
         (range padding-top)))))))


(defn- sideline-marquee-label
  [{:keys [padding-left
           padding-left-str 
           margin-left 
           label 
           label-string 
           theme
           border-style]}]
  (let [margin-left-str     (char-repeat margin-left " ")
        b?                  (= theme "sideline-bold")
        hrz                 #(char-repeat padding-left %)
        label-lns           (some-> label as-str string/split-lines)
        label-length        (some->> label-lns (mapv count) (apply max))
        label-string-lns    (some-> label-string string/split-lines)
        label-string-length (some->> label-string-lns (mapv count) (apply max))
        label-length        (or label-string-length label-length)
        bs                  border-style]
    (string/join
     (interpose
      "\n"
      (concatv
       [(bling [bs
                (str margin-left-str
                     padding-left-str
                     (if b? " ┏━━" " ┌──")
                     (char-repeat label-length (if b? "━" "─"))
                     (if b? "━━┓" "──┐"))])
        (str (bling [bs
                     (str margin-left-str
                          (if b?
                            (str "┏" (hrz "━") "┫  ")
                            (str "┌" (hrz "─") "┤  ")))])
             (bling [{:font-color :neutral}
                     (first label-lns)])
             (bling [bs (if b? "  ┃" "  │")]))]
       (for [ln (rest label-lns)]
         (bling margin-left-str
                [bs (if b? (str "┃" (hrz " ") "┃  ")
                                  (str "│" (hrz " ") "│  "))]
                (bling [:italic.neutral.bold ln])
                (bling [bs
                        (str (char-repeat (max 0 (- label-length (count ln)))
                                          " ")
                             (if b? "  ┃" "  │"))])))
       [(bling [bs
                (str margin-left-str
                     (if b? (str "┃" (hrz " ") "┗━━")
                         (str "│" (hrz " ") "└──"))
                     (char-repeat label-length (if b? "━" "─"))
                     (if b? "━━┛" "──┘"))])])))))

(defn- gutter-label-line-zero? [m i]
  (and (= (:theme m) "gutter")
       (= (:current-line-type m) :label)
       i
       (zero? i)))

(defn- current-margin-left-str-key
  [m i gutter-label-line-zero?]
  (cond
    (and (= (:theme m) "rainbow-gutter") 
         i
         (odd? i))
    :margin-left-str-odd 

    gutter-label-line-zero?
    :margin-left-str-zero

    :else
    :margin-left-str))

(defn- ln 
  ([m s]
   (ln m nil s))
  ([m i s]
   (let [gutter-label-line-zero? (gutter-label-line-zero? m i)
         current-margin-left-str (get m
                                      (current-margin-left-str-key
                                       m 
                                       i
                                       gutter-label-line-zero?))]
     (str current-margin-left-str
          (bling [(:border-style m) 
                  (get m (if gutter-label-line-zero?
                           :border-left-str-zero
                           :border-left-str))])
          (:padding-left-str m)
          s))))

(defn- lns [m k]
  (let [s                           (some-> m k)
        label-lines?                (= k :label)
        body-lines?                 (= k :value)
        gutter-label-lines?         (boolean (and (= (:theme m) "gutter")
                                                  (= k :label)))
        gutter-label-lines-with-pt? (and gutter-label-lines?
                                         (not= 0 (:padding-top m)))
        padding-lines               #(char-repeat (% m) "\n ")
        callout-has-body?           (boolean (:value m))
        s                           (cond
                                      gutter-label-lines-with-pt? 
                                      (str (bling [:bold s])
                                           (if callout-has-body?
                                             (str (padding-lines :padding-top))
                                             "\n"))

                                      label-lines?
                                      (bling [:bold s])

                                      body-lines?
                                      (str s
                                           (padding-lines :padding-bottom))

                                      :else
                                      s)
        lns-coll                    (some-> s string/split-lines)
        ret                         (string/join
                                     "\n" 
                                     (map-indexed
                                      (partial ln
                                               (assoc m
                                                      :current-line-type k)) 
                                      lns-coll))]
    ret))

(defn body-lines-with-border
  [m]
  (let [body-lns (string/split-lines (:value m))]
    (string/join
      "\n"
      (concatv
        (repeat (:padding-top m) (ln m ""))
        (mapv (partial ln m) body-lns)
        (repeat (:padding-bottom m) (ln m ""))))))

(defn- sideline-callout
  [m]
  (let [bold?       (= (:theme m) "sideline-bold")
        label-line  (cond
                      ; label-theme is marquee
                      (and (:label m)
                           (-> m :label as-str string/blank? not)
                           (contains? #{"marquee"} (:label-theme m)))
                      (sideline-marquee-label m)

                      ; label-theme is just minimal
                      :else
                      (bling (:margin-left-str m)
                           [(:border-style m) (if bold? "┏" "┌")]
                           (when (:label m)
                             (char-repeat 0 
                                          (if bold? "━" "─")))
                           (bling [:bold (some->> (:label m) (str " "))])))
        bottom-line (bling [(:border-style m)
                            (str (:margin-left-str m)
                                 (if bold? "┗" "└"))])]
    (str
      label-line
      "\n"
      (body-lines-with-border m)
      "\n"
      bottom-line)))

(defn ansi-callout-str
  [{:keys [label-theme theme value] :as m}]
  (let [sideline-variant? (contains? #{"sideline" "sideline-bold"} theme)
        sideline-variant-with-body? (boolean (and value sideline-variant?))
        sideline-variant-just-label? (and (nil? value) sideline-variant?)
        gutter-theme? (contains? #{"rainbow-gutter" "gutter"} theme)]
    ;; (? (keyed [sideline-variant?
    ;;            sideline-variant-with-body?
    ;;            sideline-variant-just-label?
    ;;            gutter-theme?]))
    (str (:margin-top-str m)
         (if sideline-variant-with-body?
           (sideline-callout m)
           (cond
             sideline-variant-just-label?
             (lns m :label)

             gutter-theme?
             (str (when (:label m)
                    (if (= label-theme "marquee")
                      (gutter-marquee-label m)
                      (lns m :label)))
                  "\n"
                  (lns m :value))))
         (:margin-bottom-str m))))


(def rainbow-colors
  ["red" "orange" "yellow" "green" "black" "white" "blue" "purple" "magenta"])

(def rainbow-colors-system
  (reverse ["system-maroon"
            "system-yellow"
            "system-olive"
            "system-lime"
            "system-black"
            "system-white"
            "system-aqua"
            "system-purple"
            "system-fuchsia"]))

(defn callout*
  [{:keys [theme] :as m}]
  (let [char                 gutter-char
        style                {:color (:color m)}
        gutter?              (= "gutter" theme)
        rainbow?             (= "rainbow-gutter" theme)
        gutter-str           (if rainbow? 
                               (bling [{:color (last rainbow-colors)} char])
                               (bling [style char]))
        rainbow-gutter-str   (apply bling
                                  (for [s (drop-last rainbow-colors)]
                                    [{:color s} char]))
        rainbow-gutter-str-odd (apply bling
                                  (for [s (drop-last rainbow-colors-system)]
                                    [{:color s} char]))

        cr                   (fn [k ch] (char-repeat (or (k m) 0) ch))
        gutter-str-zero      (bling [style
                                     (string/join 
                                      (cr :margin-left
                                          gutter-char-lower-seven-eighths))])
        margin-left-str-zero gutter-str-zero
        border-left-str-zero gutter-char-lower-seven-eighths
        s                    (ansi-callout-str
                              (merge
                               m
                               {:border-style      style
                                :border-left-str   (case theme
                                                     "sideline"
                                                     "│"
                                                     "sideline-bold"
                                                     "┃"
                                                     "gutter"
                                                     gutter-str
                                                     "rainbow-gutter"
                                                     gutter-str
                                                     " ")
                                :padding-left-str  (cr :padding-left " ")
                                :margin-left-str   (if rainbow?
                                                     rainbow-gutter-str
                                                     (cr
                                                      :margin-left
                                                      (case theme
                                                        "gutter"
                                                        gutter-str
                                                        "rainbow-gutter"
                                                        rainbow-gutter-str
                                                        " ")))

                                :margin-top-str    (cr :margin-top "\n")
                                :margin-bottom-str (cr :margin-bottom "\n")}
                               
                               (when rainbow?
                                 {:margin-left-str-odd rainbow-gutter-str-odd})
                               (when gutter?
                                 (keyed [margin-left-str-zero
                                         border-left-str-zero]))))]
    (if (true? (:data? m))
      s
      (some-> s println))))

#?(:cljs
   (do 
     (defn enriched->js-arr [value label padding-top padding-bottom-str]
       (let [rich-value?  (instance? Enriched value)
             rich-label?  (instance? Enriched label)
             tagged       (if rich-value?
                            (goog.object/get value "tagged")
                            value)
             css          (if rich-value?
                            (goog.object/get value "css")
                            #js[])
             label-tagged (some-> (if rich-label?
                                    (goog.object/get label "tagged")
                                    label)
                                  (str "\n"))
             label-css    (if rich-label?
                            (goog.object/get label "css")
                            #js[])
             arr          (.concat #js[(str label-tagged
                                            padding-top
                                            tagged
                                            padding-bottom-str)]
                                   (.concat label-css css))]
         arr))

     (defn padding-block [n warning-or-error?]
       (if (pos-int? n) (char-repeat n "\n") (when warning-or-error? "\n")))))

#?(:cljs
   (defn browser-callout
         [{:keys [value
                  label
                  warning?
                  error?
                  padding-bottom
                  padding-top
                  data?]
           :as   m}]
         (let [f                  (cond warning? 
                                        (.-warn js/console)
                                        error?
                                        (.-error js/console)
                                        :else
                                        (.-log js/console))
               warning-or-error?  (or warning? error?)
               semantic-type?     (or warning-or-error?)
               padding-bottom-str (padding-block padding-bottom
                                                 warning-or-error?)
               padding-top-str    (padding-block padding-top 
                                                 semantic-type?)
               arr                (cond
                                    (or (instance? Enriched value) 
                                        (instance? Enriched label))
                                    ;; Either the label or body is enriched
                                    (enriched->js-arr value
                                                      label
                                                      padding-top-str
                                                      padding-bottom-str)
                                    
                                    ;; Nothing is enriched
                                    ;; The empty string in the css slot needs to
                                    ;; be there in order to properly add a
                                    ;; single newline.
                                    :else
                                    #js[(str (if (coll? label)
                                               (some-> label
                                                       (shortened 50)
                                                       (str "\n"))
                                               (some-> label 
                                                       (str "\n")))
                                             padding-top-str
                                             (str value)
                                             padding-bottom-str)
                                        ""])]
           (if (true? data?)
             arr
             (.apply f js/console arr)))))

(defn- default-opt [m k strs default]
  (or (some-> (k m)
              as-str
              (maybe strs))
      default))


(defn- resolve-label
  [{:keys [label label-str] :as m}
   type-as-str]
  (let [blank-string-supplied?              (and (string? label) (string/blank? label))
        nothing-supplied?                   (nil? label)
        supplied-label                      label
        supplied-label-str                  label-str
        supplied-coll-label-shortened       (some-> m
                                                    :label
                                                    (maybe coll?)
                                                    (shortened 33))
        default-label-based-on-callout-type (some-> type-as-str
                                                    string/upper-case)]
    ;; (? (keyed [blank-string-supplied? 
    ;;            supplied-label 
    ;;            supplied-label-str 
    ;;            supplied-coll-label-shortened
    ;;            default-label-based-on-callout-type]))

    ;; TODO confirm this
    (if blank-string-supplied?
      ;; Blank string is a force-nil situation (in event a :type is provided) 
      nil
      (or 
       ;; default to type if no label supplied, but :type provided
       (when nothing-supplied? default-label-based-on-callout-type)
       supplied-coll-label-shortened 
       label))))


(defn- spacing [n default]
  (if (and (int? n) (<= 0 n)) n default))


(defn- resolve-padding-left [m sideline-theme? label-theme f]
  (let [default (if (and sideline-theme?
                         (= label-theme "minimal"))
                  1
                  2)
        pl      (or (maybe (:padding-left m) pos-int?)
                    default)]
    pl))

(defn- resolve-padding-top 
  [theme f]
  #?(:cljs (f :padding-top 0)
     :clj  (if (contains? #{"gutter" "rainbow-gutter"} theme)
             (f :padding-top 0)
             (f :padding-top 0))))

(defn- callout-opts* [m]
  (let-map
   [theme          (default-opt m
                                :theme
                                #{"sideline"
                                  "sideline-bold"
                                  "gutter"
                                  "rainbow-gutter"}
                                "sideline")
    sideline-theme? (contains? #{"sideline" "sideline-bold"} theme)
    label-theme    (or (default-opt m
                                    :label-theme
                                    #{"marquee" "minimal"}
                                    nil)
                       "minimal")
    sp             (fn [k n] (spacing (get m k) n))
    padding-top    (resolve-padding-top theme sp)
    padding-bottom (sp :padding-bottom 0)
    margin-top     (sp :margin-top 1)
    margin-bottom  (sp :margin-bottom 0)
    margin-left    (sp :margin-left 0)
    padding-left   (resolve-padding-left m sideline-theme? label-theme sp)
    type           (some-> (:type m) as-str (maybe #{"warning" "error" "info"}))
    colorway       (or (get semantics-by-semantic-type type)
                       (some-> (:colorway m) as-str))
    semantic-type  (or (get semantics-by-semantic-type type)
                       (get semantics-by-semantic-type colorway))
    warning?       (= type "warning")
    error?         (= type "error")
    color          (or (get semantics-by-semantic-type colorway)
                       (maybe colorway all-color-names)
                       "neutral")
    user-label     (:label m)
    label*         (resolve-label m type)
    label          (if (string? label*)
                     (-> label*
                         (string/replace #"\n+( +)$" #(second %))
                         (string/replace #"^( +)\n+" #(second %)))
                     label*)
     ;; TODO maybe see if label is blinged and if not, bold it.
     ;label         (if label-is-blinged? label (bling [:bold label]))
    ]))

                                        
                                        
;;         CCCCCCCCCCCCC     OOOOOOOOO     
;;      CCC::::::::::::C   OO:::::::::OO   
;;    CC:::::::::::::::C OO:::::::::::::OO 
;;   C:::::CCCCCCCC::::CO:::::::OOO:::::::O
;;  C:::::C       CCCCCCO::::::O   O::::::O
;; C:::::C              O:::::O     O:::::O
;; C:::::C              O:::::O     O:::::O
;; C:::::C              O:::::O     O:::::O
;; C:::::C              O:::::O     O:::::O
;; C:::::C              O:::::O     O:::::O
;; C:::::C              O:::::O     O:::::O
;;  C:::::C       CCCCCCO::::::O   O::::::O
;;   C:::::CCCCCCCC::::CO:::::::OOO:::::::O
;;    CC:::::::::::::::C OO:::::::::::::OO 
;;      CCC::::::::::::C   OO:::::::::OO   
;;         CCCCCCCCCCCCC     OOOOOOOOO     
                                      

(defn ^:public callout
  "Prints a message to the console with a block-based coloring motif controlled
   by the `:type` option. Returns nil.
    
   If the `:data?` option is set to `true`, it does not print anything, and
   returns a data representation of the formatting and styling, which is will be
   different depending on whether the context is a terminal emulator or a
   browser dev console.
   
   In terminal emulator consoles, this will print a colored bounding border in
   the inline start position. The color of the border is determined by the value
   of the `:type` option, or the `:colorway` option. The characteristics of this
   border are controlled by the `:theme` option.
    
   In browser consoles, a border is not used, as the background and foreground
   text of the message block is automatically colored by the browser dev tools
   logging mechanism based on the type of logging function that is used. However,
   this automatic styling only applies to the callout `:type` options of `:error`
   and `:warn`.
        
   For callouts of the type `:error`, `:warning`, or `:info`, a label is
   printed in the block start postion. If a :type option is set, the label
   string will default to an uppercased version of that string, e.g.
   {:type :INFO} => \"INFO\". If a `:label` option is supplied, that value is
   used instead. When you want to omit label for callouts of the type `:error`,
   `:warning`, or `:info`, you must explicitly set the :label option to an empty
   string.
        
   The amount of vertical padding (in number of lines) within the bounds of the
   message body can be controlled the `padding-top` and `padding-bottom` options.
   The amount of space (in number of lines) above and below the message block can
   be controlled the `margin-top` and `margin-bottom` options. Margins are only
   applicable to callouts formatted for the terminal emulator.
   
   If two arguments are provided, the first should be a map with the follwoing
   optional keys:

| Key               | Pred                    | Description                                                  |
| :---------------  | ----------------------- | ------------------------------------------------------------ |
| `:type`           | `keyword?` or `string?` | Should be one of: `:error`,  `:warning` , or `:info`. <br>Will set the label text (unless provided via `:label`). Will also set the `:colorway`, and override any provided `:colorway` value. |
| `:colorway`       | `keyword?` or `string?` | The color of the sideline border, or gutter, depending on the value of `:theme`.<br />Should be one of: `:error`,  `:warning` , `:info` , `:positive`, or `:subtle`. <br>Can also be any one of the pallete colors such as  `:magenta`, `:green`,  `:negative`, `:neutral`, etc. |
| `:theme`          | `keyword?` or `string?` | Theme of callout. Can be one of `:sideline`, `:sideline-bold`, or `:gutter`. Defaults to `:sideline`. |
| `:label`          | `any?`                  | Labels the callout. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string). <br>In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively. |
| `:label-theme`    | `keyword?` or `string?` | Theme of label. Can be one of `:marquee` or `:minimal`. Defaults to `:minimal`. |
| `:padding-top`    | `int?`                  | Amount of padding (in newlines) at top, inside callout.<br/>Defaults to `0`. |
| `:padding-bottom` | `int?`                  | Amount of padding (in newlines) at bottom, inside callout.<br>Defaults to `0`. In browser console, defaults to `1` in the case of callouts of type `:warning` or `:error`.|
| `:padding-left`   | `int?`                  | Amount of padding (in blank character spaces) at left, inside callout.<br>In console emulator, defaults to `2`. In browser console, defaults to `0`.|
| `:margin-top`     | `int?`                  | Amount of margin (in newlines) at top, outside callout.<br>Defaults to `1`. Only applies to terminal emulator printing. |
| `:margin-bottom`  | `int?`                  | Amount of margin (in newlines) at bottom, outside callout.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-left`    | `int?`                  | Amount of margin (in blank character spaces) at left, outside callout.<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:data?`          | `boolean?`              | Returns a data representation of result instead of printing it. |
"

  ;; TODO colorway can take arbitrary hex?
  ([x]
   (cond
     ;; The case when user just passes a :label value, so just border and text
     (map? x)
     (callout x nil)

     ;; The case when user just passes a string, or an instance of an Enriched (bling) object (cljs)
     #?(:cljs
        (or (string? x) (instance? Enriched x))
        :clj
        (string? x))
     (callout {} x)

     ;; Internal warning from bling about bad args
     :else
     (callout
       {:type :warning}
       ;; TODO - this is messy formatiing for data-structures, fix
       (point-of-interest
         {:type   :warning
          :header "bling.core/callout"
          :form   (cons 'callout (list x))
          :body   (str "bling-core/callout, if called with a single argument,\n"
                       "expects either:\n"
                       "- a map of options\n"
                       "- a string\n\n"
                       "Nothing will be printed.")}))))
  ([opts value]
   (if-not (map? opts)
     ;; Internal warning from bling about bad args
     (callout
       {:type :warning}
       (point-of-interest
         {:type   :warning
          :header "bling.core/callout"
          :form   (cons 'callout (list opts value))
          :body   (str "bling-core/callout expects a map of options,\n"
                       "followed by any number of values (usually strings).\n\n"
                       "Nothing will be printed.")}))
     (let [value         (some-> value (maybe #(not (string/blank? %))))
           callout-opts  (callout-opts* opts)
           callout-opts+  (merge {:value value}
                                opts
                                callout-opts)]
       #?(:cljs
          (if node?                                                             ;; TODO <- move to enriched or data
            (callout* callout-opts+)
            (browser-callout callout-opts+))

          :clj
          (callout* callout-opts+))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Enriched text public fns and helpers  --------------------------------------

(defn- ^:private tagged-str
  "Expects an EnrichedText record.
   In Clojure, returns string wrapped with appropriate sgr codes for rich
   printing. In ClojureScript, returns a string wrapped in style escape
   chars (%c)."
  [o]
  (let [f #(str (->> %
                     :style
                     (reduce-colors-to-sgr-or-css :sgr)
                     m->sgr)
                (:value %)
                "\033[0;m")]

    #?(:cljs
       (if node? (f o) (str "%c" (:value o) "%c"))
       :clj
       #_(?sgr (f o))
       (f o))))

(defn- tag->map [acc s]
  (let [[k m] (case s
                "bold" [:font-weight "bold"]
                "italic" [:font-style "italic"]
                "underline" [:text-decoration "underline"]
                "strikethrough" [:text-decoration "line-through"]
                (let [cs (:all color-codes)
                      m  (get cs s nil)]
                  (if m
                    [:color m]
                    (when-let [nm (string/replace s #"-bg$" "")]
                      (when-let [m (get cs nm nil)]
                        [:background-color m])))))]
    (if k (assoc acc k m) acc)))

(defrecord EnrichedText [value style])


#?(:cljs
   (defn- href-browser-dev-console [style v]
     (let [href  (when (map? style) (:href style))
           v     (if href
                   (if (= href v) href (str v " " href))
                   v)
           style (if href (dissoc style :href) style)]
       [style v])))


(defn href-console [style v]
  (let [href  (when (map? style) (:href style))
        v     (if href (hyperlink v href) v)
        style (if href (dissoc style :href) style)]
    [style v]))


(defn- enriched-text
  "Returns an EnrichedText record. The `:value` entry is intended to be
   displayed as a text string in the console, while the `:style` entry is a map
   of styling to be applied to the printed text.

   Example:
   #my.ns/EnrichedText {:style {:font-weight \"bold\"
                                :color       {:sgr 39
                                              :css \"#00afff\"}
                        :value \"hi\"}"
  [[style v]]
  (let [[style v] #?(:cljs
                     (if node? 
                       (href-console style v)
                       (href-browser-dev-console style v))
                     :clj
                     (href-console style v))]
    (->EnrichedText
     (str v)
     (cond
       (map? style)
       (reduce-kv convert-color {} style)

       (or (keyword? style)
           (string? style))
       (-> style
           name
           (string/split (if (keyword? style)
                           #"\."
                           #" "))
           (->> (reduce tag->map {})))))))


(defn- updated-css [css-styles x]
  (if-let [style (some-> x
                         (maybe et-vec?)
                         enriched-text
                         :style)]

    (let [style* (select-keys style browser-dev-console-props)
          style  (reduce-colors-to-sgr-or-css :css style*)
          ks     (keys style)
          resets (reduce (fn [acc k]
                           (assoc acc k "initial"))
                         {}
                         ks)]

      ;; (prn {:style* style*
      ;;       :style  style
      ;;       :ks     ks
      ;;       :resets ks})

      (conj css-styles
            (css-stylemap->str style)
            (css-stylemap->str resets)))
    css-styles))


(defn- enriched-data-inner
  [[coll css] x]
  ;; (prn (merge {:coll    coll
  ;;                :css     css
  ;;                :x       x
  ;;                :et-vec? (et-vec? x)}
  ;;             (when (et-vec? x)
  ;;               {:enriched-text (tagged-str (enriched-text x))
  ;;                :updated-css   (updated-css css x)})))
  (let [s (cond (et-vec? x)
                (tagged-str (enriched-text x))
                (not (coll? x))
                (as-str x))]
    [(conj coll s)
     (updated-css css x)]))


(defn- bling-data* [args]
  (let [[coll css] (reduce enriched-data-inner
                           [[] []]
                           args)
        tagged (string/join coll)]
    {:console-array (into-array (concatv [tagged] css))
     :tagged        tagged
     :css           css
     :args          args}))


(defn ^:public bling-data [coll]
  #?(:cljs
     (if node?
       (-> coll bling-data* :tagged)
       (bling-data* coll))
     :clj
     (-> coll bling-data* :tagged)))


(defn ^:public bling-data-css [coll]
  (-> coll bling-data* :css))


(defn ^:public bling
  "In a terminal emulator context, returns a string tagged with SGR codes to
   style the text as desired.
   
   In a browser context, returns an object, which is an instance of
   `bling.core/Enriched`, a js object with the the following fields:

   `tagged`
   A string with the appropriate tags for styling in browser consoles
  
   `css`
   An array of styles that sync with the tagged string.

   `args`
   A ClojureScript vector of the original args.

   `consoleArray`
   An array by `Array.unshift`ing the tagged string onto the css array.
   This is the format that is needed for printing in a browser console, e.g.
   `(.apply js/console.log js/console (goog.object/get o \"consoleArray\"))`.

   For browser usage, sugar for the above `.apply` call is provided with
   `bling.core/print-bling`. You can use it like this:
   `(print-bling (bling [:bold.blue \"my blue text\"]))`"

  [& coll]

  (let [coll coll #_(hiccup->coll-of-strs-and-hiccup)
        f    #(:tagged (bling-data* coll))]
    #?(:cljs
       (if node?
         (f)
         (let [{:keys [css tagged console-array args]} (bling-data* coll)]
              ;;  (js/console.log "tagged:" tagged)
              ;;  (js/console.log "css:" css)
              ;;  (js/console.log "console-array:" console-array)
              ;;  (.apply js/console.log js/console js-arr)
              (Enriched. tagged
                         (into-array css)
                         console-array
                         args)))
       :clj (f))))

                     