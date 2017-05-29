(ns cljsbin.middleware
  (:require
    [cljs.nodejs :as node]
    [macchiato.middleware.node-middleware :refer [wrap-node-middleware]]
    [macchiato.middleware.defaults :as defaults]))

(def body-parser (node/require "body-parser"))
(def response-time (node/require "response-time"))
(def morgan (node/require "morgan"))
(def favicon (node/require "serve-favicon"))

(defn wrap-defaults [handler]
  (-> handler
      (wrap-node-middleware (.text body-parser)
                            :req-map {:body "body" :text "body"})
      (wrap-node-middleware (.json body-parser)
                            :req-map {:body "body" :json "body"})
      (wrap-defaults)
      (wrap-node-middleware (favicon "public/clojure.ico"))
      (wrap-node-middleware (morgan "combined"))
      (wrap-node-middleware (response-time))
      (defaults/wrap-defaults defaults/api-defaults)))
