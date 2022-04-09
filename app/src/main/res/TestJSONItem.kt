
import com.google.gson.annotations.SerializedName

data class TestJSONItem(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)