#include <iostream>
#include <vector>

vector<int> sort(std::vector<int> a, int& counter) {
  if (a.size() == 1) return a;
  int mid = a.size() / 2;
  merge(sort(std::vector<int>(a.begin(), a.begin() + mid), counter), sort(std::vector<int>(a.begin() + mid, a.end()), counter), a, counter);
}

void merge(std::vector<int> a, std::vector<int> b, std::vector<int>& c, int& counter) {
  int l = 0, r = 0;
  c = std::vector<int>(a.size() + b.size());
  for (int i = 0; i < a.size() + b.size(); i++) {
    if (r < 

int main() {
  return 0;
}
