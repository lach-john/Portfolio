import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.Port;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.*;

public class GuardRoom
{

    int e = 65537;

    final BigInteger P_Public = new BigInteger("17801190547854226652823756245015999014"
      +  "523215636912067427327445031444286578873702077061269525212346307956715678477846"
      +  "644997065077092072785705000966838814403412974522117181850604723115003930107995"
      +  "935806739534871706631980226201971496652413506094591370759495651467285569060679"
      +  "4135837542707371727429551343320695239");

      final BigInteger G_Public = new BigInteger("17406820753240209518581198012352343653860449079"
      + "456135097849583104059995348845582314785159740894095072530779709491575949236830057425"
      + "243876103708447346718014887611810308304375498519098347260155049469132948808339549231"
      + "385000036164648264460849230407872181895999905649609776936801774927370896200668918795"
      + "6744210730");

      BigInteger ipad = new BigInteger("3636363636363636363636363636363636363636363636363636363636363636", 16);
      BigInteger opad = new BigInteger("5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c", 16);

      BigInteger aesKey;

    // big integer modular exponentiation
    public BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulus)
    {
        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);
        while (exponent.compareTo(BigInteger.ZERO) > 0)
        {
            if (exponent.testBit(0))
            {
                result = result.multiply(base).mod(modulus);
            }
            exponent = exponent.shiftRight(1);
            base = base.multiply(base).mod(modulus);
        }
        return result;
    }


    public BigInteger shaHash(String text)
    {
        BigInteger hash = BigInteger.ZERO;
        try
        {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(text.getBytes("UTF-8"));
            byte[] hashBytes = sha.digest();
            hash = new BigInteger(1, hashBytes);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return hash;
    }

    //method for RSA signature with sha256 hash
    public BigInteger sign(String text, BigInteger d, BigInteger n)
    {
        BigInteger signature = modExp(shaHash(text), d, n);
        return signature;
    }

    public BigInteger diffiePRKgen()
    {
        BigInteger temp = BigInteger.probablePrime(1024, new Random());
        return temp;
    }

    public BigInteger diffiePUKgen(BigInteger bigInt)
    {
        BigInteger temp = modExp(G_Public, bigInt, P_Public);
        return temp;
    }

    public BigInteger diffieSecretGen(BigInteger base, BigInteger exponent)
    {
        BigInteger temp = modExp(base, exponent, P_Public);
        return temp;
    }

    public BigInteger authKeyGen(BigInteger secret)
    {
        BigInteger temp = shaHash(secret.toString(e));
        return temp;
    }

    public BigInteger HMAC(BigInteger authKey, byte[] text)
    {
        BigInteger temp = shaHash((opad.xor(authKey)).toString() + shaHash((ipad.xor(authKey)).toString() + text));
        return temp;
    }
    
    //xor method byte[] 3 args
    public byte[] xor(byte[] a, byte[] b, byte[] c)
    {
        byte[] temp = new byte[a.length];
        for (int i = 0; i < a.length; i++)
        {
            temp[i] = (byte) (a[i] ^ b[i] ^ c[i]);
        }
        return temp;
    }


    public byte[] AESencryption(String message, BigInteger key)
    {
        byte[] hashedKey = shaHash(key.toString()).toByteArray();
        
        byte[] messageArray = message.getBytes();
        byte[] encryptedMessage = new byte[messageArray.length + 16];

        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);

        byte[] blockA = new byte[16];
        byte[] blockB = new byte[16];
        byte[] blockC = new byte[16];
        byte[] blockD = new byte[16];

        for (int i = 0; i < 16; i++)
        {
            blockA[i] = messageArray[i];
            blockB[i] = messageArray[i + 16];
            blockC[i] = messageArray[i + 32];
            blockD[i] = messageArray[i + 48];
        }

        byte[] encryptedBlockA = xor(blockA, iv, hashedKey);
        byte[] encryptedBlockB = xor(blockB, encryptedBlockA, hashedKey);
        byte[] encryptedBlockC = xor(blockC, encryptedBlockB, hashedKey);
        byte[] encryptedBlockD = xor(blockD, encryptedBlockC, hashedKey);

        for (int i = 0; i < 16; i++)
        {
            encryptedMessage[i] = encryptedBlockA[i];
            encryptedMessage[i + 16] = encryptedBlockB[i];
            encryptedMessage[i + 32] = encryptedBlockC[i];
            encryptedMessage[i + 48] = encryptedBlockD[i];
        }

        return encryptedMessage;
    }

    public byte[] AESdecryption(byte[] encryptedMessage, BigInteger key)
    {
        byte[] hashedKey = shaHash(key.toString()).toByteArray();
        byte[] decryptedMessage = new byte[encryptedMessage.length - 16];

        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            iv[i] = encryptedMessage[i];
        }

        byte[] blockA = new byte[16];
        byte[] blockB = new byte[16];
        byte[] blockC = new byte[16];
        byte[] blockD = new byte[16];

        for (int i = 0; i < 16; i++)
        {
            blockA[i] = encryptedMessage[i + 16];
            blockB[i] = encryptedMessage[i + 32];
            blockC[i] = encryptedMessage[i + 48];
            blockD[i] = encryptedMessage[i + 64];
        }

        byte[] decryptedBlockA = xor(blockA, iv, hashedKey);
        byte[] decryptedBlockB = xor(blockB, blockA, hashedKey);
        byte[] decryptedBlockC = xor(blockC, blockB, hashedKey);
        byte[] decryptedBlockD = xor(blockD, blockC, hashedKey);

        for (int i = 0; i < 16; i++)
        {
            decryptedMessage[i] = decryptedBlockA[i];
            decryptedMessage[i + 16] = decryptedBlockB[i];
            decryptedMessage[i + 32] = decryptedBlockC[i];
            decryptedMessage[i + 48] = decryptedBlockD[i];
        }

        return decryptedMessage;
    }






    
        





}

