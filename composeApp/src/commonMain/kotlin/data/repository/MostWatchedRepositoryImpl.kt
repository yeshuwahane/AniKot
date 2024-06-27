package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.yeshuwahane.ani.TopAnimesQuery
import domain.repository.MostWatchedRepository
import io.github.aakira.napier.Napier

class MostWatchedRepositoryImpl(val apolloClient: ApolloClient) : MostWatchedRepository {

    override suspend fun getMostWatchedAnime(
        page: Int,
        perPage: Int
    ): List<TopAnimesQuery.Medium?> {
        return try {
            val response = apolloClient.query(
                TopAnimesQuery(
                    page = Optional.Present(page),
                    perPage = Optional.Present(perPage)
                )
            )
                .execute()
                .data
                ?.topAnimesPage?.media
            response ?: emptyList()
        } catch (e: ApolloNetworkException) {
            Napier.e("Network exception: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Napier.e("Exception: ${e.message}")
            emptyList()
        }
    }
}