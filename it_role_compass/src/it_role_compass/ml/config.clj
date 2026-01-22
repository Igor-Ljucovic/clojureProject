(ns it-role-compass.ml.config)

(def FILE-PATH "job_positions_dataset.csv")
(def TARGET-COLUMN :Role)
(def RANDOM-FOREST-MODEL {:model-type :smile.classification/random-forest
                          :trees 251
                          :max-depth 15
                          :node-size 3})
(def TRAINING-SET-RATIO 0.8)
(def SEED 420)

(def ML-LABELS
  ["Not Interested" "Poor" "Beginner" "Average" "Intermediate" "Advanced"])

(def ML-FEATURE-ORDER
  ["Databases"
   "Hardware"
   "Security"
   "Networking"
   "Software development"
   "Programming skills"
   "Project management"
   "Core Technical"
   "Communication"
   "AI ML"
   "Software engineering"
   "Business analysis and data science"
   "Communication skills"
   "IT graphics designing"])
   