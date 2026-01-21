(ns it-role-compass.ml.evaluation
  (:require
    [tech.v3.dataset :as ds]))

(defn calculate-accuracy 
  [test-dataset prediction-dataset target-column]
  (let [y-true (ds/column test-dataset target-column)
        y-pred (take (count y-true) (ds/column prediction-dataset target-column))]
    (/ (count (filter true? (map = y-true y-pred)))
       (count y-true))))