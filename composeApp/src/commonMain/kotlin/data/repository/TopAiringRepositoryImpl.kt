package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.yeshuwahane.ani.AiringAnimesQuery
import domain.repository.TopAiringRepository

class TopAiringRepositoryImpl(val apolloClient: ApolloClient):TopAiringRepository {
    override suspend fun getAiringAnimes(page: Int, perPage: Int): List<AiringAnimesQuery.Medium?> {
        val response = apolloClient.query(AiringAnimesQuery(
            page = Optional.Present(page),
            perPage = Optional.Present(perPage)
        ))
            .execute()
            .data
            ?.airingPage?.media
        return response?: emptyList()
    }
}