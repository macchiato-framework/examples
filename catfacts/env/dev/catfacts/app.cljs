 (ns ^:figwheel-always catfacts.app
  (:require
    [catfacts.core :as core]
    [cljs.nodejs]
    [mount.core :as mount]
    [taoensso.timbre :refer-macros [error]]))

(mount/in-cljc-mode)

(cljs.nodejs/enable-util-print!)

(.on js/process "uncaughtException" #(js/console.error %))

(set! *main-cli-fn* core/app)
