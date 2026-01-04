(ns clojure-university-project.expert-system
  (:require
      [clojure-university-project.data :as data]
      [clojure-university-project.data-transformations :as data-transformations]))

(defn rating-with-balanced-weights 
  "Calculates a weighted rating using balanced (not yet normalized) weights.

  The absolute values of weights don't matter, only their proportions.
  For example, weights {:data 8 :math 6} and {:data 80 :math 60} return the same result.
  The number of weights also doesn't matter (3, 4, 10, etc.).

  ratings:
    Map of raw ratings, e.g. {:data 8 :math 9}

  weights:
    Map of weights per key, e.g. {:data 16 :math 12}

  label:
    String label describing the rating,
    e.g. \"Backend development, systems engineering\"

  Returns:
    A string in the form \"<rating>rating-label-separator<label>\"

  Note:
    Normalization (to percentages so that they total to 100%) is done in a later step."
  [ratings weights label]
  (let [num (reduce-kv (fn [acc key value] (+ acc (* value (get ratings key)))) 0 weights)
        den (reduce + (vals weights))]
    (str (double (/ num den)) data/rating-label-separator label)))

(defn recommended-it-job-positions 
  [ratings]
  (mapv (fn [{:keys [label weights]}]
          (rating-with-balanced-weights ratings weights label))
        data/job-weight-sets))

(defn expert-system->ml-ratings-data-refactor
  [ratings]
  (update-vals data/expert-system->ml-mapping 
    #(data-transformations/average (keep ratings %))))