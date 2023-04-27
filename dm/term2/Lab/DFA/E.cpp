#include <bits/stdc++.h>

const int MOD = 1'000'000'007;

template<class T>
struct matrix {
public:
    matrix(std::size_t n, std::size_t m) : data_(std::vector<std::vector<T>>(n, std::vector<T>(m))),
                                           n(n), m(m) {}

public:
    friend matrix operator*(const matrix &left, const matrix &right) {
        matrix result(left.n, right.m);
        for (int i = 0; i < left.n; i++) {
            for (int j = 0; j < right.m; j++) {
                result.data_[i][j] = T();
                for (int k = 0; k < left.m; k++) {
                    result.data_[i][j] = (result.data_[i][j] + (left.data_[i][k] * right.data_[k][j]) % MOD) % MOD;
                }
            }
        }
        return result;
    }

    static matrix pow(matrix m, int power) {
        if (power == 1) {
            return m;
        }
        if (power % 2 == 0) {
            return pow(m * m, power / 2);
        }
        return pow(m, power - 1) * m;
    }

    std::vector<T> &operator[](size_t index) {
        return data_[index];
    }

private:
    std::vector<std::vector<T>> data_;
    std::size_t n;
    std::size_t m;
};

struct hash {
    std::size_t operator()(const std::pair<int, char> &key) const {
        return std::hash<int>{}(key.first) + std::hash<char>{}(key.second);
    }
};

struct stHash {
    std::size_t operator()(const std::unordered_set<int> &key) const {
        std::size_t ans = 0;
        for (int i : key) {
            ans += std::hash<int>{}(i);
        }
        return ans;
    }
};

void convert_to_dfa(int s, std::unordered_set<char>& alp, std::vector<int> &end,
                    std::unordered_map<std::pair<int, char>, std::unordered_set<int>, hash> &graph, int& n) {
    std::queue<std::unordered_set<int>> P;
    P.push({s});
    std::unordered_set<std::unordered_set<int>, stHash> Q;
    std::unordered_map<std::pair<int, char>, std::unordered_set<int>, hash> ans_graph;
    while (!P.empty()) {
        auto pd = P.front();
        P.pop();
        for (char c : alp) {
            std::unordered_set<int> q;
            for (int p : pd) {
                q.insert(graph[{p, c}].begin(), graph[{p, c}].end());
            }
            for (int p : pd) {
                ans_graph[{p, c}] = q;
            }
            if (!Q.count(q)) {
                P.push(q);
                Q.insert(q);
            }
        }
    }
    std::unordered_set<int> ans_end;
    std::unordered_set<int> count;
    for (const auto& q : Q) {
        for (int p : end) {
            if (q.count(p)) {
                for (int qd : q) {
                    ans_end.insert(qd);
                }
                break;
            }
        }
        for (int qd : q) {
            count.insert(qd);
        }
    }
    ans_graph;
    end = std::vector<int>(ans_end.begin(), ans_end.end());
    n = count.size();
}


int main() {
    int n, m, k, l;
    std::ifstream in("problem5.in");
    in >> n >> m >> k >> l;
    std::vector<int> end(k);
    std::unordered_map<std::pair<int, char>, std::unordered_set<int>, hash> graph;
    std::unordered_set<char> alp;
    for (int i = 0; i < k; i++) {
        int value;
        in >> value;
        end[i] = value - 1;
    }
    // matrix<long long> mat(n, n);
    for (int i = 0; i < m; i++) {
        int a, b;
        char ch;
        in >> a >> b >> ch;
        alp.insert(ch);
        graph[{a - 1, ch}].insert(b - 1);
    }
    in.close();
    convert_to_dfa(0, alp, end, graph, n);
    matrix<long long> mat(n, n);
    for (const auto& p : graph) {
        if (!p.second.empty()) {
            mat[p.first.first - 1][*p.second.begin() - 1]++;
        }
    }
    mat = matrix<long long>::pow(mat, l);
    long long result = 0;
    for (int e: end) {
        result = (result + mat[0][e - 1]) % MOD;
    }
    std::ofstream out("problem5.out");
    out << result;
    out.close();
}