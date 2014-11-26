import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSliderComponent implements Observer{
    private JPanel mSliderPanel;
    private JSlider mSlider; 
    static final double volumeRate = 100;
    
    /**
     * Class constructor. 
     */
    public JSliderComponent() {
        mSliderPanel = new JPanel();
        mSliderPanel.setBackground(Color.DARK_GRAY);
        mSliderPanel.setForeground(Color.DARK_GRAY);
        mSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        mSlider.setMinorTickSpacing(10);
        mSlider.setMajorTickSpacing(20);
        mSlider.setPaintTicks(true);
        mSlider.setPaintLabels(true);
        mSlider.setBackground(Color.DARK_GRAY);
        mSlider.setForeground(Color.WHITE);
        
        // sets standard numeric labels 
        mSlider.setLabelTable(mSlider.createStandardLabels(20));
        mSliderPanel.setMinimumSize(new Dimension(300,100));
        mSliderPanel.add(mSlider);
    }
    
    public JSlider getSliderObj() {
        return mSlider;
    }
    
    public JPanel getSliderPanel() {
        return mSliderPanel;
    }
    
    public double getValue() {
        return (mSlider.getValue()/volumeRate);
    }
    
    public void addMouseController(ChangeListener e) {
        mSlider.addChangeListener(e);
    }
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
