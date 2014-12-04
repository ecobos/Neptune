import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
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

    /**
     * Class constructor. Creates a tree based on the vector containing the playlists
     * currently in the database
     * 
     * @param playlistFromDatabase Vector with playlist names
     */
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
        mPlaylistTree.addSelectionRow(0);
        mTreePanel = new JPanel();
        mTreePanel.setBackground(Color.DARK_GRAY);
        mTreePanel.add(mPlaylistTree);
    }
    
    /**
     * Creates a popup menu.
     * 
     * @return The constructed pop up menu
     */
    private JPopupMenu getPopupMenu() {
        JPopupMenu mPopupMenu = new JPopupMenu();
        mNewWindow = new JMenuItem("Open in new window");
        mDeletePlaylist = new JMenuItem("Delete playlist...");
        mPopupMenu.add(mNewWindow);
        mPopupMenu.add(mDeletePlaylist);
        return mPopupMenu;
    }
    
    /**
     * Get the target for the create new window functionality
     * 
     * @return The target object 
     */
    public JMenuItem getNewWindowObj(){
        return mNewWindow;
    } 
    
    /**
     * Gets the leaf node names
     * 
     * @return A treenode containing leaf data
     */
    public TreeNode[] getLeafNodeNames(){        
        return playlist.getPath();
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
    
    /**
     * Gets the name of the selected leaf node
     * 
     * @return The name of the selected leaf node
     */
    public String getSelectedLeafName(){
        return mPlaylistTree.getSelectionPath().getLastPathComponent().toString();
    }
    
    public JPanel getTreePanel(){
        return mTreePanel;
    }
    
    /**
     * Creates and adds a new node to the tree
     * 
     * @param nodeToAdd The name of the new node to be added
     */
    public void addNodeToTree(String nodeToAdd) {
        // pass in string of node and create node with it
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToAdd); 
        //playlist.add(node);
        playlistModel.insertNodeInto(node, playlist, treeIndex++);
    }
    
    public void setNewBranchAsSelected(){
        mPlaylistTree.removeSelectionRow(0);
        mPlaylistTree.removeSelectionRow(1);
        mPlaylistTree.addSelectionRow(treeIndex+1);
    }
    
    // assume the node to be deleted already exist within playlist table
    // I'm not sure if this will work since it might be making a new copy of a 
    // a child node since I make a child node with the string passed in
    //public void deleteNode(int nodeToDelete) {
    
    /**
     * Delete a node from the tree structure.
     * 
     * @param path Path to the node to be deleted
     */
    public void deleteNode(TreePath path) {
        //DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeToDelete); 
        //DefaultMutableTreeNode node = (DefaultMutableTreeNode)playlistModel.getChild(playlist, nodeToDelete);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        playlistModel.removeNodeFromParent(node);
        --treeIndex;
        mPlaylistTree.addSelectionRow(0);  
    }
    
    public void setTreeCotroller(TreeSelectionEvent tse){
        //mPlaylistTree.addMouseListener(tse);
    }
    //
    public void setTreeMouseListener(MouseListener ml){
        mNewWindow.addMouseListener(ml);
        mPlaylistTree.addMouseListener(ml);
        mDeletePlaylist.addMouseListener(ml);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
    }
}
