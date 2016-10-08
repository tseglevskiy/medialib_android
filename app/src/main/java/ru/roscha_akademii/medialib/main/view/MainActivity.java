package ru.roscha_akademii.medialib.main.view;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ActivityMainBinding;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;
import ru.roscha_akademii.medialib.main.presenter.MainPresenterImpl;

public class MainActivity
        extends MvpActivity<MainView, MainPresenter>
        implements MainView
{
    private ActivityMainBinding binding;

    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MediaLibApplication)getApplication()).component().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.hello.setOnClickListener(v -> getPresenter().helloClicked());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().start();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenterImpl(getApplicationContext());
    }

    @Override
    public void showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
    }
    
}
