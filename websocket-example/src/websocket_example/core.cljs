(ns websocket-example.core
  (:require
    [websocket-example.config :refer [env]]
    [websocket-example.middleware :refer [wrap-defaults]]
    [websocket-example.routes :refer [router]]
    [macchiato.server :as http]
    [macchiato.middleware.session.memory :as mem]
    [mount.core :as mount :refer [defstate]]
    [taoensso.timbre :refer-macros [log trace debug info warn error fatal]]))

(defn ws-handler [{:keys [websocket] :as ws-req}]
  (.on websocket "message"
       (fn [message]
           (.send websocket (str "got message: " message)))))

(defn server []
  (mount/start)
  (let [host   (or (:host @env) "127.0.0.1")
        port   (or (some-> @env :port js/parseInt) 3000)
        server (http/start
                 {:handler    (wrap-defaults router)
                  :host       host
                  :port       port
                  :on-success #(info "websocket-example started on" host ":" port)})]

    (http/start-ws server ws-handler)))


