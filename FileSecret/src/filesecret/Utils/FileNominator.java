package filesecret.Utils;

public class FileNominator {
    public static String getMetadataFileName(String orignalFilename) {
        return String.format("%s.secret.metadata", orignalFilename);
    }
    
    public static String getEncryptedFileName(String orignalFilename) {
        return String.format("%s.secret", orignalFilename);
    }
    
    public static String getMetadataFileNameByEncryptedFileName(
                            String encryptedFileName) {
        return String.format("%s.metadata", encryptedFileName);
    }
}