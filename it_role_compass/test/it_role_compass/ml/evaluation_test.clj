(ns it-role-compass.ml.evaluation-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ml.evaluation :as sut]
            [tech.v3.dataset :as ds]))

(fact "accuracy"
  (let [actual-dataset (ds/->dataset [{:role "DevOps"}
                               {:role "Backend"}
                               {:role "Frontend"}
                               {:role "Data Science"}])
        ;; 3 out of 4 match (DevOps, Backend, and Data Science)
        prediction-dataset (ds/->dataset [{:role "DevOps"}
                               {:role "Backend"}
                               {:role "QA"} 
                               {:role "Data Science"}])
        target :role
        result (sut/accuracy actual-dataset prediction-dataset target)]
    
    result => 0.75))