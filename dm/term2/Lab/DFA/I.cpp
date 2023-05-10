#include <bits/stdc++.h>

struct RevVertex {
    bool terminal;
    std::unordered_map<char, std::set<int>> t;
};

struct Vertex {
    bool terminal;
    std::unordered_map<char, int> t;
};

int n, m, k;
std::vector<RevVertex> graph_rev;
std::vector<Vertex> graph;
std::vector<int> visited;

void make_ans(int v, std::vector<Vertex>& ans, const std::vector<Vertex>& dfa, std::unordered_map<int, int>& mp) {
    int s = mp.size();
    ans[s].terminal = dfa[v].terminal;
    visited[v] = 2;
    mp[v] = s;
    for (const auto& item : dfa[v].t) {
        if (visited[item.second] != 2) {
            make_ans(item.second, ans, dfa, mp);
        }
        ans[s].t[item.first] = mp[item.second];
    } 
}

void dfs(int v) {
    visited[v] = 1;
    for (const auto& vertices : graph_rev[v].t) {
        for (int vertex : vertices.second) {
            if (visited[vertex] == 0) {
                dfs(vertex);
            }
        }
    }
}

int main() {
    std::ifstream in("fastminimization.in");
    if (!in) {
        std::cerr << "File does not open!" << std::endl;
        return 0;
    }
    in >> n >> m >> k;
    graph = std::vector<Vertex>(n);
    graph_rev = std::vector<RevVertex>(n);
    visited = std::vector<int>(n);

    for (int i = 0; i < k; i++) {
        int v;
        in >> v;
        graph[v - 1].terminal = true;
    }
    for (int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        graph[a - 1].t[c] = b - 1;
        graph_rev[b - 1].t[c].insert(a - 1);
    }
    in.close();

    for (int i = 0; i < n; i++) {
        if (graph[i].terminal && visited[i] == 0) {
            dfs(i);
        }
    }

    std::vector<int> C(n);
    std::set<int> F;
    std::set<int> Q_F;

    for (int i = 0; i < n; i++) {
        if (visited[i]) {
            if (graph[i].terminal) {
                F.insert(i);
            } else {
                Q_F.insert(i);
                C[i] = 1;
            }
        }
    }
    //std::cout << 123 << std::endl;

    std::vector<std::set<int>> P = {F, Q_F};
    std::queue<std::pair<int ,char>> q;

    for (char c = 'a'; c <= 'z'; c++) {
        q.push({0, c});
        q.push({1, c});
    }
    while (!q.empty()) {
        int cls = q.front().first;
        char ch = q.front().second;
        q.pop();
        std::unordered_map<int, std::vector<int>> involved;
        for (int i : P[cls]) {
            for (int j : graph_rev[i].t[ch]) {
                involved[C[j]].push_back(j);
            }
        }
        //std::cout << 123 << std::endl;
        for (auto& item : involved) {
            if (item.second.empty()) continue;
            if (involved[item.first].size() < P[item.first].size()) {
                int j = P.size();
                P.emplace_back();
                for (int r : involved[item.first]) {
                    P[item.first].erase(r);
                    P[j].insert(r);
                }
                if (P[j].size() > P[item.first].size()) {
                    std::swap(P[j], P[item.first]);
                }
                for (int r : P[j]) {
                    C[r] = j;
                }
                for (char s = 'a'; s <= 'z'; s++) {
                    q.emplace(j, s);
                }
            }
        }
    }
   // std::cout << 123 << std::endl;

    std::vector<Vertex> dfa(P.size());
    for (int i = 0; i < n; i++) {
        if (visited[i] == 1) {
            for (const auto& item : graph[i].t) {
                if (visited[item.second]) {
                    dfa[C[i]].t[item.first] = C[item.second];
                }
            }
            dfa[C[i]].terminal = graph[i].terminal;
        }
    }

    std::vector<Vertex> ans(P.size());
    std::unordered_map<int, int> mp;


    make_ans(C[0], ans, dfa, mp);
    ans.resize(mp.size());

    int new_m = 0, new_k = 0;
    for (const auto& item : ans) {
        new_m += item.t.size();
        if (item.terminal) {
            new_k++;
        }
    }

    std::ofstream out("fastminimization.out");

    out << ans.size() << " " << new_m << " " << new_k << "\n";

    for (int i = 0; i < ans.size(); i++) {
        if (ans[i].terminal) {
            out << i + 1 << " ";
        }
    }
    out << "\n";

    for (int i = 0; i < ans.size(); i++) {
        for (const auto& item : ans[i].t) {
            out << i + 1 << " " << item.second + 1 << " " << item.first << "\n";
        }
    }
    out.close();
    return 0;
}

