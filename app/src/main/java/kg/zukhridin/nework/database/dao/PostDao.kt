package kg.zukhridin.nework.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import kg.zukhridin.nework.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(postEntities: List<PostEntity>)

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPostsPagingSource(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM PostEntity")
    suspend fun clear()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likedById(postEntity: PostEntity)

}