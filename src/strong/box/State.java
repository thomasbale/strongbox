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
public class State {
    
    // Have all the conditions been met for encryption to happen?
    private Boolean ready = false;
    //  is this an encryption task?
    private Boolean encryption;
    //  Encryption method
    private String method = "DES";
    //  Secret key/seed
    private String key;
    // key length based on method
    private int keylength;
    
    public void init(){
        System.out.print(method);
    }
    
    public void method(String method){
        this.method = method;
        this.setKeylength();
    }
    
    public void decryption (){
        encryption = false;
    }
    
    public void key(){
        Randompass key = new Randompass();
        this.key = key.generateToken(8);
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
