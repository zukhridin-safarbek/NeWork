package kg.zukhridin.nework.paging

import androidx.paging.*
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.dto.User
import kg.zukhridin.nework.dto.UserPreview
import kg.zukhridin.nework.entity.PostEntity
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.service.APIService
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val service: APIService,
    private val postDao: PostDao,
) :
    RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
    ): MediatorResult {
        try {
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    service.getLatestPosts(state.config.initialLoadSize)
                }
                LoadType.PREPEND -> {
                    val id = state.firstItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getAfterPosts(id, state.config.initialLoadSize)
                }
                LoadType.APPEND -> {
                    val id = state.lastItemOrNull()?.id ?: return MediatorResult.Success(false)
                    service.getBeforePosts(id, state.config.initialLoadSize)
                }
            }
            if (!result.isSuccessful) {
                ApiResult.Error(result.code().toString(), result.message(), result.body())
                throw HttpException(result)
            } else {
                ApiResult.Success(result.code().toString(), result.body())
            }
            val data = result.body().orEmpty()

            postDao.insertPosts(data)



            return MediatorResult.Success(data.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}