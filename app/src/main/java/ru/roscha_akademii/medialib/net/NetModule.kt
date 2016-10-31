package ru.roscha_akademii.medialib.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.roscha_akademii.medialib.video.model.remote.VideoApi

@Module
open class NetModule {

    @Provides
    @Singleton
    internal fun providesHttpClient(): OkHttpClient {

        val fullLogInterceptor = HttpLoggingInterceptor()
        fullLogInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().followRedirects(false).addInterceptor(fullLogInterceptor).build()
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        return GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")//.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }


    @Provides
    @Singleton
    @Named("baseurl")
    open fun baseUrl(): String {
        return URL
    }

    @Provides
    @Singleton
    internal fun providesRetrofit(
            client: OkHttpClient,
            gson: Gson,
            @Named("baseurl") baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    companion object {
         val URL = "https://raw.githubusercontent.com/tseglevskiy/medialib_android/master/"
    }
}
