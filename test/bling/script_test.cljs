;; This is for testing in node
(ns bling.script-test
  (:require [bling.core-test]))

(defn main [& cli-args]
  (bling.core-test/visual-test-suite))
