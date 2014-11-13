import java.awt.Color;
import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreeNode;


/*
 Database login: 
 username: cecs343keg
 pass: chocotaco343
 DB name: nextunes
 host: mysql.karldotson.com
 */
/**
 *
 * @author Kelby, Edgar, Gil
 */
public class SongsTableComponent implements Observer /*, MouseListener, DropTargetListener */ {

    //private int mSongCount;
    private boolean mDoubleClick;
    private Vector<String> COLUMN_HEADER;
    private Vector<Vector> mSongsVector;
    private JTable mSongsTable;
    //private Database mDatabase;
    private int mSongSelectedIndex;
    private int mCurrentSongPlayingIndex;
    private JMenuItem mMenuRemoveSong;
    private JMenuItem mMenuAddSong;
    private JMenuItem mMenuAddToPlaylist;
    private DefaultTableModel mTableModel;
    private JPanel mTablePanel;
    private JPopupMenu mPopupMenu;
    private String mTableName;
    private ArrayList<JMenuItem> mSubMenu;
    private JMenuItem item; //delete
    private JMenuItem[] subMenuItems;

    public SongsTableComponent(String name, Vector<Vector> songSetFromDatabase, boolean isPlaylist) {
        mSongSelectedIndex = 0;
        mTableName = name;
        mSubMenu = new ArrayList<JMenuItem>();
        item = new JMenuItem("dummy");
        //subMenuItems = new JMenuItem[20];
        COLUMN_HEADER = new Vector<String>();
        COLUMN_HEADER.addElement("Filepath");
        COLUMN_HEADER.addElement("Title");
        COLUMN_HEADER.addElement("Artist");
        COLUMN_HEADER.addElement("Album");
        COLUMN_HEADER.addElement("Album Year");
        COLUMN_HEADER.addElement("Track #");
        COLUMN_HEADER.addElement("Genre");
        COLUMN_HEADER.addElement("Comments");

        mSongsVector = songSetFromDatabase;
        mTableModel = new DefaultTableModel(mSongsVector, COLUMN_HEADER);

        mSongsTable = new JTable();
        mSongsTable.setPreferredScrollableViewportSize(new Dimension(1200, (mSongsVector.size() + 10) * 10));
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.setAutoCreateRowSorter(true);
        mSongsTable.setModel(mTableModel);

        mSongsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mSongsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        mSongsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        mSongsTable.doLayout();

        JScrollPane scrollPane = new JScrollPane(mSongsTable);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getBounds();

        mTablePanel = new JPanel();
        mTablePanel.setMinimumSize(new Dimension(1200, 300));
        mTablePanel.add(scrollPane);

        if (isPlaylist) {//&& !mSongsTable.equals("Library")){
            mSongsTable.setComponentPopupMenu(getPlaylistPopupMenu());
        } else {
            mSongsTable.setComponentPopupMenu(getPopupMenu()); //add a popup menu to the JTable
        }

        //mTableModel.addTableModelListener(mSongsTable);
    }

    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String name) {
        mTableName = name;
    }

    public JMenuItem getMenuAddObj() {
        return mMenuAddSong;
    }

    public JMenuItem getMenuAddToPlaylistObj() {
        return mMenuAddToPlaylist;
    }

    public JPanel getTable() {
        return mTablePanel;
    }

    public JTable getTableObj() {
        return mSongsTable;
    }

    public void setBackground(Color color) {
        //setBackground(color);
        mSongsTable.setBackground(color);
    }

    public JMenuItem getMenuRemoveObj() {
        return mMenuRemoveSong;
    }

    public void setCurrentSongPlayingIndex(int currentSong) {
        mCurrentSongPlayingIndex = currentSong;
    }

    public int getCurrentSongPlayingIndex() {
        return mCurrentSongPlayingIndex;
    }

    private JPopupMenu getPopupMenu() {
        mPopupMenu = new JPopupMenu();
        mMenuAddSong = new JMenuItem("Add a song"); //new ImageIcon("/resources/add.png"));  
        mMenuAddToPlaylist = new JMenu("Add to playlist");
        //mMenuAddToPlaylist.add(new JMenuItem("This is some playlist"));
        mMenuRemoveSong = new JMenuItem("Remove selected song");
        mPopupMenu.add(mMenuAddSong);
        mPopupMenu.add(mMenuAddToPlaylist);
        mPopupMenu.add(mMenuRemoveSong);
        //JMenu subMenu = new JMenu();
        //mMenuAddToPlaylist = new HorizontalMenu();
        return mPopupMenu;
    }

    public void updatePopupSubmenu(TreeNode[] playlistName, Database db) {
        final Database database = db;
        Enumeration children = playlistName[1].children();
        mMenuAddToPlaylist.removeAll();
        String name = "";
<<<<<<< HEAD
<<<<<<< HEAD
        
        while(children.hasMoreElements()){
=======
        int x = 0;
        while (children.hasMoreElements()) {
>>>>>>> origin/master
=======
        int x = 0;
        while (children.hasMoreElements()) {
>>>>>>> origin/master
            name = children.nextElement().toString();
<<<<<<< HEAD
<<<<<<< HEAD
            JMenuItem menuItem = new JMenuItem(name);
            menuItem.setActionCommand(name);
            mMenuAddToPlaylist.add(menuItem);
=======
            item = new JMenuItem(name);
//            mSubMenu.add(item);
//            subMenuItems[x].setActionCommand(name);
            mMenuAddToPlaylist.add(item);
//            x++;

            item.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {

                    JMenuItem source = (JMenuItem) evt.getSource();
                    int songID = database.getSongID(getSongSelectedFilepath());
                    int playlistID = database.getPlaylistIDfromName(source.getText());
                    if (playlistID != 0 && songID != 0) {
                        database.addSongToPlaylist(songID, playlistID);
                        System.out.println("Song added to playlist");
                    } else {
                        System.out.println("Playlist doesn't exist");
                    }
                    System.out.println(source.getText());
                }

            });
>>>>>>> origin/master
=======
            item = new JMenuItem(name);
//            mSubMenu.add(item);
//            subMenuItems[x].setActionCommand(name);
            mMenuAddToPlaylist.add(item);
//            x++;

            item.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent evt) {

                    JMenuItem source = (JMenuItem) evt.getSource();
                    int songID = database.getSongID(getSongSelectedFilepath());
                    int playlistID = database.getPlaylistIDfromName(source.getText());
                    if (playlistID != 0 && songID != 0) {
                        database.addSongToPlaylist(songID, playlistID);
                        System.out.println("Song added to playlist");
                    } else {
                        System.out.println("Playlist doesn't exist");
                    }
                    System.out.println(source.getText());
                }

            });
