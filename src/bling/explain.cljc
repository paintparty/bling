(ns bling.explain
  (:require [fireworks.core :refer [? !? ?> !?>]]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [bling.core :refer [bling]]
            [bling.hifi :refer [hifi]]
            [bling.util :as util :refer [maybe]]
            [bling.macros :refer [keyed]]
            [malli.core :as m]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Malli explain 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def core-preds-by-keyword 
  {:vector  {:fn  vector?
             :sym 'vector?
             :tag "vector"}
   :list    {:fn  list?
             :sym 'list?
             :tag "list"}
   :seq     {:fn  seq?
             :sym 'seq?
             :tag "seq"}
   :string  {:fn  string?
             :sym 'string?
             :tag "string"}
   :map     {:fn  map?
             :sym 'map?
             :tag "map"}
   :set     {:fn  set?
             :sym 'set?
             :tag "set"}
   :keyword {:fn  keyword?
             :sym 'keyword?
             :tag "keyword"}
   :number  {:fn  number?
             :sym 'number?
             :tag "number"}
   :boolean {:fn  boolean?
             :sym 'boolean?
             :tag "boolean"}
   :true    {:fn  true?
             :sym 'true?
             :tag "true"}
   :false   {:fn  false?
             :sym 'false?
             :tag "false"}
   :nil     {:fn  nil?
             :sym 'nil?
             :tag "nil"}
   :pos-int {:fn  pos-int?
             :sym 'pos-int?
             :tag "positive integer"}
   :pos     {:fn  pos?
             :sym 'pos?
             :tag "positive number"}
   :float   {:fn  float?
             :sym 'float?
             :tag "floating point number"}
   :double  {:fn  double?
             :sym 'double?
             :tag "double"}
   :int     {:fn  int?
             :sym 'int?
             :tag "integer"}
   :neg-int {:fn  neg-int?
             :sym 'neg-int?
             :tag "negative integer"}})


(defn- clean-schema [schema]
  (walk/postwalk
   (fn [x]
     (if (map? x) (dissoc x :error/message) x))
   (m/form schema)))

(defn- clean-schema2 [schema]
  (walk/prewalk
   (fn [x]
     (if (and (vector? x)
              (nth x 2 nil)
              (map? (second x)))
       (into [(nth x 0 nil)] (subvec x 2))
       x))
   (m/form schema)))

(defn- indented-string [n s]
  (when s
    (string/join "\n"
                 (map #(str (string/join (repeat (or n 0) " "))  %) 
                      (string/split s
                                    #"\n")))))

(defn- target-key? [problem v]
  (let [in        (:in problem)
        m         (get-in v (drop-last in))
        [mek mev] (when (map? m) (find m (last in)))]
    (boolean (and (not= mev (:value problem))
                  (= mek (:value problem))))))


(defn- problem-path [missing-key? problem v]
  (cond missing-key?
        (->> problem :in drop-last (into []))
        (target-key? problem v)
        (conj (:in problem) :fireworks.highlight/map-key)
        :else
        (:in problem)))


(defn- section
  [label 
   v 
   {:keys [compact?
           ultra-compact?
           label-style
           margin-top
           section-break?
           omit-section-labels]
    :or   {section-break? true}}]
  (let  [section-break        (cond ultra-compact? "\n"
                                    compact?       "\n\n" 
                                    :else          "\n\n\n")
         section-header-break (if (or ultra-compact? compact?) "\n" "\n\n")
         label                (when label 
                                (if (and omit-section-labels 
                                         (contains? omit-section-labels label))
                                  nil
                                  label))]
    (into []
          (remove nil?
                  [(when section-break? section-break)
                   (when label (bling [label-style label]))
                   (when label section-header-break)
                   v]))))


(defn- explain-data* [malli-ex-data]
  (let [ret2 (walk/postwalk
              (fn [x]
                (if (and (map? x)
                         (contains? x :schema))
                  (assoc x :schema (m/form (:schema x)))
                  x))
              malli-ex-data)]
    ret2))

(defn- grouped-by-disjunctor [v malli-schema]
  (group-by (fn [%]
              (some->> %
                       :path
                       seq
                       (into [])
                       pop
                       (map inc)
                       (get-in (m/form malli-schema))
                       first))
            v))

(defn- schema-error-message [schema]
  (some-> schema
          (maybe vector?)
          second
          (maybe map?)
          :error/message))

(defn- enum-schema? [schema]
  (and (vector? schema)
       (< 1 (count schema))
       (= :enum (first schema))))

(defn- fn-schema? [schema]
  (let [n (count schema)]
    (and (vector? schema)
         (>= 2 n)
         (let [[k a b] schema]
           (boolean (and (= k (first schema))
                         (if (= 2 n) (fn? a) (and (map? a) (fn? b)))))))))

(defn grouped* [v malli-schema]
  (reduce
   (fn [acc [disjunctor problems]]
     (if (contains? #{:or :and} disjunctor)
       (let [schemas             
             (mapv #(-> % :schema m/form) problems)

             kw-or-sym?          
             #(or (keyword? %) (symbol? %))

             all-disjunctions-are-printable?
             (every? #(or (kw-or-sym? %)
                          #_(boolean (schema-error-message %))
                          (fn-schema? %)
                          (vector? %)
                          (enum-schema? %))
                     schemas)]
         (conj acc
               (-> problems first
                   (assoc :schemas             
                          schemas
                          :all-disjunctions-are-printable?
                          all-disjunctions-are-printable?
                          :disjunctor          
                          disjunctor
                          :problems            
                          problems)
                   (dissoc :path #_:schema))))
       acc))
   []
   (!? (grouped-by-disjunctor v malli-schema))))

(defn disjunctions*
  [{problems     :errors
    malli-schema :schema
    :as          malli-ex-data}]
  (when (!? :1 (seq problems))
    (reduce-kv
     (fn [acc k v]
       (if (< 1 (count v))
         (assoc acc k (grouped* v malli-schema))
         acc))
     {}
     (!? (group-by #(:in %) (:errors malli-ex-data))))))

(defn- collated-problems 
  [{problems :errors
    :as      malli-ex-data}]
  (let [disjunction-vals    
        (->> malli-ex-data
             disjunctions*
             vals
             (apply concat))

        problems-to-remove  
        (mapcat :problems disjunction-vals)

        disjuncted-problems 
        (mapv #(dissoc % :problems) disjunction-vals)

        filtered-problems   
        (filter #(not (contains? (into #{} problems-to-remove) %)) problems)
        
        collated-problems
        (concat filtered-problems
                disjuncted-problems)]
    (!? collated-problems)))

(defn- enum-schema->set [schema]
  (when (enum-schema? schema)
    schema
    ;; this changes enum schema into set
    #_(->> schema rest (into #{}))))

(defn- fn-schema-fn [schema]
  (some-> schema
          (maybe vector?)
          (maybe #(= 2 (count %)))
          (maybe #(= :fn (first %)))
          (maybe #(fn? (second %)))
          second))

(defn get-satisfaction 
  [{:keys [schema schema-cleaned]}]
  (or (some-> schema enum-schema->set)
      (some-> (get core-preds-by-keyword (or schema-cleaned schema)) :sym)
      (some-> schema schema-error-message symbol)
      (fn-schema-fn schema)
      schema-cleaned
      schema))


(defn disjuncted-satisfactions
  [{:keys [schemas] :as problem}
   {:keys [indentation-str] :as m}]
  (let [lb    (if (or (:ultra-compact? m) (:compact? m)) "\n" "\n\n")
        tilde (when (:surround-disjunctor-with-tilde? m) "~")]
    #?(:cljs (string/join 
              (str lb
                   indentation-str
                   indentation-str
                   tilde (some-> problem :disjunctor name) tilde
                   lb)
              (mapv #(hifi (get-satisfaction {:schema %})
                           {:margin-inline-start 2})
                    schemas))
       :clj (string/join 
             (bling lb
                    indentation-str
                    tilde [:italic (some-> problem :disjunctor name)] tilde
                    lb)
             (mapv #(bling 
                     ;; Remove this enum-schema? check when :bold is supported for hifi printing:
                     ;; https://github.com/paintparty/fireworks/issues/70
                     (let [weight (if (enum-schema? %) :normal :bold)] 
                       [weight (let [indented-string
                                     (partial indented-string (:indentation m))]
                                 (-> {:schema %}
                                     get-satisfaction
                                     hifi
                                     indented-string))]))
                   schemas)))))


(defn- file-info* 
  [{:keys [:file :line :column :function-name]}]
  (when (and file line column)
    (symbol (str file
                 (when function-name (str "/" function-name ))
                 ":"
                 line
                 ":"
                 column))))

#_(defn- highlighted-problem-section 
  [{:keys [missing-key?
           problem
           v
           indentation
           select-keys-in-problem-path?]}]
  (let [pth          (problem-path missing-key? problem v)
        narrowed-map (when (and select-keys-in-problem-path?
                                (seq pth)
                                (not-any? coll? pth)
                                (map? v))
                       (let [trimmed (select-keys v pth)]
                         (when (seq trimmed) trimmed)))
        v            (or narrowed-map v)]
    (hifi v
          {:find                (into []
                                      (remove nil? 
                                              [{:path  pth
                                                :class (if (coll? (get-in v pth))
                                                         :highlight-error
                                                         :highlight-error-underlined)}
                                               (when narrowed-map
                                                 {:pred  #(= % (first pth))
                                                  :class :info-error})]))
           :margin-inline-start indentation})) )

(defn explain-malli
  "Prints a malli validation error callout block via bling.core/callout.
   Within the block, the value is pretty-printed, potentially with syntax
   coloring. The problem value is highlighted with the `:highlight-error`
   class of the active fireworks theme, or the `:highlight-error-underlined`
   class, if the value is not a collection.
   
   If two arguments are provided, the second should be a map with the following
   optional keys:

   | Key                      | Pred                    | Description                                                  |
   | :---------------         | ----------------------- | ------------------------------------------------------------ |
   | `:function-name`         | `string?`               | The name of the function that can be used to construct the source location. Optional.
   | `:file`                  | `pos-int?` or `string?` | The file name that can be used to construct the source location. Optional.
   | `:line`                  | `pos-int?` or `string?` | The line number that can be used to construct the source location. Optional.
   | `:column`                | `pos-int?` or `string?` | The column number that can be used to construct the source location. Optional.
   | `:spacing`               | `#{:compact}`           | If the value of `:spacing` is set to `:compact`, the callout is compacted vertically. 
   | `:display-schema?`       | `boolean?`              | Displays the schema passed to the underlying call to `malli.core/explain`.
   | `:display-explain-data?` | `boolean?`              | Displays the output of `malli.core/explain` within the callout block.
   | `:callout-opts`          | `map?`                  | A map of options for the underlying call to bling.core/callout. |"
  ;; TODO update markdown table ^

  ([schema v]
   (explain-malli schema v nil))
  ([schema 
    v
    {:keys [spacing
            success-message
            highlighted-problem-section-label
            section-body-indentation
            omit-section-labels
            omit-sections
            select-keys-in-problem-path?
            preamble-section-label
            preamble-section-body
            surround-disjunctor-with-tilde?
            highlight-missing-keys?
            display-schema?
            display-explain-data?
            file-info-str
            callout-opts
            error-handler]
     :or   {success-message nil}
     :as   explain-malli-opts}] 
   (let [indentation 
         (or section-body-indentation 2)

         indentation-str 
         (string/join (repeat (or indentation 0) " "))
         
         file-info       (or (when (string? file-info-str) file-info-str)
                             (file-info* explain-malli-opts))
         ]
     (try (let [{problems     :errors
                 malli-schema :schema
                 :as          malli-ex-data}
                (m/explain schema v)]
            (if (seq problems)
              (let [compact?
                    (= :compact spacing)

                    ultra-compact?
                    (= :ultra-compact spacing)

                    collated-problems
                    (collated-problems malli-ex-data)

                    problems
                    collated-problems #_problems]
                (doseq [problem problems]
                  (let [explain-data    (when (true? display-explain-data?)
                                          (explain-data* malli-ex-data) )

                        schema          (some-> problem :schema m/form)

                        error-message   (schema-error-message schema)

                        ;; schema-cleaned  (some-> problem :schema clean-schema)
                        schema-cleaned  (some-> problem :schema clean-schema2)

                        missing-key     (if (= :malli.core/missing-key (:type problem))
                                          (some-> problem :path last)
                                          :bling.explain/no-missing-key)
                        
                        missing-key?    (not= missing-key :bling.explain/no-missing-key)

                        error-message?  (boolean (and error-message (not missing-key?)))

                        must-satisfy?   (boolean (and (not error-message)
                                                      (not missing-key?)))


                        label-style     :italic

                        fv              #(indented-string indentation (hifi %))
                        
                        display-schema? (if (false? display-schema?)
                                          false
                                          (and (not (true? display-explain-data?))
                                               (not (false? display-schema?))))
                        

                        omit-sections   (some->> omit-sections
                                                 seq
                                                 (into #{}))

                        omit-section-labels (some->> omit-section-labels
                                                     seq
                                                     (into #{}))

                        section-opts    {:compact?            compact?
                                         :ultra-compact?      ultra-compact?
                                         :label-style         label-style
                                         :omit-section-labels omit-section-labels}]
                    

                    (apply 
                     bling.core/callout
                     (concat
                      [(merge {:colorway       :error
                               :theme          #?(:cljs :minimal :clj :sideline)
                               :label-theme    #?(:clj :marquee :cljs :pipe)
                               :label          "Malli Schema Error"
                               :side-label     (when file-info
                                                 #?(:cljs
                                                    file-info
                                                    :clj
                                                    (bling [:italic file-info])))
                               :margin-top     #?(:cljs 0 :clj 1)
                               :padding-top    (cond ultra-compact? 0 compact? 1 :else 1)
                               :padding-bottom (if (or ultra-compact? compact?) 0 1)}
                              callout-opts)]
                      
                      (when preamble-section-body
                        (section preamble-section-label 
                                 #?(:cljs
                                    (bling indentation-str preamble-section-body)
                                    :clj
                                    (bling (fv preamble-section-body)))
                                 (assoc section-opts :section-break? false)))


                      (section highlighted-problem-section-label
                               ;; TODO - use fn highlighted-problem-section
                               (let [pth          (problem-path missing-key? problem v)
                                     narrowed-map (when (and select-keys-in-problem-path?
                                                             (seq pth)
                                                             (not-any? coll? pth)
                                                             (map? v))
                                                    (let [trimmed (select-keys v pth)]
                                                      (when (seq trimmed) trimmed)))
                                     v            (or narrowed-map v)]
                                 (hifi v
                                       {:find                (into []
                                                                   (remove nil? 
                                                                           [{:path  pth
                                                                             :class (if (coll? (get-in v pth))
                                                                                      :highlight-error
                                                                                      :highlight-error-underlined)}
                                                                            ;; highlighting ancestor key
                                                                            #_(when narrowed-map
                                                                                {:pred  #(= % (first pth))
                                                                                 :class :info-error})]))
                                        :margin-inline-start indentation}))
                               (assoc section-opts
                                      :section-break?
                                      (if preamble-section-body true false)))


                      (when missing-key? 
                        (section "Missing key:"
                                 (hifi missing-key
                                       (merge {:margin-inline-start indentation}
                                              (when highlight-missing-keys?
                                                {:find {:pred  #(= % missing-key)
                                                        :class :info-error}})))
                                 section-opts))


                      (when-not (contains? omit-sections :problem-value)
                        (when-not missing-key? 
                          (let [v                  (:value problem)
                                multiple-problems? (coll? v)]
                            (section (str "Problem value" (when multiple-problems? "s") ":")
                                     #?(:cljs
                                        (bling indentation-str [:bold v])
                                        :clj
                                        (if multiple-problems?
                                          (fv v)
                                          (bling [:bold (fv v)])))
                                     section-opts))))


                      (when error-message? 
                        (section "Message:" 
                                 (indented-string section-body-indentation
                                                  error-message)
                                 section-opts))
                      

                      (when must-satisfy? 
                        (if (contains? problem :disjunctor)
                          (section "Must satisfy:"
                                   (if (:all-disjunctions-are-printable? problem)
                                     (disjuncted-satisfactions 
                                      problem 
                                      (keyed [compact?
                                              ultra-compact?
                                              indentation
                                              indentation-str
                                              surround-disjunctor-with-tilde?]))
                                     #?(:cljs (hifi (:schemas problem)
                                                    {:margin-inline-start 2})
                                        :clj (bling [:bold (fv (:schemas problem))])))
                                   section-opts)
                          (let [v (get-satisfaction (keyed [schema schema-cleaned]))]
                            (section "Must satisfy:"
                                     #?(:cljs (hifi v (merge {:margin-inline-start indentation}
                                                             (when (and (vector? v)
                                                                        (= :enum (first v)))
                                                               {:find {:pred  #(= :enum %)
                                                                       :class :highlight-info}})))
                                        :clj (bling [:bold (fv v)]))
                                     section-opts))))
                      
                      (when-let [examples (when must-satisfy?
                                            (some-> schema
                                                    (maybe vector?)
                                                    (nth 1 nil)
                                                    :examples))]
                        (section (str "Example" (when (< 1 (count examples)) "s") ":") 
                                 #?(:cljs (hifi examples 
                                                {:margin-inline-start indentation
                                                 :let-bindings?       true})
                                    :clj (bling [:bold (fv examples)]))
                                 section-opts))


            ;; The schema related to the problem value.
            ;; Defaults to true, displaying schema
                      (when display-schema? 
                        (section "Schema:"
                                 #?(:cljs
                                    (hifi (m/form malli-schema) {:margin-inline-start indentation})
                                    :clj
                                    (fv (m/form malli-schema)))
                                 section-opts))


            ;; The result of calling malli.core/explain on the value.
            ;; Defaults to false, not displaying schema
                      (when (true? display-explain-data?)
                        (section "Result of malli.core/explain:"
                                 #?(:cljs
                                    (hifi explain-data {:margin-inline-start indentation})
                                    :clj
                                    (fv explain-data))
                                 section-opts)))))))

              (when-not (nil? success-message)
                (case success-message 
                  ::explain-malli-success-verbose
                  (apply
                   bling.core/callout 
                   (concat [(merge {:type :positive}
                                   callout-opts
                                   {:label "Malli Schema Validation Success"})]
                           ["\n\n"
                            (bling [:italic "Source:"])
                            "\n\n"
                            (bling indentation-str file-info-str)
                            "\n\n\n"
                            (bling [:italic "Value:"])
                            "\n\n"
                            (hifi v {:margin-inline-start 2})
                            "\n\n\n"
                            #_(bling [:italic "Schema:"])
                            #_"\n\n"
                            #_(hifi schema {:margin-inline-start 2})
                            ]))
                  ::explain-malli-success-simple
                  (println (str "Malli schema validation success"
                                (when file-info-str
                                  (str " @ " file-info-str))))
                  (println success-message)
                  ))))
             (catch #?(:cljs js/Object :clj Throwable) 
                    e
               #_(println e)
               (if (and error-handler (fn? error-handler))
                 (error-handler e)
                 (apply
                  bling.core/callout 
                  (concat [(merge {:type :error}
                                  callout-opts
                                  {:label "Error (Caught)"})]
                          ["\n"
                           (bling [:italic "Message from Clojure:"])
                           "\n\n"
                           (str "  "
                                #?(:cljs
                                   (.-message e)
                                   :clj
                                   (.getMessage e)))
                           "\n\n"
                           "\n\n"
                           (bling [:italic "Schema:"])
                           "\n\n"
                           (hifi schema {:margin-inline-start 2})
                           "\n\n"
                           "\n\n"
                           (bling [:italic "Stack trace:"])
                           "\n\n"
                           (str e)

                           ]))))))))



;; TODO - create logic for this to reduce number of problems?
;; If vector is all maps, eliminate the [:vector :string] branch 
;; If vector is all strings, eliminate the [:vector :string] branch 
#_(explain-malli [:or
                [:vector :map]
                [:vector :string]]
               ["hi" {:a 1}]
               {:success-message "yearr"})
