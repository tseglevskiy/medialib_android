package ru.roscha_akademii.medialib.main.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.ActivityModule;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ActivityMainBinding;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;

public class MainActivity
        extends MvpActivity<MainView, MainPresenter>
        implements MainView
{
    private ActivityMainBinding binding;

    @Inject
    MainPresenter injectedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MediaLibApplication)getApplication())
                .component()
                .activityComponent(new ActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.hello.setOnClickListener(v -> getPresenter().helloClicked());
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
    public void showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
    }
    
}
