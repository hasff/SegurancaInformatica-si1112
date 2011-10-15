package filesecret;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileSecretMetadata {
    private static final String ASSYMETRIC_CRYPTOGRAPHIC_ALGORITHM = "rsa";
    
    private final String _secretKeyAlgorithm;
    private final byte[] _secretKeyCryptogram;
    private final X509Certificate _publicKeyCer;
    
    private FileSecretMetadata(byte[] secretKeyCryptogram, 
            String secretKeyAlgorithm, X509Certificate publicKeyCer) {
        _publicKeyCer = publicKeyCer;
        _secretKeyCryptogram = secretKeyCryptogram;
        _secretKeyAlgorithm = secretKeyAlgorithm;
    }
    
    public static FileSecretMetadata Create(SecretKey secretKey, 
            X509Certificate publicKeyCer) {
        try {
            
            PublicKey pK = publicKeyCer.getPublicKey();
            Cipher cipher = Cipher.getInstance(ASSYMETRIC_CRYPTOGRAPHIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pK);
            
            byte[] secreKeyCryptogram 
                    = cipher.doFinal(secretKey.getEncoded());
            
            return new FileSecretMetadata(secreKeyCryptogram,
                    secretKey.getAlgorithm(),
                    publicKeyCer);
            
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new UnknownError();
    }
    
    public static FileSecretMetadata Read(String metadataFilePath) throws FileNotFoundException {
        FileOutputStream metadaFileOutStream = new FileOutputStream(metadataFilePath);
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void Save(String saveFilePath) throws FileNotFoundException, IOException {
        FileOutputStream metadataFileOutStream = new FileOutputStream(saveFilePath);
        
        byte[] secretKeyAlgorithmBytes = _secretKeyAlgorithm.getBytes();
        int len = secretKeyAlgorithmBytes.length;
        
        metadataFileOutStream.write((len >>  24) & 0xFF);
        metadataFileOutStream.write((len >>  16) & 0xFF);
        metadataFileOutStream.write((len >>  8) & 0xFF);
        metadataFileOutStream.write(len & 0xFF);
        
        metadataFileOutStream.write(secretKeyAlgorithmBytes);
    }
    
    public SecretKey GetSecretKey(PrivateKey privateKey) {
        try {
            
            Cipher cipher = Cipher.getInstance(ASSYMETRIC_CRYPTOGRAPHIC_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            
            byte[] secretKey = cipher.doFinal(_secretKeyCryptogram);
            
            return new SecretKeySpec(secretKey, _secretKeyAlgorithm);
            
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(FileSecretMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new UnknownError();
    }
    
    public X509Certificate GetPublicKeyCertificate() {
        return _publicKeyCer;
    }
}