(ns bling.sugar
  "Top-level api namespace for public functions which assist in styling text
   with ANSI SGR, for output to terminal environments.

   Supports output for system terminal emulator, as well development consoles in
   web popular web browser development dev tool environments.
   
   For more background, see ok-color.org."
  (:require [fireworks.core :refer [? !? ?> !?> pprint] :rename {pprint fwpp}]
            [clojure.string :as str]
            [clojure.set :as set]
            [me.flowthing.pp :refer [pprint]]
            ))



;; Utils -------------------------------------------------------------------

(defn as-str [x]
  (str (if (or (keyword? x) (symbol? x)) (name x) x)))

(defn  when->>
  "If (= (pred x) true), returns x, otherwise nil.
   Useful in a `clojure.core/some->>` threading form."
  [pred x]
  (when (or (true? (pred x))
            (when (set? pred) (contains? pred x)))
    x))

;; Debugging, Errors and Warnings ----------------------------------------------

(def ^:private sgr-tags*
  "Used for constructing warning, error, and, info messages"
  {:wavy-underline        "\033[4:3m"
   :orange-wavy-underline "\033[38;5;208;4:3m"
   :italic                "\033[3m"
   :gray                  "\033[38;5;247m"
   :bold-gray             "\033[38;5;247;1m"
   :italic-gray           "\033[3;38;5;247m"
   :bold-italic-gray      "\033[3;38;5;247;1m"
   :bold-italic           "\033[3;1m"
   :bold-orange           "\033[1;38;5;208m"
   :orange                "\033[38;5;208m"
   :green                 "\033[38;5;196m"
   :red                   "\033[38;5;196m"
   :blue                  "\033[38;5;39m"
   :bold                  "\033[1m"
   :dim                   "\033[2m"
   :sgr-tag-close         "\033[0;m"})


(defn- sgr* [k v level]
  (if (pos? level)
    (str (get sgr-tags* k) v (:sgr-tag-close sgr-tags*))
    v))

(defn- pprinted-coll [coll]
  (str/replace (with-out-str
                 (pprint (if (set? coll)
                           (->> coll sort (into []))
                           coll)))
               #"\n$"
               ""))

(defn- satisfactions [desc level]
  (str/join (sgr* :italic "\n\n~ OR ~\n\n" level)
            (mapv #(str
                    (:label %)
                    (when (:pred %)
                      (str "\n"
                           (pprinted-coll (:pred %)))))
                  desc)))


(defn- supplied-value [supplied level]
  (sgr* :bold
        (cond
          (= supplied nil)
          "nil"
          (string? supplied)
          (str "\"" supplied "\"")
          :else
          (str/replace (with-out-str
                         (pprint supplied))
                       #"\n$"
                       ""))
        level))


