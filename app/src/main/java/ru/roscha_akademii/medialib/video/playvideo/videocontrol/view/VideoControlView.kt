package ru.roscha_akademii.medialib.video.playvideo.videocontrol.view

import com.hannesdorfmann.mosby.mvp.MvpView

interface VideoControlView : MvpView {
    fun showTime(duration: Long)

    fun showCurrentTime(position: Long)

    fun showBuffered(bufferedPosition: Long)

    fun setPlayPauseAction(mode: PlayPauseMode)

    fun setFullscreenAction(mode: FullscreenMode)

    enum class PlayPauseMode {
        PLAY, PAUSE
    }

    enum class FullscreenMode {
        FULL, NORMAL
    }
}
