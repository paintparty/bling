(ns bling.banner
  (:require
   [bling.macros :refer [keyed start-dbg! stop-dbg! nth-not-found ?]]
   [fireworks.pp]
   [bling.util :as util :refer [sjr maybe]]
   [bling.defs :as defs]
   [clojure.string :as string]
   [bling.fonts.ansi-shadow :refer [ansi-shadow]]
   #?(:cljs [bling.js-env :refer [node?]])
   ))


;; Change to true for dev, for local debugging inside functions
#_(when false
  (require '[bling.macros :refer [?]])
  (defn ? [_ _]
    (println "Stub for dev debugging macro :: bling.banner/?. Comment me out")))



(defn- print-caught-exception! [fn-name]
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
                      defs/orange-tag-close
                      "  "
                      defs/bold-tag-open
                      "EXCEPTION (Caught) "
                      defs/sgr-tag-close
                      fn-name
                      "\n"))
          (when (contains? #{:sideline-bold :sideline}
                           defs/internal-warning-border-style)
            (str defs/orange-tag-open
                 border-char-bottom
                 defs/orange-tag-close))))))


(defn- print-warning! [lns]
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
                      defs/orange-tag-close
                      "  "
                      defs/bold-tag-open "WARNING" defs/sgr-tag-close
                      "\n")
                 (mapv 
                  #(str defs/orange-tag-open
                        border-char
                        defs/orange-tag-close
                        "  "
                        % 
                        "\n")
                  lns)
                 )
          (when (contains? #{:sideline-bold :sideline}
                           defs/internal-warning-border-style)
            (str defs/orange-tag-open
                 border-char-bottom
                 defs/orange-tag-close))))))

(defn- maybe-wrap-in-double-quotes [x option-value-is-string?]
  (cond (and option-value-is-string?
             (string/starts-with? x "\"")
             (string/ends-with? x "\""))
        x

        option-value-is-string? 
        (str "\"" x "\"")

        :else
        x))

(defn- invalid-banner-opt-warning!
  [{:keys [option-key 
           option-value
           valid-desc
           valid-colors
           valid-examples
           default-val-msg]}]
  (let [option-key              (str option-key)
        option-value-is-string? (string? option-value)
        option-value-shortened  (util/shortened option-value 33)
        option-value-wrapped    (if (nil? option-value)
                                  (str "nil")
                                  (maybe-wrap-in-double-quotes
                                   option-value-shortened
                                   option-value-is-string?))]
    (print-warning!
     (util/concatv
      [""
       "bling.banner/banner"
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
            defs/orange-tag-close)]

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
      default-val-msg))))

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

(defn- trim-coll-sides [coll n]
  (reduce 
   (fn [acc i]
     (if (odd? i)
       (rest acc)
       (drop-last acc)))
   coll 
   (range n)))

(defn- inflate-coll-sides [coll n]
  (into []
        (reduce 
         (fn [acc i]
           (if (odd? i)
             (util/concatv acc [(last acc)])
             (util/concatv [(first acc)] acc)))
         coll 
         (range n))))

(defn- inflate-color-coll [coll n]
  (reduce (fn [acc v] (apply conj acc (repeat n v))) [] coll))

