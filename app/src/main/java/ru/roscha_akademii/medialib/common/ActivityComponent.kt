package ru.roscha_akademii.medialib.common


import dagger.Subcomponent
import ru.roscha_akademii.medialib.main.presenter.MainPresenter
import ru.roscha_akademii.medialib.main.view.MainActivity
import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoActivity

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun inject(activity: ShowVideoActivity)

    fun mainPresenter(): MainPresenter
}
