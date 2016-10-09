package ru.roscha_akademii.medialib;

import android.app.Instrumentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssertsFileHelper {
    public static String readFileFromAsserts(Instrumentation instrumentation, String file) throws IOException {
        InputStream is = instrumentation.getContext().getResources().getAssets().open(file);

        final StringBuilder sb = new StringBuilder();

        String strLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((strLine = reader.readLine()) != null) {
            sb.append(strLine);
        }

        return sb.toString();
    }

}
