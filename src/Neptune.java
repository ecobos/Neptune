
//import com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Neptune {

    private JPanel mMainPanel, mContentPanel, mTreePanel;
    private GridBagConstraints mBounds;
    private SongsTableComponent mTable;
    private ButtonsComponent mButtons;
    private TextAreaComponent mSongInfo;
    private MenuComponent mMenuBar;
    private JTreeComponent mTree;
    private JSliderComponent mSlider;
    private JFrame mainFrame;
    private JProgressBarComponent mProgress;

    /**
     * Class constructor
     *
     * @param table Table's content
     * @param buttons Song control buttons
     * @param menu Frame's menu
     * @param text Text area content
     * @param slider Volume slider
     */
    public Neptune(SongsTableComponent table, ButtonsComponent buttons, MenuComponent menu, TextAreaComponent text, JSliderComponent slider, JProgressBarComponent progress) {
       
        mMainPanel = new JPanel(new BorderLayout());
        mContentPanel = new JPanel();
        mContentPanel.setLayout(new GridBagLayout());
        mTreePanel = new JPanel();
        mTreePanel.setLayout(new BorderLayout());
        mBounds = new GridBagConstraints();

        mTable = table;
        mButtons = buttons;
        mSongInfo = text;
        mMenuBar = menu;

        mSlider = slider;
        mProgress = progress;

        //mBounds.anchor = GridBagConstraints.WEST;
        //mBounds.gridheight = 1;
        mBounds.gridx = 0;
        mBounds.gridy = 0;
        mBounds.fill = GridBagConstraints.HORIZONTAL;
        mBounds.gridwidth = 1;
        mBounds.gridx = 1;
        mBounds.insets = new Insets(0, 0, 40, 0);
        mContentPanel.add(mButtons.getButtonsPanel(), mBounds);
        
<<<<<<< HEAD (4f8151e) - Fixed #16 and #26
        mBounds.insets = new Insets(140, 10, 0, 10);
=======
        mBounds.insets = new Insets(140, 0, 0, 0);
>>>>>>> origin/master (286b22e) - Create JProgre
        mContentPanel.add(mProgress.getProgressPanel(), mBounds);

        mBounds.gridx = 5;
        mBounds.gridy = 0;
        mBounds.insets = new Insets(10, 40, 10, 0);
        mContentPanel.add(mSongInfo.getTextArea(), mBounds);

        //mContentPanel.setBackground(Color.red);
        // mContentPanel.setMinimumSize(new Dimension(300,100));
        mContentPanel.add(mSlider.getSliderPanel());

        mBounds.anchor = GridBagConstraints.PAGE_END;
        mBounds.gridx = 0;
        mBounds.gridy = 1;
        mBounds.ipady = 20;
        mBounds.gridwidth = GridBagConstraints.REMAINDER;
        mBounds.insets = new Insets(0, 0, 0, 0);;
        mContentPanel.add(mTable.getTable(), mBounds);

        mContentPanel.setBackground(Color.DARK_GRAY);

        //JSplitPane split = new JSplitPane();
        //split.setLeftComponent(mTreePanel);
        //split.setRightComponent(mContentPanel);
        mainFrame = new JFrame(mTable.getTableName());
        
        // add window listener 
        mainFrame.addWindowListener( new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        showDialog(mainFrame);
                        //mMenuBar.getQuitObj();
                        //System.out.println("Quit was clicked.");
                        //String[] fields = {"showArtist", "showAlbum", "showYear", "showGenre", "showComments"}; 	 	
                        //for(int i=0;i<5;i++) {
                            //mTable.setPlayerSettings(, mTable.getChangedSettings()[i]); 
                            //mTable.setColSetting(fields[i], mTable.getChangedSettings());
                        //}
                        System.exit(0);
                    }
                } );
        
        //Closes program only when Library window is closed
        if(mTable.getTableName().matches("Library")){
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
        //mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        mainFrame.setJMenuBar(mMenuBar.getMenu());
        mainFrame.setMinimumSize(new Dimension(1330, 660));
        mainFrame.add(mContentPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Overloaded class constructor
     *
     * @param table table's contnet
     * @param buttons Song control buttons
     * @param menu Frame's menu
     * @param text Text area content
     * @param tree Playlist tree structure
     * @param slider Volume slider
     * @param progress
     */
    public Neptune(SongsTableComponent table, ButtonsComponent buttons, MenuComponent menu, TextAreaComponent text, JTreeComponent tree, JSliderComponent slider, JProgressBarComponent progress) {
        this(table, buttons, menu, text, slider, progress);
        mTree = tree;
        mTreePanel.setMinimumSize(new Dimension(100, 100));

        mTreePanel.add(mTree.getTreePanel());
        mainFrame.remove(mContentPanel);
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(mTreePanel);
        split.setRightComponent(mContentPanel);
        mainFrame.add(split);
        mainFrame.pack();
        mainFrame.setVisible(true);

    }

    public void destroyFrame() {
        mainFrame.dispose();
    }

    /**
     * Sets the menu based on the frame's context
     *
     * @param isPlaylist True means that its a playlist view
     */
    public void setPlaylistMenuBar(boolean isPlaylist) {
        mMenuBar.setPlaylistMenu(isPlaylist);
    }

    public JPanel getPanelObj() {
        return mContentPanel;
    }

    /**
     * Sets the song information text
     *
     * @param info Song information
     */
    public void setText(String info) {
        mSongInfo.setText(info);
    }

    public void setController(ActionListener controller) {
        mMenuBar.setController(controller);
        mButtons.setController(controller);

    }

    public void setMouseListener(MouseListener controller) {
        mTable.addMouseController(controller);
    }
    
//    public void setWindowLister() {
//        addWindowListener(new WindowAdapter()
//        {
//            public void windowClosing(WindowEvent e)
//            {
//                System.out.println("Clicked on Red X");
//                e.getWindow().dispose();
//            }
//        });
//    }

    public void setDropController(DropTargetListener controller) {
        mTable.addDropController(controller);
    }
    
    public static void showDialog(Component c) {
        JOptionPane.showMessageDialog(c, "Trying to save settings on clicking X... Not working yet");
    }
}
