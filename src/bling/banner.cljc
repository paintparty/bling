(ns bling.banner
  (:require
   [bling.macros :refer [keyed ? start-dbg! stop-dbg! nth-not-found]]
   [bling.fonts]
   [bling.util :as util :refer [sjr]]
   [clojure.pprint :refer [pprint]]
   [bling.defs :as defs]
   [clojure.string :as string]))

(defn- maybe [x pred]
  (when (if (set? pred)
          (contains? pred x)
          (pred x))
    x))

(defn print-warning! [lns]

  (let [[border-char-top
         border-char
         border-char-bottom]
        (get defs/internal-warning-border-chars
             defs/internal-warning-border-style)]
    (println
     (str (apply str 
                 (str "\n"
                      defs/orange-tag-open
                      border-char-top
                      defs/sgr-tag-close
                      "  "
                      defs/bold-tag-open "WARNING" defs/sgr-tag-close
                      "\n")
                 (mapv 
                  #(str defs/orange-tag-open
                        border-char
                        defs/sgr-tag-close
                        "  "
                        % 
                        "\n")
                  lns)
                 )
          (when (contains? #{:sideline-bold :sideline}
                           defs/internal-warning-border-style)
            (str defs/orange-tag-open
                 border-char-bottom
                 defs/sgr-tag-close))))))

(defn- maybe-wrap-in-double-quotes [x option-value-is-string?]
  (if option-value-is-string? 
    (str "\"" x "\"")
    x))

