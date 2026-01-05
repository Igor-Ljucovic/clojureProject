(ns clojure-university-project.expert-system
  (:require
    [clojure-university-project.data :as data]
    [clojure-university-project.data-transformations :as data-transformations]))

(defn balance-weights 
  [ratings weights label]
  (let [num (reduce-kv (fn [acc key value] (+ acc (* value (get ratings key)))) 0 weights)
        den (reduce + (vals weights))]
    (double (/ num den))))

(defn recommended-it-job-positions 
  [ratings]
  (into {}
        (map (fn [{:keys [label weights]}]
               [label (balance-weights ratings weights label)])
                data/job-weight-sets)))

(defn expert-system->ml-ratings-data-refactor
  [ratings]
  (update-vals data/expert-system->ml-mapping 
    #(data-transformations/average (keep ratings %))))