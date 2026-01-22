(ns it-role-compass.ml.ui
  (:require
      [clojure.string :as str]))

(defn machine-learning-report->string
  [{:keys [accuracy it-job-position-predictions]}]
  (let [header [(format "Our machine learning model correctly guessed the IT job positions of real IT workers %.2f%% of the time." 
                         (* 100 (double accuracy)))
                "All IT job position prediction probabilities:"]
        lines  (map (fn [[job probability]] 
                      (format "%4.2f%% %s" (* 100 (double probability)) job))
                    it-job-position-predictions)]
    (str/join "\n" (into header lines))))
