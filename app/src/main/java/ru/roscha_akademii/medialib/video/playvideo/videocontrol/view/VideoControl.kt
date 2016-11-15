package ru.roscha_akademii.medialib.video.playvideo.videocontrol.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout

import java.util.Formatter
import java.util.Locale

import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlCallback
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlInterface
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.presenter.VideoControlPresenter
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.presenter.VideoControlPresenterImpl

import kotlinx.android.synthetic.main.videocontrol.view.*

/*
 * вдохновение и идеи для расшиpения этого класса можно брать в
 * com.google.android.exoplayer2.ui.PlaybackControlView
 */

class VideoControl @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
: MvpFrameLayout<VideoControlView, VideoControlPresenter>(context, attrs, defStyleAttr), VideoControlView, VideoControlInterface {
    private val PROGRESS_BAR_MAX = 1000

    internal var callback: VideoControlCallback? = null

    internal val positionListener: SeekBar.OnSeekBarChangeListener


    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.videocontrol, this, true)

        positionListener =
                object : SeekBar.OnSeekBarChangeListener {

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        dragging = true
                        if (callback != null) callback!!.pauseAutohide()
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            timeCurrentField.text = stringForTime(filmDuration * progress / PROGRESS_BAR_MAX)
                        }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        dragging = false
                        getPresenter().seekTo(seekBar.progress, seekBar.max)
                        if (callback != null) callback!!.resumeAutohide()
                    }
                }

        mediacontrollerProgress.max = PROGRESS_BAR_MAX
        mediacontrollerProgress.setOnSeekBarChangeListener(positionListener)

        playButton.setOnClickListener { getPresenter().gonnaPlay() }
        pauseButton.setOnClickListener { getPresenter().gonnaPause() }

        fullscreenButton.setOnClickListener { getPresenter().gonnaFullScreen() }
        fullscreenExitButton.setOnClickListener { getPresenter().gonnaNormalScreen() }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        if (getPresenter() != null) {
            if (visibility == View.VISIBLE) {
                getPresenter().setIsVisible()
            } else {
                getPresenter().setIsInvisible()
            }
        }
    }

    override fun createPresenter(): VideoControlPresenter {
        val presenter = VideoControlPresenterImpl()

        presenter.setCallback(callback)
        player?.let {
            presenter.setPlayer(it)
        }

        if (visibility == View.VISIBLE) {
            presenter.setIsVisible()
        } else {
            presenter.setIsInvisible()
        }

        return presenter
    }

    override fun setCallback(callback: VideoControlCallback) {
        this.callback = callback
        getPresenter()?.setCallback(callback)
    }

    /*
     * Player control
     *
     */

    internal var player: ExoPlayer? = null

    override fun setPlayer(player: ExoPlayer) {
        this.player = player
        getPresenter()?.setPlayer(player)
    }

    override fun releasePlayer() {
        getPresenter()?.revokePlayer()
    }

    /*
     * Dragging in Progress bar
     *
     */

    internal var dragging = false

    private val formatBuilder = StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())

    private fun stringForTime(timeMsSrc: Long): String {
        var timeMs = timeMsSrc
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0
        }
        val totalSeconds = (timeMs + 500) / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        formatBuilder.setLength(0)

        return if (hours > 0)
            formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        else
            formatter.format("%02d:%02d", minutes, seconds).toString()
    }

    private fun progressBarValue(position: Long): Int {
        if (filmDuration == 0L) return 0
        return (position * PROGRESS_BAR_MAX / filmDuration).toInt()
    }

    /*
     * VideoControlViewInterface
     *
     */

    internal var filmDuration: Long = 0

    override fun showTime(duration: Long) {
        this.filmDuration = duration
        timeField.text = stringForTime(duration)
    }

    override fun showCurrentTime(position: Long) {
        if (!dragging) {
            timeCurrentField.text = stringForTime(position)
            mediacontrollerProgress.progress = progressBarValue(position)
        }
    }

    override fun showBuffered(bufferedPosition: Long) {
        mediacontrollerProgress.secondaryProgress = progressBarValue(bufferedPosition)
    }

    override fun setPlayPauseAction(mode: VideoControlView.PlayPauseMode) {
        if (mode === VideoControlView.PlayPauseMode.PLAY) {
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE

        } else if (mode === VideoControlView.PlayPauseMode.PAUSE) {
            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
        }
    }

    override fun setFullscreenAction(mode: VideoControlView.FullscreenMode) {
        if (mode === VideoControlView.FullscreenMode.FULL) {
            fullscreenButton.visibility = View.VISIBLE
            fullscreenExitButton.visibility = View.GONE
        } else {
            fullscreenButton.visibility = View.GONE
            fullscreenExitButton.visibility = View.VISIBLE
        }
    }

}


