package data.utils

import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend inline fun <reified T : Any> apiCall(apiCall: () -> HttpResponse): DataResource<T> {
    return try {
        val response: HttpResponse = apiCall.invoke()

        if (response.status.value in 200..299) {
            val responseBody = response.body<T>()
            DataResource.success(responseBody)
        } else if(response.status.value == 401){
            DataResource.error(error = Throwable("401"), data = null)
        }
        else {
            DataResource.error(error = Throwable(response.bodyAsText()), data = response.body())
        }
    } catch (exception: Exception) {
        Napier.d("exception: ${exception.message}")
        DataResource.error(error = exception.cause, data = null)
    }
}