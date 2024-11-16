(ns bling.sample
  (:require 
   [clojure.string :as string]
   #?(:cljs
      [bling.core :refer [bling callout print-bling point-of-interest]]
      :clj
      [bling.core :refer [bling callout point-of-interest]])))

(def ^:private colors-ordered
  ["system-black"   
   "system-maroon"  
   "system-green"   
   "system-olive"   
   "system-navy"    
   "system-purple"  
   "system-teal"    
   "system-silver"  
   "system-grey"    
   "system-red"     
   "system-lime"     
   "system-yellow"   
   "system-blue"     
   "system-fuchsia"  
   "system-aqua"     
   "system-white"   
   "red"
   "orange"
   "yellow"
   "olive"
   "green"
   "blue"
   "purple"
   "magenta"
   "gray"
   "black"
   "white"])

(def ^:private printer
  #?(:cljs print-bling :clj println))

(defn example-custom-callout
  [{:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header (str "This is not a real warning"
                                          "\n"
                                          "Your header message goes here.")
                             :body   (str "The body of your template goes here."
                                          "\n"
                                          "Second line of copy."
                                          "\n"
                                          "Another line.")}
                            point-of-interest-opts)
        message      (point-of-interest poi-opts)
        callout-opts (merge callout-opts
                            {:padding-top 1})]
    (callout callout-opts message)))

(defn callout+ [{callout-type :type
                 callout-label :label
                :as m}]
  (callout m
           (str "Callout with :type of "
                callout-type
                (when callout-label " and custom :label")
                (when (contains? #{:warning "warning" :error "error"}
                                 callout-type)
                  (str "\n"
                       "This is not a real "
                       (name callout-type))) )))

(defn sample []
  ;; CALLOUT examples with default border -------------------------------------
  (callout+ {:type :info})
  (callout+ {:type  :info :label "My custom label"})
  (callout+ {:type :warning})
  (callout+ {:type :error})
  (callout+ {:type  :positive :label "SUCCESS!"})
  
  (println)
  ;; CALLOUT examples with medium border --------------------------------------
  (callout+ {:type :info :border-weight :medium})
  (callout+ {:type :warning :border-weight :medium})
  (callout+ {:type :error :border-weight :medium})
  (callout+ {:type          :positive
             :label         "SUCCESS!"
             :border-weight :medium})

  (println)
  ;; CALLOUT examples with heavy border ---------------------------------------
  (callout+ {:type          :info :border-weight :heavy})
  (callout+ {:type :warning :border-weight :heavy})
  (callout+ {:type :error :border-weight :heavy})
  (callout+ {:type          :positive
             :label         "SUCCESS!"
             :border-weight :heavy})
  
  (println)
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :warning}
    :callout-opts           {:type :warning}})
  

  ;; Combo styles ------------------------------------------------------------
  (println)
  (printer (bling [:bold.italic "bold & italic"]
                  "\n"
                  [:italic.blue "italic & colored"]
                  "\n"
                  [:bold.italic.white.blue-bg
                   "bold & italic & colored & colored-bg"]
                  "\n"
                  [:bold.italic.blue.underline
                   "bold & italic & colored & underline"]
                  "\n"
                  [:bold.italic.blue.strikethrough 
                   "bold & italic & colored & strikethrough"]))

  
  ;; All the colors ----------------------------------------------------------
  (println)
  (let [colors   (filter #(not (re-find #"^system-" %)) colors-ordered)
        max-char (apply max (map count colors))]
    (doseq [s     colors
            :when (not (re-find #"^system-" s))]
      (let [spaces (string/join (repeat (- max-char (count s)) " "))
            f      (fn lab
                     ([m] (lab m s))
                     ([m s] [(merge {:color s} m) s]))]
        (printer (bling (f {:background-color s
                            :color            :white
                            :font-weight      :bold}
                           (str " " s " " ))
                        spaces

                        " "
                        (f {:font-weight :bold})
                        " "
                        spaces

                        " "
                        (f {})
                        " "
                        spaces

                        " "
                        (f {:text-decoration :strikethrough})
                        " "
                        spaces

                        " "
                        (f {:text-decoration :underline})
                        " "
                        spaces

                        " "
                        (f {:font-style :italic})
                        " "
                        spaces))))))

