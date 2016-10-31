package ru.roscha_akademii.medialib.videocardview.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout
import kotlinx.android.synthetic.main.videolist_card.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.video.StorageStatus
import ru.roscha_akademii.medialib.video.VideoDb
import ru.roscha_akademii.medialib.video.VideoStorage
import ru.roscha_akademii.medialib.videocardview.VideoCardInterface
import ru.roscha_akademii.medialib.videocardview.presenter.VideoCardPresenter
import ru.roscha_akademii.medialib.videocardview.presenter.VideoCardPresenterImpl
import javax.inject.Inject

class VideoCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
: MvpFrameLayout<VideoCardView, VideoCardPresenter>(context, attrs, defStyleAttr), VideoCardView, VideoCardInterface {
    @Inject
    lateinit var videoDb: VideoDb

    @Inject
    lateinit var videoStorage: VideoStorage

    override var videoId: Long? = null
        set(value) {
            field = value
            presenter?.videoId = value
        }


    init {
        (context.applicationContext as MediaLibApplication)
                .component
                .inject(this)

        LayoutInflater
                .from(context)
                .inflate(R.layout.videolist_card, this, true)
    }

    override fun createPresenter(): VideoCardPresenter {
        return VideoCardPresenterImpl(videoDb, videoStorage) // TODO move to dagger
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.videoId = videoId
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.stop()
    }

    override fun showDescription(desc: String) {
        descriptionField.text = desc
    }

    override fun showTitle(title: String) {
        titleField.text = title
    }

    override fun showStatus(status: StorageStatus, percent: Int?) {
        when(status) {
            StorageStatus.LOCAL -> {
                statusField.text = "on device"
                statusField.setOnClickListener {
                    presenter?.removeLocal()
                }
            }

            StorageStatus.PROGRESS -> {
                statusField.text = "$percent%"
                statusField.setOnClickListener {
                    presenter?.removeLocal()
                }
            }

            else -> {
                statusField.text = "in cloud"
                statusField.setOnClickListener {
                    presenter?.saveLocal()
                }
            }

        }
    }

}