package kg.zukhridin.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kg.zukhridin.nework.converter.CoordinateConverter
import kg.zukhridin.nework.converter.PostLikeOwnerIdsConverter
import kg.zukhridin.nework.converter.PostUserConverter
import kg.zukhridin.nework.dto.*

@Entity
@TypeConverters(PostLikeOwnerIdsConverter::class, CoordinateConverter::class, PostUserConverter::class)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
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
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: Attachment? = null,
    val ownedByMe: Boolean,
    val users: Map<Long, UserPreview> = mapOf(),
) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        published,
        coords,
        link,
        likeOwnerIds,
        mentionIds,
        mentionedMe,
        likedByMe,
        attachment,
        ownedByMe,
        users
    )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.authorId,
            post.author,
            post.authorAvatar,
            post.authorJob,
            post.content,
            post.published,
            post.coords,
            post.link,
            post.likeOwnerIds,
            post.mentionIds,
            post.mentionedMe,
            post.likedByMe,
            post.attachment,
            post.ownedByMe,
            post.users
        )
    }
}