package strong.box;

import java.io.File;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

// Holds the current session state to ensure consistency and security
public class State {
    // Have all the conditions been met for encryption to happen?
    private Boolean ready = false;
    //  is this an encryption task?
    private Boolean encryption = true;
    private int cipherMode = ENCRYPT_MODE;
    //  Encryption method e.g. AES
    private String method;
    private String direction = "Encryption";
    //  Secret key/seed
    private String key = null;
    // key length based on method
    private int keylength = 0;
    // From and to where?
    private File target;
    private File input;

    public void init(){
        System.out.print("Initialised session with method: " + method + "...\n");
        System.out.print("Initialised session with  key: " + key + "...\n");
        System.out.print("Initialised session with mode: " + cipherMode + "...\n");
        System.out.print("Initialised session with keylength: " + keylength + "...\n");
    }

    public Boolean setInput(int mode, File input){
        this.input = input;
        cipherMode = mode;
        //  Are we ready to go?
        this.flush();
        System.out.print("UPDATED: ...\n");
        System.out.print("Initialised session with method: " + method + "...\n");
        System.out.print("Initialised session with key: " + key + "...\n");
        System.out.print("Initialised session with mode: " + cipherMode + "...\n");
        System.out.print("Initialised session with keylength: " + keylength + "...\n");
        return true;
    }

    // Getter functions
    public Boolean isReady(){
        return ready;
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

    public String getMethod(){
        return method;
    }

    public String direction(){
        return direction;
    }
    // Set methods
    public void setMode(Boolean encryption){
        if(ready == true){
            return;
        }
        this.encryption = encryption;
        if(encryption == false){
            direction = "Decryption";
        }
        this.flush();
    }
    public void method(String method){
        this.method = method;
        this.setKeylength();
    }
    public void flush (){
        if(cipherMode == DECRYPT_MODE){
           System.out.print("Decrypt mode enabled...\n");
           this.direction = "Decryption";
          // System.out.print("Target set to ...\n" + target.toPath().toString());
           this.target = new File(input.toPath().toString() + ".dec");
           System.out.print("Target set to ...\n" + target.toPath().toString());
           this.setKeylength();
           this.checkReady();
           return;
        }
        this.target = new File(input.toPath().toString() + ".enc");
        this.setKeylength();
        this.checkReady();
        return;
    }
    public void reset(){
    ready = false;
    encryption = true;
    cipherMode = ENCRYPT_MODE;
    method = null;
    key = null;
    keylength = 0;
    target = null;
    }

    private void checkReady(){
        if(method != null && keylength != 0 && input != null && target != null){
            ready = true;
        }
        return;
    }

    public void key(){
        Randompass key = new Randompass();
        this.key = key.generateToken(this.keylength);
    }

    public void setTarget(File target){
        this.target = target;
    }

    private void setKeylength(){
      switch(method){
      case  "DES" : this.keylength = 16; this.method = "DES";break;
      case  "AES" : this.keylength = 16;break;
      case  "DESede" : this.keylength = 16;break;
      case  "RSA" : this.keylength = 16;break;
      default : this.keylength = 16;
        }
    }

    public void setKey(String key){
          this.key = key;
      }
}
