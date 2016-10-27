package ru.roscha_akademii.medialib.videocontrol.view

import android.content.Context
import android.databinding.DataBindingUtil
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
import ru.roscha_akademii.medialib.databinding.VideocontrolBinding
import ru.roscha_akademii.medialib.videocontrol.VideoControlCallback
import ru.roscha_akademii.medialib.videocontrol.VideoControlInterface
import ru.roscha_akademii.medialib.videocontrol.presenter.VideoControlPresenter
import ru.roscha_akademii.medialib.videocontrol.presenter.VideoControlPresenterImpl

/*
 * вдохновение и идеи для расшиpения этого класса можно брать в
 * com.google.android.exoplayer2.ui.PlaybackControlView
 */

class VideoControl @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
: MvpFrameLayout<VideoControlView, VideoControlPresenter>(context, attrs, defStyleAttr), VideoControlView, VideoControlInterface {
    private val PROGRESS_BAR_MAX = 1000

    private val binding: VideocontrolBinding

    internal var callback: VideoControlCallback? = null

    internal val positionListener: SeekBar.OnSeekBarChangeListener


    init {
        binding = DataBindingUtil.inflate<VideocontrolBinding>(
                LayoutInflater.from(context),
                R.layout.videocontrol,
                this,
                true)

        positionListener =
                object : SeekBar.OnSeekBarChangeListener {

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        dragging = true
                        if (callback != null) callback!!.pauseAutohide()
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            binding.timeCurrent.text = stringForTime(filmDuration * progress / PROGRESS_BAR_MAX)
                        }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        dragging = false
                        getPresenter().seekTo(seekBar.progress, seekBar.max)
                        if (callback != null) callback!!.resumeAutohide()
                    }
                }

        binding.mediacontrollerProgress.max = PROGRESS_BAR_MAX
        binding.mediacontrollerProgress.setOnSeekBarChangeListener(positionListener)

        binding.play.setOnClickListener { getPresenter().gonnaPlay() }
        binding.pause.setOnClickListener { getPresenter().gonnaPause() }

        binding.fullscreen.setOnClickListener { getPresenter().gonnaFullScreen() }
        binding.fullscreenExit.setOnClickListener { getPresenter().gonnaNormalScreen() }
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

        presenter.setCallback(callback!!)
        presenter.setPlayer(player!!)
        if (visibility == View.VISIBLE) {
            presenter.setIsVisible()
        } else {
            presenter.setIsInvisible()
        }

        return presenter
    }

    override fun setCallback(callback: VideoControlCallback) {
        this.callback = callback
        if (getPresenter() != null) {
            getPresenter().setCallback(callback)
        }
    }

    /*
     * Player control
     *
     */

    internal var player: ExoPlayer? = null

    override fun setPlayer(player: ExoPlayer) {
        this.player = player
        if (getPresenter() != null) {
            getPresenter().setPlayer(player)
        }
    }

    override fun releasePlayer() {
        getPresenter().revokePlayer()
    }

    /*
     * Dragging in Progress bar
     *
     */

    internal var dragging = false

    private val formatBuilder = StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())

    private fun stringForTime(timeMs: Long): String {
        var timeMs = timeMs
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

    override fun showTime(filmDuration: Long) {
        this.filmDuration = filmDuration
        binding.time.text = stringForTime(filmDuration)
    }

    override fun showCurrentTime(position: Long) {
        if (!dragging) {
            binding.timeCurrent.text = stringForTime(position)
            binding.mediacontrollerProgress.progress = progressBarValue(position)
        }
    }

    override fun showBuffered(bufferedPosition: Long) {
        binding.mediacontrollerProgress.secondaryProgress = progressBarValue(bufferedPosition)
    }

    override fun setPlayPauseAction(mode: VideoControlView.PlayPauseMode) {
        if (mode === VideoControlView.PlayPauseMode.PLAY) {
            binding.play.visibility = View.VISIBLE
            binding.pause.visibility = View.GONE

        } else if (mode === VideoControlView.PlayPauseMode.PAUSE) {
            binding.play.visibility = View.GONE
            binding.pause.visibility = View.VISIBLE
        }
    }

    override fun setFullscreenAction(mode: VideoControlView.FullscreenMode) {
        if (mode === VideoControlView.FullscreenMode.FULL) {
            binding.fullscreen.visibility = View.VISIBLE
            binding.fullscreenExit.visibility = View.GONE
        } else {
            binding.fullscreen.visibility = View.GONE
            binding.fullscreenExit.visibility = View.VISIBLE
        }
    }

}


