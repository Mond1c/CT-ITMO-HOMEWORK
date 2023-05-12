#include <bits/stdc++.h>

const std::string OUTPUT_FILE = "problem3.out";
  
void write_answer(const std::string& s) {
    std::ofstream out(OUTPUT_FILE);
    out.write(s.c_str(), s.length());
    out.close();
}

void dfs(int v, std::vector<std::vector<int>>& dfa, std::vector<int>& d) {
    if (d[v] > 0) {
        return;
    }
    d[v] = 1;
    for (int i : dfa[v]) {
        dfs(i, dfa, d);
    }
}

bool inf(int v, std::vector<std::vector<int>>& dfa, std::vector<int>& d) {
    if (d[v] == 0) return false;
    if (d[v] == 2) return true;
    d[v] = 2;
    for (int i : dfa[v]) {
        if (inf(i, dfa, d)) {
            d[v] = 1;
            return true;
        }
    } 
    d[v] = 1;
    return false;
}

int count(int v, std::vector<std::vector<int>>& dfa, std::set<int>& end, std::vector<int>& d) {
    if (d[v] == 0) return 0;
    int res = 0;
    if (end.find(v) != end.end()) {
        ++res;
    }
    for (int i : dfa[v]) {
        res = (res + count(i, dfa, end, d)) % 1'000'000'007;
    }
    return res;
}

int main() {
    int n, m, k;
    std::ifstream in("problem3.in");
    in >> n >> m >> k;
    std::set<int> end;
    for (int i = 0; i < k; i++) {
        int value;
        in >> value;
        end.insert(value - 1);
    }
    std::vector<std::vector<int>> dfa(n);
    std::vector<std::vector<int>> dfa_rev(n);
    for (int i = 0; i < m; i++) {
        int a, b;
        in >> a >> b;
        char c;
        in >> c;
        dfa[a - 1].push_back(b - 1);
        dfa_rev[b - 1].push_back(a - 1);
    }
    std::vector<int> d(n);
    for (int i : end) {
        dfs(i, dfa_rev, d);
    }
    if (inf(0, dfa, d)) {
        write_answer("-1");
    } else {
        write_answer(std::to_string(count(0, dfa, end, d)));
    }
    return 0;
}