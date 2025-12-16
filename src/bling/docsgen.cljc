;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public function metadata-map -> docstring
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns bling.docsgen
  (:require [fireworks.core]
            [fireworks.core :refer [? !? ?> !?>]]
            [clojure.string :as string]
            #?(:clj [rewrite-clj.zip :as z])
            #?(:clj [rewrite-clj.node :as n])
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


(defn vec-of-strings?
  "Returns true if x is a vector where every element is a string."
  [x]
  (and (vector? x) (every? string? x)))


(defn docsgen-section
  [{:keys [desc examples options]} acc x]
  (cond
    (= x :desc)
    (conj acc
          (cond (vec-of-strings? desc)
                (string/replace 
                 (join-lines " " 
                             (mapv #(string/replace % #"\"" "\\\\\"") 
                                   desc))
                 #"\n ([^\s])" 
                 #(str "\n" (second %)))
                (string? (string/replace desc #"\"" "\\\""))
                desc))

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
                           (or (conj vc 
                                     (some->> example 
                                                 :id 
                                                 (vector :examples)))
                               vc))
                         []))]
    (vec (remove nil? (concat [:desc] examples [:options])))))


(defn format-malli-options-schema-for-docstring 
  "Experimental utility for repl usage, intended for turning existing malli 
   schemas for options maps into codeblocks that live in a docstring.
   
   Resulting docstrings and optimized for consumption by
   [quickdoc](https://github.com/borkdude/quickdoc)."
  [m]
  (let [template (or (:docsgen m)
                     (docsgen-template m))
        sections (reduce (partial docsgen-section m) [] template)]
    (-> (str "\"" (string/join "\n\n" sections) "\"")
        (string/split #"\n")
        (->> (map-indexed (fn [i x] (str (if (zero? i) "  " "   ") x)))
             join-lines)
        (string/replace #"(?:\n +)+\"$" "\"" ))))


#?(:clj
   (do

     (defn create-doc-string
       "Capitalizes the first letter of a string."
       [s]
       (string/capitalize s))

     (defn has-desc-metadata?
       "Checks if a node has metadata with a :desc key."
       [zloc]
       (when-let [meta-node (z/down (z/right (z/right zloc)))]
         (and (z/map? meta-node)
              (loop [m-loc (z/down meta-node)]
                (cond
                  (nil? m-loc) false
                  (and (n/keyword-node? m-loc)
                       (= :desc (z/sexpr m-loc))) true
                  :else (recur (z/right (z/right m-loc))))))))

     (defn extract-desc-value
       "Extracts the value of :desc from metadata map."
       [zloc]
       (when-let [meta-node (z/down (z/right (z/right zloc)))]
         (loop [m-loc (z/down meta-node)]
           (when m-loc
             (if (and (n/keyword-node? m-loc)
                      (= :desc (z/sexpr m-loc)))
               (z/sexpr (z/right m-loc))
               (recur (z/right (z/right m-loc))))))))

     (defn insert-docstring
       "Inserts a docstring between function name and metadata."
       [zloc docstring]
       (-> zloc
           (z/right)                    ; move to fn name
           (z/right)                    ; move to metadata or params
           (z/insert-left docstring)    ; insert docstring before metadata
           (z/insert-left (n/newline-node)) ; add newline for formatting
           (z/up)))                     ; go back to defn level
     
     (defn process-defn
       "Processes a single defn form, adding docstring if it has :desc metadata."
       [zloc stats]
       (if (and (z/list? zloc)
                (= 'defn (z/sexpr (z/down zloc))))
         (let [fn-name (z/sexpr (z/down (z/right (z/down zloc))))]
           (if (has-desc-metadata? (z/down zloc))
             (let [desc-value   (extract-desc-value (z/down zloc))
                   docstring    (create-doc-string desc-value)
                   updated-zloc (insert-docstring (z/down zloc) docstring)]
               [updated-zloc (update stats :updated conj fn-name)])
             [zloc stats]))
         [zloc stats]))

     (defn update-file-docstrings
       "Main function to process a source file and add docstrings from :desc metadata."
       [source-path]
       (let [zloc               (z/of-file source-path)
             [final-zloc stats] (loop [loc   zloc
                                       stats {:updated []}]
                                  (if (z/end? loc)
                                    [loc stats]
                                    (let [[new-loc new-stats] (process-defn loc stats)]
                                      (recur (z/next new-loc) new-stats))))
             updated-content    (z/root-string final-zloc)]
         
    ;; Write updated content to file
         (spit source-path updated-content)
         
    ;; Print report
         (println "\n=== Docstring Update Report ===")
         (println (str "File: " source-path))
         (println (str "Functions updated: " (count (:updated stats))))
         (when (seq (:updated stats))
           (println "Updated functions:")
           (doseq [fn-name (:updated stats)]
             (println (str "  - " fn-name))))
         (println "==============================\n")
         
         stats))
     
     ))
