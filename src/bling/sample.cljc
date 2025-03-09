(ns bling.sample
  (:require 
   [clojure.string :as string]
   [clojure.pprint :as pprint]
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
  (let [poi-opts     (merge {:header (str "This is not a real error or warning"
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

(defn callout+
  [{callout-type  :type
    callout-colorway  :colorway
    callout-label :label
    :as           m}]
  (let [body (str "Callout with "
                  (if callout-type "type" "colorway")
                  " of "
                  (or callout-type callout-colorway)
                  (when callout-label " and custom :label")
                  (when (contains? #{:warning "warning" :error "error"}
                                   callout-type)
                    (str "\n"
                         "This is not a real "
                         (name callout-type))))
        k :italic.subtle]
    (println 
     (str
      "\n\n\n"
      (bling [k ";; (callout "])
      (string/join
       (bling [k "\n;; "]) 
       (map-indexed (fn [i s]
                      (bling [k
                              (str (if (zero? i)
                                     ""
                                     "         ")
                                   s)])) 
                    (string/split (string/replace 
                                   (with-out-str (pprint/pprint m))
                                   #"\n$"
                                   "")
                                  #",")))
      "\n"
      (bling [k (str ";;          \"" (string/replace body #"\n" "\\\\n"))])
      #_(string/join
       (bling [k "\\n\n"]) 
       (map-indexed (fn [i s]
                      (bling [k
                              (str "         "
                                   (when (zero? i) "\"")
                                   s)])) 
                    (string/split body #"\n")))
      (bling [k "\""])
      (bling [k "\n;; =>\n"])))

    (callout (assoc m :margin-top 0 :margin-bottom 0)
             body)))

;; (defn sample []
;;   (callout (assoc {:type        :error
;;                    :theme       :gutter
;;                    :margin-left 3}
;;                   :margin-top 0
;;                   :margin-bottom 0
;;                   :padding-top 1
;;                   ;; :padding-bottom 1
;;                   )
;;            (str "afadsfsdfsadf\nasfdsadfasdf" "as")))
(defn print-comment [s]
  (println (bling [:italic.subtle s])))

(defn sample []
  (println)
  (print-comment ";; Below are some samples using bling.core/callout")
  (print-comment ";; https://github.com/paintparty/bling")

  (println)
  (print-comment ";; You need to require the following things to make this work:")
  (print-comment ";; (require '[bling.core :refer [bling callout point-of-interest]])")

  ;; CALLOUT examples with default :sideline theme -------------------------------------
  (callout+ {:type :info})
  (callout+ {:type  :info :label "My custom label"})
  (callout+ {:type :warning})
  (callout+ {:type :error})
  (callout+ {:colorway :positive :label "SUCCESS!"})
  
  (println)
  ;; CALLOUT examples with :sideline-bold theme--------------------------------------
  (callout+ {:type :info :theme :sideline-bold })
  (callout+ {:type :warning :theme :sideline-bold})
  (callout+ {:type :error :theme :sideline-bold})
  (callout+ {:colorway          :positive
             :label         "SUCCESS!"
             :theme :sideline-bold})

  (println)
  ;; CALLOUT examples with :gutter theme ---------------------------------------
  (callout+ {:type          :info :theme :gutter})
  (callout+ {:type :warning :theme :gutter})
  (callout+ {:type :error :theme :gutter})
  (callout+ {:colorway          :positive
             :label         "SUCCESS!"
             :theme :gutter})

  (println)
  ;; CALLOUT examples with :gutter theme ---------------------------------------
  (callout+ {:type :info :theme :gutter :margin-left 3})
  (callout+ {:type :warning :theme :gutter :margin-left 3})
  (callout+ {:type :error :theme :gutter :margin-left 3})
  (callout+ {:colorway    :positive
             :label       "SUCCESS!"
             :theme       :gutter
             :margin-left 3})
  

  (println)
  (println)
  (println)
  (print-comment
     ";; Below is an example of a custom error template with")
  (print-comment
     ";; a point-of-interest diagram. See readme for more details.")
  (example-custom-callout
   {:point-of-interest-opts {:file   "example.ns.core"
                             :line   11
                             :column 1
                             :form   '(+ 1 true)
                             :type   :error}
    :callout-opts           {:type :error}})


  (println)
  (println)
  (println)
  (print-comment
     ";; Below is an example of a custom warning template with")
  (print-comment
     ";; a point-of-interest diagram. See readme for more details.")
  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(myfun foo baz)
                             :type                  :warning
                             :text-decoration-index 2}
    :callout-opts           {:type :warning
                             :label "WARNING: Invalid arg value"}})

  (example-custom-callout
   {:point-of-interest-opts {:file                  "example.ns.core"
                             :line                  11
                             :column                1
                             :form                  '(+ foo baz)
                             :text-decoration-index 2
                             :type                  :error}
    :callout-opts           {:type :error
                             :label "ERROR: ClassCastException"}})


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

