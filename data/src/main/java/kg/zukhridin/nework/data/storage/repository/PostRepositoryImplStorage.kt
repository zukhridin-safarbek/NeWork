package kg.zukhridin.nework.data.storage.repository

import androidx.paging.*
import kg.zukhridin.nework.data.entity.PostEntity
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.data.paging.PostRemoteMediator
import kg.zukhridin.nework.data.service.requests.PostAPIService
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.domain.storage.repositories.PostRepositoryStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImplStorage @Inject constructor(
    postService: PostAPIService,
    private val postDao: PostDao,
    private val appAuth: AppAuth
) : PostRepositoryStorage {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = {
            postDao.getPostsPagingSource()
        },
        remoteMediator = PostRemoteMediator(service = postService, postDao = postDao)
    ).flow.map {
        it.map(PostEntity::toDto)
    }

    override suspend fun getPostById(postId: Int): Post {
        return postDao.getPostById(postId).toDto()
    }

    override suspend fun insertPost(post: Post) {
        postDao.insertPost(PostEntity.fromDto(post))
    }

    override suspend fun updatePost(post: Post) {
        postDao.updatePostById(PostEntity.fromDto(post))
    }

    override suspend fun likeById(post: Post) {
        val list = post.likeOwnerIds.toMutableList()
        appAuth.authStateFlow.value?.id?.let { list.add(it) }
        postDao.likedById(PostEntity.fromDto(post.copy(likeOwnerIds = list, likedByMe = true)))
    }

    override suspend fun dislikeById(post: Post) {
        val list = post.likeOwnerIds.toMutableList()
        appAuth.authStateFlow.value?.id?.let { list.remove(it) }
        postDao.likedById(PostEntity.fromDto(post.copy(likeOwnerIds = list, likedByMe = false)))
    }

    override suspend fun deletePostById(id: Int) {
        postDao.deleteById(id)
    }

    override suspend fun clearAllPosts() {
        postDao.clearAllPosts()
    }
}