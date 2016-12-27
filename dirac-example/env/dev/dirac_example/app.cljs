 (ns ^:figwheel-always dirac-example.app
  (:require
    [dirac-example.core :as core]
    [dirac.runtime]
    [cljs.nodejs]
    [mount.core :as mount]))

(dirac.runtime/install!)

(mount/in-cljc-mode)

(cljs.nodejs/enable-util-print!)

(.on js/process "uncaughtException" #(js/console.error %))

(set! *main-cli-fn* core/app)
