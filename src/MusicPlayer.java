import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import maryb.player.Player;
import maryb.player.decoder.MP3Decoder;
import maryb.player.PlayerState;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.decoder.Control;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.decoder;

//import javazoom.jl.player.Player;


/**
 *
 * @author ecobos
 */
public class MusicPlayer extends JPanel implements ActionListener {

    //private JPanel tablePanel;

    private JPanel mMainPanel, mTablePanel;
    private GridBagConstraints mBounds;
    private Library mSongs;
    private PlayerButton mAddButton, mPlayButton, mPauseButton, mPrevButton, mStopButton, mNextButton;
    private Player mPlayer;
    private JTextArea mTextArea;
    private TheMenu mMenuBar; 
    private int mLastSongPlayedIndex; // saves the last song played
    
    public MusicPlayer() {
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new GridBagLayout());
        mBounds = new GridBagConstraints();
        mPlayer = new Player();
        mSongs = new Library();
        createButtons();
        createSongsTable();

        JFrame frame = new JFrame("Neptune");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mMenuBar = new TheMenu();
        mMenuBar.getAddSongItem().addActionListener(this);
        mMenuBar.getDeleteSongItem().addActionListener(this);
        mMenuBar.getPlaySongNotInLibrary().addActionListener(this);
        frame.setJMenuBar(mMenuBar.getMenu());
        frame.setMinimumSize(new Dimension(1330, 660));
        frame.add(mMainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        new MusicPlayer();
    }

    private void createButtons() {
        mPlayButton = new PlayerButton(95, 95, "/resources/play.png");
        mPlayButton.addActionListener(this);
        mPrevButton = new PlayerButton(80, 80, "/resources/prev.png");
        mPrevButton.addActionListener(this);
        mPauseButton = new PlayerButton(80, 80, "/resources/pause.png");
        mPauseButton.addActionListener(this);
        mStopButton = new PlayerButton(80, 80, "/resources/stop.png");
        mStopButton.addActionListener(this);
        mNextButton = new PlayerButton(80, 80, "/resources/next.png");
        mNextButton.addActionListener(this);
        mAddButton = new PlayerButton(80, 80, "/resources/add.png");
        mAddButton.addActionListener(this);

        mTextArea = new JTextArea(10, 45);
        mTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mTextArea.setForeground(Color.ORANGE);
        mTextArea.setEditable(false);

        mBounds.fill = GridBagConstraints.HORIZONTAL;
        mBounds.gridwidth = 1;
        mBounds.insets = new Insets(0, 5, 0, 5);;
        mBounds.gridx = 0;
        mBounds.gridy = 0;
        //c.anchor = GridBagConstraints.PAGE_END;
        mMainPanel.add(mPrevButton, mBounds);
        mBounds.gridx = 1;
        mBounds.gridy = 0;
        mMainPanel.add(mPauseButton, mBounds);
        mBounds.gridx = 2;
        mBounds.gridy = 0;
        mMainPanel.add(mPlayButton, mBounds);
        mBounds.gridx = 3;
        mBounds.gridy = 0;
        mMainPanel.add(mStopButton, mBounds);
        mBounds.gridx = 4;
        mBounds.gridy = 0;
        mMainPanel.add(mNextButton, mBounds);
        mBounds.gridx = 5;
        mBounds.gridy = 0;
        mBounds.insets = new Insets(10, 40, 10, 0);
        mMainPanel.add(mTextArea, mBounds);
        mBounds.insets = new Insets(0, 100, 90, 0);
        mBounds.gridx = 6;
        mBounds.gridy = 0;
        mBounds.ipady = 0;
        mMainPanel.add(mAddButton, mBounds);
    }

    private void createSongsTable() {     
        mTablePanel = mSongs.getTable();
        mBounds.anchor = GridBagConstraints.PAGE_END;
        mBounds.gridx = 0;
        mBounds.gridy = 1;
        mBounds.ipady = 20;
        mBounds.gridwidth = GridBagConstraints.REMAINDER;
        mBounds.insets = new Insets(0, 0, 0, 0);;

        mMainPanel.add(mTablePanel, mBounds);
        mMainPanel.setBackground(Color.DARK_GRAY);
    }
    public void playSong(){
        playSong(mSongs.getCurrentSongSelected());
    }
    
