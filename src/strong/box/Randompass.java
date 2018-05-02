
package strong.box;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

// Class to generate random security keys as seeds for ciphers
//  Class also has function to adjust key lengths on system, however, this function is not used
public class Randompass {
    // adapted from https://gist.github.com/davidadale/4075606
    // Generate and return a random seed token of the correct length
   protected static SecureRandom random = new SecureRandom();
   public synchronized String generateToken(int length) {
               long longToken = Math.abs( random.nextLong() );
               String random = Long.toString(longToken,length);
               random = String.format("0x%08X", longToken);
               random = random.substring(0, Math.min(random.length(), length));
               System.out.println(random);
               return (random);
        }

        // function to manipulate key length and allow maximum. This is not used.
        // source: https://github.com/Delthas/JavaSkype/commit/c329dc06c28b0b2a6f2b6c05cc08bad1e0cdf9c8
   public static void fixKeyLength() {
    String errorString = "Failed manually overriding key-length permissions.";
    int newMaxKeyLength;
    try {
        if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
            Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
            Constructor con = c.getDeclaredConstructor();
            con.setAccessible(true);
            Object allPermissionCollection = con.newInstance();
            Field f = c.getDeclaredField("all_allowed");
            f.setAccessible(true);
            f.setBoolean(allPermissionCollection, true);

            c = Class.forName("javax.crypto.CryptoPermissions");
            con = c.getDeclaredConstructor();
            con.setAccessible(true);
            Object allPermissions = con.newInstance();
            f = c.getDeclaredField("perms");
            f.setAccessible(true);
            ((Map) f.get(allPermissions)).put("*", allPermissionCollection);

            c = Class.forName("javax.crypto.JceSecurityManager");
            f = c.getDeclaredField("defaultPolicy");
            f.setAccessible(true);
            Field mf = Field.class.getDeclaredField("modifiers");
            mf.setAccessible(true);
            mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(null, allPermissions);

            newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
        }
    } catch (Exception e) {
        throw new RuntimeException(errorString, e);
    }
    if (newMaxKeyLength < 256)
        throw new RuntimeException(errorString); // hack failed
}
}
