(ns clojure-university-project.core
  (:require [clojure.string :as str])
  (:gen-class))

(def ^:private questions
  [{:id :data           :q "How interesting do you find working with large amounts of data? (0-10)"}
   {:id :math           :q "How good are you at advanced math (integrals, derivatives)? (0-10)"}
   {:id :algorithms     :q "How interesting do you find data structures and algorithmic problems? (0-10)"}
   {:id :physics        :q "How good are you at mechanical and electrical physics? (0-10)"}
   {:id :geometry       :q "How interesting does working with 2D and 3D geometry sound? (0-10)"}
   {:id :abstraction    :q "How fun does abstracting to more general rules sound? (0-10)"}
   {:id :optimization   :q "How interesting does optimizing memory/speed sound? (0-10)"}
   {:id :engineering    :q "How interesting do you find designing/engineering new things? (0-10)"}
   {:id :hardware       :q "How interesting do you find working with hardware? (0-10)"}
   {:id :ui             :q "How fun does making things look nice and simple sound? (0-10)"}
   {:id :analysis       :q "How good are you at identifying weak spots in things or people? (0-10)"}
   {:id :wrong-way      :q "How good are you at finding the only wrong way to do something? (0-10)"}
   {:id :testing        :q "How interesting does testing and predicting bugs sound? (0-10)"}
   {:id :people         :q "How interesting do you find working with and understanding people? (0-10)"}
   {:id :ux             :q "How much do you care about how technology feels and interacts? (0-10)"}
   {:id :interactivity  :q "How exciting is the idea of building interactive UI? (0-10)"}
   {:id :edge-cases     :q "How fun is it to imagine how something could go wrong? (0-10)"}
   {:id :scalability    :q "How much does it interest you to make things that grow huge? (0-10)"}
   {:id :statistics     :q "How interesting does statistics and probability sound? (0-10)"}
   {:id :empathy        :q "How naturally do you predict what someone will find confusing? (0-10)"}
   {:id :simplification :q "How satisfying is it to take messy info and structure it simply? (0-10)"}
   {:id :patience       :q "How patient are you when you need to explain things calmly? (0-10)"}
   {:id :debugging      :q "How willing are you to spend a long time finding tiny bugs? (0-10)"}
   {:id :monotony       :q "Would you mind solving the same problems the majority of the time? (0-10)"}
   {:id :money          :q "How much do you care about how much money you'll make? (0-10)"}])

(def ^:private job-weight-sets
  [{:label "Data analytics, data science, ML, AI"
    :weights {:data 160 :statistics 140 :math 120}}
   {:label "Backend development, systems engineering"
    :weights {:engineering 16 :algorithms 14 :optimization 12}}
   {:label "DevOps"
    :weights {:engineering 14 :debugging 16 :monotony 12}}
   {:label "Embedded systems, IoT, firmware, robotics, hardware-related"
    :weights {:hardware 16 :engineering 12 :physics 12}}
   {:label "Game development, simulations, graphics"
    :weights {:geometry 14 :algorithms 14 :optimization 14 :math 12}}
   {:label "Frontend development, mobile development"
    :weights {:ui 16 :simplification 14 :people 10}}
   {:label "UX/UI design, product design"
    :weights {:ux 16 :empathy 16 :people 10}}
   {:label "QA engineering, test automation"
    :weights {:testing 16 :edge-cases 16 :debugging 14 :monotony 10}}
   {:label "Cyber security"
    :weights {:edge-cases 16 :debugging 16}}])

(def ^:private rating-label-separator "|")
(def ^:private rating-label-separator-regex
  (re-pattern (java.util.regex.Pattern/quote rating-label-separator)))

(defn ask-for-it-topics-ratings
  [question]
  (loop []
    (println question)
    (flush)
    (let [input (read-line)
          value (parse-double input)]
      (if (and value (<= 0 value 10))
        value
        (do
          (println "Please enter a number from 0 to 10 (decimals allowed).")
          (recur))))))

(defn it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn rating-with-balanced-weights 
  "Calculates a weighted rating using balanced (not yet normalized) weights.

  The absolute values of weights don't matter, only their proportions.
  For example, weights {:data 8 :math 6} and {:data 80 :math 60} return the same result.
  The number of weights also doesn't matter (3, 4, 10, etc.).

  ratings:
    Map of raw ratings, e.g. {:data 8 :math 9}

  weights:
    Map of weights per key, e.g. {:data 16 :math 12}

  label:
    String label describing the rating,
    e.g. \"Backend development, systems engineering\"

  Returns:
    A string in the form \"<rating>rating-label-separator<label>\"

  Note:
    Normalization (to percentages so that they total to 100%) is done in a later step."
  [ratings weights label]
  (let [num (reduce-kv (fn [acc key value] (+ acc (* value (get ratings key)))) 0 weights)
        den (reduce + (vals weights))]
    (str (double (/ num den)) rating-label-separator label)))

(defn recommended-it-job-positions 
  [ratings]
  (mapv (fn [{:keys [label weights]}]
          (rating-with-balanced-weights ratings weights label))
        job-weight-sets))

(defn it-job-suitability-messages 
  [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably should work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

(defn random-ratings
  [n min-val max-val]
  (vec
    (repeatedly n
      #(let [rand-num (+ min-val (* (rand) (- max-val min-val)))] 
       (/ (Math/round (* 100 rand-num)) 100.0)))))

(defn power-transform-job-ratings 
  "Power is used to adjust the distribution of ratings so that the users can better know
  which jobs suit them best. Instead of linear scaling, exponential scaling is used.
  power=4 is recommended, since it will differentiate better between close ratings. 
  Instead of having almost all the percentages in the 15-25% range, it will spread them out more."
  [jobs power]
  (map #(let [[rating label] (str/split % rating-label-separator-regex)] 
          [(Math/pow (Double/parseDouble rating) power) label]) jobs))

(defn normalize-job-ratings-to-percent 
  [pairs]
  (let [total (apply + (map first pairs))]
    (map (fn [[rating label]] [(* 100 (/ rating total)) label]) pairs)))

(defn rank-job-ratings-desc
  [rating-label-pairs]
  (sort-by first > rating-label-pairs))

(defn format-jobs 
  [normalized-data]
  (map (fn [[rating label]] (format "%.1f%% %s" rating label)) 
       normalized-data))

;; HACK: single responsibility principle violation
(defn run-app 
  []
  (println "Rate each topic from 0 (hate it) to 10 (love it).")
   (let [;; This creates the map directly: {:data 8 :math 10 ...}
        ratings (into {} (for [{:keys [id q]} questions]
                          [id (ask-for-it-topics-ratings q)]))
        total   (reduce + (vals ratings))
        average (/ (double total) (count ratings))
        it-job-suitability (it-job-suitability-messages average)
        strengths (it-skills-by-rating-threshold ratings >= 7)
        weaknesses (it-skills-by-rating-threshold ratings <= 4)
        job-positions (recommended-it-job-positions ratings)]
    (println (format "Average interest: %.2f" average))
    (println it-job-suitability)
    (println "Strong skills:" (str/join ", " strengths))
    (println "Weak skills:" (str/join ", " weaknesses))
    (println "Recommended IT job positions:")
    (doseq [line (-> job-positions
                 (power-transform-job-ratings 4)
                 (normalize-job-ratings-to-percent)
                 (rank-job-ratings-desc)
                 (format-jobs))]
    (println line))))

(defn -main
  [& args]
  (run-app))
