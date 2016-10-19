package ru.roscha_akademii.medialib.viewvideo.view;

import android.util.Log;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;

import java.util.List;

public class StubTextRendererOutput implements TextRenderer.Output {
    @Override
    public void onCues(List<Cue> cues) {
        if (cues != null) {
            for (Cue c : cues) {
                Log.d("happy", "cue " + c.text.toString());
            }
        }
    }
}
