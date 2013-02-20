(ns invasion.core-test
  (:use clojure.test
        midje.sweet
        invasion.core))

(fact "Min possible value"
  (min-value "11001001") => 201
  (provided
    (lowest-symbol-mapping [\1 \0]) => {\1 1, \0 0}
    (to-decimal 2 [1 1 0 0 1 0 0 1]) => 201))

(fact "Convert to decimal"
  (to-decimal 2 [1 1 0 0 1 0 0 1]) => 201
  (to-decimal 3 [1 1 0 0 1 0 0 1]) => 2944
  (to-decimal 4 [1 1 0 0 1 0 0 1]) => 20545)

(fact "Lowest symbol mapping"
  (lowest-symbol-mapping [\1 \0]) => {\1 1, \0 0}
  (lowest-symbol-mapping (seq "abcd")) => {\a 1, \b 0, \c 2, \d 3})

(fact "Integrated tests"
  (min-value "11001001") => 201
  (min-value "cats") => 75
  (min-value "zig") => 11)
