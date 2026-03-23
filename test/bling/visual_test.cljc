;; Namespace for visual testing and sandbox dev during development

(ns bling.visual-test
  (:require
   #?(:cljs [bling.js-env :refer [node?]])
   [bling.ansi]
   [bling.core :as bling :refer [?sgr
                                 !?sgr
                                 bling
                                 print-bling
                                 callout
                                 point-of-interest
                                 bling-colors*
                                 stringified
                                 highlighted-location
                                 with-floating-label
                                 with-ascii-underline]]
   [fireworks.core :refer [? !? ?> !?> pprint] :rename {pprint fwpp}]
   [fireworks.defs]
   [fireworks.util]
   [fireworks.state]
   [fireworks.sample :refer [array-map-of-everything-cljc]]
   [clojure.pprint :refer [pprint]]
   [bling.sample :as sample]
   [bling.util :as util :refer [maybe->> maybe-> when-> when->>]]
   [bling.defs]
   [bling.explain]
   [bling.fonts]
   [bling.fonts.miniwi :refer [miniwi]]
   [bling.fonts.ansi-shadow :refer [ansi-shadow]]
   [bling.fonts.drippy :refer [drippy]]
   [bling.fonts.big :refer [big]]
   [bling.fonts.big-money :refer [big-money]]
   [bling.fonts.rounded :refer [rounded]]
   [bling.fonts.isometric-1 :refer [isometric-1]]
   [bling.fontlib]
   [bling.banner]
   [bling.browser :as browser]
  ;;  [bling.hifi :refer [print-hifi hifi chopped]]
   [malli.core :as m]
   [clojure.string :as string]
   ;; [taoensso.tufte :as tufte :refer [p profile]]
   [lasertag.core :refer [tag-map]]

   ;; testing sequence of variants / options
   [bling.cycle]

   ;; impl ns for the stuff in the visual test suite 
   [bling.visual-test-impl :refer [random-callouts bling-basics]]
   
   [fireworks.ansi :as ansi]
   [fireworks.state :as state]))



;; (callout {:type :info}
;;          "Example callout"
;;          "\n"
;;          "with :type of :info")

;; (callout {:type  :info
;;           :label "My custom label"}
;;          "Example callout"
;;          "\n"
;;          "with :type of :info and custom :label")

;; (callout {:type :warning}
;;          "Example callout"
;;          "\n"
;;          "with :type of :warning")

;; (callout {:type :error}
;;          "Example callout"
;;          "\n"
;;          "with :type of :error")

;; (callout {:colorway :positive}
;;          "Example callout"
;;          "\n"
;;          "with :colorway of :positive")

;; (callout {:colorway :subtle}
;;          "Example callout"
;;          "\n"
;;          "with :type of :subtle (or :gray)")

;; (callout {:colorway :magenta}
;;          "Example callout"
;;          "\n"
;;          "with :type of :magenta")


;; (callout "ライブラリは中国語、日本語、韓国語のテキストをサポートしています")
#_(println (bling.banner/banner
          {
          ;;  :gradient-colors    [:green :blue]
          ;;  :gradient-direction :to-bottom
           :text               "b"
          ;;  :font               ansi-shadow
           :font               big 
           }))



#_(?  {:print-with println} (with-floating-label
         "One
Two
Three
Four"
         {:line-index 4
          :label-text "<-foo"}))

