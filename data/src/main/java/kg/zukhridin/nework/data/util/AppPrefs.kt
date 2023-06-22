package kg.zukhridin.nework.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kg.zukhridin.nework.domain.models.PostItemClick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPrefs @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs = context.getSharedPreferences("postItemClick", Context.MODE_PRIVATE)
    private val postIdKey = "postId"
    private val userIdKey = "userId"
    private val fragmentNameKey = "fragmentName"

    private val _postItemCLickStateFlow = MutableStateFlow<PostItemClick?>(null)

    init {
        val postId = prefs.getInt(postIdKey, 0)
        val userId = prefs.getInt(userIdKey, 0)
        _postItemCLickStateFlow.value = PostItemClick(postId, userId)
    }

    @Synchronized
    fun setFragmentName(fragmentName: String) {
        with(prefs.edit()) {
            putString(fragmentNameKey, fragmentName)
            apply()
        }
    }

    fun getFragmentName(): String? {
        return prefs.getString(fragmentNameKey, null)
    }

    val postItemClickStateFlow: StateFlow<PostItemClick?> = _postItemCLickStateFlow.asStateFlow()

    @Synchronized
    fun setPostClickPostId(postId: Int) {
        val userId = prefs.getInt(userIdKey, 0)
        _postItemCLickStateFlow.value = PostItemClick(postId = postId, userId = userId)
        with(prefs.edit()) {
            putInt(postIdKey, postId)
            apply()
        }
    }

    @Synchronized
    fun setPostClickUserId(userId: Int) {
        val postId = prefs.getInt(postIdKey, 0)
        _postItemCLickStateFlow.value = PostItemClick(postId = postId, userId = userId)
        with(prefs.edit()) {
            putInt(userIdKey, userId)
            apply()
        }
    }


}