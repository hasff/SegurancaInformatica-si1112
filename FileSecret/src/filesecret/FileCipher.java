package filesecret;

import filesecret.Utils.FileNominator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FileCipher {
    private static final int BUFFER_SIZE = 1024;
    private static final String SYMMETRIC_CRYPTOGRAPHIC_ALGORITHM = "DES";
    
    private final X509Certificate _X509Cerfificate;
    private final KeyStore _KeyStore;
    
    public FileCipher(
            X509Certificate x509Certificate, 
            KeyStore keyStore,
            String pathCerts)
    {
        if (x509Certificate == null)
            throw new IllegalArgumentException("x509Certificate: can not be null!");
        /*if (keyStore == null)
            throw new IllegalArgumentException("KeyStore: can not be null!"); */
        
        this._X509Cerfificate = x509Certificate;
        this._KeyStore = keyStore;
        
        //TODO: validate 'x509Certificate' certificate
    }
    
    // Cipher
    public FileCipherResult doCipher(String file_path) throws NoSuchAlgorithmException, 
            NoSuchPaddingException, FileNotFoundException, IOException, 
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
            CertificateEncodingException
    {
        FileCipherResult result = null; 
        
        // Generate Public Simetric Key.
        KeyGenerator keyGen = KeyGenerator.getInstance(SYMMETRIC_CRYPTOGRAPHIC_ALGORITHM);
        SecretKey ks = keyGen.generateKey();
        
        //Create 'file to encrypt' stream reader
        File file = new File(file_path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        //Create 'file to encrypt' cryptogram stream
        FileOutputStream cryptogramOutStream = 
                new FileOutputStream(FileNominator.getEncryptedFileName(file_path));
        
        //Create and init cipher algorithm
        Cipher c = Cipher.getInstance(SYMMETRIC_CRYPTOGRAPHIC_ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, ks);
        
        //Encrypt!
        byte fileContentByteArr[] = new byte[BUFFER_SIZE];
        int readedBytes;
        
        while ((readedBytes = fileInputStream.read(fileContentByteArr)) != -1)
            cryptogramOutStream.write(c.update(fileContentByteArr,0,readedBytes));
        cryptogramOutStream.write(c.doFinal());
        
        cryptogramOutStream.close();
        fileInputStream.close();
        
        //Create metadata file and save it
        FileSecretMetadata metadata = FileSecretMetadata.Create(ks, _X509Cerfificate);
        metadata.Save(FileNominator.getMetadataFileName(file_path));
        
        return result;
    }
}