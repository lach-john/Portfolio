//Lachlan McQualter c3205442 - SENG4500 A1

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class TaxServer
{
    public static void main(String[]args) throws IOException
    {
        // Server connection established - Session start
        ServerSocket ServerSocket = new ServerSocket(Integer.parseInt(args[0]));
        Socket s = ServerSocket.accept();
        System.out.println("Connection Established");
        InputStreamReader reader = new InputStreamReader(s.getInputStream());
        BufferedReader buffered = new BufferedReader(reader);
        PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
        System.out.println("CLIENT: " + buffered.readLine());
        writer.println("TAX: OK");
        // ------------------------------------------------------------------------
        
        // ArrayList established, uses comparator method within TaxBracket
        ArrayList<TaxBracket> incomes = new ArrayList<TaxBracket>(12);

        // Use switch to match user input with desired method 
        while(s.isConnected())
        {
            String command = "";
            Double commandDouble = 0.0;

            // Try/Catch block checks for case where user has entered a number rather than a command
            try 
            {
                commandDouble = Double.parseDouble(buffered.readLine());
                command = "INCOME";
            } 
            catch (NumberFormatException nfe) 
            {
                command = buffered.readLine();
                System.out.println(command);
            }

            // Switch block containing cases for each command or for when user enters a number
            switch (command) 
            {  
                case "INCOME": 
                        System.out.println("Client: " + commandDouble);
                        boolean found = false;
                        writer.println("INCOME: OK");
                        for(int i = 0; i < incomes.size(); i++)
                        {
                            if(commandDouble >= incomes.get(i).getLeft() && commandDouble <= incomes.get(i).getRight())
                            {
                                Double taxPayable = taxPayable(commandDouble, incomes.get(i));
                                writer.println("Tax payable is: " + taxPayable);
                                found = true;
                                break;
                            }
                        }
                        if(!found)
                        {
                            writer.println("I do not know: " + commandDouble);
                        }
                        break;
                case "TAX": 
                        System.out.println("Client: " + command);
                        writer.println("TAX: OK");
                        Double taxDouble = 0.0;
                        boolean taxFound = false;
                        
                        // Try/Catch block makes sure the entered number once TAX has been called, is legitimate
                        try 
                        {
                            taxDouble = Double.parseDouble(buffered.readLine());
                        } 
                        catch (NumberFormatException nfe) 
                        {
                            writer.println("Error: a legitimate income amount must be entered");
                            break;
                        }
                        for(int i = 0; i < incomes.size(); i++)
                        {
                            if(taxDouble >= incomes.get(i).getLeft() && taxDouble <= incomes.get(i).getRight())
                            {
                                Double taxPayable = taxPayable(taxDouble, incomes.get(i));
                                writer.println("Tax payable is: " + taxPayable);
                                taxFound = true;
                                break;
                            }
                        }
                        if(!taxFound)
                        {
                            writer.println("I do not know: " + taxDouble);
                        }
                        break;
                case "UPDATE": 
                        System.out.println("Client: " + command);
                        writer.println("UPDATE: OK");

                        //Pull new tax bracket data from buffered reader --> user input
                        String Start = buffered.readLine();
                        String End = buffered.readLine();
                        String BaseTax = buffered.readLine();
                        String Cents = buffered.readLine();

                        //Create new bracket
                        TaxBracket bracket = new TaxBracket(Double.parseDouble(Start), Double.parseDouble(End), Double.parseDouble(BaseTax), (Double.parseDouble(Cents)/100));

                        //Case 0: Empty ArrayList  
                        if(incomes.isEmpty())
                        {
                            incomes.add(bracket);
                            System.out.println("test 1");
                            writer.println("New tax bracket added succesfully");
                            break;
                        }

                        //Case 1: Bracket overlaps an existing bracket at its upper bound
                        //Case 2: Bracket overlaps an existing bracket at its lower bound 
                        //Case 3: Bracket overlaps an existing bracket at its upper bound the next bracket at its lower bound
                        //Case 4: Bracket exists inside another larger bracket
                        //Case 5: Bracket encompasses another smaller bracket 
                        //Case 6: Bracket is largest in the list with no overlap 
                        //Case 7: Bracket is smallest in the list with no overlap
                        for(int i = 0; i < incomes.size(); i++)
                        {
                            if(incomes.get(i).getRight() >= bracket.getLeft() && bracket.getRight() > incomes.get(i).getRight())
                            {
                                incomes.get(i).setRight(bracket.getLeft() - 1);
                                try 
                                {
                                    if(bracket.getRight() >= incomes.get(i+1).getLeft())
                                    {
                                        incomes.get(i+1).setLeft(bracket.getRight() + 1);
                                        incomes.add(i, bracket);
                                        System.out.println("test 2");
                                        writer.println("New tax bracket added succesfully");
                                        break;
                                    }
                                    else if(bracket.getRight() < incomes.get(i+1).getLeft())
                                    {
                                        incomes.add(i, bracket);
                                        System.out.println("test 3");
                                        writer.println("New tax bracket added succesfully");
                                        break;
                                    }
                                } 
                                catch (IndexOutOfBoundsException ioobe) 
                                {
                                    incomes.add(i+1, bracket);
                                    System.out.println("test 4");
                                    writer.println("New tax bracket added succesfully");
                                    break;
                                }
                                
                                break;
                            }
                            else if(bracket.getRight() >= incomes.get(i).getLeft() && bracket.getLeft() < incomes.get(i).getLeft())
                            {
                                incomes.get(i).setLeft(bracket.getRight() + 1);
                                try 
                                {
                                    if(bracket.getLeft() > incomes.get(i-1).getRight())
                                    {
                                        incomes.add(i, bracket);
                                        System.out.println("test 5");
                                        writer.println("New tax bracket added succesfully");
                                        break;
                                    }
                                } 
                                catch (IndexOutOfBoundsException ioobe) 
                                {
                                    incomes.add(i, bracket);
                                    System.out.println("test 6");
                                    writer.println("New tax bracket added succesfully");
                                    break;
                                }

                                break;
                            }
                            else if(bracket.getLeft() > incomes.get(i).getLeft() && bracket.getRight() < incomes.get(i).getRight())
                            {
                                TaxBracket temp = new TaxBracket(bracket.getRight() + 1, incomes.get(i).getRight(), incomes.get(i).getBaseTax(), incomes.get(i).getCentsPer());
                                incomes.add(i+1, temp);
                                incomes.get(i).setLeft(bracket.getLeft() - 1);
                                incomes.add(i, bracket);
                                System.out.println("test 7");
                                writer.println("New tax bracket added succesfully");
                                break;
                            }
                            else if(bracket.getLeft() <= incomes.get(i).getLeft() && bracket.getRight() >= incomes.get(i).getRight())
                            {
                                incomes.remove(i);
                                incomes.add(i, bracket);
                                System.out.println("test 8");
                                writer.println("New tax bracket added succesfully");
                                break;
                            }
                            else if(!incomes.contains(incomes.get(i).getRight() > bracket.getLeft()))
                            {
                                incomes.add(incomes.size() - 1, bracket);
                                writer.println("New tax bracket added succesfully");
                                break;
                            }
                            else if(!incomes.contains(incomes.get(i).getLeft() < bracket.getRight()))
                            {
                                incomes.add(0, bracket);
                                writer.println("New tax bracket added succesfully");
                                break;
                            }

                        }

                        //Sort the ArrayList in ascending order
                        Collections.sort(incomes);

                        break;
                case "QUERY":  
                        System.out.println("Client: " + command);
                        writer.println("QUERY: OK");

                        //ArrayList size sent from server to create a for loop to print on client end
                        writer.println(incomes.size());
                        for(int i = 0; i < incomes.size(); i++)
                        {
                            writer.println(incomes.get(i).getBracket() + " " + incomes.get(i).getBaseTax() + " " + incomes.get(i).getCentsPer());
                        }
                        break;
                case "BYE": 
                        System.out.println("Client: " + command);
                        writer.print("BYE: OK");

                        //Reader, writer, buffered reader and  are all closed 
                        reader.close();
                        writer.close();
                        buffered.close();
                        s.close();

                        //args index 0 is incremented by 1 and the server listens on this new port 
                        int newPort = Integer.parseInt(args[0]) + 1;
                        String newPortString = String.valueOf(newPort);
                        args[0] = newPortString;
                        main(args);
                        break;
                case "END": 
                        System.out.println("Client: " + command); 
                        writer.println("END: OK");
                        s.close();
                        System.exit(0);
                        break;
            }
            
        }

        
    }

    //Tax calculation method 
    public static Double taxPayable(Double income, TaxBracket Tb)
    {
        Double taxPayable = 0.0;
        taxPayable = Tb.getBaseTax() + ((income - Tb.getLeft()) * Tb.getCentsPer());
        return taxPayable;

    }

         
}
