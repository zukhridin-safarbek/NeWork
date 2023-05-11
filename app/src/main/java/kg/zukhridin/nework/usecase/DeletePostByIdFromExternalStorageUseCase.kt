package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.database.dao.PostDao

class DeletePostByIdFromExternalStorageUseCase(private val postDao: PostDao) {
    suspend fun deletePostByIdFromExternalStorage(postId: Int) {
        postDao.deleteById(postId)
    }
}