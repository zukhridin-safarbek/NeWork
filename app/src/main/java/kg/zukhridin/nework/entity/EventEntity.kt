package kg.zukhridin.nework.entity

import androidx.room.*
import kg.zukhridin.nework.converter.CoordinateConverter
import kg.zukhridin.nework.converter.PostLikeOwnerIdsConverter
import kg.zukhridin.nework.converter.PostUserConverter
import kg.zukhridin.nework.dto.Attachment
import kg.zukhridin.nework.dto.Coordinates
import kg.zukhridin.nework.dto.EventDto
import kg.zukhridin.nework.dto.UserPreview
import kg.zukhridin.nework.utils.StatusType

@Entity
@TypeConverters(PostLikeOwnerIdsConverter::class, CoordinateConverter::class, PostUserConverter::class)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String? = null,
    val authorJob: String? = null,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates? = null,
    @ColumnInfo(name = "status_type")
    val type: StatusType,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean,
    val speakerIds: List<Int> = emptyList(),
    val participatedIds: List<Int> = emptyList(),
    val participatedByMe: Boolean,
    @Embedded
    val attachment: Attachment? = null,
    val link: String? = null,
    val ownedByMe: Boolean,
    val users: Map<Long, UserPreview> = mapOf()
) {
    fun toDto() = EventDto(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        datetime,
        published,
        coords,
        type,
        likeOwnerIds,
        likedByMe,
        speakerIds,
        participatedIds,
        participatedByMe,
        attachment,
        link,
        ownedByMe,
        users
    )

    companion object {
        fun fromDto(eventDto: EventDto) = EventEntity(
            eventDto.id,
            eventDto.authorId,
            eventDto.author,
            eventDto.authorAvatar,
            eventDto.authorJob,
            eventDto.content,
            eventDto.datetime,
            eventDto.published,
            eventDto.coords,
            eventDto.type,
            eventDto.likeOwnerIds,
            eventDto.likedByMe,
            eventDto.speakerIds,
            eventDto.participatedIds,
            eventDto.participatedByMe,
            eventDto.attachment,
            eventDto.link,
            eventDto.ownedByMe,
            eventDto.users
        )
    }
}