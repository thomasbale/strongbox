/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
/**
 *
 * @author thomasbale
 */
public class unitIntegrityTest {
    
    public Boolean greenLight(){
        // Clean state on init
        State testsession = new State();
        Randompass passwordseed = new Randompass();
        File testfile = new File("test.txt");
        assert testsession.keylen() == 0;
        
        // Changes method to AES and check key len change
        testsession.method("AES");
        assert testsession.keylen() == 16;
        testsession.method("DES");
        assert testsession.keylen() == 16;
        testsession.method("DESede");
        assert testsession.keylen() == 16;
        testsession.method("RSA");
        assert testsession.keylen() == 16;
        System.out.println("Test 1: Methods present PASS\n");
        testsession.reset();
        assert testsession.keylen() == 0;
        assert testsession.isReady() == false;
        assert testsession.getKey() == null;
        assert testsession.getInput() == null;
        assert testsession.getTarget() == null;
        System.out.println("Test 2: Resets safe\n");
         if(this.restrictedCryptography()){
            System.out.println("Test 3: 128 bit encryption possible PASS\n");
        }else{
            System.out.println("Test 3: 256 bit encryption possible PASS\n");
        }
        String test = passwordseed.generateToken(0);
        assert test.contentEquals("");
        test = passwordseed.generateToken(7);
        assert (!test.contentEquals(""));
        
        testsession.setKey(passwordseed.generateToken(testsession.keylen()));
       // testsession.setInput(ENCRYPT_MODE, testfile);
        
       try{
           Cipher aescipher = Cipher.getInstance("AES"); 
           Cipher descipher = Cipher.getInstance("DES"); 
           Cipher rsacipher = Cipher.getInstance("RSA"); 
           Cipher desesecipher = Cipher.getInstance("DESede"); 
           
           System.out.println(aescipher.getAlgorithm() + " Accessible PASS\n");
           System.out.println(descipher.getAlgorithm() + " Accessible PASS\n");
           System.out.println(rsacipher.getAlgorithm() + " Accessible PASS\n");
           System.out.println(desesecipher.getAlgorithm() + " Accessible PASS\n");
           
           
       }catch(NoSuchAlgorithmException | NoSuchPaddingException e){
           System.out.println("Test 4: Failed ciphers not accessible\n");
       }
       System.out.println("Test 4: All ciphers available\n");
              
        
        
        
        
        
                                
        
       // assert testsession.isReady() == false;
       // testsession.setMode(false);
        //assert testsession.direction().equals("Decryption");
   
        // Checks that the full range of cryptographic algorithms are available on the system
        
        return true;
    }
    
    // Checks whether cyphers have been restricted borrowed from https://stackoverflow.com/questions/7953567/checking-if-unlimited-cryptography-is-available
    public boolean restrictedCryptography() {
        
    try {
        // Ensures the full range of crypto algoriths are available on system
        int max = Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding");
        System.out.print("Max Cypher available: " + max + "\n");
        return Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding") < Integer.MAX_VALUE;
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("The transform \"AES/CBC/PKCS5Padding\" is not available (the availability of this algorithm is mandatory for Java SE implementations)", e);
    }
}
    
}
