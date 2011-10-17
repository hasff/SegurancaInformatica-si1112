package filesecret.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileCipherJPane extends JPanel {
    private FileSecretUserInterfacetListener _fileSecretUserInterfacetListener;
    private JButton btn_selectFileToCipher;
    private JLabel label_fileCipher;
    
    public FileCipherJPane() {
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        label_fileCipher = new JLabel("File Cipher");
        add(label_fileCipher, BorderLayout.PAGE_START);
        
        btn_selectFileToCipher = new JButton("Select file");
        add(btn_selectFileToCipher, BorderLayout.CENTER);
        
        btn_selectFileToCipher.addMouseListener(new FileCipherJPaneMouserListener(this));
    }

    public void setFileSecretUserInterfacetListener(
            FileSecretUserInterfacetListener fileSecretUserInterfacetListener) {
        _fileSecretUserInterfacetListener = fileSecretUserInterfacetListener;
    }
    
    private class FileCipherJPaneMouserListener implements MouseListener {
        private final FileCipherJPane _fileCipherJPane;
                
        private FileCipherJPaneMouserListener(FileCipherJPane fileCipherJPane) {
            _fileCipherJPane = fileCipherJPane;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() == btn_selectFileToCipher)
            {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(_fileCipherJPane);
                
                if(returnVal == JFileChooser.APPROVE_OPTION)
                    _fileSecretUserInterfacetListener.onFileCipher(chooser.getSelectedFile().getAbsolutePath());
                
                return;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}