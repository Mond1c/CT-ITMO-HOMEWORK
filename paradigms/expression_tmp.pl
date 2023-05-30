:- load_library('alice.tuprolog.lib.DCGLibrary').

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is A * -1.

evaluate(const(Value), _, Value).
evaluate(variable(Name), Args, R) :- lookup(Name, Args, R).
evaluate(operation(Op, A, B), Args, R) :-
	evaluate(A, Args, AV),
	evaluate(B, Args, BV),
	operation(Op, AV, BV, R).
evaluate(operation(Op, A), Args, R) :-
	evaluate(A, Args, AV),
	operation(Op, AV, R).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Name)) --> [Name], { member(Name, ['x', 'y', 'z']) }.
expr_p(const(Value)) -->
	{ nonvar(Value, number_chars(Value, Chars)) },
	digits_p(Chars),
	{ Chars = [_ | _], number_chars(Value, Chars) }.
digits_p_helper(['-' | T]) -->
	['-'], digits_p(T),
	{not(empty(T))}.
digits_p_helper(H) --->
	digits_p(H),
	{not(empty
digits_p([]) --> [].
digits_p([H | T]) -->
	{ member(H, ['.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'])},
	[H],
	digits_p(T).

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].

expr_p(operation(Op, A, B)) --> ['('], expr_p(A), op_p(Op), expr_p(B), [')'].
infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(expr_p(E), C).

