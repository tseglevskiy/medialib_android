package ru.roscha_akademii.medialib;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import ru.roscha_akademii.medialib.common.MediaLibApplication;
import ru.roscha_akademii.medialib.databinding.ActivityMainBinding;

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

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show();
    }
    
}
