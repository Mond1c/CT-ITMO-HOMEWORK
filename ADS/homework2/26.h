#include <stdio.h>
#include <stdlib.h>

typedef struct node {
  int value;
  struct node* left;
  struct node* right;
} node_t;


node_t* new_node(int value) {
    node_t* ptr = malloc(sizeof(node_t));
    ptr->value = value;
    return ptr;
}

void free_node(node_t* ptr) {
    free(ptr);
}

void free_tree(node_t* ptr) {
    if (ptr == NULL) return;
    node_t* left = ptr->left;
    node_t* right = ptr->right;
    free_node(ptr);
    free_tree(left);
    free_tree(right);
}

void add_tree(node_t* head, int value) {
    if (head == NULL) {
        head = new_node(value);
        return;
    }
    if (value < head->value) {
        if (head->left == NULL) {
            head->left = new_node(value);
            return;
        }
        add_tree(head->left, value);
    } else {
        if (head->right == NULL) {
            head->right = new_node(value);
            return;
        }
        add_tree(head->right, value);
    }
}

int extract_min_from_tree(node_t* head) {
    node_t* slow = head;
    node_t* fast = head;
    while (fast->left != NULL) {
        slow = head;
        fast = fast->left;
    }
}