(defn- indent-block [indent-str s]
  (->> (str/split s #"\n")
       (mapv #(str indent-str %))
       (str/join "\n")))


(defn- block-label [s level]
  (str (sgr* :italic s level) "\n\n"))


(defn- supplied-value-label [prop level]
  (block-label (str (sgr* :italic
                          (str "Supplied value" (when prop " for"))
                          level)
                    (sgr* :bold-italic prop level)
                    ":")
               level))


(defn- supplied-must-satisfy-label [prop level]
  (block-label (str (sgr* :italic
                          "The value of "
                          level)
                    (sgr* :bold-italic prop level)
                    (sgr* :italic
                          (str " must satisfy:")
                          level))
               level))


(defn- print-block [label type prop spec supplied level]
  (println
   (str (sgr* (case type :warning :bold-orange :error :bold-red :bold)
              (str "\n╭─── " label " ───── sugar.core/styled\n")
              level)
        "\n"
        (indent-block
         "  "
         ;; (sgr* :bold prop level)
         (let [desc (get-in spec [prop :desc])]
           (str
            (when (not (nil? supplied))
              (str (supplied-value-label prop level)
                   (indent-block "   " (supplied-value supplied level))
                   "\n\n\n"))
            (when (not (nil? prop))
              (str (supplied-must-satisfy-label prop level)
                   (str (indent-block "   "
                                      (satisfactions desc level))
                        "\n\n\n"
                        (block-label "Predicate:" level)
                        "    "
                        (sgr* :bold
                              (get-in spec [prop :pred-display])
                              level)
                        "\n\n\n")))
            "\n\n")))
        "\n\n"
        (sgr* :orange
              "╰────────────────────────────────────────"
              level))))

(defn ^:public ?sgr
  "For debugging of ANSI SGR tagged output.

   Prints the value with escaped ANSI SGR codes so you can read them in terminal
   emulators (otherwise text would just get colored). Preserves coloring.

   Returns the value."
  [s]
  (println
   (str/replace s
                #"\u001b\[((?:[0-9]|;|(?:9|4(?::[1-5])?))*)m"
                (str "\033[38;5;231;48;5;247m" ; <- white on gray, actual ansi sgr tag for fake ansi tag
                     "\\\\033[$1m"             ; <- fake ansi sgr tag text
                     "\033[0m"                 ; <- reset, actual ansi sgr for resetting fake ansi tag
                     "\033[$1m"                ; <- original ansi sgr, to preserve coloring
                     )))
  s)


;; Color helpers ---------------------------------------------------------------

(def hex-color-re #"([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})")

(defn rgb->8-bit
  "Uses a Manhattan distance algorithm to convert RGB to 8-bit color."
  [r g b]
  (letfn [(trunc [v]
            (if (neg? v)
              #?(:cljs
                 (js/Math.ceil v)
                 :clj
                 (Math/ceil v))
              #?(:cljs
                 (js/Math.floor v)
                 :clj
                 (Math/floor v))))
          (dist-sq [x, y, z, r, g, b]
            (+ (* (- x r) (- x r))
               (* (- y g) (- y g))
               (* (- z b) (- z b))))
          (ci [c]
            (cond
              (< c 48)  0
              (< c 115) 1
              :else     (trunc (/ (- c 35) 40))))]
    (let [ri       (ci r)
          gi       (ci g)
          bi       (ci b)
          clr-i    (+ (* 36 ri) (* 6 gi) bi)
          avg      (trunc (/ (+ r g b)  3))
          gry-i    (if (> avg 238) 23 (trunc (/ (- avg 3) 10)))
          hexes    [0, 0x5F, 0x87, 0xAF, 0xD7, 0xFF]
          rc       (nth hexes ri)
          gc       (nth hexes gi)
          bc       (nth hexes bi)
          gry      (+ 8 (* 10 gry-i))
          clr-dist (dist-sq rc, gc, bc, r, g, b)
          gry-dist (dist-sq gry, gry, gry, r, g, b)
          ret      (if (<= clr-dist gry-dist)
                     (+ 16 clr-i)
                     (+ 232 gry-i))
          ret      #?(:cljs
                      (js/Math.round ret)
                      :clj
                      (cond (double? ret)
                            (Math/round ret)
                            :else
                            ret))]
      ret)))


(defn hex->rgb*
  "Converts a hex(a) color (string) to an [r g b] vector representation."
  [s]
  (let [s (if (contains? #{4 5} (count s))
            (let [base (subs s 1)]
              (str "#" base base))
            s)
        [_ r1 r2 g1 g2 b1 b2 a1 a2] s
        f #?(:cljs #(js/parseInt (str "0x" %1 %2))
             :clj #(Integer/valueOf (str %1 %2) 16))
        r (f r1 r2)
        g (f g1 g2)
        b (f b1 b2)
        a (when (and a1 a2)
            (let [v (f a1 a2)]
              #?(:cljs v
                 :clj (Double/parseDouble (format "%.2f"
                                                  (double (/ v 255)))))))]
    [[r g b] a]))

(defn hex->rgb
  "Converts a hex(a) color (string) to an [r g b] vector representation."
  [s]
  (-> s hex->rgb* (nth 0 nil)))

(defn hex->rgba
  "Converts a hex(a) color (string) to an [r g b a] vector representation."
  [s]
  (let [ret* (hex->rgb* s)
        rgb  (nth ret* 0 nil)
        a    (nth ret* 1 nil)]
    (conj rgb (if a a 1))))

