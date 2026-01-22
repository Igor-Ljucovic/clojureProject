(ns it-role-compass.ml.model
  (:require
    [tech.v3.dataset :as ds]
    [scicloj.metamorph.core :as mm]
    
    [it-role-compass.utils :as utils]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.pipeline :as pipe]
    [it-role-compass.ml.evaluation :as evaluation]))

(defonce ^:private model-state (atom {}))

(defn require-smile! 
  []
  (utils/silently (require 'scicloj.ml.smile.classification)))

(defn train-once! 
  [ml-model]
  (require-smile!)
  (let [{:keys [dataset feature-columns labels]} (pipe/load-dataset config/FILE-PATH config/TARGET-COLUMN)
        shuffled-dataset  (ds/shuffle dataset {:seed config/SEED})
        row-count         (ds/row-count shuffled-dataset)
        split-index       (int (* config/TRAINING-SET-RATIO row-count))
        
        training-dataset  (ds/select-rows shuffled-dataset (vec (range 0 split-index)))
        reference-dataset (ds/select-rows shuffled-dataset (vec (range split-index row-count)))
        model-pipeline    (pipe/build-model-pipeline feature-columns config/TARGET-COLUMN ml-model)
        fit-context       (utils/silently (mm/fit-pipe training-dataset model-pipeline))

        encoding-pipeline         (pipe/build-encoding-pipeline feature-columns config/TARGET-COLUMN)
        encoded-reference-dataset (pipe/run-pipeline reference-dataset encoding-pipeline fit-context)
        prediction-dataset        (pipe/run-pipeline reference-dataset model-pipeline fit-context)
        accuracy                  (evaluation/accuracy encoded-reference-dataset prediction-dataset config/TARGET-COLUMN)]
    {:feature-columns   feature-columns
     :labels            labels
     :reference-dataset reference-dataset
     :model-pipeline    model-pipeline
     :fit-context       fit-context
     :accuracy          accuracy}))

(defn init-model!
  [ml-model]
  (or (get @model-state ml-model)
      (let [trained (train-once! ml-model)]
        (swap! model-state #(if (contains? % ml-model) % (assoc % ml-model trained)))
        (get @model-state ml-model))))
