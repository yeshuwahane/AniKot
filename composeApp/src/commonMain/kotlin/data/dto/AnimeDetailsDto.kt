package data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailsDto(
    @SerialName("data")
    val data: AnimeData
)

@Serializable
data class AnimeData(
    @SerialName("Media")
    val media: AnimeMedia
)


@Serializable
data class AnimeMedia(
    @SerialName("coverImage")
    val coverImage: CoverImage,
    @SerialName("description")
    val description: String,
    @SerialName("episodes")
    val episodes: Int,
    @SerialName("genres")
    val genres: List<String>,
    @SerialName("id")
    val id: Int,
    @SerialName("season")
    val season: String,
    @SerialName("seasonYear")
    val seasonYear: Int,
    @SerialName("status")
    val status: String,
    @SerialName("title")
    val title: Title
)