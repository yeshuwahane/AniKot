package presentation.home.searchanime

import com.yeshuwahane.ani.AnimeByCategoryQuery
import com.yeshuwahane.ani.SearchAnimeQuery
import data.utils.DataResource
import presentation.AnimeState

data class SearchUiState(
    val searchUiState: DataResource<List<AnimeState?>> = DataResource.initial(),
    val genreUiState: DataResource<List<String?>> = DataResource.initial()
)




fun SearchAnimeQuery.Medium.toSearchUiState():AnimeState{
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString(),
        id = id
    )
}

fun AnimeByCategoryQuery.Medium.toSearchUiState():AnimeState{
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString(),
        id = id
    )
}
