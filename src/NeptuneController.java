
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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class NeptuneController implements ActionListener, MouseListener, DropTargetListener, ChangeListener{
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
    
    
    NeptuneController(){
        player = new BasicPlayer();
        playerControl = (BasicController) player;
        isPaused = false;
    }
    public void addPlayerView(Neptune mp){
        this.neptune = mp;
    }
    public void addSlider(JSliderComponent slider) {
        this.mSlider = slider;
    }
    public void addDatabaseModel(Database conn){
        this.mDatabase = conn;
    }
    
    public void addButtonsView(ButtonsComponent buttons){
        this.mButtons = buttons;
    }
    
    public void addMenuView(MenuComponent menu){
        this.mMenuBar = menu;
    }
    
    public void addTextView(TextAreaComponent text){
        this.mSongInfo = text;
    }
    
    public void addTableModel(SongsTableComponent table){
        this.mTable = table;
    }
    
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
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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

    private void deleteSongSelected() {
        System.out.println("Deleted song: " + mTable.getSongSelected().get(2));
        mDatabase.deleteSong(mTable.getSongSelected());     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // QUIT BUTTON
        if (e.getSource() == mMenuBar.getQuitObj()) {
            System.out.println("Quit was clicked.");
            System.exit(0);
        } // ABOUT BUTTON
        else if (e.getSource() == mMenuBar.getAboutObj()) {
            System.out.println("Clicked the about section");
            String info = "Neptune Music Player\n\nDeveloped by:\n\t\tEdgar Cobos\n\t\tKelby Sapien\n\t\tGil Pena\n\nVersion: 1.0";
            JOptionPane.showMessageDialog(mMenuBar.getMenu(), info, "About", PLAIN_MESSAGE, new ImageIcon(this.getClass().getResource("/resources/neptune.png")));
        } // ADD SONG BUTTON
        else if (e.getSource() == mMenuBar.getAddSongObj() || e.getSource() == mButtons.getAddSongObj()) {
            this.addSong();
        } // DELETE SONG
        else if (e.getSource() == mMenuBar.getDeleteSongObj()) {
            this.deleteSongSelected();
        } // PLAY SONG NOT IN LIBRARY
        else if (e.getSource() == mMenuBar.getSongNotInLibObj()) {
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
        else if (e.getSource() == mButtons.getPlayObj()) { 
            mTable.setCurrentSongPlayingIndex(mTable.getSongSelectedIndex());
            playSong(mTable.getSongSelected());
            System.out.println("Playing: " + mTable.getSongSelected().get(1));
        } // STOP SONG BUTTON
        else if (e.getSource() == mButtons.getStopObj()) {
            try {
                playerControl.stop();
                isPaused = false;
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // PAUSE SONG BUTTON
        else if (e.getSource() == mButtons.getPauseObj()) {
            try {
                playerControl.pause();
                isPaused = true;
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Neptune.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // NEXT SONG BUTTON
        else if (e.getSource() == mButtons.getNextObj()) {
            
            playSong(mTable.getNextSong());
            
//            System.out.println("mLastSongPlayedIndex: " + mLastSongPlayedIndex + " song count: " + (mTable.getSongsCount() - 1));
//            mLastSongPlayedIndex++;
//            if (mLastSongPlayedIndex >= mTable.getSongsCount()) {
//
//                System.out.println("Last song playing");
//                mLastSongPlayedIndex = 0; // update the last song index within the player class
//                playSong(mTable.getSongSelected(mLastSongPlayedIndex));
//            } else {
//                playSong(mTable.getSongSelected(mLastSongPlayedIndex));
//            }
        } // PREVIOUS SONG BUTTON
        else if (e.getSource() == mButtons.getPrevObj()) {
            // this ensures that if the mouse is clicked to a different row, we 
            // can still play the song that is currently next
            playSong(mTable.getPrevSong());
            
//            mLastSongPlayedIndex--;
//            if (mLastSongPlayedIndex < 0) {
//                //System.out.println("First song playing");
//                mLastSongPlayedIndex = mTable.getSongsCount() - 1;
//                playSong(mTable.getSongSelected(mLastSongPlayedIndex));
//            } else {
//                playSong(mTable.getSongSelected(mLastSongPlayedIndex));
//            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == mMenuBar.getAddSongObj() || e.getSource() == mTable.getMenuAddObj()) {
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
        } else if (e.getSource() == mMenuBar.getDeleteSongObj() || e.getSource() == mTable.getMenuRemoveObj()) {
            deleteSongSelected();

        } else if (e.getSource() == mTable.getTableObj()) {
            isPaused = false;
            System.out.println("The Jtable was clicked");
            mTable.setSongSelected();
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
                    for (File file : files) {
                        System.out.println(file.getAbsolutePath());
                        mDatabase.addSong(file.getAbsolutePath());
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
        System.out.println("Player control");
        
        try {
            playerControl.setGain(mSlider.getValue());
        } catch (BasicPlayerException ex) {
            Logger.getLogger(NeptuneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
