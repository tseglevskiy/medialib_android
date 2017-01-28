package ru.roscha_akademii.medialib.video.playvideo.videocontrol

import com.arellomobile.mvp.MvpDelegate
import com.google.android.exoplayer2.ExoPlayer

import ru.roscha_akademii.medialib.video.playvideo.videocontrol.view.VideoControlView

interface VideoControlInterface {
    fun setCallback(parentDelegate: MvpDelegate<*>, callback: VideoControlCallback)

    fun setPlayer(player: ExoPlayer)

    fun releasePlayer()

    fun setFullscreenAction(mode: VideoControlView.FullscreenMode)
}
