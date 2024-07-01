package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.yeshuwahane.ani.AiringAnimesQuery
import domain.repository.TopAiringRepository
import io.github.aakira.napier.Napier

class TopAiringRepositoryImpl(val apolloClient: ApolloClient) : TopAiringRepository {
    override suspend fun getAiringAnimes(page: Int, perPage: Int): List<AiringAnimesQuery.Medium> {
        return try {
            val response = apolloClient.query(
                AiringAnimesQuery(
                    page = Optional.Present(page),
                    perPage = Optional.Present(perPage)
                )
            )
                .execute()
                .data
                ?.airingPage?.media
            response?.filterNotNull() ?: emptyList()
        } catch (e: ApolloNetworkException) {
            Napier.e("Network exception: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Napier.e("Exception: ${e.message}")
            emptyList()
        }
    }
}

