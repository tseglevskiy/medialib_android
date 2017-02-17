package ru.roscha_akademii.medialib.book

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.roscha_akademii.medialib.book.model.remote.BookApi
import ru.roscha_akademii.medialib.book.model.remote.BookUpdate
import javax.inject.Singleton

@Module
open class BookModule {
    @Provides
    @Singleton
    internal fun providesBookApi(retrofit: Retrofit)
            : BookApi
            = retrofit.create(BookApi::class.java)

    @Provides
    @Singleton
    open internal fun providesBookUpdate(api: BookApi)
            : BookUpdate
            = BookUpdate(api)
}


