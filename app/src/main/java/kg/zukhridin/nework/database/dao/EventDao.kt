package kg.zukhridin.nework.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.entity.PostEntity
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
}