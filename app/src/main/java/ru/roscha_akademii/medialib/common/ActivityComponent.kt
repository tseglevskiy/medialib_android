package ru.roscha_akademii.medialib.common

import dagger.Subcomponent
import ru.roscha_akademii.medialib.book.onebook.presenter.OneBookPresenter
import ru.roscha_akademii.medialib.book.onebook.view.OneBookActivity
import ru.roscha_akademii.medialib.book.showlist.list.presenter.BookListPresenter
import ru.roscha_akademii.medialib.mainscreen.presenter.MainScreenPresenter
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenterImpl
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoActivity
import ru.roscha_akademii.medialib.video.showlist.list.presenter.VideoListPresenterImpl
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoFragment

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun mainPresenter(): VideoListPresenterImpl

    fun bookListPresenter(): BookListPresenter

    fun showVideoPresenterImpl(): ShowVideoPresenterImpl

    fun oneBookPresenter(): OneBookPresenter

    fun mainScreenPresenter(): MainScreenPresenter



    fun inject(fragment: ListOfVideoFragment)

    fun inject(activity: ShowVideoActivity)

    fun inject(oneBookActivity: OneBookActivity)
}
