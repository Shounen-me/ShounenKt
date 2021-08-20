import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class ShounenClient(private val token: String) {

    /**
     * Gets a user from the API
     * @param searchParameter = Discord ID or user name (with which the user is registered in the API)
     * @return User
     */
    open fun getUser(searchParameter: String): User? {
        try {
            val con = if (searchParameter.toLongOrNull() == null)
                getUserConnection(Request_Types.GET, name = searchParameter)
            else getUserConnection(Request_Types.GET, id = searchParameter)
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
            val con = getUserConnection(Request_Types.POST, user.id, user.userName)
            con!!.connect()
            return con.responseCode == 201
        } catch (ignored: Exception) {
        }
        return false
    }


    /**
     * Adds specified anime to a user's anime list
     * @param discordID = Discord ID
     * @param id = unique ID of the anime on MAL
     * @param episodes = count of episodes watched
     * @return boolean: true if anime was added successfully, false if not
     */
    open fun postAnime(discordID: String?, id: String, episodes: Int): Boolean {
        try {
            val con = getMALConnection("anime", Request_Types.POST, discordID, id, count = episodes)
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: Exception) {
        }
        return false
    }

    /**
     * Adds specified manga to a user's manga list
     * @param discordID = Discord ID
     * @param id = unique ID of the anime on MAL
     * @param chapters = count of chapters read -> optional
     * @return boolean: true if anime was added successfully, false if not
     */
    open fun postManga(discordID: String?, id: String, chapters: Int = 0): Boolean {
        try {
            val con = if (chapters != 0) getMALConnection("manga", Request_Types.POST, discordID, id, count = chapters)
            else getMALConnection("manga", Request_Types.POST, discordID, id)
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
            val con = getUserConnection(Request_Types.DELETE, id)
            con!!.connect()
            return con.responseCode == 200
        } catch (ignored: java.lang.Exception) {
        }
        return false
    }


    /**
     * Initiates the sync process of a user to the MAL API (to sync their account with the shounen.me API)
     * @param id = Discord ID
     * @return String: Redirect link for MAL authorization
     */
    open fun malSync(id: String): String {
        try {
            val con = getMALConnection("sync", Request_Types.GET, discordID = id)
            if (con != null) {
                println(con.requestMethod)
            }
            con!!.connect()
            return BufferedReader(InputStreamReader(con.inputStream)).readLine()
        } catch (ignored: Exception) {}
        return ""
    }



    private fun getUserConnection(requestTypes: Request_Types, id: String? = "", name: String? = "", profilePicture: String = ""):
            HttpURLConnection? {
        try {
            val url = when (requestTypes) {
                Request_Types.POST -> {
                    if (profilePicture == "")
                        URL( Urls.localhost + "/user/$token/$id/$name")
                    else
                        URL(Urls.localhost + "/user/profile/$token/$id")
                }
                Request_Types.GET -> {
                    if (id == "")
                        URL(Urls.localhost + "/user/$name")
                    else
                        URL(Urls.localhost + "/user/$id")
                }
                Request_Types.DELETE -> URL(Urls.localhost + "user/$id")
            }
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = requestTypes.toString()
            return con
        } catch (ignored: Exception) { }
        return null
    }

    private fun getMALConnection(query: String, requestTypes: Request_Types, discordID: String? = "", id: String? = "", info_type: String? = "", count: Int = 0): HttpURLConnection? {
        try {
            val url: URL = when(query) {
                "sync" -> URL(Urls.localhost + "mal/sync/$discordID/init")
                "info" -> URL(Urls.localhost + "mal/info/$discordID/$info_type/$id")
                "anime" -> URL(Urls.localhost + "mal/anime/$token/$discordID/$id/$count")
                else -> URL(Urls.localhost + "mal/manga/$token/$discordID/$id/$count")
            }
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = requestTypes.toString()
            return con
        } catch (ignored: Exception) { }
        return null
    }


}