(defn hex->8-bit [s]
  (->> s hex->rgb (apply rgb->8-bit)))

(defn hex->rgb-or-8-bit [s level]
  (if (= level 1) (hex->8-bit s) (hex->rgb s)))


;; Color and style defs --------------------------------------------------------

(def base-colors
  "Bling pallette"
  ["red"
   "orange"
   "yellow"
   "olive"
   "green"
   "cyan"
   "blue"
   "purple"
   "magenta"
   "gray"
   "black"
   "white"])

(def base-user-colors
  "OG 16-color user pallette"
  ["user-red"
   "user-yellow"
   "user-green"
   "user-cyan"
   "user-blue"
   "user-magenta"
   "user-black"
   "user-white"
   "user-bright-red"
   "user-bright-yellow"
   "user-bright-green"
   "user-bright-cyan"
   "user-bright-blue"
   "user-bright-magenta"
   "user-bright-black"
   "user-bright-white"])

(def user-colors-foreground
  {"user-black"            30
   "user-red"              31
   "user-green"            32
   "user-yellow"           33
   "user-blue"             34
   "user-magenta"          35
   "user-cyan"             36
   "user-white"            37
   "user-bright-black"     90
   "user-bright-red"       91
   "user-bright-green"     92
   "user-bright-yellow"    93
   "user-bright-blue"      94
   "user-bright-magenta"   95
   "user-bright-cyan"      96
   "user-bright-white"     97})

(def user-colors-background
  {"user-black-bg"          40
   "user-red-bg"            41
   "user-green-bg"          42
   "user-yellow-bg"         43
   "user-blue-bg"           44
   "user-magenta-bg"        45
   "user-cyan-bg"           46
   "user-white-bg"          47
   "user-bright-black-bg"   100
   "user-bright-red-bg"     101
   "user-bright-green-bg"   102
   "user-bright-yellow-bg"  103
   "user-bright-blue-bg"    104
   "user-bright-magenta-bg" 105
   "user-bright-cyan-bg"    106
   "user-bright-white-bg"   107})

(def colors-24-bit
  "Bling pallette - 24-bit color variants optimized for unity and contrast"
  {"hard-red"            [255 0 0]
   "soft-red"            [207 130 130]
   "red"                 [215 0 95]
   "medium-red"          [255 0 0]
   "light-red"           [255 0 135]
   "dark-red"            [215 0 0]
   "extra-light-red"     [255 175 175]
   "extra-dark-red"      [150 0 0]
   "hard-orange"         [255 135 0]
   "soft-orange"         [175 135 95]
   "orange"              [215 135 0]
   "medium-orange"       [215 135 0]
   "light-orange"        [255 175 0]
   "dark-orange"         [175 95 0]
   "extra-light-orange"  [255 202 176]
   "extra-dark-orange"   [140 84 0]
   "hard-yellow"         [255 215 0]
   "soft-yellow"         [175 175 0]
   "yellow"              [215 175 0]
   "medium-yellow"       [215 175 0]
   "light-yellow"        [215 215 0]
   "dark-yellow"         [175 135 0]
   "extra-light-yellow"  [255 255 175]
   "extra-dark-yellow"   [95 95 0]
   "hard-olive"          [95 175 95]
   "soft-olive"          [175 175 135]
   "olive"               [135 175 0]
   "medium-olive"        [135 175 0]
   "light-olive"         [215 215 175]
   "dark-olive"          [135 135 0]
   "extra-light-olive"   [215 255 175]
   "extra-dark-olive"    [0 95 0]
   "hard-green"          [95 215 0]
   "soft-green"          [135 175 135]
   "green"               [0 215 0]
   "medium-green"        [0 215 0]
   "light-green"         [95 255 0]
   "dark-green"          [0 135 0]
   "extra-light-green"   [179 255 195]
   "extra-dark-green"    [0 82 0]
   "hard-cyan"           [0 195 217]
   "soft-cyan"           [79 176 163]
   "cyan"                [18 199 163]
   "medium-cyan"         [0 215 0]
   "light-cyan"          [0 232 190]
   "dark-cyan"           [0 126 130]
   "extra-light-cyan"    [175 255 255]
   "extra-dark-cyan"     [0 95 95]
   "hard-blue"           [0 135 255]
   "soft-blue"           [135 175 215]
   "blue"                [0 175 255]
   "medium-blue"         [0 175 255]
   "light-blue"          [95 215 255]
   "dark-blue"           [0 95 215]
   "extra-light-blue"    [179 222 255]
   "extra-dark-blue"     [0 0 194]
   "hard-purple"         [175 95 215]
   "soft-purple"         [175 175 215]
   "purple"              [175 135 255]
   "medium-purple"       [175 135 255]
   "light-purple"        [215 175 255]
   "dark-purple"         [175 0 215]
   "extra-light-purple"  [227 209 255]
   "extra-dark-purple"   [95 0 135]
   "hard-magenta"        [255 0 175]
   "soft-magenta"        [215 135 175]
   "magenta"             [255 0 215]
   "medium-magenta"      [255 0 255]
   "light-magenta"       [255 135 255]
   "dark-magenta"        [215 0 175]
   "extra-light-magenta" [244 200 247]
   "extra-dark-magenta"  [125 0 125]
   "gray"                [143 143 143]
   "medium-gray"         [158 158 158]
   "light-gray"          [198 198 198]
   "dark-gray"           [108 108 108]
   "extra-light-gray"    [230 230 230]
   "extra-dark-gray"     [56 56 56]
   "black"               [0 0 0]
   "white"               [255 255 255]})


