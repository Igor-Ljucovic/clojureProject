(ns clojure-university-project.ml.config)

(def FILE-PATH "job_positions_dataset.csv")
(def TARGET-COLUMN :Role)
(def RANDOM-FOREST-MODEL {:model-type :smile.classification/random-forest
                          :trees 251
                          :max-depth 15
                          :node-size 3})
(def TRAINING-SET-RATIO 0.8)
(def SEED 420)