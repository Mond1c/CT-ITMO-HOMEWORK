#include <iostream>
using namespace std;
bool NextSet(int* a, int n, int m)
{
    int k = m;
    for (int i = k - 1; i >= 0; --i)
        if (a[i] < n - k + i + 1)
        {
            ++a[i];
            for (int j = i + 1; j < k; ++j)
                a[j] = a[j - 1] + 1;
            return true;
        }
    return false;
}
void Print(int* a, int n)
{
    static int num = 1;
    cout.width(3);
    cout << num++ << ": ";
    for (int i = 0; i < n; i++)
        cout << a[i] << " ";
    cout << endl;
}
int mai1n()
{
    int n, m, * a;
    cout << "N = ";
    cin >> n;
    cout << "M = ";
    cin >> m;
    a = new int[n];
    for (int i = 0; i < n; i++)
        a[i] = i + 1;
    Print(a, m);
    if (n >= m)
    {
        while (NextSet(a, n, m))
            Print(a, m);
    }
    cin.get(); cin.get();
    return 0;
}