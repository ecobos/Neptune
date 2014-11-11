import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;


public class Neptune{

    private JPanel mMainPanel;
    private GridBagConstraints mBounds;
    private SongsTableComponent mTable;
    private ButtonsComponent mButtons;
    private TextAreaComponent mSongInfo;
    private MenuComponent mMenuBar;
    private JTreeComponent mTree;
    private JSliderComponent mSlider;
 
    public Neptune(SongsTableComponent table, ButtonsComponent buttons, MenuComponent menu, TextAreaComponent text, JTreeComponent tree, JSliderComponent slider) {
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new GridBagLayout());
        mBounds = new GridBagConstraints();
        
        mTable = table;
        mButtons = buttons;
        mSongInfo = text;
        mMenuBar = menu;
        mTree = tree;
        mSlider = slider;
        
        mMainPanel.add(mSlider.getSliderPanel());
        
        mBounds.fill = GridBagConstraints.VERTICAL;
        //mBounds.anchor = GridBagConstraints.WEST;
        //mBounds.gridheight = 1;
        mBounds.gridx = 0;
        mBounds.gridy = 0;
        mMainPanel.add(mTree.getTreePanel(), mBounds);
        
        //mBounds.anchor = GridBagConstraints.NONE;
        mBounds.fill = GridBagConstraints.HORIZONTAL;
        mBounds.gridwidth = 1;
        mBounds.gridx = 1;
        mMainPanel.add(mButtons.getButtonsPanel(), mBounds);

        mBounds.gridx = 5;
        mBounds.gridy = 0;
        mBounds.insets = new Insets(10, 40, 10, 0);
        mMainPanel.add(mSongInfo.getTextArea(), mBounds);

        mBounds.anchor = GridBagConstraints.PAGE_END;
        mBounds.gridx = 0;
        mBounds.gridy = 1;
        mBounds.ipady = 20;
        mBounds.gridwidth = GridBagConstraints.REMAINDER;
        mBounds.insets = new Insets(0, 0, 0, 0);;
        mMainPanel.add(mTable.getTable(), mBounds);

        mMainPanel.setBackground(Color.DARK_GRAY);

        JFrame frame = new JFrame("Neptune");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mMenuBar.getMenu());
        frame.setMinimumSize(new Dimension(1330, 660));
        frame.add(mMainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public JPanel getPanelObj(){
        return mMainPanel;
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
