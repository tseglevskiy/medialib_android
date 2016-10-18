package ru.roscha_akademii.medialib.viewvideo.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.ActivityModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ShowvideoActivityBinding;
import ru.roscha_akademii.medialib.viewvideo.presenter.ShowVideoPresenter;

public class ShowVideoActivity extends MvpActivity<ShowVideoView, ShowVideoPresenter> {
    private static final String EXTRA_ID = "id";
    private ShowvideoActivityBinding binding;

    public static Intent getStartIntent(Context context, long id) {
        Intent intent = new Intent(context, ShowVideoActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public long getVideoId() {
        return getIntent().getLongExtra(EXTRA_ID, -1);
    }

    @Inject
    ShowVideoPresenter injectedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MediaLibApplication)getApplication())
                .component()
                .activityComponent(new ActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.showvideo_activity);
        binding.videoId.setText("" + getVideoId());
    }

    @NonNull
    @Override
    public ShowVideoPresenter createPresenter() {
        return injectedPresenter;
    }

}
