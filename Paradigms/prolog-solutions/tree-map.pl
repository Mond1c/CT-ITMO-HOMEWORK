%% avltree(Root, LeftSubTree, RightSubTree, Height).
node(Key, Value, avltree((Key, Value), null, null, 1)).

key(avltree((Key, _ ), _, _, _), Key).
value(avltree((_, Value), _, _, _), Value).
leftTree(avltree(_, Left, _, _), Left).
rightTree(avltree(_, _, Right, _), Right).
height(avltree(_, _, _, H), H).
height(null, 0).

balanceFactor(Lefty, Righty, Value) :- height(Lefty, LH), height(Righty, RH), Value is LH - RH.
balanceFactor(avltree(_, Lefty, Righty, _), Value) :- balanceFactor(Lefty, Righty, Value).

% ================ % Balance % ================ %

balance(null, null).

balance(avltree(X, L, R, H), Result) :-
	balance(L, LB), balance(R, RB),
	balanceBalanced(avltree(X, LB, RB, H), Result). 

balanceBalanced(Tree, Tree) :- % balanced already
	balanceFactor(Tree, Factor), abs(Factor) < 2, !.

balanceBalanced(Tree, BalancedTree) :- % rotate-left
	balanceFactor(Tree, Factor), -2 is Factor, 
	rightTree(Tree, RT), balanceFactor(RT, RFactor), RFactor < 1, !,
	rotate_left(Tree, BalancedTree).

balanceBalanced(Tree, BalancedTree) :- % rotate-right-left
	balanceFactor(Tree, Factor), -2 is Factor, 
	rightTree(Tree, RT), balanceFactor(RT, RFactor), 1 is RFactor, !,
	rotate_right_left(Tree, BalancedTree).

balanceBalanced(Tree, BalancedTree) :- % rotate-right
	balanceFactor(Tree, Factor), 2 is Factor, 
	leftTree(Tree, LT), balanceFactor(LT, LFactor), LFactor > -1, !,
	rotate_right(Tree, BalancedTree).
	
balanceBalanced(Tree, BalancedTree) :- % rotate-left-right
	balanceFactor(Tree, Factor), 2 is Factor, 
	leftTree(Tree, LT), balanceFactor(LT, LFactor), -1 is LFactor, !,
	rotate_left_right(Tree, BalancedTree).

% ================ % Rotations % ================ %

rotate_left(avltree(X, T1, avltree(Z, T2, T3, _), _), avltree(Z, avltree(X, T1, T2, XH), T3, H)) :- 
	height(T1, T1H), height(T2, T2H), height(T3, T3H), 
	maxWithIncrement(T1H, T2H, XH),
	maxWithIncrement(XH, T3H, H).

rotate_right(avltree(X, avltree(Z, T1, T2, _), T3, _), avltree(Z, T1, avltree(X, T2, T3, XH), H)) :-
	height(T1, T1H), height(T2, T2H), height(T3, T3H),
	maxWithIncrement(T2H, T3H, XH),
	maxWithIncrement(XH, T1H, H).

rotate_right_left(avltree(X, T1, avltree(Z, avltree(Y, T2, T3, _), T4, _), _), avltree(Y, avltree(X, T1, T2, XH), avltree(Z, T3, T4, ZH), H)) :-
	height(T1, T1H), height(T2, T2H), height(T3, T3H), height(T4, T4H),
	maxWithIncrement(T1H, T2H, XH),
	maxWithIncrement(T3H, T4H, ZH),
	maxWithIncrement(XH, ZH, H).

rotate_left_right(avltree(Z, avltree(X, T1, avltree(Y, T2, T3, _), _), T4, _), avltree(Y, avltree(X, T1, T2, XH), avltree(Z, T3, T4, ZH), H)) :-
	height(T1, T1H), height(T2, T2H), height(T3, T3H), height(T4, T4H),
	maxWithIncrement(T1H, T2H, XH),
	maxWithIncrement(T3H, T4H, ZH),
	maxWithIncrement(XH, ZH, H).

% ================ % Utility % ================ %

max(X, Y, X) :- X >= Y, !. 
max(X, Y, Y) :- X =< Y. 

maxWithIncrement(A, B, C) :- max(A, B, T), C is T + 1.

% ================ % User-Interface % ================ %

insert(null, Key, Value, avltree((Key, Value), null, null, 1)) :- !. % inserting in empty tree.
insert(avltree((RootKey, RootValue), L, R, H), Key, Value, avltree((RootKey, RootValue), NewL, R, NewH)) :- % Key not found: searching it in left side
	Key < RootKey, !, 
	insert(L, Key, Value, NewL),
	height(R, RH), height(NewL, NewLH), maxWithIncrement(RH, NewLH, NewH).

insert(avltree((RootKey, RootValue), L, R, H), Key, Value, avltree((RootKey, RootValue), L, NewR, NewH)) :- % Key not found: searching it in right side
	Key > RootKey, !, 
	insert(R, Key, Value, NewR),
	height(NewR, NewRH), height(L, LH), maxWithIncrement(NewRH, LH, NewH).

insert(avltree((Key, _), L, R, H), Key, Value, avltree((Key, Value), L, R, H)). % Key found: replacing it