    private void playSong(Vector<String> songToPlay){
//        if(songToPlay.equals(mPlayer.getSourceLocation())){
//            //do nothing
//        }
        
        if(mPlayer.getState() == PlayerState.PLAYING){
            System.out.println("Stopping player");
            //mPlayer.stop();
        } 
        
        
        mPlayer.setSourceLocation(songToPlay.get(0));
        mTextArea.setText("\n\n Current song playing:\n\tArtist: " + songToPlay.get(1) 
                        + "\n\tSong: "+ songToPlay.get(2) + "\n\tAlbum: " 
                        + songToPlay.get(3) + "\n\tSong " +  (mLastSongPlayedIndex + 1) + " of " + mSongs.getSongsCount());
        
        // still need to check this
        // a while loop might be needed to keep on checking for isEndOfMediaReached()
        // might mean lots of overhead
        mPlayer.play();
        
        if(mPlayer.getState() == PlayerState.PAUSED_BUFFERING){
            System.out.println("Song is buffering, please wait...");
        }
//        try {
//            
//        }
//        catch(Exception e)
//        {
//            if(mPlayer.isEndOfMediaReached()) {
//                playSong(mSongs.getNextSong());
//            }
//        }
        
        // avoids throwing exception if song reaches end
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //Handle open button action.
        if (e.getSource() == mAddButton || e.getSource() == mMenuBar.getAddSongItem()) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(MusicPlayer.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToDatabase(file.getAbsolutePath());
                //mTablePanel = mSongs.getTable();
                
                
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }

            //Handle save button action.
        }
        //else if(e.getSource() == )
        
        else if(e.getSource() == mMenuBar.getDeleteSongItem()) {
            mSongs.deleteSong(mSongs.getCurrentSongSelected());
            mTablePanel = mSongs.getTable();
            //this.createSongsTable();
            System.out.println("Deleted song: " + mSongs.getCurrentSongSelected().get(2));
        }
        else if(e.getSource() == mMenuBar.getPlaySongNotInLibrary()) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(MusicPlayer.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToLocalTable(file.getAbsolutePath());
                System.out.println("Adding song with filepath: " + file.getAbsolutePath() + " to library.");
                //mTablePanel = mSongs.getTable();
                
                
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
        }
        
        else if (e.getSource() == mPlayButton) {
            // Debugging playing
            mLastSongPlayedIndex = mSongs.getCurrentSongSelectedIndex(); // saves the last song played
            playSong(mSongs.getCurrentSongSelected());
            System.out.println("Playing: " +  mSongs.getCurrentSongSelected().get(1));
            /*
            try {
                
                //FileInputStream songFile = new FileInputStream("C:\\Users\\Kelby\\Desktop\\song.mp3");
                //Player p = new Player(songFile);
                playSong(mSongs.getCurrentSongSelected());
               
                
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Unable to play song");
            }
            */
            // debugging 
        } else if (e.getSource() == mStopButton) {
            mPlayer.stop();
        } else if (e.getSource() == mPauseButton) {
            mPlayer.pause();
        } else if(e.getSource() == mNextButton) {
            // this ensures that if the mouse is clicked to a different row, we 
            // can still play the song that is currently next
            System.out.println("mLastSongPlayedIndex: " + mLastSongPlayedIndex + " song count: " + (mSongs.getSongsCount()-1));
            if(mLastSongPlayedIndex == mSongs.getSongsCount()-1) {
                
                System.out.println("Last song playing");
                mLastSongPlayedIndex = 0; // update the last song index within the player class
                playSong(mSongs.getCurrentSongSelected(mLastSongPlayedIndex++));
            }
            
            else if(mPlayer.getState() == PlayerState.PLAYING || mPlayer.getState() == PlayerState.PAUSED) { 
                mPlayer.stop();
                System.out.println("mLastSongPlayedIndex: " + mLastSongPlayedIndex);
                playSong(mSongs.getCurrentSongSelected(++mLastSongPlayedIndex)); // updates first, then uses the value
            }
            else { // already at the stopped state, so just play from here
                //playSong(mSongs.getNextSong();
                playSong(mSongs.getCurrentSongSelected(mLastSongPlayedIndex));
            }
            
            
        }
        else if (e.getSource() == mPrevButton){
            // this ensures that if the mouse is clicked to a different row, we 
            // can still play the song that is currently next
            if(mLastSongPlayedIndex == 0) {
                System.out.println("First song playing");
                mLastSongPlayedIndex = mSongs.getSongsCount() - 1;
                playSong(mSongs.getCurrentSongSelected(mLastSongPlayedIndex--));
            }
            
            else if(mPlayer.getState() == PlayerState.PLAYING || mPlayer.getState() == PlayerState.PAUSED) {
                mPlayer.stop();
                System.out.println("mLastSongPlayedIndex: " + mLastSongPlayedIndex);
                playSong(mSongs.getCurrentSongSelected(--mLastSongPlayedIndex)); // updates first, then uses the value
            }
            else {
                //playSong(mSongs.getPrevSong());
                playSong(mSongs.getCurrentSongSelected(mLastSongPlayedIndex));
            }
            
        }
        else if (e.getSource() == mMainPanel && mSongs.getDoubleClick()){
            mLastSongPlayedIndex = mSongs.getCurrentSongSelectedIndex(); // saves the last song played
            playSong(mSongs.getCurrentSongSelected());
            System.out.println("Playing: " +  mSongs.getCurrentSongSelected().get(1));
        }
        
    }

}
