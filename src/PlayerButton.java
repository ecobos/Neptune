
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class PlayerButton extends JButton{
 
    PlayerButton(int x, int y, String pathToImage) {
        Icon buttonIcon = new ImageIcon(this.getClass().getResource(pathToImage));
        setIcon(buttonIcon);
        setPreferredSize(new Dimension(x, y));
    }  
}
