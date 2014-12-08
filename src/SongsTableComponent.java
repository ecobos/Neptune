import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel; 	 	
import javax.swing.table.TableRowSorter;
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
    private String mSelectedSongFilePath;
    private String mNextSongFilePath;
    private String mPrevSongFilePath;
    private JMenuItem mMenuRemoveSong;
    private JMenuItem mMenuAddSong;
    private JMenuItem mMenuAddToPlaylist;
    private DefaultTableModel mTableModel;
    private JPanel mTablePanel;
    private JPopupMenu mPopupMenu;
    private JPopupMenu mColumnPopupMenu;
    private JCheckBox mAlbumColumn;
    private JCheckBox mArtistColumn;
    private JCheckBox mYearColumn;
    private JCheckBox mGenreColumn;
    private JCheckBox mCommentColumn;
    private String mTableName;
    private ArrayList<JMenuItem> mSubMenu;
    private JMenuItem item; //delete
    private JMenuItem[] subMenuItems;
    private final TableColumnHider mColumnHider;
    private JCheckBox mCheckBox[];
    
    // made this two private so that scrollbar changes goes up or down when go to current song selected is clicked
    private JScrollPane mScrollPane;
    private JScrollBar mScrollBar;
    private String[] settings; 

    /**
     * Class constructor.
     *
     * @param name The table's name
     * @param songSetFromDatabase Vector containing all the songs for this table
     * @param isPlaylist True means that its a playlist view
     * @param playerSettings settings of the player 
     */
    public SongsTableComponent(String name, Vector<Vector> songSetFromDatabase, boolean isPlaylist, String[] playerSettings) { 
        settings = new String[5]; 
        mCheckBox = new JCheckBox[5];
	         	 	
        // SETTINGS -- upon start 	 	
        for(int i=0; i<playerSettings.length; i++) { 	 	
            settings[i] = playerSettings[i]; 	 	
        } 	 	
         	
        mSongSelectedIndex = 0;
        mCurrentSongPlayingIndex = 0;
        mTableName = name;
        mSubMenu = new ArrayList<JMenuItem>();
        item = new JMenuItem("dummy");
        //subMenuItems = new JMenuItem[20];
        COLUMN_HEADER = new Vector<String>();
        COLUMN_HEADER.addElement("Filepath");
        COLUMN_HEADER.addElement("Title"); // index 1
        COLUMN_HEADER.addElement("Artist"); // index 2
        COLUMN_HEADER.addElement("Album");
        COLUMN_HEADER.addElement("Album Year");
        COLUMN_HEADER.addElement("Track #");
        COLUMN_HEADER.addElement("Genre");
        COLUMN_HEADER.addElement("Comments"); // swapped with track #
        COLUMN_HEADER.addElement("ID"); 
        COLUMN_HEADER.addElement("Length");
        
        mSongsVector = songSetFromDatabase;
        mTableModel = new DefaultTableModel(mSongsVector, COLUMN_HEADER){

            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };
        
        mSongsTable = new JTable();
        mSongsTable.setPreferredScrollableViewportSize(new Dimension(1200, (mSongsVector.size() + 10) * 10));
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.setAutoCreateRowSorter(true);
        mSongsTable.setModel(mTableModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(mTableModel); // added table sorter	106	         
        sorter.toggleSortOrder(1); // sort on title 	 	
        mSongsTable.setRowSorter(sorter); 
        

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
       
        mSongsTable.getColumnModel().getColumn(0).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(1).setMinWidth(200);
        mSongsTable.getColumnModel().getColumn(2).setMinWidth(200);
        mSongsTable.getColumnModel().getColumn(3).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(5).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(5).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(5).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(6).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(7).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(8).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(8).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setPreferredWidth(0);
        
        mSongsTable.doLayout();

        mScrollPane = new JScrollPane(mSongsTable);
        mScrollPane.setWheelScrollingEnabled(true);
        mScrollPane.getBounds();
        mScrollBar = mScrollPane.getVerticalScrollBar();
        //vertical.setValue( vertical.getMaximum() );

        mTablePanel = new JPanel();
        mTablePanel.setMinimumSize(new Dimension(1200, 300));
        mTablePanel.add(mScrollPane);
        mSongsTable.getTableHeader().setComponentPopupMenu(getColumnPopupMenu());
        if (isPlaylist) {//&& !mSongsTable.equals("Library")){
            mSongsTable.setComponentPopupMenu(getPlaylistPopupMenu());
        } else {
            mSongsTable.setComponentPopupMenu(getPopupMenu()); //add a popup menu to the JTable
        }
        
        mColumnHider = new TableColumnHider(mSongsTable);
        hideColumnsUponStart(); 	 	
        //mColumnHider.hide("Artist"); 
        //mTableModel.addTableModelListener(mSongsTable);
    }

     /* ignore for now
    public void sortSongsVector(int sortByIndex) {
        switch(sortByIndex) {
            case 1: 
                break;
            case 2:
                break;
            case 3: 
                break;
            case 4: 
                break;
        }
    }
    
    public void sortByTitle() {
        
    }
    */
    public void hideColumnsUponStart() {
        String[] columnNames = {"Artist", "Album", "Album Year", "Genre", "Comments"};
        for(int i=0;i<5;i++) { 	 	
           if(settings[i].equals("false")) { 
               mCheckBox[i].setSelected(false);
               mColumnHider.hide(columnNames[i]); 	 	
           }  	 	
        } 	 	
	         	 	
    } 
    
    public JTable getSongsTableObj(){
        return mSongsTable;
    }
    
    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String name) {
        mTableName = name;
    }
    /**
     * Scroll bar moves to this location when Goto current song selected is called
     * @param songIndex 
     */
    // NOT WORKING
    public void setScrollBarPosition(int songIndex) {
        mScrollBar.setValue(songIndex);
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
    
    public void scrollToSongPlaying(){
        System.out.println("Scroll: " + mCurrentSongPlayingIndex);
        mSongsTable.getSelectionModel().setSelectionInterval(mCurrentSongPlayingIndex, mCurrentSongPlayingIndex);
        mSongsTable.scrollRectToVisible(new Rectangle(mSongsTable.getCellRect(mCurrentSongPlayingIndex, 0, true)));
    }
    
    public void scrollToSelectedSong(){
        getSongSelected();
        mSongsTable.getSelectionModel().setSelectionInterval(mSongSelectedIndex, mSongSelectedIndex);
        mSongsTable.scrollRectToVisible(new Rectangle(mSongsTable.getCellRect(mSongSelectedIndex, 0, true)));
    }

    /**
     * Sets the song at the specified index as the current song
     *
     * @param currentSong Currently selected (clicked on) song
     */
    public void setSongPlayingIndex() {
        if(mSongsTable.getSelectedRow() == -1){
            mCurrentSongPlayingIndex = 0;
        } 
        else {
            mCurrentSongPlayingIndex = mSongsTable.getSelectedRow();
        }
    }
    
    public int getSongPlayingIndex(){
        return mCurrentSongPlayingIndex;
    }
    
    public void setSongPlayingIndex(int songPlaying) {
        mCurrentSongPlayingIndex = songPlaying;
    }
    
    public void setNextSongPlayingIndex(){
        int next = mCurrentSongPlayingIndex + 1;
        if (next >= getSongsCount()) {
                    next = 0;
        }
        mCurrentSongPlayingIndex = next;
    }

    public void setPrevSongPlayingIndex(){
        int prev = mCurrentSongPlayingIndex - 1;
        if (prev < 0) {
            prev = getSongsCount() - 1; //wrap around the index
        }
        mCurrentSongPlayingIndex = prev;
    }
    
    /**
     * Returns the index of the currently selected (clicked on) song
     *
     * @return index of clicked on song
     */
    public int getCurrentSongPlayingIndex() {
        return mCurrentSongPlayingIndex;
    }
    
    /**
     * Highlights selected row (song) that is playing
     * @param row is the row of the current song playing
     */
    public void setSelectionInterval(int row) {
        mSongsTable.setRowSelectionInterval(row, row);
        mSongsTable.scrollRectToVisible(new Rectangle());
    }

    /**
     * Creates a popup menu and its related internal structure for the songs
     * table
     *
     * @return A constructed popup menu
     */
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

    /**
     * Updates the popup menu's playlist data
     *
     * @param playlistName playlists' root name
     * @param db A database reference to be able to retrieve current data
     */
    public void updatePopupSubmenu(TreeNode[] playlistName, Database db) {
        final Database database = db;
        Enumeration children = playlistName[1].children();
        mMenuAddToPlaylist.removeAll();
        String name = "";
        int x = 0;
        while (children.hasMoreElements()) {
            name = children.nextElement().toString();
            //JMenuItem menuItem = new JMenuItem(name);
            //menuItem.setActionCommand(name);
            //mMenuAddToPlaylist.add(menuItem);
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

    public String[] getChangedSettings() {
        return settings;
    }
    
    public void setColSetting(String fieldToChange, String boolVal) {
        switch(fieldToChange) {
            case "Artist": settings[0] = boolVal;
                break;
            case "Album": settings[1] = boolVal;
                break;                
            case "Album Year": settings[2] = boolVal;
                break; 
            case "Genre": settings[3] = boolVal;
                break;
            case "Comments": settings[4] = boolVal;
                break;
        }
    }
    
    private JPopupMenu getColumnPopupMenu() {
        mColumnPopupMenu = new JPopupMenu();
        String[] columnNames = {"Artist", "Album", "Album Year", "Genre", "Comments"};
        
        for (int i = 0; i < columnNames.length; i++) {
            mCheckBox[i] = new JCheckBox(columnNames[i]);
            mCheckBox[i].setSelected(true);
            mCheckBox[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    JCheckBox cb = (JCheckBox) evt.getSource();
                    String columnName = cb.getText();
                    System.out.println(columnName);
                    if (cb.isSelected()) {
                        mColumnHider.show(columnName);
                        setColSetting(columnName, "true");
                    } else {
                        mColumnHider.hide(columnName);
                        setColSetting(columnName, "false");
                    }
                }
            });
            mColumnPopupMenu.add(mCheckBox[i]);
        }
        return mColumnPopupMenu;
    }
    
    public JCheckBox getArtistColumnObj(){
        return mArtistColumn;
    }
    
    public JCheckBox getAlbumColumnObj(){
        return mAlbumColumn;
    }
    
    public JCheckBox getYearColumnObj(){
        return mYearColumn;
    }
    
    public JCheckBox getGenreColumnObj(){
        return mGenreColumn;
    }
    
    public JCheckBox getCommentColumnObj(){
        return mCommentColumn;
    }
    
    public void setController(ActionListener controller){
        mArtistColumn.addActionListener(controller);
        mAlbumColumn.addActionListener(controller);
        mYearColumn.addActionListener(controller);
        mGenreColumn.addActionListener(controller);
        mCommentColumn.addActionListener(controller);
    }	
    
	
    public void addMouseController(MouseListener controller) {
        mSongsTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = mSongsTable.columnAtPoint(e.getPoint());
                String name = mSongsTable.getColumnName(col);
                int row = mSongsTable.rowAtPoint(e.getPoint());
                mSongSelectedIndex = row;
                System.out.println("Column index selected " + col + " " + name);
                col = mSongsTable.columnAtPoint(e.getPoint());
                Object selectedObj = mSongsTable.getValueAt(row, col);
                Object filepathObj = mSongsTable.getValueAt(row, 0);
                mSelectedSongFilePath = (String)filepathObj;
                System.out.println("Selected object: " + selectedObj.toString());
                System.out.println("Filepath: " + filepathObj);
                setSongSelected();
                System.out.println("Row selected: " + mSongSelectedIndex);
            }
        });
        
        mSongsTable.addMouseListener(controller);/*new MouseAdapter()); {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = mSongsTable.rowAtPoint(e.getPoint());
                mSongSelectedIndex = row;
                int col = mSongsTable.columnAtPoint(e.getPoint());
                Object selectedObj = mSongsTable.getValueAt(row, col);
                Object filepathObj = mSongsTable.getValueAt(row, 0);
                mSelectedSongFilePath = (String)filepathObj;
                System.out.println("Selected object: " + selectedObj.toString());
                System.out.println("Filepath: " + filepathObj);
            }
        }
    );*/
        
        
        mMenuAddSong.addMouseListener(controller);
        mMenuRemoveSong.addMouseListener(controller);
        mPopupMenu.addMouseListener(controller);

        //subMenuItems.addMouseListener(controller);
    }

    public void addDropController(DropTargetListener controller) {
        new DropTarget(mTablePanel, controller);
    }
    
    /**
     * Set the currently selected (clicked on) song
     */
    public void setSongSelected() {
        mSongSelectedIndex = mSongsTable.getSelectedRow();
    }
    
    /**
     * Get the currently selected (clicked on) song
     */
    public int getSongSelected() {
        return mSongSelectedIndex;
    }

    /**
     * Gets all the data for the selected(clicked on) song
     * 
     * @return Vector containing selected songs data
     */
    public Vector getSongSelectedVector() {
        //setSongSelected();
        Vector currentSongRow = mSongsVector.get(mSongSelectedIndex);
        mSongsTable.setRowSelectionInterval(mSongSelectedIndex, mSongSelectedIndex);
        return currentSongRow;
    }

    /**
     * Returns the data the specified song's table index
     * 
     * @param index target song's index
     * @return Vector containing the song's data
     */
    public Vector getSongSelected(int index) {
        Vector currentSongRow = mSongsVector.get(index);
        mSongsTable.setRowSelectionInterval(index, index);
        return currentSongRow;
    }
    
    public Vector getSongSelected(String filepath) {
        int size = 0;
        int index = 0;
        while(size < mSongsVector.size()){
            if(mSongsVector.get(size).get(0).equals(filepath)){
                index = size;
            }
            size++;
        }
        Vector currentSongRow = mSongsVector.get(index);
        mSongsTable.setRowSelectionInterval(index, index);
        return currentSongRow;
    }

    public int getSongSelectedID(){
        Vector currentSongRow = mSongsVector.get(mSongSelectedIndex);
        return Integer.parseInt((String)currentSongRow.lastElement());
    }
    
    /**
     * Returns the currently selected song's filepath
     * 
     * @return Filepath to currently selected song
     */
    public String getSongSelectedFilepath() {
        setSongSelected();
        //Vector currentSongRow = mSongsVector.get(mSongSelectedIndex);
        return mSelectedSongFilePath;//(String) currentSongRow.get(0);
    }

    public void setSongSelectedFilepath(String filepath) {
        mSelectedSongFilePath = filepath;
    }
    
    public void setNextSongFilepath(String filepath){
        mNextSongFilePath = filepath;
    }
    
    public String getNextFilepath(){
        return mNextSongFilePath;
    }
    
    public void setPrevSongFilepath(String filepath){
        mPrevSongFilePath = filepath;
    }
    
    public String getPrevFilepath(){
        return mPrevSongFilePath;
    }
    
    /**
     * Returns the currently selected (clicked on) song's index
     * @return index of clicked on song 
     */
    public int getSongSelectedIndex() {
        return mSongSelectedIndex;
    }

    /**
     * Returns the column by which the table is sorted
     * @return column by which the table is sorted
     */
    //public int getSelectedColumnHeader() {
    //    return mColumnHeaderIndex;
    //}
    
    /**
     * Returns the column by which the table is sorted
     * @return column by which the table is sorted
     */
    //public int getSelectedColumnHeader() {
    //    return mColumnHeaderIndex;
    //}
    
    /**
     * The number of song currently contained by the table
     * 
     * @return number of songs
     */
    public int getSongsCount() {
        return mSongsVector.size();
    }

    public boolean getDoubleClick() {
        return mDoubleClick;
    }

    /**
     * Gets the next song after the current song in the table. Wraps around the 
     * table if necessary
     * 
     * @return Vector containing all data of the next song
     */
    public Vector getNextSong() {
//        mSongSelectedIndex = row;
//                int col = mSongsTable.columnAtPoint(e.getPoint());
//                Object selectedObj = mSongsTable.getValueAt(row, col);
//                Object filepathObj = mSongsTable.getValueAt(row, 0);
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
     * Gets the previous song before the current song in the table. Wraps around
     * the table if neccessary
     *
     * @return Vector containing all data of the previous song
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
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
       
        mSongsTable.getColumnModel().getColumn(0).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(1).setMinWidth(200);
        mSongsTable.getColumnModel().getColumn(2).setMinWidth(200);
        mSongsTable.getColumnModel().getColumn(3).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(5).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(5).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(5).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(6).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(7).setMinWidth(100);
        mSongsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        mSongsTable.getColumnModel().getColumn(8).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(8).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setMinWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setMaxWidth(0);
        mSongsTable.getColumnModel().getColumn(9).setPreferredWidth(0);
        
        mSongsTable.doLayout();
        hideColumnsUponStart();
    }

}
