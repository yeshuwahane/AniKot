package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.yeshuwahane.ani.AiringAnimesQuery
import com.yeshuwahane.ani.AnimeDetailQuery
import com.yeshuwahane.ani.AnimeListQuery
import com.yeshuwahane.ani.TopAnimesQuery
import com.yeshuwahane.ani.TopRatedAnimesQuery
import com.yeshuwahane.ani.type.MediaSeason
import com.yeshuwahane.ani.type.MediaStatus
import data.utils.DataResource
import data.utils.apiCall
import domain.repository.AnimeRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText


class AnimeRepositoryImpl(val apolloClient: ApolloClient) : AnimeRepository {

    override suspend fun getNewAnime(page: Int, perPage: Int): List<AnimeListQuery.Medium?> {
        return try {
            val response = apolloClient
                .query(
                    AnimeListQuery(
                        page = Optional.Present(page),
                        perPage = Optional.Present(perPage)
                    )
                )
                .execute()
                .data
                ?.pageInfo?.media

            Napier.v("Api Called")
            response ?: emptyList()
        } catch (e: ApolloNetworkException) {
            Napier.e("Network exception: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Napier.e("Exception: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getTopRatedAnimes(page: Int, perPage: Int): List<TopRatedAnimesQuery.Medium?> {
        return try {
            val response = apolloClient.query(
                TopRatedAnimesQuery(
                    page = Optional.Present(page),
                    perPage = Optional.Present(perPage)
                )
            )
                .execute()
                .data
                ?.topRatedAnimesPage?.media

            response ?: emptyList()
        } catch (e: ApolloNetworkException) {
            Napier.e("Network exception: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Napier.e("Exception: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getAnimeDetails(mediaId: Int): AnimeDetailQuery.MediaInfo {
        return try {
            val response = apolloClient.query(AnimeDetailQuery(mediaId = mediaId))
                .execute()
                .data
                ?.mediaInfo

            response ?: AnimeDetailQuery.MediaInfo(
                0,
                title = AnimeDetailQuery.Title("", "", ""),
                description = "",
                coverImage = AnimeDetailQuery.CoverImage("", ""),
                episodes = 0,
                season = MediaSeason.UNKNOWN__,
                seasonYear = 0,
                status = MediaStatus.UNKNOWN__,
                genres = emptyList(),
                averageScore = 0,
                trailer = AnimeDetailQuery.Trailer("", ""),
                studios = AnimeDetailQuery.Studios(edges = emptyList())
            )
        } catch (e: ApolloNetworkException) {
            Napier.e("Network exception: ${e.message}")
            AnimeDetailQuery.MediaInfo(
                0,
                title = AnimeDetailQuery.Title("", "", ""),
                description = "",
                coverImage = AnimeDetailQuery.CoverImage("", ""),
                episodes = 0,
                season = MediaSeason.UNKNOWN__,
                seasonYear = 0,
                status = MediaStatus.UNKNOWN__,
                genres = emptyList(),
                averageScore = 0,
                trailer = AnimeDetailQuery.Trailer("", ""),
                studios = AnimeDetailQuery.Studios(edges = emptyList())
            )
        } catch (e: Exception) {
            Napier.e("Exception: ${e.message}")
            AnimeDetailQuery.MediaInfo(
                0,
                title = AnimeDetailQuery.Title("", "", ""),
                description = "",
                coverImage = AnimeDetailQuery.CoverImage("", ""),
                episodes = 0,
                season = MediaSeason.UNKNOWN__,
                seasonYear = 0,
                status = MediaStatus.UNKNOWN__,
                genres = emptyList(),
                averageScore = 0,
                trailer = AnimeDetailQuery.Trailer("", ""),
                studios = AnimeDetailQuery.Studios(edges = emptyList())
            )
        }
    }
}