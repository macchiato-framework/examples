(ns file-upload.app
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [file-upload.core-test]))

(doo-tests 'file-upload.core-test)


