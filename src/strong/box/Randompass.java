/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong.box;
import java.security.SecureRandom;


/**
 *
 * @author thomasbale
 */
public class Randompass {
    
    // adapted from https://gist.github.com/davidadale/4075606
    // Purpose of this class is to generate and return a random seed token of the correct length
    
   protected static SecureRandom random = new SecureRandom();
   
   public synchronized String generateToken(int length) {
               long longToken = Math.abs( random.nextLong() );
               String random = Long.toString(longToken,length);
               System.out.println(random);
               return (random);
        }
}
