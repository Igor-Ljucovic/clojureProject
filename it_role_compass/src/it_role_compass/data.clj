(ns it-role-compass.data)

(def questions
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

(def job-weight-sets
  [{:label "Data analytics, data science, ML, AI"
    :weights {:data 8 :statistics 7 :math 6}}
   {:label "Backend development, databases"
    :weights {:engineering 8 :algorithms 7 :optimization 6}}
   {:label "DevOps"
    :weights {:engineering 7 :debugging 8 :monotony 6}}
   {:label "Embedded systems, IoT, firmware, robotics, hardware-related"
    :weights {:hardware 8 :engineering 6 :physics 6}}
   {:label "Game development, simulations, graphics-related"
    :weights {:geometry 7 :algorithms 7 :optimization 7 :math 6}}
   {:label "Frontend development, mobile app development"
    :weights {:ui 8 :simplification 7 :people 5}}
   {:label "UX/UI design"
    :weights {:ux 8 :empathy 8 :people 5}}
   {:label "QA, test automation"
    :weights {:testing 8 :edge-cases 8 :debugging 7 :monotony 5}}
   {:label "Cyber security"
    :weights {:edge-cases 8 :debugging 8}}])

(def expert-system->ml-mapping
  {"Databases"
   [:data :algorithms :scalability :optimization :debugging]
   "Hardware"
   [:hardware :physics :engineering :debugging :optimization]
   "Security"
   [:edge-cases :wrong-way :debugging :analysis]
   "Networking"
   [:monotony :debugging :patience :analysis]
   "Software development"
   [:engineering :abstraction :algorithms :debugging]
   "Programming skills"
   [:algorithms :abstraction :optimization :debugging]
   "Project management"
   [:people :patience :simplification :scalability :money]
   "Core Technical"
   [:engineering :algorithms :debugging :optimization]
   "Communication"
   [:people :empathy :patience :simplification]
   "AI ML"
   [:data :math :statistics :algorithms :abstraction]
   "Software engineering"
   [:scalability :engineering :abstraction :debugging :testing]
   "Business analysis and data science"
   [:people :analysis :data :simplification :statistics]
   "Communication skills"
   [:people :empathy :patience :simplification]
   "IT graphics designing"
   [:ui :ux :interactivity :geometry]})
