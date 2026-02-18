
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
                                 with-ascii-decoration]]
   [fireworks.core :refer [? !? ?> !?> pprint] :rename {pprint fwpp}]
   [fireworks.defs]
   [fireworks.util]
   [fireworks.state]
   [fireworks.sample :refer [array-map-of-everything-cljc]]
   [clojure.pprint :refer [pprint]]
   #_[bling.sample :as sample]
   [bling.util :as util :refer [maybe->> maybe-> when->]]
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
   [bling.hifi :refer [print-hifi hifi chopped]]
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

;; (tufte/add-handler! :my-console-handler (tufte/handler:console))


;; [:br] should work stand-alone
(!? (fireworks.defs/bling-sgr-color :dark-red))


     
;; (println
;;  (bling.core/with-ascii-decoration 
;;   (bling.core/bling
;;    [:red "Line 1" [:br]]
;;    [:blue "Line 2" [:br]]
;;    (hifi {:foo {:bar [12345
;;                       :asfasdfasdfsdfasdfasz
;;                       'aafasfasd]}}
;;          {:find {:path  [:foo :bar]
;;                  :class :highlight-error }})
;;    "\n"
;;    "Another line"
;;    "\n"
;;    "Last")))



#_(print-hifi
 {:go []}
 {:non-coll-mapkey-length-limit 30
  :find                         (vec
                                 (remove nil?
                                         [{:path  [:go]
                                           :class :highlight-error}
                                          (when false
                                            {})]))})

#_(println (with-ascii-decoration 
           (bling.core/bling
            [:red "Line 1" [:br]]
            [:blue "Line 2" [:br]]
            (bling.hifi/hifi
             {:foo {:bar [12345
                          :asfasdfasdfsdfasdfasz
                          'aafasfasd]}}
             {:find {:path  [:foo :bar]
                     :class :highlight-error }})
            "\n"
            "Another line"
            "\n"
            "Last")))

(bling.explain/explain-malli*
 [:map-of
  [:or :keyword :int]
  [:or :string :keyword]]
 {"wtf"   :key
  1     "hiasfdasdfasdfasdfasdfas"
  :bars "hiasfdasdfasdfasdfasdfas"
  }
 (merge {
         :display-schema? false
         :form            {:foo :key :bar "hi"}
         :spacing         :compact
         :callout-opts    (assoc
                           {} #_(file+line+col-map (meta &form))
                           :label-theme
                           :marquee)}
        {} #_(file+line+col-map (meta &form))
        #_opts))

;;------------------------------------------------------------------------------
;; Testing sequence
;;------------------------------------------------------------------------------

;; TODO - Add highlights (primary, secondary and option) to the options map of
;;        the printed function call.


#_(bling.cycle/variants
 bling.core/point-of-interest
 {:print-desc?    true
  :print-fn-call? false})

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

#_(do
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
    (bling.cycle/callout-option-sequences
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
  (random-callouts)
  (bling-basics))

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

#_(sample/explain-malli-examples)





