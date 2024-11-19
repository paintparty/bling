(defproject io.github.paintparty/bling "0.4.1"
  :description "Rich text console printing for Clojure(Script)"
  :url "https://github.com/paintparty/bling"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :repl-options {:init-ns bling.core}
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false}]])
