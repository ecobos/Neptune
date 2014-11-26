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
    private JMenuItem mDeleteSongPlaylist;
    private JMenuItem mPlaylist;
    
    /**
     * Class constructor. 
     * 
     * @param isPlaylist True means that its a playlist view. 
     */
    public MenuComponent(boolean isPlaylist) {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        mAbout = new JMenuItem("About");
        
        mPlaySongNotInLibrary = new JMenuItem("Play song not in library");
        mAddSong = new JMenuItem("Add song to library");
        mPlaylist = new JMenuItem("Create a new playlist");
        mDeleteSongPlaylist = new JMenuItem("Delete selected song from playlist");
        mDeleteSong = new JMenuItem("Delete selected song from library");
        mQuit = new JMenuItem("Quit");

        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(mPlaySongNotInLibrary);
        fileMenu.add(mAddSong);
        fileMenu.add(mPlaylist);
        
        if(isPlaylist){
            mDeleteSong.setVisible(false);
        }
        else{
            mDeleteSongPlaylist.setVisible(false);
        }
        
        fileMenu.add(mDeleteSongPlaylist);
        fileMenu.add(mDeleteSong);
        fileMenu.add(mQuit);

        mMenuBar.add(fileMenu);
        mMenuBar.add(mAbout);
    }
    
    /**
     * Sets the menu for specific type of view 
     * 
     * @param isPlaylist True means that its a playlist view
     */
    public void setPlaylistMenu(boolean isPlaylist){
        if(isPlaylist){
            mDeleteSong.setVisible(!isPlaylist);
            mDeleteSongPlaylist.setVisible(isPlaylist);
        }
        else{
            mDeleteSongPlaylist.setVisible(isPlaylist);
            mDeleteSong.setVisible(!isPlaylist);
        }
        mMenuBar.repaint();
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
    public JMenuItem getDeleteSongPlaylistObj(){
        return mDeleteSongPlaylist;
    }
    public JMenuBar getMenu() {
        return mMenuBar;
    }
    public JMenuItem getPlaylistObj(){
        return mPlaylist;
    }
     //**********************

    public void setController(ActionListener controller){
        mAbout.addActionListener(controller);
        mPlaySongNotInLibrary.addActionListener(controller);
        mAddSong.addActionListener(controller);
        mDeleteSong.addActionListener(controller);
        mQuit.addActionListener(controller);
        mPlaylist.addActionListener(controller);
        mDeleteSongPlaylist.addActionListener(controller);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
