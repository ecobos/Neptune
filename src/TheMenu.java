import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author ecobos
 */
public class TheMenu extends JPanel implements ActionListener{

    public TheMenu() {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu subMenu = new JMenu("About");

        mAddSong = new JMenuItem("Add song to library");
        mAddSong.addActionListener(this);
        mQuit = new JMenuItem("Quit");
        mQuit.addActionListener(this);

        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(mAddSong);
        fileMenu.add(mQuit);

        mMenuBar.add(fileMenu);
        mMenuBar.add(subMenu);
    }

    public JMenuBar getMenu() {
        return mMenuBar;
    }

    public void actionPerformed(ActionEvent event) {
            // will want to close the database connection first
        if (event.getSource() == mQuit){
            System.out.println("Quit was clicked.");
            System.exit(0);
        }
        else if(event.getSource() == mAddSong){
            Library mSongs = new Library();
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(TheMenu.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToLibrary(file.getAbsolutePath());
                System.out.println("Adding: " + file.getName() + ".\n");
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
            
        }
    }
    private JMenuBar mMenuBar;
    private JMenuItem mQuit;
    private JMenuItem mAddSong;
}
