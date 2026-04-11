
(ns bling.demo.browser
  (:require
  ;;  [bling.banner :refer [banner]]
  ;;  [bling.fonts.miniwi :refer [miniwi]]
  ;;  [bling.fonts.ansi-shadow :refer [ansi-shadow]]
  ;;  [bling.fonts.drippy :refer [drippy]]
  ;;  [bling.fonts.big :refer [big]]
  ;;  [bling.fonts.big-money :refer [big-money]]
  ;;  [bling.fonts.rounded :refer [rounded]]
  ;;  [bling.fonts.isometric-1 :refer [isometric-1]]
  ;;  [bling.hifi :refer [print-hifi hifi]]
  ;;  [clojure.math :as math]
  ;;  [fireworks.core :refer [? !? ?> !?>]]
  ;;  [fireworks.defs]
   [reagent.dom.client :as rdomc]
   [reagent.ratom]
  ;;  ["ansi-to-html" :as Convert]
  ;;  ["ansi_up" :refer [AnsiUp]]

   ;; ------------------------------

   [reagent.core :as r]
   [clojure.walk :as walk]
   [bling.core :refer [bling print-bling point-of-interest ?sgr]]
   [bling.util :refer [when->]]
   [bling.hifi :refer [hifi]]
   [bling.browser :as browser]
   [bling.demo.ansi-to-html :as ansi-to-html]
   [zprint.core :as zp]
   [goog.string]
   [me.flowthing.pp :as pp :refer [pprint]]
   [domo.core :as domo]
   [clojure.string :as string]

   ))


(def sample-side-label-text
  "my.ns.core:44:2")

(def simple-body-example
  {:ansi-sgr-string (bling.core/bling "The body of your callout goes here."
                                      "\n\n"
                                      "Second line of copy."
                                      "\n\n"
                                      "Third line of copy.")
   :source          ["The body of your callout goes here."
                     "\n\n"
                     "Second line of copy."
                     "\n\n"
                     "Third line of copy."]})

(def example-with-source [["cool"]])


(def poi-body-example
  {:ansi-sgr-string (bling.core/bling
                     [:p "You are trying to add a boolean to a number."]
                     (->> (bling.core/point-of-interest
                           {:margin-top             1
                            :header-file-info-style {:font-style :italic}
                            :form                   (let [s (bling.hifi/hifi '(+ 1 true))]
                                                      (-> s
                                                          (bling.core/with-ascii-underline
                                                            (assoc {:line-index 0}
                                                                   :text-decoration-color :red))))
                            :file                   "foo"
                            :line                   111
                            :column                 33}))
                     [:p "Additional information can go here"]
                     [:p "Another line of text"])
   :source          ["You are trying to add a boolean to a number."
                     "\n\n"
                     '(->> (bling.core/point-of-interest
                            {:margin-top             1
                             :header-file-info-style {:font-style :italic}
                             :form                   (let [s (bling.hifi/hifi '(+ 1 true))]
                                                       (-> s
                                                           (bling.core/with-ascii-underline
                                                             (assoc {:line-index 0}
                                                                    :text-decoration-color :red))))
                             :file                   "foo"
                             :line                   111
                             :column                 33}))
                     "\n"
                     "Additional information can go here"
                     "\n\n"
                     "Another line of text"]})


(def simple-body-with-bling-example
  {:ansi-sgr-string (bling.core/bling
                     [:p "Example of callout body with Bling styling."]
                     [:p
                      "Line with "
                      [:bold.purple "bold purple"]
                      " and "
                      [:italic.blue "italic blue"]
                      " text."]
                     [:p "Lines in a " [:blue ":p"] " hiccup tag will insert a trailing\n"
                      [:olive "\"\\n\\n\""] " for spacing between paragraphs."]
                     #_[:p
                      "Example hyperlink:\n"
                      [{:href "https://github.com/paintparty/bling"}
                       "View the official Bling docs"]
                      ;; (bling.ansi-to-html/html-hyperlink
                      ;;  [{:href "https://github.com/paintparty/bling"}
                      ;;   "View the official Bling docs"])
                        ])
   :source          ['(bling.core/bling
                       "Example of callout body with Bling styling."
                       "\n\n"
                       "Line with " [:bold.purple "bold purple"] " and " [:italic.blue "italic blue"] " text."
                       "\n\n"
                       [:p "Lines in a " [:blue ":p"] " hiccup tag will insert a trailing\n"
                        [:olive "\"\\n\\n\""] " for spacing between paragraphs."]
                       #_[:p
                        "Example hyperlink:\n"
                        [{:href "https://github.com/paintparty/bling"} "View the official Bling docs"]])]})

