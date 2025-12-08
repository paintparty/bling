;; This is for testing in node
(ns bling.script-test
  (:require [bling.visual-test]))

(defn main [& cli-args]
  (bling.visual-test/visual-test-suite))
