#include <bits/stdc++.h>
#define ll long long

const char OPERATIONS[] = {'+', '-', '*', '/'};


int get_priority(char ch) {
	if (ch == '*' || ch == '/') return 2;
	if (ch == '+' || ch == '-') return 1;
	return 0;
}

bool is_operation(char ch) {
	return std::find(&OPERATIONS[0], &OPERATIONS[4], ch) != &OPERATIONS[4];
}

ll calc(ll x, ll y, char operation) {
	switch (operation) {
	case '+': return x + y;
	case '-': return x - y;
	case '*': return x * y;
	case '/': return x / y;
	}
}

bool extract(std::stack<ll>& values, std::stack<char>& operations) {
	if (values.size() < 2 || operations.empty()) {
		return false;
	}
	ll y = values.top(); values.pop();
	ll x = values.top(); values.pop();
	char operation = operations.top(); operations.pop();
	values.push(calc(x, y, operation));
	return true;
}

int main() {
	std::istringstream ss;
	std::string expression;
	std::getline(std::cin, expression);
	std::stack<ll> values;
	std::stack<char> operations;
	for (int i = 0; i < expression.size(); ++i) {
		char ch = expression[i];
		if (ch == ' ') continue;
		if (ch == '(') {
			operations.push(ch);
		} else if (ch == ')') {
			while (!operations.empty() && operations.top() != '(') {
				if (!extract(values, operations)) {
					std::cout << "WRONG" << std::endl;
					return 0;
				}
			}
			if (!operations.empty()) {
				operations.pop();
			}
		} else if (is_operation(ch)) {
			while (!operations.empty() && get_priority(operations.top()) >= get_priority(ch)) {
				if (!extract(values, operations)) {
					std::cout << "WRONG" << std::endl;
					return 0;
				}
			}
			operations.push(ch);
		} else if (isdigit(ch)) {
			ll value = 0;
			while (i < expression.size() && isdigit(expression[i])) {
				value = (value * 10) + (expression[i++] - '0');
			}
			values.push(value);
			--i;
		} else {
			std::cout << "WRONG" << std::endl;
			return 0;
		}
	}
	while (!operations.empty()) {
		if (!extract(values, operations)) {
			std::cout << "WRONG" << std::endl;
			return 0;
		}
	}
	if (values.size() != 1) {
		std::cout << "WRONG" << std::endl;
		return 0;
	}
	std::cout << values.top() << std::endl;
	return 0;
}