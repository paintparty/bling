(ns bling.sample
  (:require 

  ;;  [bling.macros :refer [let-map keyed ?]] ;;<-- just for debugging
   [clojure.string :as string]
   [clojure.pprint :refer [pprint]]
   [bling.banner :refer [banner]]
   [bling.fontlib]
   [bling.fonts]
   [bling.macros :refer [keyed ? start-dbg! stop-dbg! nth-not-found]]

   #?(:cljs
      [bling.core :refer [bling bling! callout print-bling point-of-interest]]
      :clj
      [bling.core :refer [bling bling! callout point-of-interest]
       
       ])))

(def ^:private colors-ordered
  ["system-black"   
   "system-maroon"  
   "system-green"   
   "system-olive"   
   "system-navy"    
   "system-purple"  
   "system-teal"    
   "system-silver"  
   "system-grey"    
   "system-red"     
   "system-lime"     
   "system-yellow"   
   "system-blue"     
   "system-fuchsia"  
   "system-aqua"     
   "system-white"   
   "red"
   "orange"
   "yellow"
   "olive"
   "green"
   "blue"
   "purple"
   "magenta"
   "gray"
   "black"
   "white"])

(def ^:private printer
  #?(:cljs print-bling :clj println))

(defn all-the-colors 
  ([]
   (all-the-colors nil))
  ([{:keys [label variant]}]
   (let [colors   (filter #(not (re-find #"^system-" %)) colors-ordered)
         max-char (apply max (map count colors))]
     (when label (bling! "\n" [:italic label] "\n"))
     (doseq [s     colors #_(take 1 colors)
             :when (not (re-find #"^system-" s))]
       (let [spaces (string/join (repeat (- max-char (count s)) " "))
             f      (fn lab
                      ([m] (lab m s))
                      ([m s] 
                       (let [color-name 
                             (if (contains? #{"medium" "light" "dark"}
                                            variant)
                               (str variant "-" s)
                               s)]
                         [(merge {:color color-name} m) s])))]
         (printer (bling 

                   (f {:background-color s
                       :color            :white
                       :font-weight      :bold}
                      (str " " s " " ))

                   spaces

                   " "
                   (f {:font-weight :bold})
                   " "
                   spaces

                   " "
                   (f {})
                   " "
                   spaces

                   " "
                   (f {:text-decoration :strikethrough})
                   " "
                   spaces

                   " "
                   (f {:text-decoration :underline})
                   " "
                   spaces

                   " "
                   (f {:font-style :italic})
                   " "
                   spaces)))))))


(defn example-custom-callout
  [{:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header (str "This is not a real error or warning"
                                          "\n"
                                          "Your header message goes here.")
                             :body   (str "The body of your template goes here."
                                          "\n"
                                          "Second line of copy."
                                          "\n"
                                          "Another line.")}
                            point-of-interest-opts)
        message      (point-of-interest poi-opts)
        callout-opts (merge callout-opts
                            {:padding-top 1})]
    (callout callout-opts message)))

#?(:clj
   (defn print-commented-example-call! [m k body]
     (println 
      (str
       "\n\n\n"
       (bling [k ";; (callout "])
       (string/join
        (bling [k "\n;; "]) 
        (map-indexed (fn [i s]
                       (bling [k
                               (str (if (zero? i)
                                      ""
                                      "         ")
                                    s)])) 
                     (string/split (string/replace 
                                    (with-out-str (println m))
                                    #"\n$"
                                    "")
                                   #",")))
       "\n"
       (bling [k (str ";;          \"" (string/replace body #"\n" "\\\\n"))])
       (bling [k "\""])
       (bling [k "\n;; =>\n"])))))

(defn callout+
  [{callout-type  :type
    callout-colorway  :colorway
    callout-label :label
    :as           m}]
  (let [body (str "Callout with "
                  (if callout-type "type" "colorway")
                  " of "
                  (or callout-type callout-colorway)
                  (when callout-label " and custom :label")
                  (when (contains? #{:warning "warning" :error "error"}
                                   callout-type)
                    (str "\n"
                         "This is not a real "
                         (name callout-type))))
        k :italic.subtle]
      #?(:clj (print-commented-example-call! m k body))
      (callout (assoc m :margin-top 0 :margin-bottom 0) body)))

(defn print-comment [s]
  (printer (bling [:italic.subtle s])))

#_(defn sample []
  (callout+ {:theme       :gutter #_:sideline-bold
             :colorway    :positive
             :label       [1 2 3 4 5]
             :label-theme :marquee
             }))

(defn sample []
  (println)
  (print-comment ";; Below are some samples using bling.core/callout")
  (print-comment ";; https://github.com/paintparty/bling")

  (println)
  (print-comment ";; You need to require the following things to make this work:")
  (print-comment ";; (require '[bling.core :refer [bling callout point-of-interest]])")

  ;; CALLOUT examples with default :sideline theme, minimal label --------------
  (callout+ {:type :info})
  (callout+ {:type  :info :label "My custom label"})
  (callout+ {:type :warning})
  (callout+ {:type :error})
  (callout+ {:colorway :positive :label "SUCCESS!"})
  

  (println)
  ;; CALLOUT examples with :sideline-bold theme, minimal label -----------------
  (callout+ {:type :info :theme :sideline-bold })
  (callout+ {:type :warning :theme :sideline-bold})
  (callout+ {:type :error :theme :sideline-bold})
  (callout+ {:colorway :positive :label "SUCCESS!" :theme :sideline-bold})
  (println)

  ;; CALLOUT examples with :sideline-bold theme, marquee label  ----------------
  (callout+ {:type :info :theme :sideline-bold :label-theme :marquee})
  (callout+ {:type :warning :theme :sideline-bold :label-theme :marquee})
  (callout+ {:type :error :theme :sideline-bold :label-theme :marquee})
  (callout+ {:theme :sideline-bold :colorway :positive :label "SUCCESS!" :label-theme :marquee})

  (println)
  ;; CALLOUT examples with :gutter theme  --------------------------------------
  (callout+ {:type          :info :theme :gutter})
  (callout+ {:type :warning :theme :gutter})
  (callout+ {:type :error :theme :gutter})
  (callout+ {:colorway          :positive
             :label         "SUCCESS!"
             :theme :gutter})

  (println)
  ;; CALLOUT examples with :gutter theme, thicker gutter -----------------------
  (callout+ {:type :info :theme :gutter :margin-left 3})
  (callout+ {:type :warning :theme :gutter :margin-left 3})
  (callout+ {:type :error :theme :gutter :margin-left 3})
  (callout+ {:colorway    :positive
             :label       "SUCCESS!"
             :theme       :gutter
             :margin-left 3})
  

  (println)
  (println)
  (println)
  (print-comment
     ";; Below is an example of a custom error template with")
  (print-comment
     ";; a point-of-interest diagram. See readme for more details.")
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type :error}})


  (println)
  (println)
  (println)
  (print-comment
     ";; Below is an example of a custom warning template with")
  (print-comment
     ";; a point-of-interest diagram. See readme for more details.")
  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(myfun foo baz)
                             :type                  :warning
                             :text-decoration-index 2}
    :callout-opts           {:type :warning
                             :label "WARNING: Invalid arg value"}})

  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(+ foo baz)
                             :text-decoration-index 2
                             :type                  :error}
    :callout-opts           {:type :error
                             :label "ERROR: ClassCastException"}})


  (println)
  (println)
  (println)
  (print-comment
     ";; Below is an example of a custom warning template with")
  (print-comment
     ";; a point-of-interest diagram. See readme for more details.")
  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(myfun foo baz)
                             :type                  :warning
                             :text-decoration-index 2}
    :callout-opts           {:type        :warning
                             :label-theme :marquee
                             :label       "WARNING: Invalid arg value"}})

  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(+ foo baz)
                             :text-decoration-index 2
                             :type                  :error}
    :callout-opts           {:type        :error
                             :label-theme :marquee
                             :label       "ERROR: ClassCastException"}})


  ;; Hyperlink support ---------------------------------------------------------
  (println)
  (println)
  (printer (bling [{:href            "http://example.com"}
                   "cmd + click to follow this hyperlink"]))


  ;; Underline styles ----------------------------------------------------------
  (println)
  (println)
  (printer (bling [:underline "underline"]
                  "\n"
                  [:double-underline "double-underline"]
                  "\n"
                  [:wavy-underline "wavy-underline"]
                  "\n"
                  [:dotted-underline "dotted-underline"]
                  "\n"
                  [:dashed-underline "dashed-underline"]))
  

  ;; Combo styles ------------------------------------------------------------
  (println)
  (println)
  (printer (bling [:bold.italic "bold & italic"]
                  "\n"
                  [:italic.blue "italic & colored"]
                  "\n"
                  [:bold.italic.white.blue-bg
                   "bold & italic & colored & colored-bg"]
                  "\n"
                  [:bold.italic.blue.underline
                   "bold & italic & colored & underline"]
                  "\n"
                  [:bold.italic.blue.strikethrough 
                   "bold & italic & colored & strikethrough"]
                  ))

  
  ;; All the colors ----------------------------------------------------------
  (println)
  (println)
  (all-the-colors))



