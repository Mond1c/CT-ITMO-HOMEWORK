#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>



int main() {
  int n;
  std::cin >> n;
  std::vector<int> h(n);
  for (int i = 0; i < n; ++i)
    std::cin >> h[i];
  std::vector<std::pair<int, int>> dp(n);
  dp[0] = {h[0], 1};
  for (int i = 1; i < n; i++) {
    int height = std::min(dp[i - 1].first, h[i]);
    int width = dp[i - 1].second + 1;
    if (h[i] < height * width) {
      dp[i] = {height, width};
    } else {
      dp[i] = {h[i], 1};
    }
  }
  int ans = 0;
  for (int i = 0; i < n; ++i) {
    ans = std::max(ans, dp[i].first * dp[i].second);
  }
  std::cout << ans << std::endl;
  return 0;
}
