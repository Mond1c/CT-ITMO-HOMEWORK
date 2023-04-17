(defn is-vector? [v] (and (vector? v) (or (every? number? v))))
(defn is-matrix? [m] (and (not (empty? (filter vector? m))) (not (number? m))))
(defn is-valid-data? [args] (or (every? empty? args) (apply = (mapv (fn [x] (mapv number? x)) args))))
(defn is-tensor? [t] (or (number? t) (is-vector? t) (is-matrix? t)))

(defn new-v-m-op [op]
  (fn [& args]
    {:pre [(and
            (every? vector? args)
            (is-valid-data? args))]
     :post [(and
             (every? vector? args)
             (is-valid-data? args))]}
    (apply mapv op args)))

(def v+ (new-v-m-op +))
(def v- (new-v-m-op -))
(def v* (new-v-m-op *))
(def vd (new-v-m-op /))

(defn scalar [& s] (apply + (apply v* s)))

(defn v*s [v & s]
  {:pre [(every? number? s)]}
  (mapv (partial * (reduce * s)) v))

(def m+ (new-v-m-op v+))
(def m- (new-v-m-op v-))
(def m* (new-v-m-op v*))
(def md (new-v-m-op vd))


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


(defn compatible? [m1 m2]
  (and (is-matrix? m1) (is-matrix? m2) (== (count (first m1)) (count m2))))

(defn transpose 
  [m]
  {:pre [(is-matrix? m)]
   :post [(or (== (count %) 0) (and (compatible? % m) (compatible? m %)))]}
  (apply mapv vector m))

(defn m*m ([& m] 
           (reduce
            (fn [m1 m2]
              {:pre [(compatible? m1 m2)]
               :post [(and (== (count m1) (count %))
                           (== (count (first m2)) (count (first %))))]}
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

(defn t-size-eq? [args] (every? (fn [x] (== (count (first args)) (count x))) args))

(defn tensor-op [op]
  (letfn [(tensor' [& args]
                   {:pre [(or (every? number? args) (t-size-eq? args))]}
                   (if (every? number? args) (apply op args) (apply mapv tensor' args)))]
    (fn [& args] {:pre [(every? is-tensor? args)] :post [(is-tensor? %)]} (apply tensor' args))))

(def t+ (tensor-op +))
(def t- (tensor-op -))
(def t* (tensor-op *))
(def td (tensor-op /))