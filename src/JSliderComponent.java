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
    
    public JSliderComponent() {
        mSliderPanel = new JPanel();
        mSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
        mSlider.setMinorTickSpacing(10);
        mSlider.setMajorTickSpacing(20);
        mSlider.setPaintTicks(true);
        mSlider.setPaintLabels(true);
        
        // sets standard numeric labels 
        mSlider.setLabelTable(mSlider.createStandardLabels(20));
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
