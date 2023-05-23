package kg.zukhridin.nework.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.data.service.repository.*
import kg.zukhridin.nework.data.storage.repository.EventRepositoryImplStorage
import kg.zukhridin.nework.data.storage.repository.JobRepositoryImplStorage
import kg.zukhridin.nework.data.storage.repository.PostRepositoryImplStorage
import kg.zukhridin.nework.domain.service.repositories.EventRepositoryService
import kg.zukhridin.nework.domain.service.repositories.JobRepositoryService
import kg.zukhridin.nework.domain.service.repositories.PostRepositoryService
import kg.zukhridin.nework.domain.service.repositories.UserRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.EventRepositoryStorage
import kg.zukhridin.nework.domain.storage.repositories.JobRepositoryStorage
import kg.zukhridin.nework.domain.storage.repositories.PostRepositoryStorage
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindPostRepositoryStorage(impl: PostRepositoryImplStorage): PostRepositoryStorage

    @Binds
    @Singleton
    fun bindJobRepositoryStorage(impl: JobRepositoryImplStorage): JobRepositoryStorage

    @Binds
    @Singleton
    fun bindEventRepositoryStorage(impl: EventRepositoryImplStorage): EventRepositoryStorage

    @Binds
    @Singleton
    fun bindUserRepositoryService(impl: UserRepositoryImplService): UserRepositoryService

    @Binds
    @Singleton
    fun bindPostRepositoryService(impl: PostRepositoryImplService): PostRepositoryService

    @Binds
    @Singleton
    fun bindJobRepositoryService(impl: JobRepositoryImplService): JobRepositoryService

    @Binds
    @Singleton
    fun bindEventRepositoryService(impl: EventRepositoryImplService): EventRepositoryService
}