package kg.zukhridin.nework.domain.models

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String, // date-time
    val coords: kg.zukhridin.nework.domain.models.Coordinates? = null,
    val link: String?,
    val likeOwnerIds: List<Int> = emptyList(),
    val mentionIds: List<Int> = emptyList(),
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: kg.zukhridin.nework.domain.models.Attachment? = null,
    val ownedByMe: Boolean,
    val users: Map<Long, kg.zukhridin.nework.domain.models.UserPreview> = mapOf(),
)