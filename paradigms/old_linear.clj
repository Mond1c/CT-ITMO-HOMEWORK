(defn is-vector? [checker v] (and (vector? v) (every? checker v)))
(defn is-matrix? [m] (and (not (empty? (filter vector? m))) (not (number? m))))
(defn is-valid-data? [args] (or (every? empty? args) (apply = (mapv (fn [x] (mapv number? x)) args))))

(defn coord [op]
  (fn [& args] 
  {:pre [(and
          (every? vector? args)
          (is-valid-data? args))]}
    (apply mapv op args)))

(def v+ (coord +))
(def v- (coord -))
(def v* (coord *))
(def vd (coord /))

(defn scalar [& s] (apply + (apply v* s)))

(defn v*s [v & s]
  {:pre [(every? number? s)]}
  (mapv (partial * (reduce * s)) v))

(def m+ (coord v+))
(def m- (coord v-))
(def m* (coord v*))
(def md (coord vd))


(defn m*s [m & s]
  {:pre [(and (is-matrix? m) (every? number? s))]}
  (mapv
    (fn [v]
      (v*s v (reduce * s))) m))

(defn m*v [m v]
  {:pre [(is-matrix? m)]}
  (mapv
    (fn [s]
      (scalar s v)) m))

(defn transpose
  [m]
  (apply mapv vector m))


(defn m*m ([& m]
  {:pre [(if (<= (count m) 1)
           (or 
            (every? vector? m)
            (every? empty? m))
           (and
            (every? vector? m)
            (if (is-vector? number? (second m))
              (or (every? empty? m) (== (count (first m)) (count (second m))))
              (or (every? empty? m) (== (count (first (first m))) (count (second m)))))))]}         
  (reduce
    (fn [m1 m2]
      (mapv
        (fn [v] (m*v (transpose m2) v)) m1)) m)))

(defn vect
  [& v]
  {:pre [(and
          (every? vector? v)
          (is-valid-data? v))]}
  (reduce
    (fn [a b]
      (let [[x y z] a matrix [[0 (- z) y] [z 0 (- x)] [(- y) x 0]]]
        (m*v matrix b)))
    v))



(defn operation-precond [size-equals? args] (or (every? number? args) (size-equals? args)))

(defn create-operation [isType? size-equals?]
  (fn [operation]
    (letfn [(tensor-calc' [& args]
              {:pre [(operation-precond size-equals? args)]}
              (if (every? number? args) (apply operation args) (apply mapv tensor-calc' args)))]
      (fn [& args] {:pre [(every? isType? args)] :post [(isType? %)]} (apply tensor-calc' args)))))

(defn vsize-equals? [args] (every? (fn [x] (== (count (first args)) (count x))) args))
(def msize-equals? vsize-equals?)
(def tsize-equals? msize-equals?)
(defn isTensor? [t] (or (number? t)
                        (every? number? t) (and (vsize-equals? t)
                                                (isTensor? (reduce #(apply conj %1 %2) t)))))


(def t-operation (create-operation isTensor? tsize-equals?))
(def t+ (t-operation +))
(def t- (t-operation -))
(def t* (t-operation *))
(def td (t-operation /))
