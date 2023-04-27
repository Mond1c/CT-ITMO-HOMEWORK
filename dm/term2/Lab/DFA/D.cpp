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

int main() {
    int n, m, k, l;
    std::ifstream in("problem4.in");
    in >> n >> m >> k >> l;
    std::vector<int> end(k);
    for (int i = 0; i < k; i++) {
        int value;
        in >> value;
        end[i] = value - 1;
    }
    matrix<long long> mat(n, n);
    for (int i = 0; i < m; i++) {
        int a, b;
        char ch;
        in >> a >> b >> ch;
        mat[a - 1][b - 1]++;
    }
    in.close();
    mat = matrix<long long>::pow(mat, l);
    long long result = 0;
    for (int e: end) {
        result = (result + mat[0][e]) % MOD;
    }
    std::ofstream out("problem4.out");
    out << result;
    out.close();
}