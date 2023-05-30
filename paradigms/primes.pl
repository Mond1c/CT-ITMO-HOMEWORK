build_for_i_j(I, J, N) :-
	J < N,
	assert(composite(J)),
	add_min_div(J, I),
	J1 is J + I,
	build_for_i_j(I, J1, N).
build_for_i(I, N) :-
	\+ composite(I),
	I1 is I * I,
	build_for_i_j(I, I1, N).
build_for_i(I, N) :-
	I * I < N,
	I1 is I + 1,
	build_for_i(I1, N).

add_min_div(N, _) :- min_div(N, X), !.
add_min_div(N, I) :- assert(min_div(N, I)).

init(MAX_N) :- (build_for_i(2, MAX_N); true).

prime(N) :- N > 1, \+ composite(N).

is_min_div(P, P) :- prime(P), !.
is_min_div(N, P) :- min_div(N, P).

is_sorted_primes([N]) :- prime(N).
is_sorted_primes([H1, H2 | T]) :- H1 =< H2, prime(H1), is_sorted_primes([H2 | T]).

product([], 1).
product([H | T], R) :- product(T, R1), R is R1 * H.

prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :- number(N), !, is_min_div(N, H), N1 is div(N, H), prime_divisors(N1, T).
prime_divisors(N, L) :- list(L), is_sorted_primes(L), product(L, N).

pow2(_, 0, 1).
pow2(A, B, R) :-
   B > 0, 1 is mod(B, 2),
   B1 is B - 1, pow2(A, B1, R1),
   R is A * R1.
pow2(A, B, R) :-
    B > 0, 0 is mod(B, 2),
    B2 is div(B, 2), pow2(A, B2, R2),
    R is R2 * R2.


find_power(R, N, R, P) :- 
    \+ 0 is mod(N, P).
find_power(R, N, CUR, P) :-
    !, 0 is mod(N, P),
    N1 is N//P,
    CUR1 is CUR + 1,
    find_power(R, N1, CUR1, P).

is_sorted_compact_primes([(A, B)]) :- B > 0, prime(A).
is_sorted_compact_primes([(A1, B1), (A2, B2) | T]) :- A1 < A2, B1 > 0, prime(A1), is_sorted_compact_primes([(A2, B2) | T]).

compact_product([], 1).
compact_product([(A, B) | T], R) :- compact_product(T, R1), pow2(A, B, D), R is R1 * D.

compact_prime_divisors(1, []) :- !.
compact_prime_divisors(N, [(A, B) | T]) :-
	number(N), !, is_min_div(N, A),
    	find_power(B, N, 0, A),
	pow2(A, B, C),
	N1 is N // C,
	compact_prime_divisors(N1, T).
compact_prime_divisors(N, D) :- list(D), is_sorted_compact_primes(D), compact_product(D, N).
