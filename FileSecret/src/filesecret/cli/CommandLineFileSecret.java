package filesecret.cli;

import filesecret.FileCipher;
import filesecret.FileDecipher;
import java.io.File;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CommandLineFileSecret {
    private static final String X509_CERT = "X509";
    private static final String CIPHER_OP  = "cipher";
    private static final String DECIPHER_OP = "decipher";
    
    private static final String KEYSTORE_PASSWORD = "changeit";
    
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
    
    /***
     * args[1] - X509 Certificate.
     * args[2] - Intermediate Certificates dir.
     * args[3] - Trust Anchors.
     * args[4] - file to cipher.
     * @param args 
     */
    private static void cipherOp(String args[]) {
        if(args.length < 5){
            notEnoughArgs();
            return;
        }
        
        X509Certificate pubCert = getPublicKeyCertificate(args[1]);
        CertStore certStore = getIntermediateCertificates(args[2]);
        KeyStore trustAnchors = getTrustAnchors(args[3]);
        
        FileCipher fileCipher = null;
        try
        {
            fileCipher = new FileCipher(pubCert, certStore, trustAnchors);
        }
        catch (CertPathBuilderException e)
        {
            System.out.println("Certificate is not trusted!");
            handleException(e);
            return;
        }
        catch(Exception e)
        {
            handleException(e);
            return;
        }
        
        long startTimeMillis = System.currentTimeMillis();
        
        System.out.print("File ciphering started... ");
        
        try {

            String fileToCipher = args[4];
            fileCipher.doCipher(fileToCipher);
        } catch (Exception e) {
            handleException(e);
        }
        
        long endtimeInMillis = System.currentTimeMillis();
        
        System.out.println(String.format("done. elapsed time = %d ms", (endtimeInMillis - startTimeMillis)));
    }
    
    /***
     * args[1] - KeyStore.
     * args[2] - File to decipher.
     * @param args 
     */
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
    
    private static CertStore getIntermediateCertificates(String intermediateCertsPath)
    {
        CertStore certStoreResult = null;
        
        try
        {
            File[] certFiles = new File(intermediateCertsPath).listFiles();
            LinkedList<Certificate> certificateList = new LinkedList<Certificate>();
            CertificateFactory cf = CertificateFactory.getInstance(X509_CERT);

            for (File certFile : certFiles)
            {
                certificateList.add(cf.generateCertificate(new FileInputStream(certFile)) );
            }
            
            certStoreResult = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certificateList));
        }
        catch(Exception e)
        {
            handleException(e);        
        }
        
        return certStoreResult;
    }
    
    private static KeyStore getTrustAnchors(String trustAnchorsPath)
    {
        KeyStore trustAnchorsResult = null;
        
        try
        {
            trustAnchorsResult = KeyStore.getInstance("JKS");
            trustAnchorsResult.load(new FileInputStream(trustAnchorsPath), KEYSTORE_PASSWORD.toCharArray());
        }
        catch (Exception e)
        {
            handleException(e);         
        }
        
        return trustAnchorsResult;
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
        System.out.println("EXCEPTION : " + e.getMessage());
    }
}