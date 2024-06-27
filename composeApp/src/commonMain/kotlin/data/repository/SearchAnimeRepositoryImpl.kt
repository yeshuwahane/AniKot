package data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.yeshuwahane.ani.AnimeByCategoryQuery
import com.yeshuwahane.ani.GenresQuery
import com.yeshuwahane.ani.SearchAnimeQuery
import domain.repository.SearchAnimeRepository
import io.github.aakira.napier.Napier

class SearchAnimeRepositoryImpl(val apolloClient: ApolloClient): SearchAnimeRepository {

    override suspend fun searchAnimeByName(
        search: String,
        page: Int,
        perPage: Int
    ): List<SearchAnimeQuery.Medium?> {
        return try {
            val response = apolloClient
                .query(
                    SearchAnimeQuery(
                        search = Optional.Present(search),
                        page = Optional.Present(page),
                        perPage = Optional.Present(perPage)
                    )
                )
                .execute()
                .data
                ?.searchPage?.media
            response ?: emptyList()
        } catch (e: Exception) {
            Napier.e("Exception ${e.message}")
            emptyList()
        }
    }

    override suspend fun getAnimeByCategory(
        category: String,
        page: Int,
        perPage: Int
    ): List<AnimeByCategoryQuery.Medium?> {
        return try {
            val response = apolloClient
                .query(
                    AnimeByCategoryQuery(
                        category = category,
                        page = Optional.Present(page),
                        perPage = Optional.Present(perPage)
                    )
                )
                .execute()
                .data
                ?.animeByCategoryPage?.media
            response ?: emptyList()
        } catch (e: Exception) {
            Napier.e("Exception ${e.message}")
            emptyList()
        }
    }

    override suspend fun getGenresList(): List<String?> {
        return try {
            val response = apolloClient
                .query(GenresQuery())
                .execute()
                .data
                ?.genreCollection
            response ?: emptyList()
        } catch (e: Exception) {
            Napier.e("Exception ${e.message}")
            emptyList()
        }
    }
}
