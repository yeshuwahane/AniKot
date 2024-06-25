package di

import com.apollographql.apollo3.ApolloClient
import data.Constant
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
fun networkModule() = module {

    single {
        ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co")
            .build()
            .also {
                Napier.base(DebugAntilog())
                Napier.d("ApolloClient, GraphQl")
            }

    }

    single {
        HttpClient {
            defaultRequest {
                url(Constant.BASE_URL)
            }
            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }.also { Napier.base(DebugAntilog()) }

            install(HttpTimeout) {
                socketTimeoutMillis = 10_000
                requestTimeoutMillis = 10_0000
            }

        }
    }



}