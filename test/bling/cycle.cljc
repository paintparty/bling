(ns bling.cycle
  (:require
   [clojure.string :as string]
   [fireworks.core :refer [? !? ?> !?>]]
   [bling.core :as bling :refer [bling print-bling callout point-of-interest]]
   [bling.util :as util :refer [when-> char-repeat]]
   [bling.hifi :refer [print-hifi hifi]]


   [malli.core :as m]))

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

(defn pco
  [base-m
   theme-m
   {:keys [animate?
           frame-rate
           fqfn-name
           fn-name
           print-fn-call?
           second-arg-f
           merged-override-map
           public-f
           printing-f
           margin-top
           ::errors]}]
  (when animate? (clear-screen))
  (some-> margin-top (char-repeat "\n") println)
  (try (let [m    (merge base-m theme-m merged-override-map)
             call (if second-arg-f
                    (public-f m (second-arg-f merged-override-map))
                    (public-f m))]
         (printing-f call)
         (when print-fn-call? (print-hifi (list fn-name m))))
       (catch #?(:cljs js/Object :clj Throwable)
              e
         (swap! errors conj {:f fqfn-name
                             :m (merge base-m theme-m merged-override-map)
                             :error e})))
  #?(:clj (when animate? (Thread/sleep (or frame-rate 100)))))


#_(defn poi
    [base-m
     theme-m
     {:keys [animate? frame-rate second-arg-f om]}]
    (when animate? (clear-screen))
    (println "\n\n\n\n\n")
    (let [m (merge base-m theme-m om)]
      (? m)
      (println
       (point-of-interest m)))
    #?(:clj (when animate? (Thread/sleep (or frame-rate 100)))))


#_(let [{:keys [option-f coll k]}
        {:option-f (fn [x _] x)
         :coll     [true false]
         :k        :truncate-form-to-single-line?}]
    (let [form   "(foo 1
     2
     \"afas\"
     \"adsfasdfasdfadsfs\")"
          base-m {:animate?                             false
                  :form                                 form
                  :frame-rate                           50
                  :file                                 nil
                  :line                                 nil
                  :column                               nil
                  :type                                 nil
                  :text-decoration-color                :red
                  :text-decoration-style                :wavy
                  :text-decoration-relative-line-number 3
                  :truncate-form-to-single-line?        false}]
      (doseq [i coll]
        (poi base-m {} {:om (merge {k (option-f i coll)})}))))


(defn- option-sequence
  [{:keys [theme
           lap?
           coll
           fqfn-name
           fn-name
           print-desc?
           printing-f
           option-f
           k
           om
           public-f
           second-arg-f
           base-m
           frame-rate
           ::errors]}
   {:keys [option-filter
           margin-top
           animate?]}]
  (when (cond (set? option-filter)
              (or (not (seq option-filter))
                  (contains? option-filter k))

              (fn? option-filter)
              (option-filter k)

              :else
              true)
    (let [coll   (if lap? (concat coll (reverse coll)) coll)]
      (doseq [x coll]
        (pco base-m
             theme
             (let [merged-override-map (merge {k (option-f x coll)} om)]
               {:second-arg-f        second-arg-f
                :fqfn-name           fqfn-name
                :fn-name             fn-name
                :print-desc?         print-desc?
                :public-f            public-f
                :option-f            option-f
                :printing-f          printing-f            
                :merged-override-map merged-override-map
                :animate?            animate?
                :margin-top          margin-top
                :frame-rate          frame-rate
                ::errors             errors}))))))


