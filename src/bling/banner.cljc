(ns bling.banner
  (:require
   [bling.macros :refer [let-map keyed ?]]
   [bling.fonts :refer [fonts]]
   [clojure.pprint :refer [pprint]]
   [bling.defs :as defs]
   [clojure.string :as string]))

(defn- maybe [x pred]
  (when (if (set? pred)
          (contains? pred x)
          (pred x))
    x))

(defn print-warning! [lns]
  (println
   (apply str 
          (str "\n"
               defs/orange-tag-open
               defs/gutter-char-lower-seven-eighths
               defs/sgr-tag-close
               "  "
               defs/bold-tag-open "WARNING" defs/sgr-tag-close
               "\n")
          (mapv 
           #(str defs/orange-tag-open
                 defs/gutter-char
                 defs/sgr-tag-close
                 "  "
                 % 
                 "\n")
           lns))))

(defn- maybe-wrap-in-double-quotes [x]
  (if (string? x) 
    (str "\"" x "\"")
    (str x)))

(defn invalid-banner-opt-warning!
  [{:keys [option-key 
           option-value
           valid-desc
           valid-colors
           valid-examples
           default-val-msg]}]
  (let [option-key   (str option-key)
        option-value (maybe-wrap-in-double-quotes option-value)]
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
             option-value
             defs/sgr-tag-close)
       (str #_(apply str (repeat (+ 1 (count option-key)) " "))
        defs/orange-tag-open
            (apply str (repeat (count option-value) "^"))
            defs/sgr-tag-close)
       ""
       (str "The value for the " option-key " option must be:")
       ]

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
         (str "Valid gradient color pairs for " option-key " option:")])
      valid-colors
      default-val-msg
      ))))