(defn invalid-banner-opt-warning!
  [{:keys [option-key 
           option-value
           valid-desc
           valid-colors
           valid-examples
           default-val-msg]}]
  (let [option-key              (str option-key)
        option-value-is-string? (string? option-value)
        option-value-shortened  (util/shortened option-value 33)
        option-value-wrapped    (maybe-wrap-in-double-quotes
                                 option-value-shortened
                                 option-value-is-string?)]
    (print-warning!
     (concat
      [""
       "bling.banner/banner*"
       ""
       (str "Invalid value for the "
            defs/bold-tag-open option-key defs/sgr-tag-close
            " option:")
       ""
       (str  defs/bold-tag-open
             option-value-wrapped
             defs/sgr-tag-close)
       (str #_(sjr (+ 1 (count option-key)) " ")
            defs/orange-tag-open
            (sjr (count option-value-wrapped) "^")
            defs/sgr-tag-close)]

      (when valid-desc
        [""
         (str "The value for the " option-key " option must be:")])

      (cond (coll? valid-desc)
            valid-desc
            (string? valid-desc)
            [valid-desc])

      (when valid-examples 
        [""
         "Examples:"])

      valid-examples

      (when valid-colors
        [""
         (str "Valid color pairs for " option-key " option:")])
      valid-colors
      default-val-msg
      ))))





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
           disable-italics?
           disable-font-weights?]
    :as   m}]
  (let [fgc             (x->sgr fgc* :fg)
        bgc             (x->sgr bgc* :bg)
        italic          (when (and (not disable-italics?)
                                   (contains? #{"italic" :italic} font-style))
                          "3")
        weight          (when (contains? #{:bold "bold"} font-weight) "1")
        ;; text-decoration (sgr-text-decoration m)
        ret             (str "\033["
                             (string/join ";"
                                          (remove nil?
                                                  [italic
                                                   fgc
                                                   weight
                                                   bgc
                                                  ;;  text-decoration
                                                   ]))
                             "m")]

    ret))

(defn trim-coll-sides [coll n]
  (reduce 
   (fn [acc i]
     (if (odd? i)
       (rest acc)
       (drop-last acc)))
   coll 
   (range n)))

(defn inflate-coll-sides [coll n]
  (into []
        (reduce 
         (fn [acc i]
           (if (odd? i)
             (concat acc [(last acc)])
             (concat [(first acc)] acc)))
         coll 
         (range n))))

(defn inflate-color-coll [coll n]
  (reduce (fn [acc v] (apply conj acc (repeat n v))) [] coll))

(defn color-rows [gr cells]
  (let [gr-count
        (count gr)

        debug?   
        (start-dbg! #_color-rows) ;; Turn on/off, ignore/unignore color-rows arg

        dbg
        (if debug? #(? :- %) (fn [_] nil))

        ret
        (cond
          (< cells gr-count)
          (do (dbg "Number of cells is less than number of colors in gradient range")
              (dbg (keyed [cells gr-count]))
              (trim-coll-sides gr (- gr-count cells)))

          (> cells gr-count)
          (do 
            (dbg "Number of cells is more than number of colors in gradient range")
            (when debug? (? :- (keyed [cells gr-count])))
            (let [rm                   (rem cells gr-count)
                  multiplier           (/ cells gr-count)
                  multiplier-ceil-int  (Math/round (Math/ceil (double multiplier)))
                  multiplier-floor-int (Math/round (Math/floor (double multiplier)))]
              (when debug? (? :- (keyed [rm multiplier multiplier-ceil-int multiplier-floor-int])))

              (if (zero? rm)
                (do (dbg "Remainder is 0. Inflating the gradient range")
                    (inflate-color-coll gr multiplier-ceil-int))
                (if (> rm (dec (Math/round (double (/ gr-count 2)))))

                  (do (dbg "Remainder is greater than half the gradient-range.")
                      (let [coll (inflate-color-coll gr multiplier-ceil-int)
                            diff (- (count coll) cells)]
                        (dbg (str "Inflating the gradient range by repeating each color " multiplier " times")) 
                        (dbg (str "Then trimming the gradient range by alternately removing vals from the coll sides, " diff " times"))
                        (trim-coll-sides coll diff)))

                  (do (dbg "Remainder is less than half the gradient-range.")
                      (dbg (str "Inflating the gradient range by repeating each color " multiplier " times"))
                      (dbg (str "Then inflating the gradient range incrementally by alternately adding vals to the coll sides, " rm " times"))
                      (inflate-coll-sides (inflate-color-coll gr multiplier-floor-int)
                                          rm))))))
          :else
          (do (dbg "Number of cells is the same as the number of colors in gradient range. Returning the gradient range as-is.")
              gr))]
    (stop-dbg! debug?)
    ret))

(defn sgr-gradient
  [char-height rows sgr-gradient-range]
  (let [row-colors  (color-rows sgr-gradient-range char-height)]
    (into []
          (map-indexed
           (fn [i s]
             (str (m->sgr {:color (nth row-colors i (nth-not-found))})
                  s
                  "\033[0;m"))
           rows))))

(def gradient-pairs-map
  {:green  :blue
   :yellow :purple
   :red    :magenta
   :orange :purple})

(def gradient-pairs-set
  (reduce 
   (fn [acc [k v]]
     (conj acc #{(name k) (name v)}))
   #{}
   gradient-pairs-map))

(def gradient-pairs-all-base-colors-set
  (reduce-kv (fn [acc k v]
               (conj acc (name k) (name v))) 
             #{}
             gradient-pairs-map))


(def gradient-points
  {:green [22 40 82] ;; to blue
   :yellow [100 136 178] ;; to purple
   :red [124 160 196] ;; to magenta
   :orange [130 172 214] ;; to purple
   })


(def shade-names ["dark" "medium" "light"])

;; (def shade-names ["dark" "medium-dark" "medium" "medium-light" "light"])

(defn shade-map [k i n]
    (let [prefix (nth shade-names i (nth-not-found))
          range  (range n (+ n 6))
          c1     (str prefix "-" (name k))
          c2     (str prefix "-" (-> k gradient-pairs-map name))]
      (concat [[c1 c2] range
               [c2 c1] (reverse range)]
              (when (= "medium" prefix)
                (let [c1 (name k)
                      c2 (-> k gradient-pairs-map name)]
                  [[c1 c2] range
                   [c2 c1] (reverse range)])))))

(def gradient-ranges
  (apply array-map
         (reduce-kv 
          (fn [acc k shades]
            (let [shade-maps
                  (map-indexed (partial shade-map k) shades)]
              (apply conj acc (apply concat shade-maps))))
          []
          gradient-points)))

(def gradient-colors-set
  (reduce-kv
    (fn [acc k _] (apply conj acc k))
    #{}
    gradient-ranges))

(def gradient-directions
  {"to bottom" :vertical
   "to top"    :vertical
   "to right"  :horizontal
   "to left"   :horizontal})

(defn resolve-base-gradient-color-for-theme 
  [opts c]
  (let [prefix (case (:contrast opts)
                 ;; :super-soft

                 :soft
                 defs/bling-theme

                 :medium
                 "medium"

                 ;; :super-hard

                 ;; covers :hard 
                 (case defs/bling-theme
                   "light" "dark"
                   "dark" "light"
                   "medium"))]
    (str prefix "-" c)))

(defn invalid-gradient-opt-warning!
  [{:keys [gradient
           gradient-pairs-map
           show-examples?]}]
  (invalid-banner-opt-warning! 
   {:option-key     :gradient
    :option-value   gradient
    :valid-desc     (when show-examples?
                      "A valid css linear-gradient string.")
    :valid-examples (when show-examples?
                      (map-indexed
                       (fn [i [k v]] 
                         (str "\""
                              (if (odd? i)
                                "to bottom"
                                "to top")
                              ", "
                              (name k)
                              ", " 
                              (name v)
                              "\"")) 
                       gradient-pairs-map))
    :valid-colors   (map (fn [[k v]] 
                           (str "\""
                                (name k)
                                ", " 
                                (name v)
                                "\"")) 
                         gradient-pairs-map)}))
(defn gradient-map
  "Expects a string as first argument representing a linear-gradient in standard
   css syntax:
   \"to bottom, yellow, purple\"

   Expects a boolean as second argument. If the user has BLING_THEME env
   var set, the darker or lighter variant of the base color will be used,
   depending on the value of BLING_THEME, which corresponds to the background
   terminal. The purpose is to lower the contrast for a more subtle look.

   Returns a map:
   {:gradient-range       '(40 41 42 43 44 45) ;; <- range of sgr codes
    :gradient-orientation :vertical
    :vertical-gradient?   true
    :horizontal-gradient? false
   }"
  [s opts]
  (when-let [[direction c1 c2]
             (some-> s
                     (maybe string?)
                     (string/split #", "))]
    (let [valid-gradient-pairs? (contains? gradient-pairs-set #{c1 c2})]
          (if (and (contains? gradient-directions direction)
                   valid-gradient-pairs?
                   #_(let [prefix-c1 (re-find #"^light-|^dark-" c1)
                           prefix-c2 (re-find #"^light-|^dark-" c2)]
                       (if (= prefix-c1 prefix-c2)
                         true
                         (print-warning! 
                          ['bling.banner/gradient-map
                           ""
                           (str "A gradient from "
                                defs/bold-tag-open "\"" c1 "\"" defs/sgr-tag-close
                                " to "
                                defs/bold-tag-open "\"" c2 "\"" defs/sgr-tag-close
                                " is not valid") ]) )))
            (let [
                  c1  (resolve-base-gradient-color-for-theme opts c1)
                  c2  (resolve-base-gradient-color-for-theme opts c2)
                  k   (case direction "to top" [c2 c1] [c1 c2])
                  gr  (get gradient-ranges k)
                  go  (get gradient-directions direction :vertical)
                  vg? (and gr (= go :vertical))
                  hg? (and gr (= go :horizontal))]
              {:gradient-range       gr
               :gradient-orientation go
               :vertical-gradient?   vg?
               :horizontal-gradient? hg?})
            (invalid-gradient-opt-warning!
             {:gradient           s
              :gradient-pairs-map gradient-pairs-map
              :show-examples?     valid-gradient-pairs?})))))

(defn horizontal-gradient [composed gr opts]
  (let [width      (-> composed first count)
        col-colors (color-rows gr width)]

    (keyed [width col-colors gr])

    (mapv #(string/join
            (map-indexed
             (fn [i s]/
               (str (m->sgr {:color       (nth col-colors i (nth-not-found))
                             :font-weight (:font-weight opts)})
                    s
                    "\033[0;m"))
             (do (count %)
                 %)))
          composed)))

;; TODO FIGURE OUT wtf is going on here.
(defn- composed 
  [letter-spacing rest-chars first-char]

  ;; (? [(count first-char) (mapv count rest-chars)])
  ;; (pprint first-char)
  ;; (println (string/join "\n" first-char ))

  ;; (pprint (first rest-chars))
  ;; (println (string/join "\n" (first rest-chars) ))

  (let [debug?   
        (start-dbg! #_composed) ;; Turn on/off, ignore/unignore first arg

        dbg
        (if debug? #(? :- %) (fn [_] nil))

        ret (map-indexed 
             (fn [char-band-index char-band]
               (reduce (fn [acc nth-char]
                         (str acc
                              (when (pos-int? letter-spacing)
                                (sjr letter-spacing " "))
                              (nth nth-char char-band-index (nth-not-found))))
                       char-band
                       rest-chars))
             first-char)]
    (stop-dbg! debug?)
    ret))

(defn- banner-margin
  [kw n]
  (some-> n
          (repeat (case kw :inline " " :block "\n" ""))
          string/join))

;; (defn- validate-option [kw v fallback]
;;   (if (pred v)
;;     v
;;     (do
;;       fallback)))

(defn- banner-margin-map
  [m]
  (reduce 
   (fn [acc k]
     (let [v (get m k)]
       (if (or (pos-int? v)
               (some-> v zero?))
         (assoc acc k v)
         (do 
           (when v
             (invalid-banner-opt-warning!
              {:option-key   k
               :option-value v
               :valid-desc   ["A non-negative integer, reprenting the number of "
                              (case k
                                :margin-top
                                "blank rows above the banner"
                                :margin-bottom
                                "blank rows below the banner"
                                :margin-right
                                "blank columns to the right of the banner"
                                ;;:margin-left (default)
                                "blank columns to the left of the banner"
                                )]}))
           acc))))
   {}
   [:margin-left
    :margin-right
    :margin-top
    :margin-bottom]))

(defn- maybe-with-inline-margins
  [{:keys [margin-left margin-right]} vc]
  (if (or margin-left margin-right)
    (let [f (partial banner-margin :inline)]
      (mapv #(str (f margin-left) % (f margin-left))
            vc))
    vc))

(defn- maybe-with-block-margins 
  [{:keys [margin-top margin-bottom]} vc]
  (let [s (string/join "\n" vc)
        f (partial banner-margin :block)]
    (if (or margin-top margin-bottom)
      (str (f margin-top) s (f margin-bottom))
      s)))

(defn- char-map-or-space-char-map 
  [{:keys [chars-array-map]} char-str]
  (get chars-array-map
       char-str
       (get chars-array-map
            " "
            "")))

(defn- missing-char-str [width s]
  (case width
    0 ""
    1 s
    2 (str " " s)
    3 (str " " s " ")
    (if (odd? width)
      (let [pad (sjr (/ (dec width) 2) " ")]
        (str pad s pad))
      (let [pad (sjr (/ width 2) " ")]
        (str pad s (subs pad 1))))))

(defn- replacement-char-vec [font-map s]
  (into []
        (let [{:keys [width height]} 
              (char-map-or-space-char-map font-map " ")

              s
              (missing-char-str width s)

              band-str
              (sjr width " ")]

          (if (odd? height)
            (let [block-pad (repeat (/ (dec height) 2) band-str)]
              (concat block-pad
                      [s]
                      block-pad))
            (concat (repeat (dec (/ height 2)) band-str)
                    [s]
                    (repeat (/ height 2) band-str))))))


(defn banner-str-chars
  [font-map char-height text display-missing-chars?]
  (let [debug?   
        (start-dbg! #_banner-str-chars) ;; Turn on/off, ignore/unignore first arg 
        
        dbg
        (if debug? #(? :- %) (fn [_] nil))

        ret 
        (into []
              (keep #(let [{:keys [bands height] :as char-map}
                           (char-map-or-space-char-map font-map %)

                           char-height-is-less-than-font-height?
                           (some->> height (> char-height))
                           
                           every-band-is-an-empty-string?
                           (every? (fn [s] (= "" s)) bands)
                           
                           skip?
                           (or char-height-is-less-than-font-height?
                               every-band-is-an-empty-string?)

                           display-substitute?
                           (and display-missing-chars? skip?)]
                       (when (= (:character char-map) "\"")
                         (dbg (keyed [char-map
                                          char-height-is-less-than-font-height?
                                          every-band-is-an-empty-string?
                                          skip?
                                          display-substitute?])))
                       (if skip?
                         (when display-substitute?
                           (replacement-char-vec font-map %))
                         bands))
                    (string/split text #"")))]
        (stop-dbg! debug?)
        ret))


(defn banner 
  [{:keys [text
           letter-spacing
           gradient
           :dev/print-font!
           display-missing-chars?]
    :as opts
    :or {display-missing-chars? true}
    user-font :font}]
  (let [valid-text?  (and (string? text)
                          (not (string/blank? text)))
        default-font bling.fonts/ansi-shadow
        font         (or user-font default-font)
        valid-font?  true
        ;; font scheming checking for devving of fonts
        ;; valid-font?  (let [{:keys [widest-char
        ;;                            char-height
        ;;                            max-char-width
        ;;                            chars-array-map]}
        ;;                    font]
        ;;                (boolean (and (string? widest-char)
        ;;                              (= 1 (count widest-char))
        ;;                              (pos-int? char-height)
        ;;                              (pos-int? max-char-width)
        ;;                              (map? chars-array-map)
        ;;                              (seq chars-array-map))))
        ]

    (when-not valid-text?
      (invalid-banner-opt-warning! 
       {:option-key     :text
        :option-value   text
        :valid-desc     "A non-blank string"}))

    (when-not valid-font?
      (invalid-banner-opt-warning! 
       {:option-key      :font
        :option-value    user-font
        :valid-desc      "One of the Figlet fonts that ships with Bling."
        :valid-examples  (map #(str "bling.fonts/" % ) defs/banner-fonts-vec)
        :default-val-msg [""
                          (str "The default font "
                               "bling.fonts/" (:font-sym default-font)
                               " will be used")]}))

    ;; (when print-font! (pprint valid-font?))

    (when (and valid-font? valid-text?)
      (let [{:keys [char-height]}
            font

            text-str-chars
            (banner-str-chars font char-height text display-missing-chars?)

            {:keys [gradient-range 
                    vertical-gradient?
                    horizontal-gradient?]}
            (gradient-map gradient opts)


              ;; TODO - get this working
            
              ;; red-blue-range-level 
              ;; ;; for light theme it is 0-4
              ;; ;; for dark theme it is 1-5
              ;; 0
            
              ;; gradient-range
              ;; (map #(+ (* red-blue-range-level 6) %) '(21 56 91 126 161 196))
            
            first-char
            (if vertical-gradient?
              (sgr-gradient char-height (first text-str-chars) gradient-range)
              (first text-str-chars))

            rest-chars
            (if vertical-gradient?
              (mapv #(sgr-gradient char-height % gradient-range)
                    (rest text-str-chars))
              (rest text-str-chars))
            
            composed
            (composed letter-spacing rest-chars first-char)


            composed 
            (if horizontal-gradient?
              (horizontal-gradient composed gradient-range opts) 
              composed)

            margins
            (banner-margin-map opts)]
        (->> composed
             (maybe-with-inline-margins margins)
             (maybe-with-block-margins margins))
        ))))