(defn- color-cells
  [gr cells-int kw]
  (let [dec-cells-int?
        (and (= kw :rows) (> cells-int 1))

        cells-int
        (if dec-cells-int? (dec cells-int) cells-int)

        gr-count
        (count gr)

        debug?   
        (start-dbg! #_color-cells) ;; Turn on/off, ignore/unignore first arg

        dbg
        (if debug? #(? :- %) (fn [_] nil))

        _
        (dbg (str "Creating cells for " kw))

        ret
        (cond
          (< cells-int gr-count)
          (do (dbg "Number of cells-int is less than number of colors in gradient range")
              (dbg (keyed [cells-int gr-count]))
              (trim-coll-sides gr (- gr-count cells-int)))

          (> cells-int gr-count)
          (do 
            (dbg "Number of cells is more than number of colors in gradient range")
            (when debug? (keyed [cells-int gr-count]))
            (let [rm                   (rem cells-int gr-count)
                  multiplier           (/ cells-int gr-count)
                  multiplier-ceil-int  (Math/round (Math/ceil (double multiplier)))
                  multiplier-floor-int (Math/round (Math/floor (double multiplier)))]
              (when debug? 
                (keyed [rm
                        multiplier
                        multiplier-ceil-int
                        multiplier-floor-int]))
              (if (zero? rm)
                (do (dbg "Remainder is 0. Inflating the gradient range")
                    (inflate-color-coll gr multiplier-ceil-int))
                (if (> rm (dec (Math/round (double (/ gr-count 2)))))

                  (do (dbg "Remainder is greater than half the gradient-range.")
                      (let [coll (inflate-color-coll gr multiplier-ceil-int)
                            diff (- (count coll) cells-int)]
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
              gr))
              
        ret
        (if dec-cells-int?
          (util/concatv ret (take-last 1 ret))
          ret)]

    (stop-dbg! debug?)
    ret))

(defn- color-rows [gr n]
  (color-cells gr n :rows))

(defn- color-columns [gr n]
  (color-cells gr n :columns))

(defn- sgr-gradient
  [char-height rows sgr-gradient-range font-weight]
  (let [row-colors (color-rows sgr-gradient-range char-height)]
    (into []
          (map-indexed
           (fn [i s]
             (str (m->sgr {:color (nth row-colors i (nth-not-found))
                           :font-weight font-weight})
                  s
                  "\033[0;m"))
           rows))))

(def gradient-points
  {:green  [28 34 76] ;; to blue
   :yellow [100 136 178] ;; to purple
   :red    [124 160 196] ;; to magenta
   :orange [130 172 214] ;; to purple
   })

(def gradient-points-cool-warm
  {:cool [27 33 39] ;; to warm
   })

(def gradient-pairs-cool-warm-map
  {:cool :warm})

(def gradient-pairs-map
  (merge {:yellow  :purple
          :orange  :purple
          :red     :magenta
          :green   :blue}
         gradient-pairs-cool-warm-map))

(def gradient-pairs-as-keywords-set
  (reduce 
   (fn [acc [k v]]
     (conj acc #{k v}))
   #{}
   gradient-pairs-map))

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

(def shade-names ["dark" "medium" "light"])


(defn- shade-map [pairs-map range-fn k i n]
  (let [prefix    (nth shade-names i (nth-not-found))
        grd-range (range-fn n)
        c1        (str prefix "-" (name k))
        c2        (str prefix "-" (-> k pairs-map name))]
    (util/concatv [[c1 c2] grd-range
                   [c2 c1] (reverse grd-range)]
                  (when (= "medium" prefix)
                    (let [c1 (name k)
                          c2 (-> k pairs-map name)]
                      [[c1 c2] grd-range
                       [c2 c1] (reverse grd-range)])))))

(defn- shade-maps [acc shades range-fn k]
 (->> shades
      (map-indexed (partial shade-map
                            gradient-pairs-map
                            range-fn
                            k))
      (apply util/concatv)
      (apply conj acc)))


(defn- gradient-ranges* [range-fn gradient-points-vec]
  (apply array-map 
         (reduce-kv 
          (fn [acc k shades]
            (shade-maps acc shades range-fn k))
          []
          gradient-points-vec)))

(def gradient-ranges
  (gradient-ranges* #(range % (+ % 6))
                    gradient-points))

(def gradient-ranges-cool-warm
  (gradient-ranges* #(range % (+ % (* 6 36)) 36)
                    gradient-points-cool-warm))

(def gradient-colors-set
  (reduce-kv
    (fn [acc k _] (apply conj acc k))
    #{}
    gradient-ranges))

(def gradient-directions
  {:to-bottom :vertical
   :to-top    :vertical
   :to-right  :horizontal
   :to-left   :horizontal})

(defn resolve-base-gradient-color-for-theme 
  [contrast color-str]
  (let [prefix (case contrast
                 ;; :super-soft
                 
                 :low
                 defs/bling-mood

                 :medium
                 "medium"

                 ;; :super-hard
                 
                 ;; covers :hard 
                 (case defs/bling-mood
                   "light" "dark"
                   "dark" "light"
                   "medium"))]

    #_(? {:opts/contrast    (:contrast opts)
          :defs/bling-mood defs/bling-mood
          :prefix           prefix})

    (str prefix "-" color-str)))

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

   Expects a map (banner opts) as second argument, from which a value for the
   :contrast option may be provided.
   
   If the user has BLING_MOOD env var set, the darker or lighter variant of the
   base color will be used, depending on the value of BLING_MOOD, which
   corresponds to the background terminal. The purpose is to lower or heighten
   the contrast.

   Returns a map:
   {:gradient-range       '(40 41 42 43 44 45) ;; <- range of sgr codes
    :gradient-orientation :vertical
    :vertical-gradient?   true
    :horizontal-gradient? false
   }"
  [
   #_{direction :gradient-direction
      s         :gradient-colors
      :keys     [gradient-shift contrast]
      :as       opts}
   gradient-colors
   direction
   gradient-shift
   contrast]
  (let [[c1 c2] gradient-colors
        c1      (name c1)
        c2      (name c2)
        c1      (resolve-base-gradient-color-for-theme contrast c1)
        c2      (resolve-base-gradient-color-for-theme contrast c2)
        k       (if (contains? #{"to top" "to left"} direction)
                  [c2 c1]
                  [c1 c2])
        gr      (or (get gradient-ranges k)
                    (let [coll (get gradient-ranges-cool-warm k)
                          gs   gradient-shift]
                      (if gs
                        (mapv #(- % gs) coll)
                        coll)))
        go      (get gradient-directions direction :vertical)
        vg?     (and gr (= go :vertical))
        hg?     (and gr (= go :horizontal))]
    {:gradient-range       gr
     :gradient-orientation go
     :vertical-gradient?   vg?
     :horizontal-gradient? hg?}))



(defn horizontal-gradient [composed gr opts]
  (let [width      (-> composed first count)
        col-colors (color-columns gr width)]

    (keyed [width col-colors gr])

    (mapv #(string/join
            (map-indexed
             (fn [i s]
               (str (m->sgr {:color       (nth col-colors i (nth-not-found))
                             :font-weight (:font-weight opts)})
                    s
                    "\033[0;m"))
             (do (count %)
                 %)))
          composed)))


(defn- composed 
  [letter-spacing rest-chars first-char]
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
 {:bands ["    " "    " "    " "    " "    " "    " "    "],
  :i 0,
  :character " ",
  :width 4,
  :height 7}
       #_(get chars-array-map
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
              (util/concatv block-pad
                            [s]
                            block-pad))
            (util/concatv (repeat (dec (/ height 2)) band-str)
                          [s]
                          (repeat (/ height 2) band-str))))))


(defn banner-str-chars
  [font-map
   char-height
   text
   display-missing-chars?]
  (let [debug?   
        (start-dbg! #_banner-str-chars) ;; Turn on/off, ignore/unignore first arg 
        
        dbg
        (if debug? #(? :- %) (fn [_] nil))

        _ (do (when debug?
                (println "Args to banner-str-chars"))
              (dbg (let [font-map 
                         (assoc font-map
                                :chars-array-map
                                '{... ...}
                                :example
                                "...")]
                     (keyed [font-map
                             char-height
                             text
                             display-missing-chars?]))))

        ret 
        (into []
              (keep #(let [char
                           %

                           char-map
                           (get font-map %)

                           char-map-exists?
                           (boolean char-map)
                           
                           {:keys [bands height] :as char-map}
                           (char-map-or-space-char-map font-map %)

                           char-height-is-less-than-font-height?
                           (some->> height (> char-height))
                           
                           every-band-is-an-empty-string?
                           (every? (fn [s] (= "" s)) bands)

                           every-band-is-a-blank-string?
                           (every? (fn [s] (string/blank? s)) bands)
                           
                           skip?
                           (or char-height-is-less-than-font-height?
                               every-band-is-an-empty-string?
                               (and (not= % " ")
                                    every-band-is-a-blank-string?))

                           display-substitute?
                           (and display-missing-chars? skip?)
                           
                           bands
                           (if skip?
                             (when display-substitute?
                               (replacement-char-vec font-map %))
                             bands)]
                       (dbg (keyed [char
                                    char-map-exists?
                                    char-map
                                    char-height-is-less-than-font-height?
                                    every-band-is-an-empty-string?
                                    skip?
                                    display-substitute?
                                    display-missing-chars?
                                    bands]))
                       bands)
                    (string/split text #"")))]
        (stop-dbg! debug?)
        ret))

#_{:display-missing-chars? true,
 :skip? false,
 :char-map
 {:bands ["    " "    " "    " "    " "    " "    " "    "],
  :i 0,
  :character " ",
  :width 4,
  :height 7},
 :char-height-is-less-than-font-height? false,
 :display-substitute? false,
 :char-map-exists? false,
 :every-band-is-an-empty-string? false,
 :bands ["    " "    " "    " "    " "    " "    " "    "],
 :char "Ꮉ"}

#_{:display-missing-chars? true,
 :skip? true,
 :char-map
 {:bands ["    " "    " "    " "    " "    " "    " "    "],
  :i 0,
  :character " ",
  :width 4,
  :height 7},
 :char-height-is-less-than-font-height? true,
 :display-substitute? true,
 :char-map-exists? false,
 :every-band-is-an-empty-string? false,
 :bands
 ["     "
  "     "
  "     "
  "     "
  "     "
  "  Ꮉ  "
  "     "
  "     "
  "     "
  "     "
  "     "],
 :char "Ꮉ"}

;; (defn- valid-font-kw?* [kw]
;;   (contains? bling.fonts/fonts-by-kw kw))


(defn- valid-font?*
  "Validate the shape of a bling banner figlet font map"
  [{:keys [widest-char char-height max-char-width chars-array-map]}]
  (boolean
   (and (string? widest-char)
        (= 1 (count widest-char))
        (pos-int? char-height)
        (pos-int? max-char-width)
        (map? chars-array-map)
        (seq chars-array-map))))


(defn- split-css-gradient-str [s]
  (some-> s
          (maybe string?)
          (string/split #" ")))

(def shift-min 0)
(def shift-max 5)

(defn zero-or-pos-int? [n] 
  (or (pos-int? n)
      (when (number? n) (zero? n))))

(defn- valid-gradient-shift?*
  [n]
  (and (zero-or-pos-int? n)
       (<= shift-min n)
       (>= shift-max n)))

(defn- valid-gradient-pairs?*
  [vc]
  (boolean (some->> (maybe vc vector?)
                    (into #{})
                    (contains? gradient-pairs-as-keywords-set))))

(declare caught-exception!)

(defn- resolved-gradient-shift [valid? x]
  (if valid?
    x
    (if (int? x)
      (if (< x 0) shift-min shift-max)
      0)))

(defn banner* 
  [{:keys [text
           letter-spacing
           font-weight
           gradient-direction
           gradient-colors
           gradient-shift
           contrast
           :dev/print-font!
           display-missing-chars?]
    :as opts
    :or {display-missing-chars? true
         letter-spacing         0
         gradient-colors        nil 
         gradient-shift         0
         gradient-direction     :to-bottom
         font-weight            :normal}
    ;; user-font-kw :font
    user-font :font
    }]

  ;; TODO - All these validations should happen in malli
  (try
    (let [valid-text?               (and (string? text) (not (string/blank? text)))
          valid-letter-spacing?     (zero-or-pos-int? letter-spacing)
          valid-font-weight?        (or (nil? font-weight)
                                        (contains? #{:normal :bold} font-weight))

          ;; Use these if user can choose fonts by keyword ---------------------
          ;; valid-font-kw?            (valid-font-kw?* user-font-kw)
          ;; resolved-user-font        (get bling.fonts/fonts-by-kw user-font-kw)
          ;; default-font-kw           :ansi-shadow
          ;; default-font              (get bling.fonts/fonts-by-kw default-font-kw)
          ;; font                      (or resolved-user-font default-font)
          ;; -------------------------------------------------------------------
          
          ;; Use these if we are forcing user to explicitly require fonts ------
          default-font              ansi-shadow
          valid-font?               (valid-font?* user-font)
          font                      (if valid-font? user-font default-font)
          ;; -------------------------------------------------------------------

          no-gradient?              (or (nil? gradient-colors)
                                        defs/no-color?)
          gradient-colors*          gradient-colors
          gradient-shift*           gradient-shift
          gradient-direction*       gradient-direction
          gradient-colors           (some-> gradient-colors
                                            (maybe valid-gradient-pairs?*))
          gradient-desired?         (not no-gradient?)
          valid-gradient-pairs?     (boolean (if no-gradient?
                                               true
                                               (valid-gradient-pairs?* gradient-colors)))
          will-render-gradient?     (and gradient-desired? valid-gradient-pairs?)
          valid-gradient-shift?     (valid-gradient-shift?* gradient-shift)
          valid-gradient-direction? (contains? #{:to-right
                                                 :to-left
                                                 :to-bottom
                                                 :to-top}
                                               gradient-direction)
          gradient-direction        (if valid-gradient-direction? 
                                      gradient-direction
                                      :to-bottom)
          gradient-shift            (resolved-gradient-shift valid-gradient-shift?
                                                             gradient-shift) 
          valid-contrast?          (or (nil? contrast)
                                       (boolean 
                                        (and (not (nil? contrast))
                                             (contains? #{:low :high :medium}
                                                        contrast))))
          ]
      
      #_(? (keyed [no-gradient?
                 gradient-desired?
                 valid-gradient-pairs?
                 valid-gradient-shift?
                 valid-gradient-direction?]))

      (when-not valid-font-weight?
        (invalid-banner-opt-warning! 
         {:option-key   :font-weight
          :option-value font-weight
          :valid-desc   #{:bold :normal}}))


      (when gradient-desired? 

        (when-not will-render-gradient?
          (invalid-banner-opt-warning! 
           {:option-key      :gradient-colors
            :option-value    gradient-colors*
            :valid-desc      (concat
                              ["One of:"]
                              (map (fn [[k v]] 
                                     (str "["
                                          k
                                          " " 
                                          v
                                          "]")) 
                                   gradient-pairs-map) )
            :default-val-msg [""
                              "No gradient will be applied"]}))

        (when will-render-gradient?
          (when-not valid-gradient-shift?
            (invalid-banner-opt-warning! 
             {:option-key      :gradient-shift
              :option-value    gradient-shift*
              :valid-desc      "An integer between 0 and 5 (inclusive)"
              :default-val-msg [""
                                (str "A value of " gradient-shift " will be used")]})))


        (when will-render-gradient?
          (when-not valid-gradient-direction?
            (invalid-banner-opt-warning! 
             {:option-key      :gradient-direction
              :option-value    gradient-direction*
              :valid-desc      (str "One of: " #{:to-right
                                                 :to-left
                                                 :to-bottom
                                                 :to-top})
              :default-val-msg [""
                                "The default value :to-bottom will be used"]})))

        (when will-render-gradient?
          (when-not valid-contrast?
            (invalid-banner-opt-warning! 
             {:option-key      :contrast
              :option-value    contrast
              :valid-desc      (str "One of: " #{:high :low :medium})}))))

      (when-not valid-letter-spacing?
        (invalid-banner-opt-warning! 
         {:option-key     :letter-spacing
          :option-value   letter-spacing
          :valid-desc     "A positive integer"}))

      (when-not valid-text?
        (invalid-banner-opt-warning! 
         {:option-key     :text
          :option-value   text
          :valid-desc     "A non-blank string"}))


      ;; Use this if user can choose fonts by keyword --------------------------
      ;; (when-not valid-font-kw?
      ;;   (invalid-banner-opt-warning! 
      ;;    {:option-key      :font
      ;;     :option-value    user-font-kw
      ;;     :valid-desc      (concat [""
      ;;                               "A keyword alias for one of Bling's Figlet fonts:"
      ;;                               ""]
      ;;                              (map #(keyword %) defs/banner-fonts-vec))
      ;;     :default-val-msg [""
      ;;                       (str "The default font "
      ;;                            default-font-kw
      ;;                            " will be used")]}))
      ;; -----------------------------------------------------------------------


      ;; Use these if we are forcing user to explicitly require fonts ----------
      (when-not valid-font?
        (invalid-banner-opt-warning! 
         {:option-key      :font
          :option-value    user-font
          :valid-desc      "One of the Figlet fonts that ships with Bling."
          :valid-examples  (map #(str "bling.fonts." % "/" % ) 
                                defs/banner-fonts-vec)
          :default-val-msg [""
                            (str "The default font "
                                 "bling.fonts."
                                 (:font-sym default-font)
                                 "/"
                                 (:font-sym default-font)
                                 " will be used")]}))
      ;; -----------------------------------------------------------------------



      

      ;; Use this if user can choose fonts by keyword --------------------------
      ;; (when (and valid-font-kw? valid-text?)
      ;; -----------------------------------------------------------------------

      ;; Use these if we are forcing user to explicitly require fonts ----------
      (when (and valid-font? valid-text?)
      ;; -----------------------------------------------------------------------

        (let [opts
             (if valid-gradient-shift?
               opts
               (assoc opts :gradient-shift 0))

              {:keys [char-height]}
              font

              text-str-chars
              (banner-str-chars font char-height text display-missing-chars?)

              {:keys [gradient-range vertical-gradient? horizontal-gradient?]}
              (when will-render-gradient?
                (gradient-map gradient-colors
                              gradient-direction
                              gradient-shift
                              contrast))

              first-char
              (if vertical-gradient?
                (sgr-gradient char-height
                              (first text-str-chars)
                              gradient-range
                              font-weight)
                (first text-str-chars))

              rest-chars
              (if vertical-gradient?
                (mapv #(sgr-gradient char-height
                                     % 
                                     gradient-range
                                     font-weight)
                      (rest text-str-chars))
                (rest text-str-chars))
              
              composed
              (composed letter-spacing rest-chars first-char)


              composed 
              (if horizontal-gradient?
                (horizontal-gradient composed gradient-range opts) 
                composed)

              margins
              (banner-margin-map opts) ]
          (->> composed
               (maybe-with-inline-margins margins)
               (maybe-with-block-margins margins)))))

    (catch #?(:cljs js/Object :clj Exception)
           e
      (print-caught-exception! "bling.banner/banner")
      (println "User-supplied options:")
      (println (string/replace 
                (with-out-str 
                  (println 
                   (reduce-kv (fn [m k v]
                                (assoc m
                                       k
                                       (if (string? v)
                                         (str "\"" v "\"")
                                         v)))
                              {} 
                              (-> opts
                                  (assoc-in [:font :chars-array-map] "...")
                                  (assoc-in [:font :example] "\"...\""))
                              )))
                #", :"
                "\n :"))
      (println "Error from clojure:")
      (println e))))


;; TODO add :bands option for returning colorized vectors of character bands
;; - then expose compose as a public fn

(defn banner
  "Returns a multi-line Figlet (ascii art) string with the provided `:text`
   option.
   

   Intended to render a \"single line\" banner.


   Can be optionally colorized with the `:gradient` option, using a small set of
   pre-defined gradients:

   ```(banner {:text \"Hello\" :gradient-colors [:green :blue]})```
   

   Valid `:gradient-color` values: 

   `[:green :blue]`, 
   `[:red :magenta]`, 
   `[:yellow :purple]`, 
   `[:orange :purple]`, 
   `[:cool :warm]`



   Valid `:gradient-direction` values: 

   `:to-top`, 
   `:to-bottom`, 
   `:to-right`, 
   `:to-left`



   Can be colorized solid with `bling.core/bling`:

   ```(bling [:red (banner {:text \"Hello\"})])```

   
| Key                   | Pred       | Description   |
| :---------------      | -----------| ------------- |
| `:font`               | `map?`     | Must be one of the fonts that ships with Bling: `bling.fonts.ansi-shadow/ansi-shadow`,  `bling.fonts.big-money.big-money/big-money` , `bling.fonts.big/big`, `bling.fonts.miniwi/miniwi`, `bling.fonts.drippy/drippy,` or `bling.fonts.isometric-1/isometric-1`. Defaults to `bling.fonts.ansi-shadow/ansi-shadow`. |
| `:text`               | `string?`  | The text to set in the banner.
| `:font-weight`        | `keyword?` | If set to `:bold`, each subchar in figlet characters will be bolded. Only applies when a gradient is set.
| `:gradient-colors`    | `vector?`  | Expects a vector of 2 keywords. Only the following color pairs are valid: `[:green :blue]`, `[:red :magenta]`, `[:yellow :purple]`, `[:orange :purple]`, `[:cool :warm]`.  Only applies to terminal emulator printing|
| `:gradient-direction` | `keyword?` | Expects a keyword. Must be one of: `:to-top`, `:to-bottom`, `:to-right`, and `:to-left`.  Only applies to terminal emulator printing|
| `:gradient-shift`     | `int?`     | If gradient is `[:warm :cool]` pair, this will shift the hue. `0-5`. Defaults to `0`.|
| `:contrast`           | `keyword?` | If gradient is set, this will force an overall lighter or darker tone. Defaults to `:medium`. If the user has a `BLING_MOOD` env var set, it will default to `:high` in order to optimize contrast for the users terminal theme (light or dark) |
| `:margin-top`         | `int?`     | Amount of margin (in newlines) at top, outside banner. <br>Defaults to `1`. Only applies to terminal emulator printing. |
| `:margin-bottom`      | `int?`     | Amount of margin (in newlines) at bottom, outside banner. <br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-left`        | `int?`     | Amount of margin (in blank character spaces) at left, outside banner. <br>Defaults to `0`. Only applies to terminal emulator printing. |
| `:margin-right`       | `int?`     | Amount of margin (in blank character spaces) at right, outside banner. <br>Defaults to `0`. Only applies to terminal emulator printing. |"

  [m]
 #?(:cljs
    (if node?
      (banner* m)
      (let [style (:browser-style m)]
        (when style
          (when-not (string? style)
            (js/console.warn 
             (str "Warning"
                   "\n\n"
                  "bling.banner/banner."
                  "\n\n"
                  "The :browser-style option must be a valid css style string."
                  "\n\n"
                  "Example:"
                  "\n\n"
                  "(bling.banner/banner\n"
                  " {:text  \"My Banner\"})"
                  "\n  :style \"color: blue; font-size: 24px;\""
                  ))))
        (.apply js/console.log 
                js/console 
                #js[(str "%c" (:text m) "%c")
                    (str style)
                    "font-size:default;"])))
    :clj
    (banner* m)))

;; TODO currently, font-weight has no effect if no gradient is specified.
;;      you can set it with bling though.


;; TODO add :neutral gradient option, maybe \"to right, neutral, gray\"
