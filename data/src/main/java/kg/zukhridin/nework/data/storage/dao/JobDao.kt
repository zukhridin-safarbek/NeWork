package kg.zukhridin.nework.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.zukhridin.nework.data.entity.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(jobEntity: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobEntities: List<JobEntity>)

    @Query("SELECT * FROM jobentity")
    fun getJobs(): Flow<List<JobEntity>>

    @Query("DELETE FROM jobentity WHERE id = :jobId")
    suspend fun deleteJobById(jobId: Int)

    @Query("SELECT * FROM jobentity WHERE userId = :userId")
    fun getJobsByUserId(userId: Int): Flow<List<JobEntity>>

    @Query("DELETE FROM jobentity")
    suspend fun clearJobs(): Int
}