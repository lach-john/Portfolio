import java.util.*;

public class Shop
{
    // Initial objects, lists and variables.
    // controlWait is used to control the synchronization blocks.
    private final Object controlWait;
    final int maxCap = 5;
    private int finished = 0;
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

    public Shop()
    {
        // Initialises the controlWait object.
        controlWait = new Object();
    }

    public void startTimer()
    {
        // Starts the timer.
        clock.scheduleAtFixedRate(task, 1000, 1000);
    }
    
    public void open(Customer customer)
    {
        // open method uses synch blocks to control access to the shop.
        // customer list used to keep track of customers.
        // seated list used to keep track of customers that are seated.
        // seated.add() within control block to make sure that the customer flow is 1 by 1 into shop and no customers
        // are left suspended in wait(while) loop.
              
    try
        {
            while(customer.getArrivalTime() != stopWatch)
            {
                Thread.sleep(500);
            }
                if (seated.size() >= 5)
                {
                    synchronized(controlWait){
                        while (seated.size() != 0)
                        {
                            Thread.sleep(500);
                        }
                    }
                }
                    synchronized(controlWait)
                    {
                        seated.add(customer);
                    }
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
                    seated.remove(customer);

        }
    catch (Exception e)
        {
                System.out.println("Error: " + e);
        }
    

    if (customers.size() == getFinished())
    {
        // When all customers have left the shop, the timer is cancelled & results displayed
        output();
        System.exit(0);
    }

    }

    public void setFinished(int newFinished)
    {
        // Sets the number of customers that will have left the shop when program stops.
        finished = newFinished;
    }

    public int getFinished()
    {
        // Returns the number of customers that will have left the shop when program stops.
        return finished;
    }

    public void output() 
    {
        // outputs customer statistics
        //sort customers by arrival time
        Collections.sort(customers, new Comparator<Customer>()
        {
            public int compare(Customer c1, Customer c2)
            {
                return c1.getArrivalTime() - c2.getArrivalTime();
            }
        });

        System.out.println("Customer\tArrives\t\tSeats\t\tLeaves");
        for (int i = 0; i < customers.size(); i++)
        {
            System.out.println(customers.get(i).getID() + "\t\t" + customers.get(i).getArrivalTime() + "\t\t" + customers.get(i).getSeatingTime() + "\t\t" + customers.get(i).getLeavingTime());
        }

    }
} 