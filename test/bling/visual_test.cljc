
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
                                 bling-colors*]]
   [fireworks.core :refer [? !? ?> !?> pprint] :rename {pprint fwpp}]
   [fireworks.sample :refer [array-map-of-everything-cljc]]
   [clojure.pprint :refer [pprint]]
   #_[bling.sample :as sample]
   [bling.util :as util :refer [maybe->> maybe->]]
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
   [bling.visual-test-impl :refer [random-callouts bling-basics]]))


;; (tufte/add-handler! :my-console-handler (tufte/handler:console))

  
;;------------------------------------------------------------------------------
;; Testing sequence
;;------------------------------------------------------------------------------

(def filter-themes
  #{
    #_{:theme :sideline}
    #_{:theme :sandwich}
    #_{:theme           :sandwich
     :border-notches? true}
    #_{:theme :gutter}
    #_{:theme :boxed}})

(def filter-options
  #{
    ;; :label
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
  
    #_
    (bling.cycle/callout-option-sequences 
     {:padding-frame-rate padding-frame-rate
      :label-theme        :simple
      :filter-themes      filter-themes
      :filter             filter-options})

  ;; Test with :theme :box
  (bling.cycle/callout-option-sequences 
   {:padding-frame-rate 75
    :filter-themes      #{{:theme :boxed}}
    :filter             filter-options}))


#_(let [m {:border-style    :solid
         :label           "Hello"
        ;;  :side-label      "foo.cljs:11:12"
        ;;  :label-theme     :simple
         :label-theme     :marquee
         :theme :sideline-bold
        ;;  :theme           :sandwich
        ;;  :theme           :gutter
        ;;  :theme           :boxed
        ;;  :border-notches? true
         ;; :padding-inline  3
        ;;  :padding-top     3
        ;;  :margin-left     0
         :colorway        :subtle
        ;;  :min-width 30
        ;;  :width 20
        ;;  :margin-left 5
        ;;  :width :auto
         }
      m2 (assoc m :theme :sandwich)]

  (callout m (bling.hifi/hifi m))
  
  #_(callout m2 (bling.hifi/hifi m2))
  )


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





