// BSTree.hpp
// SENG1120 - Assignment3
// Lachlan McQualter

// tree parameters
// each item in left sub tree is the smallest 
// each item in the parent sub tree is at max, as big as the right sub tree
// items in the right sub tree are at smallest, as small as the parent sub tree, or bigger

#include <cstdlib>
#include <iostream>
#include "BTNode.h"

template<typename T>
BSTree<T>::BSTree() // default constructor
{
        root = NULL;
        treeSize = 0;
        population = 0;
        populationGreater = 0;
}

template<typename T>
BSTree<T>::~BSTree()
{
        // empty destructor
}

template<typename T>
void BSTree<T>::setRoot(BTNode<T>* rootNode) // set root function, takes in node* and sets it as root node
{
        root = rootNode;
        rootNode->getLeft()->setParent(rootNode);
        rootNode->getRight()->setParent(rootNode);
}

template<typename T> // returns root node*
BTNode<T>* BSTree<T>::getRoot()
{
        return root;
}

template<typename T> //public interface add function
void BSTree<T>::add( const Cities& addData)
{       
        if (root == NULL) // creates node, otherwise calls recursive function to insert rest of tree
	{
                
        root = new BTNode<T>(addData);
        temp = root;
		treeSize ++;
        population += addData.get_population();
        if (addData.get_population() > 10000000) // population calculators
        {
            populationGreater += addData.get_population();
        }                         
	}
        
        else 
        {
                insert(temp, addData); 
        }
                      
}

template<typename T> 
void BSTree<T>::insert(BTNode<T>* temp, const Cities& insertData)
{	
                
    if (insertData < temp->getData()) // if data is smaller, becomes left child or function climbs down and runs comparison again 
    {                               //until empty node is found 
                        
        if (temp->getLeft() == NULL)
        {
            BTNode<T>* newBTNode = new BTNode<T>(insertData);
            temp->setLeft(newBTNode);
            newBTNode->setParent(temp);
            treeSize ++;
            population += newBTNode->getData().get_population(); // population calculators
            if (newBTNode->getData().get_population() > 10000000)
            {
                populationGreater += newBTNode->getData().get_population();
            }	
        }
        else
        {
            temp = temp->getLeft(); 
            insert(temp, insertData); // recursive function
        }

                        
        }
        else if (insertData > temp->getData()) // if data is smaller, becomes left child or function climbs down and runs comparison again 
        {                                   //until empty node is found 

            if (temp->getRight() == NULL) 
        {
            BTNode<T>* newBTNode = new BTNode<T>(insertData);
            temp->setRight(newBTNode);
            newBTNode->setParent(temp);
            treeSize ++;
            population += newBTNode->getData().get_population(); // population calculators
            if (newBTNode->getData().get_population() > 10000000)
            {
                populationGreater += newBTNode->getData().get_population();
            }	
        }
        else
        {
            temp = temp->getRight();
            insert(temp, insertData); // recursive function
        }
                       
    }
               


}

template<typename T>
int BSTree<T>::size() //returns tree size
{
        return treeSize;
}

template<typename T>
void BSTree<T>::remove(const T& remData) // public remove interface 
{

    if(treeSize == 0)
    {
        return;
    }
    else
    {
        privRemove(root, remData);
    }

}

