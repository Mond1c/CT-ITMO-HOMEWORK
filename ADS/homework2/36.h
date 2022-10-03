//
// Created by pocht on 27.09.2022.
//

#ifndef HOMEWORK2_36_H
#define HOMEWORK2_36_H

#include <stdio.h>
#include "heap.h"

void solve36() {
    int* arr = malloc(sizeof(int) * 5);
    for (int i = 0; i < 5; ++i) arr[i] = i + 1;
    heap_t* h = new_heap();
    h->n = 5; h->capacity = 5; h->a = arr;
    for (int i = 0; i < 5; ++i) sift_down(h, i);
    for (int i = 0; i < 5; ++i) printf("%d ", h->a[i]);
}

#endif //HOMEWORK2_36_H
