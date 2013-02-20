(defproject invasion "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :profiles {:dev {:dependencies [[midje "1.4.0-beta1"]
                                  [com.stuartsierra/lazytest "1.2.3"]]}}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.logic "0.7.5"]]
  :repositories {"stuart" "http://stuartsierra.com/maven2"}
  :main invasion.main)
