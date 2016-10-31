package ru.roscha_akademii.medialib.video.playvideo.videocontrol.presenter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Timeline
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlCallback
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.view.VideoControlView
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.tools.StubExoPlayerEventListener

class VideoControlPresenterImpl : MvpBasePresenter<VideoControlView>(), VideoControlPresenter {
    private var player: ExoPlayer? = null
    private var isVisible = false

    private val playerEventListener = object : StubExoPlayerEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            updatePlayPauseButton()
            updateProgress()
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
            updateProgress()
        }

        override fun onPositionDiscontinuity() {
            updateProgress()
        }
    }
    private var callback: VideoControlCallback? = null

    override fun setCallback(callback: VideoControlCallback) {
        this.callback = callback
    }

    override fun setPlayer(player: ExoPlayer) {
        this.player?.removeListener(playerEventListener)
        this.player = player
        player.addListener(playerEventListener)
        updateAll()
    }

    override fun revokePlayer() {
        player?.removeListener(playerEventListener)
        player = null
    }

    /*
     * Commands from View
     *
     */

    override fun gonnaPlay() {
        player?.playWhenReady = true

    }

    override fun gonnaPause() {
        player?.playWhenReady = false
    }

    override fun gonnaFullScreen() {
        callback?.gonnaFullScreen()
    }

    override fun gonnaNormalScreen() {
        callback?.gonnaNormalScreen()
    }

    override fun setIsVisible() {
        isVisible = true
        updateAll()
    }

    override fun setIsInvisible() {
        isVisible = false
    }

    override fun seekTo(progress: Int, max: Int) {
        player?.seekTo(positionValue(progress, max))
    }

    private fun positionValue(progress: Int, max: Int): Long {
        val duration = player?.duration ?: C.TIME_UNSET

        return if (duration != C.TIME_UNSET) duration * progress / max else 0
    }

    /*
     * Update view
     *
     */

    private fun updateAll() {
        updateProgress()
        updatePlayPauseButton()
    }

    private fun updateProgress() {
        if (!isVisible) return
        if (view == null) return

        val duration = if (player == null) 0 else player!!.duration
        val position = if (player == null) 0 else player!!.currentPosition
        val bufferedPosition = if (player == null) 0 else player!!.bufferedPosition

        view?.showTime(duration)
        view?.showCurrentTime(position)
        view?.showBuffered(bufferedPosition)

        updateProgressHandler.clear()

        // Schedule an update if necessary.
        val playbackState = if (player == null)
            ExoPlayer.STATE_IDLE
        else
            player!!.playbackState

        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {
            if (player!!.playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                var delayMs = 1000 - position % 1000
                if (delayMs < 200) delayMs += 1000

                updateProgressHandler.update(delayMs)
            } else {
                updateProgressHandler.update(1000)

            }
        }
    }

    private fun updatePlayPauseButton() {
        val playing = player != null
                && player!!.playWhenReady
                && player!!.playbackState != ExoPlayer.STATE_ENDED

        if (playing) {
            callback?.onPlay()
        } else {
            callback?.onPause()
        }

        if (!isVisible || view == null) return

        if (playing) {
            view?.setPlayPauseAction(VideoControlView.PlayPauseMode.PAUSE)
        } else {
            view?.setPlayPauseAction(VideoControlView.PlayPauseMode.PLAY)
        }
    }

    /*
     * Updater
     *
     */

    private val updateProgressHandler = ProgressHandler()

    @SuppressLint("HandlerLeak")
    private inner class ProgressHandler : Handler() {
        private val MSG_UPDATE = 938373

        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_UPDATE) {
                updateProgress()
            } else {
                super.handleMessage(msg)
            }
        }

        internal fun clear() {
            removeMessages(MSG_UPDATE)
        }

        internal fun update(delay: Long) {
            sendEmptyMessageDelayed(MSG_UPDATE, delay)
        }

    }


}
