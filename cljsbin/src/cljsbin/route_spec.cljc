(ns cljsbin.route-spec
  (:require [bidi.bidi :as bidi]))

(defmacro route-spec
  "Build a route spec usable by bidi (map method to tagged handler)
  and augment the handler map with metadata that can be used to generate
  a router index."
  [method sym & {:keys [bidi-tag no-display]}]
  ;; this is pretty ugly
  `{~method (merge (bidi/tag ~sym ~bidi-tag)
                   (meta (var ~sym))
                   {:no-display ~no-display})})
