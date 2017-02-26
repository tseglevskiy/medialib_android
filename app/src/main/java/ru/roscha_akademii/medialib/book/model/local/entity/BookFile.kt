package ru.roscha_akademii.medialib.book.model.local.entity

data class BookFile(
        val bookId: Long,
        val url: String
) {
    constructor(book: Book, url: String):this(
            bookId = book.id,
            url = url
    )
}


