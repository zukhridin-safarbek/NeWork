package kg.zukhridin.nework.dto

import kg.zukhridin.nework.utils.StatusType

data class EventDto(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates? = null,
    val type: StatusType,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean,
    val speakerIds: List<Int> = emptyList(),
    val participatedIds: List<Int> = emptyList(),
    val participatedByMe: Boolean,
    val attachment: Attachment? = null,
    val link: String?,
    val ownedByMe: Boolean,
    val users: Map<Long, UserPreview> = mapOf()
)