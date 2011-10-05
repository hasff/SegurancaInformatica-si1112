package filesecret;

import filesecret.gui.FileSecretUserInterfacetListener;
import filesecret.gui.FileSecretJFrame;

public class FileSecret {
    
    public static void main(String[] args) {
        FileSecretJFrame mainJFrame = new FileSecretJFrame();
        
        mainJFrame.setFileSecretUserInterfacetListener(new FileSecretUserInterfacetListener(){
            @Override
            public void onFileCipher(String fileToCipherPath) {
                System.out.println(fileToCipherPath);
            }
        });
    }
}