>>>>>>> origin/master
        }
        //mMenuRemoveSong.getName();
    }

    public ArrayList<JMenuItem> getSubMenuItems() {
        return mSubMenu;
    }

    private JPopupMenu getPlaylistPopupMenu() {
        mPopupMenu = new JPopupMenu();
        mMenuAddSong = new JMenuItem("Add a song to playlist"); //new ImageIcon("/resources/add.png"));   
        mMenuRemoveSong = new JMenuItem("Remove selected song from playlist");
        mPopupMenu.add(mMenuAddSong);
        mPopupMenu.add(mMenuRemoveSong);
        return mPopupMenu;
    }

    public void addMouseController(MouseListener controller) {
        mSongsTable.addMouseListener(controller);
        mMenuAddSong.addMouseListener(controller);
        //mMenuAddToPlaylist.addMouseListener(controller);
        mMenuRemoveSong.addMouseListener(controller);
        mPopupMenu.addMouseListener(controller);

        //subMenuItems.addMouseListener(controller);
    }

    public void addDropController(DropTargetListener controller) {
        new DropTarget(mTablePanel, controller);
    }

    public void setSongSelected() {
        mSongSelectedIndex = mSongsTable.getSelectedRow();
    }

    public Vector getSongSelected() {
        Vector currentSongRow = mSongsVector.get(mSongSelectedIndex);
        mSongsTable.setRowSelectionInterval(mSongSelectedIndex, mSongSelectedIndex);
        return currentSongRow;
    }

    public Vector getSongSelected(int index) {
        Vector currentSongRow = mSongsVector.get(index);
        mSongsTable.setRowSelectionInterval(index, index);
        return currentSongRow;
    }

    public String getSongSelectedFilepath() {
        Vector currentSongRow = mSongsVector.get(mSongSelectedIndex);
        return (String) currentSongRow.get(0);
    }

    public int getSongSelectedIndex() {
        return mSongSelectedIndex;
    }

    public int getSongsCount() {
        return mSongsVector.size();
    }

    public boolean getDoubleClick() {
        return mDoubleClick;
    }

    public Vector getNextSong() {
        mCurrentSongPlayingIndex++;
        if (mCurrentSongPlayingIndex >= getSongsCount()) {
            mCurrentSongPlayingIndex = 0;
        }
        Vector currentSongRow = mSongsVector.get(mCurrentSongPlayingIndex);
        System.out.println("Next song:" + mCurrentSongPlayingIndex + " song count = " + getSongsCount());
        mSongsTable.setRowSelectionInterval(mCurrentSongPlayingIndex, mCurrentSongPlayingIndex);
        return currentSongRow;
    }

    /**
     * updated getPrevSong... works now as it should
     *
     * @return
     */
    public Vector getPrevSong() {
        System.out.println("Song index prev: " + mCurrentSongPlayingIndex);
        mCurrentSongPlayingIndex--;
        System.out.println("Index: " + mCurrentSongPlayingIndex);
        //String[] currentSongRow = new String[8];
        if (mCurrentSongPlayingIndex < 0) {
            mCurrentSongPlayingIndex = getSongsCount() - 1; //wrap around the index
        }
        Vector currentSongRow = mSongsVector.get(mCurrentSongPlayingIndex);
        mSongsTable.setRowSelectionInterval(mCurrentSongPlayingIndex, mCurrentSongPlayingIndex);
        return currentSongRow;
    }

    @Override
    public void update(Observable o, Object arg) {
        //mSongsVector.clear();
        mSongsVector = (Vector<Vector>) arg;

        /*
         if(arg != null){
         mTableModel.addRow((Vector)arg);
         }else{
         mTableModel.removeRow(mSongSelectedIndex);
         }
         */
        mTableModel.setDataVector(mSongsVector, COLUMN_HEADER);
        //mTableModel.fireTableDataChanged();
        mSongsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mSongsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        mSongsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        mSongsTable.doLayout();
    }

}