(defn callout-option-sequences
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
                :om   {:min-width 40} ; <-companion props
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
                :option-f   (fn [x _] x)
                :coll       styles*
                :lap?       false
                :frame-rate 500}

               {:k          :border-weight
                :option-f   (fn [x _] x)
                :coll       weights*
                :lap?       false
                :frame-rate 500}


               {:k          :border-shape
                :option-f   (fn [x _] x)
                :coll       [:round]
                :lap?       false
                :frame-rate 600}

               {:k          :border-shape
                :option-f   (fn [x _] x)
                :coll       (concat (keys bling.core/bling-colors) [:subtle])
                :lap?       false
                :frame-rate 100}]]
        (option-sequence
         (merge {:frame-rate   25
                 :lap?         true
                 :coll         [0 1 2 3 4 5]
                 :option-f     (fn [i coll] (nth coll i))
                 :second-arg-f (fn [merged-override-map]
                                 (bling.hifi/hifi merged-override-map))
                 :public-f     callout
                 :base-m       {:border-style :solid
                                :label        "Hello"
                                :side-label   "side_label.clj:11:44"
                                :label-theme  label-theme}
                 :theme        theme}
                m)
         opts)))))


(defn- options-with-variants
  {:desc "Expects a malli `:map` schema], (not `:map-of`)."
   :examples '[[(options-with-variants 
                 [:map 
                  [:foo {:gen/min 0
                         :gen/max 10}
                   :int]
                  [:bar {:gen/elements ["Jingo" "Bango" "Gongo"]  }
                   :string]
                  [:foo {:desc "Hi"}
                   [:enum :red :yellow :blue]]])
                [{:k    :foo
                  :coll [0 1 2 3 4 5 6 7 8 9 10]}
                 {:k    :bar
                  :coll [0 1 2 3 4 5 6 7 8 9 10]}
                 {:k    :foo
                  :coll [:red :yellow :blue]}
                 ]]]}
  [options-map-schema variant-coll-f]
  (->> options-map-schema
       (reduce-kv (fn [vc i m]
                    (if (pos? i)
                      (let [[k a b]
                            m

                            {gen-min      :gen/min
                             gen-max      :gen/max
                             gen-elements :gen/elements
                             frame-rate   :gen/frame-rate
                             :as          opts}
                            (when-> a map?)

                            schema
                            (if opts b a)

                            coll
                            (or (when variant-coll-f
                                  (variant-coll-f schema))
                                (when (= schema :boolean) [true false])

                                (some-> schema
                                        (when-> coll?)
                                        (when-> #(= (first %) :enum))
                                        (subvec 1))

                                (and (every? int? [gen-min gen-max])
                                     (< gen-min gen-max)
                                     (some-> (range gen-min (inc gen-max))
                                             vec))

                                (seq gen-elements)
                                [])

                            _ (?)
                            coll
                            (with-meta coll {:k k :size (count coll)})]
                        (conj vc
                              {:k          k
                               :desc       (:desc opts)
                               :frame-rate frame-rate
                               :coll       coll}))
                      vc))
                  [])))


(defn variant-coll [m k]
  (!? :trace (->> m
              k
              :coll 
              (map-indexed (fn [i v]
                             (with-meta {k v}
                               {::i i ::k k}))))))


