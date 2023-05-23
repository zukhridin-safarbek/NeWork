package kg.zukhridin.nework.data.entity

import androidx.room.*
import kg.zukhridin.nework.data.converter.CoordinateConverter
import kg.zukhridin.nework.data.converter.PostLikeOwnerIdsConverter
import kg.zukhridin.nework.data.converter.PostUserConverter
import kg.zukhridin.nework.domain.models.Attachment
import kg.zukhridin.nework.domain.models.Coordinates
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.enums.StatusType
import kg.zukhridin.nework.domain.models.UserPreview

@Entity
@TypeConverters(
    PostLikeOwnerIdsConverter::class,
    CoordinateConverter::class,
    PostUserConverter::class
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String? = null,
    val authorJob: String? = null,
    val content: String,
    val datetime: String,
    val published: String?,
    val coords: Coordinates? = null,
    @ColumnInfo(name = "status_type")
    val type: StatusType,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean,
    val speakerIds: List<Int> = emptyList(),
    val participatedIds: List<Int>? = emptyList(),
    val participatedByMe: Boolean,
    @Embedded
    val attachment: Attachment? = null,
    val link: String? = null,
    val ownedByMe: Boolean,
    val users: Map<Long, UserPreview> = mapOf(),
    val seen: Boolean = false
) {
    fun toDto() = Event(
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
        fun fromDto(event: Event) = EventEntity(
            event.id,
            event.authorId,
            event.author,
            event.authorAvatar,
            event.authorJob,
            event.content,
            event.datetime,
            event.published,
            event.coords,
            event.type,
            event.likeOwnerIds,
            event.likedByMe,
            event.speakerIds,
            event.participatedIds,
            event.participatedByMe,
            event.attachment,
            event.link,
            event.ownedByMe,
            event.users
        )
    }
}