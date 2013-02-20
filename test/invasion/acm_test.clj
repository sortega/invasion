(ns invasion.acm-test
  (:use midje.sweet
        invasion.acm)
  (:import [java.io BufferedReader StringReader]))

(fact "Slurp cases into memory"
  (slurp-cases (BufferedReader. (StringReader. "3\na\nb\nc\n"))) =>
  ["a" "b" "c"])

(fact "Format case outputs"
  (format-cases [1 "b" :c]) => (str "Case #1: 1\n"
                                    "Case #2: b\n"
                                    "Case #3: :c\n"))
