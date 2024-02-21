//Lachlan McQualter c3205442 - SENG4500 A1

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TaxClient
{
    public static void main(String[]args) throws IOException
    {
        // Server connection established - Session start
        Socket ClientSocket = new Socket("localhost", Integer.parseInt(args[0]));
        PrintWriter writer = new PrintWriter(ClientSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        InputStreamReader reader = new InputStreamReader(ClientSocket.getInputStream());
        BufferedReader buffered = new BufferedReader(reader);
        writer.println("TAX");
        System.out.println("Server: " + buffered.readLine());
        // ----------------------------------------------------------------------------------

        while(ClientSocket.isConnected())
        {
            // Prints: enter prompt statement
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println("Please enter a command prompt: (TAX, UPDATE, QUERY, END, BYE). Or simply enter the income amount you wish to query");

            String command = scanner.nextLine();
            Double commandDouble = 0.0;

            // Check if command input is a double, or a string
            try 
            {
                commandDouble = Double.parseDouble(command);
                writer.println(commandDouble);
            } 
            catch (NumberFormatException nfe) 
            {
                writer.println(command);
                writer.println(command);
            }

            //Case: Income
            if(commandDouble != 0.0)
            {
                System.out.println("Server: " + buffered.readLine());
                System.out.println(buffered.readLine());
            }

            //Case: TAX
            if(command.equals("TAX"))
            {
                System.out.println("Server: " + buffered.readLine());
                System.out.println("Enter income: ");
                String incomeTax = scanner.nextLine();
                writer.println(incomeTax);
                System.out.println("Server: " + buffered.readLine());
            }

            //Case: UPDATE
            if(command.equals("UPDATE"))
            {
                System.out.println("Server: " + buffered.readLine());

                //Lower bracket limit
                System.out.println("Enter starting income: ");
                String lowerLimit = scanner.nextLine();
                writer.println(lowerLimit);
                //Upper bracket limit
                System.out.println("Enter ending income: ");
                String upperLimit = scanner.nextLine();
                writer.println(upperLimit);
                //Base tax amount
                System.out.println("Enter base tax amount: ");
                String baseTax = scanner.nextLine();
                writer.println(baseTax);
                //Cents per dollar paid
                System.out.println("Enter tax payed within the threshold, per dollar, in cents (enter a whole number, not a decimal): ");
                String centsPer = scanner.nextLine();
                writer.println(centsPer);
                System.out.println("Server: " + buffered.readLine());
            }

            //Case: QUERY
            if(command.equals("QUERY"))
            {
                System.out.println("Server: " + buffered.readLine());
                
                //Length of server list collected, used to create for loop to print from buffered reader
                Integer length = Integer.parseInt(buffered.readLine());
                for(int i = 0; i < length; i++)
                {
                    System.out.println("Server: " + buffered.readLine());
                }
            }

            //Case: BYE
            if(command.equals("BYE"))
            {
                //To reconnect, run client on original port number + 1 
                System.out.println("Server: " + buffered.readLine());
                break;
            }

            //Case: END
            if(command.equals("END"))
            {
                System.out.println("Server: " + buffered.readLine());
                break;
            }

        }

        //Scanner and socket closed 
        scanner.close();
        ClientSocket.close();
    }
        
}