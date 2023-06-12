package kg.zukhridin.nework.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.data.storage.database.AppAuth
import kg.zukhridin.nework.domain.enums.MediaType
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.MediaModel
import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.domain.service.repositories.PostRepositoryService
import kg.zukhridin.nework.domain.service.repositories.WallRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.PostRepositoryStorage
import kg.zukhridin.nework.domain.storage.repositories.WallRepositoryStorage
import kg.zukhridin.nework.presentation.utils.getAudiosFromGallery
import kg.zukhridin.nework.presentation.utils.getImagesFromGallery
import kg.zukhridin.nework.presentation.utils.getVideosFromGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repositoryService: PostRepositoryService,
    private val repositoryStorage: PostRepositoryStorage,
    private val wallRepositoryStorage: WallRepositoryStorage,
    private val wallRepositoryService: WallRepositoryService,
    appAuth: AppAuth,
) : ViewModel() {
    val data: Flow<PagingData<Post>> = appAuth.authStateFlow.flatMapLatest { myId ->
        repositoryStorage.data.map { pagingData ->
            pagingData.map { post ->
                post.copy(ownedByMe = post.authorId == myId?.id)
            }
        }
    }.flowOn(Dispatchers.Default)
    private val _mediasFromExternalStorage = MutableLiveData<ArrayList<MediaModel>>()
    val mediasFromExternalStorage: LiveData<ArrayList<MediaModel>>
        get() = _mediasFromExternalStorage


    fun likeById(post: Post) {
        viewModelScope.launch {
            if (post.likedByMe) {
                repositoryStorage.dislikeById(post)
                repositoryService.dislikeById(post)
            } else {
                repositoryStorage.likeById(post)
                repositoryService.likeById(post)
            }
        }
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        try {
            repositoryStorage.deletePostById(id)
            repositoryService.deletePostById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateById(post: Post) = viewModelScope.launch {
        try {
            repositoryStorage.updatePost(post)
            repositoryService.updatePostById(post)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun mediasFromGallery(context: Context, mediaType: MediaType) {
        viewModelScope.launch {
            when (mediaType) {
                MediaType.IMAGE -> {
                    val medias = context.getImagesFromGallery()
                    _mediasFromExternalStorage.value = medias
                }
                MediaType.VIDEO -> {
                    val medias = context.getVideosFromGallery()
                    _mediasFromExternalStorage.value = medias
                }
                MediaType.AUDIO -> {
                    val medias = context.getAudiosFromGallery()
                    _mediasFromExternalStorage.value = medias
                }
            }
        }
    }

    suspend fun insertPost(post: Post): Pair<Boolean, ErrorResponseModel> {
        return repositoryService.insertPost(post)
    }

    suspend fun getPostById(postId: Int): Post = withContext(viewModelScope.coroutineContext) {
        repositoryStorage.getPostById(postId)
    }

    suspend fun clearAllPosts() {
        repositoryStorage.clearAllPosts()
    }
    suspend fun getWallByUserId(userId: Int) = withContext(viewModelScope.coroutineContext){
        wallRepositoryService.getWallsByUserId(userId)
    }

    val wallData: Flow<PagingData<Post>> = wallRepositoryStorage.data.flowOn(Dispatchers.Default)
    suspend fun getWallById(wallId: Int): Post = withContext(viewModelScope.coroutineContext){
        wallRepositoryStorage.getWallById(wallId)
    }
}