(ns clojure-university-project.utils)

(defn it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

