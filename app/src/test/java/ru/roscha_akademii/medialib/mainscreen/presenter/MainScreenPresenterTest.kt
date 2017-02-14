package ru.roscha_akademii.medialib.mainscreen.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.mainscreen.view.MainScreenView
import ru.roscha_akademii.medialib.mainscreen.view.`MainScreenView$$State`
import ru.roscha_akademii.medialib.update.UpdateScheduler

class MainScreenPresenterTest {
    lateinit var updateScheduler: UpdateScheduler
    lateinit var view: MainScreenView
    lateinit var viewState: `MainScreenView$$State`

    lateinit var presenter: MainScreenPresenter // SUT

    @Before
    fun setUp() {
        updateScheduler = mock<UpdateScheduler>()

        view = mock<MainScreenView>()
        viewState = mock<`MainScreenView$$State`>()

        presenter = MainScreenPresenter(updateScheduler) // SUT
        presenter.attachView(view)
        presenter.setViewState(viewState)
    }

    @Test
    fun start() {
        verify(updateScheduler, times(1)).startBySchedule()
    }
}