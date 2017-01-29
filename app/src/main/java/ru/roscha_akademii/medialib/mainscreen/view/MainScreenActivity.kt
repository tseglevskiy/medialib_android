package ru.roscha_akademii.medialib.mainscreen.view

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.main_screen_activity.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityComponent

class MainScreenActivity : MvpAppCompatActivity(), MainScreenView {
    lateinit var activityComponent: ActivityComponent

//    @InjectPresenter
//    lateinit var presenter: MainScreenPresenter
//
//    @ProvidePresenter
//    fun createPresenter(): MainScreenPresenter {
//        return activityComponent.mainScreenPresenter()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        activityComponent = (application as MediaLibApplication).component.activityComponent(ActivityModule(this))
//        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_screen_activity)

        viewPager.adapter = MainScreenAdapter(supportFragmentManager)
    }
}


