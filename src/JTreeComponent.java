import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeComponent implements Observer {
    private Vector<String> mPlaylistVector; // vector used to store retrieved playlist names
    private JTree mPlaylistTree; 
    private DefaultMutableTreeNode playlistRoot; // child nodes are added to this root
    // the root is added to the tree
    // tree --> root --> children
    public JTreeComponent(Vector<String> playlistFromDatabase) {
        // create root node that will get passed in to create the tree
        playlistRoot = new DefaultMutableTreeNode("Playlists");
        // tree gets created using the playlistRoot node
        mPlaylistTree= new JTree(playlistRoot);
        // get the list of playlist from database
        mPlaylistVector = playlistFromDatabase; 
        // add the playlists in vector "mPlaylistVector" as children of the root "playlistRoot"
        for(int i=0; i<mPlaylistVector.size(); i++) {
            addNodeToTree(mPlaylistVector.get(i));
        }
    }
    
    public JTree getJTreeObj() {
        return mPlaylistTree;
    }
    
    // add child nodes to root
    public void addNodeToTree(String nodeToAdd) {
        // pass in string of node and create node with it
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToAdd); 
        playlistRoot.add(node);
    }
    
    // assume the node to be deleted already exist within playlist table
    // I'm not sure if this will work since it might be making a new copy of a 
    // a child node since I make a child node with the string passed in
    public void deleteNode(String nodeToDelete) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToDelete); 
        playlistRoot.remove(node);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
