(ns bling.core
  (:require [clojure.string :as string]
            [fireworks.core :refer [? !? ?> !?>]]
            [clojure.walk :as walk]
            [bling.ansi :as ansi :refer [strlen-minus-ansi-sgr]]
            [bling.browser]
            [bling.hifi]
            [bling.defs :as defs]
            [bling.macros :refer [let-map keyed]]
            [bling.util :as util :refer [maybe-> when->]]
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
    "orange"  {:sgr 166 :css "#d75f00"}
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
    "orange"  {:sgr 208 :css "#ff8700"}
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
    "orange"     {:sgr      172
                  :css      "#d78700"
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

(def ^:public bling-colors
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
                            (case (some-> text-decoration-style as-str)
                              "wavy" "^"
                              "dashed" "-"
                              "dotted" "•"
                              "underline" "─"
                              "double" "═"
                              "^")))))

(def ^:private form-limit 33)

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

;; Unicode characters ----------------------------------------------------------

(def ^:private bdc
  {:h   {:double     "═"
         :bold       "━"
         :thin       "─"
         :thin-round "─"}

   :h+u {:double     "╩"
         :bold       "┻"
         :thin       "┴"
         :thin-round "┴"}

   :h+d {:double     "╦"
         :bold       "┳"
         :thin       "┬"
         :thin-round "┬"}

   :v   {:double     "║"
         :bold       "┃"
         :thin       "│"
         :thin-round "│"}

   :v+l {:double     "╣"
         :bold       "┫"
         :thin       "┤"
         :thin-round "┤"}

   :v+r {:double     "╠"
         :bold       "┣"
         :thin       "├"
         :thin-round "├"}

   :tl  {:double     "╔"
         :bold       "┏"
         :thin       "┌"
         :thin-round "╭"}

   :tr  {:double     "╗"
         :bold       "┓"
         :thin       "┐"
         :thin-round "╮"}

   :bl  {:double     "╚"
         :bold       "┗"
         :thin       "└"
         :thin-round "╰"}

   :br  {:double     "╝"
         :bold       "┛"
         :thin       "┘"
         :thin-round "╯"}

   :mj  {:double     "╝"
         :bold       "┛"
         :thin       "┘"
         :thin-round "╯"}})

