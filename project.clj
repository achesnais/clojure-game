(defproject clojure-game "0.1.0-SNAPSHOT"
  :description "A Sandbox for a game in Clojure."
  :url "https://github.com/achesnais/clojure-game"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-lanterna "0.9.4"]
                 [org.clojure/tools.namespace "0.2.9"]]
  :profiles
  {:dev {:source-paths ["dev"]
         :dependencies [[org.clojure/tools.namespace "0.2.9"]]}})
