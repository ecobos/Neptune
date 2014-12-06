
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class JProgressBarComponent {
    public JProgressBar progressBar;
    private JPanel mProgressPanel;
    private int progress; 
    private int seconds;
    private int minuteHolder;
    private String centerFormat;
    
    private JTextArea elapsedTime;
    private JTextArea remainingTime;
    
    public JProgressBarComponent(){
        mProgressPanel = new JPanel();
        mProgressPanel.setBackground(Color.DARK_GRAY);
        mProgressPanel.setForeground(Color.DARK_GRAY);
        
        elapsedTime = new JTextArea(4,4);
        elapsedTime.setFont(new Font("Monospaced", Font.PLAIN, 16));     
        elapsedTime.setBackground(Color.DARK_GRAY);
        elapsedTime.setForeground(Color.WHITE);
        elapsedTime.setEditable(false);
        
        remainingTime = new JTextArea(4,4);
        remainingTime.setFont(new Font("Monospaced", Font.PLAIN, 16));     
        remainingTime.setBackground(Color.DARK_GRAY);
        remainingTime.setForeground(Color.WHITE);
        remainingTime.setEditable(false);
        
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(400,7));
        progress = 0;
        minuteHolder = 0;
        centerFormat = ":";
        mProgressPanel.setMinimumSize(new Dimension(500,7));
        
        mProgressPanel.add(elapsedTime);
        mProgressPanel.add(progressBar);
        mProgressPanel.add(remainingTime);
        elapsedTime.setText(" 0:00");
        remainingTime.setText(" 0:00");
    }
    
    public void updateProgress(long newValue, String length){
        progress = (int)newValue/1000000;
        minuteHolder = progress / 60;
        
        seconds = progress % 60;        
        //System.out.println(length);
        int songLength = Integer.parseInt(length) - progress;
        int sLengthSeconds = songLength % 60;
        int sLengthMinute = songLength / 60;
        String sCenterFormat;
           
        // elapsed time
        if(seconds < 10){
            centerFormat = ":0";
        }else{
            centerFormat = ":";
        }
        
        // remaining time
        if(sLengthSeconds < 10){
            sCenterFormat = ":0";
        }else{ 
            
            sCenterFormat = ":";
        }
            
        elapsedTime.setText(" "+minuteHolder +centerFormat+ Integer.toString(seconds));
        remainingTime.setText(" "+sLengthMinute +sCenterFormat+ Integer.toString(sLengthSeconds));
        progressBar.setValue(progress); //convert microseconds to seconds
        if(Integer.parseInt(length) == progress) {
            progressBar.setValue(seconds);
        }
    }
    
    // parameter is in milliseconds
    public void setLength(int songLength){
        songLength /= 1000;
        progressBar.setMinimum(0);
        progressBar.setMaximum(songLength);
    }
    
    public JPanel getProgressPanel(){
        return mProgressPanel;
    }
}
