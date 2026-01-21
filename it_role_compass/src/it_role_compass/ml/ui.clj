(ns it-role-compass.ml.ui)

(defn print-report!
  [{:keys [accuracy it-job-position-predictions]}]
  (println (format "Model accuracy: %.2f%%" (* 100 (double accuracy))))
  (println "All job probabilities:")
  (doseq [[it-job-position probability] it-job-position-predictions]
    (println (format "%5.2f%% %s" (* 100 (double probability)) it-job-position))))
