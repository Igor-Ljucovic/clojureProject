(ns it-role-compass.ml.ui
  (:require
      [clojure.string :as str]))

(defn machine-learning-report->string
  [{:keys [accuracy it-job-position-predictions]}]
  (let [header [(format "Model accuracy: %.2f%%" (* 100 (double accuracy)))
                "All job probabilities:"]
        lines  (map (fn [[job probability]] 
                      (format "%5.2f%% %s" (* 100 (double probability)) job))
                    it-job-position-predictions)]
    (str/join "\n" (into header lines))))
