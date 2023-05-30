build_table(I, J) :-
    I1 is I * I,
    \+ I1 > J,  
    I2 is I + 1,
    add_primes(I, J),
    build_table(I2, J).
    
add_primes(I, J) :-
    composite_table(I), !.
add_primes(I, J) :-
    \+ composite_table(I), !,
    assert(prime_table(I)), 
    I1 is I * I,
    add_all_min_divs(I, I1, J).

add_min_div(S, _) :-
    composite_table(S), !.
add_min_div(S, R) :-
    assert(composite_table(S)),
    assert(min_div_prime(S, R)), !.

add_all_min_divs(_, E1, E) :- 
    EE is E1 * E1,
    EE > E, !.
add_all_min_divs(R, S, E) :- 
    add_min_div(S, R),
    SR is S + R,
    add_all_min_divs(R, SR, E).


init(MAX_N) :- 
    (assert(composite_table(1)),
    build_table(2, MAX_N); true).


composite(N) :-      
    composite_table(N), !.
composite(N) :-
    prime_table(N), !, false.
composite(N) :- 
    prime_table(P), 
    0 is mod(N, P), !, 
    assert(composite_table(N)),
    assert(min_div_prime(N, P)).

prime(N) :- prime_table(N), !.
prime(N) :- \+ composite(N), !.


is_sorted_primes([H]) :- prime(H).
is_sorted_primes([H1, H2 | T]) :- H1 =< H2, prime(H1), is_sorted_primes([H2 | T]).

product([], 1).
product([H | T], R) :- product(T, R1), R is H * R1.

prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :-
    number(N), composite(N), !,
    min_div_prime(N, H), 
    N1 is N // H,
    prime_divisors(N1, T).
prime_divisors(N, D) :- list(D), !, is_sorted_primes(D), product(D, N).

is_sorted_compact_primes([(A, B)]) :- B > 0, prime(A).
is_sorted_compact_primes([(A1, B1), (A2, B2) | T]) :- A1 < A2, B1 > 0, prime(A1), is_sorted_compact_primes([(A2, B2) | T]).

fast_power(_, 0, 1).
fast_power(A, B, R) :-
   B > 0, 1 is mod(B, 2),
   B1 is B - 1, fast_power(A, B1, R1),
   R is A * R1.
fast_power(A, B, R) :-
    B > 0, 0 is mod(B, 2),
    B2 is div(B, 2), fast_power(A, B2, R2),
    R is R2 * R2.

pow_check(R, N, R, P) :- 
    \+ 0 is mod(N, P).
pow_check(R, N, CUR, P) :-
    !, 0 is mod(N, P),
    N1 is N//P,
    CUR1 is CUR + 1,
    pow_check(R, N1, CUR1, P).

abs_div(R, N, P) :-
    \+ 0 is mod(N, P), !,
    R = N.
abs_div(R, N, P) :-
    N1 is div(N, P),
    abs_div(R, N1, P).

compact_product([], 1).
compact_product([(A, B) | T], R) :- compact_product(T, R1), fast_power(A, B, P), R is R1 * P.

compact_prime_divisors(1, []).
compact_prime_divisors(N, [(A, B) | T]) :-
	number(N), prime(N), !,
	A = N,
	B = 1,
	T = [].
compact_prime_divisors(N, [(A, B) | T]) :-
	number(N), !,
	min_div_prime(N, A),
    pow_check(B, N, 0, A),
    abs_div(N1, N, A),
	compact_prime_divisors(N1, T).
compact_prime_divisors(N, D) :- list(D), is_sorted_compact_primes(D), compact_product(D, N).