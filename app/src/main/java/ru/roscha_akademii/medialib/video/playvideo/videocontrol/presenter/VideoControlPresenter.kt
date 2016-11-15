package ru.roscha_akademii.medialib.video.playvideo.videocontrol.presenter

import com.google.android.exoplayer2.ExoPlayer
import com.hannesdorfmann.mosby.mvp.MvpPresenter

import ru.roscha_akademii.medialib.video.playvideo.videocontrol.VideoControlCallback
import ru.roscha_akademii.medialib.video.playvideo.videocontrol.view.VideoControlView

interface VideoControlPresenter : MvpPresenter<VideoControlView> {
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
