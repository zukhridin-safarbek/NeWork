package kg.zukhridin.nework.data.storage.dao

import androidx.paging.PagingSource
import androidx.room.*
import kg.zukhridin.nework.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(eventEntities: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getEventsPagingSource(): PagingSource<Int, EventEntity>

    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getData(): Flow<List<EventEntity>>

    @Query("DELETE FROM EventEntity")
    suspend fun clearAllEvent()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun eventLikeById(eventEntity: EventEntity)
    @Query("DELETE FROM evententity WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Int)
}