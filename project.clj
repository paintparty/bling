(defproject io.github.paintparty/bling "0.8.3"
  :description "Rich text console printing for Clojure(Script)"
  :url "https://github.com/paintparty/bling"
  :license {:name "MIT"}
  :source-paths ["src"
                 ;; for local dev of fireworks and lasertag deps
                 ;; "../lasertag/src"
                ;;  "../fireworks/src"
                 ]
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [io.github.paintparty/fireworks "0.12.0"]
                 [metosin/malli "0.18.0"]]
  :repl-options {:init-ns bling.core}
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false}]])
