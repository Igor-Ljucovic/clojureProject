(ns it-role-compass.ml.model-test
  (:require
    [midje.sweet :refer :all]
    [it-role-compass.ml.model :as sut]))

(fact "initialize-model!"
  (reset! @#'sut/model-state {})

  (let [calls    (atom 0)
        trained  {:model :trained}
        ml-model {:model-type :dummy}]

    (with-redefs [sut/train-once! (fn [_]
                                    (swap! calls inc)
                                    trained)]
      ;; first call trains, second call uses cache, prove it only trained once
      (sut/initialize-model! ml-model) => trained
      (sut/initialize-model! ml-model) => trained
      @calls => 1)))
