(defproject clojure_university_project "1.0.0-Alpha "
  :description "CLI expert system that recommends IT job positions based on user's interests/skills"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.2"]
                 [criterium "0.4.6"]]

  :main ^:skip-aot clojure-university-project.core
  :target-path "target/%s"

  :profiles
  {:dev
   {:dependencies [[midje/midje "1.10.10"]]
    :plugins      [[lein-midje "3.2.1"]]}

   :uberjar
   {:aot :all
    :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})