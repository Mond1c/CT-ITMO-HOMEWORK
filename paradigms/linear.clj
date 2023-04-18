(defn is-vector? [v] (and (vector? v) (every? number? v)))
(defn is-matrix? [m] (every? is-vector? m))
(defn is-valid-data? [args] (or (every? empty? args) (apply = (mapv (fn [x] (mapv number? x)) args))))
(defn is-tensor? [t] (or (number? t) (is-vector? t) (every? vector? t)))
(defn v-size-eq? [args] (every? (fn [v] (== (count (first args)) (count v))) args))
(defn m-size-eq? [args] (every? (fn [m] (and (== (count (first args)) (count m))
                                             (== (count (first (first args))) (count (first m))))) args))
(def t-size-eq? v-size-eq?)

(defn new-v-m-op [op]
  (fn [& args]
    {:pre [(and
            (every? vector? args)
            (is-valid-data? args)
            (or (v-size-eq? args)
                (m-size-eq? args)))]
     :post [(or
             (is-vector? %)
             (is-matrix? %))]}
    (apply mapv op args)))

(def v+ (new-v-m-op +))
(def v- (new-v-m-op -))
(def v* (new-v-m-op *))
(def vd (new-v-m-op /))

(defn scalar [& s] (apply + (apply v* s)))

(defn v*s [v & s]
  {:pre [(every? number? s)]
   :post [(or (number? %) (is-vector? %))]}
  (mapv (partial * (reduce * s)) v))

(def m+ (new-v-m-op v+))
(def m- (new-v-m-op v-))
(def m* (new-v-m-op v*))
(def md (new-v-m-op vd))


(defn m*s [m & s]
  {:pre [(and (is-matrix? m) (every? number? s))]
   :post [(is-matrix? %)]}
  (mapv
   (fn [v]
     (v*s v (reduce * s))) m))

(defn m*v [m v]
  {:pre [(and (is-matrix? m) (is-vector? v))]
   :post [(or (is-matrix? %) (is-vector? %))]}
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
          (is-valid-data? v))]
   :post [(is-vector? %)]}
  (reduce
   (fn [v1 v2]
     (let [[x y z] v1 m [[0 (- z) y] [z 0 (- x)] [(- y) x 0]]]
       (m*v m v2)))
   v))



(defn tensor-op [op]
  (letfn [(tensor' [& args]
                   {:pre [(or (every? number? args) (t-size-eq? args))]}
                   (if (every? number? args) (apply op args) (apply mapv tensor' args)))]
    (fn [& args] {:pre [(every? is-tensor? args)] :post [(is-tensor? %)]} (apply tensor' args))))

(def t+ (tensor-op +))
(def t- (tensor-op -))
(def t* (tensor-op *))
(def td (tensor-op /))