; src/test/java/test/test.clj
;; (require "src\\test\\java\\test\\test.clj")

(define hox null)
(define x 323.0) ;; throw error

(define getter (lambda (x y) (+ x (+ x y))))
(define (my-test)((list (define (a) (100)) (define (b) (200)))))
(cond (< 10 20) (display "its works") ())

(define (counter6 n) (if (= n 0) (n) (cond (display n) (counter6 (- n 1)))))

(define my-list (list 2 2 3 3 5 45))
(define instance (create-instance my-list))
(make-instance! my-list lst1)
(make-copy! my-list lst1)
(define instance (create-instance my-list))
(define copy (create-copy my-list))
(list.add my-list 100)
(list.get my-list 2)
(list.size my-list)