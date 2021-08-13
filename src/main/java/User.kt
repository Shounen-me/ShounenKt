class User(val id: String, val userName: String,
           profilePicture: String, val animeList: Int, malUserName: String) {

    val profilePicture: String = profilePicture
        get() = field.substring(9, field.length - 2)

}