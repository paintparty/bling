(ns bling.hifi
  (:require [fireworks.core]
            [clojure.string :as string]
            [bling.ansi]
            #?(:cljs [bling.browser :as browser])
            #?(:cljs [bling.js-env :refer [node?]])
            ))

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
