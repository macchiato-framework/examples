(ns file-upload.routes
  (:require
    [bidi.bidi :as bidi]
    [hiccups.runtime]
    [macchiato.util.response :as r])
  (:require-macros
    [hiccups.core :refer [html]]))

(def fs (js/require "fs"))

(defn home [req res raise]
  (-> (html
       [:html
        [:body
         [:h2 "Hello World!"]
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
            :value "submit"}]]]])
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

(defn file-handler [req res raise]
  (let [{:keys [filename tempfile]} (-> req :params :upload first)
        os (.createWriteStream fs filename)]
    (.pipe (.createReadStream fs tempfile) os))
  (res (r/ok "ok")))

(def routes
  ["/"
   [["" home]
    ["file" file-handler]
    [true not-found]]])

(defn router [req res raise]
  (let [route (->> req :uri (bidi/match-route routes) :handler)]
    (route req res raise)))
