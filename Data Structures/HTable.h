// HTable.h
// SENG1120 - Assignment3
// Lachlan McQualter
#ifndef LACHLAN_TABLE
#define LACHLAN_TABLE
#include <cstddef>
#include <iostream>
#include "Cities.h"

template<typename T>
class HTable
{

 public:
    
    // default constructor and destructor 
    HTable();
    ~HTable();

    // remove function
    void remove(const T remData);
    
    // add function
    void add(const T &addData);

    // table print function
    void print(std::ostream &output) const;

    // population calculators
    int calculateTotalPop() const; 
    int calculatePopGreaterThan(int num); 

    //returns table size, mostly used during construction to track size of table
    int size();

    private:

    const static int hashSize = 10000;   
    T hashData[hashSize];     
    int hashfun(const T& value); 
    int populationStd;
    int populationGreater;
    int tableCounter;
};

    // overloads << operator to print table
    template <typename T>
    std::ostream& operator <<(std::ostream& output, HTable<T>& table);


#include "HTable.hpp"
#endif