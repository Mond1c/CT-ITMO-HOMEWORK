(require 'clojure.math)

(defn improvedDivision
  ([denom] (/ 1.0 denom))
  ([num & denoms] (/ (double num) (apply * denoms))))

(def constant constantly)

(defn variable [name]
  (fn [vars] (vars name)))

(defn opCreator [functor]
  (fn [& args]
    (fn [vars]
      (apply functor (mapv #(% vars) args)))))

(def add (opCreator +))
(def subtract (opCreator -))
(def multiply (opCreator *))
(def divide (opCreator improvedDivision))

(def negate (opCreator -))

(defn sumExpImpl [& args] (apply + (mapv clojure.math/exp args)))
(def sumexp (opCreator sumExpImpl))

(def lse (opCreator (comp clojure.math/log sumExpImpl)))

(def mapOfOperators
  {'+      add, '-, subtract,
   '/      divide, '* multiply,
   'negate negate,
   'sumexp sumexp, 'lse lse})

(defn parseClojureToken [token]
  (cond
    (list? token) (apply (mapOfOperators (first token)) (mapv parseClojureToken (rest token)))
    (number? token) (constant token)
    (symbol? token) (variable (str token))))

(def parseFunction (comp parseClojureToken read-string))

;;;============================HW 11==============================;;;
(definterface IExpression
  (^Number evaluate [vars])
  (^user.IExpression diff [varName])
  (^String toStringInfix []))

(defn evaluate [expression vars] (.evaluate expression vars))
(defn toString [expression] (.toString expression))
(defn toStringInfix [expression] (.toStringInfix expression))
(defn diff [expression varName] (.diff expression varName))

(declare ZERO_CONSTANT ONE_CONSTANT MINUS_ONE_CONSTANT
         Add Subtract
         Multiply Divide
         Negate
         Meansq RMS)

(deftype ConstantClass [number]
  IExpression
  (evaluate [this vars] number)
  (diff [this varName] ZERO_CONSTANT)
  (toStringInfix [this] (str number))
  Object
  (toString [this] (str number)))

(def getFirstchar (comp clojure.string/lower-case str first))
(deftype VariableClass [name]
  IExpression
  (evaluate [this vars] (vars (getFirstchar name)))
  (diff [this varName] (if (= varName (getFirstchar name)) ONE_CONSTANT ZERO_CONSTANT))
  (toStringInfix [this] name)
  Object
  (toString [this] name))

(defn Constant [number] (ConstantClass. number))
(defn Variable [name] (VariableClass. name))

(def ONE_CONSTANT (Constant 1))
(def ZERO_CONSTANT (Constant 0))
(def MINUS_ONE_CONSTANT (Constant -1))

(defmacro creatorOperator [constructorName stringRepresentation functor diffCoefficientsFunctor]
  (let [className (symbol (str constructorName "Class"))]
    `(do
       (deftype ~className [args#]
         IExpression
         (evaluate [this vars#] (apply ~functor (mapv #(.evaluate % vars#) args#)))
         (diff [this varName#] (apply Add
                                      (keep-indexed
                                       (fn [index# argument#]
                                         (Multiply (diff argument# varName#)
                                                   (apply ~diffCoefficientsFunctor index# args#)))
                                       args#)))
         (toStringInfix [this] (if (= (count args#) 2)
                                 (str "(" (toStringInfix (first args#)) " " ~stringRepresentation " " (toStringInfix (second args#)) ")")
                                 (str ~stringRepresentation " " (toStringInfix (first args#)))))
         Object
         (toString [this] (str "(" ~stringRepresentation " " (clojure.string/join " " args#) ")")))

       (defn ~constructorName [& args#] (new ~className args#)))))


(creatorOperator Add "+" +
                 (fn [index & arguments] ONE_CONSTANT))

(creatorOperator Subtract "-" -
                 (fn [index & arguments]
                   (if
                    (and (= index 0) (> (count arguments) 1))
                     ONE_CONSTANT
                     MINUS_ONE_CONSTANT)))

(creatorOperator Negate "negate" -
                 (fn [index & arguments] MINUS_ONE_CONSTANT))

(defn removeNth
  "Return new sequence without nth element."
  [seq_ n] (concat (take n seq_) (drop (inc n) seq_)))

(creatorOperator Multiply "*" *
                 (fn [index & arguments] (apply Multiply (removeNth arguments index))))
;; (fgh)' = gh * f' + fh * g' + fg * h'

(creatorOperator Divide "/" improvedDivision
                 (fn
                   ([index denom] (Negate (Divide ONE_CONSTANT (Multiply denom denom))))
                   ([index num & denoms]
                    (if (== index 0)
                      (apply Divide ONE_CONSTANT denoms)
                      (Negate (apply Divide num (nth denoms (dec index)) denoms))))))
;; (f/ghk)' = 1/(ghk) * f' - f/(ghk * g) * g' - f/(ghk * h) * h' - f/(ghk * k) * k'

(defn MeansqImpl [& arguments] (/ (apply + (map #(* % %) arguments)) (count arguments)))

(creatorOperator Meansq "meansq" MeansqImpl
                 (fn [index & arguments] (Divide (Multiply (nth arguments index) (Constant 2)) (Constant (count arguments)))))

(creatorOperator RMS "rms" (comp clojure.math/sqrt MeansqImpl)
                 (fn [index & arguments]
                   (Divide (nth arguments index)
                           (Multiply (Constant (count arguments)) (apply RMS arguments)))))

(def mapOfClasses
  {'+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'negate Negate
   'meansq Meansq
   'rms RMS})

(defn parseObjectToken [token]
  (cond
    (list? token) (apply (mapOfClasses (first token)) (mapv parseObjectToken (rest token)))
    (number? token) (Constant token)
    (symbol? token) (Variable (str token))))

(def parseObject (comp parseObjectToken read-string))

; ================== HW 12 =============== ;

;* Аргументы: число больше 0 → `true`, иначе → `false`
;        * Результат: `true` → 1, `false` → 0
;* `Not` (`!`) - отрицание: `!5` равно 0
;          * `And` (`&&`) – и: `5 & -6` равно 0
;                    * `Or`  (`||`) - или: `5 & -6` равно 1
;                              * `Xor` (`^^`) - исключающее или: `5 ^ -6` равно 1

(defn toBoolean [arg] (if (> arg 0) 1 0))
(creatorOperator Not "!"  (fn [arg] (if (= (toBoolean arg) 0) 1 0)) #())
(creatorOperator And "&&" (fn [arg1 arg2] (if (= (* (toBoolean arg1) (toBoolean arg2)) 0) 0 1)) #())
(creatorOperator Or "||" (fn [arg1 arg2] (if (= (+ (toBoolean arg1) (toBoolean arg2)) 0) 0 1)) #())
(creatorOperator Xor "^^" (fn [arg1 arg2] (if (= (+ (toBoolean arg1) (toBoolean arg2)) 1) 1 0)) #())

(def mapOfClassesHW12
  {'+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'negate Negate
   '!      Not
   '&&     And
   '||     Or
   (symbol "^^")     Xor
   })

(load-file "parser.clj")

(def +concatSeqs (partial +seqf concat))
(def *all-chars (mapv char (range 0 128)))
(defn *chars [p] (+char (apply str (filter p *all-chars))))
(def *digit (*chars #(Character/isDigit %)))
(def *letter (*chars #(Character/isLetter %)))
(def *space (*chars #(Character/isWhitespace %)))
(def *ws (+ignore (+star *space)))

(def *number (+map read-string (+str (+concatSeqs
                                      (+seq (+opt (+char "-")))
                                      (+plus *digit)
                                      (+seq (+opt (+char ".")))
                                      (+star *digit)))))
(def *Constant (+map Constant *number))
(def *parseVariable (+str (+plus (+char "xyzXYZ"))))
(def *Variable (+map Variable *parseVariable))
(defn *word [wordRepr] (apply +seq (map (comp +char str) (str wordRepr))))
(defn *parseOperator [stringRepr] (+map (constantly (get mapOfClassesHW12 (symbol stringRepr))) (*word stringRepr)))
(defn *parseOperators [& stringReprs] (apply +or (mapv *parseOperator stringReprs)))

(declare *parsePrimitive, *parseAddSub, *parseMulDiv, *parseXOR, *parseOR, *parseAND)
(defn *create-unaries [& stringReprs] (+map (fn [[op arg]] (op arg))
                                            (+seq *ws (apply (comp +or *parseOperators) stringReprs) *ws (delay *parsePrimitive))))
(def *parseUnary (*create-unaries "negate" "!"))

(defn *sequenceOfOperators [next-parser & currents]
  (let [currentsParser (apply (comp +or *parseOperators) currents)]
    (+seqf (partial apply concat)
           *ws (+seq next-parser) *ws
           (+star (+seq currentsParser *ws next-parser *ws)))))

(defn *fromListToTree [args]
  (let [[head & tail] args]
    (reduce (fn [left [oper right]] (oper left right)) head (partition 2 tail))))

(defn *BinaryOperationCreator [next-parser & currents]
  (+map (partial *fromListToTree)
        (apply *sequenceOfOperators next-parser currents)))

(def *parsePrimitive (+or *Constant
                          *Variable
                          *parseUnary
                          (+seqn 1 (+char "(") (delay *parseXOR) (+char ")"))))
(def *parseUnary (+map (fn [[op arg]] (op arg)) (+seq (*parseOperator "negate") *ws *parsePrimitive)))
(def *parseMulDiv (*BinaryOperationCreator *parsePrimitive "*" "/"))
(def *parseAddSub (*BinaryOperationCreator *parseMulDiv "+" "-"))
(def *parseAND (*BinaryOperationCreator *parseAddSub "&&"))
(def *parseOR (*BinaryOperationCreator *parseAND "||"))
(def *parseXOR (*BinaryOperationCreator *parseOR "^^"))

(def parseObjectInfix (+parser *parseXOR))
