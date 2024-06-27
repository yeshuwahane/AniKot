package di

import data.repository.AnimeRepositoryImpl
import data.repository.MostWatchedRepositoryImpl
import data.repository.SearchAnimeRepositoryImpl
import data.repository.TopAiringRepositoryImpl
import org.koin.dsl.module
import presentation.animegrids.AnimeGridViewModel
import presentation.home.animedetails.DetailViewModel
import presentation.home.dashboard.DashboardViewModel
import presentation.home.searchanime.SearchViewModel


fun commonModule () =  module {
    single {
        AnimeRepositoryImpl(get())
    }
    single {
        TopAiringRepositoryImpl(get())
    }
    single {
        MostWatchedRepositoryImpl(get())
    }
    single {
        SearchAnimeRepositoryImpl(get())
    }
    single {
        DashboardViewModel(get(),get(),get())
    }
    single {
        DetailViewModel(get())
    }
    single {
        AnimeGridViewModel(get(),get(),get())
    }
    single {
        SearchViewModel(get())
    }

}