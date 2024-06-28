package data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import com.yeshuwahane.ani.AiringAnimesQuery
import presentation.AnimeState
import presentation.home.dashboard.toDashboardUiState

class AiringAnimesPagingSource(
    private val repository:TopAiringRepositoryImpl
): PagingSource<Int, AnimeState>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeState> {
        return try {
            val currentPage = params.key ?: 1
            val animes = repository.getAiringAnimes(currentPage, 50)
                .map { it?.toDashboardUiState() }
                .filterNotNull()

            LoadResult.Page(
                data = animes,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (animes.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, AnimeState>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}