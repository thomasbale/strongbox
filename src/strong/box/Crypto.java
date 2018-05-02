/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;

/**
 *
 * @author thomasbale
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

   static Boolean fileProcessor(State session){
       
       if(session.isReady()==false){
           System.out.print("Session not ready abort");
           return false;
       }
       
	 try {
             
               Randompass keyr = new Randompass();
    
               session.setKey(keyr.generateToken(session.keylen()));
               int cipherMode = session.cipherMode();
               //String key = "This is a secret";
              // File inputFile = session.getInput();
               File outputFile = session.getTarget();
               
               //byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
               //IvParameterSpec ivspec = new IvParameterSpec(iv);
               
              //int cipherMode = Cipher.ENCRYPT_MODE;
              File inputFile = session.getInput();
              //File outputFile = new File(session.getInput().toPath().toString() + ".dec");
              String key = "This is a secret";
             
                      
	       Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
               
	       Cipher cipher = Cipher.getInstance("AES");       
               cipher.init(cipherMode, secretKey);
          
                                                  
               
	       FileInputStream inputStream = new FileInputStream(inputFile);
	       byte[] inputBytes = new byte[(int) inputFile.length()];
	       inputStream.read(inputBytes);

	       byte[] outputBytes = cipher.doFinal(inputBytes);

	       FileOutputStream outputStream = new FileOutputStream(outputFile);
	       outputStream.write(outputBytes);

	       inputStream.close();
	       outputStream.close();
               
        

	    } catch (/*InvalidAlgorithmParameterException | */NoSuchPaddingException | NoSuchAlgorithmException
                     | InvalidKeyException | BadPaddingException
	             | IllegalBlockSizeException | IOException e) {
		e.printStackTrace();
            }
         return true;
     }


}
