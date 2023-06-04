(defn vectorOfNumbers?
  [vec]
  (and
   (vector? vec)
   (every? number? vec)))

(defn haveEqualSize?
  [& colls]
  {:pre [(coll? colls)]}
  (apply = (mapv count colls)))

(defn creatorVectorCoordByCoordOperation
  [functorOnNumbers]
  (fn [& vecs]
    {:pre [(every? vectorOfNumbers? vecs), (apply haveEqualSize? vecs)]
     :post [(vector? %), (apply haveEqualSize? % vecs)]}
    (apply mapv functorOnNumbers vecs)))

(def v+ (creatorVectorCoordByCoordOperation +))
(def v- (creatorVectorCoordByCoordOperation -))
(def v* (creatorVectorCoordByCoordOperation *))
(def vd (creatorVectorCoordByCoordOperation /))

(defn v*s
  ([vec & scalars]
   {:pre [(vectorOfNumbers? vec), (every? number? scalars)]
    :post [(vector? %), (haveEqualSize? % vec)]}
   (mapv (fn [coord] (* coord (apply * scalars))) vec)))

(defn scalar
  [& vecs]
  {:pre [(haveEqualSize? vecs), (every? vectorOfNumbers? vecs)]
   :post [(number? %)]}
  (reduce + (apply v* vecs)))

(defn vect
  ([vec] vec)
  ([vec1 vec2]
   {:pre [(vectorOfNumbers? vec1), (vectorOfNumbers? vec2), (= (count vec1) (count vec2) 3)]
    :post [(vectorOfNumbers? %), (haveEqualSize? % vec1)]}
   (vector
    (- (* (get vec1 1) (get vec2 2)) (* (get vec1 2) (get vec2 1)))
    (- (* (get vec1 2) (get vec2 0)) (* (get vec1 0) (get vec2 2)))
    (- (* (get vec1 0) (get vec2 1)) (* (get vec1 1) (get vec2 0)))))
  ([vec1 vec2 & vecs]
   (apply vect (vect vec1 vec2) vecs)))

;; matrices

(defn matrix?
  [matrix]
  (and (vector? matrix)
       (> (count matrix) 0)
       (every? vectorOfNumbers? matrix)
       (apply haveEqualSize? matrix)))

(defn rows
  [matrix]
  {:pre [(matrix? matrix)]}
  (reduce (fn [answer curRow] (if (not= 0 (count curRow)) (inc answer) answer)) 0 matrix))

(defn cols
  [matrix]
  {:pre [(matrix? matrix)]}
  (count (peek matrix)))

(defn matricesHaveEqualSize?
  [& matrices]
  {:pre [(every? matrix? matrices)]
   :post [(boolean? %)]}
  (and (apply = (mapv rows matrices))
       (apply = (mapv cols matrices))))

(defn creatorMatrixCoordByCoordOperation
  [func]
  (fn [& matrices]
    {:pre [(every? matrix? matrices), (apply matricesHaveEqualSize? matrices)]
     :post [(matrix? %), (apply haveEqualSize? % matrices)]}
    (apply mapv (fn [& vectors] (apply mapv func vectors)) matrices)))

(def m+ (creatorMatrixCoordByCoordOperation +))
(def m- (creatorMatrixCoordByCoordOperation -))
(def m* (creatorMatrixCoordByCoordOperation *))
(def md (creatorMatrixCoordByCoordOperation /))

(defn transpose
  [matrix]
  {:pre [matrix? matrix]
   :post [(or (= % [])
              (and (matrix? %) (= (rows matrix) (cols %)), (= (cols matrix) (rows %))))]}
  (apply mapv vector matrix))

(defn m*s
  [matrix & scalars]
  {:pre [(matrix? matrix), (every? number? scalars)]
   :post [(matricesHaveEqualSize? % matrix)]}
  (mapv #(v*s % (apply * scalars)) matrix))

(defn m*v
  [matrix, vector]
  {:pre [(matrix? matrix), (vectorOfNumbers? vector), (= (count vector) (cols matrix))]
   :post [(vectorOfNumbers? %), (== (count %) (count matrix))]}
  (mapv (fn [m_vec] (scalar m_vec vector)) matrix))

(defn m*m
  ([left] left)
  ([left right]
   {:pre [(matrix? left), (matrix? right), (== (cols left) (rows right))]
    :post [(== (rows left) (rows %)), (== (cols right) (cols %))]}
   (mapv (fn [outer_vec] (m*v (transpose right) outer_vec)) left))
  ([left right & others] (apply m*m (m*m left right) others)))

;; Tensors

(defn tensor?
  [tens]
  (or (number? tens)
      (vectorOfNumbers? tens)
      (and (every? vector? tens) (apply haveEqualSize? tens) (tensor? (vec (apply concat tens))))))

(defn tensorEqualSize?
  [& tensors]
  {:pre [every? tensor? tensors]}
  (if (every? #(not (number? %)) tensors)
    (and (apply haveEqualSize? tensors) (recur (mapv first tensors)))
    (every? number? tensors)))

(defn tensorOp [functor]
  (fn [& tensors]
    {:pre [(every? tensor? tensors), (apply tensorEqualSize? tensors)],
     :post [(tensor? %), (apply tensorEqualSize? % tensors)]}
    (if (every? number? tensors)
      (apply functor tensors)
      (apply mapv (tensorOp functor) tensors))))

(def t+ (tensorOp +))
(def t- (tensorOp -))
(def t* (tensorOp *))
(def td (tensorOp /))
