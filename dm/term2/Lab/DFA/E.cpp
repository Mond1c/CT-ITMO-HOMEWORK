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
                    result.data_[i][j] = (result.data_[i][j] + (left.data_[i][k] * right.data_[k][j])) % MOD;
                }
            }
        }
        return result;
    }

    friend std::ostream& operator<<(std::ostream& out, const matrix& mat) {
        for (int i = 0; i < mat.n; i++) {
            for (int j = 0; j < mat.m; j++) {
                out << mat[i][j] << " ";
            }
            out << std::endl;
        }
        return out;
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
    
    std::vector<T> operator[](size_t index) const {
        return data_[index];
    }
 
private:
    std::vector<std::vector<T>> data_;
    std::size_t n;
    std::size_t m;
};

struct hash {
    size_t operator()(const std::pair<int, char>& key) const {
        return std::hash<int>{}(key.first) + std::hash<char>{}(key.second);
    }
};

std::map<std::pair<int, char>, int> convert(std::map<std::pair<int, char>, std::set<int>>& nfa, std::set<char> chars,
std::vector<int>& ends, int& n) {
    std::map<std::pair<int, char>, int> dfa;
    std::queue<std::set<int>> states;
    std::vector<int> new_ends;
    std::map<std::set<int>, int> mp;
    std::set<std::set<int>> visisted;
    int i = 0;
    states.push({1});
    while (!states.empty()) {
        auto cur = states.front(); states.pop();
        for (char ch : chars) {
            std::set<int> s;
            bool isEnd = false;
            for (int state : cur) {
                if (nfa.find({state, ch}) != nfa.end()) {
                    for (int p : nfa[{state, ch}]) {
                        s.insert(p);
                        if (std::find(ends.begin(), ends.end(), p) != ends.end()) {
                            isEnd = true;
                        }
                    }
                }
            }
            if (!s.empty()) {
                if (mp.find(cur) == mp.end()) {
                    mp[cur] = i++;
                }
                if (mp.find(s) == mp.end()) {
                    mp[s] = i++;
                }
                int j = mp[cur];
                dfa[{j, ch}] = mp[s];
                if (isEnd) {
                    new_ends.push_back(mp[s]);
                }
                if (visisted.find(s) == visisted.end()) {
                    visisted.insert(s);
                    states.push(s);
                }
            }
        }
    }
    n = i;
    ends = new_ends;
    return dfa;
}

int main() {
    int n, m, k, l;
    std::ifstream in("problem5.in");
    in >> n >> m >> k >> l;
    std::vector<int> end(k);
    for (int i = 0; i < k; i++) {
        int value;
        in >> value;
        end[i] = value;
    }
    std::map<std::pair<int, char>, std::set<int>> graph;
    std::set<char> chars;
    for (int i = 0; i < m; i++) {
        int a, b;
        char ch;
        in >> a >> b >> ch;
        chars.insert(ch);
        graph[{a, ch}].insert(b);
    }
    in.close();
    std::map<std::pair<int, char>, int> dfa = convert(graph, chars, end, n);
    //std::cout << dfa.size() << std::endl;
    matrix<long long> mat(n, n);
    for (const auto& item : dfa) {
        //std::cout << item.first.first - 1 << " " << item.second - 1 << std::endl;
        mat[item.first.first][item.second]++;
    }
    //std::cout << mat << std::endl;
    mat = matrix<long long>::pow(mat, l);
    long long result = 0;
    //std::cout << mat << std::endl;
    std::set<int> ends(end.begin(), end.end());
    for (int e : ends) {
     //   std::cout << e << std::endl;
        result = (result + mat[0][e]) % MOD;
    }
    std::ofstream out("problem5.out");
    out << result;
    out.close();
}
