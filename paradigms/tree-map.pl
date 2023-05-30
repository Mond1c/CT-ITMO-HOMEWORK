% Node:
% key 
% value
% left
% right
% height

height(null, 0) :- !.
height(node(_, _, _, _, H), H) :- !.

max_value(A, B, R) :- (A > B -> R = A; R = B).

balance(node(_, _, Left, Right, _), R) :-
	height(Left, LeftHeight),
	height(Right, RightHeight),
	R is LeftHeight - RightHeight.

correct_height(node(Key, Value, Left, Right, _), node(Key, Value, Left, Right, NewHeight)) :-
	height(Left, LeftHeight),
	height(Right, RightHeight),
	max_value(LeftHeight, RightHeight, R),
	NewHeight is R + 1.

rotate_left(node(Key, Value, Left, node(RightKey, RightValue, RightLeft, RightRight, _), _), R) :-
	correct_height(node(Key, Value, Left, RightLeft, _), R1),
	correct_height(node(RightKey, RightValue, R1, RightRight, _), R).

rotate_right(node(Key, Value, node(LeftKey, LeftValue, LeftLeft, LeftRight, _), Right, _), R) :-
	correct_height(node(Key, Value, LeftRight, Right, _), R1),
	correct_height(node(LeftKey, LeftValue, LeftLeft, R1, _), R).



big_rotate(Node, Node) :- !.

big_rotate(node(Key, Value, Left, Right, H), R) :-
	balance(node(_, _, Left, Right, _), Balance),
	Balance < -1,
	balance(Right, RightBalance),
	(RightBalance =< 1 ->
	rotate_left(node(Key, Value, Left, Right, H), R);
	rotate_right(Right, R1),
	rotate_left(node(Key, Value, Left, R1, H), R)), !.

big_rotate(node(Key, Value, Left, Right, H), R) :-
	balance(node(_, _, Left, Right, _), Balance),
	Balance > 1,
	balance(Left, LeftBalance),
	(LeftBalance >= 1 ->
	rotate_right(node(Key, Value, Left, Right, H), R);
	rotate_left(Left, L),
	rotate_right(node(Key, Value, L, Right, H), R)), !.



map_put(null, Key, Value, R) :- R = node(Key, Value, null, null, 1).
map_put(node(Key, _, Left, Right, H), K, V, R) :-
	Key = K, !,
	R = node(Key, V, Left, Right, H).
map_put(node(Key, Value, Left, Right, H), K, V, R) :-
	Key > K, !,
	map_put(Left, K, V, R1),
	correct_height(node(Key, Value, R1, Right, H), R2),
	big_rotate(R2, R).
map_put(node(Key, Value, Left, Right, H), K, V, R) :-
	Key =< K, !,
	map_put(Right, K, V, R1),
	correct_height(node(Key, Value, Left, R1, H), R2),
	big_rotate(R2, R).

map_build([], null) :- !.

map_build([(Key, Value) | T], TreeMap) :-
	map_build(T, Root),
	map_put(Root, Key, Value, R1),
	correct_height(R1, R2),
	big_rotate(R2, TreeMap).

map_get(node(Key, Value, _, _, _), K, V) :-
	Key = K, !,
	Value = V.

map_get(node(Key, _, Left, _, _), K, V) :-
	Key > K, !,
	map_get(Left, K, V).
map_get(node(Key, _, _, Right, _), K, V) :-
	Key < K, !,
	map_get(Right, K, V).

map_remove(null, _, null) :- !.

map_remove(node(Key, Value, Left, Right, H), K, R) :-
	K < Key, !,
	map_remove(Left, K, R1),
	correct_height(node(Key, Value, R1, Right, H), R2),
	big_rotate(R2, R).
map_remove(node(Key, Value, Left, Right, H), K, R) :-
	K > Key, !,
	map_remove(Right, K, R1),
	correct_height(node(Key, Value, Left, R1, H), R2),
	big_rotate(R2, R).

get_min(Node, Node) :- !.
get_min(node(_, _, Left, _, _), R) :-
	\+ Left = null, !,
	get_min(Left, R).


map_remove(node(Key, _, null, null, _), K, null) :- Key = K, !.
map_remove(node(Key, _, Left, null, _), K, Left) :- Key = K, !.
map_remove(node(Key, _, null, Right, _), K, Right) :- Key = K, !.
map_remove(node(Key, _, Left, Right, H), K, R) :-
	Key = K, !,
	get_min(Right, node(Key1, Value1, _, _, _)),
	map_remove(Right, Key1, R1),
	correct_height(node(Key1, Value1, Left, R1, H), R2),
	big_rotate(R2, R).
map_putIfAbsent(null, Key, Value, R) :- R = node(Key, Value, null, null, 1).
map_putIfAbsent(node(Key, Value, Left, Right, H), K, V, R) :-
	Key = K, !,
	R = node(Key, Value, Left, Right, H).
map_putIfAbsent(node(Key, Value, Left, Right, H), K, V, R) :-
	Key > K, !,
	map_putIfAbsent(Left, K, V, R1),
	correct_height(node(Key, Value, R1, Right, H), R2),
	big_rotate(R2, R).
map_putIfAbsent(node(Key, Value, Left, Right, H), K, V, R) :-
	Key =< K, !,
	map_putIfAbsent(Right, K, V, R1),
	correct_height(node(Key, Value, Left, R1, H), R2),
	big_rotate(R2, R).
