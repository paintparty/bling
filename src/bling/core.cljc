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


;; Adds about 10kb to a cljs bundle
(ns bling.core
  (:require [clojure.string :as string]
            #?(:cljs [goog.object])))

;; Defs -----------------------------------------------------------------------

(def ^:private xterm-colors-by-id
  {39  "#00afff"
   40  "#00d700"
   178 "#d7af00"
   247 "#9e9e9e"
   231 "#ffffff"
   201 "#ff00ff"
   202 "#ff5f00"
   16  "#000000"})

(def ^:private browser-dev-console-props 
  [:text-decoration-line      
   :text-decoration-style     
   :text-decoration-color 
   :text-underline-offset     
   :text-decoration-thickness 
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

(def ^:private colors-source
  {"red"    {:sgr 202 :semantic "negative"} 
   "yellow" {:sgr 178 :semantic "warning"}
   "green"  {:sgr 40 :semantic "positive"}
   "blue"   {:sgr 39 :semantic "accent"}
   "magenta"{:sgr 201}
   "gray"   {:sgr 247 :semantic "subtle"}
   "black"  {:sgr 16}
   "white"  {:sgr 231}})

(select-keys xterm-colors-by-id
             (->> colors-source
                  vals
                  (map :sgr)
                  (into [])))

(def ^:private semantics-by-callout-type
  {"error"    "negative"
   "warning"  "warning"
   "positive" "positive"
   "info"     "accent"
   "accent"   "accent"
   "subtle"   "subtle"
   "neutral"  "neutral"})

(def ^:private all-color-names
  (into #{}
        (concat (keys semantics-by-callout-type)
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

(defn- squiggly-underline [s]
  (string/join (repeat (count s) "^")))


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

(defn- readable-sgr [x]
  #?(:cljs x
     :clj (str "\\033" (subs x 1))))

(defn- ns-info-str
  [{:keys [file line column]}]
  (str (some-> file (str ":")) line ":" column))

(defn- regex? [v]
  #?(:clj (-> v type str (= "class java.util.regex.Pattern"))
     :cljs (-> v type str (= "#object[RegExp]"))))

(defn- surround-with-quotes [x]
  (str "\"" x "\""))

(defn- shortened
  "Stringifies a collection and truncates the result with ellipsis 
   so that it fits on one line."
  [v limit]
  (let [as-str         (str v)
        regex?         (regex? v)
        double-quotes? (or (string? v) regex?)
        regex-pound    #?(:cljs nil :clj (when regex? "#"))]
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
              ret       (str n ";2;" r ";" g ";" b)]
          ret)))))

(defn- m->sgr
  [{fgc*        :color
    bgc*        :background-color
    :keys       [font-style 
                 font-weight
                 disable-italics?
                 disable-font-weights?]
    :as         m}]
  (let [fgc    (x->sgr fgc* :fg)
        bgc    (x->sgr bgc* :bg)
        italic (when (and (not disable-italics?)
                          (contains? #{"italic" :italic} font-style))
                 "3;")
        weight (when (and (not disable-font-weights?)
                          (contains? #{"bold" :bold} font-weight))
                 ";1")
        ret    (str "\033[" 
                    italic
                    fgc
                    weight
                    (when (or (and fgc bgc)
                              (and weight bgc))
                      ";")
                    bgc
                    "m")]
    ret))


;; Color-related fns  ---------------------------------------------------------
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
  (let [colors    (assoc-hex-colors colors-source )
        semantics (reduce-colors color-names-by-semantic* colors)
        callouts  (reduce-colors semantics-by-callout-type semantics)]
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
           (when (nameable? v)
             (get (:all color-codes)
                  (as-str v)
                  nil))
           v)))

(defn- et-vec? [x]
  (and (vector? x)
       (= 2 (count x))
       (-> x
           (nth 0)
           (maybe #(or (keyword? %)
                       (map? %))))))


;; Formatting helper fns  -----------------------------------------------------

(defn- callout-type [opts]
  (let [x (:type opts)]
    (cond 
      (keyword? x) (name x)
      (string? x)  x
      :else
      (some-> (when (map? x)
                (or (get x :type nil)
                    (get x "type" nil)))
              (maybe nameable?)
              name))))

(defn- spacing [n default]
  (if (and (int? n) (<= 0 n)) n default))


(declare callout)
(declare bling)
(declare print-bling)


(defn- maybe-wrap [x]
  (when (et-vec? x)
    #?(:cljs
       (js/console.warn
        "bling.core/point-of-interest\n\n"
        "Supplied value for :header option:\n\n"
        x
        "\n\n"
        "If you are trying to style this text, this value needs to be wrapped"
        "in a vector like this:\n\n"
        [x]
        "\n\n")
       :clj
       ()))
  (cond (coll? x) x :else [x]))


;; Formatting exceptions ----------------------------------------------------------

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
                              (map StackTraceElement->vec))

             ;; If regex is legit, get a list of indexes that match the regex
             ;; passed in by user. Regex will match on ns or filename where
             ;; user's their program lives. Then get the last index of a match
             ;; (within the mini-strace). If regex is not legit, use the depth.
             last-index  (if (= java.util.regex.Pattern (type regex))
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
             with-header [(or header "Stacktrace preview:") "\n"]
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
;; maybe useful if any weird behavior arises
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


;; Line and point of interest public fns  -------------------------------------
(defn- enriched-args [o]
  #?(:cljs
     (if (instance? Enriched o)
       (goog.object/get o "args")
       (when o [o]))
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
   
| Key             | Pred                   | Description                                                  |
| :--------       | -----------------      | ------------------------------------------------------------ |
| `:file`         | `string?`              | File or namespace                                            |
| `:line`         | `integer?`             | Line number                                                  |
| `:column`       | `integer?`             | Column number                                                |
| `:form`         | `any?`                 | The form to draw attention to. Will be cast to string and truncated at 33 chars |
| `:type`         | `keyword` or `string?` | Controls the color of the squiggly underline. Should be one of: `:error` `:warning`, or `:neutral`. Defaults to `:neutral` |
| `:header`       | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:body`         | `any?`                 | Typically, a string. If multi-line, string should be composed with newlines as desired. In a browser context, can be an instance of `bling.core/Enriched` (produced by using `bling.core/enriched`)|
| `:margin-block` | `int?`                 | Controls the number of blank lines above and below the diagram.<br/>Defaults to 1.|
"

  [{:keys [line
           file
           column
           form
           header
           body
           margin-block]
    squiggly-color :type
    :as opts}]
  (let [file-info    (ns-info-str opts) 
        gutter       (some-> line str count spaces)
        color        (or (some-> squiggly-color
                                 as-str
                                 (maybe all-color-names))
                         "neutral")
        form-as-str  (shortened form 33)
        squig        (squiggly-underline form-as-str)
        bolded-form  [{:font-weight :bold} form-as-str]
        bolded-squig [{:font-weight :bold
                       :color       color} squig]
        header       (enriched-args header)
        body         (enriched-args body)
        mb*          (or (some-> margin-block (maybe pos-int?))
                         (if (some-> margin-block zero?)
                           0
                           1))
        mb           (char-repeat mb* "\n")
        ret          (apply bling
                            (concat
                             header
                             (when header ["\n"])
                             (cond (and line column file form)
                                   [mb
                                    gutter " ┌─ " file-info "\n"
                                    gutter " │  \n"
                                    line   " │ " bolded-form "\n"
                                    gutter " │ " bolded-squig
                                    mb]
                                   
                                   form
                                   [mb
                                    bolded-form "\n"
                                    bolded-squig
                                    mb])
                             (when body ["\n"])
                             body))]
    ;; #?(:cljs (do (js/console.log header)
    ;;              ))
    ret))

(defn with-label-and-border 
  [{:keys [label
           border-weight
           border-style
           border-left-str
           pt
           padding-bottom
           padding-left-str
           margin-left-str
           margin-top-str
           value]
    :as m}
   label-line]
  (let [pt-and-value             
        (str (when label "\n")
             (char-repeat pt "\n")
             value)
        
        newlines?                         
        (re-find #"\n" pt-and-value)

        pt-and-value-with-border*
        (string/replace 
         (if (or (not label)
                 (not newlines?))
           (str "\n" pt-and-value)
           pt-and-value)
         #"\n"
         (str "\n"
              margin-left-str
              (bling [border-style
                         border-left-str]
                        padding-left-str)))

        ;; In the case of medium or heavy border,
        ;; with newlines and but no label,
        ;; we need to remove the leading space character.
        pt-and-value-with-border
        (if (or (and newlines?
                     (not label)
                     (not= border-weight "light"))
                (and #_(nil? margin-top-str)
                     (not label)
                     (not= border-weight "light")))
          (string/replace pt-and-value-with-border* #"^\n" "")
          pt-and-value-with-border*)

        pb
        (spacing padding-bottom 0)]

    #_(when (:data? m)
      (prn 'pt-and-value pt-and-value)
      (prn 'newlines? newlines?)
      (prn 'pt-and-value-with-border pt-and-value-with-border)
      (prn 'pb pb))

    ;; (prn 'pt pt-and-value)
    ;; (prn 'pt-and-value-with-border* pt-and-value-with-border*)
    ;; (prn 'pt-and-value-with-border pt-and-value-with-border)

    (str
     label-line
     pt-and-value-with-border
     (char-repeat pb 
                  (str "\n"
                       (bling [border-style
                                  border-left-str]))))))
(defn callout*
  [{:keys [label
           border-weight
           padding-left
           margin-top
           margin-bottom
           margin-left
           data?
           color]
    :as m}]
  (let [light-border?      (= border-weight "light")
        light-border-style {:font-weight :bold
                            :color       color}
        label-opts         light-border-style
        thick-border-style {:background-color (if (= "neutral" color)
                                                "gray"
                                                color)
                            :color            :white
                            :font-weight      :bold}
        border-style       (if light-border? 
                             light-border-style
                             thick-border-style)
        border-left-str    (case border-weight
                             "light"   "┃"
                             "medium"  " "
                             "heavy"   "  ")
        padding-left       (spacing padding-left
                                    (if light-border? 1 2))
        padding-left-str   (char-repeat padding-left " ")
        margin-left-str    (char-repeat margin-left " ")
        margin-top-str     (char-repeat margin-top "\n")
        opts*              (merge m
                                  {:label-opts       label-opts
                                   :border-style     border-style  
                                   :border-left-str  border-left-str
                                   :padding-left-str padding-left-str
                                   :margin-left-str  margin-left-str
                                   :margin-top-str   margin-top-str})
        callout-str        
        (str margin-top-str
             (if (contains? #{"heavy" "medium"} border-weight)

               ;; heavy style border
               (let [label-line                       
                     (when label
                       (bling 
                        margin-left-str
                        [border-style (case border-weight
                                        "medium"  " "
                                        "heavy"   "  ")]
                        padding-left-str
                        [label-opts (str label)]))
                     
                     w-label
                     (with-label-and-border opts* label-line)]
                 #_(prn w-label)
                 w-label)

               ;; light border
               (let [hrz-edge   (char-repeat (max 0 (dec padding-left))
                                             "━")
                     label-line (bling [light-border-style
                                           (str margin-left-str
                                                "┏"
                                                hrz-edge
                                                (some->> label (str " ")))])]
                 (str
                  (with-label-and-border opts* label-line)
                  (str "\n"
                       (bling [light-border-style
                                  (str margin-left-str
                                       "┗"
                                       hrz-edge)])))))
             (char-repeat margin-bottom "\n"))]
    (if (true? data?)
      (do
        ;; (prn 'callout-str callout-str)
        ;; (prn 'margin-top-str margin-top-str)
        callout-str)
      (println callout-str))))


#?(:cljs
(defn browser-callout
  [{:keys [value
           label
           callout-type
           pt
           padding-bottom
           padding-left
           data?]
    :as   m}]
  (let [padding-left 
        (spacing padding-left 0)

        padding-left-str
        (char-repeat padding-left " ")

        f
        (case callout-type 
          "warning" (.-warn  js/console)
          "error"   (.-error  js/console)
          (.-log  js/console))

        pb 
        (spacing padding-bottom
                 (if (contains? #{"warning" "error"} callout-type) 1 0))

        pb-str
        (when (pos-int? pb)
          (char-repeat pb "\n"))

        pt-str
        (when (pos-int? pt)
          (char-repeat pt "\n"))

        label
        (cond
          (instance? Enriched label)
          label
          (and label
               (contains? #{"info"
                            "positive"
                            "negative"
                            "subtle"}
                          (as-str callout-type)))
          (bling [{:color (as-str callout-type)} (str label)])
          :else
          label)
        arr
        (if (or (instance? Enriched value)
                (instance? Enriched label))
          
          ;; Something is enriched
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
                                               pt-str
                                               tagged
                                               pb-str
                                               )]
                                      (.concat label-css css))]
            arr)

            ;; Nothing is enriched
            (let [value (str value)
                  value (if (re-find #"\n" value)
                          (string/replace value
                                          #"\n"
                                          (str "\n" padding-left-str))
                          (str padding-left-str value))

                  ;; The empty string in the css slot needs to be there
                  ;; in order to properly add a single newline
                  arr #js[(str (some-> label (str "\n"))
                               pt-str
                               value
                               pb-str)
                          ""]]
              arr))]

            (if (true? data?)
              arr 
              (.apply f js/console arr)))))


(defn ^:public callout
  "Prints a message to the console with a block-based coloring motif controlled
   by the `:type` option. Returns nil.
    
   If the `:data?` option is set to `true`, it does not print anything, and
   returns a data representation of the formatting and styling, which is will be
   different depending on whether the context is a terminal emulator or a browser
   dev console.
   
   In terminal emulator consoles, this will print a colored bounding border in
   the inline start position. The color of the border is determined by the value
   of the `:type` option. The weight/intensity of this border is controlled by
   the `:border-weight` option.
    
   In browser consoles, a border is not used, as the background and foreground
   text of the message block is automatically colored by the browser dev tools
   logging mechanism based on the type of logging function that is used. However,
   this automatic styling only applies to the callout `:type` options of `:error`
   and `:warn`.
        
   For callouts of the type `:error`, `:warning`, or `:info`, a bolded label is
   printed, by default, in the same color as the callout styling, in the block
   start postion. If a :type option is set, the label string will default to an
   uppercased version of that string, e.g. {:type :INFO} => \"INFO\". If a
   `:label` option is supplied, that value is used instead. When you want to omit 
   label for callouts of the type `:error`, `:warning`, or `:info`, you must
   explicitly set the :label option to an empty string.
        
   The amount of vertical padding (in number of lines) within the bounds of the
   message body can be controlled the `padding-top` and `padding-bottom` options.
   The amount of space (in number of lines) above and below the message block can
   be controlled the `margin-top` and `margin-bottom` options. Margins are only
   applicable to callouts formatted for the terminal emulator.
   
   If two arguments are provided, the first should be a map with the follwoing
   optional keys:

| Key               | Pred                    | Description                                                  |
| :---------------  | -----------------       | ------------------------------------------------------------ |
| `:label`          | `any?`                  | Labels the callout. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string). <br>In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to \"WARNING\", \"ERROR\", or \"INFO\", respectively. |
| `:type`           | `keyword?` or `string?` | Controls the color of the border and label.<br />Should be one of: `:error`,  `:warning` , `:info` , `:positive`, or `:subtle`. <br>Can also be any one of the pallete colors such as  `:magenta`, `:green`,  `:negative`, `:neutral`, etc. |
| `:border-weight`  | `keyword?` or `string?` | Controls the weight of the border. Can be one of `:medium`, `:heavy`, or `:light`. Defaults to `:light`, which renders default border with standard unicode, single-line box-drawing character. |
| `:padding-top`    | `int?`                  | Amount of padding (in lines) at top of callout (inside callout block).<br/>Defaults to 0. |
| `:padding-bottom` | `int?`                  | Amount of padding (in lines) at bottom of callout (inside callout block).<br>Defaults to 0. In browser console, defaults to `1` in the case of callouts of type `:warning` or `:error`.|
| `:padding-left`   | `int?`                  | Amount of padding (in lines) at left of callout (inside callout block).<br>In console emulator, defaults to `1` when `:border-weight` is `:light`, and `2` when `:border-weight` is `:medium` or `:heavy`. In browser console, defaults to `0`.|
| `:margin-top`     | `int?`                  | Amount of margin (in lines) at top of callout (outside callout block).<br>Defaults to `1`. Only applies to terminal emulator printing. |
| `:margin-bottom`  | `int?`                  | Amount of margin (in lines) at bottom of callout (outside callout block).<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-left`    | `int?`                  | Amount of margin (in lines) at left of callout (outside callout block).<br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:data?`          | `boolean?`              | Returns a data representation of result instead of printing it. |
"

  ([value]
   (callout {} value))
  ([{:keys [label
            wrap?
            data?
            border-weight
            margin-top
            margin-bottom
            margin-left
            padding-top
            padding-bottom
            padding-left]
     :as   opts}
    value]
   (if-not (map? opts)
     (callout
      {:type :warning
       ;; :border-weight :heavy
       }
      (point-of-interest
       {:type   :warning
        :header "bling.core/callout"
        :form   (cons 'callout (list opts value))
        :body   (str "bling-core/callout expects a map of options,\n"
                     "followed by any number of values (usually strings).\n\n"
                     "Nothing will be printed.")}))
     (let [pt            (spacing padding-top 0)
           margin-top    (spacing margin-top 1)
           margin-bottom (spacing margin-bottom 0)
           margin-left   (spacing margin-left 0)
           callout-type  (callout-type opts)
           color         (or callout-type "neutral")
           border-weight (or (some-> border-weight
                                     (maybe #{:light
                                              "light"
                                              :heavy
                                              "heavy"
                                              :medium
                                              "medium"})
                                     name)
                             "light")
           wrap?         (true? wrap?)
           label         (if (and (string? label)
                                  (string/blank? label))
                           nil
                           (or label
                               (get alert-type->label
                                    callout-type
                                    nil)))
           callout-opts  {:value          value
                          :label          label
                          :callout-type   callout-type
                          :border-weight  border-weight
                          :pt             pt
                          :padding-bottom padding-bottom
                          :padding-left   padding-left
                          :margin-top     margin-top
                          :margin-bottom  margin-bottom
                          :margin-left    margin-left
                          :data?          data?
                          :color          color}]

      ;;  (pprint callout-opts)
       #?(:cljs
          ;; move to enriched or data
          (browser-callout callout-opts)
          

          :clj
          (callout* callout-opts))))))


;; Enriched text public fns and helpers  --------------------------------------

(defn- ^:private tagged-str
  "Expects an EnrichedText record.
   In Clojure, returns string wrapped with appropriate sgr codes for rich
   printing. In ClojureScript, returns a string wrapped in style escape
   chars (%c)."
  [o]
  #?(:cljs
     (str "%c" (:value o) "%c")
     :clj
     (do 
       (str (->> o
                 :style
                 (reduce-colors-to-sgr-or-css :sgr)
                 m->sgr)
            (:value o)
            "\033[0;m"))))

(defn- tag->map [acc s]
  (let [[k m] (case s
                "bold"   [:font-weight "bold"]
                "italic" [:font-style "italic"]
                (let [cs (:all color-codes)
                      m  (get cs s nil)]
                  (if m
                    [:color m]
                    (when-let [nm (string/replace s #"-bg$" "")] 
                      (when-let [m (get cs nm nil)]
                        [:background-color m])))))]
    (if k (assoc acc k m) acc)))

(defrecord EnrichedText [value style])

(defn- enriched-text
  "Returns an EnrichedText record. The `:value` entry is intended to be
   displayed as a text string in the console, while the `:style` entry is a map
   of styling to be applied to the printed text.

   Private, for lib internal use.
   
   Example:
   #my.ns/EnrichedText {:style {:font-weight \"bold\"
                                :color       {:sgr 39
                                              :css \"#00afff\"}
                        :value \"hi\"}"
  [[style v]]
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
         (->> (reduce tag->map {}))))))


(defn- updated-css [css-styles x]
  (if-let [style (some-> x
                         (maybe et-vec?)
                         enriched-text
                         :style)]

    (let [style  (select-keys style browser-dev-console-props) 
          style  (reduce-colors-to-sgr-or-css :css style)
          ks     (keys style)
          resets (reduce (fn [acc k]
                           (assoc acc k "initial"))
                         {}
                         ks)]
      (conj css-styles
            (css-stylemap->str style)
            (css-stylemap->str resets)))
    css-styles))


(defn- enriched-data-inner
  [[coll css] x] 
  (let [s (cond (et-vec? x)
                (tagged-str (enriched-text x))
                (not (coll? x))
                (as-str x))]
    ;; (? {:print-with prn} s)
    [(conj coll s)
     (updated-css css x)]))


(defn- bling-data* [args]
  (let [[coll css] (reduce enriched-data-inner
                           [[] []]
                           args)
        tagged     (string/join coll)]
    {:console-array (into-array (concat [tagged] css))
     :tagged        tagged
     :css           css
     :args          args}))


(defn ^:public bling-data [coll]
  #?(:cljs
     (bling-data* coll)
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

   `consoleArray`
   An array by `Array.unshift`ing the tagged string onto the css array.
   This is the format that is needed for printing in a browser console, e.g.
   `(.apply js/console.log js/console (goog.object/get o \"consoleArray\"))`.
   
   `args`
   A ClojureScript vector of the original args.

   For browser usage, sugar for the above `.apply` call is provided with
   `bling.core/print-bling`. You can use it like this:
   `(print-bling (bling [:bold.blue \"my blue text\"]))"

  [& coll]
  #?(:cljs
     (let [{:keys [css tagged console-array args]} (bling-data* coll)]
      ;;  (js/console.log "tagged:" tagged)
      ;;  (js/console.log "css:" css)
      ;;  (js/console.log "console-array:" console-array)
       #_(.apply js/console.log js/console js-arr)
       #_css
       (Enriched. tagged
                  (into-array css)
                  console-array
                  args))
     :clj (:tagged (bling-data* coll))))

