package kg.zukhridin.nework.dto

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String, // date-time
    val coords: Coordinates? = null,
    val link: String?,
    val likeOwnerIds: List<Int> = emptyList(),
    val mentionIds: List<Int> = emptyList(),
    val mentionedByMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean,
    val users: Map<Long, UserPreview> = mapOf(),
)