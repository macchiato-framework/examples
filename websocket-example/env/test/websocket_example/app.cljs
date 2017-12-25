(ns websocket-example.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [websocket-example.core-test]))

(doo-tests 'websocket-example.core-test)


