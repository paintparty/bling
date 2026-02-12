;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                                            ;;
;;      This namespace is automatically generated in bling.test-gen.          ;;
;;                                                                            ;;
;;      Do not manually add anything to this namespace.                       ;;
;;                                                                            ;;
;;      To regenerate, set `bling.test-gen/write-tests?` to `true`, then      ;;
;;      run `lein test`.                                                      ;;
;;                                                                            ;;
;;      If you want do any experimentation use `bling.visual-test`            ;;
;;                                                                            ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns
 bling.core-test
 (:require
  [clojure.test :refer [deftest is]]
  [bling.test-gen :refer [escape-sgr]]
  [bling.core :as bling :refer [bling]]
  [bling.sample :as sample :refer [callout+]]
  [clojure.string :as string]))



(deftest
 all-colors
 (is
  (=
   (-> (vec (sample/all-the-colors*)) escape-sgr string/join)
   "[\"〠38;5;231;1;48;5;196〠 red 〠0;〠     〠38;5;203;1〠red〠0;〠      〠38;5;203〠red〠0;〠      〠38;5;203;9〠red〠0;〠      〠38;5;203;4〠red〠0;〠      〠3;38;5;203〠red〠0;〠     \" \"〠38;5;231;1;48;5;172〠 orange 〠0;〠  〠38;5;208;1〠orange〠0;〠   〠38;5;208〠orange〠0;〠   〠38;5;208;9〠orange〠0;〠   〠38;5;208;4〠orange〠0;〠   〠3;38;5;208〠orange〠0;〠  \" \"〠38;5;231;1;48;5;178〠 yellow 〠0;〠  〠38;5;220;1〠yellow〠0;〠   〠38;5;220〠yellow〠0;〠   〠38;5;220;9〠yellow〠0;〠   〠38;5;220;4〠yellow〠0;〠   〠3;38;5;220〠yellow〠0;〠  \" \"〠38;5;231;1;48;5;106〠 olive 〠0;〠   〠38;5;143;1〠olive〠0;〠    〠38;5;143〠olive〠0;〠    〠38;5;143;9〠olive〠0;〠    〠38;5;143;4〠olive〠0;〠    〠3;38;5;143〠olive〠0;〠   \" \"〠38;5;231;1;48;5;40〠 green 〠0;〠   〠38;5;82;1〠green〠0;〠    〠38;5;82〠green〠0;〠    〠38;5;82;9〠green〠0;〠    〠38;5;82;4〠green〠0;〠    〠3;38;5;82〠green〠0;〠   \" \"〠38;5;231;1;48;5;39〠 blue 〠0;〠    〠38;5;81;1〠blue〠0;〠     〠38;5;81〠blue〠0;〠     〠38;5;81;9〠blue〠0;〠     〠38;5;81;4〠blue〠0;〠     〠3;38;5;81〠blue〠0;〠    \" \"〠38;5;231;1;48;5;141〠 purple 〠0;〠  〠38;5;147;1〠purple〠0;〠   〠38;5;147〠purple〠0;〠   〠38;5;147;9〠purple〠0;〠   〠38;5;147;4〠purple〠0;〠   〠3;38;5;147〠purple〠0;〠  \" \"〠38;5;231;1;48;5;201〠 magenta 〠0;〠 〠38;5;213;1〠magenta〠0;〠  〠38;5;213〠magenta〠0;〠  〠38;5;213;9〠magenta〠0;〠  〠38;5;213;4〠magenta〠0;〠  〠3;38;5;213〠magenta〠0;〠 \" \"〠38;5;231;1;48;5;247〠 gray 〠0;〠    〠38;5;249;1〠gray〠0;〠     〠38;5;249〠gray〠0;〠     〠38;5;249;9〠gray〠0;〠     〠38;5;249;4〠gray〠0;〠     〠3;38;5;249〠gray〠0;〠    \" \"〠38;5;231;1;48;5;16〠 black 〠0;〠   〠38;5;16;1〠black〠0;〠    〠38;5;16〠black〠0;〠    〠38;5;16;9〠black〠0;〠    〠38;5;16;4〠black〠0;〠    〠3;38;5;16〠black〠0;〠   \" \"〠38;5;231;1;48;5;231〠 white 〠0;〠   〠38;5;231;1〠white〠0;〠    〠38;5;231〠white〠0;〠    〠38;5;231;9〠white〠0;〠    〠38;5;231;4〠white〠0;〠    〠3;38;5;231〠white〠0;〠   \"]")))


