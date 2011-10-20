package filesecret.concurrent;

import java.io.InputStream;
import java.security.Key;

public class ConcurrrentCipher {
    private int opmode;
    private Key key;
    private int bytesPerThread;
    private int numberOfThreads;
    
    public ConcurrrentCipher(int opmode,
            Key key, 
            int bytesPerThread, 
            int numberOfThreads)
    {
        this.opmode = opmode;
        this.key = key;
        this.bytesPerThread = bytesPerThread;
        this.numberOfThreads = numberOfThreads;
    }
    
    public void doCipher(InputStream inputStream)
    {
        
    }
}
