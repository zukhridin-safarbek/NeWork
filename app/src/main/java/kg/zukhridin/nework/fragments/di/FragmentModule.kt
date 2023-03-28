package kg.zukhridin.nework.fragments.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.fragments.NewPostFragmentDialog
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FragmentModule {
    @Provides
    @Singleton
    fun provideNewPostFragmentDialog(): NewPostFragmentDialog = NewPostFragmentDialog()
}