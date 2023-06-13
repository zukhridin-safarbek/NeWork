package kg.zukhridin.nework.data.storage.dao

import androidx.paging.PagingSource
import androidx.room.*
import kg.zukhridin.nework.data.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM postentity WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity

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

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun deleteById(id: Int){
        println("id: id: $id")
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePostById(postEntity: PostEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likedById(postEntity: PostEntity)
    @Query("DELETE FROM PostEntity")
    suspend fun clearAllPosts()

}