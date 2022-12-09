//
// Created by pocht on 26.09.2022.
//

#ifndef HOMEWORK2_HEAP_H
#define HOMEWORK2_HEAP_H
#include <stdlib.h>

typedef struct heap {
    int* a;
    int n;
    int capacity;
} heap_t;

heap_t* new_heap() {
    heap_t* ptr = malloc(sizeof(heap_t));
    ptr->n = 0;
    ptr->capacity = 0;
    return ptr;
}

void free_heap(heap_t* this) {
    free(this->a);
    free(this);
}

void swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

void sift_down(heap_t* this, int i) {
    while (2 * i + 1 < this->n) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int j = left;
        if (right < this->n && this->a[right] < this->a[left]) {
            j = right;
        }
        if (this->a[i] <= this->a[j]) {
            break;
        }
        swap(&this->a[i], &this->a[j]);
        i = j;
    }
}

void sift_up(heap_t* this, int i) {
    while (this->a[i] < this->a[(i - 1) / 2]) {
        swap(&this->a[i], &this->a[(i - 1) / 2]);
        i = (i - 1) / 2;
    }
}

int extract_min(heap_t* this) {
    int min = this->a[0];
    this->a[0] = this->a[this->n - 1];
    --this->n;
    sift_down(this, 0);
    return min;
}

void insert(heap_t* this, int key) {
    if (this->capacity <= this->n) {
        if (this->capacity == 0) {
            this->capacity = 1;
            this->a = malloc(sizeof(int) * this->capacity);
        }
        else {
            this->capacity *= 2;
            this->a = realloc(this->a, sizeof(int) * this->capacity);
        }
    }
    this->a[this->n] = key;
    sift_up(this, this->n);
    ++this->n;
}

#endif //HOMEWORK2_HEAP_H
