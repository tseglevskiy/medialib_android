package ru.roscha_akademii.medialib.storage.widget.presenter

import android.os.Handler
import android.os.Message
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.StorageStatus
import ru.roscha_akademii.medialib.storage.widget.view.DownloadControlView
import ru.roscha_akademii.medialib.video.showlist.item.presenter.VideoCardPresenterImpl

class DownloadControlPresenterImpl(val storage: Storage)
: MvpBasePresenter<DownloadControlView>(), DownloadControlPresenter {
    override var url: String? = null
        set(value) {
            field = value
            updateStatus()
        }

    override var title: String? = null

    override fun saveLocal() {
        url?.let {
            storage.saveLocal(it, title ?: "", true)

            updateStatus()
            updateHandler.start()
        }
    }

    override fun removeLocal() {
        url?.let {
            storage.removeLocal(it)
        }

        updateStatus()
    }

    override fun updateStatus() {
        if (url != null) {

            val status = storage.getStatus(url!!)
            val percent = storage.getPercent(url!!)
            view?.showStatus(status, percent)

            if (status == StorageStatus.PROGRESS && view != null) {
                updateHandler.start()
            }

            view?.showStatus(status, percent)
        } else {
            view?.showStatus(StorageStatus.REMOTE, 0)
        }
    }

    override fun stop() {
        updateHandler.stop()
    }

    val updateHandler: UpdateHandler by lazy {
        UpdateHandler()
    }

    inner class UpdateHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                VideoCardPresenterImpl.MSG_UPDATE -> {
                    updateStatus()
                }

                else -> super.handleMessage(msg)
            }
        }

        fun start() {
            removeMessages(VideoCardPresenterImpl.MSG_UPDATE)
            sendEmptyMessageDelayed(VideoCardPresenterImpl.MSG_UPDATE, 3000)
        }

        fun stop() {
            removeMessages(VideoCardPresenterImpl.MSG_UPDATE)
        }
    }

}