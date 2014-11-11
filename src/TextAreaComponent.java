
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;


public class TextAreaComponent {
    private JTextArea mSongInfo;
    
    TextAreaComponent(){
        mSongInfo = new JTextArea(10, 45);
        mSongInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mSongInfo.setForeground(Color.DARK_GRAY);
        mSongInfo.setEditable(false);
    }
    
    public void setText(String newText){
        mSongInfo.setText(newText);
    }
    
    public JTextArea getTextArea(){
        return mSongInfo;
    }
}
