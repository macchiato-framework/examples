(ns cljsbin.auth
  "Auth related middleware."
  (:require
    [goog.crypt.base64 :as base64]
    [cljs.nodejs :as node]
    [clojure.string :as string]
    [macchiato.middleware.node-middleware :refer [wrap-node-middleware]]
    [macchiato.util.response :as r]))

(defn- parse-basic
  "Decode Authorization header value and return a [user pass] sequence"
  [value]
  (let [encoded (second (string/split value #" "))
        decoded (base64/decodeString encoded)]
    (string/split decoded #":")))

(defn- respond-unauth
  [req res]
  (-> (r/unauthorized)
      (r/header "WWW-Authenticate" "Basic realm=\"fake realm\"")
      (res)))

(defn wrap-basic-auth
  "Middleware to handle Basic authentication."
  ([handler authorize-fn] (wrap-basic-auth handler authorize-fn respond-unauth))
  ([handler authorize-fn unauthorized]
   (fn [req res raise]
     (if-let [value (get-in req [:headers "authorization"])]
       (let [[user pass] (parse-basic value)]
         (if (or (not user) (not pass))
           (unauthorized req res)
           (if-let [user (authorize-fn req user pass raise)]
             (handler (assoc req :user user) res raise)
             (unauthorized req res))))
       (unauthorized req res)))))

(def passport (node/require "passport"))
(def DigestStrategy (.-DigestStrategy (node/require "passport-http")))

(defn wrap-digest-auth
  "Middleware to handle Digest authentication."
  [handler authorize-fn]
  ;; hack: use the function reference as the strategy name so no more than one strategy
  ;; is registered in passport for the same function
  (let [strategy    (DigestStrategy. (js-obj "passReqToCallback" true) authorize-fn)
        passport-mw (do (.use passport authorize-fn strategy)
                        (.authenticate passport authorize-fn (js-obj "session" false)))]
    (-> handler
        (wrap-node-middleware passport-mw :req-map {:user "user"}))))
