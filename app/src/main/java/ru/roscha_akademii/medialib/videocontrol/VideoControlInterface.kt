package ru.roscha_akademii.medialib.videocontrol

import com.google.android.exoplayer2.ExoPlayer

import ru.roscha_akademii.medialib.videocontrol.view.VideoControlView

interface VideoControlInterface {
    fun setPlayer(player: ExoPlayer)

    fun releasePlayer()

    fun setCallback(callback: VideoControlCallback)

    fun setFullscreenAction(mode: VideoControlView.FullscreenMode)
}
