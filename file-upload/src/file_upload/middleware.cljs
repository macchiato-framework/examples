(ns file-upload.middleware
  (:require
    [macchiato.defaults :as defaults]))

(defn wrap-defaults [handler]
  (defaults/wrap-defaults
    handler
    (-> defaults/site-defaults
        (assoc-in [:static :resources] "public")
        (assoc-in [:params :multipart] {:upload-dir "public/files"}))))


