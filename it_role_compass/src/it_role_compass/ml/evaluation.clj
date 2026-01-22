(ns it-role-compass.ml.evaluation
  (:require
    [tech.v3.dataset :as ds]))

(defn accuracy 
  [actual-dataset prediction-dataset target-column]
  (let [y-true (ds/column actual-dataset target-column)
        y-pred (take (count y-true) (ds/column prediction-dataset target-column))]
    (double (/ (count (filter true? (map = y-true y-pred)))
               (count y-true)))))