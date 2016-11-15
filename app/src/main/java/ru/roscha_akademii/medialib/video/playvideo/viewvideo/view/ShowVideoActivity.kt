package ru.roscha_akademii.medialib.video.playvideo.viewvideo.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.hannesdorfmann.mosby.mvp.MvpActivity
import com.squareup.leakcanary.RefWatcher
import kotlinx.android.synthetic.main.showvideo_activity.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.common.widget.AspectRatioFrameLayout
import ru.roscha_akademii.medialib.common.widget.heightMatchParent
import ru.roscha_akademii.medialib.common.widget.heightWrapContent
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlCallback
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.view.VideoControlView
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter.ShowVideoPresenter
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.tools.StubExoPlayerEventListener
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.tools.StubTextRendererOutput
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.tools.StubVideoListener
import javax.inject.Inject

/*
логика реализована сейчас такая:

вертикальное расположение - с меню и данными
горизонтальное - фулскрин, с убранными заголовками.

пока не нажата "toggle full screen", телефон реагирует на вращения
если нажата - остаемся в горизонтальном расположении независимо от положения телефона
 */

class ShowVideoActivity : MvpActivity<ShowVideoView, ShowVideoPresenter>(), ShowVideoView {

    private var player: SimpleExoPlayer? = null
    internal var dataSourceFactory: DataSource.Factory? = null
    internal var extractorsFactory: ExtractorsFactory? = null
    internal var videoSource: MediaSource? = null

    val playerHandler: PlayerHandler by lazy {
        PlayerHandler()
    }

    val mainHandler: HideControlHandler by lazy {
        HideControlHandler()
    }

    private var decorView: View? = null

    internal var mode = Mode.AUTO
        set(mode) {
            field = mode
            if (mode == Mode.FULLSCREEN) {
                videoControl.setFullscreenAction(VideoControlView.FullscreenMode.NORMAL)
            } else {
                videoControl.setFullscreenAction(VideoControlView.FullscreenMode.FULL)
            }
        }


    internal var savedPosition: Long = 0
    private var url: String? = null

    val videoId: Long
        get() = intent.getLongExtra(EXTRA_ID, -1)

    @Inject
    lateinit var injectedPresenter: ShowVideoPresenter

    @Inject
    lateinit var refWatcher: RefWatcher

    fun requestLayout() {
        findViewById(android.R.id.content).requestLayout()
    }

    val videoControlCallback: VideoControlCallback by lazy {
        object : VideoControlCallback {
            override fun pauseAutohide() {
                mainHandler.freeze()
            }

            override fun resumeAutohide() {
                mainHandler.hideInMoments()
            }

            override fun onPlay() {
                keepScreenOn()
            }

            override fun onPause() {
                releaseScreen()
            }

            override fun gonnaFullScreen() {
                doToggleFullscreen()
            }

            override fun gonnaNormalScreen() {
                doToggleFullscreen()
            }
        }
    }

    internal var videoListener: SimpleExoPlayer.VideoListener = object : StubVideoListener() {
        override fun onVideoSizeChanged(width: Int,
                                        height: Int,
                                        unappliedRotationDegrees: Int,
                                        pixelWidthHeightRatio: Float) {
            Log.d("happy", "video onVideoSizeChanged" + width + " " + height
                    + " " + unappliedRotationDegrees + " " + pixelWidthHeightRatio)

            textureContainer.setAspectRatio(if (height == 0) 1F else width * pixelWidthHeightRatio / height)
            requestLayout()
        }

    }

    internal val eventListener: ExoPlayer.EventListener by lazy { StubExoPlayerEventListener() }

