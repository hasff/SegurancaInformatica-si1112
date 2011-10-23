package filesecret;

import filesecret.utils.FileNominator;
<<<<<<< HEAD
=======
import java.io.File;
import java.io.FileInputStream;
>>>>>>> 1e0283804216afeb7cf453cb867521b9a2d98268
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FileCipher {
    private static final int BUFFER_SIZE = 1024;
    private static final String SYMMETRIC_CRYPTOGRAPHIC_ALGORITHM = "DES";
    
    private final X509Certificate _X509Cerfificate;
    private final CertStore _intermediateCerts;
    private final KeyStore _trustAnchors;
    
    public FileCipher(
            X509Certificate x509Certificate, 
            CertStore intermediateCerts,
            KeyStore trustAnchors
            ) throws Exception
    {
        if (x509Certificate == null)
            throw new IllegalArgumentException("x509Certificate: can not be null!");
        
        if (intermediateCerts == null)
            throw new IllegalArgumentException("intermediateCerts: can not be null!");       
        
        if (trustAnchors == null)
            throw new IllegalArgumentException("trustAnchors: can not be null!");   
        
        _X509Cerfificate = x509Certificate;
        _intermediateCerts = intermediateCerts;
        _trustAnchors = trustAnchors;
        
        //Validate 'x509Certificate' certificate
        X509CertSelector certToValidate = new X509CertSelector();
        certToValidate.setCertificate(x509Certificate);
        
        _validateCertificate(certToValidate, _intermediateCerts, _trustAnchors);
    }
    
    private void _validateCertificate(
            X509CertSelector certToValidate, 
            CertStore intermediateCerts,
            KeyStore trustAnchors
            ) throws Exception 
    {
        try {
            PKIXBuilderParameters builderParams = new PKIXBuilderParameters(trustAnchors, certToValidate);
            builderParams.addCertStore(intermediateCerts);
            builderParams.setRevocationEnabled(false);

            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");

            // Throws CertPathBuilderException exception if is invalid.
            builder.build(builderParams);
            
        } 
        catch (Exception e) { 
            throw e;
        }
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
        
        /*
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
        
        */
        
        //Create ciphered file and save it.
        FileSecretCipher cipher = FileSecretCipher.Create(ks, file_path);
        cipher.Save(String.format("%s.secret", file_path));
        
        //Create metadata file and save it
        FileSecretMetadata metadata = FileSecretMetadata.Create(ks, _X509Cerfificate);
        metadata.Save(FileNominator.getMetadataFileName(file_path));
        
        return result;
    }
}