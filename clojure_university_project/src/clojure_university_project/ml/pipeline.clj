(ns clojure-university-project.ml.pipeline
  (:require
    [tech.v3.dataset :as ds]
    [tech.v3.dataset.metamorph :as ds-mm]
    [scicloj.metamorph.core :as mm]
    [scicloj.metamorph.ml :as ml]

    [clojure-university-project.utils :as utils]))

(defn load-dataset 
  [file-path target-column]
  (let [dataset (ds/->dataset file-path {:key-fn keyword})]
    {:dataset         dataset
     :feature-columns (remove #(= % target-column) (ds/column-names dataset))
     :roles           (->> (ds/column dataset target-column) distinct sort vec)}))

(defn build-preproc-pipe 
  [feature-columns target-column]
  (mm/pipeline
    (ds-mm/categorical->one-hot feature-columns)
    (ds-mm/categorical->number [target-column])))

(defn build-pipe 
  [feature-columns target-column model]
  (mm/pipeline
    (build-preproc-pipe feature-columns target-column)
    (ds-mm/set-inference-target target-column)
    (ml/model model)))

(defn transform-data
  [dataset pipeline fit-context]
  (:metamorph/data (utils/silently (mm/transform-pipe dataset pipeline fit-context))))