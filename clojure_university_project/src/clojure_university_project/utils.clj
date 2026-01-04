(ns clojure-university-project.utils)

(defn it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn random-ratings
  [n min-val max-val]
  (vec
    (repeatedly n
      #(let [rand-num (+ min-val (* (rand) (- max-val min-val)))] 
       (/ (Math/round (* 100 rand-num)) 100.0)))))

