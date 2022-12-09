//
// Created by pocht on 26.09.2022.
//

#ifndef HOMEWORK2_35_H
#define HOMEWORK2_35_H

#include <stdio.h>
#include "heap.h"

void solve() {
    heap_t* h = new_heap();
    insert(h, 5);
    insert(h, 4);
    insert(h, 3);
    insert(h, 2);
    insert(h, 1);
    for (int i = 0; i < h->n; ++i) printf("%d ", h->a[i]);
    free_heap(h);
}


#endif //HOMEWORK2_35_H