(defn options-sequences*
  [{options  :options
    examples :examples
    fn-name  :name
    :as      fvar-meta}
   {:keys [frame-rate
           variant-coll-f
           run-examples?
           examples-filter
           print-desc?
           print-fn-call?
           print-header?
           primary 
           secondary
           primary-filter
           printing-f
           secondary-filter]
    :or   {frame-rate     50
           printing-f      println
           run-examples?  true
           print-header?  true
           print-fn-call? true
           print-desc?    true}
    :as   opts}]
  (let [fqfn-name       (symbol (str (:ns fvar-meta) "/" fn-name))
        public-f        @(resolve fqfn-name)
        margin-left     (when print-header? 2)
        margin-left-str (some-> margin-left (util/char-repeat " "))
        errors          (atom [])]
    (when (and run-examples? examples)
      (when print-header?
        (callout
         {:label           (hifi public-f {:truncate? false})
          :label-theme     :marquee
          :border-notches? true
          :min-width       50
          :margin-bottom   2
          :theme           :sandwich}
         nil
         ))

      ;; TODO validate examples vector
      (doseq [[i {:keys [desc forms] :as example}] (map-indexed vector examples)]
        (when (and (not (-> example meta :no-print))
                   (or (not examples-filter)
                       (when (fn? examples-filter)
                         (examples-filter examples i example))))
          (when (and print-desc? desc)
            (print-bling 
             #_margin-left-str
             [:subtle.italic desc] "\n"))
          
          (doseq [[form result] forms]
            (when-let [args (and (list? form)
                                 (-> form first (= fn-name))
                                 (rest form))]
              (when print-fn-call?
                (print-bling (print-hifi form)
                             [:subtle.italic '=>]))
              (printing-f (apply public-f args)))))))
    (if primary
      (let [w-variants         (options-with-variants options variant-coll-f)
            by-key             (reduce (fn [m v]
                                         (assoc m (:k v) v)) {} 
                                       w-variants)
            primary-variants   (variant-coll by-key primary)
            secondary-variants (some->> (when-> secondary keyword?)
                                        (variant-coll
                                         by-key))]
        (!? w-variants)
        (doseq [primary-variant-map (!? :+ primary-variants)]
            (when (if-not (fn? primary-filter)
                    true
                    (primary-filter primary-variants 
                                    (-> primary-variant-map meta ::i)
                                    (-> primary-variant-map meta ::k)
                                    primary-variant-map))
              (doseq [secondary-variant-map secondary-variants]
                (when (if-not (fn? secondary-filter)
                        true
                        (secondary-filter secondary-variants 
                                          (-> secondary-variant-map meta ::i)
                                          (-> secondary-variant-map meta ::k)
                                          secondary-variant-map)) 
                  (doseq [m w-variants]
                    (option-sequence
                     (merge {::errors     errors
                             :frame-rate  50
                             :fn-name     fn-name
                             :fqfn-name   fqfn-name
                             :print-desc? print-desc?
                             :printing-f  printing-f
                             :lap?        true
                             :option-f    (fn [x _] x)
                             :public-f    public-f
                             :base-m      (merge primary-variant-map
                                                 secondary-variant-map
                                                 {:file   "guh.cljs"
                                                  :line   11
                                                  :column 2})}
                            m)
                     opts)))))))
      (println "No :primary variant supplied."))
      (println "Error count:" (count @errors))))

#?(:cljs
   ()
   :clj
   (defmacro variants 
     {:desc    "Intended for dynamic visual testing of functions that take an options map.

                The first arg should be a symbol bound to function with a metadata map that contains
                an `:options` entry in Malli `:map` schema syntax.

                Based on this schema, a (potentially) multi-dimensional sequence of option map
                variants are generated and each is passed to the function in a separate call.
                These can be printed to std out, sequentially, or in animation mode with a variable
                frame rate.

                The second argument should be a map of options."
      :options [:map
                [:animate? {:optional true :default true :desc "Display each variant then clear std out before the next is printed."} :boolean]
                [:run-examples? {:optional true :default true :desc "Runs all the examples from the `:examples` entry in the functions metadata map, except for ones that are tagged with `^no-print"} :boolean]
                [:primary {:optional true :default nil :desc "The option that will be used as the primary variant"} :keyword]
                [:secondary {:optional true :default nil :desc "The option that will be used as the secondary variant"} :keyword]
                [:printing-f {:optional true :default println :desc "Function to wrap the result of the example variant call in. Typically a printing function such as `println`"} fn?]
                [:margin-top {:optional true :default 2 :desc "The number of newlines above variant output"} :int]
                [:option-filter {:optional true :default nil :desc "A set of keywords that represents options to include. Can also be a predicate function that accepts a keyword (the option key)"} [:or [:set :keyword] fn?]]

                ;; Change to print-options-map?
                [:print-fn-call? {:optional true :default false :desc "Prints the merged options map used for each iteration"} :boolean]
                [:print-desc? {:optional true :default true :desc "Prints the value of `:desc` from the example or example option value."} :boolean]
                [:examples-filter {:optional true :default nil :desc "Use to filter the examples. Takes 3 args `[coll, i, m]`"} fn?]
                [:primary-filter {:optional true :default nil :desc "Use to filter the primary variants. Takes 4 args `[coll, i, k m]`"} fn?]
                [:secondary-filter {:optional true :default nil :desc "Use to filter the secondary variants. Takes 4 args `[coll, i, k m]`"} fn?]

                ;; Todo - make this a vector situation do you can compose the args
                [:second-arg-f {:default true :desc "Function that expects a map (the merged options map) and produced a value that will be used as the second arg."} fn?]]}
     ([f]
      `(options-sequences*
        (-> ~f var meta)
        {}))
     ([f m]
      `(options-sequences*
        (-> ~f var meta)
        ~m))))


(defn distinct-enums 
  "Removes redundant enums from schema coll used with bling.variants/variants,
   when enums feature keyword and string versions of both"
  [schema]
  (when-let [enums 
             (and (vector? schema)
                  (= (first schema) :enum)
                  (-> schema count odd?)
                  (rest schema))]
    (let [strings  (some->> enums (filter string?))
          keywords (some->> enums (filter keyword?))]
      (and (= (count strings) (count keywords))
           (= (->> keywords (mapv name) (into #{}))
              (into #{} strings))
           (vec keywords)))))


;; TODO - create a hr construct, to use when not animating 
;; Figure out how to trigger an error and do reporting
;; Create a no-print mode
;; Use to generate a test suite from examples and variants
;; Use variants function for callout
;; Update callout metadata for generation
;; 

#_(variants
 point-of-interest
 {:printing-f        println

  ;; :second-arg-f     (fn [merged-opts] nil) 

  :variant-coll-f   distinct-enums
  :margin-top       2
  :print-options?   true
  :animate?         true
  :examples-filter  (fn [coll i m] (not (-> m :desc (= "FOO"))))
  :primary-filter   (fn [coll i k m]
                      (!? [coll i k m])
                      ;;(true? (k m)) ;; pass filter if value of k entry is true
                      true)

  ;; :secondary-filter (fn [coll i k m]
  ;;                     (-> coll count dec (= i)))

  :option-filter    #{:text-decoration-style
                      #_:hifi-options}

  ;; :option-filter    (fn [option-key]
  ;;                     (= option-key :column))

  :primary          :truncate-form-to-single-line?
  :secondary        :form})


#_(println (point-of-interest {:form '(+ 2
                                       3
                                       "adsfasdfasfadsf" 
                                       "asfdsadfasdfasfd")}))

#_(println (point-of-interest {:form '(+ 2
                                       3
                                       "adsfasdfasfadsf" 
                                       "asfdsadfasdfasfd")
                             :truncate-form-to-single-line? false}))
#_
(println (point-of-interest {:line 11
                             :row  2
                             :column 33
                             :form '(+ 2 3)}))

#_
(println 
 (point-of-interest 
  {:line         11
   :row          2
   :column       33
   :text-decoration-color :magenta
   :truncate-form-to-single-line? true
   :form         
"(+ 2
   3
   \"adsfasdfasfadsf\" 
   \"asfdsadfasdfasfd\"
   3 4 4 5 6 6 6 9 9 9 )"
  :hifi-options {:print-length 10
                 :find       {:path  [4]
                              :class :highlight-error}}}))

#_
(println 
 (point-of-interest 
  {:line         11
   :row          2
   :column       33
   :form         '(+ 2
                     3
                     "adsfasdfasfadsf" 
                     "asfdsadfasdfasfd"
                     3 4 4 5 6 6 6 9 9 9 )
   :hifi-options {:print-length 10
                  :find {:path  [4]
                         :class :highlight-error}}}))


