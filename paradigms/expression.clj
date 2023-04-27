; :NOTE: arity check
(defn div
  ([arg] (/ 1 (double arg)))
  ; :NOTE: [a b]
  ([a b] (/ (double a) (double b)))
  ([a b & args] (reduce div (div a b) args)))

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

; :NOTE: simplify
(def lse-calc (comp log sumexp-calc))

(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation div))
(def negate subtract)
(def sumexp (operation sumexp-calc))
(def lse (operation lse-calc))

(def operations
  {'+ add,
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
    (list? expr) (apply (operations (first expr)) (map parse (rest expr)))
    (symbol? expr) (variable (name expr))))

; :NOTE: simplify
(def parseFunction (comp parse read-string))
