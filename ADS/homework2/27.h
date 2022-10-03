#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int value;
    struct node* next;
    struct node* prev;
} node_t;

node_t* new_node(int value) {
    node_t* ptr = malloc(sizeof(node_t));
    ptr->value = value;
    return ptr;
}

void free_node(node_t* ptr) {
    free(ptr);
}

typedef struct list {
    node_t* head;
    node_t* tail;
    node_t* median;
    int size;
} list_t;

list_t* new_list() {
    list_t* ptr = malloc(sizeof(list_t));
    ptr->size = 0;
    return ptr;
}

void free_list(list_t* this) {
    node_t* ptr = this->head;
    while (ptr->next != NULL) {
        ptr = ptr->next;
        free_node(ptr->prev);
    }
    free_node(ptr->prev);
    free_node(ptr);
}

void push_back_list(list_t* this, int value) {
    node_t* ptr = new_node(value);
    if (this->size == 0) {
        this->head = this->tail = ptr;
        this->median = ptr;
        ++this->size;
        return;
    }
    ++this->size;
    this->tail->next = ptr;
    ptr->prev = this->tail;
    this->tail = ptr;
    if (this->size % 2 == 1) this->median = this->median->next;
}

void push_front_list()

void delete_median(list_t* this) {
    if (this == NULL || this->head == NULL) return;
    --this->size;
    if (this->median->prev != NULL) {
        if (this->median == this->tail) this->tail = this->tail->prev;
        this->median = this->median->prev;
        this->median->next = this->median->next->next;
        free_node(this->median->next->prev);
        this->median->next->prev = this->median;
        return;
    }
    if (this->median->next != NULL) {
        this->head = this->head->next;
        this->median = this->head;
        free_node(this->median->prev);
        this->median->prev = NULL;
        return;
    }
    free_node(this->median);
    this->head = this->tail = NULL;
}

void run() {
    list_t* b
}
