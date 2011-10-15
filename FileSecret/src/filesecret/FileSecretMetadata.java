package filesecret;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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
    private static final String X509_CERT = "X.509";
    private static final String ASSYMETRIC_CRYPTOGRAPHIC_ALGORITHM = "rsa";
    
    private final String _secretKeyAlgorithm;
    private final byte[] _secretKeyCryptogram;
    private final X509Certificate _publicKeyCer;
    
    /*
     *  FileSecretMetadata instances are created using the 
     * static methods: Create and Read.
     */
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
    
    public static FileSecretMetadata Read(String metadataFilePath) throws 
            FileNotFoundException, IOException, CertificateException {
        //Create a FileInputStream to read  'metadataFilePath'
        FileInputStream metadaFileInStream = new FileInputStream(metadataFilePath);
        
        //Read the used symmetric encryption algorithm
        byte[] algorithm = readByteArray(metadaFileInStream);
        
        //Read the symmetric key (encrypted with a public key)
        byte[] secretKeyCrypt = readByteArray(metadaFileInStream);
        
        //...
        String secretKeyAlgorithm = new String(algorithm);
        
        //Read public key certificate used to encrypt the symmetric key
        CertificateFactory cf = CertificateFactory.getInstance(X509_CERT);
        X509Certificate pubKeycer = (X509Certificate) cf.generateCertificate(metadaFileInStream);
        
        metadaFileInStream.close();
        
        //Create a FileSecretMetadata and return
        return new FileSecretMetadata(secretKeyCrypt, secretKeyAlgorithm, pubKeycer);
    }

    public void Save(String saveFilePath) throws FileNotFoundException, IOException, CertificateEncodingException {
        FileOutputStream metadataFileOutStream = new FileOutputStream(saveFilePath);
        
        //Write the used symmetric algorithm
        byte[] secretKeyAlgorithmBytes = _secretKeyAlgorithm.getBytes();
        writeArrayToStream(metadataFileOutStream, secretKeyAlgorithmBytes);
        
        //Write the encrypted symmetric key
        writeArrayToStream(metadataFileOutStream, _secretKeyCryptogram);
        
        //Write the public key certificated used to encrypt the symmetric ket
        byte[] encondedCer = _publicKeyCer.getEncoded();
        metadataFileOutStream.write(encondedCer);
        
        metadataFileOutStream.flush();
        metadataFileOutStream.close();
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
    
    private static void writeArrayToStream(FileOutputStream stream, byte[] array) throws IOException {
        int len = array.length;
        stream.write((len >>  24) & 0xFF);
        stream.write((len >>  16) & 0xFF);
        stream.write((len >>  8) & 0xFF);
        stream.write(len & 0xFF);
        stream.write(array);
    }
    
    private static int readInt(FileInputStream stream) throws IOException {
        byte[] intBytes = new byte[4];
        
        if(stream.read(intBytes) != intBytes.length)
            throw new IndexOutOfBoundsException("stream does not have enough bytes");
        
        return intBytes[0] << 24 
                | intBytes[1] << 16 
                | intBytes[2] << 8
                | intBytes[3];
    }
    
    private static byte[] readByteArray(FileInputStream stream) throws IOException {
        int len = readInt(stream);
        
        byte[] buffer =  new byte[len];
        
        if(stream.read(buffer) != len)
            throw new IndexOutOfBoundsException("array size is inconsistent");
        
        return buffer;
    }
}