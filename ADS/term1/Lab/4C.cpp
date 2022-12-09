#include <bits/stdc++.h>

const std::vector<char> operators = {'+', '-', '*', '/'};

int calc(int x, int y, char op);

int main4C() {
    std::cin.tie(nullptr);
    std::ios::sync_with_stdio(false);
    std::string expression;
    std::getline(std::cin, expression);
    std::stack<int> operands;
    std::stack<char> ops;
    std::string num;
    int bracket = 0;
    for (char i : expression) {
        if (!std::isdigit(i) && !num.empty()) {
            operands.push(std::stoi(num));
            num = "";
        }
        if (i == '(') {
            ops.push(i);
            bracket++;
        } else if (i == ')') {
            while (!ops.empty() && ops.top() != '(') {
                if (operands.empty()) {
                    std::cout << "WRONG" << std::endl;
                    return 0;
                }
                int x = operands.top(); operands.pop();
                if (operands.empty()) {
                    std::cout << "WRONG" << std::endl;
                    return 0;
                }
                int y = operands.top(); operands.pop();
                char op = ops.top(); ops.pop();
                operands.push(calc(x, y, op));
            }
            if (!ops.empty()) ops.pop();
            bracket--;
        } else if (std::find(operators.begin(), operators.end(), i) != operators.end()) {
            if (ops.size() - bracket > (operands.size() / 2 + (operands.size() % 2))) {
                std::cout << "WRONG" << std::endl;
                return 0;
            }
            while (!ops.empty() && (ops.top() == '*' || ops.top() == '/')) {
                if (operands.empty()) {
                    std::cout << "WRONG" << std::endl;
                    return 0;
                }
                int x = operands.top(); operands.pop();
                if (operands.empty()) {
                    std::cout << "WRONG" << std::endl;
                    return 0;
                }
                int y = operands.top(); operands.pop();
                char op = ops.top(); ops.pop();
                operands.push(calc(x, y, op));
            }
            ops.push(i);
        } else if (std::isdigit(i)) {
            if (ops.size()  < (operands.size() / 2 + (operands.size() % 2))) {
                std::cout << "WRONG" << std::endl;
                return 0;
            }
            num.push_back(i);
        } else if (i != ' ') {
            std::cout << "WRONG" << std::endl;
            return 0;
        }

    }
    if (!num.empty()) {
        operands.push(std::stoi(num));
    }
    while (!ops.empty()) {
        if (operands.empty()) {
            std::cout << "WRONG" << std::endl;
            return 0;
        }
        int x = operands.top(); operands.pop();
        if (operands.empty()) {
            std::cout << "WRONG" << std::endl;
            return 0;
        }
        int y = operands.top(); operands.pop();
        char op = ops.top(); ops.pop();
        operands.push(calc(x, y, op));
    }
    std::cout << operands.top() << std::endl;
    return 0;
}

int calc(int x, int y, char op) {
    switch (op) {
        case '+': return x + y;
        case '-': return y - x;
        case '*': return x * y;
        case '/': return y / x;
        default: return 0;
    }
}
