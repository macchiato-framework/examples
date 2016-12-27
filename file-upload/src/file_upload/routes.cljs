(ns file-upload.routes
  (:require
    [bidi.bidi :as bidi]
    [hiccups.runtime]
    [macchiato.fs :as fs]
    [macchiato.middleware.anti-forgery :as af]
    [macchiato.util.response :as r])
  (:require-macros
    [hiccups.core :refer [html]]))

(def fs (js/require "fs"))

(defn list-uploaded-files []
  (-> (fs/read-dir-sync "public/files") seq))

(defn home [req res raise]
  (-> (html
        [:html
         [:body
          [:h2 "Upload a File"]
          [:form
           {:action  "/file"
            :method  "post"
            :enctype "multipart/form-data"}
           [:input
            {:type  "hidden"
             :name  "__anti-forgery-token"
             :value af/*anti-forgery-token*}]
           [:input
            {:type  "file"
             :name  "upload"
             :value "upload"}]
           [:input
            {:type  "submit"
             :value "submit"}]]
          [:hr]
          [:h2 "Uploaded Files"]
          [:ul
           (for [file (list-uploaded-files)]
             [:li [:a {:href (str "files/" file)} file]])]]])
      (r/ok)
      (r/content-type "text/html")
      (res)))

(defn not-found [req res raise]
  (-> (html
        [:html
         [:body
          [:h2 (:uri req) " was not found"]]])
      (r/not-found)
      (r/content-type "text/html")
      (res)))

(defn upload-file [req res raise]
  (let [{:keys [filename tempfile]} (-> req :params :upload first)]
    (fs.rename tempfile (str "public/files/" filename)))
  (res (r/found "/")))

(def routes
  ["/" {""     {:get home}
        "file" {:post upload-file}}])

(defn router [req res raise]
  ((:handler (bidi/match-route* routes (:uri req) req) not-found)
    req res raise))
