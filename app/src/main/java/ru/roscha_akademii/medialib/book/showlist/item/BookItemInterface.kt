package ru.roscha_akademii.medialib.book.showlist.item

import com.arellomobile.mvp.MvpDelegate


interface BookItemInterface {
    var bookId: Long?

    var parentDeleagate: MvpDelegate<*>?
}