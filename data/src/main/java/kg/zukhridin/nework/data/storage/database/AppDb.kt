package kg.zukhridin.nework.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.zukhridin.nework.data.entity.EventEntity
import kg.zukhridin.nework.data.entity.JobEntity
import kg.zukhridin.nework.data.entity.PostEntity
import kg.zukhridin.nework.data.entity.WallEntity
import kg.zukhridin.nework.data.storage.dao.EventDao
import kg.zukhridin.nework.data.storage.dao.JobDao
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.storage.dao.WallDao
import javax.inject.Singleton

@Singleton
@Database(entities = [PostEntity::class, JobEntity::class, EventEntity::class, WallEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun jobDao(): JobDao
    abstract fun eventDao(): EventDao
    abstract fun wallDao(): WallDao
}