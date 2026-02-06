(ns bling.explain
  (:require
   [bling.core :refer [bling callout]]
   [bling.hifi :refer [hifi]]
   [bling.util :as util :refer [maybe->]]
   [bling.macros :refer [keyed]]
   [clojure.string :as string]
   [clojure.walk :as walk]
   [fireworks.core :refer [!? ?]]
   [malli.core :as m]
   [malli.util :as mu]
   [malli.error :as me]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Malli explain 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:private core-preds-by-keyword
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


(defn- problem-path
  "This creates a path to the problem value within a data structure.

   It takes into account if the problem is a missing key.

   If the problem is a key in a map-entry, it adds appends a special
   :fireworks.highlight/map-key to the path, which fireworks uses to properly
   highlight the map key"
  [missing-keys? problem v]
  (cond missing-keys?
        (:in problem)
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
  (let  [section-break        (cond ultra-compact? "\n\n"
                                    compact?       "\n\n\n"
                                    :else          "\n\n\n")
         section-header-break (if (or ultra-compact? compact?) "\n" "\n")
         label                (when label
                                (if (and omit-section-labels
                                         (contains? omit-section-labels label))
                                  nil
                                  label))]
    (vec
     (remove nil?
             [(when section-break? section-break)
              (when label (bling [label-style label]))
              (when label section-header-break)
              v]))))


(defn- explain-data*
  "Calls `malli.core/form` on all :schema objects, for readability"
  [malli-ex-data]
  (let [ret2 (walk/postwalk
              (fn [x]
                (if (and (map? x)
                         (contains? x :schema))
                  (assoc x :schema (m/form (:schema x)))
                  x))
              malli-ex-data)]
    ret2))


(defn- schema-error-message [schema]
  (some-> schema
          (maybe-> vector?)
          second
          (maybe-> map?)
          :error/message))


(defn- enum-schema? [schema]
  (and (vector? schema)
       (< 1 (count schema))
       (= :enum (first schema))))


(defn- enum-schema->set [schema]
  (when (enum-schema? schema)
    (->> schema rest (into #{}))))


(defn- get-satisfaction
  [problem]
  (let [frm (problem :value-schema/form)]
    (or 
     ;; Turns [:enum :foo :bar :baz] => #{:foo :bar :baz}
     (some-> frm enum-schema->set)

     ;; Something like :int => int?
     (:value-schema/sym problem)

     ;; Returns the :error/message from schema
     (some-> frm m/properties :error/message)

     ;; Returns the :error/fn from schema
     (some-> frm m/properties :error/fn)

     ;; Returns the schema with option maps removed
     (:value-schema/cleaned problem)

     ;; Returns just schema form
     frm)))


(defn- file-info*
  [{:keys [:file :line :column :function-name]}]
  (when (and file line column)
    (symbol (str file
                 (when function-name (str "/" function-name))
                 ":"
                 line
                 ":"
                 column))))


(defn common-root-path-max [paths]
  (when (seq paths)
    (let [min-length (apply min (map count paths))]
      (loop [i 0]
        (if (>= i min-length)
          (vec (take i (first paths)))
          (let [element (nth (first paths) i)]
            (if (every? #(= element (nth % i)) paths)
              (recur (inc i))
              (vec (take i (first paths))))))))))


(defn- reduction-path*
  [p acc x]
  (cond
    (or (map? acc) (set? acc))
    (do (swap! p conj [get x])
        (!? 'map-or-set (get acc x)))
    (coll? acc)
    (do (swap! p conj [nth x])
        (!? 'coll? (nth acc x)))
    :else
    acc))


(defn- reduction-path
  [value in-path-for-group bad-value]
  (let [p (atom [])
        v (reduce (partial reduction-path* p) value in-path-for-group)
        p (let [[f] (last @p)]
            (if (and (= f get) (not= v bad-value))
              (assoc-in @p [(-> @p count dec) 0] find)
              @p))]
    p))


(defn error-summary [m]
  (assoc m 
         :error/message (me/error-message m)
         :schema/form   (m/form (:schema m))))


(defn- parent-schema* [schema v]
  (let [common-path        (->> v (mapv :path) common-root-path-max)
        parent-schema      (mu/get-in schema common-path)
        parent-schema-form (m/form parent-schema)
        junction-type      (some-> parent-schema-form
                            (maybe-> vector?)
                            seq
                            first
                            (maybe-> #{:or :and}))]
    [parent-schema-form parent-schema junction-type]))


(defn- callout-ln
  ([s v]
   (callout-ln s v nil))
  ([s v style]
   (when-not (nil? v) 
     [:p [:italic (str s ": ")]
      [(or style :blue) v]])))


(defn- ancestor-path? [grouped in-path-for-group]
  (let [debug?  false
        callout (if debug? callout (constantly nil))]
    (callout {:colorway :info
              :theme    :gutter}
             (bling [:olive.bold "Checking schema path: "
                     [:bold.blue (str ":in "  in-path-for-group)]]))
    (some (let [in-path-for-group-count (count in-path-for-group)]
            #(let [in-path           (some->> % first :in)
                   potential?        (and (not= in-path in-path-for-group)
                                          (< in-path-for-group-count (count in-path)))
                   common-root-path* (when potential?
                                       (some->> in-path
                                                (take in-path-for-group-count)
                                                vec))
                   ancestor-path?    (boolean (some-> common-root-path*
                                                      (= in-path-for-group)))]
               (callout 
                (apply bling
                       (remove
                        nil?
                        [(callout-ln "Potential ancestor path" in-path-for-group)
                         (callout-ln "Potential descendant path" in-path)
                         (callout-ln "Actual potential?" potential? (when-not potential? :red))
                         (callout-ln "Potential common root path" common-root-path*)
                         (callout-ln "Ancestor path?" ancestor-path? (if ancestor-path? :green.bold :red))
                         (when ancestor-path? [:p.bold "Errors at path " [:blue in-path] " will not be printed."])])))
               ancestor-path?))
          grouped)))


(defn- value-schema-details [v]
  (when-let [value-schema (some-> v first :schema)]
    (let [value-schema-form    (m/form value-schema)
          {:keys [fn sym tag]} (get core-preds-by-keyword value-schema-form)]
      (into {}  
            (remove (comp nil? val)
                    {:value-schema      value-schema 
                     :value-schema/form value-schema-form
                     :value-schema/fn   fn
                     :value-schema/sym  sym
                     :value-schema/tag  tag})))))


(defn- narrowed-problem-group
  [schema bad-value in-path-for-group grouped-errors value]
  (let [[parent-schema-form
         parent-schema 
         junction-type] (parent-schema* schema grouped-errors)
        ;; schema-form     (m/form schema)
        error-type (or junction-type
                       #_(when ()))]
    (merge {:value              bad-value
            :in                 in-path-for-group
            :path/reduce        (reduction-path value in-path-for-group bad-value)
            :path/common        (->> grouped-errors
                                     (mapv :path) 
                                     common-root-path-max)
            :parent-schema      parent-schema
            :parent-schema/form parent-schema-form
            :errors             (mapv error-summary grouped-errors)
            :error-group-type   error-type}
           (some->> junction-type (hash-map :junction-type))
           (when (and (not junction-type) 
                      (= 1 (count grouped-errors)))
             (value-schema-details grouped-errors)))))


(defn- narrow-problems
  [{:keys [value schema errors]
    :as   malli-ex-data}]
  (!? (m/form schema))
  (!? value)
  (!? errors)
  (let [[missing-key-errors errors]
        (util/partition-by-pred #(-> % :type (= :malli.core/missing-key)) errors)
        
        missing-key-errors-grouped
        (!? (group-by #(-> % :in pop) missing-key-errors))

        narrowed-missing-key-errors
        (reduce-kv
         (fn [vc k v]
           (conj vc
                 {:missing-keys     (mapv #(-> % :in last) v)
                  :error-group-type :missing-keys
                  :in               k 
                  :value            (get-in value k)
                  :errors           v}))
         []
         missing-key-errors-grouped)

        ;; Erros are grouped if both the erroneous values and location in the schema are the same
        grouped 
        (group-by #(select-keys % [:in :value]) errors)

        ;; Filter out errors that are ancestors of other errors so that we only report directly erroneous results ala m/humanize
        narrowed-problem-groups* 
        (!? (reduce-kv
            (fn [vc
                 {in-path-for-group :in
                  bad-value         :value} 
                 grouped-errors]
              (if (ancestor-path? grouped in-path-for-group)
                vc
                (conj vc
                      (narrowed-problem-group schema 
                                              bad-value
                                              in-path-for-group
                                              grouped-errors
                                              value))))
            []
            grouped))]
    (into (? narrowed-missing-key-errors)
          (? narrowed-problem-groups*))))


;; This is hifi printing of problem, study input and replicate
(defn- highlighted-problem-section
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
              {:find                (vec
                                     (remove nil?
                                             [{:path  pth
                                               :class (if (coll? (get-in v pth))
                                                        :highlight-error
                                                        :highlight-error-underlined)}
                                              (when narrowed-map
                                                {:pred  #(= % (first pth))
                                                 :class :info-error})]))
               :margin-inline-start indentation})))

  

  (defn ^:public explain-malli
    "Prints a Malli validation error \"callout\" block via bling.core/callout. 
   
   Within the block, the value is pretty-printed, potentially with syntax coloring. The problem value is highlighted with the `:highlight-error` class of the active fireworks theme, or the `:highlight-error-underlined` class, if the value is not a collection.
   
   If three arguments are provided, the third should be a map with the following optional keys:
   
   * **`:function-name`**
       - `string?`
       - Optional.
       - The name of the function that can be used to construct the source location.
   
   * **`:file`**
       - `[:or :pos-int :string]`
       - Optional.
       - The file name that can be used to construct the source location.
   
   * **`:line`**
       - `[:or :pos-int :string]`
       - Optional.
       - The line number that can be used to construct the source location.
   
   * **`:column`**
       - `[:or :pos-int :string]`
       - Optional.
       - The column number that can be used to construct the source location.
   
   * **`:spacing`**
       - `#{\"compact\" :compact}`
       - Optional.
       - If the value of `:spacing` is set to `:compact`, the callout is compacted vertically.
   
   * **`:display-schema?`**
       - `boolean?`
       - Optional.
       - Displays the schema passed to the underlying call to `malli.core/explain`.
   
   * **`:display-explain-data?`**
       - `boolean?`
       - Optional.
       - Displays the output of `malli.core/explain` within the callout block.
   
   * **`:callout-opts`**
       - `map?`
       - Optional.
       - A map of options for the underlying call to bling.core/callout."
    {:desc    ["Prints a Malli validation error \"callout\" block via bling.core/callout."
               "\n\n"
               "Within the block, the value is pretty-printed, potentially with syntax"
               "coloring. The problem value is highlighted with the `:highlight-error`"
               "class of the active fireworks theme, or the `:highlight-error-underlined`"
               "class, if the value is not a collection."]
     :options [:map
               {:name 'options
                :desc "If three arguments are provided, the third should be a map with the following optional keys"}
               [:function-name
                {:optional true
                 :desc     ["The name of the function that can be used to construct"
                            "the source location."]}
                :string]

               [:file
                {:optional true
                 :desc     ["The file name that can be used to construct the source"
                            "location."]}
                [:or :pos-int :string]]

               [:line
                {:optional true
                 :desc     ["The line number that can be used to construct the source"
                            "location."]}
                [:or :pos-int :string]]

               [:column
                {:optional true
                 :desc     ["The column number that can be used to construct the"
                            "source location."]}
                [:or :pos-int :string]]

               [:spacing
                {:optional true
                 :desc     ["If the value of `:spacing` is set to `:compact`, the"
                            "callout is compacted vertically."]}
                [:enum :compact "compact"]]

               [:display-schema?
                {:optional true
                 :desc     ["Displays the schema passed to the underlying call to"
                            "`malli.core/explain`."]}
                :boolean]

               [:display-explain-data?
                {:optional true
                 :desc     ["Displays the output of `malli.core/explain` within the"
                            "callout block."]}
                :boolean]

               [:callout-opts
                {:optional true
                 :desc     ["A map of options for the underlying call to"
                            "bling.core/callout."]}
                :map]
               
               [:file
                {:optional true
                 :desc     ["The file name of the call site"]}
                :string]

               [:line
                {:optional true
                 :desc     ["The line number of the call site"]}
                :int]

               [:column
                {:optional true
                 :desc     ["The column number of the call site"]}
                :int]]}
    ([schema v]
     (explain-malli schema v nil))
    ([schema
      v
      {:keys [highlighted-problem-section-label
              select-keys-in-problem-path?
              section-body-indentation
              highlight-missing-keys?
              preamble-section-label
              preamble-section-body
              display-explain-data?
              omit-section-labels
              display-schema?
              success-message
              file-info-str
              omit-sections
              callout-opts
              spacing
              column
              file
              line]
       :or   {success-message :bling.explain/explain-malli-success}
       :as   opts}]
     (let [{problems     :errors
            malli-schema :schema
            :as          malli-ex-data}
           (m/explain schema v)

           indentation
           (or section-body-indentation 2)

           indentation-str
           (string/join (repeat (or indentation 0) " "))
           
           hifi+
           (fn hifi+ 
             ([v]
              (hifi+ v nil))
             ([v opts]
              (hifi v (merge {:margin-inline-start indentation} opts))))
           
           explain-malli-opts   
           opts
           
           file-info           
           (or (when (string? file-info-str) file-info-str)
               (file-info* explain-malli-opts))

           compact?             (= :compact spacing)

           ultra-compact?       (= :ultra-compact spacing)]
       (if (seq problems)
         (let [problems (do
                          ;;  (!? (m/form schema))
                          ;;  (!? (me/error-value malli-ex-data {::me/mask-valid-values '...}))
                          ;;  (!? (me/humanize malli-ex-data {::me/mask-valid-values '...}))
                          (narrow-problems malli-ex-data))]

           (doseq [problem problems]
             (let [schema              (some-> problem :schema m/form)

                   error-message       (schema-error-message schema)

                   missing-key         (if (= :malli.core/missing-key (:type problem))
                                         (some-> problem :path last)
                                         :bling.explain/no-missing-key)

                   missing-key?        (not= missing-key :bling.explain/no-missing-key)

                   missing-keys?       (-> problem :error-group-type (= :missing-keys))

                   error-message?      (boolean (and error-message (not missing-key?)))

                   must-satisfy?       (boolean (and (not error-message)
                                                     (not missing-keys?)))

                   label-style         :italic

                   display-schema?     (and (not (true? display-explain-data?))
                                            (not (false? display-schema?)))

                   omit-sections       (some->> omit-sections
                                                seq
                                                (into #{}))

                   omit-section-labels (some->> omit-section-labels
                                                seq
                                                (into #{}))

                   section-opts        {:compact?            compact?
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
                                            (bling [:italic file-info]))
                          :margin-top     #?(:cljs 0 :clj 1)
                          :padding-top    (cond ultra-compact? 0 compact? 0 :else 1)
                          :padding-bottom (if (or ultra-compact? compact?) 0 1)}
                         callout-opts)]

                 (when preamble-section-body
                   (section preamble-section-label
                            (bling indentation-str preamble-section-body)
                            (assoc section-opts :section-break? false)))

                 (section highlighted-problem-section-label
                          ;; TODO - when would you want to use narrowed v? 
                          ;; bunched
                          ;; what if value is crazy nested or after truncation?
                          ;; macro version for filename
                          (let [path         (problem-path missing-keys? problem v)
                                narrowed-map (when (and select-keys-in-problem-path?
                                                        (seq path)
                                                        (not-any? coll? path)
                                                        (map? v))
                                               (let [trimmed (select-keys v path)]
                                                 (when (seq trimmed) trimmed)))
                                ;; v            (or narrowed-map v)
                                opts         {:find (vec
                                                     (remove nil?
                                                             [{:path  path 
                                                               :class :highlight-error}
                                                              (when narrowed-map
                                                                {:pred  #(= % (first path))
                                                                 :class :info-error})]))}
                                poi-diagram  (string/replace
                                              (bling.core/point-of-interest
                                               {:file                          file
                                                :line                          line
                                                :column                        column
                                                :truncate-form-to-single-line? false
                                                :form                          v
                                                :form-hifi-options             opts
                                                :margin-block                  0})
                                              #"\n$"
                                              "")]
                            #_(!? (keyed [path narrowed-map v malli-ex-data]))
                            poi-diagram)
                          (assoc section-opts
                                 :section-break?
                                 (if preamble-section-body true false)))

                 (when-let [missing-keys (-> problem :missing-keys seq)]
                   (section (str "Missing key"
                                 (when (< 1 (count missing-keys)) "s")
                                 ":")
                            (string/join
                             "\n"
                             (mapv
                              #(hifi+ %
                                      (when highlight-missing-keys?
                                        {:find {:pred  (fn [val] (= val %))
                                                :class :info-error}}))
                              (:missing-keys problem)))
                            section-opts))

                 (when-not (contains? omit-sections :problem-value)
                   (when-not missing-keys?
                     (section "Problem value:" 
                              (hifi+ (:value problem))
                              section-opts)))

                 (when error-message?
                   (section "Message:"
                            (indented-string indentation error-message)
                            section-opts))

                 (when must-satisfy?
                   (section "Must satisfy:"
                            (if-let [junction-form 
                                     (when (contains? problem :junction-type)
                                       (:parent-schema/form problem))]
                              (hifi+ junction-form)
                              (hifi+ (get-satisfaction problem)))
                            section-opts))

                 ;; The schema passed to bling.explain/explain-malli
                 ;; Defaults to true, displaying schema
                 (when display-schema?
                   (section "Schema:"
                            (hifi+ (m/form malli-schema))
                            section-opts))

                 ;; The result of calling malli.core/explain on the value.
                 ;; Defaults to false, not displaying schema
                 (when (true? display-explain-data?)
                   (section "Result of malli.core/explain:"
                            (hifi+ (explain-data* malli-ex-data))
                            section-opts)))))))

         (when-not (nil? success-message)
           (case success-message

             ::explain-malli-success-verbose
             (callout (merge {:colorway       :positive
                              :label-theme    :marquee
                              :padding-top    1
                              :padding-bottom 1}
                             callout-opts
                             {:label "Malli Schema Validation Success"})
                      (bling (when file-info-str [:p [:italic "Source:"]])
                             (when file-info-str [:p indentation-str file-info-str])
                             [:p [:italic "Value:"]]
                             [:p (hifi v {:margin-inline-start 2})]
                             [:p [:italic "Schema:"]]
                             (hifi schema {:margin-inline-start 2})))

             ::explain-malli-success-simple
             (println (str "Malli schema validation success"
                           (when file-info-str
                             (str " @ " file-info-str))))
             (println success-message)))))))

  (defn friends
    "One-line ASCII emoji with \"wtf\" optional prefix
   
   Basic example
   ```clojure
   (friends)
   ;; =>
   \"¯\\_(ツ)_/¯\"
   ```
   
   Example with prefix and `:flipping` moji
   ```clojure
   (friends {:prefix \"Flip a table: \", :moji :flipping})
   ;; =>
   \"Flip a table: (╯°□°）╯︵ ┻━┻\"
   ```
   
   Illustrated example
   ```clojure
   (callout {:type                   :error
             ;; :colorway            :purple           ; <- any bling palette color, overrides :type
             ;; :label               \"My label\"      ; overrides label assigned by :theme
             :side-label             \"My side label\" ; must have a :label if you want a :side-label        
             :theme                  :sideline         ; :sideline :sideline-bold :minimal :gutter
             :label-theme            :minimal          ; :minimal :marquee
             ;; :padding-top         0                 
             ;; :padding-left        2                 
             ;; :padding-bottom      0                 
             ;; :padding-right       0                 
             ;; :margin-top          1                 
             ;; :margin-botom        0                 
             ;; :margin-left         0                 
             ;; :data?               true              ; <- just returns string, no printing
             })```
   
   All the options:
   
   * **`:prefix`**
       - `string?`
       - Optional.
       - Text to prefix the moji
   
   * **`:moji`**
       - `#{:flipping :happy :shruggie :lenny :excited :crying}`
       - Optional.
       - Defaults to `:shruggie`.
       - The name of the friend. 
         [See more](https://pets.com)"
    {:desc     "One-line ASCII emoji with \"wtf\" optional prefix"

     :examples [{:desc   "Basic example"
                 :form   '(friends)
                 :result "¯\\_(ツ)_/¯"}

                {:desc   "Example with prefix and `:flipping` moji"
                 :form   '(friends {:prefix "Flip a table: "
                                    :moji   :flipping})
                 :result "Flip a table: (╯°□°）╯︵ ┻━┻"}

                {:desc "Illustrated example"
                 :form "(callout {:type                   :error
                     |          ;; :colorway            :purple           ; <- any bling palette color, overrides :type
                     |          ;; :label               \"My label\"      ; overrides label assigned by :theme
                     |          :side-label             \"My side label\" ; must have a :label if you want a :side-label        
                     |          :theme                  :sideline         ; :sideline :sideline-bold :minimal :gutter
                     |          :label-theme            :minimal          ; :minimal :marquee
                     |          ;; :padding-top         0                 
                     |          ;; :padding-left        2                 
                     |          ;; :padding-bottom      0                 
                     |          ;; :padding-right       0                 
                     |          ;; :margin-top          1                 
                     |          ;; :margin-botom        0                 
                     |          ;; :margin-left         0                 
                     |          ;; :data?               true              ; <- just returns string, no printing
                     |          })"}]

     :options  [:map
                {:desc "All the options"}
                [:prefix
                 {:desc     "Text to prefix the moji"
                  :optional true}
                 :string]
                [:moji
                 {:desc     "The name of the friend. \n[See more](https://pets.com)"
                  :optional true
                  :default  :shruggie}
                 [:enum
                  :crying
                  :flipping
                  :happy
                  :excited
                  :lenny
                  :shruggie]]]}
    ([]
     (friends nil))
    ([{:keys [prefix moji]}]
     (let [mojis {:crying   "ಥ_ಥ"
                  :flipping "(╯°□°）╯︵ ┻━┻"
                  :happy    "ヽ(・∀・)ﾉ"
                  :excited  "ヾ(≧▽≦*)o"
                  :lenny    "( ͡° ͜ʖ ͡°)"
                  :shruggie "¯\\_(ツ)_/¯"}
           moji  (if (contains? mojis moji) moji :shruggie)]
       (str prefix (get mojis moji)))))
