package data.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeListDto(
    @SerialName("data")
    val data: Data
)

@Serializable
data class Data(
    @SerialName("Page")
    val page: Page
)

@Serializable
data class Page(
    @SerialName("media")
    val media: List<Media>
)

@Serializable
data class Media(
    @SerialName("coverImage")
    val coverImage: CoverImage,
    @SerialName("description")
    val description: String,
    @SerialName("genres")
    val genres: List<String>,
    @SerialName("id")
    val id: Int,
    @SerialName("startDate")
    val startDate: StartDate,
    @SerialName("title")
    val title: Title
)

@Serializable
data class CoverImage(
    @SerialName("extraLarge")
    val extraLarge: String,
    @SerialName("large")
    val large: String

)

@Serializable
data class StartDate(
    @SerialName("day")
    val day: Int,
    @SerialName("month")
    val month: Int,
    @SerialName("year")
    val year: Int
)

@Serializable
data class Title(
    @SerialName("english")
    val english: String,
    @SerialName("native")
    val native: String,
    @SerialName("romaji")
    val romaji: String
)

