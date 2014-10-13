import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


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
public class Library extends JPanel implements MouseListener, DropTargetListener {

    public Library() {
        mSongs = new String[getSongCount()][mColumnHeaderLength];
        getSongsFromDatabase();
    }

    /**
     * Connects to database "nextunes" created on private server.
     */
    private void connectDB() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql.karldotson.com:3306/nextunes", "cecs343keg", "chocotaco343");
        } catch (SQLException e) {
            System.out.println("Connection to database failed.");
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Missing driver!");
            c.printStackTrace();
            return;
        }
        System.out.println("Connection successful");
        // connects to DB
    }

    private int getSongCount() {
        connectDB();

        try {
            Statement stat = conn.createStatement();

            // get song count from database
            ResultSet countRS = stat.executeQuery("SELECT COUNT(*) AS total FROM Songs");
            countRS.next(); // moves pointer to first element
            mSongCount = countRS.getInt("total");
            //System.out.println(songCount + " songs in library");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mSongCount;
    }

    /**
     * Gets the songs from library needed to populate table connect to DB and
     * retrieve songs, songs 2-D array will get populated here
     */
    private void getSongsFromDatabase() {
        connectDB(); // connects to DB

        try {
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Songs";
            ResultSet rs = stat.executeQuery(query);

            // Copy data from COLUMN_HEADER into the Songs array
            System.arraycopy(COLUMN_HEADER, 0, mSongs[0], 0, mColumnHeaderLength);

            /*
             ^^^ the above does the same as below but the above is optimized ^^^
             for (int column = 0; column < mColumnHeaderLength; column++) {
             mSongs[0][column] = COLUMN_HEADER[column];
             }
             */
            int row = 0; // counter to traverse trough 2D array
            while (rs.next()) {
                //int songCount = 1; // this will be the query that will get us the amount of row in the DB

                //for (int i = 0; i < songCount; i++) {
                mSongs[row][0] = rs.getString("filepath");
                mSongs[row][1] = rs.getString("title");
                mSongs[row][2] = rs.getString("artist");
                mSongs[row][3] = checkForEmptyString(rs.getString("album"));
                mSongs[row][4] = checkForEmptyString(rs.getString("year"));
                mSongs[row][5] = checkForEmptyString(rs.getString("comment"));
                mSongs[row][6] = checkForEmptyString(rs.getString("genre"));
                mSongs[row][7] = checkForEmptyString(rs.getString("track_num"));

                //System.out.println(filepath + " " +  title + " " + artist + " " + album + " " + year +
                //        " " + comment + " " + genre + " " + track_num);
                /*for (int j = 0; j < mAttributesLength; j++) {
                 mSongs[ii][0] = filepath;
                 mSongs[ii][1] = title;
                 mSongs[ii][2] = artist;
                 mSongs[ii][3] = album;
                 mSongs[ii][4] = year;
                 mSongs[ii][5] = track_num;
                 mSongs[ii][6] = genre;
                 mSongs[ii][7] = comment;
                 } */
                row++;
                //}
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get songs from database.");

        }

        // debugging deleteSong function
        //String song = "Red Rose"; // add whatever title you want 
        //deleteSong(song);
        // debugging insertSong function
        // will only insert it once, change the path if you want to insert a different one
        //insertSong("C:\\Music\\Spotiy", "Inserted Song", "Cold Playa", "XY",
        //"2016", "Just inserted", "5", "20");
    }

    private String checkForEmptyString(String stringToCheck) {
        if (stringToCheck.isEmpty()) {
            return "Unkown";
        }
        return stringToCheck;
    }

    public void addSongToDatabase(String filepath) {
        connectDB(); // We should probably include this in the constructor to avoid calling it everytime we need to update database
        String[] songTags = getSongTags(filepath);
        try {
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO Songs(filepath, title, artist, album, year, comment, genre, track_num) VALUES(?,?,?,?,?,?,?,?)");
            pstat.setString(1, songTags[0]); // value of filepath
            pstat.setString(2, songTags[1]); // value of title
            pstat.setString(3, songTags[2]); // value of artist
            pstat.setString(4, songTags[3]); // value of album
            pstat.setString(5, songTags[4]); // value year
            pstat.setString(6, songTags[5]); // value of comment
            pstat.setString(7, songTags[6]); // value genre
            pstat.setString(8, songTags[7]); // value of track_num
            pstat.executeUpdate();
            this.getSongsFromDatabase(); //update the JTable after a song insert is made
        } catch (SQLException e) {
            System.out.println("Unable to insert song. Song may already exist");
            e.printStackTrace();
        }

        //System.out.println("Song : " + title + " by " + artist + " was added.");
    }

    /**
     * Fetching songs and also TESTING INSERT AND DELETE FUNCTIONS
     *
     * @param filepath
     */
    private void deleteSong(String filepath) {
        connectDB();
        try {
            PreparedStatement pstat = conn.prepareStatement("DELETE FROM Songs WHERE filepath = ?");
            pstat.setString(1, filepath);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Unable to delete song");
        }
        System.out.println("Song with file path: " + filepath + " was deleted");
    }

    /**
     * Create GUI for table
     */
    public JPanel createTable() {
        mSongsTable = new JTable(mSongs, COLUMN_HEADER);
        //mSongsTable.setPreferredScrollableViewportSize(new Dimension(1000, 100));
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.addMouseListener((MouseListener) this);

        mScrollPane = new JScrollPane(mSongsTable);
        JPanel panel = new JPanel();
        new DropTarget(panel, this);
        mSongsTable.setPreferredScrollableViewportSize(new Dimension(1200, (mSongs.length + 10) * 10));
        mSongsTable.setFillsViewportHeight(true);
        mSongsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mSongsTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        mSongsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        mSongsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        mSongsTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        mScrollPane = new JScrollPane(mSongsTable);
        panel.add(mScrollPane);
        mSongsTable.setComponentPopupMenu(createPopupMenu()); //add a popup menu to the JTable
        return panel;
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        mMenuAddSong = new JMenuItem("Add a song");
        mMenuAddSong.addMouseListener(this);
        mMenuRemoveSong = new JMenuItem("Remove selected song");

        popupMenu.add(mMenuAddSong);
        popupMenu.add(mMenuRemoveSong);
        return popupMenu;
    }

    private String[] getSongTags(String pathToFile) {
        Mp3File mp3data = null;
        String[] songTags = new String[8];
        try {
            mp3data = new Mp3File(pathToFile);
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.out.println("Error reading mp3 file");
        }

        if (mp3data != null) {
            ID3v1 id3v1Tags = mp3data.getId3v1Tag();

            songTags[0] = pathToFile;
            songTags[1] = id3v1Tags.getTitle();
            songTags[2] = id3v1Tags.getArtist();
            songTags[3] = id3v1Tags.getAlbum();
            songTags[4] = id3v1Tags.getYear();
            songTags[5] = id3v1Tags.getComment();
            songTags[6] = id3v1Tags.getGenreDescription();
            songTags[7] = id3v1Tags.getTrack();
        }
        return songTags; //possibly returning null...need to double check
    }

    public String[] getCurrentSongSelected() {
        String[] currentSongRow = new String[8];
        for (int column = 0; column < mColumnHeaderLength; column++) {
            currentSongRow[column] = mSongs[mCurrentSongSelectedIndex][column];
        }
        mSongsTable.setRowSelectionInterval(mCurrentSongSelectedIndex, mCurrentSongSelectedIndex);
        return currentSongRow;
    }

    public String[] getNextSong() {
        mCurrentSongSelectedIndex++;
        String[] currentSongRow = new String[8];
        if (mCurrentSongSelectedIndex >= mSongCount) {
            mCurrentSongSelectedIndex = 0;
        }
        for (int column = 0; column < mColumnHeaderLength; column++) {
            currentSongRow[column] = mSongs[mCurrentSongSelectedIndex][column];
        }
        System.out.println("Next song:" + mCurrentSongSelectedIndex + " song count = " + mSongCount);
        mSongsTable.setRowSelectionInterval(mCurrentSongSelectedIndex, mCurrentSongSelectedIndex);
        return currentSongRow;
    }

    public String[] getPrevSong() {
        mCurrentSongSelectedIndex--;
        String[] currentSongRow = new String[8];
        if (mCurrentSongSelectedIndex < 0) {
            mCurrentSongSelectedIndex = mSongCount - 1; //wrap around the index
        }
        for (int column = 0; column < mColumnHeaderLength; column++) {
            currentSongRow[column] = mSongs[mCurrentSongSelectedIndex][column];
        }
        mSongsTable.setRowSelectionInterval(mCurrentSongSelectedIndex, mCurrentSongSelectedIndex);
        return currentSongRow;

    }

    // Data members
    private int mSongCount;
    //private String[] songTags;
    private static final String[] COLUMN_HEADER = {"Filepath", "Title", "Artist", "Album",
        "Album Year", "Track #", "Genre", "Comments"};
    private final int mColumnHeaderLength = COLUMN_HEADER.length;
    private final String[][] mSongs;
    private JTable mSongsTable;
    private JScrollPane mScrollPane;
    private Connection conn;
    private int mCurrentSongSelectedIndex;
    private JMenuItem mMenuRemoveSong;
    private JMenuItem mMenuAddSong;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == mMenuAddSong) {
            System.out.println("The menu shiet works");
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(Library.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                this.addSongToDatabase(file.getAbsolutePath());

                //System.out.println("Opening: " + file.getAbsolutePath() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
        } else if (e.getSource() == mSongsTable) {
            System.out.println("The Jtable was clicked");
            //JTable result = (JTable) e.getSource();
            //System.out.println(mSongsTable.getSelectedRow());
            mCurrentSongSelectedIndex = mSongsTable.getSelectedRow(); //changed by Edgar
            //System.out.println(mCurrentSongSelectedIndex);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void dragEnter(DropTargetDragEvent dragEnter) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragOver(DropTargetDragEvent dragOver) {
        mSongsTable.setBackground(Color.red);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dropActionChanged) {

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragExit(DropTargetEvent dragExit) {
        mSongsTable.setBackground(Color.white);
    }

    @Override
    public void drop(DropTargetDropEvent drop) {
        drop.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = drop.getTransferable();
        DataFlavor[] dataTypes = transferable.getTransferDataFlavors();
        mSongsTable.setBackground(Color.white);
        for (DataFlavor type : dataTypes) {

            try {
                if (type.isFlavorJavaFileListType()) {
                    List<File> files = (List) transferable.getTransferData(type);
                    for (File file : files) {
                        System.out.println(file.getAbsolutePath());
                    }
                }
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);

            }

        }
        System.out.println("Something was dropped here...is it yours?");
    }

}
