package ru.roscha_akademii.medialib.video.showlist.item.view

import com.hannesdorfmann.mosby.mvp.MvpView
import org.joda.time.LocalDate
import ru.roscha_akademii.medialib.storage.StorageStatus

interface VideoCardView : MvpView {
    fun showTitle(title: String)

//    fun showDescription(desc: String)

    fun showDuration(duration: String?)

    fun showDate(date: LocalDate?)

    fun showImage(url: String?)

    fun showStatus(url: String)
}