(deftest
 color-contrast
 (is
  (=
   (->
    (vec (flatten (sample/bling-color-contrast)))
    escape-sgr
    string/join)
   "[\"〠38;5;124;1〠red, contrast :low〠0;〠\" \"〠38;5;196;1〠red, contrast :medium〠0;〠\" \"〠38;5;203;1〠red, contrast :high〠0;〠\" \"〠38;5;166;1〠orange, contrast :low〠0;〠\" \"〠38;5;172;1〠orange, contrast :medium〠0;〠\" \"〠38;5;208;1〠orange, contrast :high〠0;〠\" \"〠38;5;136;1〠yellow, contrast :low〠0;〠\" \"〠38;5;178;1〠yellow, contrast :medium〠0;〠\" \"〠38;5;220;1〠yellow, contrast :high〠0;〠\" \"〠38;5;100;1〠olive, contrast :low〠0;〠\" \"〠38;5;106;1〠olive, contrast :medium〠0;〠\" \"〠38;5;143;1〠olive, contrast :high〠0;〠\" \"〠38;5;28;1〠green, contrast :low〠0;〠\" \"〠38;5;40;1〠green, contrast :medium〠0;〠\" \"〠38;5;82;1〠green, contrast :high〠0;〠\" \"〠38;5;26;1〠blue, contrast :low〠0;〠\" \"〠38;5;39;1〠blue, contrast :medium〠0;〠\" \"〠38;5;81;1〠blue, contrast :high〠0;〠\" \"〠38;5;129;1〠purple, contrast :low〠0;〠\" \"〠38;5;141;1〠purple, contrast :medium〠0;〠\" \"〠38;5;147;1〠purple, contrast :high〠0;〠\" \"〠38;5;163;1〠magenta, contrast :low〠0;〠\" \"〠38;5;201;1〠magenta, contrast :medium〠0;〠\" \"〠38;5;213;1〠magenta, contrast :high〠0;〠\" \"〠38;5;244;1〠gray, contrast :low〠0;〠\" \"〠38;5;247;1〠gray, contrast :medium〠0;〠\" \"〠38;5;249;1〠gray, contrast :high〠0;〠\"]")))


(deftest
 underline-styles
 (is
  (=
   (->
    (bling
     [:underline "underline"]
     "\n"
     [:double-underline "double-underline"]
     "\n"
     [:wavy-underline "wavy-underline"]
     "\n"
     [:dotted-underline "dotted-underline"]
     "\n"
     [:dashed-underline "dashed-underline"])
    escape-sgr
    string/join)
   "〠4〠underline〠0;〠\n[4:2mdouble-underline〠0;〠\n[4:3mwavy-underline〠0;〠\n[4:4mdotted-underline〠0;〠\n[4:5mdashed-underline〠0;〠")))


