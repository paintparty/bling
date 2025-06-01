(ns bling.macros
  (:require  
   [clojure.string :as string]
   [clojure.pprint :refer [pprint]]
   [clojure.edn :as edn]))

(defn- regex? [v]
  (-> v type str (= "class java.util.regex.Pattern")))

(defn- surround-with-quotes [x]
  (str "\"" x "\""))

(defn shortened
  "Stringifies a collection and truncates the result with ellipsis 
   so that it fits on one line."
  [v limit]
  (let [as-str         (str v)
        regex?         (regex? v)
        double-quotes? (or (string? v) regex?)
        regex-pound    (when regex? "#")]
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

(defmacro let-map
  "Equivalent of
   (let [a 5
         b (+ a 5)]
     {:a a :b b})"
  [kvs]
  (let [keys (keys (apply hash-map kvs))
        keyword-symbols (mapcat #(vector (keyword (str %)) %) keys)]
    `(let [~@kvs]
       (hash-map ~@keyword-symbols))))


(let [transforms {:keys keyword
                  :strs str
                  :syms identity}]
  (defmacro keyed
    "Create a map in which, for each symbol S in vars, (keyword S) is a
     key mapping to the value of S in the current scope. If passed an optional
     :strs or :syms first argument, use strings or symbols as the keys."
    ([vars] `(keyed :keys ~vars))
    ([key-type vars]
     (let [transform (comp (partial list `quote)
                           (transforms key-type))]
       (into {} (map (juxt transform identity) vars))))))


;; macro for debugging bling
(defn- ns+ln+col-str
  [form-meta]
  (let [{:keys [line column]} form-meta
        ns-str                (some-> *ns*
                                      ns-name
                                      str
                                      (str ":" line ":" column))
        ns-str                (do 
                                (str "\033[3;38;5;247m" ns-str "\033[0;m")

                                  ;; magenta bold
                                (str "\033[3;38;5;201;1m" ns-str "\033[0m")

                                  ;; olive bold
                                (str "\033[3;38;5;69;m" ns-str "\033[0m")
                                
                                )]
    ns-str))


(defn- ns+fn-str
  [form-meta]
  (let [{:keys [line column]} form-meta
        ns-str                (some-> *ns*
                                      ns-name
                                      str)
        ;; ns-str             (do 
        ;;                      (str "\033[3;38;5;247m" ns-str "\033[0;m")
        ;;                      ;; magenta
        ;;                      #_(str "\033[3;38;5;201;1m" ns-str "\033[0m"))
        ]
    ns-str))
  
  
(defmacro stop-dbg! [x]
  `(when ~x 
     (println 
      (str "\033[3;38;5;166;m"
           "-------------------------------------------------------------------"
           "\033[0m"
           "\n"))))


(defmacro start-dbg!
  ([]
   nil
   #_(let [ns-str (ns-str (meta &form))]
       `(println (str ~ns-str ))))
  ([x]
   (when x
     (let [label  (when-not (boolean? x) x)
           ns-str (ns+fn-str (meta &form))]
       `(do
          (println
           (str "\033[3;38;5;166;m" 
                (str 
                 "\n-----------------------------------------------------------"
                 "\n"
                 ~ns-str
                 "/"
                 (-> ~x var meta :name)
                 ":"
                 (-> ~x var meta :line)
                 ":"
                 (-> ~x var meta :column)
                 " -- "
                 "debugging"
                 "\n"
                 "-------------------------------------------------------------"
                 "\n")
                "\033[0m"))
          true))))) 

(defmacro nth-not-found
  ([]
   (let [ns-str (ns+ln+col-str (meta &form))]
     `(do
        #_(println
           (str ~ns-str " " :nth-not-found))
        nil))))
  
(defmacro ? 
  ([x]
   (let [ns-str (ns+ln+col-str (meta &form))]
     `(do
        (println
         (str ~ns-str
              "\n"
              (shortened (quote ~x) 25)
              "\n"
              (with-out-str (pprint ~x))))
        ~x)))
  ([label x]
   (let [label  (or (:label label) label)
         ns-str (ns+ln+col-str (meta &form))]
      ;;  (println "FOOO" ns-str)
     `(do
        (println
         (if (= :- ~label)
           (with-out-str (pprint ~x))
           #_(string/replace (with-out-str (pprint ~x)) #"\n$" "")
           (str ~ns-str
                "\n"
                ~label
                "\n"
                (with-out-str (pprint ~x)))))
        ~x))))


(defn interleave-all
  "Returns a lazy seq of the first item in each coll, then the second, etc.
  Unlike `clojure.core/interleave`, the returned seq contains all items in the
  supplied collections, even if the collections are different sizes."
  {:arglists '([& colls])}
  ([] ())
  ([c1] (lazy-seq c1))
  ([c1 c2]
   (lazy-seq
    (let [s1 (seq c1), s2 (seq c2)]
      (if (and s1 s2)
        (cons (first s1) (cons (first s2) (interleave-all (rest s1) (rest s2))))
        (or s1 s2)))))
  ([c1 c2 & colls]
   (lazy-seq
    (let [ss (keep seq (conj colls c2 c1))]
      (when (seq ss)
        (concat (map first ss) (apply interleave-all (map rest ss))))))))


(defn- vars->syms [x]
  (if-let [[_ sexp] (and (string? x)
                         (re-find #"^\$\{(.*)\}" x))]
    (if (string/starts-with? sexp "(")
      (edn/read-string sexp)
      (symbol sexp))
    x))


(defmacro blingf [s]
  (let [s      (string/replace s #"^\n" "")
        re     #"\[[^\]]*\](?:\.[a-z]+)+|\$\{[^\}]*\}"
        splits (string/split s re)]
    (->> s
         (re-seq re)
         (map 
          #(if-let [[_ blinged*] (re-find #"^\[(.*)\]" %)]
             (let [[_ bits] (re-find #"\[.*\]((?:\.[a-z]+)+)" %)
                   blinged  (vars->syms blinged*)]
               [(-> bits (subs 1) keyword)
                blinged])
             (vars->syms %)))
         (interleave-all splits)
         (cons 'bling.core/bling))))


;; v1
"Error when shelling out to lightningcss.

CSS`italic.subtle.bold`
${css-str}

Flags passed to lightningcss:`italic.subtle.bold`
${(with-out-str (fireworks.core/pprint flags))}

The following css will be returned:`italic.subtle.bold` 
css-str"


;; v2
"Error when shelling out to lightningcss.
 
 CSS:<-italic.subtle.bold
 ${css-str}
 
 Flags passed to lightningcss:<-italic.subtle.bold
 ${(with-out-str (fireworks.core/pprint flags))}
 
 The following css will be returned:<-italic.subtle.bold
 ${css-str}"

(defmacro bling-mood-env-var [] 
  (System/getenv "BLING_MOOD"))

(defmacro no-color-env-var [] 
  (System/getenv "NO_COLOR"))

(defmacro force-color-env-var [] 
  (System/getenv "FORCE_COLOR"))
