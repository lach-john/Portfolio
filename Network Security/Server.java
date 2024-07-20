import java.util.*;
import javax.sound.sampled.Port;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.*;

public class Server implements Runnable
{

    int e = 65537;
    static String servID = "457788";
    String cliID = " ";
    String True = "true";
    String message2 = "Sir... finishing this fight.. *Chief punches elite in the mouth*";
    BigInteger [] servPublicKeys = new BigInteger[2];
    BigInteger [] servPrivateKeys = new BigInteger[2];
    BigInteger PK_1;
    BigInteger PK_2;
    GuardRoom guard = new GuardRoom();
    public static ObjectInputStream objIn;
    public static ObjectOutputStream objOut;


    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8592);
            while(true)
            {
                // --------------------- Client connection ---------------------
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                // --------------------- Client connection ---------------------

                // --------------------- Port reading & writing --------------------------
                objOut = new ObjectOutputStream(socket.getOutputStream());
                objIn = new ObjectInputStream(socket.getInputStream());
                // --------------------- Port reading & writing ----------------------------
                
                // --------------------- comms channel opened by server ----------------------------
                String open = " ";
                objOut.writeObject(open);

                String helloReq = (String) objIn.readObject();
                System.out.println(helloReq);
                
                // -------------------------------------------------------------------------

                // --------------------- Generate RSA key ------------------------------
                RSAkeyGen(e);
                // ------------------------------------------------------------------------


                // --------------------- Send public key ---------------------------------
                BigInteger SPK1 = servPublicKeys[0];
                objOut.writeObject(SPK1);
                BigInteger SPK2 = servPublicKeys[1];
                objOut.writeObject(SPK2);
                // ------------------------------------------------------------------------

                // --------------------- Receive client's ID ------------------------------
                cliID = (String) objIn.readObject();
                System.out.println("Client ID received");   
                // ------------------------------------------------------------------------
                
                // --------------------- Send Server ID with signature ---------------------
                objOut.writeUTF(servID);
                BigInteger signature = guard.sign(servID, servPrivateKeys[0], servPrivateKeys[1]);
                objOut.writeObject(signature);
                //--------------------------------------------------------------------------

                //----------------------------------generate DH keys -------------------------
                BigInteger DH_Priv = guard.diffiePRKgen();
                BigInteger DH_Pub = guard.diffiePUKgen(DH_Priv);
                BigInteger Client_DH_Pub = (BigInteger) objIn.readObject();
                System.out.println("Client DH public key received");
                objOut.writeObject(DH_Pub);
                objOut.writeObject(guard.sign(DH_Pub.toString(e), servPrivateKeys[0], servPrivateKeys[1]));
                BigInteger secretKey = guard.diffieSecretGen(Client_DH_Pub, DH_Priv);
                System.out.println("Server secret key: generated");
                //--------------------------------------------------------------------------

                // --------------------- Generate Authentication key ------------------------
                BigInteger authenticationKey = guard.authKeyGen(secretKey);
                //--------------------------------------------------------------------------

                // ----------------------------- receive message 1 ------------------------------
                byte[] message1 = (byte[]) objIn.readObject();
                BigInteger message1HMAC = (BigInteger) objIn.readObject();
                byte[] decryptedMessage1 = guard.AESdecryption(message1, secretKey);
                if (guard.HMAC(authenticationKey, message1).equals(message1HMAC))
                {
                    System.out.println("Message 1 HMAC verified");
                }
                else
                {
                    System.out.println("Message 1 HMAC not verified");
                }
                // -----------------------------------------------------------------------------

                // ----------------------------- print message 1 ------------------------------
                String decryptedMessageString = new String(decryptedMessage1);
                System.out.println("Message 2 decrypted: " + decryptedMessageString);
                // --------------------------------------------------------------------------
                
                // --------------------- send message 2 ------------------------
                byte[] message2ENC = guard.AESencryption(message2, secretKey);
                objOut.writeObject(message2ENC);
                // ---------------------------------------------------------------------------

                // --------------------- Send Message 2 HMAC ------------------------
                BigInteger message2HMAC = guard.HMAC(authenticationKey, message2ENC);
                objOut.writeObject(message2HMAC);
                // ---------------------------------------------------------------------------

                // --------------------- Send message 2 signature ------------------------
                BigInteger message2Signature = guard.sign(message2HMAC.toString(), servPrivateKeys[0], servPrivateKeys[1]);
                objOut.writeObject(message2Signature);
                // ---------------------------------------------------------------------------

            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Could not listen on port: 8592");
            System.exit(-1);
        } catch (Exception e1) {

            e1.printStackTrace();
        }

    }

    // --------------------- Methods ------------------------------

    public void RSAkeyGen(int E)
    {
        // generate two random primes
        BigInteger p = BigInteger.probablePrime(1024, new Random());
        BigInteger q = BigInteger.probablePrime(1024, new Random());

        // calculate n
        BigInteger n = p.multiply(q);

        // calculate phi(n)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // calculate e
        BigInteger e = BigInteger.valueOf(E);

        // calculate d
        BigInteger d = e.modInverse(phi);

        // store public key
        servPublicKeys[0] = e;
        servPublicKeys[1] = n;

        // store private key
        servPrivateKeys[0] = d;
        servPrivateKeys[1] = n;
    }


    
}