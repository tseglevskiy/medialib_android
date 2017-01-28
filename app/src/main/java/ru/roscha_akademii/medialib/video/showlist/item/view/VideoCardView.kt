package ru.roscha_akademii.medialib.video.showlist.item.view

import com.arellomobile.mvp.MvpView
import org.joda.time.LocalDate

interface VideoCardView : MvpView {
    fun showTitle(title: String)

//    fun showDescription(desc: String)

    fun showDuration(duration: String?)

    fun showDate(date: LocalDate?)

    fun showImage(url: String?)

    fun showStatus(url: String)
}
