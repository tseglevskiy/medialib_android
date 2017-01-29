package ru.roscha_akademii.medialib.mainscreen.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.roscha_akademii.medialib.R

class DemoFragment : Fragment() {
    companion object {
        val MSG = "msg"
        fun getInstance(msg: String): DemoFragment {
            val f = DemoFragment()
            val b = Bundle()
            b.putString(MSG, msg)
            f.arguments = b
            return f
        }
    }

    val msg: String
    get() = arguments.getString(MSG)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_demo, container, false)
//        view.hello.text = msg

        return view
    }
}


