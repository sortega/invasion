(ns invasion.core)

(defn to-decimal [base digits]
  (reduce (fn [accum digit]
            (+ digit (* base accum)))
          digits))

(defn lowest-symbol-mapping [unique-symbols]
  (zipmap unique-symbols
          (cons 1 (cons 0 (iterate inc 2)))))

(defn min-value 
  ([symbols]
   (let [unique-symbols (distinct symbols)
         base           (max 2 (count unique-symbols))
         mapping        (lowest-symbol-mapping unique-symbols)
         number         (map mapping symbols)]
     (to-decimal base number))))
