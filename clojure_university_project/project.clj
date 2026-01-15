(defproject clojure_university_project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :managed-dependencies [[org.slf4j/slf4j-api "2.0.7"]]

  :dependencies
  [[org.clojure/clojure "1.11.1"]
   [techascent/tech.ml.dataset "7.022"]
   [scicloj/metamorph "0.2.4"]
   [scicloj/metamorph.ml "0.8.2" :exclusions [org.slf4j/slf4j-simple]]
   [scicloj/scicloj.ml.smile "7.4.1" :exclusions [org.slf4j/slf4j-simple]]
   [ch.qos.logback/logback-classic "1.4.14"]]

  :main ^:skip-aot clojure-university-project.core
  :target-path "target/%s"

  :profiles
  {:dev
   {:dependencies [[midje/midje "1.10.10"]]
    :plugins      [[lein-midje "3.2.1"]]}

   :uberjar
   {:aot :all
    :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
