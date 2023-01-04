#include <bits/stdc++.h>


struct player {
    int exp;
};

int main() {
    int n, m;
    std::cin >> n >> m;
    std::vector<std::pair<int, std::set<int>>> players(n);
    while (m--) {
        std::string request;
        std::cin >> request;
        if (request == "join") {
            int x, y;
            std::cin >> x >> y;
            x--; y--;
            players[x].second.insert(y);
            for (int v : players[y].second) {
                players[x].second.insert(v);
            }
            players[y].second.insert(x);
            for (int v : players[x].second) {
                players[y].second.insert(v);
            }
        } else if (request == "add") {
            int x, v;
            std::cin >> x >> v;
            x--;
            players[x].first += v;
            for (int y : players[x].second) {
                if (y != x) {
                    players[y].first += v;
                }
            }
        } else {
            int x;
            std::cin >> x;
            x--;
            std::cout << players[x].first << std::endl;
        }
    }
    return 0;
}
