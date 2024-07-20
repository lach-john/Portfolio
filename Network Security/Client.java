import java.util.*;
import javax.sound.sampled.Port;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.*;

public class Client implements Runnable
{
    int port = 0;
    String hostname = "";
    static String cliID = "784455";
    String servID = " ";
    int publicKeyE = 0;
    int publicKeyN = 0;
    String True = "true";
    String message1 = "Master Chief You Mind Telling Me What You're Doing on this Ship?";
    BigInteger [] clientPublicKeys = new BigInteger[2];
    BigInteger [] clientPrivateKeys = new BigInteger[2];
    BigInteger [] servPublicKeys = new BigInteger[2];
    GuardRoom guard = new GuardRoom();
    public static ObjectInputStream objIn;
    public static ObjectOutputStream objOut;

    public Client(int declaredPort, String declaredHostname)
    {
        port = declaredPort;
        hostname = declaredHostname;
    }
    
    @Override
    public void run()
    {
   
     try{
            System.out.println("Client attempting to connect to server...");
            Thread.sleep(5);
            // Client thread waits while server begins and waits for connection
        }
    catch (InterruptedException e)
        {
            e.printStackTrace();
        }
            
    try(Socket socket = new Socket(hostname, port))
        {
            // wait for server connection to be established 

            // --------------------- Port reading & writing --------------------------
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());
            // --------------------- Port reading & writing --------------------------

            // ------------------ input setup request message to server ---------------
            String open = (String) objIn.readObject();
            System.out.println(open);
            System.out.println("----------------------------------------------------------------");
            System.out.println(" ");      
            String helloReq = "Hello! Setup request from client...";
            objOut.writeObject(helloReq);
            //------------------------------------------------------------------------

            // --------------------- Read server public key ---------------------------
                servPublicKeys[0] = (BigInteger) objIn.readObject();
                servPublicKeys[1]  = (BigInteger) objIn.readObject();
            // ---------------------- Public key stored ------------------------------

            // ---------------------- send client ID ---------------------------------
            objOut.writeObject(cliID);
            // -----------------------------------------------------------------------

            // ---------------------- receive server ID ------------------------------
            String servID = objIn.readUTF();
            BigInteger servIDENC = (BigInteger) objIn.readObject();
            // -----------------------------------------------------------------------

            // ---------------------- decrypt server ID ---------------------------
            BigInteger servIDDEC = guard.modExp(servIDENC, servPublicKeys[0], servPublicKeys[1]);
            BigInteger servIDHASH = guard.shaHash(servID);

            if (servIDDEC.equals(servIDHASH))
            {
                System.out.println("Server ID received, signature verified");
            }
            else
            {
                System.out.println("Arbitrary ID received - not verified");
            }
            // ---------------------------------------------------------------------------
            
            //----------------------------------generate DH keys -------------------------
            BigInteger DH_Priv = guard.diffiePRKgen();
            BigInteger DH_Pub = guard.diffiePUKgen(DH_Priv);
            objOut.writeObject(DH_Pub);
            BigInteger Server_DH_Pub = (BigInteger) objIn.readObject();
            BigInteger Server_DH_PubENC = (BigInteger) objIn.readObject();
            BigInteger Server_DH_PubDEC = guard.modExp(Server_DH_PubENC, servPublicKeys[0], servPublicKeys[1]);
            BigInteger Server_DH_PubHASH = guard.shaHash(Server_DH_Pub.toString());
            if (Server_DH_PubDEC.equals(Server_DH_PubHASH))
            {
                System.out.println("Server DH public key received, signature verified");
            }
            else
            {
                System.out.println("Arbitrary DH public key received - not verified");
            }
            BigInteger secretKey = guard.diffieSecretGen(Server_DH_Pub, DH_Priv);
            System.out.println("Client secret key: generated");
            //----------------------------------------------------------------------------

            // --------------------- Generate Authentication key ------------------------
            BigInteger authenticationKey = guard.authKeyGen(secretKey);
            // ---------------------------------------------------------------------------

            // --------------------- send message 1 ------------------------
            byte[] message1ENC = guard.AESencryption(message1, secretKey);
            objOut.writeObject(message1ENC);
            // ---------------------------------------------------------------------------

            // --------------------- Send Message 1 HMAC ------------------------
            BigInteger message1HMAC = guard.HMAC(authenticationKey, message1ENC);
            objOut.writeObject(message1HMAC);
            // ---------------------------------------------------------------------------

            // --------------------- Receive Message 2 ------------------------
            byte[] message1 = (byte[]) objIn.readObject();
            BigInteger message2HMAC = (BigInteger) objIn.readObject();
            byte[] decryptedMessage2 = guard.AESdecryption(message1, secretKey);
            //--------------------------------------------------------------------------

            // --------------------- Verify Message 2 HMAC ------------------------
            if (guard.HMAC(authenticationKey, message1).equals(message2HMAC))
            {
                System.out.println("Message 2 HMAC verified");
            }
            else
            {
                System.out.println("Message 2 HMAC not verified");
            }
            // ---------------------------------------------------------------------------

            //  --------------------- verify message 2 signature ------------------------
            BigInteger message2SIGN = (BigInteger) objIn.readObject();
            BigInteger message2SIGNDEC = guard.modExp(message2SIGN, servPublicKeys[0], servPublicKeys[1]);
            if (message2SIGNDEC.equals(guard.shaHash(message2HMAC.toString())))
            {
                System.out.println("Message 2 signature verified");
            }
            else
            {
                System.out.println("Message 2 signature not verified");
            }
            // ---------------------------------------------------------------------------

            // --------------------- Print message 2 ------------------------
            String decryptedMessageString = new String(decryptedMessage2);
            System.out.println("Message 1 decrypted: " + decryptedMessageString);
            // ---------------------------------------------------------------------------

            System.exit(0);

            
            

        }
    catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        }
    catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        } 
    catch (ClassNotFoundException e1) 
        {
            e1.printStackTrace();
        } catch (Exception e) {
        
            e.printStackTrace();
        } 
    
    }





}