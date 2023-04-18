(defn div [& args]
  (if (== 1 (count args))
    (/ 1 (double (first args)))
    (reduce (fn [a b] (/ (double a) (double b))) args)))

(def constant constantly)

(defn variable [var-name]
  (fn [values] (values var-name)))

(defn operation [op]
  (fn [& args] (fn [values] (apply op (mapv (fn [value] (value values)) args)))))

(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation div))
(def negate subtract)

(def operations
  {
   '+ add,
   '- subtract,
   '* multiply,
   '/ divide,
   'negate negate
  })

(defn parse [expr]
  (cond
    (number? expr) (constant expr)
    (list? expr) (apply (operations (first expr)) (map (fn [token] (parse token)) (rest expr)))
    (symbol? expr) (variable (str expr))))

(defn parseFunction [expr]
  (parse (read-string expr)))