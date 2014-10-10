import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author ecobos
 */
public class Player extends JPanel implements ActionListener {

    //private JPanel tablePanel;

    private JPanel mMainPanel, mTablePanel;
    private GridBagConstraints mBounds;
    private Library mSongs;
    private JButton mAddButton, mPlayButton, mPauseButton, mPrevButton, mStopButton, mNextButton;

    public Player() {
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new GridBagLayout());
        mBounds = new GridBagConstraints();
        createButtons();
        createSongsTable();
    }

    public static void main(String args[]) {
        new Player().run();
    }

    public void run() {
        JFrame frame = new JFrame("nexTunes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(new TheMenu().getMenu());
        frame.setMinimumSize(new Dimension(1050, 300));

        frame.add(mMainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void createButtons() {
        Icon playIcon = new ImageIcon(this.getClass().getResource("/resources/play.png"));
        mPlayButton = new JButton(playIcon);
        mPlayButton.setPreferredSize(new Dimension(95, 95));

        Icon prevIcon = new ImageIcon(this.getClass().getResource("/resources/prev.png"));
        mPrevButton = new JButton(prevIcon);
        mPrevButton.setPreferredSize(new Dimension(80, 80));

        Icon pauseIcon = new ImageIcon(this.getClass().getResource("/resources/pause.png"));
        mPauseButton = new JButton(pauseIcon);
        mPauseButton.setPreferredSize(new Dimension(80, 80));
        mPauseButton.createToolTip().setTipText("add a song");

        Icon stopIcon = new ImageIcon(this.getClass().getResource("/resources/stop.png"));
        mStopButton = new JButton(stopIcon);
        mStopButton.setPreferredSize(new Dimension(80, 80));

        Icon nextIcon = new ImageIcon(this.getClass().getResource("/resources/next.png"));
        mNextButton = new JButton(nextIcon);
        mNextButton.setPreferredSize(new Dimension(80, 80));

        JTextArea textArea = new JTextArea(10, 25);
        textArea.setText("Current song Playing:\nShakira\nBrazil\nWorldCup\n2014");
        textArea.setEditable(false);

        Icon addIcon = new ImageIcon(this.getClass().getResource("/resources/add.png"));
        mAddButton = new JButton(addIcon);
        mAddButton.setPreferredSize(new Dimension(80, 80));
        mAddButton.addActionListener(this);

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
        //Handle open button action.
        if (e.getSource() == mAddButton) {
            int returnVal = fc.showOpenDialog(Player.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mSongs.addSongToLibrary(file.getAbsolutePath());
               
                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }

            //Handle save button action.
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
