 (ns websocket-example.app
  (:require
    [websocket-example.core :as core]
    [cljs.nodejs]
    [mount.core :as mount]))

(mount/in-cljc-mode)

(cljs.nodejs/enable-util-print!)

(set! *main-cli-fn* core/server)