#_(let [js-console-array
      (? :+ (-> (point-of-interest
                 {:margin-top             1
                  :header-file-info-style {:font-style :italic}
                  :form                   (let [s (bling.hifi/hifi '(+ 1 true))]
                                            (-> s
                                                (with-ascii-underline 
                                                  (assoc {:line-index 0}
                                                         :text-decoration-color
                                                         :red))))
                  :file                   "foo"
                  :line                   111
                  :column                 33})
                browser/ansi-sgr-string->browser-dev-console-array))

      ;; [formatted & styles] 
      ;; js-console-array

      ]
  ;; (? (->> formatted (re-seq #"%") count))
  )


#_(bling.core/print-bling
 "Example of callout body with Bling styling."
 "\n\n"
 "Line with " [:bold.purple "bold purple"] " and " [:italic.blue "italic blue"] " text"
 "\n\n"
 [:p "Lines in a " [:blue ":p"] " hiccup tag will insert a trailing\n"
  [:olive "\"\\n\\n\""] " for spacing between paragraphs."]
 [:p
  "Example hyperlink -> "
  [{:href "https://github.com/paintparty/bling"} "Official Bling docs"]])

#_(require 'bling.hifi)

#_(callout {
          :type  :info
          :border-notches? false
          :border-shape :round
          ;; :theme :boxed
          ;; :theme :sideline
          ;; :theme :sandwich
          ;;  :theme :gutter
          :label-theme :marquee

          }
         (bling.core/bling
          "Example of callout body with Bling styling aasfasdf."
          "\n\n"
          "Line with " [:bold.purple "bold purple"] " and " [:italic.blue "italic blue"] " text"
          "\n\n"
          [:p "Lines in a " [:blue ":p"] " hiccup tag will insert a trailing " [:olive "\"\\n\\n\""] ]

          ;; TODO fix this error
          #_[:p "Example hyperlink -> " [{:href "https://github.com/paintparty/bling"} "Official Bling docs"]]))

#_(bling.core/callout {:label           "duude"
                    ;;  :type            :warning
                    :theme           :sandwich
                    ;;  :theme           :sideline
                    ;;  :theme           :gutter
                     :label-theme     :marquee
                    ;;  :label-theme     :minimal
                     :border-notches? true
                     :border-shape    :round
                     :side-label      "myreallylongadfsd.ns.core:44:2"
                     :width           50}
                    "Callout body"
                    (point-of-interest
          {:margin-top 1
           :header-file-info-style {:font-style :italic}
           :form (let [s (bling.hifi/hifi '(+ 1 true))]
                   (-> s
                       (with-ascii-underline 
                         (assoc {:line-index 0}
                                :text-decoration-color
                                :red))))

           #_(let [s   (hifi {:a 1
                              :b [333 444 555]
                              :c "aadfasdfasdfads"
                              :d "asdfasdfasdfasdfasdfasdf"} 
                             {:find {:path [:b 1]}})
                   loc (highlighted-location s :highlight-error)]
               (-> s
                   (with-floating-label {:label-text   "<- Foo"
                                         :label-offset 5 
                                         :label-style  {:color :red}
                                         :line-index   (:line-index loc)})
                   (with-ascii-underline 
                     (assoc {:line-index 1}
                            :text-decoration-color :red)))) 
           :file   "foo"
           :line   111
           :column 33
           #_(bling.hifi/hifi
              {:foo {:bar [12345
                           :asfasdfasdfsdfasdfasz
                           'aafasfasd]}
               :bang 23
               :bow 55}
              {:find {:path  [:foo :bar]
                      :class :highlight-error}})})
                    "More information can go here.")



#_(println
 (let [s   (hifi {:a 1
                  :b [333 444 555]
                  :c "aadfasdfasdfads"
                  :d "asdfasdfasdfasdfasdfasdf"} 
                 {:find {:path [:b 1]
                         ;;  :class :highlight-error
                         ;;  :style fireworks.defs/highlight-error-dark
                         }})
       loc (highlighted-location s :highlight-error)]
   (-> s
       (with-floating-label {:label-text   "<- Foo"
                             :label-offset 5 
                             :label-style  {:color :red}
                             :line-index   (:line-index loc)})
       (with-ascii-underline (assoc {:line-index 1} #_loc :text-decoration-color :red)))))

;; \033[38;2;255;255;255;1;48;2;0;0;224m
;; \033[38;5;231;1;48;5;20m

#_(println (point-of-interest
          {:form   (stringified {:a 1
                                 :b [333 444 555]
                                 :c "aadfasdfasdfads"
                                 :d "asdfasdfasdfasdfasdfasdf"}
                                {
                                 :printing-fn pprint
                                ;;  :height      3
                                ;;  :width       "fu"
                                 })
           #_(let [s   (hifi {:a 1
                              :b [333 444 555]
                              :c "aadfasdfasdfads"
                              :d "asdfasdfasdfasdfasdfasdf"} 
                             {:find {:path [:b 1]}})
                   loc (highlighted-location s :highlight-error)]
               (-> s
                   (with-floating-label {:label-text   "<- Foo"
                                         :label-offset 5 
                                         :label-style  {:color :red}
                                         :line-index   (:line-index loc)})
                   (with-ascii-underline 
                     (assoc {:line-index 1}
                            :text-decoration-color :red)))) 
           :file   "foo"
           :line   111
           :column 33
           #_(bling.hifi/hifi
              {:foo {:bar [12345
                           :asfasdfasdfsdfasdfasz
                           'aafasfasd]}
               :bang 23
               :bow 55}
              {:find {:path  [:foo :bar]
                      :class :highlight-error}})}))



;;------------------------------------------------------------------------------
;; Testing sequence
;;------------------------------------------------------------------------------

;; TODO - Add highlights (primary, secondary and option) to the options map of
;;        the printed function call.


#_(bling.cycle/variants
 bling.core/point-of-interest
 {:animate?       true
  :print-desc?    false
  :print-fn-call? true
  :primary        :form})

