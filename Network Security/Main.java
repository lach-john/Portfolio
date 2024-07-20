//Lachlan McQualter - SENG2250 Assignment 3
// Markers note: HMAC is implemented but never validates, I ran out of time to fix this but believe it has something
// to do with the byte[] being sent over ports and changing slightly, so they deliver a different HMAC value
// all other aspects of the assignment are implemented and working.

import java.util.*;
import java.io.*;

public class Main 
{
    public static void main(String[] args)
    {
        final int finalPort = 8592;

        System.out.println("Please enter server port number: ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        while (port != finalPort)
        {
            System.out.println("Port number is not valid");
            port = 0;
            System.out.println("Please enter valid port number: ");
            port = scanner.nextInt();
        }
        
        Runnable server = new Server();
        Runnable client = new Client(port, "localhost");
        
        // localhost is the default hostname for the server as it exists on the same machine as the client
        
            new Thread(client).start(); 
            new Thread(server).start();
        


    }
}
    

