(ns it-role-compass.ml.model
  (:require
    [tech.v3.dataset :as ds]
    [it-role-compass.utils :as utils]
    [it-role-compass.ml.config :as config]
    [it-role-compass.ml.pipeline :as pipe]
    [scicloj.metamorph.core :as mm]))

(defonce ^:private model-state (atom nil))

(defn require-smile! []
  (utils/silently (require 'scicloj.ml.smile.classification)))

(defn calculate-accuracy 
  [test-dataset prediction-dataset target-column]
  (let [y-true (ds/column test-dataset target-column)
        y-pred (take (count y-true) (ds/column prediction-dataset target-column))]
    (/ (count (filter true? (map = y-true y-pred)))
       (count y-true))))

(defn predict-probabilities 
  [test-dataset new-person model-pipe fit-ctx roles]
  (let [prediction-dataset (pipe/transform-data (ds/concat test-dataset new-person) model-pipe fit-ctx)
        row                (ds/row-at prediction-dataset (dec (ds/row-count prediction-dataset)))
        probabilities      (->> roles
                             (map (fn [r] [r (double (get row r 0.0))]))
                             (sort-by second >))]
    {:it-job-position-predictions probabilities}))

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
        accuracy           (calculate-accuracy test-ready prediction-dataset config/TARGET-COLUMN)]
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
