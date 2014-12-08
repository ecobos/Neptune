
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
    private int eSeconds;
    private int eMinutes;
    private int songLength;
    private int rSeconds;
    private int rMinutes;
    private int eHours;
    private int rHours;
    private String eMinSecFormat;
    private String rMinSecFormat;
    private String eHourMinFormat;
    private String rHourMinFormat;
    private JTextArea elapsedTime;
    private JTextArea remainingTime;

    public JProgressBarComponent() {
        mProgressPanel = new JPanel();
        mProgressPanel.setBackground(Color.DARK_GRAY);
        mProgressPanel.setForeground(Color.DARK_GRAY);

        elapsedTime = new JTextArea(1, 4);
        elapsedTime.setFont(new Font("Monospaced", Font.PLAIN, 16));
        elapsedTime.setBackground(Color.DARK_GRAY);
        elapsedTime.setForeground(Color.WHITE);
        elapsedTime.setEditable(false);

        remainingTime = new JTextArea(1, 4);
        remainingTime.setFont(new Font("Monospaced", Font.PLAIN, 16));
        remainingTime.setBackground(Color.DARK_GRAY);
        remainingTime.setForeground(Color.WHITE);
        remainingTime.setEditable(false);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(400, 10));
        progress = 0;
        eMinutes = 0;
        eMinSecFormat = ":";
        mProgressPanel.setMinimumSize(new Dimension(500, 10));

        mProgressPanel.add(elapsedTime);
        mProgressPanel.add(progressBar);
        mProgressPanel.add(remainingTime);
        elapsedTime.setText("0:00:00");
        remainingTime.setText("0:00:00");
    }

    /**
     * This method gets hit several times per second while the song is playing
     * so if possible, we want to avoid declaring any local variables within the
     * method to prevent any performance issues.
     *
     * @param newValue
     * @param length
     */
    public void updateProgress(long newValue) {
        progress = (int) newValue / 1000000;
        eMinutes = progress / 60;
        eSeconds = progress % 60;
        eHours = progress / 3600;

        rMinutes = songLength - progress;
        rHours = rMinutes / 3600;
        rSeconds = rMinutes % 60;
        rMinutes = rMinutes / 60;

        eMinSecFormat = (eSeconds < 10) ? ":0" : ":";
        rMinSecFormat = (rSeconds < 10) ? ":0" : ":";
        eHourMinFormat = (eMinutes < 10) ? ":0" : ":";
        rHourMinFormat = (rMinutes < 10) ? ":0" : ":";

        elapsedTime.setText(eHours + eHourMinFormat + eMinutes + eMinSecFormat + eSeconds);
        remainingTime.setText(rHours + rHourMinFormat + rMinutes + rMinSecFormat + rSeconds);
        progressBar.setValue(progress); //convert microseconds to seconds

        //What does this code do? 
//        if(Integer.parseInt(length) == progress) {
//            progressBar.setValue(seconds);
//        }
    }

    // parameter is in milliseconds
    public void setLength(int aLength) {
        progressBar.setMinimum(0);
        progressBar.setMaximum(aLength);
        songLength = aLength;
    }

    public JPanel getProgressPanel() {
        return mProgressPanel;
    }
}
