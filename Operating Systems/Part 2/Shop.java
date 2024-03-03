import java.util.*;
import java.util.concurrent.Semaphore;

public class Shop
{
    // Initial objects, lists and variables.
    // IceCream is the shop access semaphore. 
    // waitList is the waiting semaphore.
    final int maxCap = 5;
    private int finished = 0;
    Semaphore iceCream = new Semaphore(5, true);
    Semaphore waitList = new Semaphore(1, true);
    ArrayList<Customer> customers = new ArrayList<Customer>();
    ArrayList<Customer> seated = new ArrayList<Customer>();
    int stopWatch = 0;
    Timer clock = new Timer();
    TimerTask task = new TimerTask()
    {
        public void run()
        {
            stopWatch++;
        }
    };
    // Timer used to keep track of global time

    public void startTimer()
    {
        // starts the timer
        clock.scheduleAtFixedRate(task, 1000, 1000);
    }
    
    public void open(Customer customer)
    {
        // method used to open the shop
        // if the shop is full, the customer waits, controlled by while loop dependent on the number of resources available through semaphore.
        // if the shop is not full, the customer enters the shop and is seated.
        // the customer is then served and leaves the shop - added to customer list and removed from seated list.
              
    try
        {
            while(customer.getArrivalTime() != stopWatch)
            {
                Thread.sleep(500);
            }
                if (iceCream.availablePermits() <= 0)
                {
                    waitList.acquire();
                    while (iceCream.availablePermits() != 5)
                    {
                        Thread.sleep(500);
                    }
                    waitList.release();
                }
                    waitList.acquire();
                    iceCream.acquire();
                    waitList.release();
                    seated.add(customer);
                    customer.setSeatingTime(stopWatch);
                    System.out.println("Customer " + customer.getID() + " is seated at " + customer.getSeatingTime());
                    while (customer.getEatingTime() > 0)
                    {
                        customer.setEatingTime(customer.getEatingTime() - 1);
                        Thread.sleep(1000);
                    }
                    System.out.println("Customer " + customer.getID() + " is leaving at " + stopWatch);
                    customer.setLeavingTime(stopWatch);
                    customers.add(customer);
                    iceCream.release();

        }
    catch (Exception e)
        {
                System.out.println("Error: " + e);
        }
    

    if (customers.size() == getFinished())
    {
        // if the number of customers in the customer list is equal to the number of customers that have finished eating, the shop closes.
        output();
        System.exit(0);
    }

    }

    public void setFinished(int newFinished)
    {
        // sets the number of customers that have finished eating.
        finished = newFinished;
    }

    public int getFinished()
    {
        // returns the number of customers that have finished eating.
        return finished;
    }

    public void output() 
    {
        // outputs customer statistics
        System.out.println("Customer\tArrives\t\tSeats\t\tLeaves");
        for (int i = 0; i < customers.size(); i++)
        {
            System.out.println(customers.get(i).getID() + "\t\t" + customers.get(i).getArrivalTime() + "\t\t" + customers.get(i).getSeatingTime() + "\t\t" + customers.get(i).getLeavingTime());
        }

    }
} 