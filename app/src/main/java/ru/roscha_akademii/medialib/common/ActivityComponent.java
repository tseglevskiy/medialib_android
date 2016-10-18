package ru.roscha_akademii.medialib.common;


import dagger.Subcomponent;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;
import ru.roscha_akademii.medialib.main.view.MainActivity;
import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoActivity;

@ActivityScope
@Subcomponent(modules = {
        ActivityModule.class
})
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(ShowVideoActivity activity);

    MainPresenter mainPresenter();
}
