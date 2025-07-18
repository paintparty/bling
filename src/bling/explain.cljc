(ns bling.explain
  (:require [fireworks.core :refer [? !? ?> !?>]]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [bling.core :refer [bling]]
            [bling.hifi :refer [hifi]]
            [bling.util :as util :refer [maybe]]
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


(defn clean-schema [schema]
  (walk/postwalk
   (fn [x]
     (if (map? x) (dissoc x :error/message) x))
   (m/form schema)))


(defn indented-string [s]
  (when s
    (string/join "\n"
                 (map #(str "  "  %) 
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
  [label v {:keys [compact? label-style]}]
  (let  [section-break        (if compact? "\n\n" "\n\n\n")
         section-header-break (if compact? "\n" "\n\n")]
    [section-break
     (bling [label-style label])
     section-header-break
     v]))


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
     (group-by #(:in %) (:errors malli-ex-data)))))

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
        (filter #(not (contains? (into #{} problems-to-remove) %)) problems)] 
    (concat filtered-problems
            disjuncted-problems)))


(defn- enum-schema->set [schema]
  (when (enum-schema? schema)
    (->> schema rest (into #{}))))


(defn get-satisfaction 
  [{:keys [schema schema-cleaned]}]
  (or (some-> schema enum-schema->set)
      (some-> (get core-preds-by-keyword (or schema-cleaned schema)) :sym)
      (some-> schema schema-error-message symbol)
      (some-> schema
              (maybe vector?)
              (maybe #(= 2 (count %)))
              (maybe #(= :fn (first %)))
              (maybe #(fn? (second %)))
              second)
      schema-cleaned
      schema))

(defn disjuncted-satisfactions
  [{:keys [schemas] :as problem}
   m]
  (let [lb    (if (:compact? m) "\n" "\n\n")
        tilde (when (:surround-disjunctor-with-tilde? m) "~")]
    #?(:cljs (string/join 
              (str lb
                   "  "
                   "  "
                   (some-> problem :disjunctor name)
                   lb)
              (mapv #(hifi (get-satisfaction {:schema %})
                           {:margin-inline-start 2})
                    schemas))
       :clj (string/join 
             (bling lb
                    "  "
                    tilde [:italic (some-> problem :disjunctor name)]
                    tilde
                    lb)
             (mapv #(bling 
                     [:bold (-> {:schema %}
                                get-satisfaction
                                hifi
                                indented-string)])
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

'[vector? {:of [:or string? greater-than-10?]}]

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
  ([schema v]
   (explain-malli schema v nil))
  ([schema 
    v
    {:keys [spacing
            surround-disjunctor-with-tilde?
            display-schema?
            display-explain-data?
            callout-opts]
     :as   explain-malli-opts}] 
   (? explain-malli-opts)
   (let [{problems     :errors
          malli-schema :schema
          :as          malli-ex-data}
         (m/explain schema v)]
     (if (seq problems)
       (let [malli-schema-cleaned
             (clean-schema malli-schema)
             
             compact?
             (= :compact spacing)

             collated-problems
             (collated-problems malli-ex-data)

             problems
             collated-problems #_problems]
         (doseq [problem problems]
           (let [explain-data    (when (true? display-explain-data?)
                                   (explain-data* malli-ex-data) )

                 schema          (some-> problem :schema m/form)

                 error-message   (schema-error-message schema)

                 schema-cleaned  (some-> problem :schema clean-schema)

                 missing-key     (if (= :malli.core/missing-key (:type problem))
                                   (some-> problem :path last)
                                   :bling.explain/no-missing-key)
                 
                 missing-key?    (not= missing-key :bling.explain/no-missing-key)

                 error-message?  (boolean (and error-message (not missing-key?)))

                 must-satisfy?   (boolean (and (not error-message)
                                               (not missing-key?)))


                 file-info       (file-info* explain-malli-opts )
                 
                 label-style     :italic
                 
                 fv              #(indented-string (hifi %))
                 
                 display-schema? (and (not (true? display-explain-data?))
                                      (not (false? display-schema?)))
                 
                 section-opts    {:compact?    compact?
                                  :label-style label-style}]
             

             (apply 
              bling.core/callout
              (concat
               [(merge {:colorway       :error
                        :theme          #?(:cljs :minimal :clj :sideline)
                        :label-theme    #?(:clj :marquee :cljs :pipe)
                        :label          "Malli Schema Error"
                        :side-label     (when file-info
                                          (bling [:italic file-info]))
                        :margin-top     1
                        :padding-top    (if compact? 1 2)
                        :padding-bottom (if compact? 0 1)}
                       callout-opts)
                
                (hifi v
                      (let [pth (problem-path missing-key? problem v)]
                        {:find (merge {:path  pth
                                       :class (if (coll? (get-in v pth))
                                                :highlight-error
                                                :highlight-error-underlined)})}))]
               

               (when missing-key? 
                 (section "Missing key:"
                          #?(:cljs
                             (bling "  " [:bold missing-key])
                             :clj
                             (bling [:bold (fv missing-key)]))
                          section-opts))


               (when-not missing-key? 
                 (section "Problem value:"
                          #?(:cljs
                             (bling "  " [:bold (:value problem)])
                             :clj
                             (bling [:bold (fv (:value problem))]))
                          section-opts))


               (when error-message? 
                 (section "Message:" 
                          (indented-string error-message)
                          section-opts))
               

               (when must-satisfy? 
                 (if (contains? (? problem) :disjunctor)
                   (section "Must satisfy:"
                            (if (:all-disjunctions-are-printable? problem)
                              (disjuncted-satisfactions 
                               problem 
                               {:compact?                        
                                compact?
                                :surround-disjunctor-with-tilde? 
                                surround-disjunctor-with-tilde?})
                              #?(:cljs (hifi (:schemas problem)
                                             {:margin-inline-start 2})
                                 :clj (bling [:bold (fv (:schemas problem))])))
                            section-opts)
                   (let [v
                         (get-satisfaction {:schema         schema
                                            :schema-cleaned schema-cleaned})]
                     (section "Must satisfy:"
                              #?(:cljs (hifi v {:margin-inline-start 2})
                                 :clj (bling [:bold (fv v)]))
                              section-opts))))


            ;; The schema related to the problem value.
            ;; Defaults to true, displaying schema
               (when display-schema? 
                 (section "Schema:"
                          #?(:cljs
                             (hifi (m/form malli-schema) {:margin-inline-start 2})
                             :clj
                             (fv (m/form malli-schema)))
                          section-opts))


            ;; The result of calling malli.core/explain on the value.
            ;; Defaults to false, not displaying schema
               (when (true? display-explain-data?)
                 (section "Result of malli.core/explain:"
                          #?(:cljs
                             (hifi explain-data {:margin-inline-start 2})
                             :clj
                             (fv explain-data))
                          section-opts)))))))

       (println "Success!")))))
