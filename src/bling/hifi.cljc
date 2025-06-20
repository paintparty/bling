(ns bling.hifi
  (:require [fireworks.core]
            #?(:cljs [bling.core :refer [Enriched]])
            #?(:cljs [bling.util :as util])
            #?(:cljs [bling.js-env :refer [node?]])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Hi-Fidelity printing 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 

(defn hifi-impl [x user-opts]
  (let [m (->> x 
               (fireworks.core/_p2
                (merge user-opts
                       {:user-opts user-opts
                        :mode      :data
                        :p-data?   true
                        :template  [:result]})))]
    #?(:cljs
       (if node?
         (-> m :formatted :string)
         (let [tagged (-> m :formatted :string)
               css    (-> m :formatted :css-styles)]
           (Enriched. tagged                                   ;; tagged
                      (into-array css)                         ;; css
                      (into-array (util/concatv [tagged] css)) ;; consoleArray
                      nil)))
       :clj
       (-> m :formatted :string))))

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

   #?(:cljs
      (if node?
        (hifi-impl x opts)
        (let [{:keys [string css-styles]} (hifi-impl x opts)
              js-arr (into-array (concat [string] css-styles ))]
          (.apply (.-log  js/console)
                  js/console
                  js-arr)))
      :clj
      (println (hifi-impl x opts)))))

