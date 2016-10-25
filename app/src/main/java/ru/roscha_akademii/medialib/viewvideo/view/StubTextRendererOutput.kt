package ru.roscha_akademii.medialib.viewvideo.view

import android.util.Log

import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextRenderer

class StubTextRendererOutput : TextRenderer.Output {
    override fun onCues(cues: List<Cue>?) {
        if (cues != null) {
            for (c in cues) {
                Log.d("happy", "cue " + c.text.toString())
            }
        }
    }
}
