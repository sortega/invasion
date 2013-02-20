(ns invasion.acm)

(defn slurp-cases [reader]
  (let [lines  (line-seq reader)
        ncases (Integer/parseInt (first lines))]
    (->> lines
         rest
         (take ncases)
         doall)))

(defn format-cases [results]
  (apply str
         (map-indexed #(format "Case #%d: %s\n" (inc %) %2) 
                      results)))
