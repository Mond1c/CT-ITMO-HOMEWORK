#include <bits/stdc++.h>
 
const std::string OUTPUT_FILE = "problem1.out";
 
void write_answer(const std::string& s) {
    std::ofstream out(OUTPUT_FILE);
    out.write(s.c_str(), s.length());
    out.close();
}
 

struct hash {
    std::size_t operator()(const std::pair<int, char>& key) const {
        return std::hash<int>{}(key.first) + std::hash<char>{}(key.second);
    }
};

int main() {
    std::ifstream in("problem1.in");
    std::string str;
    in >> str;
    int n, m, k;
    in >> n >> m >> k;
    std::vector<bool> end(n+1);
    std::unordered_map<std::pair<int, char>, int, hash> graph;
    for (int i = 0; i < k; i++) {
        int a;
        in >> a;
        end[a] = true;
    }
    for (int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        graph[{a, c}] = b;
    }
    in.close();
    int s = 1;
    int p = 0;
    while (true) {
        if (p >= str.length()) {
            if (end[s]) {
                write_answer("Accepts");
                return 0;
            } else {
                write_answer("Rejects");
                return 0;
            }
        }
        if (graph.count({s, str[p]})) {
            s = graph[{s, str[p]}];
        } else {
            write_answer("Rejects");
            return 0;
        }
        p++;
    }
}