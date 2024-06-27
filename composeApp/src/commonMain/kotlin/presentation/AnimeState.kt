package presentation



data class AnimeState(
    val title: String ? = "",
    val englishTitle:String? = "",
    val genre: List<String?> = emptyList(),
    val coverImage:String? = "",
    val startDate:String? = "",
    val description:String? = "",
    val startYear:String?="",
    val rating:String? = "",
    val id:Int? = 0,
    val episodes:Int? =0
)
