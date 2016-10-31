package ru.roscha_akademii.medialib.video.playvideo.videocontrol

interface VideoControlCallback {
    fun pauseAutohide()

    fun resumeAutohide()

    fun onPlay()

    fun onPause()

    fun gonnaFullScreen()

    fun gonnaNormalScreen()
}
