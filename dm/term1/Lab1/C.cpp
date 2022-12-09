#include <iostream>
#include <vector>
#include <cmath>
#include <string>
#include <algorithm>
#include <bitset>

struct Node;

std::vector<Node*> NODES;

struct Node {
	std::vector<int> ins;
	std::vector<int> outs;
	bool is_var;
	int value=0;

	Node(bool is_var = true, std::vector<int> ins = {}, std::vector<int> outs = {})
		: ins(ins), outs(outs), is_var(is_var) {}

	int getValue() {
		//std::cout << NODES.size() << std::endl;
		if (is_var) return value;
		std::string res;
		int p = ins.size() - 1;
		int index = 0;
		for (int i = 0; i < ins.size(); ++i) {
			int v = NODES[ins[i] - 1]->getValue();
			index += v * std::pow(2, p--);
		}
		//std::cout << res << std::endl;
		//std::cout << index << std::endl;
		return outs[index];
	}
};

class Tree {
private:
	int pos = 0;
	std::vector<Node*> nodes;
	std::vector<Node*> variables;
	Node* root;
private:
	int dfs(Node* cur) {
		if (cur == nullptr) return 0;
		int depth = 0;
		for (int i = 0; i < cur->ins.size(); ++i) {
			depth = std::max(depth, dfs(nodes[cur->ins[i] - 1]));
		}
		return depth + 1;
	}
public:
	~Tree() {
		for (int i = 0; i < nodes.size(); ++i)
			delete nodes[i];
	}

	void addNode(Node* ptr) {
		nodes.push_back(ptr);
		if (ptr->is_var) variables.push_back(ptr);
	}

	void update() {
		root = nodes.back();
		NODES = this->nodes;
	}

	int getDepth() {
		return dfs(root);
	}

	int getValue() {
		return root->getValue();
	}

	void setValue(int value) {
		variables[pos++ % variables.size()]->value = value;
	}
};

int main() {
	std::cin.tie(nullptr);
	std::ios::sync_with_stdio(false);
	Tree tree;
	int n;
	std::cin >> n;
	int variables = 0;
	for (int i = 0; i < n; ++i) {
		int m;
		std::cin >> m;
		if (m == 0) {
			tree.addNode(new Node());
			++variables;
		}
		else {
			std::vector<int> ins(m);
			std::vector<int> outs(std::pow(2, m));
			for (int j = 0; j < m; ++j)
				std::cin >> ins[j];
			for (int j = 0; j < outs.size(); ++j)
				std::cin >> outs[j];
			tree.addNode(new Node(false, ins, outs));
		}
	}
	tree.update();
	std::cout << tree.getDepth() - 1 << std::endl;
	for (int i = 0; i < std::pow(2, variables); ++i) {
		for (int j = 0; j < variables; ++j) {
			int value = (i & (1 << (variables - j - 1))) != 0;
			tree.setValue(value);
		}
		std::cout << tree.getValue();
	}
	std::cout << std::endl;
}