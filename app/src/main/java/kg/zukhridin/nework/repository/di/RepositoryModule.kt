package kg.zukhridin.nework.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.repository.EventRepository
import kg.zukhridin.nework.repository.JobRepository
import kg.zukhridin.nework.repository.PostRepository
import kg.zukhridin.nework.repository.UserRepository
import kg.zukhridin.nework.repository.impl.EventRepositoryImpl
import kg.zukhridin.nework.repository.impl.JobRepositoryImpl
import kg.zukhridin.nework.repository.impl.PostRepositoryImpl
import kg.zukhridin.nework.repository.impl.UserRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    fun bindJobRepository(impl: JobRepositoryImpl): JobRepository

    @Binds
    @Singleton
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
}