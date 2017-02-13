(ns auth-example.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [auth-example.core-test]))

(doo-tests 'auth-example.core-test)


