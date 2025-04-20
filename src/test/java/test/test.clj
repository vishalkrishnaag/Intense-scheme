; src/test/java/test/test.clj
(define test (display x) (display "hello world"))
(intense-reconfigure {
            :configs-ignore-strict true
            :warnings false
            :push/configs-assembled "ok"
             })
(def test
 (display x)
 (display "hello world"))
(test {:x "hai " :y "bai "})