// BTNode.hpp
// SENG1120 - Assignment3
// Lachlan McQualter
// This node.cpp file shows the functionality of the Node class and displays all
// setters and getters which initialise the class 

#include <cstdlib>
#include <iostream>
#include "BTNode.h"

// constructors
template<typename T>
BTNode<T>::BTNode(const T& initData)
{
    data = initData;
    left = NULL;
    right = NULL;
    parent = NULL;
}

template<typename T>
BTNode<T>::~BTNode()
{
    // empty destructor
}

// setting methods
template<typename T>
void BTNode<T>::setData(T initData)
{

   data = initData;

}

template<typename T>
void BTNode<T>::setParent(BTNode<T>* P)
{

    parent = P;


}

template<typename T>
void BTNode<T>::setLeft(BTNode<T>* L)
{

    left = L;

}

template<typename T>
void BTNode<T>::setRight(BTNode<T>* R)
{

    right = R;

}

// getting methods
template<typename T>
T& BTNode<T>::getData()
{
    return data;
}

template<typename T>
BTNode<T>* BTNode<T>::getParent()
{
    return parent;
}

template<typename T>
BTNode<T>* BTNode<T>::getLeft()
{
    return left;
}

template<typename T>
BTNode<T>* BTNode<T>::getRight()
{
    return right;
}