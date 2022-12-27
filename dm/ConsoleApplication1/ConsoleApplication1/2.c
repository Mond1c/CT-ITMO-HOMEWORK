#include <stdio.h>
#include <conio.h>
#include <stdbool.h>
#define trans(i,j)\
        {\
            int t = perm[j];\
            perm[j] = perm[i];\
            perm[i] = t;\
        }   

bool next(int perm[], int n)
{
    int i = n - 1;
    while (--i >= 0 && perm[i] > perm[i + 1])
        ;
    if (i == -1)
        return false;
    for (int j = i + 1, k = n - 1; j < k; j++, k--)
        trans(j, k);
    int j = i + 1;
    while (perm[j] < perm[i])
        j++;
    trans(i, j);
    return true;
}

void generate(int n)
{
    if (!n)
    {
        printf("[]\n");
        return;
    }
    int perm[n], i;
    for (i = 0; i < n; i++)
        perm[i] = i + 1;
    do
    {
        printf("[");
        for (i = 0; i < n - 1; i++)
            printf("%d,", perm[i]);
        printf("%d]\n", perm[i]);
    } while (next(perm, n));
}

int main()
{
    while (1)
    {
        puts("¬ведите число от 0 до 9 или символ 'q' дл€ выхода");
        char c = getche();
        printf("\n");
        if (c == 'q')
            return 0;
        if (c >= '0' && c <= '9')
            generate(c - '0');
        else
            puts("ќшибка!");
        printf("\n");
    }
}