map_put(Tree, Key, Value, Result) :-
	insert(Tree, Key, Value, UnbalancedResult),
	balance(UnbalancedResult, Result).

map_get(avltree((Key, Value), _, _, _), Key, Value) :- !. % Found key

map_get(avltree((RootKey, RootValue), L, _, _), Key, Value) :- % Key not found: searching it in left side
	Key < RootKey, !, 
	map_get(L, Key, Value).

map_get(avltree((RootKey, RootValue), _, R, _), Key, Value) :- % Key not found: searching it in right side
	Key > RootKey, !, 
	map_get(R, Key, Value).

map_build([], null) :- !. % Empty list generates empty tree
map_build([(Key, Value)], Tree) :- node(Key, Value, Tree), !. % One pair generates node.
map_build([(Key, Value) | Others], Tree) :- map_build(Others, Tree1), map_put(Tree1, Key, Value, Tree).

swap_with_minimum(KV, avltree(ResKV, null, null, H), ResKV, null) :- !. % node have no childs - end of recursion.

swap_with_minimum((Key, Value), avltree(ResKV, null, R, H), ResKV, NewThis) :- !, % node have right child - recursive call.
	tree_remove(avltree((Key, Value), null, R, H), Key, NewThis).

swap_with_minimum(KV, avltree(THISKV, L, R, H), ResKV, avltree(THISKV, NewL, R, NewH)) :- % finding minimum: going left.
	swap_with_minimum(KV, L, ResKV, NewL), 
	height(NewL, NewLH), height(R, RH), maxWithIncrement(NewLH, RH, NewH).

tree_remove(avltree((RootKey, RootValue), L, R, H), Key, avltree((RootKey, RootValue), NewL, R, NewH)) :- % finding delete-node 
	Key < RootKey, !,
	tree_remove(L, Key, NewL),
	height(NewL, NewLH), height(R, RH), maxWithIncrement(NewLH, RH, NewH).

tree_remove(avltree((RootKey, RootValue), L, R, H), Key, avltree((RootKey, RootValue), L, NewR, NewH)) :- % finding delete-node
	Key > RootKey, !,
	tree_remove(R, Key, NewR),
	height(L, LH), height(NewR, NewRH), maxWithIncrement(LH, NewRH, NewH).
	
tree_remove(avltree((Key, Value), null, null, 1), Key, null) :- !. % leaf. no swaps required.

tree_remove(avltree((Key, Value), L, null, H), Key, avltree((LKey, LValue), NewL, null, NewH)) :- !, % there's no right son - swapping with left son
	key(L, LKey), value(L, LValue), leftTree(L, LeftLeft), rightTree(L, LeftRight), height(L, LH),
	tree_remove(avltree((Key, Value), LeftLeft, LeftRight, LH), Key, NewL),
	height(NewL, NewLH), maxWithIncrement(NewLH, 0, NewH).

tree_remove(avltree((Key, Value), null, R, H), Key, avltree((RKey, RValue), null, NewR, NewH)) :- !, % there's no left son - swappnig with right one
	key(R, RKey), value(R, RValue), leftTree(R, RightLeft), rightTree(R, RightRight), height(R, RH),
	tree_remove(avltree((Key, Value), RightLeft, RightRight, RH), Key, NewR),
	height(NewR, NewRH), maxWithIncrement(NewRH, 0, NewH).

tree_remove(avltree(KV, L, R, H), Key, avltree(ResKV, L, NewR, NewH)) :- % key found, but there's left and right subtrees - swapping with minimum in right subtree.
	swap_with_minimum(KV, R, ResKV, NewR),
	height(L, LH), height(NewR, NewRH), maxWithIncrement(LH, NewRH, NewH).

tree_remove(null, V, null). % Removing null is null (key wasn't found).

map_remove(Tree, Key, Result) :-
	tree_remove(Tree, Key, UnbalancedResult),
	balance(UnbalancedResult, Result).
 
insertIfAbsent(null, Key, Value, avltree((Key, Value), null, null, 1)) :- !. % inserting in empty tree.
insertIfAbsent(avltree((RootKey, RootValue), L, R, H), Key, Value, avltree((RootKey, RootValue), NewL, R, NewH)) :- % Key not found: searching it in left side
	Key < RootKey, !, 
	insertIfAbsent(L, Key, Value, NewL),
	height(R, RH), height(NewL, NewLH), maxWithIncrement(RH, NewLH, NewH).

insertIfAbsent(avltree((RootKey, RootValue), L, R, H), Key, Value, avltree((RootKey, RootValue), L, NewR, NewH)) :- % Key not found: searching it in right side
	Key > RootKey, !, 
	insertIfAbsent(R, Key, Value, NewR),
	height(NewR, NewRH), height(L, LH), maxWithIncrement(NewRH, LH, NewH).

insertIfAbsent(avltree((Key, OldValue), L, R, H), Key, Value, avltree((Key, OldValue), L, R, H)). % Key found: nothing to do

map_putIfAbsent(Tree, Key, Value, Result) :-
	insertIfAbsent(Tree, Key, Value, UnbalancedResult),
	balance(UnbalancedResult, Result).