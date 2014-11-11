
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Vector;


public class Database extends Observable {
    private Connection conn;
    
    Database(){
       conn = null;
    }
    
    private Connection getDBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql.karldotson.com:3306/nextunes", "cecs343keg", "chocotaco343");
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection to database failed.");
            //JOptionPane.showMessageDialog(this, "Connection to database failed.");
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Missing driver!");
            c.printStackTrace();
        }
        return conn;
    }
    
    private void getSongsInDBCount(){
        //use <vector>.size() instead
    }
    
    public Vector getSongsFromDatabase(){
        Vector<Vector> songsVector = new Vector<Vector>();
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Songs";
            ResultSet rs = stat.executeQuery(query);
          
            while (rs.next()) {
                Vector<String> vectorData = new Vector<String>();
                vectorData.addElement(sanitizeEmptyString(rs.getString("filepath")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("title")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("artist")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("album")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("year")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("track_num")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("genre")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("comment")));
                songsVector.addElement(vectorData);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get songs from database.");

        }
        return songsVector;
    }
    
    private void updateSongsFromDatabase(){
        Vector<Vector> songsVector = new Vector<Vector>();
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Songs";
            ResultSet rs = stat.executeQuery(query);
          
            while (rs.next()) {
                Vector<String> vectorData = new Vector<String>();
                vectorData.addElement(sanitizeEmptyString(rs.getString("filepath")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("title")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("artist")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("album")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("year")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("track_num")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("genre")));
                vectorData.addElement(sanitizeEmptyString(rs.getString("comment")));
                songsVector.addElement(vectorData);
            }
            ///notifyObservers(songsVector);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get songs from database.");

        }
    }
    
    private String sanitizeEmptyString(String stringToCheck) {
        if (stringToCheck.isEmpty()) {
            return "Unkown";
        }
        return stringToCheck;
    }
    
    public void addSong(String songToAdd){
        SongTags tags = new SongTags();
        String[] songTags = tags.extractSongTags(songToAdd);
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO Songs(filepath, title, artist, album, year, comment, genre, track_num) VALUES(?,?,?,?,?,?,?,?)");
            pstat.setString(1, songTags[0]); // value of filepath
            pstat.setString(2, songTags[1]); // value of title
            pstat.setString(3, songTags[2]); // value of artist
            pstat.setString(4, songTags[3]); // value of album
            pstat.setString(5, songTags[4]); // value year
            pstat.setString(6, songTags[5]); // value of track_num
            pstat.setString(7, songTags[6]); // value genre
            pstat.setString(8, songTags[7]); // value of comments
            pstat.executeUpdate();
            
            Vector<String> newData = new Vector<String>();
            for(int i = 0; i < 8; i++){
                newData.addElement(songTags[i]);
            }
            
            
            setChanged();
            notifyObservers(newData);
            //updateSongsFromDatabase();
            //notifyObservers();           
        } 
        catch (SQLException e) {
            System.out.println("Unable to insert song. Song may already exist");
            e.printStackTrace();
        }
    }
    
     public void deleteSong(Vector<String> fileToDelete) {

        String filepath = fileToDelete.get(0);
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            PreparedStatement pstat = conn.prepareStatement("DELETE FROM Songs WHERE filepath = ?");
            pstat.setString(1, filepath);
            pstat.executeUpdate();
            
            setChanged();
            notifyObservers(null);
            //updateSongsFromDatabase();
            //notifyObservers(); 
        } catch (SQLException e) {
                System.out.println("Unable to delete song");
        }
    }
}

