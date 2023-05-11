package kg.zukhridin.nework.utils.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.utils.CheckNetwork
import kg.zukhridin.nework.utils.Permissions
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UtilsModule {
    @Singleton
    @Provides
    fun provideCheckNetwork(@ApplicationContext context: Context): CheckNetwork = CheckNetwork(context)

    @Singleton
    @Provides
    fun providePermissions(): Permissions = Permissions()
}