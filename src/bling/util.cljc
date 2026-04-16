(ns bling.util
  (:require [clojure.string :as string]
            #?(:clj [clojure.java.shell :as shell])))

(defn ^:public when->
  "If `(= (pred x) true)`, returns x, otherwise nil.
   Useful in a `clojure.core/some->` threading form."
  [x pred]
  (when (or (true? (pred x))
            (when (set? pred) (contains? pred x)))
    x))

(defn ^:public when->>
  "If (= (pred x) true), returns x, otherwise nil.
   Useful in a `clojure.core/some->>` threading form."
  [pred x]
  (when (or (true? (pred x))
            (when (set? pred) (contains? pred x)))
    x))

(defn ^:public maybe->
  "If `(= (pred x) true)`, returns x, otherwise nil.
   Useful in a `clojure.core/some->` threading form."
  [x pred]
  (when (or (true? (pred x))
            (when (set? pred) (contains? pred x)))
    x))

(defn ^:public maybe->>
  "If (= (pred x) true), returns x, otherwise nil.
   Useful in a `clojure.core/some->>` threading form."
  [pred x]
  (when (or (true? (pred x))
            (when (set? pred) (contains? pred x)))
    x))

(defn as-str [x]
  (str (if (or (keyword? x) (symbol? x)) (name x) x)))

(defn- regex? [v]
  #?(:clj  (-> v type str (= "class java.util.regex.Pattern"))
     :cljs (-> v type str (= "#object[RegExp]"))))

(defn- surround-with-quotes [x]
  (str "\"" x "\""))

(defn shortened
  "Stringifies a value and truncates the result with ellipsis 
   so that it fits on one line."
  [v limit]
  (let [as-str         (str v)
        regex?         (regex? v)
        double-quotes? (or (string? v) regex?)
        regex-pound    #?(:cljs nil :clj (when regex? "#"))]
    (if (> limit (count as-str))
      (if double-quotes?
        (str regex-pound (surround-with-quotes as-str))
        as-str)
      (let [ret* (-> as-str
                     (string/split #"\n")
                     first)
            ret  (if (< limit (count ret*))
                   (let [ret (->> ret*
                                  (take limit)
                                  string/join)]
                     (str (if double-quotes?
                            (str regex-pound (surround-with-quotes ret))
                            ret)
                          (when-not double-quotes? " ")
                          "..."))
                   ret*)]
        ret))))

(defn sjr [n s] (string/join (repeat n s)))

(defn concatv
  "Concatenate `xs` and return the result as a vector."
  [& xs]
  (into [] cat xs))

(defn get-terminal-width []
  #?(:js
     80
     ;; TODO - Test this
     ;;  (when (exists? js/process) 
     ;;    (let [env            (some-> js/process .-env)
     ;;          env            (when-not (nil? env) env)
     ;;          stdout         (some-> js/process .-stdout)
     ;;          stdout         (when-not (nil? stdout) stdout)
     ;;          stderr         (some-> js/process .-stderr)
     ;;          stderr         (when-not (nil? stderr) stderr)
     ;;          stdout-columns (.-columns stdout)
     ;;          stderr-columns (.-columns stderr)
     ;;          env-columns    (.-columns env)
     ;;          fallback       80
     ;;          ret            (cond (pos-int? stdout-columns)
     ;;                               stdout-columns
     ;;                               (pos-int? stderr-columns)
     ;;                               env-columns
     ;;                               (js/parseInt env-columns 10)
     ;;                               :else
     ;;                               fallback)]
     ;;      (if (pos-int? ret) ret fallback)))

     :clj
     (try
       (let [{:keys [out exit]} (shell/sh "sh" "-c" "stty size </dev/tty")]
         (if (zero? exit)
           (let [[_ cols] (-> out
                              clojure.string/trim
                              (clojure.string/split #" "))]
             (Integer/parseInt cols))
           80)) ; fallback
       (catch Exception e
         80) ; fallback for error
       )))


(defn join-lines
  ([coll]
   (join-lines "\n" coll))
  ([sep coll]
   (string/join sep coll)))


(defn partition-by-pred [pred coll]
  "Given a coll and a pred, returns a vector of two vectors. The first vector
   contains all the values from coll that satisfy the pred. The second vector
   contains all the values from the coll that do not satisfy the pred."
  (let [ret* (reduce (fn [acc v]
                       (let [k (if (pred v) :valid :invalid)]
                         (assoc acc k (conj (k acc) v))))
                     {:valid [] :invalid []}
                     coll)]
    [(:valid ret*) (:invalid ret*)]))

(defn char-repeat [n s]
  (when (pos-int? n)
    (string/join (repeat n (or s "")))))

(defn string-of-1? [x]
  (and (string? x) (= 1 (count x))))

(defn insert-at [vc i elem]
  (into (conj (subvec vc 0 i) elem)
        (subvec vc i)))

(defn flatten-map-keys [m]
  (persistent!
   (reduce-kv (fn [acc k v]
                (if (coll? k)
                  (reduce #(assoc! %1 %2 v) acc k)
                  (assoc! acc k v)))
              (transient {})
              m)))



;; Emoji utils -----------------------------------------------------------------


#?(:clj
   (def emoji-re
     ;; Clojure / Babashka
     ;; Using explicit hex ranges prevents PatternSyntaxExceptions on older JDKs.
     (let [flags "(?:[\\x{1F1E6}-\\x{1F1FF}]{2})"                 ;; Regional Indicators (Flags)
           base  "[\\x{2300}-\\x{23FF}\\x{2600}-\\x{27BF}\\x{1F300}-\\x{1FAFF}]" ;; Core Emojis, Symbols, Dingbats
           vs16  "[\\x{FE0F}]?"                                   ;; Variation Selector 16
           mods  "[\\x{1F3FB}-\\x{1F3FF}]?"                       ;; Skin tones
           zwj   "\\x{200D}"                                      ;; Zero Width Joiner
           cluster (str base vs16 mods "(?:" zwj base vs16 mods ")*")]
       (re-pattern (str flags "|" cluster))))

   :cljs
   (def emoji-re
     ;; ClojureScript, js regex
     (let [flags "(?:[\\u{1F1E6}-\\u{1F1FF}]{2})"
           base  "[\\u{2300}-\\u{23FF}\\u{2600}-\\u{27BF}\\u{1F300}-\\u{1FAFF}]"
           vs16  "[\\uFE0F]?"
           mods  "[\\u{1F3FB}-\\u{1F3FF}]?"
           zwj   "\\u200D"
           cluster (str base vs16 mods "(?:" zwj base vs16 mods ")*")]
       (js/RegExp. (str flags "|" cluster) "ug"))))


(defn contains-emoji?
  "Checks if a string contains at least one emoji or emoji cluster."
  [s]
  (boolean (re-find emoji-re (str s))))

(defn extract-emojis
  "Returns a sequence of all emojis found in the string.
   Properly groups ZWJ clusters (👨‍👩‍👧), skin tones (👍🏽), and flags (🇺🇸)."
  [s]
  #?(:clj  (re-seq emoji-re (str s))
     ;; In CLJS, re-seq mapped with first works cleanly over the "ug" RegExp
     :cljs (map first (re-seq emoji-re (str s)))))

(defn remove-emojis
  "Strips all emojis and complex grapheme clusters from the string."
  [s]
  (string/replace (str s) emoji-re ""))
