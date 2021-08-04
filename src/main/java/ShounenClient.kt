import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class ShounenClient(val token: String) {

    /**
     * Gets a user from the API
     * @param searchParameter = Discord ID or user name (with which the user is registered in the API)
     * @return User
     */
    open fun getUser(searchParameter: String?): User? {
        try {
            val con = getConnection(searchParameter, "GET")
            val reader = BufferedReader(InputStreamReader(con!!.inputStream))
            val json = JSONObject(reader.readLine())
            return User(
                json.getString("id"),
                json.getString("userName"),
                json.getString("profilePicture"),
                mutableListOf(json.getString("animeList"))
            )
        } catch (ignored: java.lang.Exception) {
            println(ignored)
        }
        return null
    }


    /**
     * Posts a user to the API
     * @param user = User with ID and (current) user name (e.g. bots gets the id and user name and executes this method)
     * @return boolean: false if user already exists, true if User was successfully stored in the API
     */
    open fun postUser(user: User): Boolean {
        try {
            val con = getConnection(String.format("%s/%s", user.id, user.userName), "POST")
            con!!.connect()
            return con.responseCode == 201
        } catch (ignored: java.lang.Exception) {
        }
        return false
    }


    /**
     * Adds specified anime to a user's anime list
     * @param id = Discord ID (which can be accessed by the bot from a user's message)
     * @param name = name of the anime (whitespaces will be seperated by '+', e.g. One Piece = One+Piece)
     * @return boolean: true if anime was added successfully, false if not
     */
    open fun addAnime(id: String?, name: String): Boolean {
        try {
            var anime = name
            anime = anime.trim { it <= ' ' }.replace(" ", "+")
            val con = getConnection(String.format("%s/anime/%s", id, anime), "POST")
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: java.lang.Exception) {
        }
        return false
    }


    /**
     * Deletes a user from the API (initiated by the user on Discord)
     * @param id = Discord ID (which can be accessed by a bot from a user's message)
     * @return boolean: true if user was deleted successfully, else false
     */
    open fun deleteUser(id: String?): Boolean {
        try {
            val con = getConnection(id, "DELETE")
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: java.lang.Exception) {
        }
        return false
    }

    /**
     * Gets Http Connection
     * @param s = {name/id} + optional /profile, /anime, ... Endpoints
     * @param request = GET, POST or DELETE
     * @return boolean: false if user already exists, true if User was successfully stored in the API
     */
    open fun getConnection(s: String?, request: String?): HttpURLConnection? {
        try {
            val url = URL(String.format("https://shounen.asuha.dev/user/%s/%s", token, s))
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = request
            return con
        } catch (ignored: Exception) { }
        return null
    }
}