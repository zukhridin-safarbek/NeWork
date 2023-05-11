package kg.zukhridin.nework.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.repository.PostRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth,
) : ViewModel() {
    val data: Flow<PagingData<Post>> = appAuth.authStateFlow.flatMapLatest { myId ->
        repository.data.map { pagingData ->
            pagingData.map { post ->
                post.copy(ownedByMe = post.authorId == myId?.id)
            }
        }
    }.flowOn(Dispatchers.Default)
    private val _mediasFromExternalStorage = MutableLiveData<ArrayList<CustomMedia>>()
    val mediasFromExternalStorage: LiveData<ArrayList<CustomMedia>>
        get() = _mediasFromExternalStorage

    suspend fun getWall(userId: Int) =
        withContext(viewModelScope.coroutineContext) {
            repository.getWall(userId)
        }

    fun likeById(post: Post) {
        viewModelScope.launch {
            repository.likeById(post)
        }
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        try {
            repository.deletePostByIdFromServer(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deletePostByIdFromExternalStorage(id: Int) = viewModelScope.launch {
        try {
            repository.deletePostByIdFromExternalStorage(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateById(post: Post, content: String) = viewModelScope.launch {
        try {
            repository.updatePostByIdFromServer(post.copy(content = content))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateFromExternalStorage(post: Post) = viewModelScope.launch {
        repository.updatePostByIdFromExternalStorage(post)
    }

    suspend fun insertPostToServer(post: Post): Boolean {
        return repository.insertPostToService(post)
    }

    fun mediasFromGallery(context: Context, customMediaType: CustomMediaType) {
        viewModelScope.launch {
            when (customMediaType) {
                CustomMediaType.IMAGE -> {
                    val medias = repository.getImagesFromGallery(context)
                    _mediasFromExternalStorage.value = medias
                }
                CustomMediaType.VIDEO -> {
                    val medias = repository.getVideosFromGallery(context)
                    _mediasFromExternalStorage.value = medias
                }
                CustomMediaType.AUDIO -> {
                    val medias = repository.getAudiosFromGallery(context)
                    _mediasFromExternalStorage.value = medias
                }
            }
        }
    }

    suspend fun getPostById(postId: Int): Post = withContext(viewModelScope.coroutineContext) {
        repository.getPostById(postId)
    }
}