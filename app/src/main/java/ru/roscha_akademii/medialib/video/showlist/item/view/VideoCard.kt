package ru.roscha_akademii.medialib.video.showlist.item.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.videolist_card.view.*
import org.joda.time.LocalDate
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.storage.StorageStatus
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.showlist.item.VideoCardInterface
import ru.roscha_akademii.medialib.video.showlist.item.presenter.VideoCardPresenter
import ru.roscha_akademii.medialib.video.showlist.item.presenter.VideoCardPresenterImpl
import javax.inject.Inject

class VideoCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
: MvpFrameLayout<VideoCardView, VideoCardPresenter>(context, attrs, defStyleAttr), VideoCardView, VideoCardInterface {
    @Inject
    lateinit var videoDb: VideoDb

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var picasso: Picasso

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
        return VideoCardPresenterImpl(videoDb, storage) // TODO move to dagger
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.post {
            presenter?.videoId = videoId
        }
    }

    //    override fun showDescription(desc: String) {
//        descriptionField.text = desc
//    }
//
    override fun showTitle(title: String) {
        titleField.text = title
//        statusField.title = title // STOPSHIP
    }

    override fun showDuration(duration: String?) {
        if (duration != null) {
            durationField.text = duration
            durationField.visibility = View.VISIBLE
        } else {
            durationField.visibility = View.GONE
        }
    }

    override fun showDate(date: LocalDate?) {
        if (date != null) {
            dateField.text = date.toString()
            dateField.visibility = View.VISIBLE
        } else {
            dateField.visibility = View.GONE
        }
    }

    override fun showImage(url: String?) {
        if (url != null) {
            picasso.load(url)
                    .fit()
                    .centerCrop().into(imageField)
            imageField.visibility = View.VISIBLE
        } else {
            imageField.visibility = View.GONE
        }
    }

    override fun showStatus(url: String) {
//        statusField.downloadUrl(mvpDelegate, url) // STOPSHIP
    }
}