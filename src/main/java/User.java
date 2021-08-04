import java.util.ArrayList;
import java.util.List;

public class User {

    private final String id;
    private final String userName;
    private String profilePicture;
    private List<String> animeList;

    // Posting a new User
    public User(String id, String userName) {
        this.id = id;
        this.userName = userName;
        this.profilePicture = "zero";
        this.animeList = new ArrayList<>();
    }

    // Getting a User from JSON
    public User(String id, String userName, String profilePicture, List<String> animeList) {
        this.id = id;
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.animeList = animeList;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePicture() {
        return profilePicture.substring(9, profilePicture.length() - 2);
    }

    public List<String> getAnimeList() {
        return animeList;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void addAnime(String anime) {
        this.animeList.add(anime);
    }

}
