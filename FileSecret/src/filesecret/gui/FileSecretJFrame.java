package filesecret.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class FileSecretJFrame extends JFrame {
    private FileCipherJPane fileCipherJPane;
    private final int WIDTH = 400;
    private final int HEIGHT = 320;
    
    public FileSecretJFrame() {
        super("File Secret");
        initialize();
    }
    
    private void initialize() {
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Add components to JFrame
        fileCipherJPane = new FileCipherJPane();
        add(fileCipherJPane, BorderLayout.PAGE_START);
        
        setVisible(true);
    }

    public void setFileSecretUserInterfacetListener(FileSecretUserInterfacetListener fileSecretUserInterfacetListener) {
        fileCipherJPane.setFileSecretUserInterfacetListener(fileSecretUserInterfacetListener);
    }
}
