package kg.zukhridin.nework.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.data.storage.dao.PostDao
import kg.zukhridin.nework.data.storage.database.AppDb
import kg.zukhridin.nework.data.storage.dao.EventDao
import kg.zukhridin.nework.data.storage.dao.JobDao
import kg.zukhridin.nework.data.storage.dao.WallDao
import kg.zukhridin.nework.data.util.Constants.DATABASE_NAME
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context,
    ): AppDb = Room.databaseBuilder(context, AppDb::class.java, DATABASE_NAME).build()

    @Provides
    fun providePostDao(appDb: AppDb): PostDao = appDb.postDao()

    @Provides
    fun provideJobDao(appDb: AppDb): JobDao = appDb.jobDao()

    @Provides
    fun provideEventDao(appDb: AppDb): EventDao = appDb.eventDao()

    @Provides
    fun provideWallDao(appDb: AppDb): WallDao = appDb.wallDao()

}