(def filter-themes
  #{#_{:theme :sideline}
    #_{:theme :sandwich}
    #_{:theme           :sandwich
       :border-notches? true}
    #_{:theme :gutter}
    #_{:theme :boxed}})

(def filter-options
  #{;; :label
    ;; :side-label
    :colorway
    ;; :border-style
    ;; :border-shape
    ;; :border-weight
    ;; :margin-left
    ;; :width
    ;; :padding-block
    ;; :padding-top
    ;; :padding-bottom
    ;; :padding-inline
    ;; :padding-left
    ;; :padding-right
    })

(def padding-frame-rate 75)

(do
    ;; Test with :label-theme :marquee

    #_(bling.cycle/callout-option-sequences
       {:padding-frame-rate padding-frame-rate
        :label-theme        :marquee
        :filter-themes      filter-themes
        :filter             filter-options})

    ;; Test with :label-theme :simple

    #_(bling.cycle/callout-option-sequences
       {:padding-frame-rate padding-frame-rate
        :label-theme        :simple
        :filter-themes      filter-themes
        :filter             filter-options})

    ;; Test with :theme :box
    #_(bling.cycle/callout-option-sequences
     {:padding-frame-rate 75
      :filter-themes      #{{:theme :boxed}}
      :filter             filter-options}))


#_(let [m  {:border-style :solid
          :label        (hifi 'bling.core/callout)
          ;;  :side-label      "foo.cljs:11:12"
          ;;  :label-theme     :simple
          :label-theme  :marquee
          :theme        :sideline-bold
          ;;  :theme           :sandwich
          ;;  :theme           :gutter
          ;;  :theme           :boxed
          :border-notches? true
          :border-shape :round
          ;; :padding-inline  3
          ;;  :padding-top     3
          ;;  :margin-left     0
          :colorway     :subtle
          ;;  :min-width 30
          ;;  :width 20
          ;;  :margin-left 5
          ;;  :width :auto
          :find         {:path  [:label]
                         :class :highlight-error-underlined}
          }
      m2 (assoc m :theme :sandwich)]

  (callout m
           (
            bling.core/with-ascii-decoration
            #_identity
            (bling.hifi/hifi m
                             {
                              ;;  :margin-inline-start 5
                              :find {:path  [:border-shape]
                                     :class :highlight-error}})))

  #_(callout m2 (bling.hifi/hifi m2)))


;;------------------------------------------------------------------------------
;; Visual test suite 
;;------------------------------------------------------------------------------

(defn visual-test-suite []
  #_(random-callouts)
  #_(bling-basics))

#_(visual-test-suite)

#_(sample/explain-malli-examples)

#_(bling.sample/sample)

#_(sample/print-bling-banner-font-collection)

#_(sample/print-bling-banner-font-samples)

#_(sample/print-bling-banner-gradients
   {:select-fonts [:isometric-1
                   :ansi-shadow]
    :display-labels? true})

#_(sample/print-bling-banner-gradient-warm-cool)

#_(sample/print-bling-banner-bold-font)

#_(sample/print-bling-banner-gradient-contrast-options)

#_(sample/print-bling-banners-with-bad-option-values)

#_(pprint
   (get-in isometric-1
           [:chars-array-map "a"]))

#_(sample/print-bling-color-contrast)

(println (sample/explain-malli-examples))





#_(bling.explain/explain-malli*
 [:map-of
  [:or :keyword :int]
  [:or :string :keyword]]
 {"wtf"   :key
  1     "hiasfdasdfasdfasdfasdfas"
  :bars "hiasfdasdfasdfasdfasdfas"}
 (merge {:display-schema? false
         :form            {:foo :key
                           :bar "hi"}
         :spacing         :compact
         :callout-opts    (assoc {} 
                                 :label-theme
                                 :marquee)}
        {}))

#_(defn my-error-callout [{:keys [header body source]}]
  (callout {:type        :error
            :theme       :gutter
            :margin-left 2
            :padding-top 1}
           header
           source
           body))

#_(let [bad-form '(+ 1 true)]
  (my-error-callout
     {:header "Your header message goes here\n"
      :source (point-of-interest 
               {:type   :error
                :file   "example.ns.core"
                :line   11
                :column 1
                :form   (with-ascii-underline
                          (str bad-form)
                          {:line-index            0
                           :text-decoration-color :red})})
      :body   (str "The body of your template goes here.\n"
                   "Second line of copy.\n"
                   "Another line.")}))
