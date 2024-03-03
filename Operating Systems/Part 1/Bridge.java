import java.util.concurrent.Semaphore;

public class Bridge
// class containing bridge crossing method
{
    // initial variables
    private final Semaphore theOneRing = new Semaphore(1);
    int neonSign = 4;

    public void cross(Farmer samWise)
    {
        // method to cross the bridge
        // samWise: the brave farmer argument that is input in the farmer class and crossing the bridge in seperate threads.
        while (neonSign <= 100)
        {
            // looping while neon sign is less than 100
            samWise.setSteps(0);
            // sets steps back to 0 every time the bridge is crossed
            try
            {
                // try catch to catch any errors
                // semaphore to allow only one farmer to cross at a time
                // if the semaphore is not available, the farmer waits
                // if the semaphore is available, the farmer crosses the bridge
                // prior to crossing, neon sign is checked again.
                System.out.println(samWise.getID() + " Waiting for bridge. Going towards " + samWise.getDirection());
                theOneRing.acquire();
                if (neonSign >= 100)
                {
                    System.exit(0);
                }
                samWise.setSteps(samWise.getSteps() + 5);
                System.out.println(samWise.getID() + " Crossing bridge step " + samWise.getSteps());
                Thread.sleep(20);
                samWise.setSteps(samWise.getSteps() + 5);
                System.out.println(samWise.getID() + " Crossing bridge step " + samWise.getSteps());
                Thread.sleep(20);
                samWise.setSteps(samWise.getSteps() + 5);
                System.out.println(samWise.getID() + " Crossing bridge step " + samWise.getSteps());
                Thread.sleep(20);
                samWise.setSteps(samWise.getSteps() + 5);
                System.out.println(samWise.getID() + " Crossing bridge step " + samWise.getSteps());
                Thread.sleep(20);
                System.out.println(samWise.getID() + " Across the bridge.");
                theOneRing.release();
                samWise.swapDirections();
                neonSign++;
                System.out.println("NEON = " + neonSign);
                // direction swapped every time the bridge is crossed.
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e);
            }
        }
       

    }

}