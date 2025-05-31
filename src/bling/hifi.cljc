(ns bling.hifi
  (:require [fireworks.core]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Hi-Fidelity printing 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 

(defn hifi-impl [x user-opts]
  (->> x 
       (fireworks.core/_p2 (merge user-opts
                                  {:user-opts user-opts
                                   :mode      :data
                                   :p-data?   true
                                   :template  [:result]}))
       :formatted
       :string))

(defn ^:public hifi
  "Hi-fidelity, pretty-printed string with syntax-coloring. Dispatches to
   fireworks.core/_p2"
  ([x]
   (hifi x nil))
  ([x opts]
  (hifi-impl x opts)))


(defn ^:public print-hifi
  "Prints a hi-fidelity, pretty-printed string with syntax-coloring. Dispatches
   to fireworks.core/_p2. Sugar for (println (bling.core/hifi x))"
  ([x]
   (print-hifi x nil))
  ([x opts]
   (println (hifi-impl x opts))))

