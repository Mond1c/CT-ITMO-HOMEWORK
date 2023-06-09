#include <bits/stdc++.h>
using namespace std;

struct Node {
    string key;
    string value;
    std::shared_ptr<Node> prev;
    std::shared_ptr<Node> next;
    shared_ptr<Node> prevAdded;
    shared_ptr<Node> nextAdded;
};

class HashMap {
private:
    const int SIZE = 1000003;
    vector<shared_ptr<Node>> data;
    shared_ptr<Node> prevN;
    shared_ptr<Node> curN;

    size_t hash(const std::string& key) {
        size_t value = 0;
        for (char ch : key) {
            value = (value * 31 + ch) % SIZE;
        }
        return value;
    }

    shared_ptr<Node> findNode(const std::string& key) {
        size_t i = hash(key);
        shared_ptr<Node> ptr = data[i];
        while (ptr != nullptr) {
            if (ptr->key == key) {
                return ptr;
            }
            ptr = ptr->next;
        }
        return nullptr;
    }

    void insertNode(const string& key, const std::string& value) {
        shared_ptr<Node> ptr = make_shared<Node>();
        ptr->key = key;
        ptr->value = value;
        size_t i = hash(key);
        if (data[i] == nullptr) {
            data[i] = ptr;
        } else {
            auto cur = data[i];
            while (cur->next != nullptr) {
                cur = cur->next;
            }
            cur->next = ptr;
            ptr->prev = cur;
        }
        if (curN != nullptr) {
            curN->nextAdded = ptr;
            ptr->prevAdded = curN;
        }
        prevN = curN;
        curN = ptr;
    }

    void removeNode(shared_ptr<Node> ptr) {
        size_t i = hash(ptr->key);
        if (ptr->prev != nullptr) {
            ptr->prev->next = ptr->next;
        } else {
            data[i] = ptr->next;
        }
        if (ptr->next != nullptr) {
            ptr->next->prev = ptr->prev;
        }
        if (ptr->nextAdded != nullptr) {
            ptr->nextAdded->prevAdded = ptr->prevAdded;
        }
        if (ptr->prevAdded != nullptr) {
            ptr->prevAdded->nextAdded = ptr->nextAdded;
        }
    }

public:
    HashMap() {
        data.resize(SIZE);
    }

    void put(const std::string& key, const std::string& value) {
        auto ptr = findNode(key);
        if (ptr != nullptr) {
            if (ptr->nextAdded != nullptr) {
                ptr->nextAdded->prevAdded = ptr->prevAdded;
            }
            if (ptr->prevAdded != nullptr) {
                ptr->prevAdded->nextAdded = ptr->nextAdded;
            }
            if (curN != nullptr) {
                curN->nextAdded = ptr;
                curN->prevAdded = prevN;
            }
            ptr->value = value;
        } else {
            insertNode(key, value);
        }
    }

    void remove(const std::string& key) {
        auto ptr = findNode(key);
        if (ptr != nullptr) {
            removeNode(ptr);
        }
    }

    std::string get(const string& key) {
        auto ptr = findNode(key);
        if (ptr != nullptr) {
            return ptr->value;
        }
        return "none";
    }

    std::string prev(const std::string& key) {
        auto ptr = findNode(key);
        if (ptr != nullptr && ptr->prevAdded != nullptr) {
            return ptr->prevAdded->value;
        }
        return "none";
    }

    std::string next(const std::string& key) {
        auto ptr = findNode(key);
        if (ptr != nullptr && ptr->nextAdded != nullptr) {
            return ptr->nextAdded->value;
        }
        return "none";
    }


};

int main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    std::string query, x;
    HashMap mp;
    while (cin >> query >> x) {
        if (query == "put") {
            string y;
            cin >> y;
            mp.put(x, y);
        } else if (query == "delete") {
            mp.remove(x);
        } else if (query == "get") {
            cout << mp.get(x) << "\n";
        } else if (query == "prev") {
            cout << mp.prev(x) << "\n";
        } else {
            cout << mp.next(x) << "\n";
        }
    }
}
