; This was made so that it could catch multiple progs functions
(define (myinterpreter x)
  (cond
    ((null? x) '())
    (else
      (cons 
        (prog (car (cdr (car x))) '())
        (myinterpreter (cdr x))
      )
    )
  )
)

; This is prog, checks if it's a integer, symbol or function
(define (prog m B)
  (cond
	; This is to return if it's an integer
    ((integer? m)  m)
	; These check if it's an alphabet character and if it is use the find function to find the match
	; NOTE: this is only lower case characters like the grammer states
    ((equal?  m 'a) (find B m))
    ((equal?  m 'b) (find B m))
    ((equal?  m 'c) (find B m))
    ((equal?  m 'd) (find B m))
    ((equal?  m 'e) (find B m))
    ((equal?  m 'f) (find B m))
    ((equal?  m 'g) (find B m))
    ((equal?  m 'h) (find B m))
    ((equal?  m 'i) (find B m))
    ((equal?  m 'j) (find B m))
    ((equal?  m 'k) (find B m))
    ((equal?  m 'l) (find B m))
    ((equal?  m 'm) (find B m))
    ((equal?  m 'n) (find B m))
    ((equal?  m 'o) (find B m))
    ((equal?  m 'p) (find B m))
    ((equal?  m 'q) (find B m))
    ((equal?  m 'r) (find B m))
    ((equal?  m 's) (find B m))
    ((equal?  m 't) (find B m))
    ((equal?  m 'u) (find B m))
    ((equal?  m 'v) (find B m))
    ((equal?  m 'w) (find B m))
    ((equal?  m 'x) (find B m))
    ((equal?  m 'y) (find B m))
    ((equal?  m 'z) (find B m))
	; This is myignore funciton
    ((equal? (car m) `myignore) 0)
	; This is myadd function
    ((equal? (car m) `myadd)
      (+
        (prog (car(cdr m)) B)
        (prog (car(cdr (cdr m))) B)
      )
    )
	; This is mymul function
    ((equal? (car m) `mymul)
      (*
        (prog (car(cdr m)) B)
        (prog(car(cdr (cdr m))) B)
      )
    )
	; This is myneg function
    ((equal? (car m) `myneg)
      (*
        (prog(car(cdr m)) B)
        -1
      )
    )
	; This is mylet function
	; This calls myeval which returns whatever expr2 was/is
    ((equal? (car m) `mylet)
      (myeval 
        (car(cdr (cdr (cdr m))))
        (cons
          (list 
            (car(cdr m))
            (prog (car(cdr (cdr m))) B)
          )
          B
        )
      )
    )
  )
)
; myeval calls prog on expr2 and sends the bindings list
(define (myeval Z L)
  (prog Z L)
)

; find looks through the bindings list to find a match recursively
(define (find L i)
  (cond
    ((equal? i (car(car L)))
      (car (cdr (car L))))
    (else
      (find (cdr L) i)
    )
  )
)
