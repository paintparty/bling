(defproject io.github.paintparty/bling "0.10.0"
  :description "Hi-fidelity console printing"
  :url "https://github.com/paintparty/bling"
  :license {:name "MIT"}
  :source-paths ["src"
                 ;; for local dev of fireworks and lasertag deps
                ;;  "../lasertag/src"
                ;;  "../fireworks/src"
                 ]
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.github.paintparty/fireworks "0.20.0"]
                 [metosin/malli "0.20.0"]
                 ]
  :repl-options {:init-ns bling.core}
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false}]])

                  