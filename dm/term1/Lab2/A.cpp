#include <bits/stdc++.h>
#define ull unsigned long long

struct Node {
    ull weight;
    std::shared_ptr<Node> left;
    std::shared_ptr<Node> right;

    explicit Node(ull weight) : weight(weight) {}
};

int mainA() {
    auto compare = [](const std::shared_ptr<Node>& lhs, const std::shared_ptr<Node>& rhs) {
        return lhs->weight > rhs->weight;
    };
    std::priority_queue<std::shared_ptr<Node>, std::vector<std::shared_ptr<Node>>, decltype(compare)> p(compare);
    int n;
    std::cin >> n;
    for (int i = 0; i < n; ++i) {
        ull w;
        std::cin >> w;
        p.push(std::make_shared<Node>(w));
    }
    while (p.size() > 1) {
        auto a = p.top(); p.pop();
        auto b = p.top(); p.pop();
        auto c = std::make_shared<Node>(a->weight + b->weight);
        c->left = a;
        c->right = b;
        p.push(c);
    }
    ull answer = 0;
    int level;
    std::queue<std::shared_ptr<Node>> q;
    q.push(p.top());
    while (!q.empty()) {
        std::queue<std::shared_ptr<Node>> c;
        while (!q.empty()) {
            auto a = q.front(); q.pop();
            if (!a->left && !a->right) answer += a->weight * level;
            if (a->left) c.push(a->left);
            if (a->right) c.push(a->right);
        }
        q = c;
        ++level;
    }
    std::cout << answer << std::endl;
    return 0;
}
