import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SongTags {
    
    /**
     * Class constructor
     */
    public SongTags(){
      // I'm feeling empty inside  
    }
    
    /**
     * Gets the song tags from a specific mp3 song file
     * 
     * @param pathToFile path to the target mp3 file
     * @return An array with all the relevant song tags
     */
    public String[] extractSongTags(String pathToFile){
        Mp3File mp3data = null;
        String[] songTags = new String[9];
        try {
            mp3data = new Mp3File(pathToFile);
            mp3data.getLengthInMilliseconds(); //GETS THE LENGTH OF THE SONG
            System.out.println("Seconds: " + mp3data.getLengthInMilliseconds()/1000.0);
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(SongsTableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(SongsTableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.out.println("Error reading mp3 file");
        }

        if (mp3data != null) {
            ID3v1 id3v1Tags = mp3data.getId3v1Tag();
            if (id3v1Tags == null) {
                id3v1Tags = mp3data.getId3v2Tag();
            }

            songTags[0] = pathToFile;
            songTags[1] = id3v1Tags.getTitle();
            songTags[2] = id3v1Tags.getArtist();
            songTags[3] = id3v1Tags.getAlbum();
            songTags[4] = id3v1Tags.getYear();
            songTags[5] = id3v1Tags.getTrack();
            songTags[6] = id3v1Tags.getGenreDescription();
            songTags[7] = id3v1Tags.getComment();
            songTags[8] = Long.toString((long) (mp3data.getLengthInMilliseconds()/1000.0));
        }
        return songTags;
    }
}
