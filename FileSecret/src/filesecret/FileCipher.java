/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesecret;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Hugo
 */
public class FileCipher {

    private final X509Certificate _X509Cerfificate;
    private final KeyStore _KeyStore;
    //private final String 
    
    public FileCipher(
            X509Certificate x509Certificate, 
            KeyStore keyStore,
            String pathCerts)
    {
        if (x509Certificate == null) throw new IllegalArgumentException("x509Certificate: can not be null!");
        if (keyStore == null) throw new IllegalArgumentException("KeyStore: can not be null!");
        
        this._X509Cerfificate = x509Certificate;
        this._KeyStore = keyStore;
       
    }
    
    // Cipher
    public FileCipherResult doCipher(String file_path) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        FileCipherResult result = null; 
        String cryptographicAlgorithm = "DES";
        int blockSize = 1024;

        // Generate Public Simetric Key.
        KeyGenerator keyGen = KeyGenerator.getInstance(cryptographicAlgorithm);
        SecretKey ks = keyGen.generateKey();
        
        // Get file content in byte[].
        File file = new File(file_path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        // Encrypt algorithm.
        Cipher c = Cipher.getInstance(cryptographicAlgorithm);
        c.init(Cipher.ENCRYPT_MODE, ks);
        
        byte fileContentByteArr[] = new byte[blockSize];
        
        while (fileInputStream.read(fileContentByteArr) != -1)
        {
            c.update(fileContentByteArr);
        }
        
        c.doFinal();
        
        return result;
    }
    
    
}
