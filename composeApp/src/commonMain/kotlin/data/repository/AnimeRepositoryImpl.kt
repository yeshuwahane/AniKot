package data.repository

import com.apollographql.apollo3.ApolloClient
import com.yeshuwahane.ani.AnimeListQuery
import data.dto.AnimeDetailsDto
import data.dto.AnimeListDto
import data.utils.DataResource
import data.utils.apiCall
import domain.repository.AnimeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AnimeRepositoryImpl(val networkClient: HttpClient, val apolloClient: ApolloClient) :
    AnimeRepository {


    override suspend fun getDashboardAnime(): DataResource<List<AnimeListDto>> {
        return apiCall {
            return apolloClient
                .query()
                .execute()
                .data
                ?.pageInfo
                ?.media




        }
    }

    override suspend fun getAnimeByID(id: Int): DataResource<AnimeDetailsDto> {
        TODO("Not yet implemented")
    }
}