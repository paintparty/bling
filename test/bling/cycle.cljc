(ns bling.cycle
  (:require
   [bling.ansi]
   [bling.core :as bling :refer [bling callout]]
   [bling.defs]
   [bling.explain]
   [bling.fonts]
   [bling.fontlib]
   [bling.banner]
   [bling.hifi :refer [print-hifi hifi chopped]]))

;;------------------------------------------------------------------------------
;; Testing sequence stuff begin
;;------------------------------------------------------------------------------

#_(print-callouts {:label-theme :minimal})
(def sample-label "Hello, the quick brown fox jumped over the lazy dog: Part I & II")
(def sample-side-label "foo_bar/baz/core.cljs:11:9123456789098765432109987663")
(def themes* [{:theme :sideline} {:theme :minimal} {:theme :minimal :border-notches? true}])
;; (def themes (take 100 (cycle themes*)))
(def weights* [:normal :bold])
(def weights (take 100 (mapcat #(repeat 50 %) weights*)))
(def styles* [:solid :double])
(def styles (take 100 (cycle styles*)))
(def block-paddings* (mapcat #(repeat 10 %) (range 0 4)))
(def block-paddings (take 100 (cycle (concat block-paddings* (reverse block-paddings*)))))
(def inline-paddings* (mapcat #(repeat 5 %) (range 0 4)))
(def inline-paddings (take 100 (cycle (concat inline-paddings* (reverse inline-paddings*)))))
(def label-lengths* (range 51 0 -1))
(def label-lengths (concat label-lengths* (reverse label-lengths*)))
(def side-label-lengths* (range 1 25))
(def side-label-lengths (take 100 (cycle (concat side-label-lengths* (reverse side-label-lengths*)))))
#_(def colorways (take 100 (cycle (mapcat #(repeat 10 %) (keys bling.core/bling-colors)))))
(def colorways (take 100 (cycle (mapcat #(repeat 10 %) [:neutral :blue :green :yellow :orange :red]))))

(defn clear-screen []
  (print "\033[H\033[2J")
  (flush))

(defn opts-as-body [m]
  (bling [:p
          "Example "
          [:blue (:theme m)]
          " theme, with "
          [:blue (:label-theme m)]
          " label"]
         (bling.hifi/hifi (list 'callout m))))

(defn print-callout+opts [m om]
  (callout m (opts-as-body m))
  #_(bling.hifi/print-hifi (list 'callout m "Callout body goes here"))

  (println)
  (println)

  ;; (callout (merge m om) (body (merge m om)))
  #_(bling.hifi/print-hifi (list 'callout (merge m om) "Callout body goes here"))
  ;; (println)
  ;; (println)
  )



(defn print-callout-loop [m] 
  (dotimes [i 100]
    (clear-screen)
    (print-callout+opts
     (merge {:label (subs sample-label 0 (nth label-lengths i))
             :side-label (subs sample-side-label 0 (nth side-label-lengths i)) 
             :colorway (nth colorways i)
             :padding-block (nth block-paddings i)
             :padding-inline (nth inline-paddings i)
             :border-weight (nth weights i)
             ;; :border-style (nth styles i)
             }
            m)
     {})
    #?(:clj (Thread/sleep 50))))

#_(doseq [theme themes*]
  (doseq [k [:minimal :marquee]]
    (print-callout-loop (merge {:border-style :solid
                                :label-theme  k}
                               theme))
    (print-callout-loop (merge {:border-style :double
                                :label-theme  k}
                               theme))))

(defn pco [base-m
           theme-m
           {:keys [animate? frame-rate body-f om]}]
  (when animate? (clear-screen))
  (println "\n\n\n\n\n")
  (let [m (merge base-m theme-m om)]
    (callout m (body-f m)))
  #?(:clj (when animate? (Thread/sleep (or frame-rate 100)))))


(defn callout-option-sequence
  [{:keys [theme    
           label-theme
           lap?     
           coll     
           option-f 
           k
           om
           frame-rate]}
   {:keys [filter]}]
  (when (or (not (seq filter))
            (contains? filter k))
    (let [base-m {:border-style :solid
                  :label        "Hello"
                  :side-label   "side_label.clj:11:44"
                  :label-theme  label-theme}
          coll   (if lap? (concat coll (reverse coll)) coll)]
      (doseq [i coll]
        (pco base-m
             theme
             {:body-f     (fn [m]
                            (bling.hifi/hifi (merge {k (option-f i coll)}
                                                    om)))
              :om         (merge {k (option-f i coll)}
                                 om)
              :animate?   true
              :frame-rate frame-rate})))))

(defn option-sequences
  [{:keys [padding-frame-rate filter-themes label-theme] 
    :or   {padding-frame-rate 100} 
    :as   opts}]
  (doseq [theme [{:theme :sandwich}
                 {:theme           :sandwich
                  :border-notches? true}
                 {:theme :sideline}
                 {:theme :gutter}
                 {:theme :boxed}]]
    (when (or (not (seq filter-themes))
              (contains? filter-themes theme))
      (doseq [m
              [{:k        :label
                :coll     label-lengths*
                :option-f (fn [i coll] (subs sample-label 0 (nth coll i)))}
               {:k        :side-label
                :coll     side-label-lengths*
                :option-f (fn [i coll] (subs sample-side-label 0 (nth coll i)))}
               {:k    :width
                :om   {:min-width 40}
                :coll (range 30 61)}
               {:k          :margin-left
                :frame-rate padding-frame-rate}
               {:k          :padding-block
                :frame-rate padding-frame-rate}
               {:k          :padding-top
                :frame-rate padding-frame-rate}
               {:k          :padding-bottom
                :frame-rate padding-frame-rate}
               {:k          :padding-inline
                :frame-rate padding-frame-rate}
               {:k          :padding-left
                :frame-rate padding-frame-rate}
               {:k          :padding-right
                :frame-rate padding-frame-rate}
               {:k          :border-style
                :option-f   (fn [i coll] i)
                :coll       styles*
                :lap?       false
                :frame-rate 500}
               {:k          :border-weight
                :option-f   (fn [i coll] i)
                :coll       weights*
                :lap?       false
                :frame-rate 500}
               {:k          :border-shape
                :option-f   (fn [i coll] i)
                :coll       [:round]
                :lap?       false
                :frame-rate 600}
               {:k          :colorway
                :option-f   (fn [i coll] i)
                :coll       (concat (keys bling.core/bling-colors) 
                                    [:subtle])
                :lap?       false
                :frame-rate 100}
               ]]
        (callout-option-sequence
         (merge {:frame-rate  25
                 :label-theme label-theme
                 :lap?        true
                 :coll        [0 1 2 3 4 5]
                 :option-f    (fn [i coll] (nth coll i))
                 :theme       theme}
                m)
         opts)))))
