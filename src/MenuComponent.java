import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JCheckBoxMenuItem;
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
    private JMenu mControls; // Controls Menu
    private JMenuItem mPlay;
    private JMenuItem mNext; 
    private JMenuItem mPrev;
    private JMenu mPlayRecent;
    private JMenuItem mGotoCurrentSong;
    private JMenuItem mIncVol;
    private JMenuItem mDecVol;
    private JMenuItem mShuffle;
    private JMenuItem mRepeat;
    
    /**
     * Class constructor. 
     * 
     * @param isPlaylist True means that its a playlist view. 
     */
    public MenuComponent(boolean isPlaylist) {
        mMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        mControls = new JMenu("Controls");
        mAbout = new JMenuItem("About"); // why JMenuItem and not simply JMenu
        
        mPlaySongNotInLibrary = new JMenuItem("Play song not in library");
        mAddSong = new JMenuItem("Add song to library");
        mPlaylist = new JMenuItem("Create a new playlist");
        mDeleteSongPlaylist = new JMenuItem("Delete selected song from playlist");
        mDeleteSong = new JMenuItem("Delete selected song from library");
        mQuit = new JMenuItem("Quit");
        
        // Controls JMenuItems
        mPlay = new JMenuItem("Play (Space)");
        mNext = new JMenuItem("Next (Ctrl+Right Arrow)");
        mPrev = new JMenuItem("Previous (Ctrl+Left Arrow)");
        mPlayRecent = new JMenu("Play Recent");
        mGotoCurrentSong = new JMenuItem("Go to Current Song");
        mIncVol = new JMenuItem("Increase Volume (Ctrl+I)");
        mDecVol = new JMenuItem("Decrease Volume (Ctrl+D)");
        mShuffle = new JCheckBoxMenuItem("Shuffle");
        mRepeat = new JCheckBoxMenuItem("Repeat");
        
        mControls.add(mPlay);
        mControls.add(mNext);
        mControls.add(mPrev);
        mControls.add(mPlayRecent);
        mControls.add(mGotoCurrentSong);
        mControls.add(mIncVol);
        mControls.add(mDecVol);
        mControls.add(mShuffle);
        mControls.add(mRepeat);

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
        mMenuBar.add(mControls);
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
    public JMenuItem getPlayControlObj(){
        return mPlay;
    }
    public JMenuItem getPrevControlObj(){
        return mPrev;
    }
    public JMenuItem getNextControlObj() {
        return mNext;
    }
    public JMenuItem getPlayRecentControlObj(){
        return mPlayRecent;
    }
    public JMenuItem getIncVolControlObj(){
        return mIncVol;
    }
    public JMenuItem getDecVolControlObj(){
        return mDecVol;
    }
    public JMenuItem getShuffleControlObj(){
        return mShuffle;
    }
    public JMenuItem getRepeatControlObj(){
        return mRepeat;
    }
    
    public JMenuItem getGoToCurrentControlObj(){
        return mGotoCurrentSong;
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
        
        // controls listeners
        mPlay.addActionListener(controller);
        mNext.addActionListener(controller);
        mPrev.addActionListener(controller);
        mPlayRecent.addActionListener(controller);
        mGotoCurrentSong.addActionListener(controller);
        mIncVol.addActionListener(controller);
        mDecVol.addActionListener(controller);
        mShuffle.addActionListener(controller);
        mRepeat.addActionListener(controller);
        
        
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