(def ^:private box-drawing-styles (into #{} (-> bdc :h keys)))

(defn- horizontal-border-char* [style weight]
  (case style
    "solid"
    (get-in bdc
            (if (= "bold" weight)
              [:h :bold]
              [:h :thin]))
    ("double")
    (get-in bdc [:h :double])
    nil))

(defn- vertical-border-char* [style weight]
  (case style
    "solid"
    (get-in bdc
            (if (= "bold" weight)
              [:v :bold]
              [:v :thin]))
    ("double")
    (get-in bdc [:v :double])
    nil))

(defn- t-shaped-border-char
  [m k]
  (let [style  (:border-style m)
        weight (:border-weight m)]
    (case style
      "solid"
      (get-in bdc
              (if (= "bold" weight)
                [k :bold]
                (if (= "round" (:border-shape m))
                  [k :thin-round]
                  [k :thin])))
      ("double")
      (get-in bdc [k :double])
      nil)))

(defn- first-or-last-horizontal-border-char
  [m top-or-bottom left-or-right horizontal-border-char]
  (let [top?   (= top-or-bottom :top)
        bottom? (= top-or-bottom :bottom)
        left? (= left-or-right :left)
        right? (= left-or-right :right)
        style  (cond (::marquee-label m)
                     (:border-style m)

                     top?
                     (:border-top-style m)

                     bottom?
                     (:border-bottom-style m)

                     :else
                     (:border-style m))
        weight (cond (::marquee-label m)
                     (:border-weight m)

                     top?
                     (:border-top-weight m)

                     bottom?
                     (:border-bottom-weight m)

                     :else
                     (:border-weight m))]
    (if (or (:border-notches? m)
            (::marquee-label m)
            (::boxed-callout-corner m))
      (let [bdc-key (if top?
                      (if left? :tl :tr)
                      (if left? :bl :br))]
        (case style
          "solid"
          (get-in bdc
                  (if (= "bold" weight)
                    [bdc-key :bold]
                    (if (= "round" (:border-shape m))
                      [bdc-key :thin-round]
                      [bdc-key :thin])))
          ("double")
          (get-in bdc [bdc-key :double])
          nil))
      horizontal-border-char)))

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

(defn- list-formatted-as-fn-call [x opts]
  (if (list? x)
    (-> x
        (bling.hifi/hifi opts)
        (string/replace-first #"\n" "")
        (string/replace #"\n$" "")
        (string/replace #"^\(|\)$" "")
        (string/replace #"\n" (str "\n" (-> x
                                            first
                                            name
                                            count
                                            inc
                                            spaces))))
    x))

;; Line and point of interest public fns  -------------------------------------

(defn ^:public point-of-interest
  "`point-of-interest` creates namespace info diagram which identifies a specific form.
   
   This provides the namespace, column, and line number, and a bolded, potentially truncated, representation of the specific form of interest. This form representation is accented with a squiggly underline.
   
   The `:line`, `:column`, `:form`, and `:file` options must all be present in order for the namespece info diagram to be rendered. If the `:form` option is supplied, but any of the others are omitted, only the form will be rendered (with an underline and no line-info diagram).
   
   By default, the diagram is created with a leading and trailing newlines. This can be set to zero, or increased, with the `:margin-block` option.
   
   Example
   ```clojure
   (point-of-interest
    {:text-decoration-style :wavy,
     :file \"myfile.core\",
     :type :error,
     :column 11,
     :line 42,
     :form '(+ 1 true)})
   ```
   
   All the options:
   
   * **`:file`**
       - `string?`
       - Optional.
       - File or namespace
   
   * **`:line`**
       - `int?`
       - Optional.
       - Line number
   
   * **`:column`**
       - `int?`
       - Optional.
       - Column number
   
   * **`:margin-block`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Controls the number of blank lines above and below the diagram.
   
   * **`:type`**
       - `#{\"warning\" \"error\" :warning :error}`
       - Optional.
       - Automatically sets the `:text-decoration-color`.
   
   * **`:text-decoration-color`**
       - `#{\"neutral\" \"magenta\" \"warning\" :neutral :green \"negative\" :negative \"error\" :warning \"green\" :error :magenta}`
       - Optional.
       - Defaults to `:neutral`.
       - Controls the color of the underline.
   
   * **`:text-decoration-style`**
       - `#{\"dashed\" :double :wavy :solid \"wavy\" \"dotted\" \"solid\" \"double\" :dashed :dotted}`
       - Optional.
       - Controls the color of the underline.
   
   * **`:truncate-form-to-single-line?`**
       - `boolean?`
       - Optional.
       - Defaults to `true`.
       - Truncates the form to a single line."
  {:desc          ["`point-of-interest` creates namespace info diagram which identifies a"
                   "specific form.\n\n"

                   "This provides the namespace, column, and line number, and a"
                   "bolded, potentially truncated, representation of the specific form of"
                   "interest. This form representation is accented with a squiggly underline.\n\n"

                   "The `:line`, `:column`, `:form`, and `:file` options must all be present in"
                   "order for the namespece info diagram to be rendered. If the `:form` option is"
                   "supplied, but any of the others are omitted, only the form will be rendered"
                   "(with an underline and no line-info diagram).\n\n"

                   "By default, the diagram is created with a leading and trailing newlines."
                   "This can be set to zero, or increased, with the `:margin-block` option."]
   :examples      [{:desc  "Example"
                    :forms '[[(point-of-interest
                               {:form                  '(+ 1 true)
                                :line                  42
                                :column                11
                                :file                  "myfile.core"
                                :text-decoration-style :wavy
                                :type                  :error})]]}]

   ;; TODO - Consider adding option for :file-info-style, which would be a map for bling
   ;; TODO - Consider adding option for :line-number-style, to decorate number in gutter, which would be a map for bling
   :options       [:map
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

                   [:truncate-form-to-single-line?
                    {:optional true
                     :default  true
                     :desc     ["Truncates the form to a single line."]}
                    :boolean]]
   :clj-docstring [[:example "Example"]
                   :desc
                   :options]}
  [{:keys [line
           file
           column
           form
           header ; <- deprecated / undocumented
           body   ; <- deprecated / undocumented
           margin-block
           text-decoration-color
           text-decoration-style
           type
           truncate-form-to-single-line?
           form-hifi-options]
    :as   opts
    :or   {truncate-form-to-single-line? true
           form-hifi-options             nil}}]
  (let [type             (some-> type as-str (maybe-> #{"warning" "error"}))
        file-info        (bling [:italic
                                 (ns-info-str opts)])
        gutter           (some-> line str count spaces)
        underline-color  (or (some->> type (get semantics-by-semantic-type))
                             (some-> text-decoration-color
                                     as-str
                                     (maybe-> all-color-names))
                             "neutral")
        form-hifi        (or  (some-> form
                                      (when-> list?)
                                      (list-formatted-as-fn-call form-hifi-options))
                              (bling.hifi/hifi form form-hifi-options))
        header           (str header)
        body             (str body)
        mb*              (or (some-> margin-block (maybe-> pos-int?))
                             (if (some-> margin-block zero?)
                               0
                               1))
        mb               (char-repeat mb* "\n")

        ;; Fix for cljs
        diagram-char     #?(:cljs (fn [s] s) :clj #(bling [:subtle %]))
        diagram          (when form
                           (if (? truncate-form-to-single-line?)
                             (let [multi-line?      (boolean (re-find #"\n" form-hifi))
                                   form-as-str      (-> form-hifi
                                                        (string/split #"\n")
                                                        first
                                                        (str (if multi-line? "..." "")))
                                   underline-str    (poi-text-underline-str
                                                     (strlen-minus-ansi-sgr form-as-str)
                                                     0
                                                     text-decoration-style)
                                   bolded-form      form-as-str
                                   underline-styled [{:font-weight :normal
                                                      :color       underline-color
                                                      :contrast    :medium}
                                                     underline-str]]
                               (if (and line column file form)
                                 [mb
                                  gutter (diagram-char " ┌─ ") file-info "\n"
                                  gutter (diagram-char " │ ") "\n"
                                  line   (diagram-char " │ ") bolded-form "\n"
                                  gutter (diagram-char " │ ") underline-styled
                                  mb
                                  "\n"]
                                 [mb
                                  bolded-form "\n"
                                  underline-styled
                                  mb]))
                             (if (and line column file form)
                               (let [form-hifi-with-gutter
                                     (-> form-hifi
                                         (string/split #"\n")
                                         (->> (map-indexed
                                               (fn [i ln]
                                                 (str (when (pos? i)
                                                        (str gutter
                                                             (diagram-char " │ ")))
                                                      ln)))
                                              (string/join "\n")))]
                                 (vec (concat [mb
                                               gutter (diagram-char " ┌─ ") file-info "\n"
                                               gutter (diagram-char " │ ") "\n"
                                               line   (diagram-char " │ ") form-hifi-with-gutter "\n"
                                               gutter (diagram-char " │ ")]
                                              [mb
                                               "\n"])))
                               form-hifi)))
        ret              (apply bling
                                (util/concatv header
                                              diagram
                                              body))]
    ret))

;; Enriched text public fns and helpers  --------------------------------------

(declare ln)
(declare lns)

;;            CCCCCCCCCCCCC          OOOOOOOOO                               
;;         CCC::::::::::::C        OO:::::::::OO                             
;;       CC:::::::::::::::C      OO:::::::::::::OO                           
;;      C:::::CCCCCCCC::::C     O:::::::OOO:::::::O     ******         ******
;;     C:::::C       CCCCCC     O::::::O   O::::::O     *:::::*       *:::::*
;;    C:::::C                   O:::::O     O:::::O     ***::::*******::::***
;;    C:::::C                   O:::::O     O:::::O        **:::::::::::**   
;;    C:::::C                   O:::::O     O:::::O     ******:::::::::******
;;    C:::::C                   O:::::O     O:::::O     *:::::::::::::::::::*
;;    C:::::C                   O:::::O     O:::::O     ******:::::::::******
;;    C:::::C                   O:::::O     O:::::O        **:::::::::::**   
;;     C:::::C       CCCCCC     O::::::O   O::::::O     ***::::*******::::***
;;      C:::::CCCCCCCC::::C     O:::::::OOO:::::::O     *:::::*       *:::::*
;;       CC:::::::::::::::C      OO:::::::::::::OO      ******         ******
;;         CCC::::::::::::C        OO:::::::::OO                             
;;            CCCCCCCCCCCCC          OOOOOOOOO                               

(defn- adjusted-sgr-strlen
  [m k]
  (- (or (count (k m)) 0)
     (or (ansi/sgr-count (k m)) 0)))

(defn- resolve-header-gap
  [m]
  (let [n
        (- (max (or (maybe-> (:width m) pos-int?) 0)
                (:min-width m))
           (or (+ (or (adjusted-sgr-strlen m :header-with-label) 0)
                  (or (adjusted-sgr-strlen m :side-label) 0))
               0))]
    (if (pos-int? n)
      (max n (:header-gap m)) 0)))

(defn- header-gap-str
  [{:keys [border-style-map
           theme
           border-top-char]
    :as m}]
  (bling [border-style-map
          (util/sjr (if (= theme "sideline")
                      (:header-gap m)
                      (resolve-header-gap m))
                    border-top-char)]))

(defn- sideline-marquee-label
  [{:keys [padding-left
           header-padding-left
           margin-left
           label
           theme
           border-style
           border-top-style
           border-weight
           border-top-weight
           border-style-map
           border-notches?
           side-label]
    :as m}]

  (let [margin-left-str    (char-repeat margin-left (if (= "gutter" theme) defs/gutter-char " "))
        margin-left-str-0  (char-repeat margin-left
                                        (if (= "gutter" theme)
                                          defs/gutter-char-lower-seven-eighths
                                          " "))

        sandwich-theme?      (= theme "sandwich")
        ;; This currently takes the first line of a multi-line label
        ;; AKA multi-line labels in the marquee label theme are not supported
        ;; Maybe add support in the future? Would have to figure out how to
        ;;    dynamically center the middle-left connector
        label-length        (ansi/strlen-minus-ansi-sgr label)
        bs                  border-style-map
        hbc                 (horizontal-border-char* border-style border-weight)
        vbc                 (vertical-border-char* border-style border-weight)
        top-and-bottom-mid (str (char-repeat 2 hbc)
                                (char-repeat label-length hbc)
                                (char-repeat 2 hbc))

        ;; TODO  header-padding-left is not working

        top-line-str       (bling [bs
                                   (str (if (= theme "gutter")
                                          margin-left-str-0
                                          margin-left-str)
                                        (char-repeat header-padding-left " ")
                                        (first-or-last-horizontal-border-char
                                         (assoc m ::marquee-label true)
                                         :top
                                         :left
                                         hbc)
                                        top-and-bottom-mid
                                        (first-or-last-horizontal-border-char
                                         (assoc m ::marquee-label true)
                                         :top
                                         :right
                                         hbc))])]
    (string/join
     (interpose
      "\n"
      (util/concatv
       [        ;; The first, or "top" line of the lines that comprise the marquee label
        top-line-str

        ;; The second, or "middle" line of the lines that comprise the marquee label
        (let [s
              (str (bling [bs
                           (str margin-left-str
                                (cond
                                  (= theme "gutter")
                                  " "

                                  (or (= theme "sideline")
                                      (and sandwich-theme?
                                           border-notches?
                                           (not (zero? header-padding-left))))
                                  (first-or-last-horizontal-border-char
                                   (assoc m ::marquee-label true)
                                   :top
                                   :left
                                   hbc)

                                  :else
                                  (when-not (and (= theme "sandwich")
                                                 (zero? header-padding-left))
                                    (horizontal-border-char* border-top-style
                                                             border-top-weight)))
                                (char-repeat (dec header-padding-left)
                                             (if (= theme "gutter") " " hbc))
                                (if (or (= theme "gutter")
                                        (and (= theme "sandwich")
                                             (zero? header-padding-left)))
                                  vbc
                                  (t-shaped-border-char m :v+l))
                                "  ")])
                   (bling [{:font-color :neutral} label])
                   "  "
                   (bling [bs (if (= theme "sandwich")
                                (t-shaped-border-char m :v+r)
                                vbc)]))]
          (str s
               (when (= theme "sandwich")
                 (header-gap-str (merge m
                                        {:header-with-label s
                                         :border-top-char   hbc})))
               (when side-label (bling "  " [:italic side-label]))))]

       ;; The last line of the lines that comprise the marquee label
       [(bling [bs
                (str margin-left-str
                     (if (= theme "sideline")
                       (str vbc
                            (char-repeat (dec header-padding-left) " "))
                       (char-repeat header-padding-left " "))
                     (first-or-last-horizontal-border-char
                      (assoc m ::marquee-label true)
                      :bottom
                      :left
                      hbc)
                     top-and-bottom-mid
                     (first-or-last-horizontal-border-char
                      (assoc m ::marquee-label true)
                      :bottom
                      :right
                      hbc))])])))))

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
          (when-not (= (:theme m) "gutter")
            (bling [(:border-style-map m)
                    (get m (if gutter-label-line-zero?
                             :border-left-str-zero
                             :border-left-str))]))
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
        padding-top-lns-coll        (when (and (= (:theme m) "gutter")
                                               (or (:label m) (:side-label m)))
                                      (char-repeat (:padding-top m) " "))
        lns-coll                    (concat padding-top-lns-coll
                                            (restore-ansi-sgr-over-lines s))
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

;; Minimal callout start -------------------------------------------------------

(defn- header-with-label
  [{:keys [border-style-map
           header-padding-left
           label
           border-top-char]
    :as   m}]
  (let [header-padding-left? (pos-int? header-padding-left)
        label-padding 1]
    (bling [border-style-map
            (when header-padding-left?
              (first-or-last-horizontal-border-char
               m
               :top
               :left
               border-top-char))]
           (when header-padding-left?
             (bling [border-style-map
                     (util/sjr (dec header-padding-left)
                               border-top-char)]))
           (str (when header-padding-left?
                  (when label
                    (spaces label-padding)))
                label
                (when label
                  (spaces label-padding))))))

(defn- gutter-label-line*
  [{:keys [label-theme
           border-style-map
           header-gap
           margin-left
           padding-left
           theme
           label
           side-label]
    :as m}]
  (cond
    (and label (= label-theme "marquee"))
    (sideline-marquee-label m)

    :else
    (let [          ;; This produces the first (left) part of the header line for a
          ;; :gutter callout with a :label-theme of :simple

          ;; It is used for callouts with a theme of :gutter

          ;; Examples:
          ;; "▆ My label"
          ;; "▆▆▆ My Label" 

          margin-left-str  (bling [border-style-map
                                   (char-repeat margin-left
                                                (if (= "gutter" theme)
                                                  defs/gutter-char-lower-seven-eighths
                                                  " "))])
          header-with-label  (str (char-repeat padding-left " ") label)
          side-label         (some->> side-label (str " "))]

      ;; This produces the second (right) part of the header line  
      (str margin-left-str
           header-with-label
           (char-repeat header-gap " ")
           (when side-label side-label)))))

(defn- sandwich-label-line*
  [{:keys [label-theme
           border-top-style
           border-top-weight
           margin-left-str
           theme
           label
           side-label]
    :as m}]
  (cond
    (and label (= label-theme "marquee"))
    (sideline-marquee-label  m)

    :else
    (let [border-top-char    (horizontal-border-char* border-top-style
                                                      border-top-weight)

          ;; This produces the first (left) part of the header line for a
          ;; callout with a :label-theme of :simple

          ;; It is used for callouts with a theme of :sideline or :simple

          ;; Examples:

          ;; "My label"
          ;; "┌ My label"
          ;; "┌── My Label" 
          ;; "════ My Label" 
          header-with-label  (header-with-label
                              (assoc m
                                     :border-top-char
                                     border-top-char))
          side-label         (some->> side-label (str " "))]

      ;; This produces the second (right) part of the header line  
      (str margin-left-str
           header-with-label
           (when (or (= theme "sandwich")
                     (and (= theme "sideline")
                          side-label))
             (header-gap-str (merge m
                                    (keyed [header-with-label
                                            border-top-char]))))
           (when side-label
             side-label)))))

(defn- sandwich-callout-label-line-length*
  [label-line m]
  (let [n (apply max
                 (mapv #(- (count %)
                           (or (ansi/sgr-count %)
                               0))
                       (string/split label-line #"\n")))]
    (max (- (or n 0) (:margin-left m)) 0)))

(defn- sandwich-callout
  [{:keys [border-style-map
           border-bottom-style
           border-bottom-weight
           margin-left-str]
    :as   m}]
  (let [label-line (sandwich-label-line* m)
        body-lines (body-lines-no-border m)]
    (str label-line
         "\n"
         body-lines
         (when-not (string/blank? body-lines) "\n")
         (when-not (string/blank? body-lines)
           (let [border-bottom-char
                 (horizontal-border-char* border-bottom-style
                                          border-bottom-weight)

                 bottom-left-box-drawing-char
                 (first-or-last-horizontal-border-char m
                                                       :bottom
                                                       :left
                                                       border-bottom-char)

                 label-line-length
                 (sandwich-callout-label-line-length* label-line m)]
             (bling margin-left-str
                    [border-style-map
                     (str bottom-left-box-drawing-char
                          (string/join (repeat (dec label-line-length)
                                               border-bottom-char)))]))))))

;; Minimal callout end ---------------------------------------------------------

(defn- sideline-callout
  [{:keys [theme
           label
           border-style
           border-weight
           side-label
           label-theme
           border-style-map
           padding-left
           margin-left-str]
    :as m}]
  (let [marquee-label?
        (and label
             (-> m :label as-str string/blank? not)
             (contains? #{"marquee"} label-theme))

        label-line
        (cond
          ; label-theme is :marquee
          marquee-label?
          (sideline-marquee-label m)

          ; label-theme is :simple
          :else
          (sandwich-label-line* m))

        bottom-line
        (bling [border-style-map
                (str margin-left-str
                     (first-or-last-horizontal-border-char
                      m
                      :bottom
                      :left
                      (horizontal-border-char* border-style
                                               border-weight)))])]
    (str
     label-line
     "\n"
     (body-lines-with-border m)
     "\n"
     bottom-line)))

(defn- ansi-callout-str
  [{:keys [theme value label side-label] :as m}]
  (let [sideline-variant?            (= "sideline" theme)
        sandwich-variant?            (= "sandwich" theme)
        sideline-variant-with-body?  (boolean (and value sideline-variant?))
        sideline-variant-just-label? (and (nil? value) sideline-variant?)
        gutter-theme?                (contains? #{"rainbow-gutter" "gutter"} theme)
        m                            (assoc-in m [:border-style-map :contrast] :medium)]
    ;; (? (keyed [sideline-variant?
    ;;            sandwich-variant?
    ;;            sideline-variant-with-body?
    ;;            sideline-variant-just-label?
    ;;            gutter-theme?]))
    (str (:margin-top-str m)
         (if sideline-variant-with-body?
           (sideline-callout m)
           (cond
             sandwich-variant?
             (sandwich-callout m)

             sideline-variant-just-label?
             (lns m :label)

             gutter-theme?
             (str (when (or label side-label)
                    (gutter-label-line* m))
                  "\n"
                  (lns m :value))))
         (:margin-bottom-str m))))

;; -----------------------------------------------------------------------------
;; Boxed callout start 
;; -----------------------------------------------------------------------------

(defn- wrapped-string-inner
  [max-cols
   {:keys [result col]
    :as   acc}
   word]
  (if (= word "")
    acc
    (let [nl?              (= word "\n")
          space+word       (str (when-not (or nl? (zero? col)) " ") word)
          space+word-width (strlen-minus-ansi-sgr space+word)
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
        (if (< max-width (strlen-minus-ansi-sgr s))
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
  [{:keys [pd horizontal-border-char colorway label header-padding-left]}]
  (let [colorize           #(if-not (re-find ansi/sgr-re %)
                              (bling [{:color colorway} %])
                              %)
        label-str          (or (some-> label
                                       (maybe-> string?)
                                       (maybe-> #(not (string/blank? %)))
                                       (str " ")
                                       (->> (str " "))
                                       colorize)
                               "")
        label-char-count   (strlen-minus-ansi-sgr label-str)
        label-pd-str       (when (and (pos? label-char-count) pd)
                             (-> (or header-padding-left pd)
                                 dec
                                 (util/sjr horizontal-border-char)
                                 (colored-border colorway)))
        label-pd-str-count (or (some-> label-pd-str
                                       strlen-minus-ansi-sgr)
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
  (let [vertical-border-char
        (some-> (or vertical-border-char
                    border-char
                    (vertical-border-char* (:border-style m)
                                           (:border-weight m)))
                (colored-border colorway))

        vertical-border-char-count
        (strlen-minus-ansi-sgr vertical-border-char)

        horizontal-border-char
        (some-> (or horizontal-border-char
                    border-char
                    (horizontal-border-char* (:border-style m)
                                             (:border-weight m)))
                (colored-border colorway))]
    (keyed [box-drawing-style
            vertical-border-char
            vertical-border-char-count
            horizontal-border-char])))

(defn- zero-or-pos? [x] (some-> x (maybe-> int?) (> -1)))

;; TODO - support "fit-width"
(defn- boxed-callout
  "Creates a callout with a border on all sides"
  ([s] (boxed-callout s nil))
  ([s
    {:keys     [width
                max-width
                min-width
                colorway
                label
                side-label
                header-padding-left]
     pd-top    :padding-top
     pd-bottom :padding-bottom
     pd-left   :padding-left
     pd-right  :padding-right
     pd-block  :padding-block
     pd-inline :padding-inline
     supplied-horizontal-border-char :horizontal-border-char
     :as       m}]
   (let [{:keys [box-drawing-style
                 vertical-border-char
                 vertical-border-char-count
                 horizontal-border-char]}
         (border-chars m)
         ;;  pd-block                   (or (some-> pd-block (maybe-> zero-or-pos?)) 2)
         pd-inline                  (or (maybe-> pd-inline pos-int?) 2)
         pd-top                     (or (some-> pd-top
                                                (maybe-> zero-or-pos?))
                                        pd-block)
         pd-bottom                  (or (some-> pd-bottom
                                                (maybe-> zero-or-pos?))
                                        pd-block)
         pd-right                   (min (or (some-> pd-right (maybe-> zero-or-pos?))
                                             pd-inline)
                                         10)
         pd-left                    (min (or (some-> pd-left (maybe-> zero-or-pos?))
                                             pd-inline)
                                         10)
         terminal-width             (resolve-terminal-width min-width max-width)
         cols                       (or width terminal-width)
         cols                       (- (max min-width cols)
                                       (or (:margin-left m) 0))
         max-inner-cols             (- cols
                                       pd-left
                                       pd-right
                                       (* 2 vertical-border-char-count))
         pd-left-str                (util/sjr pd-left " ")
         start                      (str vertical-border-char pd-left-str)
         start-count                (strlen-minus-ansi-sgr start)
         pd-ln                      (str vertical-border-char
                                         (util/sjr (- cols
                                                      (* 2 vertical-border-char-count))
                                                   " ")
                                         vertical-border-char
                                         "\n")
         pd-top-ln                  (util/sjr pd-top pd-ln)
         pd-bottom-ln               (util/sjr pd-bottom pd-ln)
         ;; this gets a border char
         corner-border-char         #(or supplied-horizontal-border-char
                                         (first-or-last-horizontal-border-char
                                          (assoc m ::boxed-callout-corner true)
                                          (case % :tl :top :tr :top :bl :bottom :br :bottom)
                                          (case % :tl :left :tr :right :bl :left :br :right)
                                          horizontal-border-char)
                                         (some->> box-drawing-style (-> bdc %)))
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
                                          (when (< 4 (- cols (+ (strlen-minus-ansi-sgr label)
                                                                (strlen-minus-ansi-sgr side-label)
                                                                4)))
                                            side-label)
                                          truncated-side-label)))
         block-border-opts          (keyed [pd-left
                                            header-padding-left
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

         margin-left-str            (char-repeat (:margin-left m) " ")
         ret
         (str margin-left-str top-border
              "\n"
              margin-left-str pd-top-ln
              (string/join
               "\n"
               (mapv (fn [ln]
                       (str margin-left-str
                            start
                            ln
                            (util/sjr (- cols
                                         (+ start-count
                                            (strlen-minus-ansi-sgr ln))
                                         vertical-border-char-count)
                                      " ")
                            vertical-border-char))
                     lns))
              "\n"
              margin-left-str pd-bottom-ln
              margin-left-str bottom-border)]
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

(defn- border-left-str
  [{:keys [theme border-style border-weight]}
   gutter-str]
  (case theme
    "sideline"
    (vertical-border-char* border-style border-weight)
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
    (let [char                   defs/gutter-char
          border-style-map       {:color    (:color m)
                                  :contrast :medium}
          gutter?                (= "gutter" theme)
          rainbow?               (= "rainbow-gutter" theme)
          gutter-str             (if rainbow?
                                   (bling [{:color (last rainbow-colors)} char])
                                   (bling [border-style-map char]))
          rainbow-gutter-str     (apply bling
                                        (for [s (drop-last rainbow-colors)]
                                          [{:color s} char]))
          rainbow-gutter-str-odd (apply bling
                                        (for [s (drop-last rainbow-colors-system)]
                                          [{:color s} char]))
          cr                     (fn [k ch] (char-repeat (or (k m) 0) ch))
          gutter-str-zero        (bling [border-style-map
                                         (string/join
                                          (cr :margin-left
                                              defs/gutter-char-lower-seven-eighths))])
          margin-left-str-zero   gutter-str-zero
          border-left-str-zero   defs/gutter-char-lower-seven-eighths
          margin-left-str        (if rainbow?
                                   rainbow-gutter-str
                                   (cr
                                    :margin-left
                                    (case theme
                                      "gutter"         gutter-str
                                      "rainbow-gutter" rainbow-gutter-str
                                      " ")))
          s                      (ansi-callout-str
                                  (merge
                                   m
                                   {:border-style-map  border-style-map
                                    :border-left-str   (border-left-str m gutter-str)
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

(defn- shortened-label [s label-max-length]
  (if (< label-max-length (count s))
    (str (apply str (take label-max-length s)) "...")
    s))

(defn- supplied-label-shortened [label label-max-length]
  (some-> label
          str
          string/split-lines
          first
          (shortened-label label-max-length)))

(defn- supplied-coll-label-shortened [m label-length-limit]
  (some-> m
          :label
          (maybe-> coll?)
          (util/shortened label-length-limit)))

(defn- resolve-label
  [{:keys [label]
    :as   m}
   label-max-length]
  (let [blank-string-supplied? (and (string? label) (string/blank? label))
        nothing-supplied?      (nil? label)]

    ;; Blank string is a force-nil situation (in event a :type is provided) 
    (if blank-string-supplied?
      nil
      ;; If no label value was supplied try a fallback based on callout type
      ;; Else use a shortened version of supplied label
      (if nothing-supplied?
        (some-> m :type name string/upper-case)
        (or (supplied-coll-label-shortened m label-max-length)
            (supplied-label-shortened label label-max-length))))))

(defn- spacing [m k default]
  (let [n (get m k)]
    (if (and (int? n) (<= 0 n)) n default)))

(defn- resolve-padding-left [m theme label]
  (if (= theme "boxed")
    (or (:padding-left m)
        (if label 1 0))
    (let [default (if (= theme "sandwich") 0 2)
          pl      (or (maybe-> (:padding-left m) pos-int?) default)]
      pl)))

(defn- resolve-block-padding-based-on-label
  [theme label]
  (if (contains? #{"sandwich" "sideline"} theme)
    (if label 1 0)
    0))

(defn- resolve-padding-top
  [theme label f]
  (let [n
        (if (contains? #{"gutter" "rainbow-gutter" "sandwich"} theme)
          (f :padding-top 0)
          (f :padding-top 0))]
    (if (zero? n)
      (resolve-block-padding-based-on-label theme label)
      n)))

(defn- resolve-padding-top2 [m theme label padding-block]
  (cond (contains? #{"gutter" "rainbow-gutter" "sandwich"} theme)
        (spacing m :padding-top padding-block)))

(defn- callout-opts* [m]
  (let-map
   [theme              (default-opt m
                                    :theme
                                    #{"sideline"
                                      "gutter"
                                      "rainbow-gutter"
                                      "sandwich"
                                      "boxed"}
                                    "sandwich")
    ; :"┌" and "└" (or similar) chars are use for the top-left and bottom left "corners" on the header and footer
    border-notches?      (if (true? (m :border-notches?))
                           true
                           (if (= theme "sideline") true false))

    sideline-theme?    (contains? #{"sideline"} theme)
    label-theme        (default-opt m
                                    :label-theme
                                    #{"marquee" "simple" "pipe"}
                                    "simple")
    label-max-length   (spacing m :label-max-length 42)
    label*             (resolve-label m label-max-length)
    label              (if (string? label*)
                         (-> label*
                             (string/replace #"\n+( +)$" #(second %))
                             (string/replace #"^( +)\n+" #(second %)))
                         label*)
    side-label           (some-> m
                                 :side-label
                                 (maybe-> string?))
    padding-block      (spacing m
                                :padding-block
                                (if (and (contains? #{"sideline" "gutter"} theme)
                                         (not label)
                                         (not side-label))
                                  0
                                  1))
    padding-inline     (spacing m
                                :padding-inline
                                (case theme
                                  "boxed"
                                  (if label 3 1)
                                  "sideline"
                                  2
                                  "gutter"
                                  2
                                  "sandwich"
                                  (cond
                                    (= "marquee" label-theme)
                                    2
                                    border-notches?
                                    2
                                    :else
                                    0)
                                  0))
    padding-top        (spacing m :padding-top padding-block)
    padding-bottom     (spacing m :padding-bottom padding-block)
    margin-top         (spacing m :margin-top #?(:cljs 0 :clj 1))
    margin-bottom      (spacing m :margin-bottom 0)
    margin-left        (let [n (spacing m :margin-left 0)]
                         (if (and (= theme "gutter") (zero? n)) 1 n))
    ;; padding-left       (resolve-padding-left m theme label)
    padding-left       (spacing m :padding-left padding-inline)
    type               (some-> (:type m) as-str (maybe-> #{"warning" "error" "info"}))
    colorway           (or (get semantics-by-semantic-type type)
                           (some-> (:colorway m) as-str))
    semantic-type      (or (get semantics-by-semantic-type type)
                           (get semantics-by-semantic-type colorway))
    warning?           (= type "warning")
    error?             (= type "error")
    color              (or (get semantics-by-semantic-type colorway)
                           (maybe-> colorway all-color-names)
                           "subtle")
    user-label           (:label m)

    ;; deprecated
    border-block-length  (let [bbl (:border-block-length m)]
                           (or (when (pos-int? bbl) bbl) 50))

    ;; new ---------------------------------------------------------------------
    border-style         (default-opt m
                                      :border-style
                                      #{"double" "solid"}
                                      "solid")
    border-weight        (default-opt m
                                      :border-weight
                                      #{"normal" "bold"}
                                      (if (= theme "gutter") "bold" "normal"))
    border-shape         (default-opt m
                                      :border-shape
                                      #{"sharp" "round"}
                                      "sharp")

    border-top-style     (let [s
                               (default-opt m
                                            :border-top-style
                                            #{"double" "solid" "none"}
                                            border-style)]
                           (if (and (= theme "sideline")
                                    (= s "none"))
                             "solid"
                             s))

    border-top-weight    (default-opt m
                                      :border-top-weight
                                      #{"bold" "normal"}
                                      border-weight)

    border-bottom-style  (let [s
                               (default-opt m
                                            :border-bottom-style
                                            #{"double" "solid" "none"}
                                            border-style)]
                           (if (and (= theme "sideline")
                                    (= s "none"))
                             "solid"
                             s))

    border-bottom-weight (default-opt m
                                      :border-bottom-weight
                                      #{"bold" "normal"}
                                      border-weight)

    header-padding-left  (spacing m :header-padding-left 2)

    min-width            (spacing m :min-width 40)

    width                (default-opt m
                                      :width
                                      #{"auto"}
                                      (spacing m :width nil))

; When width is set to auto, this is the minimum amount of spaces between the header label and the side label. This space will be occupied by the border, unless :border-style is set to :none.
    header-gap   (spacing m :header-gap 5)

    ; use to control the offset of the header label
    header-padding-left    (let [n (spacing m
                                            :header-padding-left
                                            (case theme
                                              "boxed"
                                              3
                                              "sandwich"
                                              (if (= label-theme "simple") 4 2)
                                              2)
                                            #_(case theme
                                                "sideline"
                                                2
                                                "boxed"
                                                3
                                                (if border-notches? 2 2)))]
                             (if (and (= theme "sideline") (zero? n)) 1 n))

    gutter-color         (or (get semantics-by-semantic-type colorway)
                             (maybe-> colorway all-color-names)
                             nil)]))

;;           CCCCCCCCCCCCC         OOOOOOOOO      
;;        CCC::::::::::::C       OO:::::::::OO    
;;      CC:::::::::::::::C     OO:::::::::::::OO  
;;     C:::::CCCCCCCC::::C    O:::::::OOO:::::::O 
;;    C:::::C       CCCCCC    O::::::O   O::::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;   C:::::C                  O:::::O     O:::::O 
;;    C:::::C       CCCCCC    O::::::O   O::::::O 
;;     C:::::CCCCCCCC::::C    O:::::::OOO:::::::O 
;;      CC:::::::::::::::C     OO:::::::::::::OO  
;;        CCC::::::::::::C       OO:::::::::OO    
;;           CCCCCCCCCCCCC         OOOOOOOOO 

(defn ^:public callout
  "Prints a message to the console with a block-based coloring motif. Returns nil.
   
   If the `:data?` option is set to `true`, it does not print anything, and returns a data representation of the formatting and styling.
   
   Callout prints a colored bounding border in the inline start position. The color of the border is determined by the value of the `:type` option, or the `:colorway` option. The characteristics of this border are controlled by the `:theme` option.
   
   For callouts of the type `:error`, `:warning`, or `:info`, a label is printed in the block start postion. If a :type option is set, the label string will default to an uppercased version of that string, e.g. {:type :INFO} => \"INFO\". If a `:label` option is supplied, that value is used instead. When you want to omit label for callouts of the type `:error`, `:warning`, or `:info`, you must explicitly set the :label option to an empty string.
   
   The amount of vertical padding (in number of lines) within the bounds of the message body can be controlled the `padding-top` and `padding-bottom` options. The amount of space (in number of lines) above and below the message block can be controlled the `margin-top` and `margin-bottom` options.
   
   If two arguments are provided, the first should be a map of valid options.
   
   Example call with all of the options
   ```clojure
   (callout {:type                   :error            ; :warning, :info
             ;; :colorway            :purple           ; <- any bling palette color, overrides :type
             ;; :label               \"My label\"      ; overrides label assigned by :type
             :side-label             \"My side label\" ; must have a :label if you want a :side-label        
             :theme                  :sideline         ; :sideline :sandwich :gutter
             :label-theme            :simple           ; :simple :marquee
             ;; :padding-block       0                 
             ;; :padding-top         0                 
             ;; :padding-bottom      0                 
             ;; :padding-inline      2                 
             ;; :padding-left        2                 
             ;; :padding-right       0                 
             ;; :margin-top          1                 
             ;; :margin-botom        0                 
             ;; :margin-left         0                 
             ;; :data?               true              ; <- just returns string, no printing
             
             :border-shape           :sharp            ; :round
             :border-style           :solid            ; :double
             :border-weight          :normal           ; :bold
             :width                  40
             :min-width              40
             
             ;; --- The options below exclusive to :theme of :boxed ---------------
             ;; :border-char            \"*\"
             ;; :vertical-border-char   \"**\"
             ;; :max-width              100
             ;; :padding-block          1
             ;; :padding-inline         2
             }
    (bling [:bold (str \"Line 1\" \"\\n\" \"Line 2\")])
   ```
   
   All the options:
   
   * **`:colorway`**
       - `#{\"neutral\" \"magenta\" \"warning\" \"positive\" :neutral \"info\" :green :positive \"negative\" :negative \"error\" \"subtle\" :warning \"green\" :info :error :magenta :subtle}`
       - Optional.
       - The color of the border, or gutter, depending on the value of `:theme`.
   
   * **`:theme`**
       - `#{\"boxed\" \"sideline\" :boxed :sideline \"gutter\" :sandwich \"simple\" :gutter}`
       - Optional.
       - Defaults to `:sideline`.
       - Name of callout layout template.
   
   * **`:label`**
       - `any?`
       - Optional.
       - Labels the callout. 
          In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively.
   
   * **`:side-label`**
       - `any?`
       - Optional.
       - Side label to the the callout label. 
          In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively.
   
   * **`:label-theme`**
       - `#{:simple \"marquee\" :marquee \"simple\"}`
       - Optional.
       - Defaults to `:simple`.
       - Name of label flavor.
   
   * **`:padding-block`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at top and bottom, inside callout.
   
   * **`:padding-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at top, inside callout.
   
   * **`:padding-bottom`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of padding (in newlines) at bottom, inside callout.
   
   * **`:padding-inline`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of padding (in blank character spaces) at left and right, inside callout.
   
   * **`:padding-left`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of padding (in blank character spaces) at left, inside callout.
   
   * **`:margin-top`**
       - `int?`
       - Optional.
       - Defaults to `1`.
       - Amount of margin (in newlines) at top, outside callout. Only applies to terminal emulator printing.
   
   * **`:margin-bottom`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in newlines) at bottom, outside callout. Only applies to terminal emulator printing.
   
   * **`:margin-left`**
       - `int?`
       - Optional.
       - Defaults to `0`.
       - Amount of margin (in blank character spaces) at left, outside callout.
   
   * **`:data?`**
       - `boolean?`
       - Optional.
       - Returns a data representation of result instead of printing it.
   
   * **`:header-padding-left`**
       - `int?`
       - Optional.
       - Defaults to `2`.
       - Amount of left margin (in character spaces) for the label, in the callout header
   
   * **`:header-gap`**
       - `int?`
       - Optional.
       - Defaults to `5`.
       - The gap (in character spaces) the label and the optional side lable, in the callout header.
   
   * **`:border-style`**
       - `#{:double :solid \"solid\" \"double\"}`
       - Optional.
       - Defaults to `:solid`.
       - The style of box-drawing character used.
   
   * **`:border-weight`**
       - `#{:bold \"normal\" :normal \"bold\"}`
       - Optional.
       - Defaults to `:normal`.
       - The weight of box-drawing character used. Applies only to `:border-style` of `:solid`
   
   * **`:border-shape`**
       - `#{\"sharp\" :round :sharp \"round\"}`
       - Optional.
       - Defaults to `:sharp`.
       - The corner shape of the borders, either sharp or round. Only applies when `:border-style` is `:solid` AND `:border-weight` is `:normal`
   
   * **`:border-notches?`**
       - `boolean?`
       - Optional.
       - Defaults to `:false`.
       - Only applies to `:sandwich` callout theme. Will use top-left-corner and bottom-right-corner box-drawing chars for first character of header and footer borders.
   
   * **`:border-weight`**
       - `#{:bold \"normal\" :normal \"bold\"}`
       - Optional.
       - Defaults to `:normal`.
       - The style of box-drawing character used.
   
   * **`:width`**
       - `pos-int?`
       - Optional.
       - Width of the box in number of chars, aka columns in terminal. If not set, will be the width of the terminal. If terminal width cannot be detected, will fallback to 80.
   
   * **`:min-width`**
       - `pos-int?`
       - Optional.
       - Min width of box in number of chars, aka columns in terminal. Overridden by the `:width` value, if set.
   
   * **`:border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing
   
   * **`:vertical-border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing character, for the vertical borders.
   
   * **`:horizontal-border-char`**
       - `string?`
       - Optional.
       - A char that will override the default box-drawing character, for the horizontal borders.
   
   * **`:box-drawing-style`**
       - `#{:bold :thin-round :double \"thin\" :thin \"thin-round\" \"double\" \"bold\"}`
       - Optional.
       - The style of box-drawing character used.
   
   * **`:max-width`**
       - `pos-int?`
       - Optional.
       - Max width of box in number of chars, aka columns in terminal. Overridden by the `:width` value, if set."
  {:desc     ["Prints a message to the console with a block-based coloring motif."
              "Returns nil.\n\n"

              "If the `:data?` option is set to `true`, it does not print anything, and"
              "returns a data representation of the formatting and styling.\n\n"

              "Callout prints a colored bounding border in the inline start position."
              "The color of the border is determined by the value of the `:type` option, or"
              "the `:colorway` option. The characteristics of this border are controlled by"
              "the `:theme` option.\n\n"

              "For callouts of the type `:error`, `:warning`, or `:info`, a label is"
              "printed in the block start postion. If a :type option is set, the label"
              "string will default to an uppercased version of that string, e.g."
              "{:type :INFO} => \"INFO\". If a `:label` option is supplied, that value is"
              "used instead. When you want to omit label for callouts of the type `:error`,"
              "`:warning`, or `:info`, you must explicitly set the :label option to an empty"
              "string.\n\n"

              "The amount of vertical padding (in number of lines) within the bounds of the"
              "message body can be controlled the `padding-top` and `padding-bottom` options."
              "The amount of space (in number of lines) above and below the message block"
              "can be controlled the `margin-top` and `margin-bottom` options.\n\n"

              "If two arguments are provided, the first should be a map of valid options."]

   :examples [{:desc  "Example call with all of the options"
               :forms "(callout {:type                   :error            ; :warning, :info
                      |          ;; :colorway            :purple           ; <- any bling palette color, overrides :type
                      |          ;; :label               \"My label\"      ; overrides label assigned by :type
                      |          :side-label             \"My side label\" ; must have a :label if you want a :side-label        
                      |          :theme                  :sideline         ; :sideline :sandwich :gutter
                      |          :label-theme            :simple           ; :simple :marquee
                      |          ;; :padding-block       0                 
                      |          ;; :padding-top         0                 
                      |          ;; :padding-bottom      0                 
                      |          ;; :padding-inline      2                 
                      |          ;; :padding-left        2                 
                      |          ;; :padding-right       0                 
                      |          ;; :margin-top          1                 
                      |          ;; :margin-botom        0                 
                      |          ;; :margin-left         0                 
                      |          ;; :data?               true              ; <- just returns string, no printing
                      |          
                      |          :border-shape           :sharp            ; :round
                      |          :border-style           :solid            ; :double
                      |          :border-weight          :normal           ; :bold
                      |          :width                  40
                      |          :min-width              40
                      |          
                      |          ;; --- The options below exclusive to :theme of :boxed ---------------
                      |          ;; :border-char            \"*\"
                      |          ;; :vertical-border-char   \"**\"
                      |          ;; :max-width              100
                      |          ;; :padding-block          1
                      |          ;; :padding-inline         2
                      |          }
                      | (bling [:bold (str \"Line 1\" \"\\n\" \"Line 2\")])"}
              {}]
   :options  [:map
              [:type
               {:optional true
                :desc     ["Will set the label text (unless provided via `:label`). Will also set the `:colorway`, and override any provided `:colorway` value."]}
               [:enum :error "error" :warning "warning" :info "info"]]

              [:colorway
               {:optional true
                :desc     ["The color of the border, or gutter, depending on the value of `:theme`."]}
               [:enum :error "error" :warning "warning" :info "info" :positive "positive" :subtle "subtle" :magenta "magenta" :green "green" :negative "negative" :neutral "neutral"]]

              [:theme
               {:optional true
                :default  :sideline
                :desc     ["Name of callout layout template."]}
               [:enum :sideline "sideline" :sandwich "simple" :gutter "gutter" :boxed "boxed"]]

              [:label
               {:optional true
                :desc     ["Labels the callout."
                           "\n"
                           "In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively."]}
               :any]

              [:side-label
               {:optional true
                :desc     ["Side label to the the callout label."
                           "\n"
                           "In the case of a callout `:type` of `:warning`, `:error`, or `:info`, the value of the label will default to `WARNING`, `ERROR`, or `INFO`, respectively."]}
               :any]

              [:label-theme
               {:optional true
                :default  :simple
                :desc     ["Name of label flavor."]}
               [:enum :marquee "marquee" :simple "simple"]]

              [:padding-block
               {:optional true
                :default  1
                :desc     ["Amount of padding (in newlines) at top and bottom, inside callout."]}
               :int]

              [:padding-top
               {:optional true
                :default  1
                :desc     ["Amount of padding (in newlines) at top, inside callout."]}
               :int]

              [:padding-bottom
               {:optional true
                :default  1
                :desc     ["Amount of padding (in newlines) at bottom, inside callout."]}
               :int]

              [:padding-inline
               {:optional true
                :default  2
                :desc     ["Amount of padding (in blank character spaces) at left and right, inside callout."]}
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

              [:data?
               {:optional true
                :desc     ["Returns a data representation of result instead of printing it."]}
               :boolean]

              [:header-padding-left
               {:optional true
                :default  2
                :desc     ["Amount of left margin (in character spaces) for the label, in the callout header"]}
               :int]

              [:header-gap
               {:optional true
                :default  5
                :desc     ["The gap (in character spaces) the label and the optional side lable, in the callout header."]}
               :int]

              [:border-style
               {:optional true
                :default  :solid
                :desc     ["The style of box-drawing character used."]}
               [:enum :solid "solid" :double "double"]]

              [:border-weight
               {:optional true
                :default  :normal
                :desc     ["The weight of box-drawing character used. Applies only to `:border-style` of `:solid`"]}
               [:enum :normal "normal" :bold "bold"]]

              [:border-shape
               {:optional true
                :default  :sharp
                :desc     ["The corner shape of the borders, either sharp or round. Only applies when `:border-style` is `:solid` AND `:border-weight` is `:normal`"]}
               [:enum :sharp "sharp" :round "round"]]

              [:border-notches?
               {:optional true
                :default  :false
                :theme    :sandwich
                :desc     ["Only applies to `:sandwich` callout theme. Will use top-left-corner and bottom-right-corner box-drawing chars for first character of header and footer borders."]}
               :boolean]

              [:border-weight
               {:optional true
                :theme    :sandwich
                :default  :normal
                :desc     ["The style of box-drawing character used."]}
               [:enum :normal "normal" :bold "bold"]]

              [:width
               {:optional true
                :theme    [:sandwich :boxed]
                :desc     ["Width of the box in number of chars, aka columns in"
                           "terminal. If not set, will be the width of the terminal."
                           "If terminal width cannot be detected, will fallback to 80."]}
               :pos-int]

              [:min-width
               {:optional true
                :theme    [:sandwich :boxed]
                :desc     ["Min width of box in number of chars, aka columns in"
                           "terminal. Overridden by the `:width` value, if set."]}
               :pos-int]

              ;; boxed ---------------------------------------------------------

              [:border-char
               {:optional true
                :theme    :boxed
                :desc     "A char that will override the default box-drawing"}
               :string]

              [:vertical-border-char
               {:optional true
                :theme    :boxed
                :desc     "A char that will override the default box-drawing character, for the vertical borders."}
               :string]

              [:horizontal-border-char
               {:optional true
                :theme    :boxed
                :desc     "A char that will override the default box-drawing character, for the horizontal borders."}
               :string]

              [:box-drawing-style
               {:optional true
                :theme    :boxed
                :desc     ["The style of box-drawing character used."]}
               [:enum :thin-round "thin-round" :thin "thin" :bold "bold" :double "double"]]

              [:max-width
               {:optional true
                :theme    :boxed
                :desc     ["Max width of box in number of chars, aka columns in"
                           "terminal. Overridden by the `:width` value, if set."]}
               :pos-int]]}
  [x & args]
  (if (empty? args)
    (callout {} (bling.hifi/hifi x))

    (let [opts          (maybe-> x map?)
          callout-opts  (some-> opts callout-opts*)
          callout-opts+ (merge {:value (string/join "" args)}
                               opts
                               callout-opts)]
      {:theme               (keyword (:theme callout-opts))
       :label-theme         (keyword (:label-theme callout-opts))
       :header-padding-left [(:header-padding-left opts)
                             '=>
                             (:header-padding-left callout-opts)]
       :padding-left        [(:padding-left opts)
                             '=>
                             (:padding-left callout-opts)]}
      #?(:cljs
         (if node?
           (callout* callout-opts+)
           (-> (callout* (assoc callout-opts+ :browser-dev-console? true))
               print-to-browser-dev-console))

         :clj
         (callout* callout-opts+)))))

#_{:marquee-label {:line-1 {:margin-str " "
                            :header-padding-left-str "  "
                            :first-char '(resolve-char based on border style)}
                   :line-2 {:margin-str " "
                            :header-padding-left-str '(resolve str based on theme and border-style)
                            :first-char '(resolve-char based on border style and theme and header-padding-inline)}
                   :line-3 {:margin-str " "
                            :header-padding-left-str '(resolve str based on theme and border-style)
                            :first-char '(resolve-char based on border style)}}}

;;   BBBBBBBBBBBBBBBBB      LLLLLLLLLLL                       GGGGGGGGGGGGG
;;   B::::::::::::::::B     L:::::::::L                    GGG::::::::::::G
;;   B::::::BBBBBB:::::B    L:::::::::L                  GG:::::::::::::::G
;;   BB:::::B     B:::::B   LL:::::::LL                 G:::::GGGGGGGG::::G
;;     B::::B     B:::::B     L:::::L                  G:::::G       GGGGGG
;;     B::::B     B:::::B     L:::::L                 G:::::G              
;;     B::::BBBBBB:::::B      L:::::L                 G:::::G              
;;     B:::::::::::::BB       L:::::L                 G:::::G    GGGGGGGGGG
;;     B::::BBBBBB:::::B      L:::::L                 G:::::G    G::::::::G
;;     B::::B     B:::::B     L:::::L                 G:::::G    GGGGG::::G
;;     B::::B     B:::::B     L:::::L                 G:::::G        G::::G
;;     B::::B     B:::::B     L:::::L         LLLLLL   G:::::G       G::::G
;;   BB:::::BBBBBB::::::B   LL:::::::LLLLLLLLL:::::L    G:::::GGGGGGGG::::G
;;   B:::::::::::::::::B    L::::::::::::::::::::::L     GG:::::::::::::::G
;;   B::::::::::::::::B     L::::::::::::::::::::::L       GGG::::::GGG:::G
;;   BBBBBBBBBBBBBBBBB      LLLLLLLLLLLLLLLLLLLLLLLL          GGGGGG   GGGG

;; Enriched text public fns and helpers  

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
  ;; (!? :js (-> coll bling-data* :tagged))
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
