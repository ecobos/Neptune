import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
//import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


public class ButtonsComponent /*implements Observer*/{
    private final JButton mAddButton, mPlayButton, mPauseButton, mPrevButton, mStopButton, mNextButton;
    private JPanel mButtonsPanel;
    /**
     * Class constructor 
     */
    ButtonsComponent() {
        mButtonsPanel = new JPanel();
        mButtonsPanel.setLayout(new FlowLayout());
        
//        mButtonsPanel.setSize(width, height);
//        FlowLayout layout = new FlowLayout();
//        layout.setHgap(10);              
//        layout.setVgap(10);
//        mButtonsPanel.setLayout(layout);
        
        mAddButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/add.png")));
        mAddButton.setPreferredSize(new Dimension(80, 80));
        
        mPlayButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/play.png")));
        mPlayButton.setPreferredSize(new Dimension(95, 95));
        
        mPauseButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/pause.png")));
        mPauseButton.setPreferredSize(new Dimension(80, 80));
        
        mPrevButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/prev.png")));
        mPrevButton.setPreferredSize(new Dimension(80, 80));
        
        mStopButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/stop.png")));
        mStopButton.setPreferredSize(new Dimension(80, 80));
        
        mNextButton = new JButton(new ImageIcon(this.getClass().getResource("/resources/next.png")));
        mNextButton.setPreferredSize(new Dimension(80, 80));
        
        mButtonsPanel.add(mAddButton);
        mButtonsPanel.add(mPlayButton);
        mButtonsPanel.add(mPauseButton);
        mButtonsPanel.add(mPrevButton);
        mButtonsPanel.add(mStopButton);
        mButtonsPanel.add(mNextButton);
    }  
    
    public JButton getAddSongObj(){
        return mAddButton;
    }
    public JButton getPlayObj(){
        return mPlayButton;
    }
    public JButton getPauseObj(){
        return mPauseButton;
    }
    public JButton getPrevObj(){
        return mPrevButton;
    }
    public JButton getStopObj(){
        return mStopButton;
    }
    public JButton getNextObj(){
        return mNextButton;
    }
    
    public JPanel getButtonsPanel(){
        return mButtonsPanel;
    }
    
    /**
     * Links this view with a controller. Adds action events to the buttons which are then 
     * handled by the controller. 
     * 
     * @param controller The controlling class
     */
    public void setController(ActionListener controller){  
        mAddButton.addActionListener(controller);
        mPlayButton.addActionListener(controller);
        mPauseButton.addActionListener(controller);
        mPrevButton.addActionListener(controller);
        mStopButton.addActionListener(controller);
        mNextButton.addActionListener(controller);
    }

}
