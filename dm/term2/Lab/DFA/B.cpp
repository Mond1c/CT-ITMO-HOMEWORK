#include <bits/stdc++.h>

const std::string OUTPUT_FILE = "problem2.out";

void write_answer(const std::string &s) {
    std::ofstream out(OUTPUT_FILE);
    out.write(s.c_str(), s.length());
    out.close();
}


struct hash {
    std::size_t operator()(const std::pair<int, char> &key) const {
        return std::hash<int>{}(key.first) + std::hash<char>{}(key.second);
    }
};

int main() {
    std::ifstream in("problem2.in");
    std::string str;
    in >> str;
    int n, m, k;
    in >> n >> m >> k;
    std::vector<bool> end(n + 1);
    std::unordered_map<std::pair<int, char>, std::unordered_set<int>, hash> graph;
    for (int i = 0; i < k; i++) {
        int a;
        in >> a;
        end[a] = true;
    }
    for (int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        graph[{a, c}].insert(b);
    }
    in.close();

    std::unordered_set<int> st;
    int s = 1;
    st.insert(s);
    for (char ch : str) {
        std::unordered_set<int> tmp;
        for (int i : st) {
            std::pair<int, char> pair = {i, ch};
            if (graph.count(pair)) {
                tmp.insert(graph[pair].begin(), graph[pair].end());
            }
        }
        st = tmp;
    }

    for (int i = 0; i < end.size(); i++) {
        if (st.count(i)) {
            write_answer("Accepts");
            return 0;
        }
    }

    write_answer("Rejects");
    return 0;
}