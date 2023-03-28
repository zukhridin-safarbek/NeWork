package kg.zukhridin.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.model.StateModel
import kg.zukhridin.nework.repository.PostRepository
import kg.zukhridin.nework.service.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val appAuth: AppAuth,
    private val postDao: PostDao
) : ViewModel() {
    val listData: Flow<PagingData<Post>> = appAuth.authStateFlow.flatMapLatest { myId ->
        repository.data.map { pagingData ->
            pagingData.map { post ->
                post.copy(ownedByMe = post.authorId == myId?.id)
            }
        }
    }.flowOn(Dispatchers.Default)
    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?>
        get() = _photo
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _walls = MutableLiveData<List<Post>>()
    val walls: LiveData<List<Post>> = _walls
    private val _state = MutableLiveData<StateModel>()
    val state: LiveData<StateModel>
        get() = _state

    init {
        getPosts()
        getWalls()
    }

    private fun getPosts() = viewModelScope.launch {
        postDao.getPosts().collectLatest {
            _posts.value = it.map(PostEntity::toDto)
        }
    }

    private fun getWalls() = viewModelScope.launch {
        _walls.value = repository.getWalls()
    }

    fun likeById(post: Post) {
        viewModelScope.launch {
            repository.likeById(post)
        }
    }

    fun insertPostToServer(post: Post) = viewModelScope.launch {
        repository.insertPostToService(post)
    }

    fun savePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }


}