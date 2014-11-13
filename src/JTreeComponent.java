import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.*;

public class JTreeComponent implements Observer {
    private Vector<String> mPlaylistVector; // vector used to store retrieved playlist names
    private JTree mPlaylistTree; 
    private DefaultMutableTreeNode playlistRoot; // child nodes are added to this root
    private DefaultMutableTreeNode playlist;
    private DefaultTreeModel playlistModel;
    private JPanel mTreePanel;
    private int treeIndex;
    private JMenuItem mNewWindow;
    private JMenuItem mDeletePlaylist;
// the root is added to the tree
    // tree --> root --> children
    public JTreeComponent(Vector<String> playlistFromDatabase) {
        // create root node that will get passed in to create the tree      
        // tree gets created using the playlistRoot node
        
        playlistRoot = new DefaultMutableTreeNode("Root");
        playlistModel = new DefaultTreeModel(playlistRoot);
        playlistModel.insertNodeInto(new DefaultMutableTreeNode("Library"), playlistRoot,0);
        playlist = new DefaultMutableTreeNode("Playlists");
        playlistModel.insertNodeInto(playlist, playlistRoot, 1);
        // get the list of playlist from database
        mPlaylistVector = playlistFromDatabase; 
        // add the playlists in vector "mPlaylistVector" as children of the root "playlistRoot"
        
        treeIndex = 0;
        for(int i=0; i<mPlaylistVector.size(); i++) {
            addNodeToTree(mPlaylistVector.get(i));
        }
        
        
        mPlaylistTree= new JTree(playlistModel);
        mPlaylistTree.setRootVisible(false);
        mPlaylistTree.setBackground(Color.DARK_GRAY);
        mPlaylistTree.setForeground(Color.DARK_GRAY);
        mPlaylistTree.setComponentPopupMenu(getPopupMenu());
        mTreePanel = new JPanel();
        mTreePanel.setBackground(Color.DARK_GRAY);
        mTreePanel.add(mPlaylistTree);
    }
    private JPopupMenu getPopupMenu() {
        JPopupMenu mPopupMenu = new JPopupMenu();
        mNewWindow = new JMenuItem("Open in new window");
        mDeletePlaylist = new JMenuItem("Delete playlist...");
        mPopupMenu.add(mNewWindow);
        mPopupMenu.add(mDeletePlaylist);
        return mPopupMenu;
    }
     
    public JMenuItem getNewWindowObj(){
        return mNewWindow;
    } 
    
    public JMenuItem getDeletePlaylistObj(){
        return mDeletePlaylist;
    }
    
    public JTree getJTreeObj() {
        return mPlaylistTree;
    }
    public DefaultTreeModel getTreeModelObj(){
        return playlistModel;
    }
    
    public String getSelectedLeafName(){
        return mPlaylistTree.getSelectionPath().getLastPathComponent().toString();
    }
    
    public JPanel getTreePanel(){
        return mTreePanel;
    }
    // add child nodes to root -- nodeToAdd is the name of playlist added
    public void addNodeToTree(String nodeToAdd) {
        // pass in string of node and create node with it
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToAdd); 
        //playlist.add(node);
        playlistModel.insertNodeInto(node, playlist, treeIndex++);
    }
    
    public TreePath getNextPath(String prefix) {
        TreePath path = mPlaylistTree.getNextMatch(prefix, 0, Position.Bias.Forward);
        return path;
    }
    
    // assume the node to be deleted already exist within playlist table
    // I'm not sure if this will work since it might be making a new copy of a 
    // a child node since I make a child node with the string passed in
    //public void deleteNode(int nodeToDelete) {
    public void deleteNode(TreePath path) {
        //DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToDelete); 
        //DefaultMutableTreeNode node = (DefaultMutableTreeNode)playlistModel.getChild(playlist, nodeToDelete);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        playlistModel.removeNodeFromParent(node);
        --treeIndex;
    }
    
    public void setTreeCotroller(TreeSelectionEvent tse){
        //mPlaylistTree.addMouseListener(tse);
    }
    
    public void setTreeMouseListener(MouseListener ml){
        mNewWindow.addMouseListener(ml);
        mPlaylistTree.addMouseListener(ml);
        mDeletePlaylist.addMouseListener(ml);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
    }

    
}