(def colors-8-bit
  "Bling pallette - 8-bit color variants optimized for unity and contrast"
  {"hard-red"            196
   "soft-red"            174
   "red"                 161
   "medium-red"          196
   "light-red"           198
   "dark-red"            160
   "extra-light-red"     212
   "extra-dark-red"      88
   "hard-orange"         208
   "soft-orange"         137
   "orange"              172
   "medium-orange"       172
   "light-orange"        214
   "dark-orange"         130
   "extra-light-orange"  221
   "extra-dark-orange"   58
   "hard-yellow"         220
   "soft-yellow"         142
   "yellow"              178
   "medium-yellow"       178
   "light-yellow"        184
   "dark-yellow"         136
   "extra-light-yellow"  229
   "extra-dark-yellow"   58
   "hard-olive"          71
   "soft-olive"          144
   "olive"               106
   "medium-olive"        106
   "light-olive"         187
   "dark-olive"          100
   "extra-light-olive"   192
   "extra-dark-olive"    22
   "hard-green"          76
   "soft-green"          108
   "green"               40
   "medium-green"        40
   "light-green"         82
   "dark-green"          28
   "extra-light-green"   154
   "extra-dark-green"    23
   "hard-cyan"           44
   "soft-cyan"           73
   "cyan"                42
   "medium-cyan"         40
   "light-cyan"          86
   "dark-cyan"           28
   "extra-light-cyan"    159
   "extra-dark-cyan"     23
   "hard-blue"           33
   "soft-blue"           110
   "blue"                39
   "medium-blue"         39
   "light-blue"          81
   "dark-blue"           26
   "extra-light-blue"    159
   "extra-dark-blue"     20
   "hard-purple"         134
   "soft-purple"         146
   "purple"              141
   "medium-purple"       141
   "light-purple"        183
   "dark-purple"         128
   "extra-light-purple"  189
   "extra-dark-purple"   56
   "hard-magenta"        199
   "soft-magenta"        175
   "magenta"             200
   "medium-magenta"      201
   "light-magenta"       213
   "dark-magenta"        163
   "extra-light-magenta" 225
   "extra-dark-magenta"  88
   "gray"                246
   "medium-gray"         247
   "light-gray"          251
   "dark-gray"           242
   "extra-light-gray"    253
   "extra-dark-gray"     234
   "black"               16
   "white"               231})


(def all-colors (into #{}
                      (concat base-colors
                              base-user-colors
                              ;; user-colors-keys-foreground
                              )))

(def ^:private font-style-enum
  #{"italic"
    "normal"})


