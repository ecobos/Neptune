import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class NeptuneController implements ActionListener, MouseListener, DropTargetListener, ChangeListener, BasicPlayerListener, WindowListener {

    private Neptune neptune;
    private boolean isPaused;
    private BasicPlayer player;
    private BasicController playerControl;
    private Database mDatabase;
    private ButtonsComponent mButtons;
    private MenuComponent mMenuBar;
    private TextAreaComponent mSongInfo;
    private SongsTableComponent mTable;
    private JSliderComponent mSlider;
    private JProgressBarComponent mProgress;
    private JTreeComponent mTree;
    private boolean isPlaylistView;
    private NeptuneController parent;


    /**
     * Class constructor.
     *
     * @param isPlaylistview True means that its a playlist view
     */
    NeptuneController(boolean isPlaylistview, NeptuneController theParent) {
        player = new BasicPlayer();
        player.addBasicPlayerListener(this);
        playerControl = (BasicController) player;
        isPaused = false;
        isPlaylistView = isPlaylistview;
        if(theParent != null){
            parent = theParent;
        }
    }

    public void addPlayerView(Neptune mp) {
        this.neptune = mp;
    }

    public void addSlider(JSliderComponent slider) {
        this.mSlider = slider;
    }

    public void addDatabaseModel(Database conn) {
        this.mDatabase = conn;
    }

    public void addTree(JTreeComponent tree) {
        this.mTree = tree;
    }

    public void addButtonsView(ButtonsComponent buttons) {
        this.mButtons = buttons;
    }

    public void addMenuView(MenuComponent menu) {
        this.mMenuBar = menu;
        mMenuBar.restoreSongHistory(mDatabase.getSongHistory(), this);
    }

    public void addTextView(TextAreaComponent text) {
        this.mSongInfo = text;
    }

    public void addTableModel(SongsTableComponent table) {
        this.mTable = table;
        if(!isPlaylistView){
           mTable.updatePopupSubmenu(mTree.getLeafNodeNames(), mDatabase); 
        }
        
    }
    
    public void addProgressBar(JProgressBarComponent progress){
        mProgress = progress;
    }

    /**
     * Play the specified song.
     *
     * @param songToPlay The song to be played
     */
    private void playSong(Vector<String> songToPlay) {

        if (isPaused) {
            try {
                playerControl.resume();
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
            isPaused = false;
        } else {
            try {
                File filepath = new File(songToPlay.get(0));
                playerControl.open(filepath);
                mSongInfo.setText("\n\n Current song playing:\n\tArtist: " + songToPlay.get(2)
                        + "\n\tSong: " + songToPlay.get(1) + "\n\tAlbum: "
                        + songToPlay.get(3) + "\n\tSong " + (mTable.getCurrentSongPlayingIndex() + 1) + " of " + mTable.getSongsCount());
                //String somethign = songToPlay.get(9).toString();
                //int money = Integer.parseInt(somethign);
                mProgress.setLength(Integer.parseInt(songToPlay.get(9)));
                playerControl.play();
                playerControl.setGain(mSlider.getValue());
                
                if(!mMenuBar.isShuffleEnabled()){
                    mMenuBar.addSongToHistory(songToPlay, this);
                }
                
            } catch (BasicPlayerException ex) {
                JOptionPane.showMessageDialog(mTable.getMenuAddObj(), "Song does not exist!");
            }
        }
    }
    
    public void updateLibraryData(){
        mDatabase.updateLibrary();
    }

    /**
     * Adds a song to the database
     */
    private void addSong() {
        FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(neptune.getPanelObj());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            mDatabase.addSong(file.getAbsolutePath());
            // delete the line below
            mDatabase.getPlaylistsFromDatabase();
        } else {
            System.out.println("Open command cancelled by user.\n");
        }
    }

    /**
     * Deletes the selected song from the database
     */
    private void deleteSongSelected() {
        String filepath = mTable.getSongSelectedFilepath();
        System.out.println("Deleted song: " + filepath);
        mDatabase.deleteSong(mTable.getSongSelected(filepath));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Random rand = new Random();
        String filepath = mTable.getSongSelectedFilepath();
        int randomNum; //= rand.nextInt(mTable.getSongsCount());
        // QUIT BUTTON
        if (source == mMenuBar.getQuitObj()) {
            System.out.println("Quit was clicked.");
             String[] fields = {"showArtist", "showAlbum", "showYear", "showGenre", "showComments"}; 	 	
            for(int i=0;i<5;i++) { 	 	
                mDatabase.setPlayerSettings(fields[i], mTable.getChangedSettings()[i]); 	 	
            }
            neptune.destroyFrame();
            System.exit(0);
        } // ABOUT BUTTON        
        else if (source == mMenuBar.getAboutObj()) {
            System.out.println("Clicked the about section");
            String info = "Neptune Music Player\n\nDeveloped by:\n\t\tEdgar Cobos\n\t\tKelby Sapien\n\t\tGil Pena\n\nVersion: 3.0";
            JOptionPane.showMessageDialog(mMenuBar.getMenu(), info, "About", PLAIN_MESSAGE, new ImageIcon(this.getClass().getResource("/resources/neptune.png")));
        } // ADD SONG BUTTON
        else if (source == mMenuBar.getAddSongObj() || source == mButtons.getAddSongObj()) {
            if (isPlaylistView) {
            } else {
                this.addSong();
            }
        } // DELETE SONG -- Fix deleting song from playlist, repaint playlist window --- That has been done!
        else if (source == mMenuBar.getDeleteSongObj()) {
            this.deleteSongSelected();

        } else if (source == mMenuBar.getDeleteSongPlaylistObj()) {
//            System.out.println("playlist delete");
//            mDatabase.deleteSongFromPlaylist(mTable.getSongSelectedID(), mDatabase.getPlaylistIDfromName(mTable.getTableName()));
//            mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(mTable.getTableName()));
            
            System.out.println("song association deleted from playlist");
            mDatabase.deleteSongFromPlaylist(mDatabase.getSongID(mTable.getSongSelectedFilepath()), mDatabase.getPlaylistIDfromName(mTable.getTableName()));
            mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(mTable.getTableName()));
        } // PLAY SONG NOT IN LIBRARY
        else if (source == mMenuBar.getSongNotInLibObj()) {
            FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(mMenuBar.getSongNotInLibObj());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                SongTags tags = new SongTags();
                String[] songTags = tags.extractSongTags(file.getAbsolutePath());
                //mSongs.addSongToLocalTable(file.getAbsolutePath());

                try {
                    playerControl.open(file);
                    mProgress.setLength(Integer.parseInt(songTags[8]));
                    playerControl.play();
                    mSongInfo.setText("\n\n Playing a song not in the library");
                    //System.out.println("Adding song with filepath: " + file.getAbsolutePath() + " to library.");
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
        } // PLAY BUTTON
        else if (source == mButtons.getPlayObj() || source == mMenuBar.getPlayControlObj() || e.getActionCommand().equals("Space")) {
           if (!mMenuBar.isShuffleEnabled() || mMenuBar.isShuffleEnabled() && player.getStatus() == 0 || player.getStatus() == -1) {//{mMenuBar.isShuffleEnabled() && (player.getStatus() == 0 || player.getStatus() == -1)) {//If shuffle is on and a song is playing shuffle to next
           
                //System.out.println("Shuffle songs on next" + player.getStatus());
                //mTable.setSongPlayingIndex(randomNum);
                //playSong(mTable.getSongSelected(filepath));
                //mTable.scrollToSongPlaying();
                //System.out.println("Random: " + randomNum + "\nSong playing filepath: " + filepath);
                //System.out.println("Playing: " + mTable.getSongSelectedVector().get(1));
                //mTable.scrollToSongPlaying();
            //} else {
                Object filepathObj = mTable.getSongsTableObj().getValueAt(0, 0);
                if (mTable.getSongsTableObj().getSelectedRow() == -1) {
                    mTable.setSongPlayingIndex(0);
                    playSong(mTable.getSongSelected((String)filepathObj));
                    mTable.scrollToSongPlaying();
                    mTable.setSongSelected();
                    filepathObj = mTable.getSongsTableObj().getValueAt(1, 0);
                    mTable.setNextSongFilepath((String)filepathObj);
                    filepathObj = mTable.getSongsTableObj().getValueAt(mTable.getSongsCount() - 1, 0);
                    mTable.setPrevSongFilepath((String)filepathObj);
                }
                else {
                    mTable.setSongSelected();
                    int row = mTable.getSongSelected();
                    int next = row + 1;
                    int prev = row - 1;
                    filepath = mTable.getSongSelectedFilepath();
                    mTable.setSongPlayingIndex(row);
                    playSong(mTable.getSongSelected(filepath));
                    mTable.scrollToSongPlaying();
                    mTable.setSongSelected();
                    if (next >= mTable.getSongsCount()) {
                        next = 0;
                    }
                    filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
                    mTable.setNextSongFilepath((String)filepathObj);
                    System.out.println("Next filepath: " + filepathObj + " " + next);
                    if (prev < 0) {
                        prev = mTable.getSongsCount() - 1; //wrap around the index
                    }
                    filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
                    mTable.setPrevSongFilepath((String)filepathObj);
                    System.out.println("Previous filepath: " + filepathObj + " " + prev);
                }
            }
        } // STOP SONG BUTTON
        else if (source == mButtons.getStopObj()) {
            try {
                playerControl.stop();
                isPaused = false;
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // PAUSE SONG BUTTON
        else if (source == mButtons.getPauseObj()) {
            try {
                playerControl.pause();
                isPaused = true;
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // NEXT SONG BUTTON
        else if (source == mButtons.getNextObj() || source == mMenuBar.getNextControlObj() || e.getActionCommand().equals("RightArrow")) {
            if (mMenuBar.isShuffleEnabled() && player.getStatus() == 0) {//If shuffle is on and a song is playing shuffle to next
                rand = new Random();
                randomNum = rand.nextInt(mTable.getSongsCount());
                int row = randomNum;
                int next = row + 1;
                int prev = row - 1; 
                if (next >= mTable.getSongsCount()) {
                    next = 0;
                }
                if (prev < 0) {
                    prev = mTable.getSongsCount() - 1; //wrap around the index
                }
                Object filepathObj = mTable.getSongsTableObj().getValueAt(randomNum, 0);
                System.out.println("Shuffle songs on!\nStatus: " + player.getStatus() + " Random: " + randomNum);
                mTable.setSongPlayingIndex(randomNum);
                playSong(mTable.getSongSelected((String)filepathObj));
                mTable.scrollToSongPlaying();
                System.out.println("Filepath: " + filepathObj.toString());
                filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
                mTable.setNextSongFilepath(filepathObj.toString());
                System.out.println("Next filepath: " + filepathObj + "\nNext: " + next + " Row: " + row);
                filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
                mTable.setPrevSongFilepath(filepathObj.toString());
                System.out.println("Previous filepath: " + filepathObj + " " + prev);
            } else {
                int row = mTable.getCurrentSongPlayingIndex();
                int next = row + 1;
                int prev = row; 
                if (next >= mTable.getSongsCount()) {
                    next = 0;
                }
                mTable.setSongPlayingIndex(next);
                playSong(mTable.getSongSelected(mTable.getNextFilepath()));  
                mTable.scrollToSongPlaying();
                Object filepathObj = null;
                if(next == mTable.getSongsCount() - 1){
                    filepathObj = mTable.getSongsTableObj().getValueAt(0, 0);
                }
                else {
                    filepathObj = mTable.getSongsTableObj().getValueAt(++next, 0);
                }
                mTable.setNextSongFilepath((String)filepathObj);
                System.out.println("Next filepath: " + filepathObj + " next: " + next + " row: " + row);
                filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
                mTable.setPrevSongFilepath((String)filepathObj);
                System.out.println("Previous filepath: " + filepathObj + " " + prev);
            }
        } // PREVIOUS SONG BUTTON
        else if (source == mButtons.getPrevObj() || source == mMenuBar.getPrevControlObj() || e.getActionCommand().equals("LeftArrow")) {
            if (mMenuBar.isShuffleEnabled() && player.getStatus() == 0) { //If shuffle is on and a song is playing shuffle
                rand = new Random();
                randomNum = rand.nextInt(mTable.getSongsCount());
                int row = randomNum;
                int next = row + 1;
                int prev = row - 1; 
                if (next >= mTable.getSongsCount()) {
                    next = 0;
                }
                if (prev < 0) {
                    prev = mTable.getSongsCount() - 1; //wrap around the index
                }
                Object filepathObj = mTable.getSongsTableObj().getValueAt(randomNum, 0);
                System.out.println("Shuffle songs on!\nStatus: " + player.getStatus() + " Random: " + randomNum);
                mTable.setSongPlayingIndex(randomNum);
                playSong(mTable.getSongSelected((String)filepathObj));
                mTable.scrollToSongPlaying();
                System.out.println("Filepath: " + filepathObj.toString());
                filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
                mTable.setNextSongFilepath(filepathObj.toString());
                System.out.println("Next filepath: " + filepathObj + "\nNext: " + next + " Row: " + row);
                filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
                mTable.setPrevSongFilepath(filepathObj.toString());
                System.out.println("Previous filepath: " + filepathObj + " " + prev);
            }
            else {
                int row = mTable.getCurrentSongPlayingIndex();
                int next = row;
                int prev = row - 1; 
                if (prev < 0) {
                    prev = mTable.getSongsCount() - 1; //wrap around the index
                }
                mTable.setSongPlayingIndex(prev);                
                playSong(mTable.getSongSelected(mTable.getPrevFilepath()));  
                mTable.scrollToSongPlaying();
                Object filepathObj = null;
                if(prev == 0){
                    filepathObj = mTable.getSongsTableObj().getValueAt(mTable.getSongsCount() - 1, 0);
                }
                else {
                    filepathObj = mTable.getSongsTableObj().getValueAt(--prev, 0);
                }
                mTable.setPrevSongFilepath((String)filepathObj);
                System.out.println("Previous filepath: " + filepathObj + " " + prev);                
                filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
                mTable.setNextSongFilepath((String)filepathObj);
                System.out.println("Next filepath: " + filepathObj + " next: " + next + " row: " + row);
                
            }
        } // CREATING PLAYLIST
        else if (source == mMenuBar.getPlaylistObj()) {
            System.out.println("Playlist clicked");
            String playlistName = JOptionPane.showInputDialog(mMenuBar.getMenu(), "Enter a new playlist name");
            if (playlistName.equals("")) {
                playlistName = "playlist";
            } //check for empty string
            mDatabase.addPlaylist(playlistName);
            mTree.addNodeToTree(playlistName);
            mTree.setNewBranchAsSelected();
            mTree.getJTreeObj().treeDidChange();
            mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(playlistName)); // updates library table with empty playlist set
        } // CONTROLS - GO TO CURRENT SONG
        else if (source == mMenuBar.getGoToCurrentControlObj() || e.getActionCommand().equals("Current")) {
//            System.out.println("Go to current song");
//            mTable.getSongSelected();
            mTable.scrollToSongPlaying();
            int index = mTable.getCurrentSongPlayingIndex();
            mTable.setSelectionInterval(index);
        } // CONTROLS - PLAY SONG
       /* else if (source == mMenuBar.getPlayControlObj() || e.getActionCommand().equals("Space")) {
            if (mMenuBar.isShuffleEnabled()) {
                System.out.println("Shuffle songs on " + player.getStatus());
                if (player.getStatus() == 0) {
                    System.out.println("Song is currently playing start shuffle after");
                }
                else{
                	randomNum = rand.nextInt(mTable.getSongsCount());
                    mTable.setSongPlayingIndex(randomNum);
                    playSong(mTable.getSongSelected(filepath));
                    mTable.scrollToSongPlaying();
                    System.out.println("Song playing filepath: " + filepath);
                    //System.out.println("Playing: " + mTable.getSongSelectedVector().get(1));
                } 
            } else {
                if (mTable.getSongsTableObj().getSelectedRow() == -1) {//getSongSelected(filepath) == null) { //getSongsTableObj().isColumnSelected(0)){//
                    mTable.setSongPlayingIndex(0);
                    Object filepathObj = mTable.getSongsTableObj().getValueAt(1, 0);
                    mTable.setNextSongFilepath((String)filepathObj);
                    filepathObj = mTable.getSongsTableObj().getValueAt(mTable.getSongsCount() - 1, 0);
                    mTable.setPrevSongFilepath((String)filepathObj);
                    playSong(mTable.getSongSelected(0));
                    System.out.println("No row is selected");
                } else {
                    filepath = mTable.getSongSelectedFilepath();
                    mTable.setSongSelected();
                    int selected = mTable.getSongSelected();
                    mTable.setSongPlayingIndex(selected);
                    playSong(mTable.getSongSelected(filepath));
                    System.out.println("Song playing filepath: " + filepath);
                    System.out.println("Playing: " + mTable.getSongSelectedVector().get(1));
                    System.out.println("Row: " + selected);
                }
            }
        } */
        // CONTROLS - GO TO SHUFFLE        
        else if (e.getActionCommand().equals("Shuffle")) {
            //player.getStatus() = 2 if no song was stopped
            //player.getStatus() = 0 song currently playing
            //player.getStatus() = -1 initial value. Nothing is playing
            //player.getStatus() = 1 song paused
            
            randomNum = rand.nextInt(mTable.getSongsCount());
            Object filepathObj = mTable.getSongsTableObj().getValueAt(randomNum, 0);
            if (mMenuBar.isShuffleEnabled()) {            
                System.out.println("Shuffle songs on " + player.getStatus());
                if (player.getStatus() != 0) {
                    mTable.setSongPlayingIndex(randomNum);
                    playSong(mTable.getSongSelected((String)filepathObj));
                    mTable.scrollToSongPlaying();
                }
            } else {
                System.out.println("Shuffle songs off " + player.getStatus());
            }

        } // CONTROLS - GO TO REPEAT
        else if (e.getActionCommand().equals("Repeat")) {
            //mTable.setCurrentSongPlayingIndex(mTable.getSongSelectedIndex());                
            if (mMenuBar.isRepeatEnabled()) {
                System.out.println("Repeat song on");
                //playSong(mTable.getSongSelected());
            } else {
                System.out.println("Repeat song off");
            }
        } // DECREASE VOLUME
        else if (e.getActionCommand().equals("DecVol")) {
            System.out.println("Decrement volume");
            try {
                mSlider.decrementSlider();
                playerControl.setGain(mSlider.getValue());
            } catch (BasicPlayerException ex) {
                Logger.getLogger(NeptuneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // INCREASE VOLUME
        else if (e.getActionCommand().equals("IncVol")) {
            System.out.println("Volume increase");
            try {
                mSlider.incrementSlider();
                playerControl.setGain(mSlider.getValue());
            } catch (BasicPlayerException ex) {
                Logger.getLogger(NeptuneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getActionCommand().equals("historyItem")){
            JMenuItem historyItem = (JMenuItem) e.getSource();
            Vector<String> theData = (Vector<String>)historyItem.getClientProperty("data");
            playSong(theData);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (source == mMenuBar.getAddSongObj() || source == mTable.getMenuAddObj()) {
            System.out.println("The menu shiet works");
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(mMenuBar.getAddSongObj());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mDatabase.addSong(file.getAbsolutePath());

                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
        } else if (source == mMenuBar.getDeleteSongObj() || source == mTable.getMenuRemoveObj()) {
            System.out.println("Remove song from library.");
            String filePath = mTable.getSongSelectedFilepath();
            System.out.println("Filepath: " + filePath);
            if (isPlaylistView) {
                System.out.println("playlist delete");
                mDatabase.deleteSongFromPlaylist(mDatabase.getSongID(mTable.getSongSelectedFilepath()), mDatabase.getPlaylistIDfromName(mTable.getTableName()));
                mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(mTable.getTableName()));
            } else if (!isPlaylistView && filePath != null) {
                mDatabase.deleteSongFromLibrary(filePath);
                mTable.update(mDatabase, mDatabase.getSongsFromDatabase()); // update table after deleting
                System.out.println("Song deleted from library");
            } else {
                System.out.println("Unable to delete song from library");
            }
        } //
        else if (source == mMenuBar.getPlaylistObj() || source == mTable.getMenuAddToPlaylistObj()) {
            JMenu menuItem = (JMenu) e.getComponent();
            //System.out.println(menuItem.getName());

            String playlistName = JOptionPane.showInputDialog(mMenuBar.getMenu(), "Enter playlist name");

            int songID = mDatabase.getSongID(mTable.getSongSelectedFilepath());
            int playlistID = mDatabase.getPlaylistIDfromName(playlistName);
            System.out.println(songID + " " + playlistID);
            if (playlistID != 0 && songID != 0) {
                mDatabase.addSongToPlaylist(songID, playlistID);
                System.out.println("Song added to playlist");
            } else {
                System.out.println("Playlist doesn't exist");
            }
//        } else if (source == mTable.getTableObj()) {
//            isPaused = false;
//            System.out.println("The Jtable was clicked");
            // upon clicking on table, update popup menu with playlists
//            if (!isPlaylistView) {
//                mTable.updatePopupSubmenu(mTree.getLeafNodeNames(), mDatabase);
//            }

        } else if (source == mTable.getSongsTableObj()){
            //*    
            int row = mTable.getSongsTableObj().rowAtPoint(e.getPoint());
            //int next = row + 1;
            //int prev = row - 1;
            if (row >= 0){
                mTable.setSongSelected();
                int col = mTable.getSongsTableObj().columnAtPoint(e.getPoint());
                Object selectedObj = mTable.getSongsTableObj().getValueAt(row, col);
                Object filepathObj = mTable.getSongsTableObj().getValueAt(row, 0);
                mTable.setSongSelectedFilepath((String)filepathObj);
                mTable.scrollToSelectedSong();
                System.out.println("Selected object: " + selectedObj + "\nFilepath: " + filepathObj);
            }
            /*
            if (next >= mTable.getSongsCount()) {
                next = 0;
            }
            //mTable.setNextSongPlayingIndex(next);
            filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
            System.out.println("Next filepath: " + filepathObj);
            mTable.setNextSongFilepath((String)filepathObj);
            if (prev < 0) {
                prev = mTable.getSongsCount() - 1; //wrap around the index
            }
            filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
            System.out.println("Previous filepath: " + filepathObj);
            System.out.println("Selected object: " + selectedObj.toString());
            mTable.setPrevSongFilepath((String)filepathObj);
            System.out.println("Next: " + next + " Prev: " + prev);
            //*/
                System.out.println("The Jtable was clicked");
        }else if (source == mTree.getJTreeObj()) {
            String leafName = mTree.getSelectedLeafName();
            if (leafName.equals("Library")) {
                isPlaylistView = false;
                mTable.updatePopupSubmenu(mTree.getLeafNodeNames(), mDatabase);
                mTable.update(mDatabase, mDatabase.getSongsFromDatabase());
                neptune.setPlaylistMenuBar(isPlaylistView);
            } else if (!leafName.equals("Playlists")) {
                isPlaylistView = true;
                mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(leafName));
                neptune.setPlaylistMenuBar(isPlaylistView);
                mTable.setTableName(leafName);
            }

        } else if (source == mTree.getNewWindowObj()) { //formerly mTree.getTreeObj()
            
            int selRow = mTree.getJTreeObj().getRowForLocation(e.getX(), e.getY());
            //String name = mTree.getJTreeObj().getSelectionPath().getLastPathComponent().toString();
            String leafName = mTree.getSelectedLeafName();
            System.out.println("The tree was clicked.Selected row: " + selRow + " with name: " + leafName);
            // makes call to database to update column view on new playlist view 	 	
            String[] fields = {"showArtist", "showAlbum", "showYear", "showGenre", "showComments"}; 	 	
            for(int i=0;i<5;i++) { 	 	
                mDatabase.setPlayerSettings(fields[i], mTable.getChangedSettings()[i]); 	 	
            } 
            mTree.setLibraryFocus();
            RunMVC playlist = new RunMVC(true, leafName, this);
            isPlaylistView = false;
            mTable.update(mDatabase, mDatabase.getSongsFromDatabase());
            neptune.setPlaylistMenuBar(isPlaylistView);
//            if (e.getClickCount() == 2) {
//                if (!leafName.equals("Playlists")) {
//                    System.out.println("I am broken");
//                    RunMVC playlist = new RunMVC(true, leafName);
//                }
//            }
        } else if (source == mTree.getDeletePlaylistObj()) {
            String leafName = mTree.getSelectedLeafName();
            //TreePath path = mTree.getJTreeObj().set
            //int selRow = mTree.getJTreeObj().getRowForLocation(e.getX(), e.getY());
            TreePath path = mTree.getJTreeObj().getSelectionPath();
            System.out.println("path: " + path);
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "You are about to delete the " + leafName + " playlist.\nWant to continue with this?", "Warning", dialogButton);
            if (dialogResult == JOptionPane.YES_OPTION) {
                mDatabase.deletePlaylistFromDatabase(leafName);
                mTree.deleteNode(path);
                mTree.getJTreeObj().treeDidChange();
                mTable.update(mDatabase, mDatabase.getSongsFromDatabase()); // update jtable with library songs after deleting any playlist
            }
        }
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

    //*****************************************************************************
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        mTable.setBackground(Color.GREEN);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        mTable.setBackground(Color.white);
    }

    @Override
    public void drop(DropTargetDropEvent drop) {
        
        drop.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = drop.getTransferable();
        DataFlavor[] dataTypes = transferable.getTransferDataFlavors();
        mTable.setBackground(Color.white);
      for (DataFlavor type : dataTypes) {

            try {
                if (type.isFlavorJavaFileListType()) {
                    List<File> files = (List) transferable.getTransferData(type);
                    int playlistID = 0;
                    if (isPlaylistView) {
                        playlistID = mDatabase.getPlaylistIDfromName(mTable.getTableName());
                    }
                    for (File file : files) {
                        System.out.println(file.getAbsolutePath());
                        mDatabase.addSong(file.getAbsolutePath());
                        if (isPlaylistView) {
                            mDatabase.addSongToPlaylist(mDatabase.getSongID(file.getAbsolutePath()), playlistID);
                            mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(mTable.getTableName()));
                            if(parent != null){
                                parent.updateLibraryData();
                            }
                            
                        }
                    }
                }
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(SongsTableComponent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SongsTableComponent.class.getName()).log(Level.SEVERE, null, ex);

            }

        }
        System.out.println("Something was dropped here...is it yours?");
        
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //System.out.println("Player control");

        try {
            playerControl.setGain(mSlider.getValue());
        } catch (BasicPlayerException ex) {
            System.out.println("Nothing playing to be able to change volume");
            Logger.getLogger(NeptuneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void opened(Object o, Map map) {
        System.out.println(o.toString() +"\n" + map.toString());
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map properties) {

        mProgress.updateProgress((long)properties.get("mp3.position.microseconds"));

    }
    

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        if(bpe.toString().startsWith("EOM")){
            System.out.println("You've cat to be kitten me right meow. EOM");
            if (mMenuBar.isRepeatEnabled()){
                String filepath = mTable.getSongSelectedFilepath();
                mTable.setSongPlayingIndex(mTable.getSongSelected());
                playSong(mTable.getSongSelected(filepath));
                System.out.println("Song playing filepath: " + filepath);
                System.out.println("Playing: " + mTable.getSongSelected(filepath).get(1));
                mTable.scrollToSelectedSong();
            }else {
                
                int row = mTable.getCurrentSongPlayingIndex();
                int next = row + 1;
                int prev = row - 1; 
                if (next >= mTable.getSongsCount()) {
                    next = 0;
                }
                if (prev < 0) {
                    prev = mTable.getSongsCount() - 1; //wrap around the index
                }
                mTable.setSongPlayingIndex(next);
                if (mMenuBar.isShuffleEnabled()){
                    //CODE FOR NEXT RANDOM SONG HERE
                    //player.getStatus() = 2 if no song was stopped
                    //player.getStatus() = 0 song currently playing
                    //player.getStatus() = -1 initial value. Nothing is playing
                    //player.getStatus() = 1 song paused
                    Random rand = new Random();
                    int randomNum = rand.nextInt(mTable.getSongsCount());
                    row = randomNum;
                    next = row + 1;
                    prev = row - 1; 
                    if (next >= mTable.getSongsCount()) {
                        next = 0;
                    }
                    if (prev < 0) {
                        prev = mTable.getSongsCount() - 1; //wrap around the index
                    }
                    Object filepathObj = mTable.getSongsTableObj().getValueAt(randomNum, 0);
                    System.out.println("Shuffle songs on!\nStatus: " + player.getStatus() + " Random: " + randomNum);
                    if (player.getStatus() == 0) {
                        mTable.setSongPlayingIndex(randomNum);
                        playSong(mTable.getSongSelected((String)filepathObj));
                        mTable.scrollToSongPlaying();
                    }
                }else{
                    playSong(mTable.getSongSelected(mTable.getNextFilepath()));  
                }          
                mTable.scrollToSongPlaying();
                Object filepathObj = mTable.getSongsTableObj().getValueAt(0, 0);
                System.out.println("Filepath: " + filepathObj.toString());
                filepathObj = mTable.getSongsTableObj().getValueAt(next, 0);
                mTable.setNextSongFilepath(filepathObj.toString());
                System.out.println("Next filepath: " + filepathObj + "\nNext: " + next + " Row: " + row);
                filepathObj = mTable.getSongsTableObj().getValueAt(prev, 0);
                mTable.setPrevSongFilepath(filepathObj.toString());
                System.out.println("Previous filepath: " + filepathObj + " " + prev);
            }         
        }
    }

    @Override
    public void setController(BasicController bc) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
        //JOptionPane.showMessageDialog(e.getComponent(), "HAAAAAAAAAiiiiiii!!!!");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //The most recently played song get the index 0 and the older songs get a larger number
        JMenu history = mMenuBar.getSongsHistory();
        int historySize = history.getItemCount();
        JMenuItem item;
        for(int x = 0; x < historySize; x++){
            item = history.getItem(x);       
            mDatabase.storeSong((Vector<String>)item.getClientProperty("data"), x);
        }
        
        String[] fields = {"showArtist", "showAlbum", "showYear", "showGenre", "showComments"}; 	 	
        for(int i=0;i<5;i++) { 	 	
            mDatabase.setPlayerSettings(fields[i], mTable.getChangedSettings()[i]); 	 	
        }
        //JOptionPane.showMessageDialog(e.getComponent(), "Your data is the weakest link, goodbye");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
