package kg.zukhridin.nework.domain.models

import kg.zukhridin.nework.domain.enums.StatusType

data class Event(
    val id: Int,
    val authorId: Int = 0,
    val author: String = "author",
    val authorAvatar: String? = null,
    val authorJob: String? = null,
    val content: String,
    val datetime: String,
    val published: String? = null,
    val coords: Coordinates? = null,
    val type: StatusType,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val speakerIds: List<Int> = emptyList(),
    val participatedIds: List<Int>? = emptyList(),
    val participatedByMe: Boolean,
    val attachment: kg.zukhridin.nework.domain.models.Attachment? = null,
    val link: String? = null,
    val ownedByMe: Boolean = true,
    val users: Map<Long, kg.zukhridin.nework.domain.models.UserPreview> = mapOf()
)