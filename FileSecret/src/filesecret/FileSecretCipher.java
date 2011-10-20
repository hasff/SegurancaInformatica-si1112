/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filesecret;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Hugo
 */
public class FileSecretCipher {
    private static final int BUFFER_SIZE = 1024;
    
    private final SecretKey ks;
    private final String uncipheredFilePath;
    
    private FileSecretCipher(String fp, SecretKey ks)
    {
        this.uncipheredFilePath = fp;
        this.ks = ks;
    }

    public static FileSecretCipher Create(SecretKey ks, String fileToCipherPath)
    {
        return new FileSecretCipher(fileToCipherPath, ks);
    }
    
    public void Save(String cipheredFilePath) 
            throws InvalidKeyException, 
            NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException
    {
        //Create 'file to encrypt' stream reader
        File file = new File(this.uncipheredFilePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        //Create 'file to encrypt' cryptogram stream
        FileOutputStream cryptogramOutStream = 
                new FileOutputStream(cipheredFilePath);
        
        //Create and init cipher algorithm
        Cipher c = Cipher.getInstance(this.ks.getAlgorithm());
        c.init(Cipher.ENCRYPT_MODE, ks);
        
        //Encrypt!
        byte fileContentByteArr[] = new byte[BUFFER_SIZE];
        int readedBytes;
        
        while ((readedBytes = fileInputStream.read(fileContentByteArr)) != -1)
            cryptogramOutStream.write(c.update(fileContentByteArr,0,readedBytes));
        cryptogramOutStream.write(c.doFinal());
        
        cryptogramOutStream.close();
        fileInputStream.close();    
    }  
}
