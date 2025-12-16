;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public function metadata-map -> docstring
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns bling.docsgen
  (:require [fireworks.core]
            ;; [fireworks.core :refer [? !? ?> !?>]]
            [clojure.string :as string]
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
  (->> options
       rest
       (reduce (fn [s [option m pred]]
                 (str s 
                      "* " (md-code option)
                      "\n"
                      (join-lines (sublines m pred))
                      "\n"))
               "")))


(defn- example->md [example]
  (let [{:keys [desc samples]} example]
    (str desc
         "\n\n"
         "```clojure\n"
         (join-lines (mapv #(str (docstring-quoted %)) samples))
         "\n```")))


(defn docsgen-section
  [{:keys [desc examples options]} acc x]
  (cond
    (= x :desc)
    (conj acc (join-lines "\n\n" desc))

    (and (vector? x) (= (first x) :examples))
    (if-let [ex (some->> examples
                         (filter #(-> x second (= (:id %))))
                         first
                         example->md)]
      (conj acc ex)
      acc)

    (= x :options)
    (if (seq options)
      (conj acc "All the options:" (options->md options))
      acc)))


(defn format-malli-options-schema-for-docstring 
  "Experimental utility for repl usage, intended for turning existing malli 
   schemas for options maps into codeblocks that live in a docstring."
  [m]
  (let [sections (reduce (partial docsgen-section m) [] (:docsgen m))]
    (-> (str "\"" (string/join "\n\n" sections) "\"")
        (string/split #"\n")
        (->> (map-indexed (fn [i x] (str (if (zero? i) "  " "   ") x)))
             join-lines))))
