#include <bits/stdc++.h>

struct Player {
    int clan = -1;
    int exp=0;
};

std::unordered_map<int, std::vector<std::shared_ptr<Player>>> clans;
std::unordered_map<int, std::shared_ptr<Player>> players;
int clans_count = 0;

void add(int x, int v) {
    if (!players.count(x)) {
        players[x] = std::make_shared<Player>();
    } 
    if (players[x]->clan != -1) {
        for (const auto& player : clans[players[x]->clan]) {
            player->exp += v;
        }
    } else {
        players[x]->exp += v;
    }
}

void join(int x, int y) {
    std::vector<std::shared_ptr<Player>> new_clan;
    if (!players.count(x)) {
        players[x] = std::make_shared<Player>();
    }
    if (!players.count(y)) {
        players[y] = std::make_shared<Player>();
    }
    if (players[x]->clan != -1) {
        for (const auto& player : clans[players[x]->clan]) {
            new_clan.push_back(player);
        }
        clans.erase(players[x]->clan);
    } else {
        new_clan.push_back(players[x]);
    }
    if (players[y]->clan != -1) {
        for (const auto& player : clans[players[y]->clan]) {
            new_clan.push_back(player);
        }
        clans.erase(players[y]->clan);
    } else {
        new_clan.push_back(players[y]);
    }
    clans[clans_count] = new_clan;
    players[y]->clan = players[x]->clan = clans_count++;
}

int get(int x) {
    if (!players.count(x)) {
        return 0;
    }
    return players[x]->exp;
}

int main() {
    int n, m;
    std::cin >> m >> n;
    for (int i = 0; i < n; ++i) {
        std::string request;
        std::cin >> request;
        if (request == "add") {
            int x, v;
            std::cin >> x >> v;
            add(x, v);
        } else if (request == "join") {
            int x, y;
            std::cin >> x >> y;
            join(x, y);
        } else {
            int x;
            std::cin >> x;
            std::cout << get(x) << std::endl;
        }
    }
}