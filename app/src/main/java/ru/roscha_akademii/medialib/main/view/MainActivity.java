package ru.roscha_akademii.medialib.main.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.ActivityModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ActivityMainBinding;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;
import ru.roscha_akademii.medialib.net.model.Video;

public class MainActivity
        extends MvpActivity<MainView, MainPresenter>
        implements MainView
{
    private ActivityMainBinding binding;

    @Inject
    MainPresenter injectedPresenter;

    private VideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MediaLibApplication)getApplication())
                .component()
                .activityComponent(new ActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.list.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VideoListAdapter();
        binding.list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((MediaLibApplication)getApplication()).refWatcher().watch(presenter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().start();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return injectedPresenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getPresenter().wannaUpdateVideoList();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showVideoList(List<Video> list) {
        adapter.setList(list);
    }
}
