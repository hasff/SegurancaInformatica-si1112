package filesecret;

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
import javax.crypto.SecretKeyFactory;

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
    
    public static FileSecretMetadata Read(String metadataFilePath) {
        //TODO: read from filesystem and create a FileSecretMetadata
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void Save(String saveFilePath) {
        //TODO: save to filesystem
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public SecretKey GetSecretKey(PrivateKey privateKey) 
    {
        try {
            Cipher cipher = Cipher.getInstance(ASSYMETRIC_CRYPTOGRAPHIC_ALGORITHM);
            
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance(_secretKeyAlgorithm);
            byte[] secretKey = cipher.doFinal(_secretKeyCryptogram);
           
            return null;
            
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
    
    public X509Certificate GetPublicKeyCertificate()
    {
        return _publicKeyCer;
    }
}