lookup(Key, [(Key, Value) | Tail], Value) :- !.
lookup(Key, [_ | Tail], Value) :- lookup(Key, Tail, Value).

variable(Name, variable(Name)).
const(Value, const(Value)).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, 0, A) :- !.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.

to_boolean(Integer, 0) :- Integer =< 0, !.
to_boolean(Integer, 1).

operation(op_not, A, 0) :- A > 0, !.
operation(op_not, A, 1).
operation(op_and, A, B, 1) :- to_boolean(A, A1), to_boolean(B, B1), A1 =:= 1, B1 =:= 1, !.
operation(op_and, A, B, 0).
operation(op_or, A, B, 1) :- to_boolean(A, A1), to_boolean(B, B1), A1 + B1 > 0, !.
operation(op_or, A, B, 0).
operation(op_xor, A, B, 1) :- to_boolean(A, A1), to_boolean(B, B1), 1 is A1 + B1, !.
operation(op_xor, A, B, 0).

evaluate(const(X), _, X).
evaluate(variable(X), Variables, R) :- atom_chars(X, [Head | _ ]), lookup(Head, Variables, R).
evaluate(operation(Oper, A, B), Variables, R) :- 
 evaluate(A, Variables, R1),
 evaluate(B, Variables, R2),
 operation(Oper, R1, R2, R).
evaluate(operation(Oper, A), Variables, R) :-
 evaluate(A, Variables, R1),
 operation(Oper, R1, R).

:- load_library('alice.tuprolog.lib.DCGLibrary').

non_var(V, _) :- var(V).
non_var(V, T) :- nonvar(V), call(T).

s --> [].
s --> [' '], s.

eat_variable_letters([]) --> [].
eat_variable_letters([H | Tail]) --> [H], {member(H, ['x', 'y', 'z', 'X', 'Y', 'Z'])}, eat_variable_letters(Tail).

infix_parse(variable(Name)) --> 
 { non_var(Name, atom_chars(Name, NameChars)) },
 s, eat_variable_letters(NameChars), s, 
 { Chars = [_ | _], atom_chars(Name, NameChars) }.

infix_parse(const(Value)) --> 
 { non_var(Value, number_chars(Value, Chars)) },
 s, digits_parse(Chars), s,
 { Chars = [_ | _], number_chars(Value, Chars)}.

digits_parse([]) --> [].
digits_parse(['-', H | T]) --> ['-'], digits_parse([H | T]).
digits_parse([H | T]) -->  
 { member(H, ['.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'])},
 [H], 
 digits_parse(T).

binop_parse(op_multiply) --> ['*'].
binop_parse(op_divide) --> ['/'].
binop_parse(op_add) --> ['+'].
binop_parse(op_subtract) --> ['-'].
unop_parse(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].

unop_parse(op_not) --> ['!'].
binop_parse(op_and) --> ['&', '&'].
binop_parse(op_or) --> ['|', '|'].
binop_parse(op_xor) --> ['^', '^'].

infix_parse(operation(Oper, A)) --> s, unop_parse(Oper), [' '], s, infix_parse(A), s.
infix_parse(operation(Oper, A, B)) --> s, ['('], s, infix_parse(A), [' '], s, binop_parse(Oper), [' '], s, infix_parse(B), s, [')'], s.

infix_str(Result, Str) :- ground(Result), phrase(infix_parse(Result), CharArrRes), atom_chars(Str, CharArrRes), !.
infix_str(Result, Str) :- atom(Str), atom_chars(Str, CharArr), phrase(infix_parse(Result), CharArr), !.
