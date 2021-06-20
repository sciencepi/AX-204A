import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

public class Minicraft extends JFrame {
    private static final long serialVersionUID = 1L;

    public Minicraft() {
        super("Minicraft");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        setTitle("Minicraft");
        getContentPane().add(new MinicraftPanel(), BorderLayout.CENTER);

        setSize(640, 480);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {    
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new Minicraft();
                frame.setVisible(true);
            }
        });     
    }
}