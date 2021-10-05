package com.starter.mvvm.di

import android.content.Context
import com.starter.mvvm.BuildConfig
import com.starter.mvvm.BuildConfig.MAIN_HOST
import com.starter.mvvm.utils.extensions.getVersionCode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAIN_HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val builder = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("accept", "application/json")

                request.addHeader("x-api-key", "")
//                val user = sharedPrefsUtils.getUser()
//                if (user != null)
//                    request.addHeader(
//                        "authorization",
//                        "${"token type"} ${"access token"}"
//                    )
//                request.addHeader(
//                    "Accept-Language",
//                    sharedPrefsUtils.getLanguage()
//                )
                context.getVersionCode().let { versionCode ->
                    if (versionCode.isNotEmpty()) {
                        request.addHeader("key", "")
                        request.addHeader("value", "")
                    }
                }
                it.proceed(request.build())
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        return builder
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }
}