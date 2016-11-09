package ru.roscha_akademii.medialib

import android.app.Instrumentation

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object AssertsFileHelper {
    fun readFileFromAsserts(instrumentation: Instrumentation, file: String): String {
        val stream = instrumentation.context.resources.assets.open(file)

        val sb = StringBuilder()

        var strLine: String?
        val reader = BufferedReader(InputStreamReader(stream))

        strLine = reader.readLine()
        while (strLine != null) {
            sb.append(strLine)
            strLine = reader.readLine()
        }

        return sb.toString()
    }

}
