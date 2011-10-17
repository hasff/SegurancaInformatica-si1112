package filesecret;

import filesecret.gui.FileSecretUserInterfacetListener;
import filesecret.gui.FileSecretJFrame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileSecret {
    private static String X509_CERT = "X509";
    
    public static X509Certificate getPublicKeyCertificate() throws CertificateException, FileNotFoundException
    {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Mehul\\Dropbox\\SegInf - si1112\\serie 1\\SI-Inv1112-Serie1-Enunciado_Anexos\\certificates-and-keys\\distr\\certs.end.entities\\Alice_1_cipher.cer");
        CertificateFactory cf = CertificateFactory.getInstance(X509_CERT);
        return (X509Certificate) cf.generateCertificate(inputStream);
    }
    
    public static KeyStore getKeyStore() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream keyStoreStream = new FileInputStream("C:\\Users\\Mehul\\Dropbox\\SegInf - si1112\\serie 1\\SI-Inv1112-Serie1-Enunciado_Anexos\\certificates-and-keys\\distr\\pfx\\Alice_1_cipher.pfx");
        keyStore.load(keyStoreStream,"changeit".toCharArray());
        return keyStore;
    }
    
    public static void main(String[] args) {
        FileSecretJFrame mainJFrame = new FileSecretJFrame();
        
        mainJFrame.setFileSecretUserInterfacetListener(new FileSecretUserInterfacetListener(){
            @Override
            public void onFileCipher(String fileToCipherPath) {
                try {
                    
                    FileCipher cipher = new FileCipher(
                                                getPublicKeyCertificate(), 
                                                null,
                                                null);
                    
                    cipher.doCipher(fileToCipherPath);
                    
                } catch (CertificateException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPaddingException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalBlockSizeException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadPaddingException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void onFileDecipher(String fileToDecipherPath) {
                try {
                    FileDecipher decipher = new FileDecipher(getKeyStore());
                    decipher.doDecipher(fileToDecipherPath);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CertificateException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (KeyStoreException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnrecoverableEntryException ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(FileSecret.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
