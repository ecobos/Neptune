import java.awt.Color;
import java.awt.Dimension;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import maryb.player.Player;
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
    private Player player;
    
    public MusicPlayer() {
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new GridBagLayout());
        mBounds = new GridBagConstraints();
        player = new Player();
        createButtons();
        createSongsTable();

        JFrame frame = new JFrame("nexTunes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(new TheMenu().getMenu());
        frame.setMinimumSize(new Dimension(1050, 300));

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

        JTextArea textArea = new JTextArea(10, 25);
        textArea.setText("Current song Playing:\nShakira\nBrazil\nWorldCup\n2014");
        textArea.setEditable(false);

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
        mBounds.insets = new Insets(10, 40, 10, 0);;
        mMainPanel.add(textArea, mBounds);
        mBounds.insets = new Insets(0, 100, 90, 0);;
        mBounds.gridx = 6;
        mBounds.gridy = 0;
        mBounds.ipady = 0;
        mMainPanel.add(mAddButton, mBounds);
    }

    private void createSongsTable() {
        mSongs = new Library();
        //mSongs.readTags();
        mTablePanel = mSongs.createTable();
        mBounds.anchor = GridBagConstraints.PAGE_END;
        mBounds.gridx = 0;
        mBounds.gridy = 1;
        mBounds.ipady = 20;
        mBounds.gridwidth = GridBagConstraints.REMAINDER;
        mBounds.insets = new Insets(0, 0, 0, 0);;

        mMainPanel.add(mTablePanel, mBounds);
        mMainPanel.setBackground(Color.DARK_GRAY);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();

        //String filepath = "C:\\Users\\Kelby\\Desktop\\song.mp3";
        String filepath = "/Users/ecobos/Music/iTunes/iTunes Music/A Day To Remember/Homesick/08 Homesick.mp3";
        //FileInputStream songFile = new FileInputStream("C:\\Users\\Kelby\\Desktop\\song.mp3");
        player.setSourceLocation(filepath);
        
        
        //Handle open button action.
        if (e.getSource() == mAddButton) {
            int returnVal = fc.showOpenDialog(MusicPlayer.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToLibrary(file.getAbsolutePath());
               
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }

            //Handle save button action.
        }
        
        else if (e.getSource() == mPlayButton) {
            // Debugging playing
            System.out.println("inside play event");
            try {
                
                //FileInputStream songFile = new FileInputStream("C:\\Users\\Kelby\\Desktop\\song.mp3");
                //Player p = new Player(songFile);
                player.play();
                
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Unable to play song");
            }
            // debugging 
        }
        
        else if (e.getSource() == mStopButton) {
            player.stop();
        }
        
        else if (e.getSource() == mPauseButton) {
            player.pause();
        }
//        else if (e.getSource() == saveButton) {
//            int returnVal = fc.showSaveDialog(SwingFileChooserDemo.this);
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                File file = fc.getSelectedFile();
//                //This is where a real application would save the file.
//                log.append("Saving: " + file.getName() + "." + newline);
//            } else {
//                log.append("Save command cancelled by user." + newline);
//            }
//            log.setCaretPosition(log.getDocument().getLength());
//        }
    }

}
