package ru.roscha_akademii.medialib.common;

import android.app.Activity;

import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoActivity;

public class ActivityNavigator {
    private Activity activity;

    ActivityNavigator(Activity activity) {
        this.activity = activity;
    }

    public void openVideo(long id) {
        activity.startActivity(ShowVideoActivity.getStartIntent(activity, id));
    }
}
