package kg.zukhridin.nework.repository.impl

import androidx.paging.*
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.paging.PostRemoteMediator
import kg.zukhridin.nework.repository.PostRepository
import kg.zukhridin.nework.service.APIService
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

    private val posts = mutableListOf<Post>()
    override suspend fun getPosts(): List<Post>? {
        val response = service.getPosts()
        return if (response.isSuccessful) {
            response.body()?.map {
                posts.add(it)
//                postDao.insertPost(PostEntity.fromDto(it))
            }
            response.body()
        } else {
            response.body()
        }
    }

    override suspend fun likeById(post: Post) {
        val list = post.likeOwnerIds.toMutableList()
        if (!post.likedByMe) {
            service.likeById(post.id)
            list.add(appAuth.authStateFlow.value?.id!!)
            postDao.likedById(
                PostEntity.fromDto(
                    post.copy(
                        likeOwnerIds = list,
                        likedByMe = true
                    )
                )
            )
        } else {
            list.remove(appAuth.authStateFlow.value?.id!!)
            postDao.likedById(
                PostEntity.fromDto(
                    post.copy(
                        likeOwnerIds = list,
                        likedByMe = false
                    )
                )
            )
            service.dislikeById(post.id)
        }
    }

    override suspend fun insertPostToService(post: Post) {
        val response = service.insertPost(post)
        if (!response.isSuccessful) {
            ApiResult.Error<Unit>(response.code().toString(), response.message())
        }
        ApiResult.Success<Unit>(response.code().toString(), response.body() ?: Unit)
    }
}