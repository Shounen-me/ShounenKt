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
            val con = getConnection(searchParameter, "GET", "user")
            val reader = BufferedReader(InputStreamReader(con!!.inputStream))
            val json = JSONObject(reader.readLine())
            return User(
                json.getString("id"),
                json.getString("userName"),
                json.getString("profilePicture"),
                json.getInt("animeList"),
                json.getString("malUsername")
            )
        } catch (ignored: Exception) {
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
            val con = getConnection(String.format("%s/%s", user.id, user.userName), "POST", "user")
            con!!.connect()
            return con.responseCode == 201
        } catch (ignored: Exception) {
        }
        return false
    }


    /**
     * Adds specified anime to a user's anime list
     * @param id = Discord ID
     * @param name = name of the anime (whitespaces will be seperated by '+', e.g. One Piece = One+Piece)
     * @return boolean: true if anime was added successfully, false if not
     */
    open fun addAnime(id: String?, name: String): Boolean {
        try {
            var anime = name
            anime = anime.trim { it <= ' ' }.replace(" ", "+")
            val con = getConnection(String.format("%s/anime/%s", id, anime), "POST", "user")
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: Exception) {
        }
        return false
    }


    /**
     * Deletes a user from the API (initiated by the user on Discord)
     * @param id = Discord ID
     * @return boolean: true if user was deleted successfully, else false
     */
    open fun deleteUser(id: String?): Boolean {
        try {
            val con = getConnection(id, "DELETE", "user")
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: java.lang.Exception) {
        }
        return false
    }


    /**
     * Initiates the sync process of a user to the MAL API (to sync their account with the shounen.me API)
     * @param id = Discord ID
     * @return MutableList<String>: [0] contains verifier (to be stored), [1] contains the link to the redirect
     */
    open fun malSync(id: String): MutableList<String> {
        try {
            val con = getConnection(id, "GET", "mal")
            con!!.connect()
            val response = con.responseMessage
            val params = mutableListOf<String>()
            params.add(response.split(",")[0])
            params.add(response.split(",")[1])
            return params
        } catch (ignored: Exception) {}
        return mutableListOf()
    }



    /**
     * Gets Http Connection
     * @param s = {name/id} + optional /profile, /anime, ...
     * @param request = GET, POST or DELETE
     * @param endpoint = 'user' or 'mal'
     * @return boolean: false if user already exists, true if User was successfully stored in the API
     */
    open fun getConnection(s: String?, request: String?, endpoint: String?): HttpURLConnection? {
        try {
            val url = URL(String.format("https://shounen.asuha.dev/%s/%s/%s", endpoint, token, s))
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = request
            return con
        } catch (ignored: Exception) { }
        return null
    }


}