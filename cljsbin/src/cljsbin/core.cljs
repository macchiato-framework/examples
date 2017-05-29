(ns cljsbin.core
  (:require
    [cljsbin.config :refer [env]]
    [cljsbin.middleware :refer [wrap-defaults]]
    [cljsbin.routes :refer [router]]
    [macchiato.server :as http]
    [mount.core :as mount :refer [defstate]]
    [taoensso.timbre :refer-macros [log trace debug info warn error fatal]]))

(defn main []
  (mount/start)
  (let [host (or (:host @env) "0.0.0.0")
        port (or (some-> @env :port js/parseInt) 3000)]
    (http/start
      {:handler    router
       ;; for some reason this is needed to see cookie values
       :cookies    {:signed? false}
       :host       host
       :port       port
       :on-success #(info "cljsbin started on" host ":" port)})))
