package ru.roscha_akademii.medialib.common;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.roscha_akademii.medialib.update.UpdateService;

@Singleton
public class ServiceNavigator {
    private Context context;

    @Inject
    ServiceNavigator(Context context) {
        this.context = context;
    }

    public void startUpdate() {
        context.startService(
                UpdateService.getStartIntent(context));
    }
}
