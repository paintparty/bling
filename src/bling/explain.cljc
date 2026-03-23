(ns ^:dev/always bling.explain
  (:require
   [bling.core :as bling :refer [bling callout with-ascii-underline]]
   [bling.hifi :refer [hifi]]
   [bling.util :as util :refer [when-> when->> insert-at]]
   [clojure.string :as string]
   [clojure.walk :as walk]
   [fireworks.core :refer [!? ?]]
   [malli.core :as m]
   [malli.util :as mu]
   [malli.error :as me])
  #?(:cljs
     (:require-macros [bling.explain])))

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

;; TOD0 - consider changing this to accept a str instead of n, for perf
(defn- indented-string [n s]
  (when s
    (string/join "\n"
                 (map #(str (string/join (repeat (or n 0) " "))  %)
                      (string/split (str s)
                                    #"\n")))))

(defn- target-key? [problem v]
  (boolean
   (let [in (:in problem)]
     (when (coll? v)
       (let [vectorized (walk/postwalk #(if (list? %) (vec %) %) v)
             m          (get-in vectorized (drop-last in))
             [mek mev]  (when (map? m) (find m (last in)))]
         (and (not= mev (:value problem))
              (= mek (:value problem))))))))

(defn- problem-path
  "This creates a path to the problem value within a data structure.

   Takes into account if the problem is a missing key.

   If the problem is a key in a map-entry, it adds appends a special
   :fireworks.highlight/map-key to the path, which fireworks uses to properly
   highlight the map key"
  [{:keys [missing-keys? problem v] :as opts}]
  (cond missing-keys?
        (:in problem)
        (or (:target-key? opts) (target-key? problem v))
        (conj (:in problem) :fireworks.highlight/map-key)
        :else
        (:in problem)))

(defn- section
  [label
   v
   {:keys [compact?
           ultra-compact?
           label-style
           label-margin-bottom
           margin-top
           margin-bottom
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
              (some-> margin-top (when-> pos-int?) (repeat  "\n") string/join)
              (when label (bling [label-style label]))
              (some-> label-margin-bottom (when-> pos-int?) (repeat  "\n") string/join)
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
          (when-> vector?)
          second
          (when-> map?)
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
     (!? (some-> frm enum-schema->set))

     ;; Something like :int => int?
     (!? (:value-schema/sym problem))

     ;; Returns the :error/message from schema
     (!? (some-> problem :value-schema m/properties :error/message))

     ;; Returns the :error/fn from schema
     (some-> problem :value-schema m/properties :error/fn)

     ;; Returns the schema with option maps removed
     (:value-schema/cleaned problem)

     ;; Returns just schema form
     frm)))

;; Move this into core

(defn- file-info*
  [{:keys [:file
           :line
           :column
           :function-name]}]
  (when (and line column)
    (symbol (str (if (nil? file) "[unknown file]" file)
                 (when function-name (str "/" function-name))
                 ":"
                 (bling [:medium-red  line])
                 ":"
                 column))))

(defn- common-root-path-max [paths]
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

(defn- error-summary [m]
  (assoc m
         :error/message (me/error-message m)
         :schema/form   (m/form (:schema m))))

;; you need to check this with the root path thing
;; parent schema only makes sense if all problems same path length
;; otherwise it is like a ancestor schema

;; maybe you sould do some error rollup situation

;; TODO this should be fixed for situations where there is only one problem
(defn- parent-schema* [schema v]
  (let [common-path        (->> v (mapv :path) common-root-path-max)
        parent-schema      (mu/get-in schema common-path)
        parent-schema-form (m/form parent-schema)
        junction-type      (some-> parent-schema-form
                                   (when-> vector?)
                                   seq
                                   first
                                   (when-> #{:or :and}))]
    [parent-schema-form parent-schema junction-type]))

(defn- callout-ln
  ([s v]
   (callout-ln s v nil))
  ([s v style]
   (when-not (nil? v)
     [:p [:italic (str s ": ")]
      [(or style :blue) v]])))

(defn- ancestor-path? [grouped in-path-for-group]
  (let [debug?        false]
    (when debug?
      (callout {:colorway :info
                :theme    :gutter}
               (bling [:olive.bold "Checking schema path: "
                       [:bold.blue (str ":in "  in-path-for-group)]])))
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
               (when debug?
                 (callout
                  (apply bling
                         (remove
                          nil?
                          [(callout-ln "Potential ancestor path" in-path-for-group)
                           (callout-ln "Potential descendant path" in-path)
                           (callout-ln "Actual potential?" potential? (when-not potential? :red))
                           (callout-ln "Potential common root path" common-root-path*)
                           (callout-ln "Ancestor path?" ancestor-path? (if ancestor-path? :green.bold :red))
                           (when ancestor-path? [:p.bold "Errors at path " [:blue in-path] " will not be printed."])]))))

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

(defn- schema-path-siblings [filtered]
  (let [grouped-by-path-count (group-by (fn [[k]] (count k)) filtered)
        max-path-count        (->> grouped-by-path-count keys (apply max))]
    (some-> grouped-by-path-count
            (get max-path-count)
            first
            second)))

(defn- regrouped-errors
  "This is intended to reduce the number of errors that are reported.

   Should only work when:
   - There are multiple errors in the erroneous values's error group
   - 2 or more errors are siblings based on their schema :path value
   - The grouped siblings with the longest path are used, and all other errors
     are discarded"
  [grouped-errors]
  (let [paths-by-count* (group-by #(-> % :path drop-last vec) grouped-errors)
        grouped-errors  (if (< 1 (count paths-by-count*))
                          (let [filtered  (reduce-kv (fn [m k v]
                                                       (if (< 1 (count v))
                                                         (assoc m k v)
                                                         m))
                                                     {}
                                                     paths-by-count*)]
                            (when (seq filtered)
                              (or (schema-path-siblings filtered)
                                  grouped-errors)))
                          grouped-errors)]
    grouped-errors))

(defn- map-entry-status [reduction-path value]
  (let [map-entry-el?    (some->> reduction-path
                                  (when->> #(not (empty? %)))
                                  pop
                                  (reduce (fn [acc [f x]]
                                            (f acc x))
                                          value)
                                  map?)]
    [(when map-entry-el? (some-> reduction-path last first (= find)))
     (when map-entry-el? (some-> reduction-path last first (= get)))]))

(defn- composite-error-message [error-type errors]
  (when-let [messages (and (contains? #{:or :and} error-type)
                           (let [messages (keep :error/message errors)]
                             (when (= (count messages)
                                      (count errors))
                               messages)))]
    (string/join (bling "\n" [:italic.subtle "  or"] "\n")
                 messages)))

(defn- fq-schema-name [schema]
  (let [{ns-str   :ns
         name-str :name}
        (m/properties schema)]
    (when (and ns-str name-str)
      (symbol (str ns-str "/" name-str)))))

(defn- parent-schema-form*
  [parent-schema-form]
  (let [display-schema?
        (some-> parent-schema-form
                (when-> vector?)
                second
                ::display-schema?)

        parent-schema-form
        (if display-schema?
          (if-let [m (some-> parent-schema-form
                             (when-> vector?)
                             second
                             (when-> map?)
                             (dissoc ::display-schema?))]
            (if (empty? m)
              (into [(first parent-schema-form)]
                    (subvec parent-schema-form 2))
              (insert-at parent-schema-form 1 m))
            parent-schema-form)
          parent-schema-form)]
    [display-schema?
     parent-schema-form]))

(defn- narrowed-problem-group
  [schema
   bad-value
   in-path-for-group
   schema-path-for-group
   grouped-errors value]
  (let [grouped-errors         (regrouped-errors grouped-errors)
        common-path            (->> grouped-errors
                                    (mapv :path)
                                    common-root-path-max)
        [parent-schema-form
         parent-schema
         junction-type]        (parent-schema* schema grouped-errors)
        [display-schema?
         parent-schema-form]   (parent-schema-form* parent-schema-form)
        error-type             junction-type
        errors                 (mapv error-summary grouped-errors)
        reduction-path         (reduction-path value
                                               in-path-for-group
                                               bad-value)
        [bad-map-entry-key?
         bad-map-entry-value?] (map-entry-status reduction-path value)]
    (merge {:value                   bad-value
            :in                      in-path-for-group
            ;; TODO - Figure this out so you can pinpoint the offensive part of the
            ;; schema
            ;; :path/reduce        (reduction-path (m/form schema) 
            ;;                                     schema-path-for-group
            ;;                                     parent-schema-form)
            :in/reduce               reduction-path
            :path/common             common-path
            :parent-schema           parent-schema
            :parent-schema/form      parent-schema-form
            ::display-schema?        display-schema?
            :errors                  errors
            :schema/fq-name          (fq-schema-name schema)
            :error-group-type        error-type
            :composite-error-message (composite-error-message error-type errors)}
           (some->> display-schema? (hash-map ::display-schema?))
           (some->> junction-type (hash-map :junction-type))
           (some->> bad-map-entry-key? (hash-map :bad-map-entry-key?))
           (some->> bad-map-entry-value? (hash-map :bad-map-entry-value?))
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
                   path-for-group    :path
                   bad-value         :value}
                  grouped-errors]
               (if (ancestor-path? grouped in-path-for-group)
                 vc
                 (conj vc
                       (narrowed-problem-group schema
                                               bad-value
                                               in-path-for-group
                                               path-for-group
                                               grouped-errors
                                               value))))
             []
             grouped))]
    (into (!? narrowed-missing-key-errors)
          (!? narrowed-problem-groups*))))

(defn- prune-schema-for-display
  "Truncates the `:registry` entry. If a schema node has an options map with
   only a `:error/message` entry, the map will be removed. If said map has other
   entries, the `:error/message` entry will be dissoc'd."
  [malli-schema]
  (walk/prewalk
   (fn [x]
     (cond
       (and (vector? x)
            (= (first x) :schema)
            (some-> x
                    second
                    (when-> map?)
                    (when-> #(= 1 (count %)))
                    (when->
                     #(contains? % :registry))))
       (assoc x 1 {:registry {'... '...}})

       (and (vector? x)
            (some-> x
                    second
                    (when-> map?)

                    (when->
                     #(contains? % :error/message))))
       (if (< 1 (-> x second count))
         (assoc x 1 (-> x
                        second
                        (dissoc :error/message)))
         (into [(first x)] (subvec x 2)))

       :else
       x))
   (m/form malli-schema)))

(defn- poi-diagram-find-opts [path problem narrowed-map]
  (vec
   (remove nil?
           [{:path  path
             :class (let [x (:value problem)]
                      (if (and (coll? x)
                               (< 8 (-> x str count)))
                        :highlight-error
                        :highlight-error
                        #_:highlight-error-underlined))}

            ;; If the path ends with something that is not an int, it is likely
            ;; that problem value is mapentry value, and we can hightlight the 
            ;; corrresponding key.
            ;; TODO - make this an configurable option

            (when (some-> path
                          last
                          #(and (not= :fireworks.highlight/map-key %)
                                (not (int? %))))
              {:path  (conj path :fireworks.highlight/map-key)
               :class :highlight-info})])))

(defn- poi-diagram
  [form-with-label
   {:keys [file line column]
    :as   opts}]
  (string/replace
   (bling.core/point-of-interest
    (assoc opts
           :form                   form-with-label
           :margin-top             0
           :header-file-info-style {:color      :subtle
                                    :font-style :italic}))
   #"\n$"
   ""))

(defn- highlighted-problem-section-body
  [{:keys [select-keys-in-problem-path?
           missing-keys?
           problem
           hifi+
           v]
    :as opts}]
  (let [path                (problem-path {:missing-keys? missing-keys?
                                           :problem       problem
                                           :v             v})
        narrowed-map        (when (and true #_(? select-keys-in-problem-path?)
                                       (seq path)
                                       (not-any? coll? path)
                                       (map? v))
                              (let [trimmed (select-keys v path)]
                                (when (seq trimmed) trimmed)))
        find-opts           (poi-diagram-find-opts path problem narrowed-map)
        formatted-form      (hifi+ v {:margin-inline-start 0
                                      :find                find-opts})
        highlight-loc       (bling/highlighted-location formatted-form
                                                        :error-highlight)
        form-with-underline (bling/with-ascii-underline
                              formatted-form
                              (assoc highlight-loc
                                     :text-decoration-weight :bold
                                     :text-decoration-color  :red))
        form-with-label     (bling/with-floating-label
                              form-with-underline
                              #_formatted-form
                              (assoc highlight-loc
                                     :label-text   "<- Problem"
                                     :label-offset 5
                                     :label-style  {:color      :red
                                                    :font-style :italic}))
        poi-diagram         (poi-diagram form-with-label opts)

        ;; Make a summary to print above the problem form with highlighting,
        ;; but only if the problem is of a certain profile
        problem-summary     (cond
                              (= :missing-keys (:error-group-type problem))
                              (bling "Missing keys")

                              (:bad-map-entry-value? problem)
                              (let [k (-> problem :in last)]
                                (bling [:p "Invalid entry for "
                                        [:bold (hifi k
                                                     {:find {:pred #(= % k)}})]]
                                       #_"\n"))

                              (:bad-map-entry-key? problem)
                              (let [k (-> problem :in last)]
                                (bling "Invalid map key" #_"\n\n\n")))]
    (str (some-> problem-summary (str "\n\n"))
         poi-diagram)))

(defn- printed*
  [{:keys [highlighted-problem-section-label
           preamble-section-label
           preamble-section-body
           omit-sections
           section-opts
           indentation
           hifi+]
    :as opts}
   i
   problem]
  (let [schema         (some-> problem :schema m/form)
        error-message  (schema-error-message schema)
        missing-key    (if (= :malli.core/missing-key (:type problem))
                         (some-> problem :path last)
                         :bling.explain/no-missing-key)
        missing-key?   (not= missing-key :bling.explain/no-missing-key)
        missing-keys?  (-> problem :error-group-type (= :missing-keys))
        error-message? (boolean (and error-message (not missing-key?)))
        must-satisfy?  (boolean (and (not error-message)
                                     (not missing-keys?)))]
    (concat
     (when preamble-section-body
       (section preamble-section-label
                (bling preamble-section-body)
                (assoc section-opts :section-break? false)))

     (section highlighted-problem-section-label
              ;; TODO - when would you want to use narrowed v? 
              ;; bunched
              ;; what if value is crazy nested or after truncation?
              ;; macro version for filename
              (highlighted-problem-section-body
               (assoc opts
                      :missing-keys?
                      missing-keys?
                      :problem
                      problem))
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
                          {:find {:pred  (fn [val] (= val %))
                                  :class :info}})
                  (:missing-keys problem)))
                section-opts))

     (when-not (contains? omit-sections :problem-value)
       (when-not missing-keys?
         (section "Problem value:"
                  ;; TODO - do you need this pre-formatting?
                  (let [s (hifi+ (:value problem)
                                 {:find {:path  []
                                         :class :highlight-error}})]
                    (if (string/index-of s "\n")
                      (hifi+ (:value problem))
                      s))
                  section-opts)))

     ;; TODO - get example of this working
     (when error-message?
       (section "Message:"
                (indented-string indentation error-message)
                section-opts))

     (when must-satisfy?
       (section "Must satisfy:"
                (or (when-not (-> problem ::display-schema?)
                      (indented-string indentation
                                       (:composite-error-message problem)))
                    (if-let [junction-form
                             (when (contains? problem :junction-type)
                               (:parent-schema/form problem))]
                      (hifi+ junction-form {:print-level           3
                                            :scalar-max-length 44})
                      (hifi+ (get-satisfaction (!? problem)))))
                section-opts))

     (when-let [schema-fq-name (:schema/fq-name problem)]
       (section "Fails schema:"
                (indented-string indentation (hifi schema-fq-name))
                section-opts)))))

(defn- maybe-interleaved-with-section-separators
  [printed]
  (if (< 1 (count printed))
    (interleave
     printed
     (-> (for [i (range (count printed))]
           ["\n\n\n"
            (bling [:subtle.italic
                    (str "---- Error #"
                         (+ i 2)
                         " -------------------------------------")])
            "\n\n\n"])
         drop-last
         (concat [nil])))
    printed))

(defn- of-one? [x] (some-> x count (= 1)))

;; TODO - Revisit this and decide if different defaults for :display-schema? would be better
(defn- maybe-display-schema
  [multiple-problems? problems schema display-schema? display-explain-data? hifi+ malli-schema section-opts malli-ex-data]
  (let [single-problem-top-level?
        (or (and (not multiple-problems?)
                 (= (-> problems first :parent-schema)
                    (-> problems first :value-schema))
                 ;; TODO - why this?
                 (= (-> problems first :value-schema)
                    (m/form schema)))
            (and (of-one? problems)
                 (= (-> problems first :parent-schema)
                    (-> problems first :value-schema))))]
    [     ;; The schema passed to bling.explain/explain-malli
     ;; Defaults to true, displaying schema
     ;; If `display-explain-data?` is `true`, it does not print, as it would be
     ;; redundant given that malli.core/explain-data includes the schema
     (when (and multiple-problems?
                (or display-schema? display-explain-data?))
       ["\n\n\n"
        (bling [:subtle.italic
                (str "--------------------------------------------")])])

     (when (and display-schema?
                (not single-problem-top-level?))

       (section "Schema:"
                (hifi+ (prune-schema-for-display malli-schema)
                       ;; TODO - use new :path/reduce and create subtle :class for :find highlighting to pinpoint section of spec that is offended
                       #_{:find {:path  [2 2 1 2 1 2 1 2 1]
                                 :class :highlight-error}})
                section-opts))
     ;; The result of calling malli.core/explain on the value.
     ;; Defaults to false
     (when (true? display-explain-data?)
       (section "Result of malli.core/explain:"
                (hifi+ (explain-data* malli-ex-data))
                section-opts))]))

(defn ^:public ^:no-doc explain-malli*
  ([schema v]
   (explain-malli* schema v nil))
  ([schema
    v
    {:keys [highlighted-problem-section-label
            select-keys-in-problem-path?
            section-body-indentation
            preamble-section-label
            preamble-section-body
            display-explain-data?
            section-label-style
            omit-section-labels
            display-schema?
            success-message
            file-info-str
            omit-sections
            callout-opts
            hifi-opts
            spacing
            column
            file
            line]
     :or   {success-message :bling.explain/explain-malli-success
            section-label-style {:font-style :italic :color :subtle}}
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
            (hifi v
                  (merge {:scalar-mapkey-max-length 30
                          :margin-inline-start          indentation
                          :print-level                  12}
                         opts
                         hifi-opts))))

         explain-malli-opts
         opts

         file-info
         (or (when (string? file-info-str) file-info-str)
             (bling.core/file-info-str
              (merge {:style {:font-style :italic
                              :color      :subtle}}
                     explain-malli-opts)))

         compact?
         (= :compact spacing)

         ultra-compact?
         (= :ultra-compact spacing)

         display-schema?
         (and (not (true? display-explain-data?))
              (not (false? display-schema?)))

         omit-sections
         (some->> omit-sections
                  seq
                  (into #{}))

         omit-section-labels
         (some->> omit-section-labels
                  seq
                  (into #{}))

         section-opts
         {:compact?            compact?
          :ultra-compact?      ultra-compact?
          :label-style         section-label-style
          :omit-section-labels omit-section-labels}]
     (if (seq problems)
       (let [problems
             (narrow-problems malli-ex-data)

             num-problems
             (count problems)

             multiple-problems?
             (< 1 num-problems)

             printed
             (map-indexed (partial printed*
                                   (merge opts
                                          {:indentation-str indentation-str
                                           :section-opts    section-opts
                                           :v               v
                                           :hifi+           hifi+
                                           :omit-sections   omit-sections
                                           :indentation     indentation}))
                          problems)

             printed-with-numbering
             (concat
              (maybe-interleaved-with-section-separators printed)

              (maybe-display-schema multiple-problems?
                                    problems
                                    schema
                                    display-schema?
                                    display-explain-data?
                                    hifi+
                                    malli-schema
                                    section-opts
                                    malli-ex-data))

             callout-padding-block
             (cond ultra-compact? 0 compact? 1 :else 1)

             callout-margin-block
             #?(:cljs 0 :clj 1) ;; <- TODO cljs default of 0 should be isolated to browser

             callout-label
             (str "Malli Schema Error"
                  (when multiple-problems? "s")
                  (when multiple-problems?
                    (str " (" num-problems ")")))]

         (callout
          (!? (merge {:type                :error
                      :theme               :sandwich
                      :label-theme         :simple
                      :label               callout-label
                      :side-label          file-info
                      :margin-top          callout-margin-block
                      :margin-bottom       callout-margin-block
                      ;; :min-width           60
                      :border-notches?     true
                      :header-padding-left 3
                      :padding-left        2
                      :padding-top         callout-padding-block
                      :padding-bottom      callout-padding-block}
                     callout-opts))
          (apply str (flatten printed-with-numbering)))

         problems)

       ;; If validation was successful, and user supplied a success message
       (when-not (nil? success-message)
         (case success-message

           ::explain-malli-success-verbose
           (callout (merge {:colorway       :positive
                            :label-theme    :simple
                            :padding-top    1
                            :padding-bottom 1}
                           callout-opts
                           {:label "Malli Schema Validation Success"})
                    (bling (when file-info-str
                             [:p [:italic "Source:"]])
                           (when file-info-str
                             [:p
                              indentation-str
                              file-info-str])
                           [:p [:italic "Value:"]]
                           [:p (hifi v {:margin-inline-start 2})]
                           [:p [:italic "Schema:"]]
                           (hifi schema {:margin-inline-start 2})))

           ::explain-malli-success-simple
           (println (str "Malli schema validation success"
                         (when file-info-str
                           (str " @ " file-info-str))))
           (println success-message)))))))

(defmacro ^:public explain-malli
  "Prints a Malli validation error callout block via `bling.core/callout`.
   
   Within the block, the value is pretty-printed, potentially with syntax
   coloring. The problem value is highlighted with the `:highlight-error`
   class of the active fireworks theme, or the `:highlight-error-underlined`
   class, if the value is not a collection.
   
   If three arguments are provided, the third should be a map
                        with the following optional keys:
   
   * **`:function-name`**
       - `string?`
       - Optional.
       - The name of the function that can be used to construct
         the source location.
   
   * **`:spacing`**
       - `#{\"compact\" :compact}`
       - Optional.
       - If the value of `:spacing` is set to `:compact`, the
         callout is compacted vertically.
   
   * **`:display-schema?`**
       - `boolean?`
       - Optional.
       - Displays the schema passed to the underlying call to
         `malli.core/explain`.
   
   * **`:display-explain-data?`**
       - `boolean?`
       - Optional.
       - Displays the output of `malli.core/explain` within the
         callout block.
   
   * **`:callout-opts`**
       - `map?`
       - Optional.
       - A map of options for the underlying call to
         bling.core/callout.
   
   * **`:hifi-opts`**
       - `map?`
       - Optional.
       - The options map for bling.hifi/hifi
   
   * **`:file`**
       - `string?`
       - Optional.
       - The file name of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override
   
   * **`:line`**
       - `int?`
       - Optional.
       - The line number of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override.
   
   * **`:column`**
       - `int?`
       - Optional.
       - The column number of the call site.
         This value be automatially supplied by the macro,
         so only use if you want to manually override.
   
   * **`:highlighted-problem-section-label`**
       - `string?`
       - Optional.
       - Label for the highlighted problem diagram
   
   * **`:section-body-indentation`**
       - `pos-int?`
       - Optional.
       - Number of spaces to indent the body of each section
   
   * **`:preamble-section-label`**
       - `string?`
       - Optional.
       - The label of the preamble section
   
   * **`:preamble-section-body`**
       - `string?`
       - Optional.
       - The body of the preamble section
   
   * **`:success-message`**
       - `string?`
       - Optional.
       - The message to display if value passes schema validation"
  {:desc    "Prints a Malli validation error callout block via `bling.core/callout`.
             
             Within the block, the value is pretty-printed, potentially with syntax
             coloring. The problem value is highlighted with the `:highlight-error`
             class of the active fireworks theme, or the `:highlight-error-underlined`
             class, if the value is not a collection."
   :options [:map
             {:name 'options
              :desc "If three arguments are provided, the third should be a map
                     with the following optional keys"}

             [:function-name
              {:optional true
               :desc     "The name of the function that can be used to construct
                          the source location."}
              :string]

             [:spacing
              {:optional true
               :desc     "If the value of `:spacing` is set to `:compact`, the
                          callout is compacted vertically."}
              [:enum :compact "compact"]]

             [:display-schema?
              {:optional true
               :desc     "Displays the schema passed to the underlying call to
                          `malli.core/explain`."}
              :boolean]

             [:display-explain-data?
              {:optional true
               :desc     "Displays the output of `malli.core/explain` within the
                          callout block."}
              :boolean]

             [:callout-opts
              {:optional true
               :desc     "A map of options for the underlying call to
                          bling.core/callout."}
              :map]

             [:hifi-opts
              {:optional true
               :desc     "The options map for bling.hifi/hifi"}
              :map]

             [:file
              {:optional true
               :desc     "The file name of the call site.
                          This value be automatially supplied by the macro,
                          so only use if you want to manually override"}
              :string]

             [:line
              {:optional true
               :desc     "The line number of the call site.
                          This value be automatially supplied by the macro,
                          so only use if you want to manually override."}
              :int]

             [:column
              {:optional true
               :desc     "The column number of the call site.
                          This value be automatially supplied by the macro,
                          so only use if you want to manually override."}
              :int]

             [:highlighted-problem-section-label
              {:optional true
               :desc     "Label for the highlighted problem diagram"}
              :string]

             [:section-body-indentation
              {:optional true
               :desc     "Number of spaces to indent the body of each section"}
              :pos-int]

             [:preamble-section-label
              {:optional true
               :desc     "The label of the preamble section"}
              :string]

             [:preamble-section-body
              {:optional true
               :desc     "The body of the preamble section"}
              :string]

             [:success-message
              {:optional true
               :desc     "The message to display if value passes schema validation"}
              :string]]}

  ([schema v]
   (let [{:keys [file line column]} (meta &form)]
     `(bling.explain/explain-malli* ~schema
                                    ~v
                                    {:file   ~file
                                     :line   ~line
                                     :column ~column})))
  ([schema v opts]
   (let [{:keys [file line column]} (meta &form)]
     `(bling.explain/explain-malli* ~schema
                                    ~v
                                    (merge {:file   ~file
                                            :line   ~line
                                            :column ~column}
                                           ~opts)))))
