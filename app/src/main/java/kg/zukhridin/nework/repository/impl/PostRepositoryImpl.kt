package kg.zukhridin.nework.repository.impl

import android.content.Context
import androidx.paging.*
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.*
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.paging.PostRemoteMediator
import kg.zukhridin.nework.repository.PostRepository
import kg.zukhridin.nework.service.APIService
import kg.zukhridin.nework.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val service: APIService,
    private val postDao: PostDao,
    private val appAuth: AppAuth
) : PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = {
            postDao.getPostsPagingSource()
        },
        remoteMediator = PostRemoteMediator(service = service, postDao = postDao)
    ).flow.map {
        it.map(PostEntity::toDto)
    }

    override suspend fun likeById(post: Post) {
        appAuth.authStateFlow.value?.id?.let { userId ->
            LikeByIdFromServerUseCase(service).likeByIdFromServer(
                post
            )
            LikeByIdFromExternalStorageUseCase(postDao).likeByIdFromExternalStorage(post, userId)
        }
    }

    override suspend fun getPostById(postId: Int): Post {
        return GetPostByIdFromServerUseCase(service).getPostByIdFromServer(postId)
    }

    override suspend fun getWall(userId: Int): Map<Int, List<Post>> {
        return GetWallFromServerUseCase(service).getWallFromServerUseCase(userId)
    }

    override suspend fun insertPostToService(post: Post): Boolean {
        return SavePostToServerUseCase(service).savePostToServer(post)
    }

    override suspend fun deletePostByIdFromServer(id: Int) {
        DeletePostByIdFromServerUseCase(service).deletePostByIdFromServer(id)
    }

    override suspend fun deletePostByIdFromExternalStorage(id: Int) {
        DeletePostByIdFromExternalStorageUseCase(postDao).deletePostByIdFromExternalStorage(id)
    }


    override suspend fun updatePostByIdFromServer(post: Post) {
        UpdatePostByIdFromServerUseCase(service).updatePostByIdFromServer(post)
    }

    override suspend fun updatePostByIdFromExternalStorage(post: Post) {
        UpdatePostByIdFromExternalStorageUseCase(postDao).updatePostByIdFromExternalStorage(post)
    }

    override suspend fun getImagesFromGallery(context: Context): ArrayList<CustomMedia> {
        return ImagesFromExternalStorageUseCase.imagesFromExternalStorage(context)
    }

    override suspend fun getVideosFromGallery(context: Context): ArrayList<CustomMedia> {
        return VideosFromExternalStorageUseCase.videosFromExternalStorage(context)
    }

    override suspend fun getAudiosFromGallery(context: Context): ArrayList<CustomMedia> {
        return AudiosFromExternalStorageUseCase.audiosFromExternalStorageUseCase(context)
    }
}