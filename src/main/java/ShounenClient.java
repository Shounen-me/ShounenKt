import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;

public class ShounenClient {

    private final String token;

    public ShounenClient(String token) {
        this.token = token;
    }

    /**
     * Gets a user from the API
     * @param searchParameter = Discord ID or User Name (with which the user is registered in the API)
     * @return User
     */
    public User getUser(String searchParameter) {
        try {
            HttpURLConnection con = getConnection(searchParameter, "GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            JSONObject json = new JSONObject(reader.readLine());
            return new User(json.getString("id"), json.getString("userName"), json.getString("profilePicture"),
                    Collections.singletonList(json.getString("animeList")));
        } catch (Exception ignored) {
            System.out.println(ignored);
        }
        return null;
    }


    /**
     * Posts a user to the API
     * @param user = User with ID and (current) user name (e.g. bots gets the id and user name and executes this method)
     * @return boolean: false if user already exists, true if User was successfully stored in the API
     */
    public boolean postUser(User user) {
        try {
            HttpURLConnection con = getConnection(String.format("%s/%s", user.getId(), user.getUserName()), "POST");
            con.connect();
            return con.getResponseCode() == 201;
        } catch (Exception ignored) {}
        return false;
    }


    /**
     * Adds specified anime to a user's anime list
     * @param id = Discord ID (which can be accessed by the bot from a user's message)
     * @param anime = name of the anime (whitespaces will be seperated by '+', e.g. One Piece = One+Piece)
     * @return boolean: true if anime was added successfully, false if not
     */
    public boolean addAnime(String id, String anime) {
        try {
            anime = anime.trim().replace(" ", "+");
            HttpURLConnection con = getConnection(String.format("%s/anime/%s", id, anime), "POST");
            con.connect();
            return con.getResponseCode() == 200;
        } catch (Exception ignored) {}
        return false;
    }


    /**
     * Deletes a user from the API (initiated by the user on Discord)
     * @param id = Discord ID (which can be accessed by a bot from a user's message)
     * @return boolean: true if user was deleted successfully, else false
     */
    public boolean deleteUser(String id) {
        try {
            HttpURLConnection con = getConnection(id, "DELETE");
            con.connect();
            return con.getResponseCode() == 200;
        } catch (Exception ignored) {}

        return false;
    }


    /**
     * Gets Http Connection
     * @param s = {name/id} + optional /profile, /anime, ... Endpoints
     * @param request = GET, POST or DELETE
     * @return boolean: false if user already exists, true if User was successfully stored in the API
     */
    public HttpURLConnection getConnection(String s, String request) {
        try {
            URL url = new URL(String.format("https://shounen.asuha.dev/user/%s/%s", token, s));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(request);
            return con;
        } catch (Exception ignored) {}
        return null;
    }


}
