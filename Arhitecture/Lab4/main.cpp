#include <iostream>
#include <fstream>
#include <vector>

int main() {
  std::ifstream file("png.pnm");
  std::string str;
  file >> str;
  int width, height;
  file >> width >> height;
  file >> str;
  std::cout << str << std::endl;
  std::vector<std::vector<int>> image(height, std::vector<int>(width));
  int i = 0, j = 0;
  file >> str;
  std::cout << str.size() / 640 << std::endl;
  for (char ch : str) {
    if (j == width) {
      j = 0;
      i++;
    }
    image[i][j++] = (int) ch;
  }
  for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {
      // std::cout << image[i][j] << " ";
    }
    //std::cout << std::endl;
  }
  
}
