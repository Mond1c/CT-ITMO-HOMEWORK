(defn div
      ([arg] (/ 1 (double arg)))
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

(def lse-calc (comp log sumexp-calc))

(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation div))
(def negate subtract)
(def sumexp (operation sumexp-calc))
(def lse (operation lse-calc))

(def funcOperations
  {'+      add,
   '-      subtract,
   '*      multiply,
   '/      divide,
   'negate negate
   'sumexp sumexp
   'lse    lse
   })

(defn parse [expr operationsMap constantFunc variableFunc]
      (cond
        (number? expr) (constantFunc expr)
        (list? expr) (apply (operationsMap (first expr)) (map (fn [token] (parse token operationsMap constantFunc variableFunc)) (rest expr)))
        (symbol? expr) (variableFunc (name expr))))

(defn parseFunction [expr]
      (parse (read-string expr) funcOperations constant variable))

(defn proto-get
      "Returns object property respecting the prototype chain"
      ([obj key] (proto-get obj key nil))
      ([obj key default]
       (cond
         (contains? obj key) (obj key)
         (contains? obj :prototype) (proto-get (obj :prototype) key default)
         :else default)))

(defn proto-call
      "Calls object method respecting the prototype chain"
      [this key & args]
      (apply (proto-get this key) this args))
(defn field
      "Creates field"
      [key] (fn
              ([this] (proto-get this key))
              ([this def] (proto-get this key def))))

(defn method
      "Creates method"
      [key] (fn [this & args] (apply proto-call this key args)))

(defn constructor
      "Defines constructor"
      [ctor prototype]
      (fn [& args] (apply ctor {:prototype prototype} args)))


(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))

(defn PartOfExpression [evaluate diff toString]
      {:evaluate evaluate :diff diff :toString toString})

(def _x (field :x))
(declare ZERO)
(def Constant
  (constructor
    (fn [this x] (assoc this :x x))
    (PartOfExpression
      (fn [this vars] (_x this))
      (fn [this varName] ZERO)
      (fn [this] (str (_x this))))))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))
(def _name (field :name))
(def _fullName (field :fullName))
(def Variable
  (constructor
    (fn [this varName] (assoc this :name (str varName) :fullName (str (Character/toLowerCase (first varName)))))
    (PartOfExpression
      (fn [this vars] (get vars (_name this)))
      (fn [this varName] (if (= (_name this) varName) ONE ZERO))
      (fn [this] (_fullName this)))))

(def _operationName (field :operationName))
(def _calculate (field :calculate))
(def _diffOperation (field :diffOperation))
(def _args (field :args))
(def Operation
  (constructor
    (fn [this operationName calculate diffOperation] (assoc this :operationName operationName :calculate calculate :diffOperation diffOperation))
    (PartOfExpression
      (fn [this vars] (apply (_calculate this) (mapv (fn [x] (evaluate x vars)) (_args this))))
      (fn [this varName] ((_diffOperation this) (_args this) (mapv (fn [x] (diff x varName)) (_args this)) varName))
      (fn [this] (str "(" (_operationName this) " " (clojure.string/join " " (mapv (fn [x] (toString x)) (_args this))) ")")))))

(defn newOperation [operationName calculate diffOperation]
      (constructor
        (fn [this & args] (assoc this :args args))
        (Operation operationName calculate diffOperation)))

(def Add
  (newOperation
    "+"
    +'
    (fn [f f' varName] (apply Add f'))))

(def Subtract
  (newOperation
    "-"
    -'
    (fn [f f' varName] (apply Subtract f'))))

(def Multiply
  (newOperation
    "*"
    *'
    (fn [f f' varName]
        (second (reduce (fn [[x x'] [y y']] [(Multiply x y) (Add (Multiply x' y) (Multiply x y'))])
                        (mapv vector f f'))))))

(def Negate
  (newOperation
    "negate"
    -
    (fn [f f' varName] (apply Negate f'))))


(def Divide
  (newOperation
    "/"
    div
    (fn [f f' varName]
        (if (= (count f) 1)
          (Divide (Negate (first f')) (Multiply (first f) (first f)))
          (second (reduce
                    (fn [[x x'] [y y']] [(Divide x y) (Divide (Subtract (Multiply x' y) (Multiply x y')) (Multiply y y))])
                    (mapv vector f f')))))))

(defn meansq-calc [& args] (/ (reduce + (mapv (fn [x] (* x x )) args)) (count args)))

(defn meansq-diff [f f' varName]
      (Divide (apply Add (mapv (fn [x] (diff (Multiply x x) varName)) f))
              (Constant (count f))))
(def Meansq
  (newOperation
    "meansq"
    meansq-calc
    meansq-diff))

(def RMS
  (newOperation
    "rms"
    (fn [& args] (Math/sqrt (/ (reduce + (map (fn [x] (* x x)) args)) (count args))))
    (fn [f f' varName] (Divide (meansq-diff f f' varName) (Multiply TWO (apply RMS f))))))
(def operationObjects
  {'+      Add,
   '-      Subtract,
   '*      Multiply,
   '/      Divide,
   'negate Negate
   'meansq Meansq
   'rms RMS
   })

(defn parseObject [expr] (parse (read-string expr) operationObjects Constant Variable))


(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(def _empty (partial partial -return))

(defn _char [p]
      (fn [[c & cs]]
          (if (and c (p c))
            (-return c cs))))

(defn _map [f result]
      (if (-valid? result)
        (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
      (fn [input]
          (let [ar (a input)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar)))))))

(defn _either [a b]
      (fn [input]
          (let [ar (a input)]
               (if (-valid? ar)
                 ar
                 ((force b) input)))))

(defn _parser [parser]
      (let [pp (_combine (fn [v _] v) parser (_char #{\u0000}))]
           (fn [input] (-value (pp (str input \u0000))))))

(defn +char [chars]
      (_char (set chars)))
(defn +map [f parser]
      (comp (partial _map f) parser))

(def +ignore
  (partial +map (constantly 'ignore)))

(defn- iconj [coll value]
       (if (= value 'ignore)
         coll
         (conj coll value)))
(defn +seq [& parsers]
      (reduce (partial _combine iconj) (_empty []) parsers))

(defn +seqf [f & parsers]
      (+map (partial apply f) (apply +seq parsers)))

(defn +seqn [n & parsers]
      (apply +seqf #(nth %& n) parsers))

(defn +or [parser & parsers]
      (reduce _either parser parsers))

(defn +opt [parser]
      (+or parser (_empty nil)))

(defn +star [parser]
      (letfn [(rec [] (+or (+seqf cons parser (delay (rec))) (_empty ())))]
             (rec)))

(defn +plus [parser]
      (+seqf cons parser (+star parser)))

(defn +str [parser]
      (+map (partial apply str) parser))

(def *all-chars (mapv char (range 32 128)))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+plus *digit))))
