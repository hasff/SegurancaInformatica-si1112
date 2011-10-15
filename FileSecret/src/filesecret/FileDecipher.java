package filesecret;

import filesecret.Utils.FileNominator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class FileDecipher {
    private final KeyStore _keyStore;
    
    public FileDecipher(KeyStore keyStore) {
        _keyStore = keyStore;
    }
    
    public void doDecipher(String encryptedFilePath) throws FileNotFoundException, IOException, CertificateException {
        String metadataFilePath = FileNominator
                    .getMetadataFileNameByEncryptedFileName(encryptedFilePath);
        FileSecretMetadata metadata = FileSecretMetadata.Read(metadataFilePath);
        
        X509Certificate pubKeyCert = metadata.GetPublicKeyCertificate();
        
        //_keyStore.
    }
}