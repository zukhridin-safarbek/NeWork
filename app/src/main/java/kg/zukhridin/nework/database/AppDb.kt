package kg.zukhridin.nework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.zukhridin.nework.database.dao.EventDao
import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.entity.EventEntity
import kg.zukhridin.nework.entity.JobEntity
import kg.zukhridin.nework.entity.PostEntity
import javax.inject.Singleton

@Singleton
@Database(entities = [PostEntity::class, JobEntity::class, EventEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun jobDao(): JobDao
    abstract fun eventDao(): EventDao
}