(defn invalid-gradient-opt-warning! [gradient gradient-pairs-map]
  (invalid-banner-opt-warning! 
   {:option-key     :gradient
    :option-value   gradient
    :valid-desc     "A valid css linear-gradient string."
    :valid-examples (map-indexed
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
                     gradient-pairs-map)
    :valid-colors   (map (fn [[k v]] 
                           (str "\""
                                (name k)
                                ", " 
                                (name v)
                                "\"")) 
                         gradient-pairs-map)
    }))


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
        weight          (when (and (not disable-font-weights?)
                                   (contains? #{"bold" :bold} font-weight))
                          "1")
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

(defn color-rows [grd n]
  (let [grd-count (count grd)]
    (cond
      (< n grd-count)
      (trim-coll-sides grd (- grd-count n))

      (> n grd-count)
      (let [rm        (rem n grd-count)
            muliplier (/ n grd-count)]
        (if (zero? rm)
          (inflate-color-coll grd muliplier)
          (if (> rm (dec (Math/round (double (/ grd-count 2)))))
            (let [multiplier (Math/round (Math/ceil (double muliplier)))
                  coll       (inflate-color-coll grd multiplier)]
              (trim-coll-sides coll (- (count coll) n)))
            (inflate-coll-sides grd rm))))
      :else
      grd)))

(defn sgr-gradient
  [char-height rows sgr-gradient-range]
  (let [row-colors  (color-rows sgr-gradient-range char-height)]
    (into []
          (map-indexed
           (fn [i s]
             (str (m->sgr {:color (nth row-colors i)})
                  s
                  "\033[0;m"))
           rows))))

(def gradient-pairs-map
  {:green  :blue
   :yellow :purple
   :red    :magenta
   :orange :purple})

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

(defn shade-map [k i n]
    (let [prefix (nth shade-names i)
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
  [base-colors? soft? c]
  (cond
    (and base-colors? soft?)
    (str defs/bling-theme "-" c)
    base-colors?
    (str (case defs/bling-theme
           "light" "dark"
           "dark" "light"
           "medium")
         "-"
         c)
    :else c))

(defn gradient-map
  "Expects a string as first argument representing a linear-gradient in css syntax:
   \"to bottom, yellow, purple\"

   Expects a boolean as second argument. If the user has BLING_THEME env
   var set, the darker or lighter variant of the base color will be used,
   depending on the value of BLING_THEME, which corresponds to the background
   terminal. The purpose of this is to lower the contrast for a more subtle look.

   Returns a map:
   {:gradient-range       '(40 41 42 43 44 45) ;; <- range of sgr codes for color
    :gradient-orientation :vertical
    :vertical-gradient?   true
    :horizontal-gradient? false
   }"
  [s soft?]
  (when-let [[direction c1 c2]
             (some-> s
                     (maybe string?)
                     (string/split #", "))]
    (if (and (contains? gradient-directions direction)
             (contains? gradient-colors-set c1)
             (contains? gradient-colors-set c2)
             (let [prefix-c1 (re-find #"^light-|^dark-" c1)
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
      (let [base-colors? 
            (and (contains? gradient-pairs-all-base-colors-set c1)
                 (contains? gradient-pairs-all-base-colors-set c2))
            c1  (resolve-base-gradient-color-for-theme base-colors? soft? c1)
            c2  (resolve-base-gradient-color-for-theme base-colors? soft? c2)
            k   (case direction "to top" [c2 c1] [c1 c2])
            gr  (get gradient-ranges k)
            go  (get gradient-directions direction :vertical)
            vg? (and gr (= go :vertical))
            hg? (and gr (= go :horizontal))]
        {:gradient-range       gr
         :gradient-orientation go
         :vertical-gradient?   vg?
         :horizontal-gradient? hg?})
      (invalid-gradient-opt-warning! s gradient-pairs-map))))

(defn horizontal-gradient [composed gr]
  (let [width      (-> composed first count)
        col-colors (color-rows gr width)]
    (mapv #(string/join
            (map-indexed
             (fn [i s]
               (str (m->sgr {:color (nth col-colors i)})
                    s
                    "\033[0;m"))
             %))
          composed)))

(defn- composed [letter-spacing rest-chars first-char]
  (map-indexed 
   (fn [char-band-index char-band]
     (reduce (fn [acc nth-char]
               (str acc
                    (when (pos-int? letter-spacing)
                      (string/join "" (repeat letter-spacing " ")))
                    (nth-char char-band-index)))
             char-band
             rest-chars))
   first-char))

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
       (if (pos-int? v)
         (assoc acc k v)
         (do 
           (when v
             (invalid-banner-opt-warning!
              {:option-key   k
               :option-value v
               :valid-desc   ["A positive integer, reprenting the number of "
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

(defn banner 
  [{:keys [font
           text
           letter-spacing
           gradient
           soft?]
    :as opts}]
  (let [valid-text?  (and (string? text)
                          (not (string/blank? text)))
        default-font (-> fonts var meta :default)]

    (when-not valid-text?
      (invalid-banner-opt-warning! 
       {:option-key     :text
        :option-value   text
        :valid-desc     "A non-blank string"}))

    (when-not (contains? fonts font)
      (invalid-banner-opt-warning! 
       {:option-key      :font
        :option-value    font
        :valid-desc      "One of the Figlet fonts that ships with Bling."
        :valid-examples  (map #(str "\"" % "\"") (keys fonts))
        :default-val-msg [""
                          "The default font "
                          default-font 
                          " will be used"]}))
    (when valid-text?
     (let [{:keys [char-height]
            :as   font-map}
           (get fonts
                font 
                (get fonts default-font))

           text-str-chars 
           (mapv #(string/split-lines (get-in font-map [:chars-array-map %] ""))
                 (string/split text #""))

           {:keys [gradient-range 
                   vertical-gradient?
                   horizontal-gradient?]}
           (gradient-map gradient (true? soft?))

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
             (horizontal-gradient composed gradient-range) 
             composed)

           margins
           (banner-margin-map opts)]

       (->> composed
            (maybe-with-inline-margins margins)
            (maybe-with-block-margins margins))
       ))))
