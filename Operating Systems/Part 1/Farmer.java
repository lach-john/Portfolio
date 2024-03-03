public class Farmer extends Thread
// class used to create and initialise objects of farmers
{
    private String ID;
    private int steps;
    private String direction;
    private Bridge bridge;

    public Farmer()
    {
        // initialises variables
        ID = "";
        steps = 0;
        direction = "";
    
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

    public void setSteps(int newSteps)
    {
        steps = newSteps;
    }

    public int getSteps()
    {
        return steps;
    }

    public void setDirection(String newDirection)
    {
        direction = newDirection;
    }

    public String getDirection()
    {
        return direction;
    }

    public void setBridge(Bridge newBridge)
    {
        bridge = newBridge;
    }

    public Bridge getBridge()
    {
        return bridge;
    }

    public void swapDirections()
    {
        // swaps the direction of the farmer
        if (direction.equals("north"))
        {
            direction = "south";
        }
        else if (direction.equals("south"))
        {
            direction = "north";
        }
    }

    @Override
    public void run()
    {
        // farmer thread started using bridge object.
        bridge.cross(this);
        
        try
        {
            Thread.sleep(20);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
    }

}