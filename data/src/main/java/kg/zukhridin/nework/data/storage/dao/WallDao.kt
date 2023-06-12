package kg.zukhridin.nework.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.zukhridin.nework.data.entity.WallEntity

@Dao
interface WallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalls(walls: List<WallEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWall(wall: WallEntity)

    @Query("SELECT * FROM wallentity WHERE id = :wallId")
    suspend fun getWallById(wallId: Int): WallEntity

    @Query("SELECT * FROM wallentity WHERE authorId = :userId")
    fun getWallsByUserId(userId: Int): LiveData<List<WallEntity>?>

    @Query("SELECT * FROM WallEntity ORDER BY id DESC")
    fun getWallsPagingSource(): PagingSource<Int, WallEntity>
}