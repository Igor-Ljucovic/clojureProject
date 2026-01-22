(ns it-role-compass.ml.inference
  (:require
    [tech.v3.dataset :as ds]
    
    [it-role-compass.utils :as utils]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.pipeline :as pipe]
    [it-role-compass.ml.model :as model]))

(defn- require-smile! 
  []
  (utils/silently (require 'scicloj.ml.smile.classification)))

(defn rank-predicted-probabilities 
  [reference-dataset sample-dataset model-pipeline fit-context labels]
  (let [prediction-dataset (pipe/run-pipeline (ds/concat reference-dataset sample-dataset) model-pipeline fit-context)
        new-row            (ds/row-at prediction-dataset (dec (ds/row-count prediction-dataset)))
        probabilities      (->> labels
                             (map (fn [label] [label (double (get new-row label 0.0))]))
                             (sort-by second >))]
    {:it-job-position-predictions probabilities}))

(defn predict!
  [new-sample]
  (let [{:keys [feature-columns labels reference-dataset model-pipeline fit-context accuracy]}
        (model/initialize-model! config/RANDOM-FOREST-MODEL)
        ;; "(first labels)" is the dummy target value; it won't be used in prediction.
        sample-dataset (ds/->dataset [(assoc (zipmap feature-columns new-sample)
                       config/TARGET-COLUMN
                       (first labels))])
        {:keys [it-job-position-predictions]}
        (rank-predicted-probabilities reference-dataset
                                      sample-dataset
                                      model-pipeline
                                      fit-context
                                      labels)]
    {:it-job-position-predictions it-job-position-predictions
     :accuracy                    accuracy}))