(ns invasion.diff-test
  (:use midje.sweet
        invasion.diff))

(fact "Constants derivatives is zero"
  (differentiate 1 'x) => 0
  (differentiate -3.2 'x) => 0
  (fact "Other variables are constants"
    (differentiate 'y 'x) => 0))

(fact "Derivative of the sum (and substraction) is the sum of the derivatives"
  (differentiate '(+ 1 x) 'x) => '(+ 0 1)
  (differentiate '(+ x x y) 'x) => '(+ 1 1 0)
  (differentiate '(- x) 'x) => '(- 1))

(fact "Binarize n-ary (n > 2) expressions"
  (binarize '(+ x)) => '(+ x)
  (binarize '(+ x y)) => '(+ x y)
  (binarize '(+ x y z)) => '(+ x (+ y z))
  (binarize '(+ x y z w)) => '(+ x (+ y (+ z w))))

(fact "Derivative of f*g is f*g' + f'*g"
  (let [xy  '(* x y)
        xy' '(+ (* x 0) (* 1 y))]
    (differentiate '(* x y) 'x) => xy'
    (differentiate '(* 2 x y) 'x) => `(~'+ (~'* 2 ~xy') (~'* 0 ~xy))))

(fact "Simplify expressions"
  (simplify (differentiate '(* x y) 'x)) => 'y
  (simplify (differentiate '(* 2 x y) 'x)) => '(* 2 y))
