(ns
 bling.core-test
 (:require
  [clojure.test :refer [deftest is]]
  [bling.test-gen :refer [escape-sgr]]
  [bling.core :as bling :refer [bling callout point-of-interest]]
  [bling.sample :as sample :refer [callout+]]
  [clojure.string :as string]))


;; example callouts ------------------------------------------------------------
#_(defn my-error-callout [{:keys [header body source]}]
  (callout {:type        :error
            :theme       :gutter
            :margin-left 1
            :padding-top 1}
           header
           source
           body))

#_(my-error-callout
 {:header "Your header message goes here\n"
  :source (point-of-interest 
           {:type                  :error
            :file                  "example.ns.core"
            :line                  11
            :column                1
            :form                  '(+ foo baz)
            :text-decoration-index 2})
  :body   (str "The body of your template goes here.\n"
               "Second line of copy.\n"
               "Another line.")})
;; -----------------------------------------------------------------------------

(deftest
 all-colors
 (is
  (=
   (-> (into [] (sample/all-the-colors*)) escape-sgr string/join)
   "[\"〠38;5;231;1;48;5;196〠 red 〠0;〠     〠38;5;196;1〠red〠0;〠      〠38;5;196〠red〠0;〠      〠38;5;196;9〠red〠0;〠      〠38;5;196;4〠red〠0;〠      〠3;38;5;196〠red〠0;〠     \" \"〠38;5;231;1;48;5;208〠 orange 〠0;〠  〠38;5;208;1〠orange〠0;〠   〠38;5;208〠orange〠0;〠   〠38;5;208;9〠orange〠0;〠   〠38;5;208;4〠orange〠0;〠   〠3;38;5;208〠orange〠0;〠  \" \"〠38;5;231;1;48;5;178〠 yellow 〠0;〠  〠38;5;178;1〠yellow〠0;〠   〠38;5;178〠yellow〠0;〠   〠38;5;178;9〠yellow〠0;〠   〠38;5;178;4〠yellow〠0;〠   〠3;38;5;178〠yellow〠0;〠  \" \"〠38;5;231;1;48;5;106〠 olive 〠0;〠   〠38;5;106;1〠olive〠0;〠    〠38;5;106〠olive〠0;〠    〠38;5;106;9〠olive〠0;〠    〠38;5;106;4〠olive〠0;〠    〠3;38;5;106〠olive〠0;〠   \" \"〠38;5;231;1;48;5;40〠 green 〠0;〠   〠38;5;40;1〠green〠0;〠    〠38;5;40〠green〠0;〠    〠38;5;40;9〠green〠0;〠    〠38;5;40;4〠green〠0;〠    〠3;38;5;40〠green〠0;〠   \" \"〠38;5;231;1;48;5;39〠 blue 〠0;〠    〠38;5;39;1〠blue〠0;〠     〠38;5;39〠blue〠0;〠     〠38;5;39;9〠blue〠0;〠     〠38;5;39;4〠blue〠0;〠     〠3;38;5;39〠blue〠0;〠    \" \"〠38;5;231;1;48;5;141〠 purple 〠0;〠  〠38;5;141;1〠purple〠0;〠   〠38;5;141〠purple〠0;〠   〠38;5;141;9〠purple〠0;〠   〠38;5;141;4〠purple〠0;〠   〠3;38;5;141〠purple〠0;〠  \" \"〠38;5;231;1;48;5;201〠 magenta 〠0;〠 〠38;5;201;1〠magenta〠0;〠  〠38;5;201〠magenta〠0;〠  〠38;5;201;9〠magenta〠0;〠  〠38;5;201;4〠magenta〠0;〠  〠3;38;5;201〠magenta〠0;〠 \" \"〠38;5;231;1;48;5;247〠 gray 〠0;〠    〠38;5;247;1〠gray〠0;〠     〠38;5;247〠gray〠0;〠     〠38;5;247;9〠gray〠0;〠     〠38;5;247;4〠gray〠0;〠     〠3;38;5;247〠gray〠0;〠    \" \"〠38;5;231;1;48;5;16〠 black 〠0;〠   〠38;5;16;1〠black〠0;〠    〠38;5;16〠black〠0;〠    〠38;5;16;9〠black〠0;〠    〠38;5;16;4〠black〠0;〠    〠3;38;5;16〠black〠0;〠   \" \"〠38;5;231;1;48;5;231〠 white 〠0;〠   〠38;5;231;1〠white〠0;〠    〠38;5;231〠white〠0;〠    〠38;5;231;9〠white〠0;〠    〠38;5;231;4〠white〠0;〠    〠3;38;5;231〠white〠0;〠   \"]")))


