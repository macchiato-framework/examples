(ns dirac-example.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [dirac-example.core-test]))

(doo-tests 'dirac-example.core-test)