(def ^:private font-weight-enum
  #{"bold"
    "dim"})


(def ^:private underline-style-enum
  #{"straight"
    "double"
    "wavy"
    "dotted"
    "dashed"})


(def ^:private underline-style-codes-by-style
  {"straight" 1
   "double"   2
   "wavy"     3
   "dotted"   4
   "dashed"   5})


;; Color level - result of ansi-color-support call
(def level 1)



;; Public API, supporting fns --------------------------------------------------

(defn sgr-8-bit-user [color layer]
  (some->> color
           as-str
           (get (if (= "foreground" layer)
                  user-colors-foreground
                  user-colors-background))))


(defn- sgr-color-component [fg-or-bg mode color]
  (str (if (= fg-or-bg "foreground") "38" "48")
       (if (= mode :truecolor) ";2;" ";5;")
       color))


(defn- sgr-rgb-color-sub-component [color layer level]
  (some->> (if (= level 2)
             (get colors-24-bit color)
             (some-> (get colors-8-bit color) vector))
           (str/join ";")
           (sgr-color-component layer
                                (if (= level 2)
                                  :truecolor
                                  :8-bit))))


(defn- sgr-color-str [color layer level]
  (when color
    (some->> (if (string? color)
               (or (sgr-8-bit-user color layer)
                   (sgr-rgb-color-sub-component color layer level))
               (sgr-color-component layer
                                    :8-bit
                                    (if (vector? color)
                                      (apply rgb->8-bit color)
                                      color))))))

(defn- stringify-kw [c]
  (if (keyword? c) (name c) c))


(defn- sgr-text-decoration-str
  [underline underline-style]
  (cond
    (= (stringify-kw underline) "underline")
    (if-let [n (get (stringify-kw underline-style)
                    underline-style-codes-by-style)]
      (str "4:" n)
      "4")
    (contains? #{"line-through" :strikethrough}
               (stringify-kw underline-style))
    "9"))


(def invalid-constant "__SUGAR.CORE/INVALID_VALUE__")


(defn- eight-bit-color? [x]
  (and (int? x) (<= 0 x 256)))


(defn- string-or-keyword->valid-color
  "Converts a string or keyword into one of the following, depending on the
  value of x and the value of the color level:
  - A valid bling color (string), for subsequent lookup
  - An valid rgb tuple
  - A valid numerical id 0-255, for an 8-bit color"
  [x level]
  (when (or (string? x) (keyword? x))
    (let [s (stringify-kw x)]
      (or (when (re-find hex-color-re s)
            ;; Convert hex representation into rgb or 8-bit
            (hex->rgb-or-8-bit s level))
          ;; Valid bling pallette color
          (when (contains? all-colors s) s)))))


(defn- valid-color
  [x k spec print-warnings? level]
  (if (nil? x)
    nil
    (let [ret*     (or (string-or-keyword->valid-color x level)
                       (when (and (vector? x)
                                  (let [[r g b] x]
                                    (and (eight-bit-color? r)
                                         (eight-bit-color? g)
                                         (eight-bit-color? b))))
                         x)
                       (when (eight-bit-color? x) x)
                       invalid-constant)

          invalid? (= ret* invalid-constant)

          ret      (if invalid? nil ret*)]
      (when invalid?
        (when print-warnings? (print-block "WARNING" :warning k spec x level)))
      ret)))


