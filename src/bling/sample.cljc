(ns bling.sample
  (:require 
   ;; [bling.macros :refer [let-map keyed ?]] ;; <-- just for debugging
   [fireworks.core :refer [? !? ?> !?> pprint]]
   [clojure.string :as string]
   [bling.banner :refer [banner]]
   [bling.fontlib]
   [bling.fonts]
   [bling.macros :refer [keyed
                         start-dbg!
                         stop-dbg!
                         nth-not-found]]
   [bling.explain :refer [explain-malli]]
   [bling.core :refer [?sgr
                       bling
                       bling-data
                       bling-data*
                       callout
                       print-bling
                       point-of-interest]]))

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
     (when label (print-bling "\n" [:italic label] "\n"))
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
   (defn print-commented-example-call [m k body]
     (println 
      (str
       "\n\n\n\n"
       (bling [k ";; (callout "])
       (string/join
        (bling [k "\n;; "]) 
        (map-indexed 
         (fn [i s]
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
       #_(bling [k "\n;; =>\n"])
       (bling [k "\n"])))))


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
      #?(:clj (print-commented-example-call m k body))
      (callout (assoc m :margin-top 0 :margin-bottom 0) body)))


(defn print-comment [s]
  (printer (bling [:italic.subtle s])))


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


;; For QA
;; Prints one banner per font, in neutral
(defn print-bling-banner-font-samples []
 (doseq [{:keys [font] :as m}
         [
          {:font               :miniwi
           :gradient-colors    [:purple :orange]
           :gradient-direction :to-right}
          {:font               :ansi-shadow
           :gradient-colors    [:warm :cool]
           :gradient-direction :to-top}
          {:font               :drippy
           :gradient-colors    [:red :magenta]
           :gradient-direction :to-bottom}
          {:font               :big
           :font-weight        :bold
           :gradient-colors    [:yellow :purple]
           :gradient-direction :to-top}
          {:font               :big-money
           :gradient-colors    [:green :blue]
           :gradient-direction :to-top}
          {:font               :rounded
           :gradient-colors    [:cool :warm]
           :gradient-direction :to-left
           :gradient-shift     2}
          {:font        :isometric-1
           :font-weight :bold
           :gradient-colors    [:red :magenta]
           :gradient-direction :to-right}
          ]]
   (do
     (println (str "\n\n"
                   font
                   "\n"))
     (println (bling.banner/banner
               (assoc m
                      :text
                      (or (some->> font
                                   (get bling.fonts/fonts-by-kw)
                                   :example-text)
                          "Example"))))

     ;; For showing upper-case
     #_(when-not (contains? #{"ANSI Shadow" "Isometric 1" "Drippy"} font-name)
         (println (bling.banner/banner 
                   {:font font
                    :text (string/upper-case text)}))))))

