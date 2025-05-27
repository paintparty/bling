(ns bling.explain
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [bling.core :as bling]
            [bling.hifi :refer [hifi]]
            [bling.util :as util :refer [maybe]]
            [malli.core :as m]
            ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Malli explain 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn clean-schema [schema]
  (walk/postwalk
   (fn [x]
     (if (map? x) (dissoc x :error/message) x))
   (edn/read-string (with-out-str (prn schema)))))


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
  ([schema v {:keys [:file
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
         compact? (= :compact spacing)]
     (if (seq problems)
       (doseq [problem problems]
         (let [explain-data
               (when (true? display-explain-data?)
                 (edn/read-string (with-out-str (prn ex-data))))

               schema
               (edn/read-string (with-out-str (prn (:schema problem))))

               error-message 
               (some-> schema
                       (maybe vector?)
                       second
                       (maybe map?)
                       :error/message)

               schema-cleaned
               (clean-schema (:schema problem))

               malli-schema-cleaned
               (clean-schema malli-schema)

               section-break
               (if compact? "\n\n" "\n\n\n")
               
               section-header-break
               (if compact? "\n" "\n\n")

               missing-key
               (if (= :malli.core/missing-key (:type problem))
                 (-> problem :path last)
                 :specoli.core/no-missing-key)
               
               missing-key?
               (not= missing-key :specoli.core/no-missing-key)

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
               #(indented-string (hifi %))]
           

           (bling.core/callout
            (merge {:type           :error
                    :colorway       :blue
                    :label-theme    :marquee
                    :label          "Malli validation error"
                    :padding-top    (if compact? 1 2)
                    :padding-bottom (if compact? 0 1)}
                   callout-opts)

            (bling.core/bling 
             (hifi v
                   (let [pth (problem-path missing-key? problem v)]
                     {:find (merge {:path  pth
                                    :class (if (coll? (get-in v pth))
                                             :highlight-error
                                             :highlight-error-underlined)})}))
             section-break

             [label-style (if missing-key? "Missing key:" "Problem value: ")]
             section-header-break
             [:bold  (fv (if missing-key? missing-key (:value problem))) ]
             section-break
             

             (when error-message? [label-style "Message: "])
             (when error-message?
               (str section-header-break
                    (indented-string error-message)
                    section-break))

             (when must-satisfy? [label-style "Must satisfy: "])
             (when must-satisfy? section-header-break)
             (when must-satisfy? [:bold (fv schema-cleaned)])
             (when must-satisfy? section-break)


             ;; The schema related to the problem value.
             ;; Defaults to true, displaying schema
             (when-not (false? display-schema?)
               [label-style "Schema: "])
             (when-not (false? display-schema?) 
               (str section-header-break
                    (fv malli-schema-cleaned)
                    section-break))


             ;; The result of calling malli.core/explain on the value.
             ;; Defaults to false, not displaying schema
             (when (true? display-explain-data?)
               [label-style "malli.core/explain: "])
             (when (true? display-explain-data?) 
               (str section-header-break
                    (fv explain-data)
                    section-break))


             ;; The source of the call to the malli-explain
             ;; This can be provided by the user.
             (when file-info [label-style "Source: "])
             (some->> #_(symbol "app.core/my-function:21:33") file-info
                      fv
                      (str section-header-break))))))

       (println "Success!")))))
