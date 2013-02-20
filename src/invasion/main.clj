(ns invasion.main
  (:require [clojure.java.io :as io])
  (:use invasion.core
        invasion.acm))

(defn -main [input]
  (with-open [r (io/reader input)]
    (print
      (format-cases
        (for [symbols (slurp-cases r)]
          (min-value symbols))))))