(deftest
 color-contrast
 (is
  (=
   (->
    (into [] (flatten (sample/bling-color-contrast)))
    escape-sgr
    string/join)
   "[\"〠38;5;196;1〠red, contrast :low〠0;〠\" \"〠38;5;196;1〠red, contrast :medium〠0;〠\" \"〠38;5;196;1〠red, contrast :high〠0;〠\" \"〠38;5;208;1〠orange, contrast :low〠0;〠\" \"〠38;5;208;1〠orange, contrast :medium〠0;〠\" \"〠38;5;208;1〠orange, contrast :high〠0;〠\" \"〠38;5;178;1〠yellow, contrast :low〠0;〠\" \"〠38;5;178;1〠yellow, contrast :medium〠0;〠\" \"〠38;5;178;1〠yellow, contrast :high〠0;〠\" \"〠38;5;106;1〠olive, contrast :low〠0;〠\" \"〠38;5;106;1〠olive, contrast :medium〠0;〠\" \"〠38;5;106;1〠olive, contrast :high〠0;〠\" \"〠38;5;40;1〠green, contrast :low〠0;〠\" \"〠38;5;40;1〠green, contrast :medium〠0;〠\" \"〠38;5;40;1〠green, contrast :high〠0;〠\" \"〠38;5;39;1〠blue, contrast :low〠0;〠\" \"〠38;5;39;1〠blue, contrast :medium〠0;〠\" \"〠38;5;39;1〠blue, contrast :high〠0;〠\" \"〠38;5;141;1〠purple, contrast :low〠0;〠\" \"〠38;5;141;1〠purple, contrast :medium〠0;〠\" \"〠38;5;141;1〠purple, contrast :high〠0;〠\" \"〠38;5;201;1〠magenta, contrast :low〠0;〠\" \"〠38;5;201;1〠magenta, contrast :medium〠0;〠\" \"〠38;5;201;1〠magenta, contrast :high〠0;〠\" \"〠38;5;247;1〠gray, contrast :low〠0;〠\" \"〠38;5;247;1〠gray, contrast :medium〠0;〠\" \"〠38;5;247;1〠gray, contrast :high〠0;〠\"]")))


(deftest
 underline-styles
 (is
  (=
   (->
    (bling
     (bling
      [:underline "underline"]
      "\n"
      [:double-underline "double-underline"]
      "\n"
      [:wavy-underline "wavy-underline"]
      "\n"
      [:dotted-underline "dotted-underline"]
      "\n"
      [:dashed-underline "dashed-underline"]))
    escape-sgr
    string/join)
   "〠4〠underline〠0;〠\n[4:2mdouble-underline〠0;〠\n[4:3mwavy-underline〠0;〠\n[4:4mdotted-underline〠0;〠\n[4:5mdashed-underline〠0;〠")))


(deftest
 callout-info
 (is
  (=
   (->
    (bling
     (callout+ {:data? true, :print-example-call? false, :type :info}))
    escape-sgr
    string/join)
   "〠38;5;39〠┌〠0;〠〠38;5;39〠─〠0;〠〠1〠 INFO〠0;〠\n〠38;5;39〠│〠0;〠  Callout with type of :info\n〠38;5;39〠└〠0;〠")))


(deftest
 callout-info-label
 (is
  (=
   (->
    (bling
     (callout+
      {:data? true,
       :print-example-call? false,
       :type :info,
       :label "My custom label"}))
    escape-sgr
    string/join)
   "〠38;5;39〠┌〠0;〠〠38;5;39〠─〠0;〠〠1〠 My custom label〠0;〠\n〠38;5;39〠│〠0;〠  Callout with type of :info and custom :label\n〠38;5;39〠└〠0;〠")))


(deftest
 callout-warning
 (is
  (=
   (->
    (bling
     (callout+
      {:data? true, :print-example-call? false, :type :warning}))
    escape-sgr
    string/join)
   "〠38;5;208〠┌〠0;〠〠38;5;208〠─〠0;〠〠1〠 WARNING〠0;〠\n〠38;5;208〠│〠0;〠  Callout with type of :warning\n〠38;5;208〠│〠0;〠  This is not a real warning\n〠38;5;208〠└〠0;〠")))


