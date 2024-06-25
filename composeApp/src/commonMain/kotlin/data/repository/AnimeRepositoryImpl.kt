package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.yeshuwahane.ani.AiringAnimesQuery
import com.yeshuwahane.ani.AnimeListQuery
import com.yeshuwahane.ani.TopAnimesQuery
import com.yeshuwahane.ani.TopRatedAnimesQuery
import data.utils.DataResource
import data.utils.apiCall
import domain.repository.AnimeRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class AnimeRepositoryImpl(val apolloClient: ApolloClient, val httpClient: HttpClient) :
    AnimeRepository {

    override suspend fun getNewAnime(page: Int, perPage: Int): List<AnimeListQuery.Medium?> {
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
        return response?: emptyList()
    }

    override suspend fun getTopRatedAnimes(
        page: Int,
        perPage: Int
    ): List<TopRatedAnimesQuery.Medium?> {
        val response = apolloClient.query(TopRatedAnimesQuery(
            page = Optional.Present(page),
            perPage = Optional.Present(perPage)
        ))
            .execute()
            .data
            ?.topRatedAnimesPage?.media
        return response ?: emptyList()

    }
}