package kg.zukhridin.nework.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.database.dao.PostDao
import kg.zukhridin.nework.database.AppDb
import kg.zukhridin.nework.database.dao.EventDao
import kg.zukhridin.nework.database.dao.JobDao
import kg.zukhridin.nework.utils.DATABASE_NAME
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
}