import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuComponent implements Observer{
    private JMenuBar mMenuBar;
    private JMenuItem mAbout;
    private JMenuItem mQuit;
    private JMenuItem mAddSong;
    private JMenuItem mPlaySongNotInLibrary;
    private JMenuItem mDeleteSong;
    
    public MenuComponent(boolean isPlaylist) {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        mAbout = new JMenuItem("About");
        
        mPlaySongNotInLibrary = new JMenuItem("Play song not in library");
        mAddSong = new JMenuItem("Add song to library");
              
        mQuit = new JMenuItem("Quit");

        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(mPlaySongNotInLibrary);
        fileMenu.add(mAddSong);
        
        if(isPlaylist){
            mDeleteSong = new JMenuItem("Delete song from playlist");
        }
        else{
            mDeleteSong = new JMenuItem("Delete selected song from library");
        }
        
        fileMenu.add(mDeleteSong);
        fileMenu.add(mQuit);

        mMenuBar.add(fileMenu);
        mMenuBar.add(mAbout);
    }
    
    //**********************
    //Needed for the action listerns 
    public JMenuItem getAboutObj(){
        return mAbout;
    }
    public JMenuItem getQuitObj(){
        return mQuit;
    }
    public JMenuItem getAddSongObj(){
        return mAddSong;
    }
    public JMenuItem getSongNotInLibObj(){
        return mPlaySongNotInLibrary;
    }
    public JMenuItem getDeleteSongObj(){
        return mDeleteSong;
    }
    public JMenuBar getMenu() {
        return mMenuBar;
    }
     //**********************

    public void setController(ActionListener controller){
        mAbout.addActionListener(controller);
        mPlaySongNotInLibrary.addActionListener(controller);
        mAddSong.addActionListener(controller);
        mDeleteSong.addActionListener(controller);
        mQuit.addActionListener(controller);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