    internal val textOutput by lazy { StubTextRendererOutput() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MediaLibApplication).component.activityComponent(ActivityModule(this)).inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.showvideo_activity)

        textureContainer.setAspectRatio(1.78f)


        // https://developer.android.com/training/system-ui/visibility.html
        decorView = window.decorView
        decorView!!.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                // The system bars are visible. Make any desired
                // adjustments to your UI, such as showing the action bar or
                // other navigational controls.

                videoControl.setVisibility(VISIBLE)

                mainHandler.hideInMoments()
                requestLayout()
            } else {
                // The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.

                videoControl.setVisibility(GONE)
                requestLayout()
                mainHandler.freeze()
            }
        }

        videoControl.setCallback(videoControlCallback)

        showOrHideByOrientation(isLandscape)

        getPresenter().start(videoId)
    }

    override fun createPresenter(): ShowVideoPresenter {
        return injectedPresenter
    }

    override fun onStart() {
        super.onStart()

        activatePlayer(url)
    }

    override fun onStop() {
        super.onStop()

        deactivatePlayer()
    }

    override fun showVideo(url: String) {
        this.url = url
    }

    override fun showStatus(url: String) {
        mainHandler.post {
            statusField.url = url
        }
    }

    override fun showDescription(desc: String?) {
        mainHandler.post {
            if (desc != null) {
                descriptionField.visibility = VISIBLE
                descriptionField.loadDataWithBaseURL("", desc, "text/html", "utf-8", "")
            } else {
                descriptionField.visibility = GONE
            }
        }
    }

    override fun showTitle(title: String?) {
        mainHandler.post {
            title?.let {
                supportActionBar?.title = it
                statusField.title = it
            }
        }
    }

    class PlayerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            Log.d("happy", "msg " + msg.what)

            super.handleMessage(msg)
        }
    }

    @SuppressLint("HandlerLeak")
    inner class HideControlHandler : Handler() {
        val MSG_HIDE_CONTROLS = 1

        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_HIDE_CONTROLS) {
                if (isLandscape) {
                    hideControls()
                }
            } else {
                super.handleMessage(msg)
            }
        }

        internal fun freeze() {
            removeMessages(MSG_HIDE_CONTROLS)
        }

        internal fun hideInMoments() {
            freeze()
            sendEmptyMessageDelayed(MSG_HIDE_CONTROLS, 5000)
        }
    }

    fun doToggleFullscreen() {
        if (mode == Mode.AUTO) {
            mode = Mode.FULLSCREEN
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            //            hideControls();
            mainHandler.hideInMoments()
        } else {
            mode = Mode.AUTO
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

            //            showControls();
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = newConfig.orientation
        showOrHideByOrientation(newOrientation == Configuration.ORIENTATION_LANDSCAPE)
    }

    internal val isLandscape: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    internal fun showOrHideByOrientation(isLandscape: Boolean) {
        if (isLandscape) {
            hideDetails()
            hideControls()

            textureContainer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
            textureContainer.heightMatchParent()

        } else {
            showControls()
            showDetails()

            textureContainer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
            textureContainer.heightWrapContent()
        }
    }

    internal fun showControls() {
        mainHandler.freeze()

        supportActionBar?.show()

        decorView!!.systemUiVisibility = 0
        window.setFlags(
                0,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)


        requestLayout()
    }

    internal fun hideControls() {
        supportActionBar?.hide()

        // спрятать элементы навигации и включить фулскрин
        var visibilityFlags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= 19) {
            visibilityFlags = visibilityFlags or View.SYSTEM_UI_FLAG_IMMERSIVE
        }
        window.decorView.systemUiVisibility = visibilityFlags

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        videoControl.setVisibility(GONE)
        requestLayout()
    }

    internal fun showDetails() {
        allDataLayout.visibility = VISIBLE
        switchedScroll.scrollable = true
    }

    internal fun hideDetails() {
        allDataLayout.visibility = GONE
        switchedScroll.scrollable = false
    }

    internal enum class Mode {
        FULLSCREEN,
        AUTO
    }

    /*
     * Keep screen on
     * https://developer.android.com/training/scheduling/wakelock.html
     */

    private fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun releaseScreen() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /*
     * Player
     *
     */


    private fun activatePlayer(url: String?) {

        val player = ExoPlayerFactory.newSimpleInstance(
                this, // контекст
                DefaultTrackSelector(playerHandler), // TrackSelector
                DefaultLoadControl())

        //        binding.player.setPlayer(player);

        player.setVideoTextureView(textureView)

        player.setVideoListener(videoListener)
        player.addListener(eventListener)
        player.setTextOutput(textOutput)

        dataSourceFactory = DefaultDataSourceFactory(
                this, // Context
                "MyExoPlayerDemo"   // user-agent
        )

        extractorsFactory = DefaultExtractorsFactory()

        videoSource = ExtractorMediaSource(
                Uri.parse(url), // uri источника
                dataSourceFactory, // DataSource.Factory
                extractorsFactory, // ExtractorsFactory
                playerHandler, // Handler для получения событий от плеера
                null                // Listener - объект-получатель событий от плеера
        )

        player.prepare(videoSource)
        player.seekTo(savedPosition)

        player.playWhenReady = true

        videoControl.setPlayer(player)

        this.player = player
    }

    private fun deactivatePlayer() {
        videoControl.releasePlayer()

        player?.stop()

        savedPosition = player!!.currentPosition

        playerHandler.removeCallbacksAndMessages(null)

        player?.release()

        player?.setVideoListener(null)
        player?.removeListener(eventListener)
        player?.setTextOutput(null)

        refWatcher.watch(videoSource)
        videoSource = null

        refWatcher.watch(dataSourceFactory)
        dataSourceFactory = null

        refWatcher.watch(extractorsFactory)
        extractorsFactory = null

        refWatcher.watch(player)
        player = null

    }

    companion object {
        private val EXTRA_ID = "id"

        fun getStartIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, ShowVideoActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            return intent
        }
    }

}
