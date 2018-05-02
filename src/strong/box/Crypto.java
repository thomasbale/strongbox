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
           // will cause caller to break out
           return false;
       }

	 try {
        int cipherMode = session.cipherMode();
        File outputFile = session.getTarget();
        File inputFile = session.getInput();
        //  Generates the key on the fly and saves to session. This is done once per attempted encryption.
        session.key();
        String key = session.getKey();
        //  Due to JRE versioning issues AES is the only method which can be safely defaulted. Change below to session.method() to import user selection in live version
	      Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES");
        //  initialise the live cipher
        cipher.init(cipherMode, secretKey);
        //  ready to write to file
	      FileInputStream inputStream = new FileInputStream(inputFile);
	      byte[] inputBytes = new byte[(int) inputFile.length()];
	      inputStream.read(inputBytes);
	      byte[] outputBytes = cipher.doFinal(inputBytes);
	      FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);
        //  Clean up
        inputStream.close();
        outputStream.close();
        /* add InvalidAlgorithmParameterException if using padding | */
	    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
      System.out.print("Session not ready abort"); return false;
            }
         return true;
     }
}
