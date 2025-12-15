(ns bling.hifi
  (:require [fireworks.core]
            [fireworks.core :refer [? !? ?> !?>]]
            [clojure.string :as string]
            [bling.ansi]
            #?(:cljs [bling.browser :as browser])
            #?(:cljs [bling.js-env :refer [node?]])
            
            [clojure.walk :as walk]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Hi-Fidelity printing 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:public chopped
  "Truncates lines in a formatted paragraph at supplied max-width, adding
   ellipsis. Accounts for ansi sgr tags."
  [s max-width]
  (->> (string/split s #"\n")
      (mapv #(let [sgr-count (bling.ansi/sgr-count %)]
              ;;  (println)
              ;;  (prn %)
              ;;  (println [(count %) sgr-count (- (count %) sgr-count)])
               (if (> (- (count %) sgr-count) max-width)
                 (str (subs % 0 (- max-width 3)) "...")
                 %)))
      (string/join "\n")))

(defn- hifi-impl [x user-opts]
  (let [ret  
        (->> x 
             (fireworks.core/_p2
              (merge user-opts
                     {:user-opts user-opts
                      :mode      :data
                      :p-data?   true
                      :template  [:result]}))
             :formatted
             :string)]
    (or (some->> user-opts
                 :chop-lines-at-max-width
                 (chopped ret))
        ret)))


(defn ^:public hifi
  "Hi-fidelity, pretty-printed string with syntax-coloring. Dispatches to
   fireworks.core/_p2"
  ([x]
   (hifi x nil))
  ([x opts]
  (hifi-impl x opts)))


(defn ^:public print-hifi
  "Prints a structurally formatted value with syntax-coloring based on user's
   preferred theme. Dispatches to fireworks.core/_p2.
   
   In JVM Clojure, cljs(Node), and bb, `print-hifi` is sugar for:
   `(println (bling.core/hifi x))`

   In cljs (browser dev consoles), `print-hifi` is sugar for the the following:
   `(->> (bling.core/hifi x)
         bling.browser/ansi-sgr-string->browser-dev-console-array
         (.apply js/console.log js/console))`"
  ([x]
   (print-hifi x nil))
  ([x opts]

   #?(:cljs
      (if node?
        (hifi-impl x opts)
        (->> (hifi-impl x opts)
             browser/ansi-sgr-string->browser-dev-console-array
             (.apply js/console.log js/console)))
      :clj
      (println (hifi-impl x opts)))))

#_(defn format-malli-options-schema-for-docstring 
  "Experimental utility for repl usage, intended for turning existing malli 
   schemas for options maps into codeblocks that live in a docstring."
  [docstring vc]
  (str docstring
       "\n\n"
       "   All the options:"
       "\n\n   ```Clojure\n"
       (->> vc
            rest
            (reduce (fn [s opt-vc]
                      (str s
                           (string/replace
                            (hifi opt-vc
                                  {:theme               "Universal Neutral"
                                   :truncate?           false
                                   :margin-inline-start 3})
                            #"\""
                            "\\\\\"")
                           "\n\n"))
              ""))
       "   ```"))

(defn docstring-quoted [x]
  (cond (string? x)
        (symbol (str "\\\"" x "\\\""))
        (coll? x)
        (walk/postwalk 
         (fn [x] 
           (let [ret (if (string? x)
                       (symbol (str "\\\"" x "\\\""))
                       x)]
             ret))
         x)
        :else
        x))

(defn- format-pred [pred]
  (cond (keyword? pred)
        (-> pred name (str "?") symbol) 
        (and (vector? pred) (= (first pred) :enum))
        #_(str "#(malli.core/validate " pred " %)")
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
  (remove nil?
          [(subline (md-code (format-pred pred)))
           (subline (if (= optional true) "Optional." "Required."))
           (when default
             (subline (str "Defaults to " default ".")))
           (subline (if (vector? desc) (string/join " " desc) desc))]))

(defn- join-lines
  ([coll]
   (join-lines "\n" coll))
  ([sep coll]
   (string/join sep coll)))

(defn format-malli-options-schema-for-docstring 
  "Experimental utility for repl usage, intended for turning existing malli 
   schemas for options maps into codeblocks that live in a docstring."
  [{:keys [desc examples options docsgen] :as m}]
  (str (join-lines "\n\n" desc)
       "\n\n"
       (let [{:keys [desc samples]}
             (->> examples
                  (filter #(-> % :id (= :gradients)))
                  first)]
         (str desc
              "\n\n"
              (string/join "\n" (mapv #(str "`" % "`") samples))
              ))
       "\n\n"
       "All the options:"
       "\n\n"
       (->> options
            rest
            (reduce (fn [s [option m pred]]
                      (str s 
                           "* " (md-code option)
                           "\n"
                           (string/join "\n" (sublines m pred))
                           "\n"))
                    ""))))
