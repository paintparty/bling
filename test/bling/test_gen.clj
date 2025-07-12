(ns bling.test-gen
  (:require
   [fireworks.core :refer [? !? ?> !?> pprint]] ;; <-just for debugging
   [bling.core :as bling :refer [bling]]
   [bling.sample :as sample :refer [callout+]]
   [clojure.string :as string]))

(def visual-mode? #_true false)

(def theme "Alabaster Light")

(def label "my-label")

(def default-options-map {})

(def tests (atom []))

(defn escape-sgr
  "Escape sgr codes so we can test clj output."
  [s]
  (let [_split   "✂"
        _sgr     "〠"
        replaced (string/replace s
                                 #"\u001b\[([0-9;]*)[mK]"
                                 (str _split _sgr "$1" _sgr _split))
        split    (string/split replaced
                               (re-pattern _split))
        ret      (filter seq split)]
    ret))

(defn hifi-impl [x user-opts]
  (->> x 
       (fireworks.core/_p2
        (merge user-opts
               {:user-opts user-opts
                :mode      :data
                :p-data?   true
                :template  [:result]}))
       :formatted
       :string))

(defmacro deftest+ 
  [sym v]
  (let [sym?  (symbol? v)
        list? (list? v)]
    `(do 
       (swap! tests
              conj 
              {:sym  (quote ~sym)
               :v    ~v 
               :qv   (cond ~sym? (quote ~v)
                           ~list? (quote ~v)
                           :else
                           ~v)})
       (when visual-mode?
         (? (quote ~sym) ~v)))))


(defn deftests-str 
  "This creates a string of all of the generated deftests"
  []
  (string/join 
   "\n\n"
   (mapv 
    (fn [{:keys [sym v qv]}]
      (with-out-str 
        (pprint 
         (list 
          'deftest sym
          (list 'is
                (if (-> sym meta :pre-gen)
                  (list '=
                        (concat (list '->
                                      qv
                                      'escape-sgr
                                      'string/join))
                        (-> v
                            escape-sgr
                            string/join))
                  (list '=
                        (concat (list '->
                                      (list 'bling qv))
                                '[escape-sgr string/join])
                        (-> v
                            bling
                            escape-sgr
                            string/join))))))))
    @tests)))


(defn write-tests-ns!
  "This updates/generates a fireworks.test-suite namespace.
      
   Used ocasionally during dev, when tests are modified or added, or functionality
   changes.

   This is intended to be called from repl."
  []
  (spit (str "./test/bling/core_test" ".clj") 
        (str (with-out-str 
               (pprint '(ns bling.core-test
                          (:require
                           [clojure.test :refer [deftest is]]
                           [bling.test-gen :refer [escape-sgr]]
                           [bling.core :as bling :refer [bling]]
                           [bling.sample :as sample :refer [callout+]]
                           [clojure.string :as string]))))
             "\n\n\n\n\n"
             (deftests-str))
        :append false))



(deftest+ ^:pre-gen all-colors
  (into [] (sample/all-the-colors*)))

(deftest+ ^:pre-gen color-contrast
  (into [] (flatten (sample/bling-color-contrast))))

(deftest+ underline-styles
  (bling [:underline "underline"]
         "\n"
         [:double-underline "double-underline"]
         "\n"
         [:wavy-underline "wavy-underline"]
         "\n"
         [:dotted-underline "dotted-underline"]
         "\n"
         [:dashed-underline "dashed-underline"]))

(deftest+ callout-info (callout+ {:data? true :print-example-call? false :type :info}))
(deftest+ callout-info-label (callout+ {:data? true :print-example-call? false :type  :info :label "My custom label"}))
(deftest+ callout-warning (callout+ {:data? true :print-example-call? false :type :warning}))
(deftest+ callout-error (callout+ {:data? true :print-example-call? false :type :error}))
(deftest+ callout-positive-label (callout+ {:data? true :print-example-call? false :colorway :positive :label "SUCCESS!"}))

(deftest+ sideline-bold-callout-info (callout+ {:theme :sideline-bold :data? true :print-example-call? false :type :info}))
(deftest+ sideline-bold-callout-info-label (callout+ {:theme :sideline-bold :data? true :print-example-call? false :type  :info :label "My custom label"}))
(deftest+ sideline-bold-callout-warning (callout+ {:theme :sideline-bold :data? true :print-example-call? false :type :warning}))
(deftest+ sideline-bold-callout-error (callout+ {:theme :sideline-bold :data? true :print-example-call? false :type :error}))
(deftest+ sideline-bold-callout-positive-label (callout+ {:theme :sideline-bold :data? true :print-example-call? false :colorway :positive :label "SUCCESS!"}))

(deftest+ marquee-sideline-bold-callout-info (callout+ {:label-theme :marquee :theme :sideline-bold :data? true :print-example-call? false :type :info}))
(deftest+ marquee-sideline-bold-callout-info-label (callout+ {:label-theme :marquee :theme :sideline-bold :data? true :print-example-call? false :type  :info :label "My custom label"}))
(deftest+ marquee-sideline-bold-callout-warning (callout+ {:label-theme :marquee :theme :sideline-bold :data? true :print-example-call? false :type :warning}))
(deftest+ marquee-sideline-bold-callout-error (callout+ {:label-theme :marquee :theme :sideline-bold :data? true :print-example-call? false :type :error}))
(deftest+ marquee-sideline-bold-callout-positive-label (callout+ {:label-theme :marquee :theme :sideline-bold :data? true :print-example-call? false :colorway :positive :label "SUCCESS!"}))

(deftest+ gutter-callout-info (callout+ {:theme :gutter :data? true :print-example-call? false :type :info}))
(deftest+ gutter-callout-info-label (callout+ {:theme :gutter :data? true :print-example-call? false :type  :info :label "My custom label"}))
(deftest+ gutter-callout-warning (callout+ {:theme :gutter :data? true :print-example-call? false :type :warning}))
(deftest+ gutter-callout-error (callout+ {:theme :gutter :data? true :print-example-call? false :type :error}))
(deftest+ gutter-callout-positive-label (callout+ {:theme :gutter :data? true :print-example-call? false :colorway :positive :label "SUCCESS!"}))


(deftests-str)

(def write-tests? false)

;; Call this from repl or uncomment here to regenerate test suite
(when write-tests? 
  (println "--- Writing new fireworks.test_suite namespace --------------------")
  (write-tests-ns!))
