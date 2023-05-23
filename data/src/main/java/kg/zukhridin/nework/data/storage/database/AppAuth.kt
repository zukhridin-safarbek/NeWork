package kg.zukhridin.nework.data.storage.database

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kg.zukhridin.nework.domain.models.UserAuthState
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

    private val _authStateFlow = MutableStateFlow<UserAuthState?>(null)

    init {
        val id = prefs.getInt(idKey, 0)
        val token = prefs.getString(tokenKey, null)

        if (id == 0 || token == null) {
            prefs.edit { clear() }
        } else {
            _authStateFlow.value = UserAuthState(id, token)
        }
    }

    val authStateFlow: StateFlow<UserAuthState?> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = UserAuthState(id, token)
        with(prefs.edit()) {
            putInt(idKey, id)
            putString(tokenKey, token)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = null
        prefs.edit { clear() }
    }
}