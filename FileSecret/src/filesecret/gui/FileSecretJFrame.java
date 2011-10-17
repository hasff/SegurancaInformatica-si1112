package filesecret.gui;

import java.awt.GridLayout;
import javax.swing.JFrame;

public class FileSecretJFrame extends JFrame {
    private FileCipherJPane fileCipherJPane;
    private FileDecipherJPane  fileDecipherJPane;
    private final int WIDTH = 200;
    private final int HEIGHT = 160;
    
    public FileSecretJFrame() {
        super("File Secret");
        initialize();
    }
    
    private void initialize() {
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(1,1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        fileCipherJPane =  new FileCipherJPane();
        fileDecipherJPane = new FileDecipherJPane();
        
        add(fileCipherJPane);
        add(fileDecipherJPane);
        
        setVisible(true);
    }

    public void setFileSecretUserInterfacetListener(FileSecretUserInterfacetListener fileSecretUserInterfacetListener) {
        fileCipherJPane.setFileSecretUserInterfacetListener(fileSecretUserInterfacetListener);
        fileDecipherJPane.setFileSecretUserInterfacetListener(fileSecretUserInterfacetListener);
    }
}
