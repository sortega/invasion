(ns invasion.diff
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))

;; Derive

(defn expression-type [exp _]
  (cond
    (number? exp) :number
    (symbol? exp) :variable
    (list? exp)   (first exp)
    :else         :default))

(defmulti differentiate
  "Protocol for differentiable expressions"
  expression-type)

(defmethod differentiate :default [exp x]
  (throw 
    (Exception. 
      (format "Don't know how to differentiate '%s' by '%s'" exp x))))

(defmethod differentiate :number [_ _] 0)

(defmethod differentiate :variable [exp x]
  (if (= exp x) 1 0))

(defn linear-differentiate [[op & ops] x]
  `(~op ~@(map #(differentiate % x) ops)))
(defmethod differentiate '+ [exp x]
  (linear-differentiate exp x))
(defmethod differentiate '- [exp x]
  (linear-differentiate exp x))

(defn product-differentiate [m1 m2 x]
  (let [m1' (differentiate m1 'x)
        m2' (differentiate m2 'x)]
    (list '+
          (list '* m1 m2')
          (list '* m1' m2))))

(defn binarize [[operator & operands :as exp]]
  (if (< (count operands) 3)
    exp
    (let [[op1 & others] operands]
      (list operator op1
            (binarize (apply list operator others))))))

(defmethod differentiate '* [exp x]
  (let [[_ op1 op2] (if (> (count exp) 3) 
                      (binarize exp)
                      exp)]
    (product-differentiate op1 op2 x)))

;; Simplify

(defn mapo [rel xs ys]
  (conde
    [(fresh [x rx y ry]
            (conso x rx xs)
            (conso y ry ys)
            (rel x y)
            (mapo rel rx ry))]
    [(== xs ys) (== xs '())]))

(declare simplifyo)

(defn simplify-opso [exp exp']
  (fresh [op ops ops'] 
         (conso op ops exp) 
         (mapo simplifyo ops ops') 
         (conso op ops' exp')))

(defn simplify-0-times [exp exp']
  (fresh [operands]
         (conso '* operands exp)
         (membero 0 operands) (== exp' 0)))

(defn filtero [x l l']
  (conde
    [(emptyo l) (emptyo l')]
    [(fresh [head tail tail']
            (conso head tail l)
            (filtero x tail tail')
            (conde
              [(== head x) (== l' tail')]
              [(conso head tail' l')]))])) 

(defn simplify-identity-element [exp exp']
  (fresh [identity operator operands filtered-operands]
         (conso operator operands exp)
         (condu
           [(== operator '+) (== identity 0)]
           [(== operator '*) (== identity 1)])
         (membero identity operands)
         (filtero identity operands filtered-operands)
         (conso operator filtered-operands exp')))

(defn simplify-unary [exp exp']
  (fresh [operator operand]
    (== [operator exp'] exp)
    (membero operator ['* '+])))

(defn simplifyo
  "A relation where exp is an expression and exp' is a simplification
   or exp (or the same exp)"
  [exp exp']
  (fresh [intermediate-exp]

         ;; Simplify arguments when composed
         (condu
           [(fresh [operator operands simplified-operands]
                   (conso operator operands exp)
                   (mapo simplifyo operands simplified-operands)
                   (conso operator simplified-operands intermediate-exp))]
           [(== intermediate-exp exp)])

         ;; Apply rules
         (condu
           
           [(simplify-0-times intermediate-exp exp')]
           [(simplify-identity-element intermediate-exp exp')]
           [(simplify-unary intermediate-exp exp')]

           ;; Fallback
           [(== intermediate-exp exp')])))

(defn simplify [exp]
  (letfn [(simplify-step [exp]
            (first
              (run 1 [q] (simplifyo exp q))))]
    (->> (iterate simplify-step exp)
      (partition 2 1)
      (drop-while #(apply not= %))
      ffirst)))
