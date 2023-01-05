#include "Otsu.h"
#include "Utility.h"
#include <omp.h>

int main(int argc, char **argv) {
    try {
        int threadCount = std::stoi(argv[1]);
        bool isOpenMPEnabled = true;
        if (threadCount > 0) {
            omp_set_num_threads(threadCount);
        } else if (threadCount == -1) {
            isOpenMPEnabled = false;
        }
        if (argc == 5) {
            chunk_size = std::stoi(argv[4]);
        }
        double startTime = omp_get_wtime();
        Otsu otsu(argv[2], argv[3], isOpenMPEnabled);
        otsu.Generate();
        double endTime = omp_get_wtime();
        std::cout
            << utility::GetFormatString("Time (%i thread(s)): %g ms\n", threadCount, (endTime - startTime) * 1000);
    } catch (std::exception& e) {
        std::cerr << e.what() << std::endl;
        return -1;
    }
    return 0;
}
