;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public function metadata-map -> docstring
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns bling.docsgen
  (:require [clojure.string :as string]
            [rewrite-clj.zip :as z]
            [rewrite-clj.node :as n]
            [cljfmt.core]
            [bling.ansi]
            [bling.util :refer [join-lines]]
            [clojure.walk :as walk]))


(defn- esc-quoted-symbol [x]
  (symbol (str "\\\"" x "\\\"")))


(defn- docstring-quoted [x]
  (cond (string? x)
        (esc-quoted-symbol x)
        (coll? x)
        (walk/postwalk
         (fn [x]
           (let [ret (if (string? x) (esc-quoted-symbol x) x)]
             ret))
         x)
        :else
        x))


(defn- format-pred [pred]
  (cond (keyword? pred)
        (-> pred name (str "?") symbol)
        (and (vector? pred) (= (first pred) :enum))
        (->> pred
             rest
             (mapv docstring-quoted)
             (into #{})
             str)
        :else
        pred))


(defn- md-code [s]
  (str "`" s "`"))


(defn- md-code-bold [s]
  (str "**`" s "`**"))


(defn- subline [s]
  (str "    - " s))


(defn- sublines [{:keys [optional desc default]} pred]
  (remove
   nil?
   [(subline (md-code (format-pred pred)))
    (subline (if (= optional true) "Optional." "Required."))
    (when default
      (subline (str "Defaults to `" default "`.")))
    (when-let [s (cond (and (vector? desc) (every? string? desc))
                       (string/join " " desc)

                       (and (map? desc)
                            (and (:bullet-text desc)
                                 (:sub-bullet-level desc)
                                 (:sub-bullets desc)))
                       (str (:bullet-text desc)
                            "\n"
                            (join-lines
                             (mapv #(str "        - " %)
                                   (:sub-bullets desc))))

                       (string? desc)
                       desc)]
      (subline s))]))


(defn- options->md [options]
  (let [{:keys [desc]
         :as   m}             (nth options 1 nil)
        all-the-options       (if m (drop 2 options) (rest options))
        options-section-label (str (or desc "All the options") ":")]
    [options-section-label
     (reduce (fn [s [option m pred]]
               (str s
                    "* " (md-code-bold option)
                    "\n"
                    (join-lines (sublines m pred))
                    "\n\n"))
             ""
             all-the-options)]))


(defn- example->md [example]
  (let [{:keys [desc samples]} example]
    (str desc
         "\n\n"
         "```clojure\n"
         (join-lines (mapv #(str (docstring-quoted %)) samples))
         "\n```")))


(defn- vec-of-strings?
  "Returns true if x is a vector where every element is a string."
  [x]
  (and (vector? x) (every? string? x)))


(defn- desc->str [desc]
  (cond (vec-of-strings? desc)
        (string/replace
         (join-lines " "
                     (mapv #(string/replace % #"\"" "\\\\\"")
                           desc))
         #"\n ([^\s])"
         #(str "\n" (second %)))
        (string? (string/replace desc #"\"" "\\\""))
        desc))


(defn- docsgen-section
  [{:keys [desc examples options]} acc x]
  (cond
    (= x :desc)
    (conj acc (desc->str desc))

    (and (vector? x) (= (first x) :examples))
    (if-let [ex (some->> examples
                         (filter #(-> x second (= (:id %))))
                         first
                         example->md)]
      (conj acc ex)
      acc)

    (= x :options)
    (if (seq options)
      (into acc (options->md options))
      acc)))


(defn docsgen-template [m]
  (let [examples
        (some->> m
                 :examples
                 (reduce (fn [vc example]
                           (or (some->> example
                                        :id
                                        (vector :examples)
                                        (conj vc))
                               vc))
                         []))]
    (vec (remove nil? (concat [:desc] examples [:options])))))


(defn metadata-map->docstring
  "Experimental utility for repl usage, intended for turning existing malli 
   schemas for options maps into codeblocks that live in a docstring.
   
   Resulting docstrings and optimized for consumption by
   [quickdoc](https://github.com/borkdude/quickdoc).
   
   Expects a map, usually a metadata map of a function.

   Returns a string."
  [m]
  (let [template (or (:docsgen m)
                     (docsgen-template m))
        sections (reduce (partial docsgen-section m) [] template)]
    (-> (str "\"" (string/join "\n\n" sections) "\"")
        (string/split #"\n")
        (->> (map-indexed (fn [i x] (str (if (zero? i) "  " "   ") x)))
             join-lines)
        (string/replace #"(?:\n +)+\"$" "\""))))


(defn docstring->string-node
  "Capitalizes the first letter of a string."
  [s]
  (some-> s
          (string/replace #"^  \"|\"$" "")
          (string/split #"\n")
          n/string-node))


(defn insert-docstring
  "Inserts a docstring between function name and metadata."
  [zloc s]
  (-> zloc
      (z/right)                                  ; move to fn name
      (z/right)                                  ; move to metadata or params
      (z/insert-left (docstring->string-node s)) ; insert docstr before metadata
      (z/insert-left (n/newline-node "\n"))      ; add newline for formatting
      (z/up)))                                   ; go back to defn level


(defn process-defn
  "Processes a single defn form, adding docstring if it has :desc metadata."
  [zloc stats]
  (if (and (z/list? zloc)
           (= 'defn (z/sexpr (z/down zloc))))
    (let [fn-name               (z/sexpr (-> zloc z/down z/right))
          {:keys [desc] :as mm} (z/sexpr (-> zloc z/down z/right z/right))]
      (if desc
        (let [docstring    (metadata-map->docstring mm)
              updated-zloc (insert-docstring (z/down zloc) docstring)]
          [updated-zloc (update stats :updated conj fn-name)])
        [zloc stats]))
    [zloc stats]))


(defn update-file-docstrings
  "Creates and adds docstrings to functions from function metadata.

   Expects a valid source path, and an optional map of options.

   Optionally prints a report.

   Returns a map of :source-path and :updated-functions.
   
   Options:
   
   * **`print-report?`**
       - `boolean?`
       - Optional.
       - Default is `true`.
       - Prints a report with source path and updated-functions.

   * **`cljfmt-options`**
       - `map?`
       - Optional.
       - See `cljfmt` [Formatting Options](https://github.com/weavejester/cljfmt?tab=readme-ov-file#formatting-options).
   "
  ([source-path]
   (update-file-docstrings source-path nil))
  ([source-path cljfmt-options]
   (let [zloc               (z/of-file source-path)
         [final-zloc stats] (loop [loc   zloc
                                   stats {:updated []}]
                              (if (z/end? loc)
                                [loc stats]
                                (let [[new-loc new-stats] 
                                      (process-defn loc stats)]
                                  (recur (z/next new-loc) new-stats))))
         updated-content    (-> final-zloc
                                z/root-string
                                (cljfmt.core/reformat-string 
                                 (merge {:indent-line-comments? true}
                                        (when (map? cljfmt-options)
                                          cljfmt-options))))]

     (println updated-content)

     ;; Write updated content to file
     #_(spit source-path updated-content)

     ;; Print report
     (do (println "\n---- Docstring Update Report ----\n")
         (println (str "File: " source-path "\n"))
         (println (str "Functions updated: " (count (:updated stats)) "\n"))
         (when (seq (:updated stats))
           (println "Updated functions:")
           (doseq [fn-name (:updated stats)]
             (println (str "  - " fn-name))))
         (println "\n---------------------------------\n"))

     {:source-path       source-path
      :updated-functions (:updated stats)})))

"quickdocstring

A utility to generate docstrings from function metadata maps

Why?

Function metadata maps are just maps, they are easy to read and structurally edit.
Manually formatting structured data or markdown within Clojure docstrings is not fun.


How?

Just give the function you want to document a metadata map. paradox looks for
the following entries:

```Clojure 
(defn maths
 {:desc     \"Does math\"
  :examples [{:desc    \"Add two numbers\"
              :samples '[(maths {:op + :print-result true} 2 3)]}
             {:desc    \"Subtract numbers, no printing\"
              :samples '[(maths {:op -} 8 4 2)]}]
  :options  [:map
             {:desc \"All the options\"}
             [:op {:desc \"The arithematic operation to use\"}
              :function]
             [:print-result
              {:desc     \"Print the result to standard out.\"
               :optional true
               :default  false}
              :boolean]]
 [{:keys [op print-result]} & args]
 (let [result (apply op args)]
   (when print-result (println result))
   result)

```
 
docsgen will format the docstring from your metadata and use rewrite-clj to 
add the docsstring your function:
             

```Clojure 
(defn maths
 \"Does math.

  Add two numbers, print result:
  `(maths {:op + :print-result true} 2 3)`

  Subtract numbers, no printing:
  `(maths {:op -} 8 4 2)`
 
  All the options:
  
  * `:op`
      - `:function`
      - Required.
      - The arithematic operation to use

  * `:print-result`
      - `:boolean`
      - Optional.
      - Default: `false`
      - Print the result to stdout.\"
 {:desc     \"Does math\"
  :examples [{:id      :add
              :desc    \"Add two numbers, print result.\"
              :samples '[(maths {:op + :print-result true} 2 3)]}
             {:id      :subtract
              :desc    \"Subtract numbers, no printing\"
              :samples '[(maths {:op -} 8 4 2)]}]
  :options  [:map
             {:desc \"All the options\"}
             [:op {:desc \"The arithematic operation to use\"}
              :function]
             [:print-result
              {:desc     \"Print the result to stdout.\"
               :optional true
               :default  false}
              :boolean]]
 [{:keys [op print-result]} & args]
 (let [result (apply op args)]
   (when print-result (println result))
   result)

```

You can also provide a :paradox entry, which provides a custom template:

```Clojure
[:desc
 [:examples :add]
 :options]              
```
In the above example, we left out the example with the `:id` of `:subtract`:
"
