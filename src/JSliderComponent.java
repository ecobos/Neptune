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
        mSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        mSlider.setMinorTickSpacing(5);
        mSlider.setMajorTickSpacing(25);
        mSlider.setPaintTicks(true);
        mSlider.setPaintLabels(true);
        mSlider.setBackground(Color.DARK_GRAY);
        mSlider.setForeground(Color.WHITE);
        mSlider.setSnapToTicks(true);
        
        // sets standard numeric labels 
        mSlider.setLabelTable(mSlider.createStandardLabels(25));
        mSliderPanel.setMinimumSize(new Dimension(300,100));
        mSliderPanel.add(mSlider);
    }
    
    
    
    public JSlider getSliderObj() {
        return mSlider;
    }
    
    public JPanel getSliderPanel() {
        return mSliderPanel;
    }
    
    public void incrementSlider(){
        int newValue = mSlider.getValue();
        if(newValue <= 95){
            newValue += 5;
            mSlider.setValue(newValue);
        }     
    }
    
    public void decrementSlider(){
        int newValue = mSlider.getValue();
        if(newValue >= 5){
            newValue -= 5;
            mSlider.setValue(newValue);
        } 
    }
    
    //Return a double between 0 and 1
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
