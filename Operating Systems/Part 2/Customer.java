public class Customer extends Thread
// class used to create and initialise objects of farmers
{
    private String ID;
    private int arrivalTime;
    private int eatingTime;
    private int seatingTime;
    private int leavingTime;
    private Shop shop;

    public Customer()
    {
        // initialises variables
        ID = "";
        arrivalTime = 0;
        eatingTime = 0;
        seatingTime = 0;
        leavingTime = 0;
    
    }

    // getters and setters

    public void setID(String newID)
    {
        ID = newID;
    }

    public String getID()
    {
        return ID;
    }

    public void setArrivalTime(int newArrivalTime)
    {
        arrivalTime = newArrivalTime;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public void setEatingTime(int newEatingTime)
    {
        eatingTime = newEatingTime;
    }

    public int getEatingTime()
    {
        return eatingTime;
    }

    public void setSeatingTime(int newSeatingTime)
    {
        seatingTime = newSeatingTime;
    }

    public int getSeatingTime()
    {
        return seatingTime;
    }

    public void setLeavingTime(int newLeavingTime)
    {
        leavingTime = newLeavingTime;
    }   

    public int getLeavingTime()
    {
        return leavingTime;
    }

    public void setShop(Shop newShop)
    {
        shop = newShop;
    }

    public Shop getShop()
    {
        return shop;
    }

    // public void run begins the thread using the shop object and using the customer object

    @Override
    public void run()
    {
        shop.open(this);
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
    }

}