template<typename T>
void BSTree<T>::privRemove(BTNode<T>* removalNode, const T& remData)
{
    
    if (removalNode == NULL)														
    {
        return;													
    }
    if (remData > removalNode->getData()) // if the data is bigger than the current node data, move right 														
    {
        privRemove(removalNode->getRight(), remData);	// recursive func called with new ptr position								
    }
    else if (remData < removalNode->getData()) // if the data is smaller than the current node data, move left 												
    {
        privRemove(removalNode->getLeft(), remData); // recursive func called with new ptr position								
    }
    else 														
    {
        if(removalNode->getLeft() == NULL && removalNode->getRight() == NULL)	// checks if removal node is a child							
        {
            if(root->getData() == remData)      // checks if removal node is the root
            {
                root = NULL;										
            }
            else 													
            {
                if(removalNode->getParent()->getData() < removalNode->getData()) // if right child					
                {
                    removalNode->getParent()->setRight(NULL); 	//set right child NULL
                }
                else 										
                {
                    removalNode->getParent()->setLeft(NULL); 	// set left child NULL
                }
            }

            population -= removalNode->getData().get_population(); // population calculators
            if (removalNode->getData().get_population() > 10000000)
            {
                populationGreater -= removalNode->getData().get_population();
            }
            delete removalNode;		// removal node is deleted, size --						
            removalNode = NULL;								
            treeSize --;
	
        }
        else if ((removalNode->getLeft() == NULL && removalNode->getRight() != NULL) || (removalNode->getLeft() != NULL && removalNode->getRight() == NULL)) // if removal node only has 1 child						
        {
            if (root->getData() == remData)		
            {
                if(removalNode->getRight() != NULL) 	// if the right child is the one the exists, set to null				
                {
                    removalNode->getRight()->setParent(NULL);				
                    root = removalNode->getRight();			
                }
                else 							
                {
                    removalNode->getLeft()->setParent(NULL); 	// else set the left child to null
                    root = removalNode->getLeft();  				
                }
            }
            else if(removalNode->getRight() != NULL)
            {
                        removalNode->getRight()->setParent(removalNode->getParent()); //makes parent of removal node right child the parent of removal node
                	                                                        
                if (removalNode->getParent()->getData() < removalNode->getData()) 								
                {
                    removalNode->getParent()->setRight(removalNode->getRight()); // removes node between removal node parent and removal node right child
                }
                else
                {
                    removalNode->getParent()->setLeft(removalNode->getRight()); // removes node between removal node parent and removal node left child
                }
            }
            else 															
            {
                        removalNode->getLeft()->setParent(removalNode->getParent());  //makes parent of removal node left child the parent of removal node

                if (removalNode->getParent()->getData() < removalNode->getData()) 									
                {
                    removalNode->getParent()->setRight(removalNode->getLeft()); // sets right node of removal node parent to the left node	
                }
                else 												
                {
                    removalNode->getParent()->setLeft(removalNode->getLeft()); // sets left node of removal node parent to the left node	
                }
            }
                
                population -= removalNode->getData().get_population(); // population calculators
                if (removalNode->getData().get_population() > 10000000)
                {
                    populationGreater -= removalNode->getData().get_population();
                }
                delete removalNode; 										
                treeSize--;	// removal node is deleted, size --

        		
        }												
        else 										
        {
            BTNode<T>* temporary = bottomNode(removalNode->getRight());		//creates a temp node of bottom most node in tree
            T* tempObj = new T(temporary->getData());	                        //creates temp obj pointer - data of temp node 
            privRemove(temporary, temporary->getData());		        // calls private remove function with new parameters of bottom most node
            removalNode->setData(*tempObj);								
        }
    }														
}

template<typename T>
BTNode<T>* BSTree<T>::bottomNode(BTNode<T>* search) // combs through tree to find the bottom most node on either left or right side depending on input
{
    if (search->getLeft() != NULL) 										
    {
        bottomNode(search->getLeft());										
    }
    else 																	
    {
        return search; 								
    }
} 

template <typename T>
ostream& BSTree<T>::print(ostream& output) const // public print interface
{
    if(root != NULL) {
        return printPrivate(root, output);
    }

    else {
        cout << "Tree is empty" << endl;
        return output;
    }
}


template <typename T>
ostream& BSTree<T>::printPrivate(BTNode<T>* printNode, ostream& output) const //outputs tree using inorder traversal.
{
    if(printNode != NULL) {
        printPrivate(printNode->getLeft(), output);
        output << printNode->getData() << " ";
        printPrivate(printNode->getRight(), output);
    }

    return output;
}

template<typename T>
int BSTree<T>::calculateTotalPop() // returns standard population total
{
    return population;
}

template<typename T>
int BSTree<T>::calculatePopGreaterThan(const int input) // returns greater than 10000000 population total
{
    if (input == 10000000)
    {
        return populationGreater;
    }
}

template <typename T>
ostream& operator <<(ostream& output, const BSTree<T>& tree) // overloads << operator to print tree
{
    return tree.print(output);
}