(deftest
 callout-error
 (is
  (=
   (->
    (bling
     (callout+ {:data? true, :print-example-call? false, :type :error}))
    escape-sgr
    string/join)
   "〠38;5;196〠┌〠0;〠〠38;5;196〠─〠0;〠〠1〠 ERROR〠0;〠\n〠38;5;196〠│〠0;〠  Callout with type of :error\n〠38;5;196〠│〠0;〠  This is not a real error\n〠38;5;196〠└〠0;〠")))


(deftest
 callout-positive-label
 (is
  (=
   (->
    (bling
     (callout+
      {:data? true,
       :print-example-call? false,
       :colorway :positive,
       :label "SUCCESS!"}))
    escape-sgr
    string/join)
   "〠38;5;40〠┌〠0;〠〠38;5;40〠─〠0;〠〠1〠 SUCCESS!〠0;〠\n〠38;5;40〠│〠0;〠  Callout with colorway of :positive and custom :label\n〠38;5;40〠└〠0;〠")))


(deftest
 sideline-bold-callout-info
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :info}))
    escape-sgr
    string/join)
   "〠38;5;39〠┏〠0;〠〠38;5;39〠━〠0;〠〠1〠 INFO〠0;〠\n〠38;5;39〠┃〠0;〠  Callout with type of :info\n〠38;5;39〠┗〠0;〠")))


(deftest
 sideline-bold-callout-info-label
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :info,
       :label "My custom label"}))
    escape-sgr
    string/join)
   "〠38;5;39〠┏〠0;〠〠38;5;39〠━〠0;〠〠1〠 My custom label〠0;〠\n〠38;5;39〠┃〠0;〠  Callout with type of :info and custom :label\n〠38;5;39〠┗〠0;〠")))


(deftest
 sideline-bold-callout-warning
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :warning}))
    escape-sgr
    string/join)
   "〠38;5;208〠┏〠0;〠〠38;5;208〠━〠0;〠〠1〠 WARNING〠0;〠\n〠38;5;208〠┃〠0;〠  Callout with type of :warning\n〠38;5;208〠┃〠0;〠  This is not a real warning\n〠38;5;208〠┗〠0;〠")))


(deftest
 sideline-bold-callout-error
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :error}))
    escape-sgr
    string/join)
   "〠38;5;196〠┏〠0;〠〠38;5;196〠━〠0;〠〠1〠 ERROR〠0;〠\n〠38;5;196〠┃〠0;〠  Callout with type of :error\n〠38;5;196〠┃〠0;〠  This is not a real error\n〠38;5;196〠┗〠0;〠")))


(deftest
 sideline-bold-callout-positive-label
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :colorway :positive,
       :label "SUCCESS!"}))
    escape-sgr
    string/join)
   "〠38;5;40〠┏〠0;〠〠38;5;40〠━〠0;〠〠1〠 SUCCESS!〠0;〠\n〠38;5;40〠┃〠0;〠  Callout with colorway of :positive and custom :label\n〠38;5;40〠┗〠0;〠")))


(deftest
 marquee-sideline-bold-callout-info
 (is
  (=
   (->
    (bling
     (callout+
      {:label-theme :marquee,
       :theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :info}))
    escape-sgr
    string/join)
   "〠38;5;39〠   ┏━━━━━━━━┓〠0;〠\n〠38;5;39〠┏━━┫  〠0;〠〠〠INFO〠0;〠〠38;5;39〠  ┃〠0;〠\n〠38;5;39〠┃  ┗━━━━━━━━┛〠0;〠\n〠38;5;39〠┃〠0;〠  Callout with type of :info\n〠38;5;39〠┗〠0;〠")))


(deftest
 marquee-sideline-bold-callout-info-label
 (is
  (=
   (->
    (bling
     (callout+
      {:label-theme :marquee,
       :theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :info,
       :label "My custom label"}))
    escape-sgr
    string/join)
   "〠38;5;39〠   ┏━━━━━━━━━━━━━━━━━━━┓〠0;〠\n〠38;5;39〠┏━━┫  〠0;〠〠〠My custom label〠0;〠〠38;5;39〠  ┃〠0;〠\n〠38;5;39〠┃  ┗━━━━━━━━━━━━━━━━━━━┛〠0;〠\n〠38;5;39〠┃〠0;〠  Callout with type of :info and custom :label\n〠38;5;39〠┗〠0;〠")))


