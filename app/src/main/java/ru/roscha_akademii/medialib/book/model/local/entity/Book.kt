package ru.roscha_akademii.medialib.book.model.local.entity

import ru.roscha_akademii.medialib.book.model.remote.entity.BookDTO

data class Book(
        val id: Long,
        val title: String? = null,
        var picture: String? = null,
        var description: String? = null
) {
    constructor(dto: BookDTO):this(
            id = dto.id,
            title = dto.title,
            picture = dto.picture,
            description = dto.description
    )
}


