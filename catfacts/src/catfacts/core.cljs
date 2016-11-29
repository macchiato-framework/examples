(ns catfacts.core
  (:require
    [catfacts.middleware :refer [wrap-error]]
    [catfacts.routes :refer [router]]
    [macchiato.http :refer [handler]]
    [mount.core :as mount :refer [defstate]]
    [taoensso.timbre :refer-macros [log trace debug info warn error fatal]]))

(defstate http :start (js/require "http"))

(defn app []
  (let [host   (or (.-HOST (.-env js/process)) "127.0.0.1")
        port   (or (.-PORT (.-env js/process)) 3000)]
    (mount/start)
    (-> @http
          (.createServer (handler router))
          (.listen port host #(info "started on" host ":" port)))))

(defn start-workers [cluster]
  (dotimes [_ (-> (js/require "os") .cpus .-length)]
    (.fork cluster))
  (.on cluster "exit"
       (fn [worker code signal]
         (info "worker terminated" (-> worker .-process .-pid)))))

(defn main [& args]
  (let [cluster (js/require "cluster")]
    (if (.-isMaster cluster)
      (start-workers cluster)
      (app))))

