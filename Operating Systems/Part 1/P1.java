import java.io.*;
import java.util.*;

public class P1
// main file where seperate methods are called into void main to action the program.
{
  

    
    public static void main(String[] args)
    {
        // Initialises the shop and customer objects.
        
        String[] output = new String[100];    
        Queue<Farmer> North = new LinkedList<Farmer>();
        Queue<Farmer> South = new LinkedList<Farmer>();
        ArrayList<Farmer> farmers = new ArrayList<Farmer>();
        Bridge bridge = new Bridge();
        int northCounter = 0;
        int southCounter = 0;
        String argument = args[0];
        

        try
        {
            // Reads the input file and creates the farmers.
            BufferedReader reader = new BufferedReader(new FileReader(argument));
            String line = reader.readLine();
            String s2 = "";
            while (line != null)
            {
                s2 = s2.concat(line + " "); 
                line = reader.readLine();
            }  
            output = s2.split("(?!^)");
            reader.close();
            // ouput is now an array of the input file.
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

        for (int i = 0; i < output.length; i++)
        {
            // Determines length of counters which will decide number of farmers created on each side.
            if (output[i].equals("N"))
            {
                northCounter = (Integer.parseInt(output[i + 2]));
            }
            else if (output[i].equals("S"))
            {
                southCounter = (Integer.parseInt(output[i + 2]));
            }
        }

        for (int i = 0; i < northCounter; i++)
        {
            // Creates the farmers and adds them to the farmers list for north side.
                Farmer farmer = new Farmer();
                farmer.setID("N_farmer" + (i + 1));
                farmer.setSteps(0);
                farmer.setDirection("north");
                farmer.setBridge(bridge);
                North.add(farmer);
        }

        for (int i = 0; i < southCounter; i++)
        {
            // Creates the farmers and adds them to the farmers list for south side.
                Farmer farmer = new Farmer();
                farmer.setID("S_farmer" + (i + 1));
                farmer.setSteps(0);
                farmer.setDirection("south");
                farmer.setBridge(bridge);
                South.add(farmer);
        }

        for (int i = 0; i < (North.size() + South.size()); i++)
        {
            // Adds the farmers to the farmers list.
            farmers.add(North.remove());
            farmers.add(South.remove());
        }

        for (int i = 0; i < farmers.size(); i++)
        {
            // Starts the farmers.
            Farmer frodo = new Farmer();
            frodo = farmers.get(i);
            frodo.start();
        }


    } 

    
    

}