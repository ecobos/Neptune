
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextArea;


public class TextAreaComponent {
    private JTextArea mSongInfo;
    
    /**
     * Class constructor. Creates a JTextArea and its formatting
     */
    TextAreaComponent(){
        mSongInfo = new JTextArea(10, 45);
        mSongInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mSongInfo.setForeground(Color.WHITE);
        mSongInfo.setBackground(Color.BLACK);
        mSongInfo.setMinimumSize(new Dimension(300,250));
        mSongInfo.setEditable(false);
    }
    
    /**
     * Sets the text for the JTextArea object
     * 
     * @param newText The new text
     */
    public void setText(String newText){
        mSongInfo.setText(newText);
    }
    
    public JTextArea getTextArea(){
        return mSongInfo;
    }
}
