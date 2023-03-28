package kg.zukhridin.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?>
        get() = _photo

    private val _users = MutableLiveData<List<User>?>()
    val user: LiveData<List<User>?> = _users
    val userAuthResponseCode = repository.userAuthResponseCode
    val userResponseCode = repository.userResponseCode

    init {
        getUsers()
    }

    fun userSignIn(login: String, password: String, name: String) = viewModelScope.launch {
        photo.value?.let { photoModel ->
            repository.userSignIn(login, password, name, photoModel)
            savePhoto(null, null)
        }
    }

    fun userLogIn(login: String, password: String) = viewModelScope.launch {
        repository.userLogIn(login, password)
    }

    private fun getUsers() = viewModelScope.launch {
        repository.getUsers()
        val res = repository.userResponseCode

        when (res.value) {
            is ApiResult.Success -> {
                _users.value = (res.value?.data)
            }
            else -> {
                _users.postValue(null)
            }
        }


    }

    fun savePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}