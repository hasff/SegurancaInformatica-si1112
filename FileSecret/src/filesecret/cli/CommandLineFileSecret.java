package filesecret.cli;

import filesecret.FileCipher;
import filesecret.FileDecipher;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CommandLineFileSecret {
    private static final String X509_CERT = "X509";
    private static final String CIPHER_OP  = "cipher";
    private static final String DECIPHER_OP = "decipher";
    
    public static void Run(String args[]) {
        if(args.length == 0){
            notEnoughArgs();
            return;
        }
        
        String operation = args[0];
        
        if(operation.equalsIgnoreCase(CIPHER_OP)) 
        {
            cipherOp(args);
            return;
        }
        
        if(operation.equalsIgnoreCase(DECIPHER_OP))
        {
            decipherOp(args);
            return;
        }
        
        invalidArgs();
    }
    
    private static void cipherOp(String args[]) {
        if(args.length < 3){
            notEnoughArgs();
            return;
        }
        
        X509Certificate pubCert = getPublicKeyCertificate(args[1]);
        FileCipher fileCipher = new FileCipher(pubCert, null,null);
        
        long startTimeMillis = System.currentTimeMillis();
        
        System.out.print("File ciphering started... ");
        
        try {
            String fileToCipher = args[2];
            fileCipher.doCipher(fileToCipher);
        } catch (Exception e) {
            handleException(e);
        }
        
        long endtimeInMillis = System.currentTimeMillis();
        
        System.out.println(String.format("done. elapsed time = %d ms", (endtimeInMillis - startTimeMillis)));
    }
    
    private static void decipherOp(String args[]) {
        if(args.length < 3){
            notEnoughArgs();
            return;
        }
        
        KeyStore keyStore = getKeyStore(args[1]);
        
        FileDecipher fileDecipher = new FileDecipher(keyStore);
        
        System.out.print("File deciphering started... ");
        long startTimeMillis = System.currentTimeMillis();
        
        try {
            String fileToDecipher = args[2];
            fileDecipher.doDecipher(fileToDecipher);
        } catch (Exception e) {
            handleException(e);
        }
        long endtimeInMillis = System.currentTimeMillis();
        System.out.println(String.format("done. elapsed time = %d ms", (endtimeInMillis - startTimeMillis)));
    }
    
    private static X509Certificate getPublicKeyCertificate(String pubCertPath) { 
       try{
            FileInputStream inputStream = new FileInputStream(pubCertPath);
            CertificateFactory cf = CertificateFactory.getInstance(X509_CERT);
            return (X509Certificate) cf.generateCertificate(inputStream);
       } catch(Exception e) {
           handleException(e);
           return null;
       }
    }
        
    private static KeyStore getKeyStore(String keyStorePath) {
        try
        {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream keyStoreStream = new FileInputStream(keyStorePath);
            keyStore.load(keyStoreStream, "changeit".toCharArray());
            return keyStore;
        }
        catch(Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    private static void notEnoughArgs()
    {
        System.out.println("not enough arguments");
    }
    
    private static void invalidArgs()
    {
        System.out.println("invalid arguments");
    }
    
    private static void handleException(Exception e)
    {
        
    }
}