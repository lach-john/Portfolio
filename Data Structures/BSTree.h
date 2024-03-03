// BSTree.h
// SENG1120 - Assignment3
// Lachlan McQualter
#ifndef LACHLAN_TREE
#define LACHLAN_TREE
#include <cstddef>
#include"BTNode.h"
#include"Cities.h"

template<typename T>
class BSTree
{
public:
    //constructors
    BSTree();
    ~BSTree();
    

    //mutators/setters
    void setRoot(BTNode<T>* rootNode);

    // getters
    BTNode<T>* getRoot();

    // add - public interface
    // insert - recursive add function
    void add(const Cities& addData);
    void insert(BTNode<T>* temp, const Cities& insertData);
    
    // size counter for tree
    int size();

    // remove - public interface
    // privRemove - recursive remove function
    void remove(const T& remData);
    void privRemove(BTNode<T>* removalNode, const T& remData);

    // finds bottom most node in tree on left or right side
    BTNode<T>* bottomNode(BTNode<T>* search);

    // print - public interface
    // printPrivate - recursive print function
    ostream& print(ostream& output) const;
    ostream& printPrivate(BTNode<T>* printNode, ostream& output) const;

    // return population calculations
    int calculateTotalPop();
    int calculatePopGreaterThan(const int input);


private:

    BTNode<T>* root;
    BTNode<T>* temp;
    int treeSize;
    int population;
    int populationGreater;
};

// overloads << operator to print tree
template <typename T>
ostream& operator<<(ostream& output, const BSTree<T>& tree);

#include "BSTree.hpp"
#endif