package filesecret;

import filesecret.Utils.FileNominator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class FileDecipher {
    private static final int BUFFER_SIZE = 1024;
    private final KeyStore _keyStore;
    
    public FileDecipher(KeyStore keyStore) {
        _keyStore = keyStore;
    }
    
    public void doDecipher(String encryptedFilePath) throws FileNotFoundException, IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, Exception {
        String metadataFilePath = FileNominator
                    .getMetadataFileNameByEncryptedFileName(encryptedFilePath);
        
        FileSecretMetadata metadata = FileSecretMetadata.Read(metadataFilePath);
        X509Certificate pubKeyCert = metadata.GetPublicKeyCertificate();
        
        //find pubKeyCert related private key
        PrivateKey privateKey = null;
        Enumeration<String> aliases = _keyStore.aliases();
        while(aliases.hasMoreElements()) {
            KeyStore.Entry entry = _keyStore.getEntry(aliases.nextElement(), new KeyStore.PasswordProtection("changeit".toCharArray()));
            if(entry instanceof KeyStore.PrivateKeyEntry) {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
                
                if(privateKeyEntry.getCertificate().equals(pubKeyCert)){
                    privateKey = privateKeyEntry.getPrivateKey();
                    break;
                }
            }
        }
        
        if(privateKey == null)
            throw new Exception("private key not found!");
       
       //Decrypt file!
       SecretKey secretKey =  metadata.GetSecretKey(privateKey);
       Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
       cipher.init(Cipher.DECRYPT_MODE, secretKey);
       
       String decryptedFileName 
               = FileNominator.getOriginalFileNameByEncryptedFileName(encryptedFilePath);
       
        FileOutputStream decryptedFileOutStream 
               = new FileOutputStream(decryptedFileName);
        FileInputStream encryptedFileInputStream
                = new FileInputStream(encryptedFilePath);
        
        byte fileContentByteArr[] = new byte[BUFFER_SIZE];
        int readedBytes;        
        while ((readedBytes = encryptedFileInputStream.read(fileContentByteArr)) != -1)
            decryptedFileOutStream.write(cipher.update(fileContentByteArr,0,readedBytes));
        decryptedFileOutStream.write(cipher.doFinal());
        
        encryptedFileInputStream.close();
        decryptedFileOutStream.flush();
        decryptedFileOutStream.close();
    }
}