(deftest
 marquee-sideline-bold-callout-warning
 (is
  (=
   (->
    (bling
     (callout+
      {:label-theme :marquee,
       :theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :warning}))
    escape-sgr
    string/join)
   "〠38;5;208〠   ┏━━━━━━━━━━━┓〠0;〠\n〠38;5;208〠┏━━┫  〠0;〠〠〠WARNING〠0;〠〠38;5;208〠  ┃〠0;〠\n〠38;5;208〠┃  ┗━━━━━━━━━━━┛〠0;〠\n〠38;5;208〠┃〠0;〠  Callout with type of :warning\n〠38;5;208〠┃〠0;〠  This is not a real warning\n〠38;5;208〠┗〠0;〠")))


(deftest
 marquee-sideline-bold-callout-error
 (is
  (=
   (->
    (bling
     (callout+
      {:label-theme :marquee,
       :theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :type :error}))
    escape-sgr
    string/join)
   "〠38;5;196〠   ┏━━━━━━━━━┓〠0;〠\n〠38;5;196〠┏━━┫  〠0;〠〠〠ERROR〠0;〠〠38;5;196〠  ┃〠0;〠\n〠38;5;196〠┃  ┗━━━━━━━━━┛〠0;〠\n〠38;5;196〠┃〠0;〠  Callout with type of :error\n〠38;5;196〠┃〠0;〠  This is not a real error\n〠38;5;196〠┗〠0;〠")))


(deftest
 marquee-sideline-bold-callout-positive-label
 (is
  (=
   (->
    (bling
     (callout+
      {:label-theme :marquee,
       :theme :sideline-bold,
       :data? true,
       :print-example-call? false,
       :colorway :positive,
       :label "SUCCESS!"}))
    escape-sgr
    string/join)
   "〠38;5;40〠   ┏━━━━━━━━━━━━┓〠0;〠\n〠38;5;40〠┏━━┫  〠0;〠〠〠SUCCESS!〠0;〠〠38;5;40〠  ┃〠0;〠\n〠38;5;40〠┃  ┗━━━━━━━━━━━━┛〠0;〠\n〠38;5;40〠┃〠0;〠  Callout with colorway of :positive and custom :label\n〠38;5;40〠┗〠0;〠")))


(deftest
 gutter-callout-info
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :gutter,
       :data? true,
       :print-example-call? false,
       :type :info}))
    escape-sgr
    string/join)
   "〠38;5;39〠〠0;〠〠38;5;39〠▆〠0;〠  〠1〠INFO〠0;〠\n〠38;5;39〠〠38;5;39〠█〠0;〠〠0;〠  Callout with type of :info")))


(deftest
 gutter-callout-info-label
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :gutter,
       :data? true,
       :print-example-call? false,
       :type :info,
       :label "My custom label"}))
    escape-sgr
    string/join)
   "〠38;5;39〠〠0;〠〠38;5;39〠▆〠0;〠  〠1〠My custom label〠0;〠\n〠38;5;39〠〠38;5;39〠█〠0;〠〠0;〠  Callout with type of :info and custom :label")))


(deftest
 gutter-callout-warning
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :gutter,
       :data? true,
       :print-example-call? false,
       :type :warning}))
    escape-sgr
    string/join)
   "〠38;5;208〠〠0;〠〠38;5;208〠▆〠0;〠  〠1〠WARNING〠0;〠\n〠38;5;208〠〠38;5;208〠█〠0;〠〠0;〠  Callout with type of :warning\n〠38;5;208〠〠38;5;208〠█〠0;〠〠0;〠  This is not a real warning")))


(deftest
 gutter-callout-error
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :gutter,
       :data? true,
       :print-example-call? false,
       :type :error}))
    escape-sgr
    string/join)
   "〠38;5;196〠〠0;〠〠38;5;196〠▆〠0;〠  〠1〠ERROR〠0;〠\n〠38;5;196〠〠38;5;196〠█〠0;〠〠0;〠  Callout with type of :error\n〠38;5;196〠〠38;5;196〠█〠0;〠〠0;〠  This is not a real error")))


(deftest
 gutter-callout-positive-label
 (is
  (=
   (->
    (bling
     (callout+
      {:theme :gutter,
       :data? true,
       :print-example-call? false,
       :colorway :positive,
       :label "SUCCESS!"}))
    escape-sgr
    string/join)
   "〠38;5;40〠〠0;〠〠38;5;40〠▆〠0;〠  〠1〠SUCCESS!〠0;〠\n〠38;5;40〠〠38;5;40〠█〠0;〠〠0;〠  Callout with colorway of :positive and custom :label")))
