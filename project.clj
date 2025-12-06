(defproject io.github.paintparty/bling "0.8.8"
  :description "Hi-fidelity console printing"
  :url "https://github.com/paintparty/bling"
  :license {:name "MIT"}
  :source-paths ["src"
                 ;; for local dev of fireworks and lasertag deps
                 ;; "../lasertag/src"
                  "../fireworks/src"
                 ]
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [io.github.paintparty/fireworks "0.12.6"]
                 [com.taoensso/tufte "3.0.0"]
                 [org.clj-commons/pretty "3.6.7"]
                 [metosin/malli "0.18.0"]]
  :repl-options {:init-ns bling.core}
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false}]])
