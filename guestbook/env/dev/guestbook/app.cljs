 (ns ^:figwheel-always guestbook.app
  (:require
    [guestbook.core :as core]
    [cljs.nodejs :as node]
    [mount.core :as mount]))

(mount/in-cljc-mode)

(cljs.nodejs/enable-util-print!)

(.on js/process "uncaughtException" #(js/console.error %))

(set! *main-cli-fn* core/server)
