import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author ecobos
 */
public class TheMenu {

    public TheMenu() {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu subMenu = new JMenu("Options");

        JMenuItem addSong = new JMenuItem("Add song to library");
        addSong.addActionListener(new AddSongActionListener());
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new QuitButtonActionListener());

        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(addSong);
        fileMenu.add(quit);

        mMenuBar.add(fileMenu);
        mMenuBar.add(subMenu);
    }

    public JMenuBar get() {
        return mMenuBar;
    }

    private class QuitButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // will want to close the database connection first
            System.out.println("Quit was clicked.");
            System.exit(0);
        }
    }

    private class AddSongActionListener implements ActionListener {
        JFileChooser fc = new JFileChooser();
        public void actionPerformed(ActionEvent e) {
            // will want to close the database connection first
            System.out.println("Add song button was clicked");
            int returnVal = fc.showOpenDialog(mMenuBar);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n" );
            }
            
        }
    }
    private JMenuBar mMenuBar;
}
