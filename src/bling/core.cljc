;; Adds about 12kb to a cljs bundle

(ns bling.core
  (:require [clojure.string :as string]
            [fireworks.core :refer [? !? ?> !?>]]
            [clojure.walk :as walk]
            [bling.ansi :as ansi :refer [adjusted-char-count]]
            [bling.browser]
            [bling.hifi]
            [bling.defs :as defs]
            [bling.macros :refer [let-map keyed]]
            [bling.util :as util :refer [maybe->]]
            #?(:cljs [bling.js-env :refer [node?]])
            ;; TODO - eliminate goog.object req
            #?(:cljs [goog.object])))


(declare xterm-colors-by-id)

;; TODO - Move to ansi ns
(def ^:private ESC "\u001B[")
(def ^:private OSC "\u001B]")
(def ^:private BEL "\u0007")
(def ^:private SEP ";")

(defn- hyperlink [text url]
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

;; TODO - Move to defs namespace
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
   :contrast
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

;; TODO - Move to colors namespace?
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

;; figure out light-gray and dark-gray
;; support "medium-orange to force medium"
;; support "dark-orange to force dark"
;; support "light-orange to force light"


;; TODO - Move to colors namespace?
(def ^:private bling-colors-dark
  (apply
   array-map
   ["red"     {:sgr 124 :css "#af0000"}

    "orange"  {:sgr 172 :css "#d78700"}

    "yellow"  {:sgr 136 :css "#af8700"}

    "olive"   {:sgr 100 :css "#878700"}

    "green"   {:sgr 28 :css "#008700"}

    "blue"    {:sgr 26 :css "#005fd7"}

    "purple"  {:sgr 129 :css "#af00ff"}

    "magenta" {:sgr 163 :css "#d700af"}

    "gray"    {:sgr 244 :css "#808080"}

    "black"   {:sgr 16}
    "white"   {:sgr 231}]))

;; TODO - Move to colors namespace?
(def ^:private bling-colors-light
  (apply
   array-map
   ["red"     {:sgr 203 :css "#ff5f5f"}

    "orange"  {:sgr 214 :css "#ffaf00"}

    "yellow"  {:sgr 220 :css "#ffd700"}

    "olive"   {:sgr 143 :css "#afaf5f"}

    "green"   {:sgr 82 :css "#5fff00"}

    "blue"    {:sgr 81 :css "#5fd7ff"}

    "purple"  {:sgr 147 :css "#afafff"}

    "magenta" {:sgr 213 :css "#ff87ff"}
    "gray"    {:sgr 249 :css "#b2b2b2"}
    "black"   {:sgr 16}
    "white"   {:sgr 231}]))

;; TODO Add the light and dark variants to x-term-colors-by-id
;; TODO - Move to colors namespace?
(def ^:public bling-colors*
  (apply
   array-map
   ["red"        {:sgr      196
                  :css      "#ff0000"
                  :semantic "negative"}
    "orange"     {:sgr      208
                  :css      "#ff8700"
                  :semantic "warning"}
    "yellow"     {:sgr 178 :css "#d7af00"}
    "olive"      {:sgr 106 :css "#87af00"}
    "green"      {:sgr      40
                  :css      "#00d700"
                  :semantic "positive"}
    "blue"       {:sgr      39
                  :css      "#00afff"
                  :semantic "accent"}
    "purple"     {:sgr 141 :css "#af87ff"}
    "magenta"    {:sgr 201 :css "#ff00ff"}
    "gray"       {:sgr      247
                  :css      "#9e9e9e"
                  :semantic "subtle"}
    "black"      {:sgr 16 :css "#000000"}
    "white"      {:sgr 231 :css "#ffffff"}]))


(def ^:private bling-colors
  (apply
   array-map
   (reduce-kv (fn [acc k v]
                (let [sgr-dark   (get-in bling-colors-dark [k :sgr])
                      sgr-light  (get-in bling-colors-light [k :sgr])
                      sgr-medium (:sgr v)
                      css-dark   (get-in bling-colors-dark [k :css])
                      css-light  (get-in bling-colors-light [k :css])
                      css-medium (:css v)]
                  (util/concatv (conj acc
                                      k
                                      (assoc v
                                             :sgr-dark
                                             sgr-dark
                                             :sgr-light
                                             sgr-light
                                             :css-dark
                                             css-dark
                                             :css-light
                                             css-light))
                                (when-not (contains? #{"black" "white"} k)
                                  [(str "medium-" k) {:sgr sgr-medium
                                                      :css css-medium}
                                   (str "dark-" k)   {:sgr sgr-dark
                                                      :css css-dark}
                                   (str "light-" k)  {:sgr sgr-light
                                                      :css css-light}]))))
              []
              bling-colors*)))


(def ^:private colors-source
  (merge bling-colors
         system-colors-source))


(def ^:private semantics-by-semantic-type
  {"error"    "negative"
   "warning"  "warning"
   "positive" "positive"
   "info"     "accent"
   "accent"   "accent"
   "subtle"   "subtle"
   "neutral"  "neutral"})


(def ^:private all-color-names
  ;; TODO - perf use reduce here?
  (into #{}
        (util/concatv (keys semantics-by-semantic-type)
                      (vals semantics-by-semantic-type)
                      (keys colors-source))))

(def ^:private color-names-by-semantic*
  (reduce-kv (fn [m color {:keys [semantic]}]
               (if semantic
                 (assoc m semantic color)
                 m))
             {}
             colors-source))


;; Helper functions -----------------------------------------------------------

(defn ^:public ?sgr
  "For debugging of ANSI SGR tagged output.

   Prints the value with escaped ANSI SGR codes so you can read them in terminal
   emulators (otherwise text would just get colored). Preserves coloring.

   Returns the value."
  [s]
  (println 
   (string/replace s
                   #"\u001b\[((?:[0-9]|;|(?:9|4(?::[1-5])?))*)m"
                   (str "\033[38;5;231;48;5;247m" ; <- white on gray, actual ansi sgr tag for fake ansi tag
                        "\\\\033[$1m"             ; <- fake ansi sgr tag text
                        "\033[0m"                 ; <- reset, actual ansi sgr for resetting fake ansi tag
                        "\033[$1m"                ; <- original ansi sgr, to preserve coloring
                        )))
  s)

(defn ^:public !?sgr
  "Temporarily silences debugging of sgr code printing.
   Returns the value."
  [s]
  s)

(defn- nameable? [x]
  (or (string? x) (keyword? x) (symbol? x)))

(defn- as-str [x]
  (str (if (or (keyword? x) (symbol? x)) (name x) x)))

(defn- char-repeat [n s]
  (when (pos-int? n)
    (string/join (repeat n (or s "")))))

(defn- spaces [n] (string/join (repeat n " ")))

(defn- ns-info-str
  [{:keys [file line column file-line-column]}]
  (if (not (string/blank? file-line-column))
    file-line-column
    (str (some-> file (str ":")) line ":" column)))



(defn- poi-text-underline-str [n str-index text-decoration-style]
  ;; "╱╲" <- pretty good look too
  (str (string/join (repeat str-index " "))
       (string/join (repeat n
                            (case (some-> text-decoration-style
                                          as-str)
                              "wavy" "^"
                              "dashed" "-"
                              "dotted" "•"
                              "underline" "─"
                              "double" "═"
                              "^")))))

(def ^:private form-limit 33)

(defn- poi-text-underline
  [{:keys [form form-as-str text-decoration-index text-decoration-style]}]
  (if (and text-decoration-index
          (or (pos? text-decoration-index)
              (zero? text-decoration-index))
          (coll? form)
          (< (count (as-str form)) form-limit))
    (let [form
          (vec form)

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
                            (? text-decoration-style))})
    {:text-underline-str (poi-text-underline-str
                          (count form-as-str)
                          0
                          text-decoration-style)}))

(defn- x->sgr [x k]
  (when x
    (let [n (if (= k :fg) 38 48)]
      (if (int? x)
        (str n ";5;" x)
        (let [[r g b _] x
              ret (str n ";2;" r ";" g ";" b)]
          ret)))))

(def ^:private underline-style-codes-by-style
  {"straight" 1
   "double"   2
   "wavy"     3
   "dotted"   4
   "dashed"   5})

(defn- sgr-text-decoration [m]
  (when-not (:disable-text-decoration? m)
    (cond
      (or (contains? #{"underline" :underline} (:text-decoration m))
          (contains? #{"underline" :underline} (:text-decoration-line m)))
      (if-let [n (some->> m
                          :text-decoration-style
                          as-str
                          (get underline-style-codes-by-style))]
        (str "4:" n)
        "4")
      (contains? #{"line-through" :strikethrough}
                 (:text-decoration m))
      "9")))

(defn- m->sgr
  [{fgc*  :color
    bgc*  :background-color
    :keys [font-style
           font-weight
           disable-italics?
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
        text-decoration (sgr-text-decoration m)
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


;; Move to colors namespace? or you already have them in vector by index?
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
   81  "#5fd7ff"                                            ;; light-blue
   26  "#005fd7"                                            ;; dark-blue

   40  "#00d700"                                            ;; green
   82  "#5fff00"                                            ;; light-green
   28  "#008700"                                            ;; dark-green

   106 "#87af00"                                            ;; olive
   143 "#afaf5f"                                            ;; light-olive
   100 "#878700"                                            ;; dark-olive

   141 "#af87ff"                                            ;; purple
   147 "#afafff"                                            ;; light-purple
   129 "#af00ff"                                            ;; dark-purple

   178 "#d7af00"                                            ;; yellow
   220 "#ffd700"                                            ;; light-yellow
   136 "#af8700"                                            ;; dark-yellow

   196 "#ff0000"                                            ;; red
   203 "#ff5f5f"                                            ;; light-red
   124 "#af0000"                                            ;; dark-red

   201 "#ff00ff"                                            ;; magenta
   213 "#ff87ff"                                            ;; light-magenta
   163 "#d700af"                                            ;; dark-magenta

   208 "#ff8700"                                            ;; orange
   214 "#ffaf00"                                            ;; light-orange
   172 "#d78700"                                            ;; dark-orange

   231 "#ffffff"                                            ;; white
   247 "#9e9e9e"                                            ;; gray
   })

(defn- assoc-hex-colors [m]
  (reduce-kv (fn [m color {:keys [sgr sgr-light sgr-dark css-light css-dark css]}]
               (let [hex
                     ;; TODO - try with colors/by-index to see if perf boost
                     (get xterm-colors-by-id sgr nil)]
                 (merge (assoc m
                               color
                               (merge {:sgr sgr
                                       :css hex}
                                      (when sgr-light {:sgr-light sgr-light})
                                      (when sgr-dark {:sgr-dark sgr-dark})
                                      (when css-light {:css-light css-light})
                                      (when css-dark {:css-dark css-dark}))))))
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

(declare callout)
(declare bling)
(declare print-bling)

(defn- reduce-colors-to-sgr
  "This is where the actual color value gets pulled out of the color map that is
   associated with each color (in bling.core/all-color-names).

   For terminal environments, a light or dark theme can be optionally specified
   via an environmental variable:
   
   `BLING_MOOD=light`
   `BLING_MOOD=dark`
   `BLING_MOOD=universal`.

   `UNIVERSAL` would be equivalent to the default (not setting it at all).
   
   `LIGHT` theme will use a darker version of the color, which would improve the
   contrast for users that develop with a light-themed terminal.

   `DARK` theme will use a lighter version of the color, which would improve the
   contrast for users that develop with a dark-themed terminal.
   
   If `LIGHT` OR `DARK` values are detected for the `BLING_MOOD` env var, the
   value of the `:sgr` will be changed inside this function, from
   `:sgr` to `:sgr-light` or `:sgr-dark`"
  [{:keys [contrast] :as m}]
  (reduce-kv (fn [m k v]
               (assoc m k
                      (if (and (map? v)
                               (contains? #{:color :background-color} k))
                        (if (= :color k)
                          (or (let [kw
                                    (case contrast
                                      :low
                                      (case defs/bling-mood
                                        "light" :sgr-light
                                        "dark" :sgr-dark
                                        :sgr)

                                      :medium
                                      :sgr

                                      ;; covers :high
                                      (case defs/bling-mood
                                        "light" :sgr-dark
                                        "dark" :sgr-light
                                        :sgr))]
                                (kw v))
                              (:sgr v))

                          ;; Branch for background color
                          ;; TODO - consider supporting something like
                          ;; [{:red.yellow-bg-soft} "foo"]
                          ;; or 
                          ;; [{:red.yellow-bg-hard} "foo"]

                          ;; Would involve selecting another set of soft colors
                          ;; for light and dark

                          ;; Which one would be the default for
                          ;; [{:red.yellow-bg} "foo"]

                          ;; maybe the hard one, cos that would be the same for
                          ;; light and dark, and user may not have a light or
                          ;; dark mood set.

                          ;; and then maybe you don't need "-hard", just "soft"
                          (:sgr v))
                        v)))
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
                    (maybe-> #(or (keyword? %)
                                (map? %)))))))


;; Formatting helper fns  ------------------------------------------------------

(defn- semantic-type [opts]
  (let [x (:colorway opts)]
    (cond
      (keyword? x) (name x)
      (string? x) x
      :else
      (some-> (when (map? x)
                (or (get x :colorway nil)
                    (get x "colorway" nil)))
              (maybe-> nameable?)
              name))))



;; Race-condition-free version of clojure.core/println,
;; Maybe useful to keep around if any weird behavior arises.
#?(:clj
   (defn- safe-println [& more]
     (.write *out* (str (clojure.string/join " " more) "\n"))))



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

(def ^{:no-doc true} point-of-interest-options-schema
  [:map
   [:form
    {:required true
     :desc     ["The form to draw attention to. Will be cast to string and"
                "truncated at 33 chars"]}
    :any]
   
   [:file
    {:optional true
     :desc     ["File or namespace"]}
    :string]
   
   [:line
    {:optional true
     :desc     ["Line number"]}
    :int]
   
   [:column
    {:optional true
     :desc     ["Column number"]}
    :int]
   
   [:margin-block
    {:optional true
     :default  1
     :desc     ["Controls the number of blank lines above and below the diagram."]}
    :int]
   
   [:type
    {:optional true
     :desc     ["Automatically sets the `:text-decoration-color`."]}
    [:enum :error "error" :warning "warning"]]
   
   [:text-decoration-color
    {:optional true
     :default  :neutral
     :desc     ["Controls the color of the underline."]}
    [:enum :error "error" :warning "warning" :neutral "neutral" :magenta "magenta" :green "green" :negative "negative"]]
   
   [:text-decoration-style
    {:optional true
     :desc     ["Controls the color of the underline."]}
    [:enum :wavy "wavy" :solid "solid" :dashed "dashed" :dotted "dotted" :double "double"]]
   
   [:text-decoration-index
    {:optional true
     :desc     ["If the value of `:form` is a collection, this is the index of"
                "the item to apply text-decoration (underline)."]}
    :pos-int]])


(defn ^:public point-of-interest
  "Example:

   ```Clojure
   (point-of-interest {:form                  '(+ 1 true) ; <- required
                       :line                  42
                       :column                11
                       :file                  \"myfile.core\"
                       :text-decoration-style :wavy  ; :underline :solid :dashed :dotted :double
                       :type                  :error ; :warning 
                       ;; :margin-block          0       ; <- default is one
                       ;; :text-decoration-index 2       ; <- If form is collection, this will focus underline
                       ;; :text-decoration-color :yellow ; <- and bling palette color
                       })
   ```
   
   `point-of-interest` creates namespace info diagram which identifies a
   specific form. This provides the namespace, column, and line number, and a
   bolded, potentially truncated, representation of the specific form of
   interest. This form representation is accented with a squiggly underline.
   
   The `:line`, `:column`, `:form`, and `:file` options must all be present in
   order for the namespece info diagram to be rendered. If the `:form` option is
   supplied, but any of the others are omitted, only the form will be rendered
   (with an underline and no line-info diagram).
   
   By default, the diagram is created with a leading and trailing newlines.
   This can be set to zero, or increased, with the `:margin-block` option.
   
   All the options:

   ```Clojure
   [:form
    {:required true
     :desc     [\"The form to draw attention to. Will be cast to string and\"
                \"truncated at 33 chars\"]}
    :any]

   [:file
    {:optional true
     :desc     [\"File or namespace\"]}
    :string]

   [:line
    {:optional true
     :desc     [\"Line number\"]}
    :int]

   [:column
    {:optional true
     :desc     [\"Column number\"]}
    :int]

   [:margin-block
    {:optional true
     :default  1
     :desc     [\"Controls the number of blank lines above and below the diagram.\"]}
    :int]

   [:type
    {:optional true
     :desc     [\"Automatically sets the `:text-decoration-color`.\"]}
    [:enum
     :error
     \"error\"
     :warning
     \"warning\"]]

   [:text-decoration-color
    {:optional true
     :default  :neutral
     :desc     [\"Controls the color of the underline.\"]}
    [:enum
     :error
     \"error\"
     :warning
     \"warning\"
     :neutral
     \"neutral\"
     :magenta
     \"magenta\"
     :green
     \"green\"
     :negative
     \"negative\"]]

   [:text-decoration-style
    {:optional true
     :desc     [\"Controls the color of the underline.\"]}
    [:enum
     :wavy
     \"wavy\"
     :solid
     \"solid\"
     :dashed
     \"dashed\"
     :dotted
     \"dotted\"
     :double
     \"double\"]]

   [:text-decoration-index
    {:optional true
     :desc     [\"If the value of `:form` is a collection, this is the index of\"
                \"the item to apply text-decoration (underline).\"]}
    :pos-int]
   ```"

  ;; TODO - change text-decoration-color to underline-color?
  ;; TODO - Add option for doing underline in sgr?
  [{:keys [line
           file
           column
           form
           header ; <- deprecated / undocumented
           body   ; <- deprecated / undocumented
           margin-block
           text-decoration-color
           type]
    :as   opts}]
  (let [type             (some-> type as-str (maybe-> #{"warning" "error"}))
        file-info        (ns-info-str opts)
        gutter           (some-> line str count spaces)
        underline-color  (or (some->> type (get semantics-by-semantic-type))
                             (some-> text-decoration-color
                                     as-str
                                     (maybe-> all-color-names))
                             "neutral")
        form-as-str      (util/shortened form 33)
        underline-str    (-> opts
                             (assoc :form-as-str form-as-str)
                             poi-text-underline
                             :text-underline-str)
        bolded-form      [{:font-weight :bold} form-as-str]
        underline-styled [{:font-weight :bold
                           :color       underline-color
                           :contrast    :medium}
                          underline-str]
        header           (str header)
        body             (str body)
        mb*              (or (some-> margin-block (maybe-> pos-int?))
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
                            line   (diagram-char " │ ") bolded-form "\n"
                            gutter (diagram-char " │ ") underline-styled
                            mb
                            "\n"]

                           form
                           [mb
                            bolded-form "\n"
                            underline-styled
                            mb])
        ret              (apply bling
                                (util/concatv header
                                              diagram
                                              body))]
    ret))


;; Enriched text public fns and helpers  --------------------------------------

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
  (let [margin-left-str     (char-repeat margin-left defs/gutter-char)
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
      (util/concatv
       [(bling [bs
                (str
                 (char-repeat (inc margin-left) "▄")
                 " "
                 " ┏━━"
                 (char-repeat label-length "━")
                 "━━┓")])
        (str (bling [bs
                     (str margin-left-str
                          defs/gutter-char
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
                     defs/gutter-char
                     (hrz " ")
                     "┗━━"
                     (char-repeat label-length "━")
                     "━━┛")])]
       (mapv (fn [_]
               (bling [bs
                       (str margin-left-str
                            defs/gutter-char
                            (hrz " "))]))
             (range padding-top)))))))


(defn- sideline-marquee-label
  [{:keys [padding-left
           padding-left-str
           margin-left
           label
           label-string
           theme
           border-style
           side-label]}]
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
      (util/concatv
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
             (bling [bs (if b? "  ┃" "  │")])
             (when side-label (bling "  " [:italic side-label])))]
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


(defn- restore-ansi-sgr-over-lines [s]
  (let [lns (string/split s #"\n")
        lns (if-let [[_ ansi-sgr-tag]
                     (re-find ansi/sgr-wrapped-re s)]
              (mapv #(str ansi-sgr-tag % "\033[m") lns)
              lns)]
    lns))

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
        lns-coll                    (restore-ansi-sgr-over-lines s)
        ret                         (string/join
                                     "\n"
                                     (map-indexed
                                      (partial ln
                                               (assoc m
                                                      :current-line-type k))
                                      lns-coll))]
    ret))


(defn- body-lines-with-border
  [m]
  (let [body-lns (restore-ansi-sgr-over-lines (:value m))]
    (string/join
     "\n"
     (util/concatv
      (repeat (:padding-top m) (ln m ""))
      (mapv (partial ln m) body-lns)
      (repeat (:padding-bottom m) (ln m ""))))))

(defn- body-lines-no-border
  [{:keys [margin-left-str padding-left-str] :as m}]
  (let [body-lns (string/split-lines (:value m))]
    (string/join
     "\n"
     (util/concatv
      (repeat (:padding-top m) (str margin-left-str padding-left-str))
      (mapv #(str margin-left-str padding-left-str %) body-lns)
      (repeat (:padding-bottom m) (str margin-left-str padding-left-str))))))

(defn- minimal-callout
  [{:keys [label side-label border-style border-block-length value]
    :as   m}]
  
  (let [no-label?
        (or (nil? label) (string/blank? label))

        label-line
        (if no-label?
          (bling [border-style (string/join (repeat border-block-length "═"))])
          (bling [border-style "══"]
                 [:bold (some->> label (str " "))]
                 (when label " ")
                 [border-style "════"]
                 (some->> side-label (str " "))))

        label-line-length
        (count label-line)

        matches
        (re-seq #"\u001b\[([0-9;]*)[mK]" label-line)

        n
        (reduce (fn [n [_ s]] (+ n (count s))) 0 matches)

        n
        (- label-line-length n (* 3 (count matches)))

        body-lines
        (body-lines-no-border m)]
    (str label-line
         "\n"
         body-lines
         (when-not (string/blank? body-lines) "\n")
         (when-not (string/blank? body-lines)
           (bling [border-style (string/join (repeat n "─"))])))))

(defn- sideline-callout
  [{:keys [theme label side-label label-theme border-style padding-left margin-left-str]
    :as m}]
  (let [bold?
        (= theme "sideline-bold")

        marquee-label?
        (and label
             (-> m :label as-str string/blank? not)
             (contains? #{"marquee"} label-theme))

        horizontal-extension
        (when (and (not marquee-label?) label)
          [border-style (char-repeat (max (dec padding-left) 0)
                                     (if bold? "━" "─"))])

        label-line
        (cond
          ; label-theme is :marquee
          marquee-label?
          (sideline-marquee-label m)



          ; label-theme is :minimal
          :else
          (bling margin-left-str
                 [border-style (if bold? "┏" "┌")]
                 horizontal-extension
                 (bling [:bold (some->> label (str " "))]
                        (when side-label " ")
                        (when side-label [border-style (if bold? "━━━" "───")])
                        (some->> side-label (str " ")))))

        bottom-line
        (bling [border-style
                (str margin-left-str
                     (if bold? "┗" "└"))])]
    (str
     label-line
     "\n"
     (body-lines-with-border m)
     "\n"
     bottom-line)))




(defn- ansi-callout-str
  [{:keys [label-theme theme value] :as m}]
  (let [sideline-variant?            (contains? #{"sideline" "sideline-bold"} theme)
        minimal-variant?             (= "minimal" theme)
        sideline-variant-with-body?  (boolean (and value sideline-variant?))
        sideline-variant-just-label? (and (nil? value) sideline-variant?)
        gutter-theme?                (contains? #{"rainbow-gutter" "gutter"} theme)
        m                            (assoc-in m [:border-style :contrast] :medium)]
    ;; (? (keyed [sideline-variant?
    ;;            minimal-variant?
    ;;            sideline-variant-with-body?
    ;;            sideline-variant-just-label?
    ;;            gutter-theme?]))
    (str (:margin-top-str m)
         (if sideline-variant-with-body?
           (sideline-callout m)
           (cond
             minimal-variant?
             (minimal-callout m)

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


;; -----------------------------------------------------------------------------
;; Boxed callout start 
;; -----------------------------------------------------------------------------

(def ^:private bdc
  {:h  {:double     "═"
        :bold       "━"
        :thin       "─"
        :thin-round "─"}

   :v  {:double     "║"
        :bold       "┃"
        :thin       "│"
        :thin-round "│"}

   :tl {:double     "╔"
        :bold       "┏"
        :thin       "┌"
        :thin-round "╭"}

   :tr {:double     "╗"
        :bold       "┓"
        :thin       "┐"
        :thin-round "╮"}

   :bl {:double     "╚"
        :bold       "┗"
        :thin       "└"
        :thin-round "╰"}

   :br {:double     "╝"
        :bold       "┛"
        :thin       "┘"
        :thin-round "╯"}})

(def ^:private box-drawing-styles (into #{} (-> bdc :h keys)))

(defn- wrapped-string-inner
  [max-cols
   {:keys [result col]
    :as   acc}
   word]
  (if (= word "")
    acc
    (let [nl?              (= word "\n")
          space+word       (str (when-not (or nl? (zero? col)) " ") word)
          space+word-width (adjusted-char-count space+word)
          exceeds?         (< max-cols (+ col space+word-width))
          string-to-concat (if exceeds?
                             (str "\n" word)
                             (if nl? word space+word))
          new-col          (if exceeds?
                             (count word)
                             (if nl? 0 (+ col space+word-width)))]

      ;; #_(when exceeds? (prn "exceeds-"))
      ;; #_(? :-
      ;;      {:print-with prn
      ;;       :when       ansi-sgr-seq}
      ;;      {:col              col
      ;;       :word             word
      ;;       :space+word       space+word
      ;;       :space+word-width space+word-width
      ;;       :ansi-sgr-seq     ansi-sgr-seq
      ;;       :ansi-sgr-count   ansi-sgr-count
      ;;       :string-to-concat string-to-concat
      ;;       :new-col          new-col})

      {:result (str result string-to-concat)
       :col    new-col})))


(defn- wrap-single-word [max-cols s]
  (->> s
       seq
       (partition-all max-cols)
       (mapv string/join)))


(defn- stub-leading-spaces [s]
  (string/replace s #"^ +" #(util/sjr (count %) "〠")))


(defn- with-wrapped-single-words
  [max-width acc s]
  (into acc
        (if (< max-width (adjusted-char-count s))
          (wrap-single-word max-width s)
          [s])))


(defn- wrapped-string
  "Given a string with line-breaks, returns a string, with additional line
   breaks inserted, such that the width of every line of text is below the
   `max-cols` threshold."
  [s {:keys [max-width]}]
  (->> 
   (string/split s #"\n")
   (mapv #(string/split (stub-leading-spaces %) #" "))
   (mapv #(reduce (partial with-wrapped-single-words max-width) [] %))
   (mapv #(reduce (partial wrapped-string-inner max-width)
                  {:result "" :col 0}
                  %))
   (mapv #(-> % :result (string/replace #"〠" " ")))
   (string/join "\n")))


(defn- colored-border [s colorway]
  (if colorway
    (bling [{:color colorway} s])
    s))


(defn- label-profile
  [{:keys [pd horizontal-border-char colorway label]}]
  (let [colorize           #(if-not (re-find ansi/sgr-re % )
                              (bling [{:color colorway} %])
                              %)
        label-str          (or (some-> label
                                       (maybe-> string?)
                                       (maybe-> #(not (string/blank? %)))
                                       (str " ")
                                       (->> (str " "))
                                       colorize)
                               "")
        label-char-count   (adjusted-char-count label-str)
        label-pd-str       (when (and (pos? label-char-count) pd)
                             (-> pd
                                 dec
                                 (util/sjr horizontal-border-char)
                                 (colored-border colorway)))
        label-pd-str-count (or (some-> label-pd-str
                                       adjusted-char-count)
                               0)]
    (keyed [label-str
            label-char-count
            label-pd-str
            label-pd-str-count])))


(defn- block-border*
  [{:keys [pd-left
           pd-right
           horizontal-border-char
           colorway
           cols
           corner-border-char
           lc                    
           rc                    
           label                 
           side-label-for-border
           shadow-level]
    :as   m}]
  (let [side-label
        side-label-for-border

        {:keys [label-str
                label-char-count
                label-pd-str
                label-pd-str-count]}
        (label-profile (assoc m :label label :pd pd-left))

        {side-label-str          :label-str
         side-label-char-count   :label-char-count
         side-label-pd-str       :label-pd-str
         side-label-pd-str-count :label-pd-str-count}
        (label-profile (assoc m :label side-label :pd pd-right))

        middle-border
        (colored-border
         (str (util/sjr (- cols
                           (if (some-> shadow-level zero?)
                             3
                             (- 2 (or shadow-level 0)))
                           label-pd-str-count
                           label-char-count
                           side-label-char-count
                           side-label-pd-str-count)
                        horizontal-border-char)
              (when (some-> shadow-level zero?) "┬"))
         colorway)]
    ;; Assemble the border pieces
    (str (colored-border (corner-border-char lc) colorway)
         label-pd-str
         label-str
         middle-border
         side-label-str
         side-label-pd-str
         (colored-border (corner-border-char rc) colorway))))

(defn- resolve-terminal-width
  [min-width max-width]
  #?(:cljs
     ;; TODO - make this dynamic for node
     60
     :clj
     (let [min-width (or (maybe-> min-width pos-int?)
                         17)
           w         (some-> (util/get-terminal-width)
                             (maybe-> pos-int?))
           w         (cond (< w min-width)
                           min-width
                           (some-> max-width
                                   (maybe-> pos-int?) 
                                   (< w))
                           max-width
                           :else w)]
       (or w 80))))

(defn- border-chars
  [{:keys [border-char
           vertical-border-char
           horizontal-border-char
           box-drawing-style
           colorway]
    :as   m}]
  (let [box-drawing-style          
        (if box-drawing-style
          (or (maybe-> box-drawing-style box-drawing-styles)
              :thin-round)
          (when-not (or (string? border-char)
                        (and (string? vertical-border-char)
                             (string? horizontal-border-char)))
            :thin-round))

        vertical-border-char       
        (-> (or (some->> box-drawing-style (-> bdc :v))
                vertical-border-char
                border-char
                bdc)
            (colored-border colorway))

        vertical-border-char-count 
        (adjusted-char-count vertical-border-char)

        horizontal-border-char     
        (or (some->> box-drawing-style (-> bdc :h))
            (let [c (as-str
                     (or horizontal-border-char
                         border-char
                         bdc))]
              (if (< 1 (count c)) (subs c 0 1) c)))]
    (keyed [box-drawing-style          
            vertical-border-char       
            vertical-border-char-count 
            horizontal-border-char])))


(defn- boxed-callout
  "Creates a callout with a border on all sides"
  ([s] (boxed-callout s nil))
  ([s 
    {:keys     [width
                max-width
                min-width
                colorway
                label
                side-label]
     pd-top    :padding-top
     pd-bottom :padding-bottom
     pd-left   :padding-left
     pd-right  :padding-right
     pd-block  :padding-block
     pd-inline :padding-inline
     :as       m}]
   (let [{:keys [box-drawing-style          
                 vertical-border-char       
                 vertical-border-char-count 
                 horizontal-border-char]} 
         (border-chars m)
         pd-block                   (or (maybe-> pd-block pos-int?) 1)
         pd-inline                  (or (maybe-> pd-inline pos-int?) 2)
         pd-top                     (or (maybe-> pd-top pos-int?) pd-block)
         pd-bottom                  (or (maybe-> pd-bottom pos-int?) pd-block)
         pd-hrz                     #(min (or (maybe-> % pos-int?) pd-inline) 10)
         pd-right                   (pd-hrz pd-right)
         pd-left                    (pd-hrz (? pd-left))
         terminal-width             (resolve-terminal-width min-width max-width)
         cols                       (or width terminal-width)
         max-inner-cols             (- cols
                                       pd-left
                                       pd-right
                                       (* 2 vertical-border-char-count))
         pd-left-str                (util/sjr pd-left " ")
         start                      (str vertical-border-char pd-left-str)
         start-count                (adjusted-char-count start)
         pd-ln                      (str vertical-border-char
                                         (util/sjr (- cols
                                                      (* 2 vertical-border-char-count))
                                                   " ")
                                         vertical-border-char
                                         "\n")
         pd-top-ln                  (util/sjr pd-top pd-ln)
         pd-bottom-ln               (util/sjr pd-bottom pd-ln)
         corner-border-char         #(or (some->> box-drawing-style (-> bdc %))
                                         horizontal-border-char)
         label                      (as-str label)
         truncated-label            (some-> label
                                            (maybe-> #(< (- cols 4) (count %)))
                                            (subs 0 (- cols (+ 4 3)))
                                            (str "..."))
         label                      (or truncated-label label)
         side-label                 (as-str side-label)
         truncated-side-label       (when-not label
                                      (some-> side-label
                                              (maybe-> #(< (- cols 2) (count %)))
                                              (subs 0 (- cols (+ 2 3)))
                                              (str "...")))
         side-label-for-border      (when-not truncated-label
                                      (when side-label
                                        (if label
                                          (when (< 4 (- cols (+ (adjusted-char-count label)
                                                                (adjusted-char-count side-label)
                                                                4)))
                                            side-label)
                                          truncated-side-label)))
         block-border-opts          (keyed [pd-left
                                            pd-right
                                            horizontal-border-char
                                            colorway
                                            cols
                                            corner-border-char])
         top-border                 (colored-border
                                     (block-border* 
                                      (merge block-border-opts
                                             {:lc                    :tl
                                              :rc                    :tr
                                              :label                 label
                                              :side-label-for-border side-label-for-border}))
                                     colorway)
         bottom-border              (colored-border 
                                     (block-border* 
                                      (merge block-border-opts
                                             {:lc :bl
                                              :rc :br}))
                                     colorway)

         body                       (str (when (and side-label 
                                                    (not side-label-for-border))
                                           (str side-label "\n\n"))
                                         s)
         wrapped                    (wrapped-string body
                                                    {:max-width max-inner-cols})

         lns                        (restore-ansi-sgr-over-lines wrapped)

         ret 
         (str top-border
              "\n"
              pd-top-ln
              (string/join
               "\n"
               (mapv (fn [ln]
                       (str start
                            ln
                            (util/sjr (- cols
                                         (+ start-count 
                                            (adjusted-char-count ln))
                                         vertical-border-char-count)
                                      " ")
                            vertical-border-char))
                     lns))
              "\n"
              pd-bottom-ln
              bottom-border)]
     ret)))

;; -----------------------------------------------------------------------------
;; Boxed callout end 
;; -----------------------------------------------------------------------------

(def ^:private rainbow-colors
  ["red" "orange" "yellow" "green" "black" "white" "blue" "purple" "magenta"])

(def ^:private rainbow-colors-system
  (reverse ["system-maroon"
            "system-yellow"
            "system-olive"
            "system-lime"
            "system-black"
            "system-white"
            "system-aqua"
            "system-purple"
            "system-fuchsia"]))

(defn- border-left-str [theme gutter-str]
  (case theme
    "sideline"
    "│"
    "sideline-bold"
    "┃"
    "gutter"
    gutter-str
    "rainbow-gutter"
    gutter-str
    " "))

(defn- callout*
  [{:keys [theme]
    :as   m}]
  (if (= "boxed" theme)
    (let [s (boxed-callout (:value m) m)]
      (if (true? (:data? m))
        s
        (some-> s println)))
    (let [char
          defs/gutter-char

          border-style
          {:color    (:color m)
           :contrast :medium}

          gutter?
          (= "gutter" theme)

          rainbow?
          (= "rainbow-gutter" theme)

          gutter-str
          (if rainbow?
            (bling [{:color (last rainbow-colors)} char])
            (bling [border-style char]))

          rainbow-gutter-str
          (apply bling
                 (for [s (drop-last rainbow-colors)]
                   [{:color s} char]))

          rainbow-gutter-str-odd
          (apply bling
                 (for [s (drop-last rainbow-colors-system)]
                   [{:color s} char]))

          cr
          (fn [k ch] (char-repeat (or (k m) 0) ch))

          gutter-str-zero
          (bling [border-style
                  (string/join
                   (cr :margin-left
                       defs/gutter-char-lower-seven-eighths))])

          margin-left-str-zero
          gutter-str-zero

          border-left-str-zero
          defs/gutter-char-lower-seven-eighths

          margin-left-str
          (if rainbow?
            rainbow-gutter-str
            (cr
             :margin-left
             (case theme
               "gutter"         gutter-str
               "rainbow-gutter" rainbow-gutter-str
               " ")))

          s
          (ansi-callout-str
           (merge
            m
            {:border-style      border-style
             :border-left-str   (border-left-str theme gutter-str)
             :padding-left-str  (cr :padding-left " ")
             :margin-left-str   margin-left-str
             :margin-top-str    (cr :margin-top "\n")
             :margin-bottom-str (cr :margin-bottom "\n")}

            (when rainbow?
              {:margin-left-str-odd rainbow-gutter-str-odd})
            (when gutter?
              (keyed [margin-left-str-zero
                      border-left-str-zero]))))]
      (if (or (:browser-dev-console? m)
              (true? (:data? m)))
        s
        (some-> s println)))))

#?(:cljs
   (do
     (defn ^:public print-to-browser-dev-console [s]
       (->> s
            bling.browser/ansi-sgr-string->browser-dev-console-array
            (.apply js/console.log js/console)))))


(defn- default-opt [m k set-of-strs default]
  (or (some-> (k m)
              as-str
              (maybe-> set-of-strs))
      default))


(defn- resolve-label
  [{:keys [label label-str] :as m}
   type-as-str]
  (let [blank-string-supplied?              (and (string? label) 
                                                 (string/blank? label))
        nothing-supplied?                   (nil? label)
        supplied-label                      label
        supplied-label-str                  label-str
        supplied-coll-label-shortened       (some-> m
                                                    :label
                                                    (maybe-> coll?)
                                                    (util/shortened 33))
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


(defn- resolve-padding-left [m theme]
  (if (= theme "boxed")
    (:padding-left m)
    (let [default (if (= theme "minimal") 0 2)
          pl      (or (maybe-> (:padding-left m) pos-int?) default)]
      pl)))


(defn- resolve-padding-top
  [theme f]
  #?(:cljs (f :padding-top 0)
     :clj  (if (contains? #{"gutter" "rainbow-gutter" "minimal"} theme)
             (f :padding-top 0)
             (f :padding-top 0))))


(defn- callout-opts* [m]
  (let-map
   [theme          (default-opt m
                                :theme
                                #{"sideline"
                                  "sideline-bold"
                                  "gutter"
                                  "rainbow-gutter"
                                  "minimal"
                                  "boxed"}
                                "sideline")
    sideline-theme? (contains? #{"sideline" "sideline-bold"} theme)
    label-theme    (or (default-opt m
                                    :label-theme
                                    #{"marquee" "minimal" "pipe"}
                                    nil)
                       "minimal")
    sp             (fn [k n] (spacing (get m k) n))
    padding-top    (resolve-padding-top theme sp)
    padding-bottom (sp :padding-bottom 0)
    margin-top     (sp :margin-top #?(:cljs 0 :clj 1))
    margin-bottom  (sp :margin-bottom 0)
    margin-left    (sp :margin-left 0)
    padding-left   (resolve-padding-left m theme)
    type           (some-> (:type m) as-str (maybe-> #{"warning" "error" "info"}))
    colorway       (or (get semantics-by-semantic-type type)
                       (some-> (:colorway m) as-str))
    semantic-type  (or (get semantics-by-semantic-type type)
                       (get semantics-by-semantic-type colorway))
    warning?       (= type "warning")
    error?         (= type "error")
    color          (or (get semantics-by-semantic-type colorway)
                       (maybe-> colorway all-color-names)
                       "neutral")
    user-label     (:label m)
    label*         (resolve-label m type)
    label          (if (string? label*)
                     (-> label*
                         (string/replace #"\n+( +)$" #(second %))
                         (string/replace #"^( +)\n+" #(second %)))
                     label*)
    side-label      (some-> m
                            :side-label
                            (maybe-> string?))
    border-block-length (let [bbl (:border-block-length m)]
                          (or (when (pos-int? bbl) bbl) 50))
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


;; TODO - Shoul we create callout-data as sugar for (callout {... data? true ...} ...)
;; TODO - visual tests

(def ^{:no-doc true} callout-options-map-schema
  [:map
   [:type
    {:optional true
     :desc     ["Will set the label text (unless provided via `:label`). Will also set the `:colorway`, and override any provided `:colorway` value."]}
    [:enum :error "error" :warning "warning" :info "info"]]
   
   [:colorway
    {:optional true
     :desc     ["The color of the sideline border, or gutter, depending on the value of `:theme`."]}
    [:enum :error "error" :warning "warning" :info "info" :positive "positive" :subtle "subtle" :magenta "magenta" :green "green" :negative "negative" :neutral "neutral"]]
   
   [:theme
    {:optional true
     :default  :sideline
     :desc     ["Theme of callout."]}
    [:enum :sideline "sideline" :sideline-bold "sideline-bold" :minimal "minimal" :gutter "gutter" :boxed "boxed"]]
   
   [:label
    {:optional true
     :desc     ["Labels the callout. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string)."
                "In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively."]}
    :any]
   
   [:side-label
    {:optional true
     :desc     ["Side label to the the callout label. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string)."
                "In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively."]}
    :any]
   
   [:label-theme
    {:optional true
     :default  :minimal
     :desc     ["Theme of label."]}
    [:enum :marquee "marquee" :minimal "minimal"]]
   
   [:padding-top
    {:optional true
     :default  0
     :desc     ["Amount of padding (in newlines) at top, inside callout."]}
    :int]
   
   [:padding-bottom
    {:optional true
     :default  0
     :desc     ["Amount of padding (in newlines) at bottom, inside callout."]}
    :int]
   
   [:padding-left
    {:optional true
     :default  2
     :desc     ["Amount of padding (in blank character spaces) at left, inside callout."]}
    :int]
   
   [:margin-top
    {:optional true
     :default  1
     :desc     ["Amount of margin (in newlines) at top, outside callout."
                "Only applies to terminal emulator printing."]}
    :int]
   
   [:margin-bottom
    {:optional true
     :default  0
     :desc     ["Amount of margin (in newlines) at bottom, outside callout."
                "Only applies to terminal emulator printing."]}
    :int]
   
   [:margin-left
    {:optional true
     :default  0
     :desc     ["Amount of margin (in blank character spaces) at left, outside callout."]}
    :int]
   
   [:border-block-length
    {:optional true
     :default  50
     :desc     ["The width of the top and bottom border, only applies to the `:minimal` callout theme."]}
    :int]
   
   [:data?
    {:optional true
     :desc     ["Returns a data representation of result instead of printing it."]}
    :boolean]
   
   [:box-drawing-style
    {:optional true
     :theme    :boxed
     :desc     ["The style of box-drawing character used."]}
    [:enum :thin-round "thin-round" :thin "thin" :bold "bold" :double "double"]]
   
   [:border-char
    {:optional true
     :theme    :boxed
     :desc     ["A char that will override the default box-drawing"
                "character."]}
    :string]
   
   [:vertical-border-char
    {:optional true
     :theme    :boxed
     :desc     ["A char that will override the default box-drawing"
                "character, for the vertical borders."]}
    :string]
   
   [:width
    {:optional true
     :theme    :boxed
     :desc     ["Width of the box in number of chars, aka columns in"
                "terminal. If not set, will be the width of the terminal."
                "If terminal width cannot be detected, will fallback to"
                "80."]}
    :pos-int]
   
   [:max-width
    {:optional true
     :theme    :boxed
     :desc     ["Max width of box in number of chars, aka columns in"
                "terminal. Overridden by the `:width` value, if set."]}
    :pos-int]
   
   [:min-width
    {:optional true
     :theme    :boxed
     :desc     ["Min width of box in number of chars, aka columns in"
                "terminal. Overridden by the `:width` value, if set."]}
    :pos-int]
   
   [:padding-right
    {:optional true
     :default  2
     :theme    :boxed
     :desc     ["Amount of padding (in blank character spaces) at right,"
                "inside callout."
                "In console emulator, defaults to `2`. In browser console,"
                "defaults to `0`."]}
    :int]
   
   [:padding-block
    {:optional true
     :default  1
     :theme    :boxed
     :desc     ["Amount of padding (in blank character spaces) at top and"
                "bottom, inside callout."
                "In console emulator, defaults to `1`. In browser console,"
                "defaults to `0`."]}
    :int]
   
   [:padding-inline
    {:optional true
     :default  2
     :theme    :boxed
     :desc     ["Amount of padding (in blank character spaces) at left"
                "and right, inside callout."
                "In console emulator, defaults to `2`. In browser console,"
                "defaults to `0`."]}
    :int]
   ])


(defn ^:public callout
  "Example:
   
  ```Clojure
  (callout {:type                   :error
            ;; :colorway            :purple          ; <- any bling palette color, overrides :type
            ;; :label                  \"My label\"              ; overrides label assigned by :theme
            :side-label             \"My side label\"  ; must have a :label if you want a :side-label        
            :theme                  :sideline        ; :sideline :sideline-bold :minimal :gutter
            :label-theme            :minimal         ; :minimal :marquee
            ;; :padding-top            0                 
            ;; :padding-left           2                 
            ;; :padding-bottom         0                 
            ;; :padding-right          0                 
            ;; :margin-top             1                 
            ;; :margin-botom           0                 
            ;; :margin-left            0                 
            ;; :data?                  true             ; <- just returns string, no printing
            
            :border-block-length 80               ; <- Only applies to :minimal theme + no label
            
            ;; --- The options below exclusive to :theme of :boxed ---------------
            ;; :box-drawing-style      :thin-round      ; :thin :bold :double
            ;; :border-char            \"*\"
            ;; :vertical-border-char   \"**\"
            ;; :width                  40
            ;; :max-width              100
            ;; :min-width              40
            ;; :padding-block          1
            ;; :padding-inline         2
            }
          (bling [:bold (str \"Line 1\" \"\\n\" \"Line 2\")])
   ```

   Prints a message to the console with a block-based coloring motif.
   Returns nil.
    
   If the `:data?` option is set to `true`, it does not print anything, and
   returns a data representation of the formatting and styling.
   
   Callout prints a colored bounding border in the inline start position.
   The color of the border is determined by the value of the `:type` option, or
   the `:colorway` option. The characteristics of this border are controlled by
   the `:theme` option.
    
   For callouts of the type `:error`, `:warning`, or `:info`, a label is
   printed in the block start postion. If a :type option is set, the label
   string will default to an uppercased version of that string, e.g.
   {:type :INFO} => \"INFO\". If a `:label` option is supplied, that value is
   used instead. When you want to omit label for callouts of the type `:error`,
   `:warning`, or `:info`, you must explicitly set the :label option to an empty
   string.
        
   The amount of vertical padding (in number of lines) within the bounds of the
   message body can be controlled the `padding-top` and `padding-bottom` options.
   The amount of space (in number of lines) above and below the message block
   can be controlled the `margin-top` and `margin-bottom` options.
   
   If two arguments are provided, the first should be a map of valid options.

   All the options:

   ```Clojure
   [:type
    {:optional true
     :desc     [\"Will set the label text (unless provided via `:label`). Will also set the `:colorway`, and override any provided `:colorway` value.\"]}
    [:enum
     :error
     \"error\"
     :warning
     \"warning\"
     :info
     \"info\"]]

   [:colorway
    {:optional true
     :desc     [\"The color of the sideline border, or gutter, depending on the value of `:theme`.\"]}
    [:enum
     :error
     \"error\"
     :warning
     \"warning\"
     :info
     \"info\"
     :positive
     \"positive\"
     :subtle
     \"subtle\"
     :magenta
     \"magenta\"
     :green
     \"green\"
     :negative
     \"negative\"
     :neutral
     \"neutral\"]]

   [:theme
    {:optional true
     :default  :sideline
     :desc     [\"Theme of callout.\"]}
    [:enum
     :sideline
     \"sideline\"
     :sideline-bold
     \"sideline-bold\"
     :minimal
     \"minimal\"
     :gutter
     \"gutter\"
     :boxed
     \"boxed\"]]

   [:label
    {:optional true
     :desc     [\"Labels the callout. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string).\"
                \"In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively.\"]}
    :any]

   [:side-label
    {:optional true
     :desc     [\"Side label to the the callout label. In a terminal emulator context, the value will be cast to a string. In a browser context, the label can be an instance of `bling.core/Enriched`, or any other value (which will be cast to a string).\"
                \"In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively.\"]}
    :any]

   [:label-theme
    {:optional true
     :default  :minimal
     :desc     [\"Theme of label.\"]}
    [:enum
     :marquee
     \"marquee\"
     :minimal
     \"minimal\"]]

   [:padding-top
    {:optional true
     :default  0
     :desc     [\"Amount of padding (in newlines) at top, inside callout.\"]}
    :int]

   [:padding-bottom
    {:optional true
     :default  0
     :desc     [\"Amount of padding (in newlines) at bottom, inside callout.\"]}
    :int]

   [:padding-left
    {:optional true
     :default  2
     :desc     [\"Amount of padding (in blank character spaces) at left, inside callout.\"]}
    :int]

   [:margin-top
    {:optional true
     :default  1
     :desc     [\"Amount of margin (in newlines) at top, outside callout.\"
                \"Only applies to terminal emulator printing.\"]}
    :int]

   [:margin-bottom
    {:optional true
     :default  0
     :desc     [\"Amount of margin (in newlines) at bottom, outside callout.\"
                \"Only applies to terminal emulator printing.\"]}
    :int]

   [:margin-left
    {:optional true
     :default  0
     :desc     [\"Amount of margin (in blank character spaces) at left, outside callout.\"]}
    :int]

   [:border-block-length
    {:optional true
     :default  50
     :desc     [\"The width of the top and bottom border, only applies to the `:minimal` callout theme.\"]}
    :int]

   [:data?
    {:optional true
     :desc     [\"Returns a data representation of result instead of printing it.\"]}
    :boolean]
"

  ;; TODO colorway can take arbitrary hex?
  [x & args]
  (if (empty? args)
    (cond
      ;; The case when user just passes a :label value, so just border and text
      (map? x)
      (callout x nil)

      ;; The case when user just passes a string
      (string? x)
      (callout {} x)

      ;; Internal warning from bling about bad args
      :else
      (callout
       {:type        :warning
        :theme       :sideline-bold
        :label-theme :marquee}
       ;; TODO - this is messy formatiing for data-structures, fix
       (point-of-interest
        {:type   :warning
         :header "bling.core/callout"
         :form   (cons 'callout (list x))
         :body   (str "bling-core/callout, if called with a single argument,\n"
                      "expects either:\n"
                      "- a map of options\n"
                      "- a string\n\n"
                      "Nothing will be printed.")})))

    (if-not (map? x)
      ;; Internal warning from bling about bad args
      (callout
       {:type :warning}
       (point-of-interest
        {:type   :warning
         :header "bling.core/callout"
         :form   (cons 'callout (cons x args))
         :body   (str "bling-core/callout expects a map of options,\n"
                      "followed by any number of values (usually strings).\n\n"
                      "Nothing will be printed.")}))
      (let [opts          x
            ;;  value         (some-> args (maybe-> #(not (string/blank? %))))
            callout-opts  (callout-opts* opts)
            callout-opts+ (merge {:value (string/join "" args)}
                                 opts
                                 callout-opts)]
        #?(:cljs
           (if node?                                                           ;; TODO <- move to enriched or data
             (callout* callout-opts+)
             (do 
               (-> (callout* (assoc callout-opts+ :browser-dev-console? true))
                   browser/print-to-browser-dev-console)
               #_(browser-callout callout-opts+)))

           :clj
           (callout* callout-opts+))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Enriched text public fns and helpers  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn- ^:private tagged-str
  "Expects an EnrichedText record. Returns string wrapped with appropriate ANSI
   SGR codes for styling."
  [o]
  (str (->> o
            :style
            reduce-colors-to-sgr
            m->sgr)
       (:value o)
       "\033[0;m"))


(defn- tag->map
  ([acc s]
   (tag->map false acc s))
  ([use-color-string? acc s]
   (let [kvs (case s
               "bold"             [[:font-weight "bold"]]
               "italic"           [[:font-style "italic"]]
               "underline"        [[:text-decoration "underline"]]
               "solid-underline"  [[:text-decoration "underline"]]
               "double-underline" [[:text-decoration-line "underline"]
                                   [:text-decoration-style "double"]]
               "wavy-underline"   [[:text-decoration-line "underline"]
                                   [:text-decoration-style "wavy"]]
               "dotted-underline" [[:text-decoration-line "underline"]
                                   [:text-decoration-style "dotted"]]
               "dashed-underline" [[:text-decoration-line "underline"]
                                   [:text-decoration-style "dashed"]]
               "strikethrough"    [[:text-decoration "line-through"]]
               (let [cs (:all color-codes)
                     m  (get cs s nil)]
                 (if m
                   [[:color (if use-color-string? s m)]]
                   (when-let [nm (string/replace s #"-bg$" "")]
                     (when-let [m (get cs nm nil)]
                       [[:background-color (if use-color-string? nm m)]])))))]
     (if kvs
       (reduce (fn [acc [k m]] (assoc acc k m)) acc kvs)
       acc))))


(defrecord ^:private EnrichedText [value style])


#?(:cljs
   (defn- href-browser-dev-console [style v]
     (let [href  (when (map? style) (:href style))
           v     (if href
                   (if (= href v) href (str v " " href))
                   v)
           style (if href (dissoc style :href) style)]
       [style v])))


(defn- href-console [style v]
  (let [href  (when (map? style) (:href style))
        v     (if href (hyperlink v href) v)
        style (if href
                (merge {:text-decoration-line :underline}
                       (dissoc style :href))
                style)]
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
                     (let [f (if node? href-console href-browser-dev-console)]
                       (f style v))
                     :clj
                     (href-console style v))]
    (->EnrichedText
     (str v)
     (let [m (cond
               ;; The style is provided as a map e.g.
               ;; (bling [{:font-weight      :bold
               ;;          :font-style       :italic
               ;;          :color            :red
               ;;          :background-color :yellow-bg
               ;;          :text-decoration  :wavy-underline}
               ;;         "Hello"])
               (map? style)
               ;; TODO - can this be more performant?
               (reduce-kv convert-color {} style)

               ;; The style is provided as a tokenized kw e.g. 
               ;; (bling [:bold.red.wavy-underline "Hello"])
               (keyword? style)
               (-> style
                   name
                   (string/split #"\.")
                   (->> (reduce tag->map {}))))]
       (if (and defs/no-color? (map? m))
         (dissoc m :color :background-color)
         m)))))


(defn- enriched-data-inner
  [coll x]
  (let [s (cond (et-vec? x)
                (tagged-str (enriched-text x))
                (not (coll? x))
                (as-str x))]
    (conj coll s)))


;; Hiccup w nested styles, supports :p elements ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- tags->maps [coll]
  (walk/postwalk
   (fn [x]
     (if-let [k (some-> x
                        (maybe-> vector?)
                        (nth 0 nil)
                        (maybe-> keyword?))]
       (if (= [:br] x)
         "\n"
         (into [(-> k
                    name
                    (string/split #"\.")
                    (->> (reduce (partial tag->map true) {})))]
               (rest x)))
       x))
   coll))


(defn- get-style-map [x]
  (some-> x
          (maybe-> vector?)
          (nth 0 nil)
          (maybe-> map?)))


(defn- wrapped-val? [x]
  (boolean (some-> x
                   (maybe-> vector?)
                   (maybe-> #(= 2 (count %)))
                   (nth 0 nil)
                   (maybe-> map?))))


(defn- nest-styles [coll]
  (walk/prewalk
   (fn [x]
     (if-let [m (get-style-map x)]
       ;; If it is a vector with multiple things
       (mapv (fn [v]
               (if-let [m2 (get-style-map v)]
                 (into [(merge m m2)] (rest v))
                 v))
             x)
       x))
   coll))


(defn- wrap-naked-strings [coll]
  (walk/postwalk
   (fn [x]
     (if-let [m (get-style-map x)]
       (if (and (< 2 (count x))
                (some #(not (coll? %)) x))
         (mapv #(if (not (coll? %))
                  [m %]
                  %)
               (rest x))
         x)
       x))
   coll))


(defn- rollup [acc coll]
  (reduce
   (fn [acc x]
     (cond
       (and (vector? x)
            (every? wrapped-val? x))
       (apply conj acc x)

       (wrapped-val? x)
       (conj acc x)

       (vector? x)
       (rollup acc x)

       (not (coll? x))
       (conj acc x)

       :else
       acc))
   acc
   coll))


(defn- p-tag-match* [x]
  (some-> x
          (maybe-> vector?)
          (nth 0 nil)
          (maybe-> keyword?)
          name
          (->> (re-find #"^p(?:\.(\S+))?"))))


(defn- p-tag* [x]
  (when-let [tag
             (p-tag-match* x)]
    (or (some-> tag second keyword)
        :normal)))


(defn- padded-paragraphs
  "Normalizes any :p vectors, adding 2 newlines to end of vector, unless the
   :p vector is the last arg.
   
   Example:

   (padded-paragraphs 
    [[:p.bold 
      \"foo\"
      [:red \"bar\"]]
     \"baz\"])
   =>
   [[:bold 
     \"foo\"
     [:red \"bar\"]
     \"\n\n\"]
    \"baz\"]"
  [args]
  (let [last-index (dec (count args))]
    (vec
     (map-indexed (fn [i x]
                    (if-let [tag (p-tag* x)]
                      (into [tag]
                            (concat (rest x)
                                    (when-not (= i last-index)
                                      ["\n" "\n"])))
                      x))
                  args))))


(defn- nested->flat
  "Hydrates args that are :p elements and flattens nested bling hiccup vectors"
  [args]
  (-> args
      padded-paragraphs
      tags->maps
      nest-styles
      wrap-naked-strings
      (->> (rollup []))
      ;; Maybe unwrap vals here if {:font-style :normal}, for perf
      ))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- contains-nested? [args]
  (some #(when (vector? %)
           (or (some-> (nth % 0 nil) (= :p))
               (some vector? %)))
        args))

(defn- bling-data* [args]
  (let [args   (if (contains-nested? args)
                 (nested->flat (vec args))
                 args)
        coll   (reduce enriched-data-inner [] args)
        tagged (string/join coll)]

    {:tagged tagged
     :args   args}))


(defn ^:public bling
  "Giving any number of strings or hiccup-like vectors, returns a string tagged
   with ANSI SGR codes to style the text as desired."
  [& coll]
  #_(!? :js (-> coll bling-data* :tagged))
  (-> coll bling-data* :tagged))

(defn ^:public print-bling
  "In JVM Clojure, cljs(Node), and bb, `print-bling` is sugar for:
   (println (bling [:bold.blue \"my blue text\"]))

   In cljs (browser dev consoles), `print-bling` is sugar for the the following:
   `(print-to-browser-dev-console (bling [:bold.blue \"my blue text\"]))`

  Example:
  `(print-bling [:bold.blue \"my blue text\"])"
  [& args]
  (let [bling-str (apply bling args)]
    #?(:cljs
       (if node?
         (println bling-str)
         (print-to-browser-dev-console bling-str))
       :clj
       (println bling-str))))
