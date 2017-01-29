package ru.roscha_akademii.medialib.mainscreen.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.mainscreen.view.MainScreenView
import ru.roscha_akademii.medialib.update.UpdateScheduler

@InjectViewState
class MainScreenPresenter(internal var updateScheduler: UpdateScheduler)
    : MvpPresenter<MainScreenView>(), MainScreenPresenterIf {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        updateScheduler.startBySchedule()
    }

    override fun wannaUpdateVideoList() {
        updateScheduler.startNow()
    }
}


