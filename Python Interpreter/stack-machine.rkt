#lang racket
(require "opcodes.rkt")
(provide make-stack-machine)
(provide run-stack-machine)
(provide get-stack)
(provide get-varnames)
(provide get-consts)
(provide get-names)
(provide get-code)
(provide get-IC)
(provide empty-stack)
(provide make-stack)
(provide push)
(provide pop)
(provide top)


;; TODO 1:
;; Alegeți metoda de reprezentarea a unei stive.
;; Implementați:
(define empty-stack null)
(define (make-stack) null)

(define (push element stack) (cons element stack))
(define (top stack) (if (pair? stack)
                        (car stack)
                        stack))
(define (pop stack) (if (pair? stack)
                        (cdr stack)
                        '()))

;; TODO 2:
;; Alegeți metoda de reprezentare a unei mașini stivă.
;; Definiți make-stack-machine, acesta trebuie sa primeasca cele 4 segmente de date
;; Veți avea nevoie de o stivă pentru execuție și un counter ca să stiți
;; la ce instrucțiune sunteți.
(define (make-stack-machine stack co-varnames co-consts co-names co-code IC)
  (list stack co-varnames co-consts co-names co-code IC))

;; Definiți funcțiile `get-varnames`, `get-consts`, `get-names`,
;; `get-code`, `get-stack`, `get-IC` care primesc o mașina stivă și întorc
;; componenta respectivă

;; ex:
;; > (get-varnames (make-stack-machine empty-stack 'dummy-co-varnames (hash) (hash) (list) 0))
;; 'dummy-co-varnames
(define (get-varnames stack-machine) (list-ref stack-machine 1))

;; ex:
;; > (get-consts (make-stack-machine empty-stack (hash) 'dummy-co-consts (hash) (list) 0))
;; 'dummy-co-consts
(define (get-consts stack-machine) (list-ref stack-machine 2))

;; ex:
;; > (get-names (make-stack-machine empty-stack (hash) (hash) 'dummy-co-names (list) 0))
;; 'dummy-co-names
(define (get-names stack-machine) (list-ref stack-machine 3))

;; ex:
;; > (get-code (make-stack-machine empty-stack (hash) (hash) (hash) 'dummy-co-code 0))
;; dummy-co-code
(define (get-code stack-machine) (list-ref stack-machine 4))

;; Întoarce stiva de execuție.
;; ex:
;; > (get-code (make-stack-machine 'dummy-exec-stack (hash) (hash) (hash) (list) 0))
;; dummy-exec-stack
(define (get-stack stack-machine) (list-ref stack-machine 0))

;; Întoarce instruction counterul.
;; ex:
;; > (get-code (make-stack-machine empty-stack (hash) (hash) (hash) (list) 0))
;; 0
(define (get-IC stack-machine) (list-ref stack-machine 5))



(define symbols (list 'STACK 'CO-VARNAMES 'CO-CONSTS 'CO-NAMES 'CO-CODE 'INSTRUCTION-COUNTER))

;; TODO 3:
;; Definiți funcția get-symbol-index care gasește index-ul simbolului in listă.
(define (get-symbol-index symbol)
  (cond
    [(equal? symbol (car symbols)) 0]
    [(equal? symbol (car (cdr symbols))) 1]
    [(equal? symbol (car (cdr (cdr symbols)))) 2]
    [(equal? symbol (car (cdr(cdr (cdr symbols))))) 3]
    [(equal? symbol (car (cdr (cdr (cdr (cdr symbols)))))) 4]
    
    [(equal? symbol (car (cdr (cdr (cdr (cdr (cdr symbols))))))) 5]))

;; Definiți funcția update-stack-machine care intoarce o noua mașina stivă
;; înlocuind componenta corespondentă simbolului cu item-ul dat în paremetri.
;; > (get-varnames (update-stack-machine "new-varnames" 'CO-VARNAMES stack-machine))
;; "new-varnames"
;; > (get-varnames (update-stack-machine "new-names" 'CO-NAMES stack-machine))
;; "new-names"

(define (update-stack-machine item symbol stack-machine)
  (cond
    [(equal? symbol (car symbols)) (make-stack-machine item (list-ref stack-machine 1) (list-ref stack-machine 2)
                                                       (list-ref stack-machine 3) (list-ref stack-machine 4)
                                                       (list-ref stack-machine 5))]
    [(equal? symbol (car (cdr symbols))) (make-stack-machine (list-ref stack-machine 0) item (list-ref stack-machine 2)
                                                       (list-ref stack-machine 3) (list-ref stack-machine 4)
                                                       (list-ref stack-machine 5))]
    [(equal? symbol (car (cdr (cdr symbols)))) (make-stack-machine (list-ref stack-machine 0) (list-ref stack-machine 1) item 
                                                       (list-ref stack-machine 3) (list-ref stack-machine 4)
                                                       (list-ref stack-machine 5))]
    [(equal? symbol (car (cdr(cdr (cdr symbols))))) (make-stack-machine (list-ref stack-machine 0) (list-ref stack-machine 1)
                                                       (list-ref stack-machine 2) item 
                                                       (list-ref stack-machine 4) (list-ref stack-machine 5))]
    [(equal? symbol (car (cdr (cdr (cdr (cdr symbols)))))) (make-stack-machine (list-ref stack-machine 0) (list-ref stack-machine 1)
                                                       (list-ref stack-machine 2) (list-ref stack-machine 3) item 
                                                       (list-ref stack-machine 5))]
    
    [(equal? symbol (car (cdr (cdr (cdr (cdr (cdr symbols))))))) (make-stack-machine (list-ref stack-machine 0) (list-ref stack-machine 1)
                                                       (list-ref stack-machine 2) (list-ref stack-machine 3) 
                                                       (list-ref stack-machine 4) item)]))

;; Definiți funcția push-exec-stack care primește o masină stivă și o valoare
;; și intoarce o noua mașina unde valoarea este pusă pe stiva de execuție


(define (push-exec-stack value stack-machine)
  (update-stack-machine (push value (get-stack stack-machine)) 'STACK stack-machine))

;;  Definiți funcția pop-exec-stack care primește o masină stivă
;;  și intoarce o noua mașina aplicând pop pe stiva de execuție.
(define (pop-exec-stack stack-machine)
  (update-stack-machine (pop (get-stack stack-machine)) 'STACK stack-machine))

;; TODO 4:
;; Definiți funcția run-stack-machine care execută operații pană epuizează co-code.
(define (run-stack-machine-ok stack-machine stack-code counter max_value)
  
  (if (equal? counter max_value)
      stack-machine
      (case (top (list-ref stack-code counter))
        ['POP_TOP (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (+ 1 counter) max_value)]
        [ 'LOAD_CONST (run-stack-machine-ok (push-exec-stack (hash-ref (get-consts stack-machine)
                                                                       (cdr (list-ref stack-code counter)))
                                                                       stack-machine) stack-code (+ 1 counter) max_value)]
        [ 'LOAD_GLOBAL (run-stack-machine-ok (push-exec-stack (hash-ref (get-names stack-machine) (cdr (list-ref stack-code counter))) stack-machine)
                                             stack-code (+ 1 counter) max_value)]
        ['STORE_FAST (run-stack-machine-ok (make-stack-machine (pop (get-stack stack-machine)) (hash-set (get-varnames stack-machine) (cdr (list-ref stack-code counter))
                                                               (top (get-stack stack-machine)))
                                                               (get-consts stack-machine) (get-names stack-machine) (get-code stack-machine)
                                                               (get-IC stack-machine)) stack-code (+ 1 counter) max_value)]
        ['LOAD_FAST (run-stack-machine-ok (push-exec-stack (hash-ref (get-varnames stack-machine) (cdr(list-ref stack-code counter)))
                                                           stack-machine) stack-code (+ 1 counter) max_value)]
        ['BINARY_ADD (run-stack-machine-ok (push-exec-stack (+ (top (get-stack stack-machine)) (top(cdr(get-stack stack-machine))))
                                                            (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['BINARY_SUBTRACT (run-stack-machine-ok (push-exec-stack (- (top(cdr(get-stack stack-machine))) (top(get-stack stack-machine)))
                                                                 (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['BINARY_MODULO (run-stack-machine-ok (push-exec-stack (modulo (top(cdr(get-stack stack-machine))) (top (get-stack stack-machine)))
                                                               (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['INPLACE_ADD (run-stack-machine-ok (push-exec-stack (+ (top (get-stack stack-machine)) (top(cdr(get-stack stack-machine))))
                                                             (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['INPLACE_SUBTRACT (run-stack-machine-ok (push-exec-stack (- (top(cdr(get-stack stack-machine))) (top(get-stack stack-machine)))
                                                                  (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['INPLACE_MODULO (run-stack-machine-ok (push-exec-stack (modulo (top(cdr(get-stack stack-machine))) (top (get-stack stack-machine)))
                                                                (pop-exec-stack (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['JUMP_ABSOLUTE (run-stack-machine-ok stack-machine stack-code (/ (cdr(list-ref stack-code counter))2) max_value)]   
        ['POP_JUMP_IF_FALSE (if (false? (top(get-stack stack-machine)))
                                (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (/ (cdr(list-ref stack-code counter)) 2) max_value)
                                (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (+ 1 counter) max_value))]
        ['POP_JUMP_IF_TRUE (if (false? (top(get-stack stack-machine)))
                                (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (+ 1 counter) max_value)
                                (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (/(cdr(list-ref stack-code counter)) 2) max_value))]
        ['RETURN_VALUE (run-stack-machine-ok stack-machine stack-code (+ 1 counter) max_value)]
        ['COMPARE_OP (run-stack-machine-ok (push-exec-stack ((get-cmpop (cdr(list-ref stack-code counter)))
                                (top(cdr(get-stack stack-machine)))(top (get-stack stack-machine))) (pop-exec-stack(pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value)]
        ['SETUP_LOOP (run-stack-machine-ok stack-machine stack-code (+ 1 counter) max_value)]
        ['POP_BLOCK (run-stack-machine-ok stack-machine stack-code (+ 1 counter) max_value)]
        ['GET_ITER (run-stack-machine-ok stack-machine stack-code (+ 1 counter) max_value)]
        ['FOR_ITER (if (null? (top(top(get-stack stack-machine))))
                       (run-stack-machine-ok (pop-exec-stack stack-machine) stack-code (+ 1 (+ counter(/(cdr (list-ref stack-code counter))2)))  max_value)
                       (run-stack-machine-ok (push-exec-stack (top(top (get-stack stack-machine))) (push-exec-stack (pop(top(get-stack stack-machine)))
                                             (pop-exec-stack stack-machine))) stack-code (+ 1 counter) max_value))]
        ['CALL_FUNCTION (run-stack-machine-ok (push-exec-stack(apply (get-function (list-ref (get-stack stack-machine) (cdr(list-ref stack-code counter)))) (take (get-stack stack-machine)(cdr(list-ref stack-code counter))))
                                              (update-stack-machine(list-tail (get-stack stack-machine)(+ 1(cdr(list-ref stack-code counter)))) 'STACK stack-machine))
                                              stack-code (+ 1 counter) max_value)]
        )
   )
 )

(define (run-stack-machine stack-machine)
  ;(update-stack-machine '(0) 'INSTRUCTION-COUNTER stack-machine)
  (run-stack-machine-ok stack-machine (get-code stack-machine) 0 (length (get-code stack-machine)))  
  )
 
