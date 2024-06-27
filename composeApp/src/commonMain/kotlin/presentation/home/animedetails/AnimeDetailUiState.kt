package presentation.home.animedetails

import com.yeshuwahane.ani.AnimeDetailQuery
import data.utils.DataResource

data class AnimeDetailUiState(
    val animeUiState: DataResource<AnimeDetailState> = DataResource.initial(),
    )


data class AnimeDetailState(
    val title: String? = "",
    val englishTitle: String? = "",
    val description: String? = "",
    val coverImage: String? = "",
    val episodes: Int? = 0,
    val season: String? = "",
    val seasonYear: Int? = 0,
    val status: String? = "",
    val genres: List<String?>? = emptyList(),
    val rating: String? = "",
    val trailer:String?="",
    val studios:String?="",
    val id:Int? = 0

    )

fun AnimeDetailQuery.MediaInfo.toAnimeDetailState(): AnimeDetailState{
    return AnimeDetailState(
        title = title?.native,
        englishTitle = title?.english,
        description = description,
        coverImage = coverImage?.large,
        episodes = episodes,
        season = season?.name,
        seasonYear = seasonYear,
        status = status?.name,
        genres = genres,
        rating = averageScore.toString(),
        trailer = trailer?.site,
        studios = studios?.edges?.get(0)?.node?.name,
        id = id
    )
}
