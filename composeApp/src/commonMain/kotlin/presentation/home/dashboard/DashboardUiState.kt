package presentation.home.dashboard

import com.yeshuwahane.ani.AiringAnimesQuery
import com.yeshuwahane.ani.AnimeListQuery
import com.yeshuwahane.ani.TopAnimesQuery
import com.yeshuwahane.ani.TopRatedAnimesQuery
import data.utils.DataResource
import presentation.AnimeState


data class DashboardUiState(
    val dashboardUiState: DataResource<List<AnimeState?>> = DataResource.initial(),
    val airingUiState: DataResource<List<AnimeState?>> = DataResource.initial(),
    val topAnimesUiState: DataResource<List<AnimeState?>> = DataResource.initial()
    )





fun AnimeListQuery.Medium.toDashboardUiState(): AnimeState {
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString()
    )
}

fun AiringAnimesQuery.Medium.toDashboardUiState(): AnimeState{
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString()
    )
}

fun TopAnimesQuery.Medium.toDashboardUiState(): AnimeState{
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString()
        )
}

fun TopRatedAnimesQuery.Medium.toDashboardUiState(): AnimeState{
    return AnimeState(
        title = title?.native,
        englishTitle = title?.english,
        genre = genres.orEmpty(),
        coverImage = coverImage?.large,
        startDate = startDate.toString(),
        description = description,
        startYear = startDate?.year.toString()
    )
}


