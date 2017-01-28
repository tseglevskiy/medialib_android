package ru.roscha_akademii.medialib.storage.widget.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.downloadcontrol.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.StorageStatus
import ru.roscha_akademii.medialib.storage.stub.StorageStub
import ru.roscha_akademii.medialib.storage.widget.DownloadControlInterface
import ru.roscha_akademii.medialib.storage.widget.presenter.DownloadControlPresenterImpl
import javax.inject.Inject

class DownloadControl @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : FrameLayout(context,
        attrs,
        defStyleAttr),
        DownloadControlView,
        DownloadControlInterface {

    @Inject
    lateinit var storage: Storage

    private val mainHandler: Handler by lazy {
        Handler()
    }

    override var url: String? = null
        set(value) {
            field = value

            mainHandler.post {
                presenter.url = value
            }
        }

    override var title: String? = null
        set(value) {
            field = value

            mainHandler.post {
                presenter.title = value
            }
        }

    @InjectPresenter
    lateinit var presenter: DownloadControlPresenterImpl

    override var parentDelegate: MvpDelegate<*>? = null
        set(value) {
            field = value

            val mvpDelegate = MvpDelegate<DownloadControl>(this)
            mvpDelegate.setParentDelegate(value, hashCode().toString())

            mvpDelegate.onCreate()
            mvpDelegate.onAttach()
        }


    @ProvidePresenter
    fun createPresenter(): DownloadControlPresenterImpl {
        return DownloadControlPresenterImpl(storage)
    }

    init {
        val app = context.applicationContext
        when (app) {
            is MediaLibApplication -> app
                    .component
                    .inject(this)
            else -> {
                storage = StorageStub()
            }
        }

        LayoutInflater
                .from(context)
                .inflate(R.layout.downloadcontrol, this, true)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.stop()
    }

    override fun showStatus(status: StorageStatus, percent: Int?) {
        when (status) {
            StorageStatus.LOCAL -> {
                downloadControlStatusField.text = "on device"
                downloadControlStatusField.setOnClickListener {
                    presenter.removeLocal()
                }
            }

            StorageStatus.PROGRESS -> {
                downloadControlStatusField.text = "$percent%"
                downloadControlStatusField.setOnClickListener {
                    presenter.removeLocal()
                }
            }

            else -> {
                downloadControlStatusField.text = "in cloud"
                downloadControlStatusField.setOnClickListener {
                    presenter.saveLocal()
                }
            }

        }
    }
}