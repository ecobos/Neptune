import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;


public class Neptune{

    private JPanel mMainPanel,mContentPanel, mTreePanel;
    private GridBagConstraints mBounds;
    private SongsTableComponent mTable;
    private ButtonsComponent mButtons;
    private TextAreaComponent mSongInfo;
    private MenuComponent mMenuBar;
    private JTreeComponent mTree;
    private JSliderComponent mSlider;
    private JFrame mainFrame;
 
    public Neptune(SongsTableComponent table, ButtonsComponent buttons, MenuComponent menu, TextAreaComponent text, JSliderComponent slider) {
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
        
        //mBounds.anchor = GridBagConstraints.WEST;
        //mBounds.gridheight = 1;
        mBounds.gridx = 0;
        mBounds.gridy = 0;
        mBounds.fill = GridBagConstraints.HORIZONTAL;
        mBounds.gridwidth = 1;
        mBounds.gridx = 1;
        mContentPanel.add(mButtons.getButtonsPanel(), mBounds);

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
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        mainFrame.setJMenuBar(mMenuBar.getMenu());
        mainFrame.setMinimumSize(new Dimension(1330, 660));
        mainFrame.add(mContentPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    public Neptune(SongsTableComponent table, ButtonsComponent buttons, MenuComponent menu, TextAreaComponent text, JTreeComponent tree, JSliderComponent slider){
        this(table, buttons, menu, text, slider);
        
        mTree = tree;
        mTreePanel.setMinimumSize(new Dimension(100,100));
  
        mTreePanel.add(mTree.getTreePanel());
        mainFrame.remove(mContentPanel);
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(mTreePanel);
        split.setRightComponent(mContentPanel);
        mainFrame.add(split);
        mainFrame.pack();
        mainFrame.setVisible(true);
        
    }
    public void destroyFrame(){
        mainFrame.dispose();
    }
    
    public void setPlaylistMenuBar(boolean isPlaylist){
        mMenuBar.setPlaylistMenu(isPlaylist);
    }
    
    public JPanel getPanelObj(){
        return mContentPanel;
    }

    public void setText(String info){
        mSongInfo.setText(info);
    }
    
    public void setController(ActionListener controller){
        mMenuBar.setController(controller);
        mButtons.setController(controller);
        
    }
    
    public void setMouseListener(MouseListener controller){
        mTable.addMouseController(controller);
    }
    
    public void setDropController(DropTargetListener controller){
        mTable.addDropController(controller);
    }
    
   

    

}