;; For QA
;; Prints every bling banner font (all the characters), over six rows
(defn print-bling-banner-font-collection []
 (doseq [font
         [:miniwi
          :ansi-shadow
          :drippy
          :big
          :big-money
          :rounded
          :isometric-1]
        :let [gs 3
              ;; fw "normal"
              fw :bold]]
  (dotimes [n (count bling.fontlib/ascii-chars-partitioned-6-rows-vec)]
    (let [t (nth bling.fontlib/ascii-chars-partitioned-6-rows-vec n nil)]
     (print-bling 
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
;; TODO use the actual font
;; - put :example text in the font
;; 
(defn print-bling-banner-gradients 
  [{:keys [select-fonts display-labels?]}]
  (doseq [[font-kw font]
          (if (seq select-fonts)
            (select-keys bling.fonts/fonts-by-kw select-fonts)
            bling.fonts/fonts-by-kw)]
    (doseq [[k v] bling.banner/gradient-pairs-map]
      (doseq [direction [:to-right :to-left :to-top :to-bottom]]
        (let [opts {:gradient-colors    [k v]
                    :gradient-direction direction
                    :font               font-kw
                    :text               (or (some->> font :example-text)
                                            "Example")}]
          (when display-labels?
            (println)
            (pprint (assoc opts :font font-kw) {:max-width 33})
            (println))
          (print-bling 
           (bling.banner/banner opts)))))))


;; Prints cool to warm gradients with shifts
(defn print-bling-banner-gradient-warm-cool []
  (println "\n")
  (doseq [n (range 6)]
    (let [opts {:gradient-colors    [:cool :warm]
                :gradient-direction :to-right
                :gradient-shift     n}
          ]
      (pprint opts)
      (println)
      (print-bling (bling.banner/banner 
                    (merge opts
                           {:font          bling.fonts/ansi-shadow
                            :font-weight   :bold
                            :text          "ABCDEFG"
                            :contrast      :medium
                            :margin-bottom 1
                            ;; :display-missing-chars? true
                            }))))))

;; For QA
;; Print with bold font
(defn print-bling-banner-bold-font []
  (println "\n\n")
  (doseq [k [:bold :normal]]
    (let [opts {:font               :big-money
                :font-weight        k
                :gradient-colors    [:cool :warm]
                :gradient-direction :to-right
                :text               "ABCDEFG"}]
      (pprint opts)
      (println)
      (print-bling (bling.banner/banner opts)))))

;; For QA
;; Print with contrast options
(defn print-bling-banner-gradient-contrast-options []
  (doseq [[k v] bling.banner/gradient-pairs-map]
    (let [grd (str "to bottom, " (name k) ", " (name v))]
      (doseq [k [:high :medium :low]]
        (let [opts {:gradient-colors    [:cool :warm]
                    :gradient-direction :to-bottom
                    :contrast           k}]
          (pprint opts)
          (println)
          (print-bling (bling.banner/banner 
                        (merge opts 
                               {:font     :ansi-shadow
                                :gradient grd
                                :contrast k
                                :text     "ABCDEFG"}))))))))

;; For QA
;; Print with bad input for each option, to test warnings 
(defn print-bling-banners-with-bad-option-values []
  (doseq [m
          [
          ;;  {:letter-spacing "example-bad-option-value"}
          ;;  {:gradient-shift "example-bad-option-value"}
          ;;  {:text 'example-bad-option-value}
          ;;  {:font 'example-bad-option-value}
          ;;  {:font-weight :bbold}
          ;;  {:gradient "to lleft, green, blue"}
           {:gradient-colors    [:cool :warms]
            :gradient-direction :to-right
            :gradient-shift     "s"}
           ]]
    (println (str "\nBad option for " (ffirst m)))
    (print-bling (bling.banner/banner 
                  (merge {:font :ansi-shadow
                          :text "TEST"}
                         m)))))


(defn print-bling-color-contrast []
  (doseq [color 
          ["red"    
           "orange" 
           "yellow" 
           "olive"  
           "green"  
           "blue"   
           "purple" 
           "magenta"
           "gray"]]
    (doseq [contrast [:low :medium :high ]]
      (println (bling [{:contrast         contrast 
                        :color            color}
                       (str "Contrast " contrast)])))))  


(def Address
  [:map
   [:id string?]
   [:tags [:set keyword?]]
   [:address
    [:map
     [:street string?]
     [:city string?]
     [:zip int?]
     [:lonlat [:tuple double? double?]]]]])

(def Address-quoted
  '[:map
    [:id string?]
    [:tags [:set keyword?]]
    [:address
     [:map
      [:street string?]
      [:city string?]
      [:zip int?]
      [:lonlat [:tuple double? double?]]]]])

(defn- print-explain-malli-example-header
  ([s v]
   (print-explain-malli-example-header s v nil))
  ([s v opts]
   (println 
    (str "\n"
         s
         "\n\n"
         (with-out-str
           (pprint 
            (if opts
              (list 'explain-malli Address-quoted v opts)
              (list 'explain-malli Address-quoted v))))))))

(defn explain-malli-missing-map-key
  []
  (let [v {:id      "Lillan"
           :tags    #{:artesan :coffee :garden}
           :address {:street "Ahlmanintie 29"
                     :zip    33100
                     :lonlat [61.4858322, 87.34]}}]
    (print-explain-malli-example-header
     "The result of bling.core/explain-malli, highlighting a collection with a missing key."
     v)
    (explain-malli Address v)))

(defn explain-malli-default []
  (let [v {:id      "Lillan"
           :tags    #{:artesan "coffee" :garden}
           :address {:street "Ahlmanintie 29"
                     :city   "Tempare"
                     :zip    33100
                     :lonlat [61.4858322, 87.34]}}]
    (print-explain-malli-example-header
     "The result of bling.core/explain-malli, with default options:"
     v)
    (explain-malli Address v)))


(defn explain-malli-no-schema []
  (let [v {:id      "Lillan"
           :tags    #{:artesan "coffee" :garden}
           :address {:street "Ahlmanintie 29"
                     :city   "Tempare"
                     :zip    33100
                     :lonlat [61.4858322, 87.34]}}
        opts {:display-schema? false}]
    (print-explain-malli-example-header
     "The result of bling.core/explain-malli, with :display-schema? set to false:"
     v
     opts)
    (explain-malli Address v opts)))


(defn explain-malli-with-source-info []
  (let [v {:id      "Lillan"
           :tags    #{:artesan "coffee" :garden}
           :address {:street "Ahlmanintie 29"
                     :city   "Tempare"
                     :zip    33100
                     :lonlat [61.4858322, 87.34]}}
        opts {:file            "myns.core"
              :function-name   "my-function"
              :line            21
              :column          33
              ;; :display-explain-data? true
              ;; :spacing         :compact
              }]

    (print-explain-malli-example-header
     "The result of bling.core/explain-malli, with :display-schema? set to false:"
     v
     opts)
    (explain-malli Address v opts)))


(defn explain-malli-with-explain-data []
  (let [v {:id      "Lillan"
           :tags    #{:artesan "coffee" :garden}
           :address {:street "Ahlmanintie 29"
                     :city   "Tempare"
                     :zip    33100
                     :lonlat [61.4858322, 87.34]}}
        opts {:file            "myns.core"
              :function-name   "my-function"
              :line            21
              :column          33
              :display-explain-data? true
              ;; :spacing         :compact
              }]
    (print-explain-malli-example-header
     "The result of bling.core/explain-malli, with :display-schema? set to false:"
     v
     opts)
    (explain-malli Address v opts)))


(defn explain-malli-examples []
  (explain-malli-default)
  (explain-malli-missing-map-key)
  (explain-malli-no-schema)
  (explain-malli-with-source-info)
  (explain-malli-with-explain-data))



;; Make example figlets for banner images

;; Make exhaustive banner example image

;; docs for banner

