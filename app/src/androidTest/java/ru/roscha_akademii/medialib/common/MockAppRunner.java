package ru.roscha_akademii.medialib.common; // MockAppRunner

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class MockAppRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        return super.newApplication(cl, MockMediaLibApplication.class.getName(), context);
    }
}

