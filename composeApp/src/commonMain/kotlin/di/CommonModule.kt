package di

import data.repository.AnimeRepositoryImpl
import data.repository.MostWatchedRepositoryImpl
import data.repository.TopAiringRepositoryImpl
import org.koin.dsl.module
import presentation.home.dashboard.DashboardViewModel


fun commonModule () =  module {
    single {
        AnimeRepositoryImpl(get(),get())
    }
    single {
        TopAiringRepositoryImpl(get())
    }
    single {
        MostWatchedRepositoryImpl(get())
    }
    single {
        DashboardViewModel(get(),get(),get())
    }

}