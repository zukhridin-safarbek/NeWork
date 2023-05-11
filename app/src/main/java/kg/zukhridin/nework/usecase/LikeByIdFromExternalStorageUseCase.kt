package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.entity.PostEntity

class LikeByIdFromExternalStorageUseCase(private val postDao: PostDao) {
    suspend fun likeByIdFromExternalStorage(post: Post, userId: Int){
        val list = post.likeOwnerIds.toMutableList()
        if (!post.likedByMe) {
            list.add(userId)
            postDao.likedById(
                PostEntity.fromDto(
                    post.copy(
                        likeOwnerIds = list,
                        likedByMe = true
                    )
                )
            )
        } else {
            list.remove(userId)
            postDao.likedById(
                PostEntity.fromDto(
                    post.copy(
                        likeOwnerIds = list,
                        likedByMe = false
                    )
                )
            )
        }
    }
}