;; -----------------------------------------------------------------------------
;; Banners
;; -----------------------------------------------------------------------------

(defn gf [sym]
  (get bling.fontlib/fonts-by-sym sym))

;; fonts from fontlib, for dev
#_(do
  (def miniwi (gf 'miniwi))
  (def ansi-shadow (gf 'ansi-shadow))
  (def drippy (gf 'drippy))
  (def big (gf 'big))
  (def big-money (gf 'big-money))
  (def rounded (gf 'rounded))
  (def isometric-1 (gf 'isometric-1))
    )

;; fonts from fonts, for release
(do 
  (def miniwi bling.fonts/miniwi)
  (def ansi-shadow bling.fonts/ansi-shadow)
  (def drippy bling.fonts/drippy)
  (def big bling.fonts/big)
  (def big-money bling.fonts/big-money)
  (def rounded bling.fonts/rounded)
  (def isometric-1 bling.fonts/isometric-1))

(def example-text-by-font-sym
  {'miniwi      "Miniwi"
   'ansi-shadow "Ansi"
   'drippy      "Drippy"
   'big         "Big"
   'big-money   "Money"
   'rounded     "Rounded"
   'isometric-1 "ABCDE"})

;; For QA
;; Prints one banner per font, in neutral
(defn print-bling-banner-font-samples! []
 (doseq [{:keys [font] :as m}
         [
          {:font     miniwi
           :gradient "to right, purple, orange"
           :contrast :high}
          {:font     ansi-shadow
           :gradient "to top, warm, cool"
           :contrast "high"}
          {:font     drippy
           :gradient "to bottom, red, magenta"}
          {:font        big
           :gradient    "to top, yellow, purple"}
          {:font     big-money
           :gradient "to top, green, blue"}
          {:font           rounded
           :gradient       "to left, cool, warm"
           :gradient-shift 2}
          {:font     isometric-1
           :font-weight :bold
           :gradient "to right, red, magenta"}
          ]]
   (do
     (println (str "\n\n"
                   (symbol (str "bling.fonts/" (:font-sym font)))
                   "\n"))
     (println (bling.banner/banner
               (assoc m
                      :text
                      (->> font
                           :font-sym
                           (get example-text-by-font-sym)))))

     ;; For showing upper-case
     #_(when-not (contains? #{"ANSI Shadow" "Isometric 1" "Drippy"} font-name)
         (println (bling.banner/banner 
                   {:font font
                    :text (string/upper-case text)}))))))

;; For QA
;; Prints every bling banner font (all the characters), over six rows
(defn print-bling-banner-font-collection! []
 (doseq [font
         [miniwi
          ansi-shadow
          drippy
          big
          big-money
          rounded
          isometric-1]
        :let [gs 3
              ;; fw "normal"
              fw "bold"]]
  (dotimes [n (count bling.fontlib/ascii-chars-partitioned-6-rows-vec)]
    (let [t (nth bling.fontlib/ascii-chars-partitioned-6-rows-vec n nil)]
     (bling! 
      (when (zero? n)
       (bling "\n"
              "\n"
              "\n"
              "===== " [:bold (:font-name font)] " ============================" 
              "\n"
              "\n"))
      (bling.banner/banner 
       {:font           font
        :text           t
        :font-weight    fw
        :gradient-shift gs}))))))

;; Prints all gradients 
(defn print-bling-banner-gradients! 
  [{:keys [select-fonts display-labels?]}]
  (doseq [[_ {:keys [font-name]
              :as   font}]
          (if (seq select-fonts)
            (select-keys bling.fontlib/fonts-by-sym select-fonts)
            bling.fontlib/fonts-by-sym)]
    (doseq [[k v] bling.banner/gradient-pairs-map]
      (doseq [direction ["right" "left" "top" "bottom"]]
        (let [grd (str "to " direction ", " (name k) ", " (name v))]
          (when display-labels? (println grd))
          (bling! (bling.banner/banner 
                   {:font     font
                    :text     (->> font
                                   :font-sym
                                   (get example-text-by-font-sym))
                    :gradient grd})))))))



;; Prints cool to warm gradients with shifts
(defn print-bling-banner-gradient-warm-cool! []
  (doseq [n (range 6)]
    (let [dir "to right"]
      (println (str "\"" dir ", cool, warm\", with gradient-shift " n))
      (bling! (bling.banner/banner 
               {:font           bling.fonts/ansi-shadow
                :font-weight    :bold
                :text           "ABCDEFG"
                :gradient       (str dir ", cool, warm")
                :gradient-shift n
                :contrast       :medium
              ;; :display-missing-chars? true
                })))))

;; For QA
;; Print with bold font
(defn print-bling-banner-bold-font! []
  (doseq [k [:bold :normal]]
    (bling! (bling.banner/banner 
             {:font        bling.fonts/big-money
              :font-weight k
              :gradient    "to bottom, red, magenta"
              :text        "ABCDEFG"}))))


;; For QA
;; Print with contrast options
(defn print-bling-banner-gradient-contrast-options! []
  (doseq [[k v] bling.banner/gradient-pairs-map]
    (let [grd (str "to bottom, " (name k) ", " (name v))]
      (doseq [k [:high :medium :low]]
        (println (str "\"" grd "\", with " (name k) " contrast"))
        (bling! (bling.banner/banner 
                 {:font     bling.fonts/ansi-shadow
                  :gradient grd
                  :contrast k
                  :text     "ABCDEFG"}))))))

;; For QA
;; Print with bad input for each option, to test warnings 
(defn print-bling-banners-with-bad-option-values! []
  (doseq [m
          [
          ;;  {:letter-spacing "example-bad-option-value"}
          ;;  {:gradient-shift "example-bad-option-value"}
          ;;  {:text 'example-bad-option-value}
          ;;  {:font 'example-bad-option-value}
          ;;  {:font-weight :bbold}
           {:gradient "to lleft, green, blue"}
           ]]
    (println (str "\nBad option for " (ffirst m)))
    (bling! (bling.banner/banner 
             (merge {:font bling.fonts/ansi-shadow
                     :text "TEST"}
                    m)))))

(defn print-bling-banner-samples! []
  (doseq [k [:bold :normal]]
    (bling! (bling.banner/banner 
             {:font        bling.fonts/big-money
              :font-weight k
              :gradient    "to bottom, red, magenta"
              :text        "ABCDEFG"}))))


;; Make example figlets for banner images

;; Make exhaustive banner example image

;; docs for banner

(println "\n")

;; Prints the entire font collection
;; (print-bling-banner-font-collection!)

;; (print-bling-banner-font-samples!)

(print-bling-banner-gradients! 
 {
  :select-fonts ['isometric-1]
  :display-labels? false})

;; (print-bling-banner-gradient-warm-cool!)
;; (print-bling-banner-bold-font!)
;; (print-bling-banner-gradient-contrast-options!)
;; (print-bling-banners-with-bad-option-values!)


(println)

;; (pprint
;;  (get-in bling.fonts/isometric-1 
;;          [:chars-array-map "a"]))

(println)

;; (pprint (dissoc bling.fonts/isometric-1
;;                 :chars-array-map
;;                 :example
;;                 :desc
;;                 :missing-chars))

;; (println
;;  (banner {:text     "A" 
;;           :font     bling.fonts/isometric-1
;;           :gradient "to top, red, magenta"}))

