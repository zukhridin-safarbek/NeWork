package kg.zukhridin.nework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.entity.PostEntity
import javax.inject.Singleton

@Singleton
@Database(entities = [PostEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
}