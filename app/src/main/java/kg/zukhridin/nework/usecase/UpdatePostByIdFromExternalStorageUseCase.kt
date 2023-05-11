package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.entity.PostEntity

class UpdatePostByIdFromExternalStorageUseCase(private val postDao: PostDao) {
    suspend fun updatePostByIdFromExternalStorage(post: Post) {
        postDao.updatePostById(PostEntity.fromDto(post))
    }
}