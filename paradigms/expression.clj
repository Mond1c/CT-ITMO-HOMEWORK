; :NOTE: arity check
(defn div
  ([] 1)
  ([arg] (/ 1 (double arg)))
  ([arg & args] (reduce (fn [a b] (/ (double a) (double b))) (conj args arg))))

(def constant constantly)

(defn variable [var-name]
  (fn [values] (values var-name)))

(defn operation [op]
  (fn [& args] (fn [values] (apply op (mapv (fn [value] (value values)) args)))))

(defn exp [x]
  (Math/exp x))

(defn log [x]
  (Math/log x))

(defn sumexp-calc [& args]
  (reduce + (mapv exp args)))

; :NOTE: comp
(defn lse-calc [& args]
  (apply (comp log sumexp-calc) args))

(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation div))
(def negate subtract)
(def sumexp (operation sumexp-calc))
(def lse (operation lse-calc))

(def operations
  {
   '+ add,
   '- subtract,
   '* multiply,
   '/ divide,
   'negate negate
   'sumexp sumexp
   'lse lse
  })

(defn parse [expr]
  (cond
    (number? expr) (constant expr)
    ; :NOTE: simplify
    (list? expr) (apply (operations (first expr)) (map parse (rest expr)))
    (symbol? expr) (variable (str expr))))

; :NOTE: comp
(defn parseFunction [expr]
  ((comp parse read-string) expr))
