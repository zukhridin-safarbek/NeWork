package kg.zukhridin.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.model.ErrorResponseModel
import kg.zukhridin.nework.model.PhotoModel
import kg.zukhridin.nework.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val users: LiveData<List<User>?> = _users

    init {
        getUsers()
    }

    suspend fun userRegistration(
        login: String,
        password: String,
        name: String
    ): Pair<Boolean, ErrorResponseModel?> =
        withContext(viewModelScope.coroutineContext) {
            if (photo.value != null) {
                println("registrationWithPhoto")
                repository.registrationWithPhoto(login, password, name, photo.value!!)
            } else {
                println("registrationWithOutPhoto")
                repository.registrationWithOutPhoto(login, password, name)
            }
        }

    suspend fun userAuthentication(
        login: String,
        password: String
    ): Pair<Boolean, ErrorResponseModel?> = withContext(viewModelScope.coroutineContext) {
        repository.userAuthentication(login, password)
    }

    suspend fun getUser(userId: Int): User? {
        return repository.getUserById(userId)
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