(defn- valid-enum-option
  [x k spec print-warnings? level]
  (if (nil? x)
    nil
    (or (and (or (string? x)
                 (keyword? x))
             (let [s (stringify-kw x)]
               (some->> s
                        (when->> #(contains? (get-in spec [k :pred]) %)))))
        (when print-warnings?
          (print-block "WARNING" :warning k spec x level)))))


(defn- valid-boolean-option
  [x k spec print-warnings? level]
  (if (nil? x)
    nil
    (let [valid? (or (true? x) (false? x))]
      (if valid?
        x
        (when print-warnings?
          (print-block "WARNING" :warning k spec x level))))))


(def boolean-spec
  {:pred         #(when (boolean? %) %)
   :pred-display 'boolean?
   :desc         [{:label "A boolean value - `true` or `false`"}]})


(def color-spec
  {:pred         valid-color
   :pred-display 'sugar.core/valid-color
   :desc         [{:label "An rgb vector such as [22 233 122]."}
                  {:label "A numerical id of an 8-bit color, from 0 to 255"}
                  {:label "A string or keyword that resolves to one of:"
                   :pred  all-colors}]})

(def underline-style-spec
  {:pred         underline-style-enum
   :pred-display underline-style-enum
   :desc         [{:label "An rgb vector such as [22 233 122]."}
                  {:label "A numerical id of an 8-bit color, from 0 to 255"}
                  {:label "A string or keyword that resolves to one of:"
                   :pred  all-colors}]})

(def enum-desc
  [{:label "A string or keyword that resolves to one of:"}])


(def options-spec
  {:font-weight      {:pred         font-weight-enum
                      :pred-display font-weight-enum
                      :desc         enum-desc}
   :font-style       {:pred         font-style-enum
                      :pred-display font-style-enum
                      :desc         enum-desc}
   :underline-style  underline-style-spec
   :color            color-spec
   :background-color color-spec
   :reverse?         boolean-spec
   :strikethough?    boolean-spec
   :underline?       boolean-spec
   :hide?            boolean-spec})


(defn ^:private styled
  [x m]
  (try (if (zero? level)
         x
         (if-let [{:keys [font-weight
                          font-style
                          reverse?
                          hide?
                          blink?
                          strikethrough?
                          underline?
                          underline-style
                          color
                          background-color
                          validate-options?]
                   :or   {validate-options? true}}
                  m]
           (let [validate-options?
                 (if (true? validate-options?) true false)

                 print-warnings?
                 true

                 font-style
                 (when-let [font-style
                            (valid-enum-option font-style
                                               :font-style
                                               options-spec
                                               print-warnings?
                                               level)]
                   (when (= font-style "italic") 3))


                 font-weight
                 (when-let [font-weight
                            (valid-enum-option font-weight
                                               :font-weight
                                               options-spec
                                               print-warnings?
                                               level)]
                   (case font-weight
                     "bold" 1
                     "dim" 2
                     nil))

                 color
                 (when-let [color (valid-color color
                                               :color
                                               options-spec
                                               print-warnings?
                                               level)]
                   (sgr-color-str color "foreground" level))

                 background-color
                 (when-let [color (valid-color background-color
                                               :background-color
                                               options-spec
                                               print-warnings?
                                               level)]
                   (sgr-color-str color "background" level))

                 underline
                 (valid-boolean-option underline?
                                       :underline?
                                       options-spec
                                       print-warnings?
                                       level)

                 underline-style
                 (valid-enum-option underline-style
                                    :underline-style
                                    options-spec
                                    print-warnings?
                                    level)

                 text-decoration
                 (sgr-text-decoration-str underline underline-style)

                 reverse
                 (when (valid-boolean-option reverse?
                                             :reverse?
                                             options-spec
                                             print-warnings?
                                             level)
                   7)

                 strikethrough
                 (when (valid-boolean-option strikethrough?
                                             :strikethrough?
                                             options-spec
                                             print-warnings?
                                             level)
                   9)

                 hide
                 (when (valid-boolean-option hide?
                                             :hide?
                                             options-spec
                                             print-warnings?
                                             level)
                   8)

                 blink
                 (when (valid-boolean-option blink?
                                             :blink?
                                             options-spec
                                             print-warnings?
                                             level)
                   5)

                 sgr

                 (str (->> [font-style
                            font-weight
                            text-decoration
                            blink
                            reverse
                            hide
                            strikethrough
                            color
                            background-color]
                           (remove nil?)
                           (str/join ";")
                           (str "\033[")))]

            ;; Simulate error
            ;;  #?(:clj (throw (Throwable. "something")))
             (or (some-> sgr (str "m" x "\033[m"))
                 x))
           x))
       (catch #?(:cljs js/Object :clj Throwable)
              e
         (do
           (print-block "Error(Caught)" :error nil nil m 0)
           x))))

