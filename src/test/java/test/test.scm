; src/test/java/test/test.clj
;; (require "src\\test\\java\\test\\test.clj")

(define hox null)
(define x 323.0) ;; throw error
(define (test) (display x)
(display "hello world"))
(test :x "hello world")

(define getter (lambda (x y) (+ x (+ x y))))
