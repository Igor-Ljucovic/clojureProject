(ns it-role-compass.ml.model
  (:require
    [tech.v3.dataset :as ds]
    [it-role-compass.utils :as utils]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.pipeline :as pipe]
    [it-role-compass.ml.evaluation :as evaluation]
    [scicloj.metamorph.core :as mm]))

(defonce ^:private model-state (atom nil))

(defn require-smile! []
  (utils/silently (require 'scicloj.ml.smile.classification)))

(defn train-once! 
  []
  (require-smile!)
  (let [{:keys [dataset feature-columns roles]} (pipe/load-dataset config/FILE-PATH config/TARGET-COLUMN)
        shuffled  (ds/shuffle dataset {:seed config/SEED})
        row-cnt   (ds/row-count shuffled)
        split-idx (int (* config/TRAINING-SET-RATIO row-cnt))
        train     (ds/select-rows shuffled (vec (range 0 split-idx)))
        test      (ds/select-rows shuffled (vec (range split-idx row-cnt)))

        model-pipe (pipe/build-pipe feature-columns config/TARGET-COLUMN config/RANDOM-FOREST-MODEL)
        fit-context    (utils/silently (mm/fit-pipe train model-pipe))

        pre-pipe           (pipe/build-preproc-pipe feature-columns config/TARGET-COLUMN)
        test-ready         (pipe/transform-data test pre-pipe fit-context)
        prediction-dataset (pipe/transform-data test model-pipe fit-context)
        accuracy           (evaluation/calculate-accuracy test-ready prediction-dataset config/TARGET-COLUMN)]
    {:feature-columns feature-columns
     :roles           roles
     :test            test
     :pipe            model-pipe
     :fit-context     fit-context
     :accuracy        accuracy}))

(defn init-model! 
  []
  (or @model-state
      (let [trained-model-state (train-once!)]
        (if (compare-and-set! model-state nil trained-model-state)
          trained-model-state
          @model-state))))
