package kg.zukhridin.nework.presentation.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.presentation.utils.Permissions
import kg.zukhridin.nework.presentation.utils.CheckNetwork
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CustomUtilsModule {
    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer =
        ExoPlayer.Builder(context).build()
    @Singleton
    @Provides
    fun provideCheckNetwork(@ApplicationContext context: Context): CheckNetwork = CheckNetwork(context)

    @Singleton
    @Provides
    fun providePermissions(): Permissions =
        Permissions()
}