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

public class FileDecipherJPane extends JPanel {
    private FileSecretUserInterfacetListener _fileSecretUserInterfacetListener;
    private JButton btn_selectFileToDecipher;
    private JLabel label_fileDecipher;
    
    public FileDecipherJPane() {
        initialize();
    }
    
    public void setFileSecretUserInterfacetListener(
        FileSecretUserInterfacetListener fileSecretUserInterfacetListener) {
        _fileSecretUserInterfacetListener = fileSecretUserInterfacetListener;
    }

    private void initialize() {
        label_fileDecipher = new JLabel("File Decipher");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(label_fileDecipher, BorderLayout.PAGE_START);
        btn_selectFileToDecipher = new JButton("Select file");
        add(btn_selectFileToDecipher, BorderLayout.CENTER);
        btn_selectFileToDecipher.addMouseListener(new FileDecipherJPaneMouserListener(this));
    }
 
    public class FileDecipherJPaneMouserListener implements MouseListener {
        private final FileDecipherJPane _fileDecipherJPane;
                
        private FileDecipherJPaneMouserListener(FileDecipherJPane fileCipherJPane)
        {
            _fileDecipherJPane = fileCipherJPane;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if(btn_selectFileToDecipher == e.getSource())
            {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(_fileDecipherJPane);
                
                if(returnVal == JFileChooser.APPROVE_OPTION)
                    _fileSecretUserInterfacetListener.onFileDecipher(chooser.getSelectedFile().getAbsolutePath());
                
                return;
            }
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}