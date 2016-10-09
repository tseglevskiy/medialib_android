package ru.roscha_akademii.medialib.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    @Provides
    @Singleton
    OkHttpClient providesHttpClient() {

        HttpLoggingInterceptor fullLogInterceptor = new HttpLoggingInterceptor();
        fullLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(fullLogInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
//.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(
            OkHttpClient client,
            Gson gson,
            @Named("base url") String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    VideoApi providesVideoApi(Retrofit retrofit) {
        return retrofit.create(VideoApi.class);
    }

}
