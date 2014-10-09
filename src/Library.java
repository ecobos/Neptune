import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.Dimension;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

// I CHANGED THIS LINE
// What does the Sapien say??
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
public class Library {

    public Library() {
        mSongs = new String[30][mAttributesLength]; // 30 will be replaced by songs count... that can be 
        // done with a query getting song count from library 

        getSongs();
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

    /**
     * Gets the songs from library needed to populate table connect to DB and
     * retrieve songs, songs 2-D array will get populated here
     */
    private void getSongs() {
        connectDB(); // connects to DB

        try {
            Statement stat = conn.createStatement();

            // get song count from database
            ResultSet countRS = stat.executeQuery("SELECT COUNT(*) AS total FROM Songs");
            countRS.next(); // moves pointer to first element
            int songCount = countRS.getInt("total");
            System.out.println(songCount + " songs in library");

            String query = "SELECT * FROM Songs";
            ResultSet rs = stat.executeQuery(query);
            //ResultSetMetaData metaData = rs.getMetaData();
            ///rs.next();
            //int songCount = rs.getInt(1);

            // populate column data 
            for (int i = 0; i < mAttributesLength; i++) {
                mSongs[0][i] = ATTRIBUTES[i];
            }

            int ii = 0; // counter to traverse trough 2D array
            while (rs.next()) {
                //int songCount = 1; // this will be the query that will get us the amount of row in the DB

                //for (int i = 0; i < songCount; i++) {
                String filepath = rs.getString("filepath");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String album = rs.getString("album");
                String year = rs.getString("year");
                String comment = rs.getString("comment");
                String genre = rs.getString("genre");
                String track_num = rs.getString("track_num");

                    //System.out.println(filepath + " " +  title + " " + artist + " " + album + " " + year +
                //        " " + comment + " " + genre + " " + track_num);
                for (int j = 0; j < mAttributesLength; j++) {
                    mSongs[ii][0] = filepath;
                    mSongs[ii][1] = title;
                    mSongs[ii][2] = artist;
                    mSongs[ii][3] = album;
                    mSongs[ii][4] = year;
                    mSongs[ii][5] = track_num;
                    mSongs[ii][6] = genre;
                    mSongs[ii][7] = comment;
                }
                ii++;
                //}
            }
        } 
        catch (SQLException e) {
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

    private void insertSong(String filepath, String title, String artist, String album,
            String year, String comment, String genre, String track_num) {
        connectDB(); // We should probably include this in the constructor to avoid calling it everytime we need to update database
        try {
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO Songs(filepath, title, artist, album, year, comment, genre, track_num) VALUES(?,?,?,?,?,?,?,?)");
            pstat.setString(1, filepath); // sets first ? to the value of filepath
            pstat.setString(2, title); // sets second ? to value of title and so on... 
            pstat.setString(3, artist);
            pstat.setString(4, album);
            pstat.setString(5, year);
            pstat.setString(6, comment);
            pstat.setString(7, genre);
            pstat.setString(8, track_num);
            pstat.executeUpdate();
            this.getSongs();
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
        mSongsTable = new JTable(mSongs, ATTRIBUTES);
        mSongsTable.setPreferredScrollableViewportSize(new Dimension(1000, 100));
        mSongsTable.setFillsViewportHeight(true);
        mScrollPane = new JScrollPane(mSongsTable);
        JPanel panel = new JPanel();
        panel.add(mScrollPane);
        return panel;
    }
    
    public void addSongToLibrary(String pathToFile){
        Mp3File mp3data = null;
        try{
            mp3data = new Mp3File(pathToFile);
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ioe){
            System.out.println("Error reading mp3 file");
        }
        if (mp3data != null){
        ID3v1 id3v1Tags = mp3data.getId3v1Tag();
        insertSong(pathToFile, id3v1Tags.getTitle(), id3v1Tags.getArtist(), 
                id3v1Tags.getAlbum(), id3v1Tags.getYear(), id3v1Tags.getComment(), 
                id3v1Tags.getGenreDescription(), id3v1Tags.getTrack());
        //System.out.println("The id3v1 artist tag is " + mp3data.getId3v1Tag().getArtist());
        }
    }

    // Data members
    private static final String[] ATTRIBUTES = {"Filepath", "Title", "Artist", "Album",
        "Album Year", "Track #", "Genre", "Comments"};
    private final int mAttributesLength = ATTRIBUTES.length;
    private final String[][] mSongs;
    private JTable mSongsTable;
    private JScrollPane mScrollPane;
    private Connection conn;

}

