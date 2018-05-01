/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;

import java.io.File;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 *
 * @author thomasbale
 */
public class State {
    
    // Have all the conditions been met for encryption to happen?
    private Boolean ready = false;
    //  is this an encryption task?
    private Boolean encryption = true;
    private int cipherMode = ENCRYPT_MODE;
    //  Encryption method
    private String method = "AES";
    //  Secret key/seed
    private String key = "This is a secret";
    // key length based on method
    private int keylength = 0;
    private File target;
    private File input;
    

    public void init(){

        
        System.out.print("Initialised session with method: " + method + "...\n");
        System.out.print("Initialised session with default key: " + key + "...\n");
        System.out.print("Initialised session with mode: " + cipherMode + "...\n");
        System.out.print("Initialised session with keylength: " + keylength + "...\n");
    }
    
    public Boolean setInput(Boolean encryption, File input){
        // set mode/ set encryption/dec
        // set method
        // set input
        // set target
        // generate key
        // set keylen
        // set ready - return true
        this.input = input;
        this.setMode(encryption);
        this.flush();
        
        System.out.print("UPDATED: ...\n");
        System.out.print("Initialised session with method: " + method + "...\n");
        System.out.print("Initialised session with default key: " + key + "...\n");
        System.out.print("Initialised session with mode: " + cipherMode + "...\n");
        System.out.print("Initialised session with keylength: " + keylength + "...\n");
        
        
        return true;
    }
    
    public int keylen(){
        return keylength;
    }
    
    public int cipherMode(){
        return cipherMode;
    }
    
    public String getKey(){
        return this.key;
    }
    
    public File getInput(){
        return this.input;
    }
    
    public File getTarget(){
        return this.target;
    }
    
    public void setMode(Boolean encryption){
        this.encryption = encryption;
        this.flush();
    }
    
    public void method(String method){
        this.method = method;
        this.setKeylength();
    }
    
    public void flush (){
        if(this.encryption = false){
           this.cipherMode = DECRYPT_MODE;
           System.out.print("Decrypt mode enabled...\n");
           this.target = new File(input.toPath().toString() + ".dec");
           return;
        }
        this.target = new File(input.toPath().toString() + ".enc");
        this.setKeylength();
    }
    
    public void key(){
        Randompass key = new Randompass();
        this.key = key.generateToken(8);
    }
    
    public void setTarget(File target){
        this.target = target;
    }
    
    private void setKeylength(){
        
      switch(method){
      case  "DES" : this.keylength = 7;
      case  "AES" : this.keylength = 16;
      case  "DESede" : this.keylength = 18;
      case  "RSA" : this.keylength = 18;
      default : this.keylength = 7;
    }
        
   }
}
