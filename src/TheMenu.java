import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ecobos
 */
public class TheMenu extends JPanel implements ActionListener{

    public TheMenu() {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        mAbout = new JMenuItem("About");
        mAbout.addActionListener(this);

        mPlaySongNotInLibrary = new JMenuItem("Play song not in library");
        mAddSong = new JMenuItem("Add song to library");
        //mAddSong.addActionListener(this);
        mDeleteSong = new JMenuItem("Delete selected song from library");
        mQuit = new JMenuItem("Quit");
        mQuit.addActionListener(this);

        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(mPlaySongNotInLibrary);
        fileMenu.add(mAddSong);
        fileMenu.add(mDeleteSong);
        fileMenu.add(mQuit);

        mMenuBar.add(fileMenu);
        mMenuBar.add(mAbout);
    }

    public JMenuBar getMenu() {
        return mMenuBar;
    }
    
    public JMenuItem getAddSongItem(){
        return mAddSong;
    }
    
    public JMenuItem getDeleteSongItem() {
        return mDeleteSong;
    }
    
    public JMenuItem getPlaySongNotInLibrary() {
        return mPlaySongNotInLibrary;
    }

    public void actionPerformed(ActionEvent event) {
            // will want to close the database connection first
        if (event.getSource() == mQuit){
            System.out.println("Quit was clicked.");
            System.exit(0);
        }else if(event.getSource() == mAbout){
            System.out.println("Clicked the about section");
            String info = "Neptune Music Player\n\nDeveloped by:\n\t\tEdgar Cobos\n\t\tKelby Sapien\n\t\tGil Pena\n\nVersion: 1.0";
            JOptionPane.showMessageDialog(mMenuBar, info, "About", WIDTH, new ImageIcon(this.getClass().getResource("/resources/neptune.png")));
        }
        else if(event.getSource() == mQuit){
            Library mSongs = new Library();
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(TheMenu.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToDatabase(file.getAbsolutePath());
                System.out.println("Adding: " + file.getName() + ".\n");
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
            
        }
    }
    private JMenuBar mMenuBar;
    private JMenuItem mAbout;
    private JMenuItem mQuit;
    private JMenuItem mAddSong;
    private JMenuItem mPlaySongNotInLibrary;
    private JMenuItem mDeleteSong;
}
