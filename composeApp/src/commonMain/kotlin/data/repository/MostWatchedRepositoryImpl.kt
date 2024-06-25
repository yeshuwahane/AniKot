package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.yeshuwahane.ani.TopAnimesQuery
import domain.repository.MostWatchedRepository

class MostWatchedRepositoryImpl(val apolloClient: ApolloClient):MostWatchedRepository {

    override suspend fun getMostWatchedAnime(
        page: Int,
        perPage: Int
    ): List<TopAnimesQuery.Medium?> {

        val response = apolloClient.query(
            TopAnimesQuery(
                page = Optional.Present(page),
                perPage = Optional.Present(perPage)
            )
        )
            .execute()
            .data
            ?.topAnimesPage?.media
        return response ?: emptyList()
    }
}