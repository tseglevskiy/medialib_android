package ru.roscha_akademii.medialib;

import android.databinding.DataBindingUtil;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import ru.roscha_akademii.medialib.databinding.ActivityMainBinding;

public class MainActivity
        extends MvpActivity<MainView, MainPresenter>
        implements MainView
{

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Teest Leak Canary
         wrongWay();


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

    // Teest Leak Canary
    private void wrongWay() {
        new Thread() {
            @Override
            public void run() {
                while (true){
                    SystemClock.sleep(1000);
                }
            }
        }.start();
    }
}
