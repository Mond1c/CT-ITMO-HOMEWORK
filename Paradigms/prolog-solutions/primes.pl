prime(N) :- prime_table(N), !. % Read from table
prime(2).
prime(N) :-
  N > 2,
  1 is N mod 2,
  not composite_run(N, 3),
  assert(prime_table(N)). % Write to table

composite_run(A, D) :- D * D =< A, 0 is A mod D, !.
composite_run(A, D) :- D * D =< A, D1 is D + 2, composite_run(A, D1).
 
composite(A) :- not prime(A).

prime_divisors(A, D) :- number(A), !, prime_divisors_run(A, 2, D).
prime_divisors(A, D) :- is_increasing(D), is_every_prime(D), multiply(D, A).

is_increasing([]).
is_increasing([H]).
is_increasing([F, S | T]) :- F =< S, is_increasing([S | T]).

is_every_prime([]).
is_every_prime([H | T]) :- prime(H), is_every_prime(T).

multiply([], 1).
multiply([H | T], R) :- multiply(T, R1), R is R1 * H.

prime_divisors_run(1, _, []) :- !.
prime_divisors_run(N, D, [D | R1]) :-
  0 is N mod D, !,
  N1 is N // D,
  prime_divisors_run(N1, D, R1).
prime_divisors_run(N, D, R) :-
  N mod D > 0,
  D * D < N,
  D1 is D + 1,
  prime_divisors_run(N, D1, R).
prime_divisors_run(N, D, [N]) :- D * D >= N, N > 1.

compact_prime_divisors(A, D) :- 
  not number(A), !, 
  is_increasing_pairs(D), 
  is_first_primes(D), 
  multiply_pair_list(A, D).
compact_prime_divisors(A, D) :- 
  prime_divisors(A, Divisors), compactize(Divisors, D).

is_increasing_pairs([]).
is_increasing_pairs([(H, C)]).
is_increasing_pairs([(H1, C1), (H2, C2) | Tail]) :- 
  H1 < H2, C1 > 0, C2 > 0, is_increasing_pairs([(H2, C2) | Tail]).

is_first_primes([]).
is_first_primes([(H, C) | Tail]) :- prime(H), is_first_primes(Tail).

fast_power(_, 0, 1) :- !.
fast_power(A, B, R) :-
  B > 0, 1 is mod(B, 2), !,
  B1 is B - 1, fast_power(A, B1, R1),
  R is A * R1.
fast_power(A, B, R) :-
  B > 0, 0 is mod(B, 2),
  B2 is div(B, 2), fast_power(A, B2, R2),
  R is R2 * R2.

multiply_pair_list(1, []) :- !.
multiply_pair_list(R, [(H, C) | Tail]) :- 
  multiply_pair_list(R1, Tail), fast_power(H, C, R2), R is R1 * R2.

compactize([], []).
compactize([D], [(D, 1)]).
compactize([D, D | Tail], [(D, P) | Result]) :- 
  !, 
  compactize([D | Tail], [(D, P1) | Result]), 
  P is P1 + 1.
compactize([D, OtherD | Tail], [(D, 1) | Result]) :- 
  compactize([OtherD | Tail], Result).
