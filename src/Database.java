
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
    
    // should we also retrieve the playlist_ID here? If so, I can make a vector of vectors
    // otherwise a string vector suffices since each playlist name should be unique
    public Vector getPlaylistsFromDatabase(){
        Vector<String> playlistsVector = new Vector<String>();
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Playlists";
            ResultSet rs = stat.executeQuery(query);
          
            while (rs.next()) {
                playlistsVector.add(rs.getString("playlist_name"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get playlists from database.");

        }
        // debugging
        for(int i=0; i<playlistsVector.size();i++) 
        {
            System.out.println(playlistsVector.get(i));
        }
        return playlistsVector;
    }
    
    public void deleteSongFromPlayList(int songID, int playlistID) {
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            
            PreparedStatement pstat = conn.prepareStatement("DELETE FROM song_playlist WHERE song_ID = ? AND playlist_ID = ?");
            pstat.setInt(1, songID); // value of songID
            pstat.setInt(2, playlistID);
            pstat.executeUpdate();
            
            setChanged();
            //notifyObservers(null);
            //updateSongsFromDatabase(); // will be updateSongsFromPlaylist
            getPlaylistSongsFromDatabase(getPlaylistNameFromID(playlistID));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get song ID from database.");
        }
    }
    
    public void deletePlaylistFromDatabase(String playlistName){
        int playlistID = getPlaylistIDfromName(playlistName);
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            
            PreparedStatement pstat = conn.prepareStatement("DELETE FROM song_playlist WHERE playlist_ID = ?");
            pstat.setInt(1, playlistID); // value of songID
            pstat.executeUpdate();
            
            pstat = conn.prepareStatement("DELETE FROM Playlists WHERE playlist_ID = ?");
            pstat.setInt(1, playlistID); // value of songID
            pstat.executeUpdate();
            
            conn.close();
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get song ID from database.");
        }
    }
    
    public int getSongID(String filepath) {
        int songID = 0;
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Songs WHERE filepath = \"" + filepath + "\"";
            ResultSet rs = stat.executeQuery(query);
            
            while (rs.next()) {
                songID = rs.getInt("song_ID");
            }
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to get song ID from database.");
        }
        return songID;
    }
    
    public int getPlaylistIDfromName(String playlistName) {
        int playlistID = 0; 
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT playlist_ID FROM Playlists WHERE playlist_name = \"" + playlistName + "\"";
            ResultSet rs = stat.executeQuery(query);
            
            while (rs.next()) {
                playlistID = rs.getInt("playlist_ID");
            }
            
            
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return playlistID; 
    }
    
    public String getPlaylistNameFromID(int playlistID) {
        String playlistName = ""; 
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT playlist_name FROM Playlists WHERE playlist_ID = '" + playlistID + "'";
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                playlistName = rs.getString("playlist_name");
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return playlistName; 
    }
    
    public Vector getPlaylistSongsFromDatabase(String playlistName){
        Vector<Vector> songsVector = new Vector<Vector>();
        try {
            this.getDBConnection();
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Songs NATURAL JOIN song_playlist NATURAL JOIN Playlists WHERE playlist_name = '" + playlistName + "'";
            
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
            notifyObservers(songsVector);
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
            notifyObservers(songsVector);
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
            //notifyObservers(newData);
            updateSongsFromDatabase();
            //notifyObservers();           
        } 
        catch (SQLException e) {
            System.out.println("Unable to insert song. Song may already exist");
            e.printStackTrace();
        }
    }
    
    public void addPlaylist(String playlistName) {
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO Playlists(playlist_name) VALUES(?)");
            pstat.setString(1, playlistName); // value of name of playlist
            pstat.executeUpdate();
            
            // new playlist then gets added to JTree by controller class
            /*
            Vector<String> newData = new Vector<String>();
            for(int i = 0; i < 8; i++){
                newData.addElement(songTags[i]);
            }
            */
            
            //setChanged();
            //notifyObservers(newData);
            //updateSongsFromDatabase();
            //notifyObservers();           
        } 
        catch (SQLException e) {
            System.out.println("Unable to insert playlist. A playlist by the same name may already exist.");
            e.printStackTrace();
        }
    }
    
    /**
     * adds songs to the association table song_playlist
     * @param songID
     * @param playListID 
     */
    public void addSongToPlaylist(int songID, int playlistID) {
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO song_playlist(song_ID, playList_ID) VALUES(?,?)");
            pstat.setInt(1, songID); // value of songID
            pstat.setInt(2, playlistID);
            pstat.executeUpdate();
            
            // new playlist gets added to table as it it called by controller class
            /*
            Vector<String> newData = new Vector<String>();
            for(int i = 0; i < 8; i++){
                newData.addElement(songTags[i]);
            }
            */
            
            //setChanged();
            //notifyObservers(newData);
            //updateSongsFromDatabase();
            //notifyObservers();           
        } 
        catch (SQLException e) {
            System.out.println("Unable to insert playlist. A playlist by the same name may already exist.");
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
            //notifyObservers(null);
            updateSongsFromDatabase();
            //notifyObservers(); 
        } catch (SQLException e) {
                System.out.println("Unable to delete song");
        }
     }
     
        public void deleteSongFromLibrary(String filepath) {

       // String filepath = fileToDelete.get(0);
        try {
            if (conn.isClosed()) {
                this.getDBConnection();
            }
            PreparedStatement pstat = conn.prepareStatement("DELETE FROM Songs WHERE filepath = ?");
            //String query = "SELECT * FROM Songs WHERE filepath = \"" + filepath + "\"";
            pstat.setString(1, filepath);
            pstat.executeUpdate();
            
            setChanged();
            //notifyObservers(null);
            updateSongsFromDatabase();
            //notifyObservers(); 
        } catch (SQLException e) {
                System.out.println("Unable to delete song");
        }
    }
}

