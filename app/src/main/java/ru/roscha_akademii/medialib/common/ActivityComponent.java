package ru.roscha_akademii.medialib.common;


import dagger.Subcomponent;
import ru.roscha_akademii.medialib.main.presenter.MainPresenter;
import ru.roscha_akademii.medialib.main.view.MainActivity;

@ActivityScope
@Subcomponent(modules = {
        ActivityModule.class
})
public interface ActivityComponent {
    void inject(MainActivity activity);

    MainPresenter mainPresenter();
}
