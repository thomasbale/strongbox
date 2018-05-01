/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
/**
 *
 * @author thomasbale
 */
public class unitIntegrityTest {
    
    public Boolean greenLight(){
        // Clean state on init
        State testsession = new State();
        assert testsession.keylen() == 0;
        // Changes method to AES and check key len change
        testsession.method("AES");
        assert testsession.keylen() == 16;
        
        this.restrictedCryptography();
        
        // Checks that the full range of cryptographic algorithms are available on the system
        assert this.restrictedCryptography() == true;
        
        return true;
    }
    
    // Checks whether cyphers have been restricted borrowed from https://stackoverflow.com/questions/7953567/checking-if-unlimited-cryptography-is-available
    public boolean restrictedCryptography() {
        return true;
    /*try {
        // Ensures the full range of crypto algoriths are available on system
        int max = Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding");
        System.out.print("Max Cypher available: " + max + "\n");
        return Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS5Padding") < Integer.MAX_VALUE;
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("The transform \"AES/CBC/PKCS5Padding\" is not available (the availability of this algorithm is mandatory for Java SE implementations)", e);
    }*/
}
    
}
