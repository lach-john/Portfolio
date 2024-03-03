// HTable.hpp
// SENG1120 - Assignment3
// Lachlan McQualter
template<typename T>
HTable<T>::HTable() // default constructor creates instance of HTable and assigns it the value type and number of variables contained within hashSize
{
    populationStd = 0;
    int counter = 0;

    while(counter < hashSize)
    {
        hashData[counter] = T(); 
        counter ++;
    }
}

template <typename T>
HTable<T>::~HTable() // default destructor iterates through table and sets all values to empty values
{
    int counter = 0;

    while(counter < hashSize)
    {
        hashData[counter].set_name(" "); 
        hashData[counter].set_population(NULL);
        counter ++;
    }
}

template <typename T>
void HTable<T>::remove(const T remData) // finds remData input within table and sets values to empty values
{
    int num = hashfun(remData); 

    if (hashData[num] == T())
    {
        return;
    } 
    else 
    {
        populationStd -= remData.get_population();
        hashData[num].set_population(NULL);
        hashData[num].set_name("");

        tableCounter --;
    }
}

template <typename T>
void HTable<T>::add(const T& addData) // adds input data into table based on parameters in hashfun
{
    int num = hashfun(addData); 
    populationStd += addData.get_population();
    hashData[num] = addData; 

    tableCounter ++;
}

template <typename T>
void HTable<T>::print(std::ostream &output) const //public print interface iterates through table, ensuring data exists, then printing current data
{
    int counter = 0;

    while (counter < hashSize)
    {
        if (!(hashData[counter] == T())) 
        {
           output << hashData[counter];
        }

        counter ++;
    }
}

template <typename T>
int HTable<T>::calculateTotalPop() const // returns total pop of all data in table
{   
    return populationStd;
}


template <typename T>
int HTable<T>::calculatePopGreaterThan(int num) // returns total pop of all data within certain parameters by iterating through table and checking each data set against parameters
{
    int counter = 0;
    populationGreater = 0; 
    
    while (counter < hashSize) 
    {
        if (!(hashData[counter]==T())) 
        {
            if (hashData[counter].get_population() > num)
            {
                populationGreater += hashData[counter].get_population();
            }
        }

        counter ++;
    }
    return populationGreater;
}

template <typename T> 
int HTable<T>::hashfun(const T& value) // creates key based on length of string (name) which hash table uses to organise and order itself 
{ 
 int position = 0; 
 string temp = value.get_name(); 
 
 for (int i=0; i<(int)temp.length(); i++) 
 { 
  position += (i+1) * (i+1) * temp.at(i); 
 } 
 return position % hashSize; 
}

template <typename T>
int HTable<T>::size() // returns table size 
{   
    return tableCounter;
}

//Overloaded operator
template <typename T>
ostream& operator <<(std::ostream& output, HTable<T>& hashTable) //overload operator for printing table
{
    hashTable.print(output);
    return output;
}