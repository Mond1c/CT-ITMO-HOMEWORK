(defn is-vector? [v] (and (vector? v) (every? number? v)))

(defn coord [op]
  (fn [& args] (apply mapv op args)))

(def v+ (coord +))
(def v- (coord -))
(def v* (coord *))
(def vd (coord /))

(defn scalar [& s] (apply + (apply v* s)))

(defn v*s [v & s]
  (mapv (partial * (reduce * s)) v))

(def m+ (coord v+))
(def m- (coord v-))
(def m* (coord v*))
(def md (coord vd))


(defn m*s [m & s]
  (mapv
    (fn [v]
      (v*s v (reduce * s))) m))

(defn m*v [m v]
  (mapv
    (fn [s]
      (scalar s v)) m))

(defn transpose
  [m]
  (apply mapv vector m))


(defn m*m ([& m]
  (reduce
    (fn [m1 m2]
      (mapv
        (fn [v] (m*v (transpose m2) v)) m1)) m)))

(defn vect
  [& v]
  (reduce
    (fn [a b]
      (let [[x y z] a matrix [[0 (- z) y] [z 0 (- x)] [(- y) x 0]]]
        (m*v matrix b)))
    v))
