package hasher;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	public static void main(String[] args) throws IOException 
	{
		if(args.length < 2)
			throw new IllegalArgumentException("arguments missing");
		
		String hashFunction = args[0];
		MessageDigest messageDigest;
		
		//Create hash function instance
		try {
			messageDigest = MessageDigest.getInstance(hashFunction);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("invalid hash function name");
		}
		
		//Read file and calculate hash value
		String filePath = args[1];
		FileInputStream fileReader = new FileInputStream(filePath);
		
		int i;
		while((i = fileReader.read()) != -1)
			messageDigest.update((byte) i);
		
		byte[] hash = messageDigest.digest();
		
		for(i=0; i < hash.length; ++i) {
			System.out.printf("%x ",hash[i]);
		}
	}
}