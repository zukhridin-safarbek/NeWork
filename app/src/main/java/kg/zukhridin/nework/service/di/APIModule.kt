package kg.zukhridin.nework.service.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.zukhridin.nework.BuildConfig
import kg.zukhridin.nework.database.AppAuth
import kg.zukhridin.nework.service.APIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object APIModule {
    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (androidx.viewbinding.BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        appAuth: AppAuth,
    ): OkHttpClient {
        return try {
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val request = appAuth.authStateFlow.value?.token?.let {
                        chain.request().newBuilder()
                            .addHeader("Authorization", it)
                            .build()
                    } ?: chain.request()
                    chain.proceed(request)
                }
                .build()
        }catch (e: Exception){
            throw Exception(e.message)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun apiService(
        retrofit: Retrofit,
    ): APIService = retrofit.create()
}