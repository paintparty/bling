(ns bling.js-env)


(defonce node?
  (boolean 
   (some->> 
    (or (when (and (exists? js/window)
                   (exists? js/window.document))
          :browser)

        (when (and (exists? js/process)
                   (not (nil? (some-> js/process .-versions)))
                   (not (nil? (some-> js/process .-versions .-node))))
          :node)

        (when (and (identical? "object" (js/goog.typeOf js/self))
                   (.-constructor js/self)
                   (= (some-> js/self .-constructor .-name)
                      "DedicatedWorkerGlobalScope"))
          :web-worker)

        (when (or (exists? js/window)
                  (and (exists? js/navigator)
                       (when-let [nav (.-userAgent js/navigator)]
                         (and (identical? "object" (js/goog.typeOf nav))
                              (or (.includes nav "Node.js")
                                  (.includes nav "jsdom"))))))
          :js-dom)

        (when (and (exists? js/Deno)
                   (exists? (.-version js/Deno))
                   (exists? (some-> js/Deno .-version .-deno)))
          :deno))
    (contains? #{:deno :node}))))

(when node? (defonce BLING_MOOD (some-> js/process .-env .-BLING_MOOD)))
