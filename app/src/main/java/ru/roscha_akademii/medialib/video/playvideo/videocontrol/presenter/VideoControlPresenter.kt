package ru.roscha_akademii.medialib.video.playvideo.videocontrol.presenter

import com.google.android.exoplayer2.ExoPlayer
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlCallback

interface VideoControlPresenter {
    fun setCallback(callback: VideoControlCallback?)

    fun setPlayer(player: ExoPlayer)

    fun revokePlayer()

    fun gonnaPlay()

    fun gonnaPause()

    fun gonnaFullScreen()

    fun gonnaNormalScreen()

    fun setIsVisible()

    fun setIsInvisible()

    fun seekTo(progress: Int, max: Int)

}