(deftest
 callout-info
 (is
  (=
   (->
    (callout+ {:data? true, :print-example-call? false, :type :info})
    escape-sgr
    string/join)
   "〠38;5;39〠─〠0;〠〠38;5;39〠───〠0;〠 INFO 〠38;5;39〠──────────────────────────────〠0;〠\n\nCallout with type of :info\n\n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 callout-info-label
 (is
  (=
   (->
    (callout+
     {:data? true,
      :print-example-call? false,
      :type :info,
      :label "My custom label"})
    escape-sgr
    string/join)
   "〠38;5;39〠─〠0;〠〠38;5;39〠───〠0;〠 My custom label 〠38;5;39〠───────────────────〠0;〠\n\nCallout with type of :info and custom :label\n\n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 callout-warning
 (is
  (=
   (->
    (callout+ {:data? true, :print-example-call? false, :type :warning})
    escape-sgr
    string/join)
   "〠38;5;172〠─〠0;〠〠38;5;172〠───〠0;〠 WARNING 〠38;5;172〠───────────────────────────〠0;〠\n\nCallout with type of :warning\nThis is not a real warning\n\n〠38;5;172〠────────────────────────────────────────〠0;〠")))


(deftest
 callout-error
 (is
  (=
   (->
    (callout+ {:data? true, :print-example-call? false, :type :error})
    escape-sgr
    string/join)
   "〠38;5;196〠─〠0;〠〠38;5;196〠───〠0;〠 ERROR 〠38;5;196〠─────────────────────────────〠0;〠\n\nCallout with type of :error\nThis is not a real error\n\n〠38;5;196〠────────────────────────────────────────〠0;〠")))


(deftest
 callout-positive-label
 (is
  (=
   (->
    (callout+
     {:data? true,
      :print-example-call? false,
      :colorway :positive,
      :label "SUCCESS!"})
    escape-sgr
    string/join)
   "〠38;5;40〠─〠0;〠〠38;5;40〠───〠0;〠 SUCCESS! 〠38;5;40〠──────────────────────────〠0;〠\n\nCallout with colorway of :positive and custom :label\n\n〠38;5;40〠────────────────────────────────────────〠0;〠")))


(deftest
 sideline-bold-callout-info
 (is
  (=
   (->
    (callout+
     {:theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :info})
    escape-sgr
    string/join)
   "〠38;5;39〠─〠0;〠〠38;5;39〠───〠0;〠 INFO 〠38;5;39〠──────────────────────────────〠0;〠\n\nCallout with type of :info\n\n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 sideline-bold-callout-info-label
 (is
  (=
   (->
    (callout+
     {:theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :info,
      :label "My custom label"})
    escape-sgr
    string/join)
   "〠38;5;39〠─〠0;〠〠38;5;39〠───〠0;〠 My custom label 〠38;5;39〠───────────────────〠0;〠\n\nCallout with type of :info and custom :label\n\n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 sideline-bold-callout-warning
 (is
  (=
   (->
    (callout+
     {:theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :warning})
    escape-sgr
    string/join)
   "〠38;5;172〠─〠0;〠〠38;5;172〠───〠0;〠 WARNING 〠38;5;172〠───────────────────────────〠0;〠\n\nCallout with type of :warning\nThis is not a real warning\n\n〠38;5;172〠────────────────────────────────────────〠0;〠")))


(deftest
 sideline-bold-callout-error
 (is
  (=
   (->
    (callout+
     {:theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :error})
    escape-sgr
    string/join)
   "〠38;5;196〠─〠0;〠〠38;5;196〠───〠0;〠 ERROR 〠38;5;196〠─────────────────────────────〠0;〠\n\nCallout with type of :error\nThis is not a real error\n\n〠38;5;196〠────────────────────────────────────────〠0;〠")))


(deftest
 sideline-bold-callout-positive-label
 (is
  (=
   (->
    (callout+
     {:theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :colorway :positive,
      :label "SUCCESS!"})
    escape-sgr
    string/join)
   "〠38;5;40〠─〠0;〠〠38;5;40〠───〠0;〠 SUCCESS! 〠38;5;40〠──────────────────────────〠0;〠\n\nCallout with colorway of :positive and custom :label\n\n〠38;5;40〠────────────────────────────────────────〠0;〠")))


(deftest
 marquee-sideline-bold-callout-info
 (is
  (=
   (->
    (callout+
     {:label-theme :marquee,
      :theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :info})
    escape-sgr
    string/join)
   "〠38;5;39〠  ┌────────┐〠0;〠\n〠38;5;39〠──┤  〠0;〠〠〠INFO〠0;〠  〠38;5;39〠├〠0;〠〠38;5;39〠────────────────────────────〠0;〠\n〠38;5;39〠  └────────┘〠0;〠\n  \n  Callout with type of :info\n  \n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 marquee-sideline-bold-callout-info-label
 (is
  (=
   (->
    (callout+
     {:label-theme :marquee,
      :theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :info,
      :label "My custom label"})
    escape-sgr
    string/join)
   "〠38;5;39〠  ┌───────────────────┐〠0;〠\n〠38;5;39〠──┤  〠0;〠〠〠My custom label〠0;〠  〠38;5;39〠├〠0;〠〠38;5;39〠─────────────────〠0;〠\n〠38;5;39〠  └───────────────────┘〠0;〠\n  \n  Callout with type of :info and custom :label\n  \n〠38;5;39〠────────────────────────────────────────〠0;〠")))


(deftest
 marquee-sideline-bold-callout-warning
 (is
  (=
   (->
    (callout+
     {:label-theme :marquee,
      :theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :warning})
    escape-sgr
    string/join)
   "〠38;5;172〠  ┌───────────┐〠0;〠\n〠38;5;172〠──┤  〠0;〠〠〠WARNING〠0;〠  〠38;5;172〠├〠0;〠〠38;5;172〠─────────────────────────〠0;〠\n〠38;5;172〠  └───────────┘〠0;〠\n  \n  Callout with type of :warning\n  This is not a real warning\n  \n〠38;5;172〠────────────────────────────────────────〠0;〠")))


(deftest
 marquee-sideline-bold-callout-error
 (is
  (=
   (->
    (callout+
     {:label-theme :marquee,
      :theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :type :error})
    escape-sgr
    string/join)
   "〠38;5;196〠  ┌─────────┐〠0;〠\n〠38;5;196〠──┤  〠0;〠〠〠ERROR〠0;〠  〠38;5;196〠├〠0;〠〠38;5;196〠───────────────────────────〠0;〠\n〠38;5;196〠  └─────────┘〠0;〠\n  \n  Callout with type of :error\n  This is not a real error\n  \n〠38;5;196〠────────────────────────────────────────〠0;〠")))


(deftest
 marquee-sideline-bold-callout-positive-label
 (is
  (=
   (->
    (callout+
     {:label-theme :marquee,
      :theme :sideline-bold,
      :data? true,
      :print-example-call? false,
      :colorway :positive,
      :label "SUCCESS!"})
    escape-sgr
    string/join)
   "〠38;5;40〠  ┌────────────┐〠0;〠\n〠38;5;40〠──┤  〠0;〠〠〠SUCCESS!〠0;〠  〠38;5;40〠├〠0;〠〠38;5;40〠────────────────────────〠0;〠\n〠38;5;40〠  └────────────┘〠0;〠\n  \n  Callout with colorway of :positive and custom :label\n  \n〠38;5;40〠────────────────────────────────────────〠0;〠")))


(deftest
 gutter-callout-info
 (is
  (=
   (->
    (callout+
     {:theme :gutter,
      :data? true,
      :print-example-call? false,
      :type :info})
    escape-sgr
    string/join)
   "〠38;5;39〠▆〠0;〠  INFO     \n〠38;5;39〠█〠0;〠   \n〠38;5;39〠█〠0;〠  Callout with type of :info\n〠38;5;39〠█〠0;〠   ")))


(deftest
 gutter-callout-info-label
 (is
  (=
   (->
    (callout+
     {:theme :gutter,
      :data? true,
      :print-example-call? false,
      :type :info,
      :label "My custom label"})
    escape-sgr
    string/join)
   "〠38;5;39〠▆〠0;〠  My custom label     \n〠38;5;39〠█〠0;〠   \n〠38;5;39〠█〠0;〠  Callout with type of :info and custom :label\n〠38;5;39〠█〠0;〠   ")))


(deftest
 gutter-callout-warning
 (is
  (=
   (->
    (callout+
     {:theme :gutter,
      :data? true,
      :print-example-call? false,
      :type :warning})
    escape-sgr
    string/join)
   "〠38;5;172〠▆〠0;〠  WARNING     \n〠38;5;172〠█〠0;〠   \n〠38;5;172〠█〠0;〠  Callout with type of :warning\n〠38;5;172〠█〠0;〠  This is not a real warning\n〠38;5;172〠█〠0;〠   ")))


(deftest
 gutter-callout-error
 (is
  (=
   (->
    (callout+
     {:theme :gutter,
      :data? true,
      :print-example-call? false,
      :type :error})
    escape-sgr
    string/join)
   "〠38;5;196〠▆〠0;〠  ERROR     \n〠38;5;196〠█〠0;〠   \n〠38;5;196〠█〠0;〠  Callout with type of :error\n〠38;5;196〠█〠0;〠  This is not a real error\n〠38;5;196〠█〠0;〠   ")))


(deftest
 gutter-callout-positive-label
 (is
  (=
   (->
    (callout+
     {:theme :gutter,
      :data? true,
      :print-example-call? false,
      :colorway :positive,
      :label "SUCCESS!"})
    escape-sgr
    string/join)
   "〠38;5;40〠▆〠0;〠  SUCCESS!     \n〠38;5;40〠█〠0;〠   \n〠38;5;40〠█〠0;〠  Callout with colorway of :positive and custom :label\n〠38;5;40〠█〠0;〠   ")))
