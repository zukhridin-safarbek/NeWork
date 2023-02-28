package kg.zukhridin.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _users
    val responseCode = repository.responseCode.asLiveData(Dispatchers.Default)

    init {
        getUsers()
    }

    fun authorization(login: String, password: String) = viewModelScope.launch {
        repository.signIn(login, password)
    }

    private fun getUsers() = viewModelScope.launch {
        _users.postValue(repository.getUsers())
    }
}