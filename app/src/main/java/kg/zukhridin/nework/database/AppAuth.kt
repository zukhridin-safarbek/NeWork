package kg.zukhridin.nework.database

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kg.zukhridin.nework.dto.UserAuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"
    private val nameKey = "name"
    private val avatarKey = "avatar"

    private val _authStateFlow = MutableStateFlow<UserAuthState?>(null)

    init {
        val id = prefs.getInt(idKey, 0)
        val token = prefs.getString(tokenKey, null)
        val avatar = prefs.getString(avatarKey, null)
        val name = prefs.getString(nameKey, null)

        if (id == 0 || token == null) {
            prefs.edit { clear() }
        } else {
            _authStateFlow.value = UserAuthState(id, token, name ?: "name", avatar ?: "")
        }
    }

    val authStateFlow: StateFlow<UserAuthState?> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String, name: String, avatar: String) {
        _authStateFlow.value = UserAuthState(id, token, name, avatar)
        with(prefs.edit()) {
            putInt(idKey, id)
            putString(tokenKey, token)
            putString(nameKey, name)
            putString(avatarKey, avatar)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = null
        prefs.edit { clear() }
    }
}