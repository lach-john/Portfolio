import java.util.*;
import java.io.*;

public class P2
{
    public static void main(String[] args)
    {
        // initial variables and objects
        String argument = args[0];
        String[] output = new String[100];
        int counter = 0;
        ArrayList<Customer> customers = new ArrayList<Customer>();
        Shop shop = new Shop();   
        
        // file reader
    try
        {
            BufferedReader reader = new BufferedReader(new FileReader(argument));
            String line = reader.readLine();
            String s2 = "";
            while (line != null)
            {
                s2 = s2.concat(line + " "); 
                line = reader.readLine();
            }  
            output = s2.split("\\s+");
            reader.close();
            // output gets split by line into an array
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

        while (counter < output.length && !output[counter].equals("END"))
        {
            Customer worker = new Customer();
            worker.setArrivalTime(Integer.parseInt(output[counter]));
            worker.setID(output[counter + 1]);
            worker.setEatingTime(Integer.parseInt(output[counter + 2]));
            worker.setShop(shop);
            customers.add(worker);
            counter = counter + 3;
            // while loop iterates through array and creates customer objects
        }

        shop.setFinished(customers.size());
        // sets the number of customers that will be served
        shop.startTimer();
        // starts the timer

        for (int i = 0; i < customers.size(); i++)
        {
            // for loop iterates through customer objects and starts their threads
            Customer temp = new Customer();
            temp = customers.get(i);
            temp.start();
        }

    }
}