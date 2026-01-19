(ns it-role-compass.expert-system
  (:require
    [it-role-compass.data :as data]
    [it-role-compass.data-transformations :as data-transformations]))

(defn balance-weights 
  [ratings weights]
  (let [num (reduce-kv (fn [acc key value] (+ acc (* value (get ratings key)))) 0 weights)
        den (reduce + (vals weights))]
    (double (/ num den))))

(defn recommended-it-job-position-weights
  [ratings]
  (into {}
        (map (fn [{:keys [label weights]}]
               [label (balance-weights ratings weights)])
                data/job-weight-sets)))

(defn expert-system->ml-ratings-data-refactor
  [ratings]
  (update-vals data/expert-system->ml-mapping 
    #(data-transformations/average (keep ratings %))))