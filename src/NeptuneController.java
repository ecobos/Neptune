
import java.awt.Color;
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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
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

public class NeptuneController implements ActionListener, MouseListener, DropTargetListener, ChangeListener, BasicPlayerListener {

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
    private JTreeComponent mTree;
    private boolean isPlaylistView;

    /**
     * Class constructor.
     *
     * @param isPlaylistview True means that its a playlist view
     */
    NeptuneController(boolean isPlaylistview) {
        player = new BasicPlayer();
        player.addBasicPlayerListener(this);
        playerControl = (BasicController) player;
        isPaused = false;
        isPlaylistView = isPlaylistview;
        
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
    }

    public void addTextView(TextAreaComponent text) {
        this.mSongInfo = text;
    }

    public void addTableModel(SongsTableComponent table) {
        this.mTable = table;
        mTable.updatePopupSubmenu(mTree.getLeafNodeNames(), mDatabase);
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
                playerControl.open(new File(songToPlay.get(0)));
                mSongInfo.setText("\n\n Current song playing:\n\tArtist: " + songToPlay.get(2)
                        + "\n\tSong: " + songToPlay.get(1) + "\n\tAlbum: "
                        + songToPlay.get(3) + "\n\tSong " + (mTable.getCurrentSongPlayingIndex() + 1) + " of " + mTable.getSongsCount());
                playerControl.play();

            } catch (BasicPlayerException ex) {
                JOptionPane.showMessageDialog(mTable.getMenuAddObj(), "Song does not exist!");
            }
        }
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
        System.out.println("Deleted song: " + mTable.getSongSelected().get(2));
        mDatabase.deleteSong(mTable.getSongSelected());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Random rand = new Random();
        String filepath = mTable.getSongSelectedFilepath();
        int randomNum = rand.nextInt(mTable.getSongsCount());
        // QUIT BUTTON
        if (source == mMenuBar.getQuitObj()) {
            System.out.println("Quit was clicked.");
            neptune.destroyFrame();
            //System.exit(0);
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
            System.out.println("playlist delete");
            mDatabase.deleteSongFromPlaylist(mTable.getSongSelectedID(), mDatabase.getPlaylistIDfromName(mTable.getTableName()));
            mTable.update(mDatabase, mDatabase.getPlaylistSongsFromDatabase(mTable.getTableName()));
        } // PLAY SONG NOT IN LIBRARY
        else if (source == mMenuBar.getSongNotInLibObj()) {
            FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(mMenuBar.getSongNotInLibObj());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //mSongs.addSongToLocalTable(file.getAbsolutePath());

                try {
                    playerControl.open(file);
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
        else if (source == mButtons.getPlayObj()) {
            mTable.setCurrentSongPlayingIndex(mTable.getSongSelectedIndex());
            playSong(mTable.getSongSelected(filepath));
            System.out.println("Song playing filepath: " + filepath);
            System.out.println("Playing: " + mTable.getSongSelected(filepath).get(1));
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
        else if (source == mButtons.getNextObj() || e.getActionCommand().equals("Ctrl+Right Arrow")) {
            playSong(mTable.getNextSong());
        } // PREVIOUS SONG BUTTON
        else if (source == mButtons.getPrevObj() || e.getActionCommand().equals("Ctrl+Left Arrow")) {
            // this ensures that if the mouse is clicked to a different row, we 
            // can still play the song that is currently next
            playSong(mTable.getPrevSong());
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
        else if (source == mMenuBar.getGoToCurrentControlObj()) {
            mTable.setSelectionInterval(mTable.getCurrentSongPlayingIndex());
            mTable.setScrollBarPosition(mTable.getCurrentSongPlayingIndex()); // NOT WORKING
            //mTable.update(mDatabase, source);
        } // CONTROLS - PLAY SONG
        else if (source == mMenuBar.getPlayControlObj() || e.getActionCommand().equals("Space")) {
            if (mMenuBar.isShuffleEnabled()) {
                System.out.println("Shuffle songs on " + player.getStatus());
                if (player.getStatus() == -1 || player.getStatus() == 2) {
                    playSong(mTable.getSongSelected(randomNum));
                }
            } else {
                if (mTable.getSongSelected() == null) {
                    playSong(mTable.getSongSelected(0));
                } else {
                    mTable.setCurrentSongPlayingIndex(mTable.getSongSelectedIndex());
                    playSong(mTable.getSongSelected(filepath));
                    System.out.println("Playing: " + mTable.getSongSelected(filepath).get(1));
                }
            }
            //mTable.update(mDatabase, source);
        } // CONTROLS - GO TO NEXT SONG
        else if (source == mMenuBar.getNextControlObj() || e.getActionCommand().equals("RightArrow")) {
            playSong(mTable.getNextSong());
        } // CONTROLS - GO TO PREV SONG
        else if (source == mMenuBar.getPrevControlObj() || e.getActionCommand().equals("LeftArrow")) {
            playSong(mTable.getPrevSong());
        } // CONTROLS - GO TO SHUFFLE
        else if (e.getActionCommand().equals("Shuffle")) {
            //player.getStatus() = 2 if no song was stopped
            //player.getStatus() = 0 song currently playing
            //player.getStatus() = -1 initial value. Nothing is playing
            //player.getStatus() = 1 song paused
            
            randomNum = rand.nextInt(mTable.getSongsCount());
            if (mMenuBar.isShuffleEnabled()) {            
                System.out.println("Shuffle songs on " + player.getStatus());
                if (player.getStatus() != 0) {
                    playSong(mTable.getSongSelected(randomNum));
                }
            } else {
                System.out.println("Shuffle songs off " + player.getStatus());
            }/*
             if(mTable.getSongSelected() == null) {
             playSong(mTable.getSongSelected(randomNum));
             }
             else {
             mTable.setCurrentSongPlayingIndex(mTable.getSongSelectedIndex());
             playSong(mTable.getSongSelected());
             }
             playSong(mTable.getSongSelected(randomNum));*/

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
        else{}

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
        } else if (source == mTable.getTableObj()) {
            isPaused = false;
            System.out.println("The Jtable was clicked");
            mTable.setSongSelected();
            // upon clicking on table, update popup menu with playlists
//            if (!isPlaylistView) {
//                mTable.updatePopupSubmenu(mTree.getLeafNodeNames(), mDatabase);
//            }

        } else if (source == mTree.getJTreeObj()) {
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
            RunMVC playlist = new RunMVC(true, leafName);
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
        }
    }

    @Override
    public void opened(Object o, Map map) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map properties) {
        System.out.println("Progress: "+ l);
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setController(BasicController bc) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }
}
