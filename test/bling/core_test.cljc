;; Namespace for visual testing and sandbox dev during development

(ns bling.core-test
  (:require
   #?(:cljs [bling.js-env :refer [node?]])
   ;; [fireworks.core :refer [? !? ?> !?>]] ;; <-just for debugging
   [bling.core :refer [bling print-bling callout point-of-interest bling-colors*]]
   [bling.sample :as sample]
   [bling.fonts]
   [bling.banner]
   [clojure.pprint :refer [pprint]]
   [clojure.string :as string]))

(def printer
  #?(:cljs (if node? println print-bling) :clj println))

(declare example-custom-callout)

(defn print-fake-comment [& lns]
  (println)
  (println)
  (doseq [ln lns]
    (println 
     (bling 
      [:italic.subtle ln]))))
   
(def poi-opts
  {:file                  "example.ns.core"
   :line                  11
   :column                1
   :form                  '(+ 1 true)
   :text-decoration-index 2})

(defn print-seq [sep f coll]
  (printer (apply bling (interpose sep (mapv f coll)))))

(def callout-examples
  [{:type :info
    :s    "Example callout, with :type of :info"}
   {:type  :info
    :label "My custom label"
    :s     "Example callout, with :type of :info and custom :label"}
   {:type :warning
    :s    "Example callout, with :type of :warning"}
   {:type :error
    :s    "Example callout, with :type of :error"}
   {:colorway :positive
    :label    "SUCCESS!"
    :s        "Example callout, with :colorway of :positive, and custom :label"}
   {:colorway        :subtle
    :label           "My label"
    :s               "Example callout, with :colorway of :subtle (or :gray)"
    :docs/secondary? true}
   {:colorway        :magenta
    :label           "My label"
    :s               "Example callout, with :colorway of :magenta"
    :docs/secondary? true}
   {:label           "My label"
    :s               "Example callout, default"
    :docs/secondary? true}])

(defn bling-basics
  ([]
   (bling-basics false))
  ([extras?]

   ;; Hyperlink
   (printer
    (bling [{:text-decoration :underline
             :href            "http://example.com"}
            "cmd (or ctrl) + click on me to follow hypelink"]))

   (println)

   ;; Underline styles
   (print-seq "\n" 
              (fn [s] [(keyword s) s])
              ["bold"
               "italic"
               "strikethrough"
               "underline"
               "double-underline"
               "wavy-underline"
               "dotted-underline"
               "dashed-underline"])
   
   
   (println)

   ;; Colors
   (print-seq " " 
              (fn [s] [(keyword (str "bold." s)) (string/capitalize s)])
              (keys bling-colors*))

   (println)

   ;; Background colors
   (print-seq " " 
              (fn [s] 
                (when-not (contains? #{"black" "white"} s)
                  [(keyword (str "white.bold." s "-bg"))
                   (str " " (string/capitalize s) " ")]))
              (keys bling-colors*))

   
   (println)

   ;; Semantic colors
   (print-seq " " 
              (fn [s] [(keyword (str "bold." s)) (string/capitalize s)])
              ["negative"
               "error"
               "warning"
               "positive"
               "info"
               "subtle"
               "neutral"])
   
   ;; Combos colors
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
   
   

   ;; CALLOUT examples  --------------------------------------------------------
   (println)

   (let [print-callout-examples
         (fn [theme opts]
           (let []
             ;; (print-fake-comment  (str ";; callout examples, {:theme " theme "}"))
             (doseq [{callout-type :type
                      colorway     :colorway
                      label        :label}
                     callout-examples]
               (let [merged-opts
                     (merge {:type        callout-type
                             :label-theme :minimal}
                            opts
                            (when theme {:theme theme})
                            (when label {:label label})
                            (when colorway {:colorway colorway}))

                     example-call
                     (let [a (string/replace (with-out-str (pprint merged-opts))
                                             #"\n$"
                                             "")]
                       (str "(callout\n "
                            (if (re-find #"\n" a)
                              (string/replace a #",\n|, " "\n ")
                              (string/replace a #", " "\n  "))
                            ")"))

                     callout-body
                     (str 
                      "\n"
                      (if colorway
                        (str "Example callout, with :colorway of " colorway)
                        (if callout-type
                          (str "Example callout, with :type of " callout-type)
                          (str "Example callout," " default")))
                      "\n\n"
                      example-call)]
                 (callout merged-opts callout-body)))))]
     
     (print-fake-comment  ";; callout examples, {:label-theme :minimal}")

     (doseq [theme [:sideline :sideline-bold :gutter]]
       (print-callout-examples theme {}))

     (doseq [theme [:gutter]]
       (print-callout-examples theme {:margin-left 2}))

     (doseq [theme [:sideline :sideline-bold]]
       (print-callout-examples theme {:label-theme :marquee})))
   

   ;; TODO make custom error and warning examples ------------------------------
   (print-fake-comment
      ";; Custom error and warning templates with a point-of-interest "
      ";; diagram. Checkout Templates section in readme for more details.")

   (doseq [callout-opts
           [{}
            {:label-theme :marquee}
            {:theme :sideline-bold}
            {:label-theme :marquee
             :theme       :sideline-bold}
            {:theme :gutter}]]
     ;;  :label "WARNING: Invalid arg value"
     (doseq [t [:warning :error]]
       (example-custom-callout
        {:point-of-interest-opts (assoc poi-opts :type t)
         :callout-opts           (assoc callout-opts :type t)})))
   
   (callout
    {:type         :warning
     :theme        :gutter
     :padding-left 4}
    "Example callout, with :type of :warning, padding-left of 4.")))


(defn example-custom-callout
  [{:keys [point-of-interest-opts callout-opts]}]
  (let [poi-opts     (merge {:header "Your header message goes here."
                             :body   (str "The body of your template goes here."
                                          "\n"
                                          "Second line of copy."
                                          "\n"
                                          "Another line.")}
                            point-of-interest-opts)
        message      (point-of-interest poi-opts)
        callout-opts (merge {:padding-top 1}
                            callout-opts)]
    (callout callout-opts message)))


(defn random-callouts []
   (callout {:label "Callout, no body, body is \"\""} "")

   (callout {:theme :gutter :label "Callout, no body, body is \"\""} "")
   
   (callout {:label "Callout, no body"} nil)

   (callout {:theme :gutter :label "Callout, no body, body is nil"} nil)
   
   (callout {:label "Callout, no body, body is not supplied"})

   (callout {:theme :gutter :label "Callout, no body, body is not supplied"})
   
   (callout "Callout, only body")

   (callout (bling [:magenta "Callout, only body"]))

   (callout {:theme :gutter}  "Callout, only body, body is nil")

   (callout {:label "Callout, no body"})

   (callout {:label "Callout, blank body"} "")

   (callout {:label ""})

   (callout "")

   (callout "just body")
   
   (callout "Default callout, no options")

   (callout {:padding-left 2} "Default callout, no label, left padding")

   (callout {:label "My label"} "Default callout, custom label")

   (callout {:type :warning} "Callout, type :warning, default label")

   (callout {:type  :warning
             :label "My warning"}
            "Callout, type :warning, custom label")

   (callout {:type     :warning
             :label-string " My warning "
             :label        (bling [:bold.yellow-bg.black " My warning "])}
            "Callout, type :warning, custom enriched label")

   (callout {:type       :warning
             :padding-top    1
             :padding-bottom 1
             :padding-left   2} 
            "Callout, type :warning, default label, custom padding")

   (callout {:type     :positive
             :label-string " YES "
             :label        (bling [:positive-bg.white.bold " YES "])}
            "Callout, type :positve, enriched label")

   (callout {:label "YES"
             :colorway  "positive"}
            "Callout, type :positve, user label")

   (callout {:colorway :magenta
             :theme    :gutter}
            (bling [:blue "One line."]
                   #_"\n"
                   #_[:red "red"]))

   (callout {:colorway :magenta
             :theme    :gutter
             ;; :label "foo"
             }
            (bling [:blue "1 of 2 lines."]
                   "\n"
                   [:red "2nd line"])))

(defn examples-warnings-for-bad-arg-to-callout []
   (callout {:colorway :neutral
            :theme    :rainbow-gutter }
          (bling [:bold.neutral 
                  "The following callouts would be"]
                  "\n"
                  [:bold.neutral "issued by bling.core/callout, if the"]
                                  "\n"
                  [:bold.neutral "user were to supply malformed args."]))

   ;; This should issue a warning callout with point-of-interest
   (callout nil)
 
   ;; This should issue a warning callout with point-of-interest
   (callout [1 2 3])
 
   ;; This should issue a warning callout with point-of-interest
   (callout nil)
 
   ;; This should issue a warning callout with point-of-interest
   (callout [1 2 3]))



;; Single callout
#_(callout {:theme :sideline-bold
          :label-theme :marquee
          :type        :warning
          ;; :theme    :gutter
             ;; :label "foo"
          }
         "Hello")

;; Single callout , gutter
#_(callout
  {:type :info
   :label-theme :minimal
   :margin-left 2
   :theme :gutter})

;; Single banner
#_(print-bling 
 (bling.banner/banner 
  {
   :font               bling.fonts/ansi-shadow
  ;;  :font-weight        :bold
   :text               "ABCDEFG"
   :gradient-colors    [:cool :warm]
  ;;  :gradient-direction nil
  ;;  :gradient-shift      0
  ;;  :contrast           :low
  ;;  :margin-bottom      1
   ;; :display-missing-chars? true
   }))


;; Single colors
;; (print-bling [:bold.light-blue "Hello"])
;; (print-bling [:bold.blue "Hello"])
;; (print-bling [:bold.dark-blue "Hello"])
;; (print-bling [:bold.blue "Hello"])
;; (?sgr (bling [:bold.red "hello"]))


(defn visual-test-suite []
  (random-callouts)
  (bling-basics)
  (examples-warnings-for-bad-arg-to-callout))

#_(visual-test-suite)

(bling.sample/sample)

#_(sample/print-bling-banner-font-collection)

#_(sample/print-bling-banner-font-samples)

#_(sample/print-bling-banner-gradients 
 {:select-fonts [
                 ;; :isometric-1
                 :ansi-shadow
                 ]
  :display-labels? true})

#_(sample/print-bling-banner-gradient-warm-cool)

#_(sample/print-bling-banner-bold-font)

#_(sample/print-bling-banner-gradient-contrast-options)

#_(sample/print-bling-banners-with-bad-option-values)

#_(pprint
   (get-in bling.fonts/isometric-1 
           [:chars-array-map "a"]))

#_(sample/print-bling-color-contrast)
