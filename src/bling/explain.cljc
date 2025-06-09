(ns bling.explain
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [bling.core :as bling]
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


(defn- section [compact? label-style condition label v]
  (let  [section-break        (if compact? "\n\n" "\n\n\n")
         section-header-break (if compact? "\n" "\n\n")]
    [(when condition section-break)
     (when condition (bling/bling [label-style label]))
     (when condition section-header-break)
     (when condition v)]))


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
    {:keys [:file
            :line
            :column
            :function-name
            :spacing
            :display-schema?
            :display-explain-data?
            :callout-opts]}] 
   (let [{problems     :errors
          malli-schema :schema
          :as          ex-data}
         (m/explain schema v)

         malli-schema-cleaned
         (clean-schema malli-schema)

         compact?
         (= :compact spacing)]

     (if (seq problems)
       (doseq [problem problems]
         (let [explain-data
               (when (true? display-explain-data?)
                 (edn/read-string (with-out-str (prn ex-data))))

               schema
               (m/form (:schema problem))

               error-message 
               (some-> schema
                       (maybe vector?)
                       second
                       (maybe map?)
                       :error/message)

               schema-cleaned
               (clean-schema (:schema problem))

               missing-key
               (if (= :malli.core/missing-key (:type problem))
                 (-> problem :path last)
                 :bling.explain/no-missing-key)
               
               missing-key?
               (not= missing-key :bling.explain/no-missing-key)

               error-message?
               (boolean (and error-message (not missing-key?)))

               must-satisfy?
               (boolean (and (not error-message) (not missing-key?)))


               file-info
               (when (and file line column)
                 (symbol (str file
                              (when function-name (str "/" function-name ))
                              ":"
                              line
                              ":"
                              column)))
               
               label-style :italic
               
               fv
               #(indented-string (hifi %))
               
               display-schema?
               (and (not (true? display-explain-data?))
                    (not (false? display-schema?)))
               
               section
               (partial section compact? label-style)]
           

           (apply 
            bling.core/callout
            (concat
             [(merge {:colorway       :error
                      :theme          #?(:cljs :minimal :clj :sideline)
                      :label-theme    #?(:clj :marquee :cljs :pipe)
                      :label          "Malli Schema Error"
                      :side-label     (when file-info
                                        (bling/bling [:italic file-info]))
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
             
             (section missing-key? 
                      "Missing key:"
                      #?(:cljs
                         (bling/bling "  " [:bold missing-key])
                         :clj
                         (bling/bling [:bold (fv missing-key)])))


             (section (not missing-key?) 
                      "Problem values:"
                      #?(:cljs
                         (bling/bling "  " [:bold (:value problem)])
                         :clj
                         (bling/bling [:bold (fv (:value problem))])))


             (section error-message? 
                      "Message:" 
                      (indented-string error-message))
             

             (let [v (or (some-> (get core-preds-by-keyword schema-cleaned) :sym)
                         schema-cleaned)]
               (section must-satisfy? 
                        "Must satisfy:"
                        #?(:cljs (hifi v {:margin-inline-start 2})
                           :clj (bling/bling 
                            [:bold (fv v)]))))
             

            ;; The schema related to the problem value.
            ;; Defaults to true, displaying schema
             (section display-schema? 
                      "Schema:"
                      #?(:cljs
                         (hifi malli-schema-cleaned {:margin-inline-start 2})
                         :clj
                         (fv malli-schema-cleaned)))


            ;; The result of calling malli.core/explain on the value.
            ;; Defaults to false, not displaying schema
             (section (true? display-explain-data?) 
                      "Result of malli.core/explain:"
                      #?(:cljs
                         (hifi explain-data {:margin-inline-start 2})
                         :clj
                         (fv explain-data)))))))

       (println "Success!")))))
