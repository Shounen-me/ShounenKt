class User(val id: String, val userName: String,
           profilePicture: String, val animeList: MutableList<String> = mutableListOf()) {

    val profilePicture: String = profilePicture
        get() = field.substring(9, field.length - 2)

    fun addAnime(anime: String) = this.animeList.add(anime)

}