(def sample-body-content
  {"Simple"                         simple-body-example
   "With additional Bling styling"  simple-body-with-bling-example
   "With Point Of Interest Diagram" poi-body-example})



(defonce S
  (reagent.ratom/atom
   {:callout-type       "error"
    :callout-theme      "sandwich"
    :border-shape       "sharp"
    :label-theme        "simple"
    :label?             true
    :sample-side-label? true
    :body-content       "Simple"
    :active-view        "Callout Config"}))

(defn header-label-text []
  (or (:label-text @S)
      (let [s (:callout-type @S)]
        (if (= s "neutral")
          "Your label"
          (string/upper-case s)))))

(defn control-radio-group
  [{:keys [kw values]}]
  [:div.section.flex-row
   [:div.section-label.foreground (-> kw name (string/replace #"-" " "))]
   (into [:div.radio-pill]
         (for [value values
               :let  [section-id* kw
                      id (str (name kw) "-" value)]]
           [:<>
            [:input
             {:id         id
              :type       "radio"
              :name       kw
              :value      value
              :checked    (= value (kw @S))
              :on-change  #(swap! S assoc section-id* value)}]
            [:label {:for id}
             (str #_":" value)]]))])

(defn easy-checkbox-input [k]
  [:input.easy-checkbox-input.transition.background
   {:type           "checkbox"
    :on-change      #(swap! S
                            update-in
                            [k]
                            (fn [b] (if b false true)))
    :defaultChecked (when (k @S) true)}])

(defn easy-checkbox [{:keys [k section-label]}]
  [:label.easy-checkbox.transition
   (when section-label
     [:span
      [:span.flex-row-center.transition.easy-label
       [:span.easy-label-text section-label]]])
   [easy-checkbox-input k]])


(defn copy-to-clipboard! [text copied?]
  (-> js/navigator
      .-clipboard
      (.writeText text)
      (.then #(reset! copied? true))
      (.catch #(js/console.error "Copy failed" %))))

(def copied? (reagent.ratom/atom false))

(defn snippet-textarea [content]
  [:div.snippet.relative
   [:textarea {:read-only true
               :value     content
               :class     [:foreground :background]}]
   [:div.snippet-header.background
    {:data-ks-position "top-left-corner-inside"}
    "clojure"]
   [:button.foreground.background.copy-snippet
    {:aria-label       "Copy code"
     :data-ks-position "top-right-corner-inside"
     :on-click         (fn []
                         (-> js/navigator .-clipboard
                             (.writeText content)
                             (.then #(reset! copied? true)))
                         (js/setTimeout #(reset! copied? false) 2000))
     :style            {:width           "32px"
                        :height          "32px"
                        :cursor          "pointer"
                        :border          "none"
                        :border-radius   "6px"
                        :display         "flex"
                        :align-items     "center"
                        :justify-content "center"}}
    (if @copied?
      [:svg {:viewBox        "0 0 24 24"
             :width          15
             :height         15
             :fill           "none"
             :stroke         "currentColor"
             :stroke-width   "2"
             :stroke-linecap "round"}
       [:polyline {:points "20 6 9 17 4 12"}]]
      [:svg {:viewBox        "0 0 24 24"
             :width          15
             :height         15
             :fill           "none"
             :stroke         "currentColor"
             :stroke-width   "1.8"
             :stroke-linecap "round"}
       [:rect {:x      9
               :y      9
               :width  13
               :height 13
               :rx     2}]
       [:path {:d "M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"}]])]])

(def octocat-svg
  [:svg {:width   "36"
         :height  "34"
         :viewBox "0 0 36 34"
         :fill    "none"
         :xmlns   "http://www.w3.org/2000/svg"}
   [:path {:d    "M18.0001 0C8.0602 0 0 7.80373 0 17.4304C0 25.1316 5.15756 31.6654 12.3096 33.9701C13.2092 34.1315 13.5395 33.592 13.5395 33.1316C13.5395 32.716 13.5226 31.3429 13.515 29.8865C8.50739 30.9408 7.45073 27.8299 7.45073 27.8299C6.63188 25.8152 5.45203 25.2794 5.45203 25.2794C3.81895 24.1976 5.57522 24.2199 5.57522 24.2199C7.38267 24.3428 8.33442 26.0161 8.33442 26.0161C9.93994 28.6807 12.5454 27.9104 13.5726 27.465C13.7341 26.3385 14.2006 25.5694 14.7154 25.1342C10.7173 24.6935 6.51445 23.1989 6.51445 16.5201C6.51445 14.6171 7.21758 13.0621 8.36902 11.8414C8.18213 11.4024 7.56605 9.62969 8.54339 7.22867C8.54339 7.22867 10.055 6.76023 13.4948 9.01541C14.9306 8.62922 16.4704 8.43558 18.0001 8.42891C19.5299 8.43558 21.071 8.62908 22.5094 9.01541C25.945 6.76037 27.4545 7.2288 27.4545 7.2288C28.4342 9.62955 27.8179 11.4025 27.631 11.8414C28.7851 13.0621 29.4834 14.6171 29.4834 16.5201C29.4834 23.2148 25.2726 24.6889 21.2643 25.1205C21.9099 25.6613 22.4852 26.7221 22.4852 28.3483C22.4852 30.6804 22.4644 32.5575 22.4644 33.1316C22.4644 33.5955 22.7884 34.139 23.7008 33.9677C30.8489 31.6605 36 25.1292 36 17.4306C36 7.80373 27.9409 0 18.0001 0ZM6.7417 24.83C6.70205 24.9166 6.56128 24.9426 6.43317 24.8831C6.30253 24.8263 6.22927 24.7083 6.27145 24.6214C6.31027 24.5322 6.45131 24.5073 6.58153 24.567C6.71231 24.624 6.78684 24.7431 6.7417 24.83ZM7.62708 25.595C7.5413 25.6721 7.37339 25.6363 7.25948 25.5145C7.14178 25.3931 7.1197 25.2305 7.20675 25.1523C7.29534 25.0752 7.45805 25.1113 7.57603 25.2328C7.69388 25.3558 7.71666 25.5171 7.62708 25.595ZM8.23458 26.5738C8.12419 26.6481 7.94391 26.5785 7.83239 26.4235C7.72214 26.2685 7.72214 26.0827 7.83478 26.0082C7.94658 25.9337 8.12419 26.0007 8.23711 26.1546C8.34708 26.3121 8.34708 26.498 8.23444 26.574L8.23458 26.5738ZM9.2617 27.7075C9.16313 27.8129 8.95289 27.7846 8.79905 27.6408C8.64169 27.5002 8.59795 27.3009 8.69695 27.1955C8.7968 27.0898 9.00816 27.1195 9.16313 27.2622C9.31922 27.4025 9.36703 27.6033 9.2617 27.7075ZM10.5895 28.0901C10.5459 28.2267 10.3434 28.2888 10.1396 28.2308C9.936 28.171 9.80283 28.0112 9.84389 27.8732C9.88622 27.7357 10.0896 27.6711 10.2949 27.7332C10.4982 27.7926 10.6318 27.9514 10.5895 28.0903V28.0901ZM12.1004 28.2526C12.1054 28.3963 11.9326 28.5154 11.7186 28.5181C11.5034 28.5228 11.3292 28.4065 11.3268 28.2648C11.3268 28.1198 11.4958 28.0018 11.7111 27.9982C11.9251 27.9941 12.1004 28.1096 12.1004 28.2526ZM13.5847 28.1974C13.6102 28.3377 13.4616 28.4818 13.249 28.5202C13.0402 28.5571 12.8467 28.4705 12.8201 28.3314C12.7942 28.1876 12.9457 28.0436 13.1542 28.0064C13.3671 27.9706 13.5575 28.0549 13.5847 28.1974Z"
           :fill "#161614"}]])

(defn counter-badge [icon icon-label count-text href]
  [:a {:href href
       :target "_blank"}
   [:div.counter-badge.flex-row
    {:style {:gap           0
             :opacity       0.8
             :font-size     "var(--font-size-xs)"
             :font-weight   "var(--font-weight-semi-bold)"
             :height        :28px
             :font-family   "system-ui"
             :border        "1px solid rgba(128, 128, 128, 0.625)"
             :border-radius :4px}}
    [:div.identity.flex-row-start 
     {:style {:padding                   "0 8px" 
              :border-top-left-radius    :4px
              :border-bottom-left-radius :4px
              :height                    :100%
              :align-items               :center
              :gap                       :5px}}
     icon
     [:span.icon-label icon-label]]
    [:div.count.flex-row-center 
     {:style {:background-color "rgba(0, 0, 0, 0.444)"
              :align-items      :center
              :height           :100%
              :padding          "0 8px"
              :border-top-right-radius :4px
              :border-bottom-right-radius :4px
              :border-left      "1px solid rgba(128, 128, 128, 0.444)"}}
     count-text]]])

(def octicon-star
  [:svg.octicon.octicon-star
   {:viewBox     "0 0 16 16"
    :width       "16"
    :height      "16"
    :aria-hidden "true"
    :style       {:height :17pxn
                  :filter "grayscale(1) contrast(1) brightness(1) invert()"}}
   [:path
    {:d "M8 .25a.75.75 0 0 1 .673.418l1.882 3.815 4.21.612a.75.75 0 0 1 .416 1.279l-3.046 2.97.719 4.192a.751.751 0 0 1-1.088.791L8 12.347l-3.766 1.98a.75.75 0 0 1-1.088-.79l.72-4.194L.818 6.374a.75.75 0 0 1 .416-1.28l4.21-.611L7.327.668A.75.75 0 0 1 8 .25Zm0 2.445L6.615 5.5a.75.75 0 0 1-.564.41l-3.097.45 2.24 2.184a.75.75 0 0 1 .216.664l-.528 3.084 2.769-1.456a.75.75 0 0 1 .698 0l2.77 1.456-.53-3.084a.75.75 0 0 1 .216-.664l2.24-2.183-3.096-.45a.75.75 0 0 1-.564-.41L8 2.694Z"}]])

(defn bling->reagent-unsafe-html-attr
  ([s]
   (bling->reagent-unsafe-html-attr s {}))
  ([s attrs]
   (merge attrs
          {:dangerouslySetInnerHTML (->> s
                                         ansi-to-html/->html
                                         r/unsafe-html)})))


(defn main-view []

  ;; #_(println
  ;;    (->> (bling.core/point-of-interest
  ;;          {:margin-top             1
  ;;           :header-file-info-style {:font-style :italic}
  ;;           :form                   (let [s (bling.hifi/hifi '(+ 1 true))]
  ;;                                     (-> s
  ;;                                         (bling.core/with-ascii-underline
  ;;                                           (assoc {:line-index 0}
  ;;                                                  :text-decoration-color :red))))
  ;;           :file                   "foo"
  ;;           :line                   111
  ;;           :column                 33})))

  ;; #_[:div "HI"]
  ;; #_(into [:div.code]
  ;;         (? :+ (bling.browser/ansi-sgr-string->hiccup
  ;;                (bling.banner/banner
  ;;                 {
  ;;                  ;;  :gradient-colors    [:green :blue]
  ;;                  ;;  :gradient-direction :to-bottom
  ;;                  :text "B"
  ;;                  :font big}))))
  ;; #_[:div
  ;;    {:style                   {:font-family "var(--code-font-stack)"}
  ;;     :dangerouslySetInnerHTML (bling->reagent-unsafe-html-attr
  ;;                               (bling.core/point-of-interest
  ;;                                {:margin-top             1
  ;;                                 :header-file-info-style {:font-style :italic}
  ;;                                 :form                   (let [s (bling.hifi/hifi '(+ 1 true))]
  ;;                                                           (-> s
  ;;                                                               (bling.core/with-ascii-underline
  ;;                                                                 (assoc {:line-index 0}
  ;;                                                                        :text-decoration-color :red))))
  ;;                                 :file                   "foo"
  ;;                                 :line                   111
  ;;                                 :column                 33})
  ;;                               )}]
  
  ;; #_[:div.absolute-center 
  ;;  {:style {:width :800px}
  ;;   ;; :dangerouslySetInnerHTML (r/unsafe-html
  ;;   ;;                           "<div class=\"absolute-center\" style=\"width: 800px;\">\n  <svg width=\"100%\" viewBox=\"0 0 800 500\" xmlns=\"http://www.w3.org/2000/svg\">\n    <defs>\n      <filter id=\"shadow\" x=\"-8%\" y=\"-12%\" width=\"116%\" height=\"130%\">\n        <feDropShadow dx=\"0\" dy=\"12\" stdDeviation=\"18\"\n                      flood-color=\"#000000\" flood-opacity=\"0.55\"/>\n      </filter>\n    </defs>\n    <rect x=\"100\" y=\"100\" width=\"600\" height=\"300\" rx=\"12\" ry=\"12\"\n          fill=\"#1a1a1a\" filter=\"url(#shadow)\"/>\n    <rect x=\"100\" y=\"100\" width=\"600\" height=\"36\" rx=\"12\" ry=\"12\" fill=\"#252525\"/>\n    <rect x=\"100\" y=\"118\" width=\"600\" height=\"18\" fill=\"#252525\"/>\n    <circle cx=\"120\" cy=\"118\" r=\"7\" fill=\"#696969\"/>\n    <circle cx=\"144\" cy=\"118\" r=\"7\" fill=\"#696969\"/>\n    <circle cx=\"168\" cy=\"118\" r=\"7\" fill=\"#696969\"/>\n    <foreignObject x=\"116\" y=\"152\" width=\"568\" height=\"232\">\n      <div xmlns=\"http://www.w3.org/1999/xhtml\"\n           style=\"font-family: 'JetBrains Mono'; font-size: 18px; line-height: 1.85; color: #ffffff; padding: 8px 8px 8px 0; word-break: break-word; white-space: pre-wrap;\">\n        First line of text here is it going to wrap or not idk, but wwssafdfasda\n      </div>\n    </foreignObject>\n  </svg>\n</div>"
  ;;   ;;                           )
  ;;   }
  ;;  #_[:img {:src "/graphics/terminal.svg"}]
   
  ;;  (let [height (reagent.ratom/atom 300)]
  ;;    [:svg {:width   "100%"
  ;;           :viewBox (? (str "0 0 600 " @height))
  ;;           :xmlns   "http://www.w3.org/2000/svg"}
  ;;     [:defs
  ;;      [:filter#shadow {:x      "-8%"
  ;;                       :y      "-12%"
  ;;                       :width  "116%"
  ;;                       :height "130%"}
  ;;       [:feDropShadow {:dx            "0"
  ;;                       :dy            "12"
  ;;                       :stdDeviation  "18"
  ;;                       :flood-color   "#000000"
  ;;                       :flood-opacity "0.55"}]]]
  ;;     (comment "Terminal window (100px margin on all sides of an 800×500 canvas)")
  ;;     [:rect {:x      "100"
  ;;             :y      "100"
  ;;             :width  "600"
  ;;             :height "300"
  ;;             :rx     "12"
  ;;             :ry     "12"
  ;;             :fill   "#1a1a1a"
  ;;             :filter "url(#shadow)"}]
  ;;     (comment "Title bar")
  ;;     [:rect {:x      "100"
  ;;             :y      "100"
  ;;             :width  "600"
  ;;             :height "36"
  ;;             :rx     "12"
  ;;             :ry     "12"
  ;;             :fill   "#252525"}]
  ;;     [:rect {:x      "100"
  ;;             :y      "118"
  ;;             :width  "600"
  ;;             :height "18"
  ;;             :fill   "#252525"}]
  ;;     (comment "Traffic light dots")
  ;;     [:circle {:cx   "120"
  ;;               :cy   "118"
  ;;               :r    "7"
  ;;               ;; :fill "#ff5f57"
  ;;               :fill "#696969"
  ;;               }]
  ;;     [:circle {:cx   "144"
  ;;               :cy   "118"
  ;;               :r    "7"
  ;;               ;; :fill "#febc2e"
  ;;               :fill "#696969"
  ;;               }]
  ;;     [:circle {:cx   "168"
  ;;               :cy   "118"
  ;;               :r    "7"
  ;;               ;; :fill "#28c840"
  ;;               :fill "#696969"
  ;;               }]
  ;;     #_(comment
  ;;         "Body text — edit the content inside <div>")
  ;;     [:foreignObject {:x      "116"
  ;;                      :y      "152"
  ;;                      :width  "568"
  ;;                      :height "232"}
  ;;      [:div
  ;;       {:xmlns "http://www.w3.org/1999/xhtml"
  ;;        :style {:font-family "JetBrains Mono"
  ;;                :font-size   :18px
  ;;                :line-height 1.85
  ;;                :color       "#ffffff"
  ;;                :padding     "8px 8px 8px 0"
  ;;                :word-break  :break-word
  ;;                :white-space :pre-wrap}}
  ;;       "First line of text here is it going to wrap or not idk, but wwssafdfasda"]]])
  ;;  ]
  ;; #_[:div.absolute.centered
     
  ;;    ]


  #_[:div "HI"]
  [:<> 
     [:nav.absolute-block-start-inside.flex-row-center
      [:div.inner.flex-row-space-between
       [:div.badges.flex-row-start
        [:div.versioned-badge.flex-row-start
         [:span.badge "Bling"]
         [:span.version "v0.20.0"]]
        #_[:div.versioned-badge.flex-row-start
           [:span.badge.playground "Playground"]
           [:span.version "alpha"]]]
       (into [:div.menu.flex-row-center ]
             (for [v ["About" "Callout Config" "Docs"]]
               [:button.pill.foreground
                {:class         [(-> v
                                     string/lower-case
                                     (string/replace #" " "-"))
                                 (when (= v (:active-view @S))
                                   "selected")]
                 :on-mouse-down #(swap! S assoc :active-view v)}
                v]))
       [:div.end-menu.flex-row-end 
        [counter-badge
         octicon-star
         "Star"
         "218"
         "https://github.com/paintparty/bling"]
        [counter-badge
         [:img
          {:src   "./public/graphics/clojars-logo-bw2.png"
           :style {:height :17px
                   :filter "grayscale(1) contrast(1) brightness(1) invert()"}}]
         "Clojars"
         "168K"
         "https://clojars.org/io.github.paintparty/bling"]]]]

     

     ;; DOCS
     [:div.docs-section.absolute-block-start-inside 
      {:style {:display (if (not= "Docs" (:active-view @S)) "none" "flex")}}
      [:div 
       [:p "Official docs can be found. "
        [:a.foreground {:href   "https://github.com/paintparty/bling"
                        :target "_blank"} "here"]]
       [:br]
       [:p "Public APIs are " 
        [:a.foreground {:href   "https://github.com/paintparty/bling/blob/main/API.md"
                        :target "_blank"}
         "here"]]
       [:br]
       [:p "New docs site coming soon!"]]]
     
     ;; ABOUT
     [:div.about-section.absolute-block-start-inside.foreground
      {:style {
               :display (if (not= "About" (:active-view @S)) "none" "flex")}}
      [:div 
       [:p "Bling is a library that provides hifi console printing for Clojure dialects."]
       [:br]
       [:p
        "Project repo can be found " 
        [:a.foreground
         {:href   "https://github.com/paintparty/bling"
          :target "_blank"} 
         "here"]]
       [:br]
       [:p 
        "Consumable via "
        [:a.foreground {:href   "https://clojars.org/io.github.paintparty/bling"
                        :target "_blank"} "Clojars" ]]]]

     [:div.panel.absolute-block-start-inside.flex-row
      {
       :style {:display (if (not= "Callout Config" (:active-view @S)) "none" "flex")}}
      [:div.controls.flex-col
       [:span.explanation.foreground 
        [:p.intro "Bling is a library that provides hifi console printing for Clojure dialects."]
        [:p
         [:span.code "bling.core/callout"]
         " is designed for the construction of cleanly formatted, easy-to-read message blocks. The UI below allows you to preview different options, and generate snippets for your own projects."]]
       ;; CALLOUT THEME
       [control-radio-group {:kw     :callout-theme
                             :values ["sandwich" "sideline" "gutter" "boxed"]}]

       ;; CALLOUT TYPE
       [control-radio-group {:kw     :callout-type
                             :values ["neutral" "error" "warning" "info"]}]

       ;; BORDER SHAPE
       [control-radio-group {:kw     :border-shape
                             :values ["sharp" "round"]}]

       ;; LABEL THEME
       [control-radio-group {:kw     :label-theme
                             :values ["simple" "marquee"]}]


       ;; LABEL TEXT
       [:div.section.flex-row.label-text
        [easy-checkbox {:k             :label?
                        :section-label "label text"}]
        [:div.relative
         [:input.foreground
          {:type        "text"
           :max-length   26
           :value       (header-label-text)
           :placeholder "Your label"
           ;; :on-focus    #(swap! S assoc :label-text-field-has-focus? true)
           ;; :on-blur     #(swap! S assoc :label-text-field-has-focus? false)
           :on-change   #(swap! S assoc :label-text (domo/etv %))}]
         [:div.text-clear.absolute-inline-end-inside
          {:role          :button
           :on-mouse-down #(swap! S assoc :label-text nil)}
          [:span.material-symbols-outlined "refresh"]]]]


       ;; SAMPLE SIDE LABEL
       [easy-checkbox {:k             :sample-side-label?
                       :section-label "sample side label"}]

       ;; SAMPLE BODY TEXT
       [:div.sample-body-text.flex-row
        [:div.section-label.foreground
         "sample body text"]
        [:div.select-wrapper.foreground
         (into [:select.foreground
                {:on-change #(swap! S assoc :body-content (domo/etv %))
                 :value     (:body-content @S)}]
               (for [value (keys sample-body-content)]
                 [:option {:value value
                           ;; :selected (when (= (:body-content @S) value) "")
                           }
                  value]))]]]

      ;; CALLOUT PREVIEW IN EMULATED TERMINAL EMULATOR
      [:div.terminal-section.flex-col 
       [:div.terminal.flex-col
        [:div.terminal-header.flex-row
         [:div.pill]
         [:div.pill]
         [:div.pill]]
        [:div.terminal-body.flex-col
         [:div.callout.flex-col
          {:class [(:callout-theme @S)
                   (or (:callout-type @S) "neutral")
                   (when (= (:border-shape @S) "round") "rounded-border")]
           :style {:justify-content :stretch}}

          ;; header
          (let [header-label-text* (when (:label? @S)
                                     (let [s (header-label-text)]
                                       (when (and s (not (string/blank? s)))
                                         s)))]
            [:div.callout-header.flex-row.terminal-background
             {:class [(str (name (or (:label-theme @S) :simple)) "-label")
                      (when (:side-label? @S) "with-side-label")
                      (when-not header-label-text* "no-label")
                      (when-not (:sample-side-label? @S) "no-side-label")]}
             [:div.header-padding-left.header-border]
             (when header-label-text*
               [:div.callout-label.termina-background header-label-text*])
             [:div.header-gap.header-border]
             (when (:sample-side-label? @S)
               [:div.callout-side-label.terminal-background sample-side-label-text])
             [:div.header-padding-right.header-border]])
          
          ;; body
          [:div.callout-body.flex-col
           [:div.body-text
            (->> @S
                 :body-content 
                 (get sample-body-content)
                 :ansi-sgr-string
                 bling->reagent-unsafe-html-attr)

            #_(->> @S :body-content (get sample-body-content) :hiccup)]]

          ;; footer
          [:div.callout-footer-wrap [:div.callout-footer.flex-row]]]]]

       ;; SNIPPET TO GET THE CODE
       [:div.snippet
        [snippet-textarea
         (zp/zprint-str
          (str 
           (with-out-str 
             (pprint 
              (remove nil?
                      (list 'require 
                            (symbol "'bling.core")
                            (when (= (:body-content @S)
                                     "With Point Of Interest Diagram")
                              (symbol "'bling.hifi"))))))
           "\n\n"
           (with-out-str
             (pprint
              (concat
               (list
                'bling.core/callout
                (merge {:type            (keyword (:callout-type @S))
                        :theme           (keyword (:callout-theme @S))
                        :label-theme     (keyword (:label-theme @S))
                        :border-notches? true
                        :border-shape    (keyword (:border-shape @S))
                        :side-label      (when (keyword (:sample-side-label? @S))
                                           sample-side-label-text)}
                       (some->> (:sample-side-label? @S)
                                keyword
                                (hash-map :side-label))
                       {:label (str "\"" (:label-text @S) "\"")}))
               (:source (get sample-body-content  (:body-content @S)))))))
          {:parse-string-all? true
           :parse             {:interpose "\n\n"} 
           :style             :community
           :map               {:comma? false}
           :fn-force-nl       #{:flow}
           :fn-map            {:default :flow}})]]]]])

(defonce root (rdomc/create-root (.getElementById js/document "app")))

(defn ^:dev/after-load start []
  (js/console.clear)
  (rdomc/render root [main-view]))

(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop [])
