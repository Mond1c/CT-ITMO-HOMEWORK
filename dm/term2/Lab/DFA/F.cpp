#include <bits/stdc++.h>

struct hash {
    std::size_t operator()(const std::pair<int, char> &key) const {
        return std::hash<int>{}(key.first) + std::hash<char>{}(key.second);
    }
};

struct DFA {
    int n;
    std::vector<bool> end;
    std::unordered_map<std::pair<int, char>, int, hash> graph;
};

DFA read_dfa(std::ifstream &in) {
    int n, m, k;
    in >> n >> m >> k;
    DFA dfa;
    dfa.n = n;
    dfa.end = std::vector<bool>(n + 1);
    for (int i = 0; i < k; i++) {
        int a;
        in >> a;
        dfa.end[a] = true;
    }
    for (int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        dfa.graph[{a, c}] = b;
    }
    return dfa;
}

bool dfs(DFA &a, DFA &b, int u, int v, std::vector<bool> &visited) {
    visited[u] = true;
    if (a.end[u] != b.end[v]) {
        return false;
    }
    for (char ch = 'a'; ch <= 'z'; ch++) {
        if (!a.graph.count({u, ch}) && !b.graph.count({v, ch})) {
            continue;
        }
        if ((!a.graph.count({u, ch})) != (!b.graph.count({v, ch})) ||
            !visited[a.graph[{u, ch}]] && !dfs(a, b, a.graph[{u, ch}], b.graph[{v, ch}], visited)) {
            return false;
        }
    }
    return true;
}

void write_answer(const std::string &s) {
    std::ofstream out("isomorphism.out");
    out.write(s.c_str(), s.length());
    out.close();
}

int main() {
    std::ifstream in("isomorphism.in");
    DFA a = read_dfa(in);
    DFA b = read_dfa(in);
    std::vector<bool> visited(a.n);
    if (dfs(a, b, 1, 1, visited)) {
        write_answer("YES");
    } else {
        write_answer("NO");
    }
    return 0;
}