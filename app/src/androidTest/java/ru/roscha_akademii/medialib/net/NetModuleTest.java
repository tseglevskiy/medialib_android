package ru.roscha_akademii.medialib.net;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertTrue;

public class NetModuleTest {
    String json;

    @Before
    public void setUp() throws Exception {
        String file = "video.json";

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        InputStream is = instrumentation.getContext().getResources().getAssets().open(file);

        final StringBuilder sb = new StringBuilder();

        String strLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((strLine = reader.readLine()) != null) {
            sb.append(strLine);
        }

        json = sb.toString();

        Log.d("happy", json);
    }

    @Test
    public void assetsFileExists() {
        assertTrue("test file should be exists", json.length() > 10);
    }

}