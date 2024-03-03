// BTNode.h
// SENG1120 - Assignment3
// Lachlan McQualter
#ifndef LACHLAN_NODE
#define LACHLAN_NODE
#include <cstddef>
#include "Cities.h"


template<typename T>
class BTNode
{
public:

// constructor
BTNode(const T& initData);

// Destructor
~BTNode();

// Mutators
void setData(T initData);
void setParent(BTNode<T>* P);
void setLeft(BTNode<T>* L);
void setRight(BTNode<T>* R);

// getters
T& getData();
BTNode<T>* getParent();
BTNode<T>* getLeft();
BTNode<T>* getRight();

private:

T data;
BTNode<T>* left;
BTNode<T>* right;
BTNode<T>* parent;

};

#include "BTNode.hpp"
#endif

