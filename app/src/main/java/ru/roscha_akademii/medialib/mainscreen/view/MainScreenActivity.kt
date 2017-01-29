package ru.roscha_akademii.medialib.mainscreen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.main_screen_activity.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityComponent
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.mainscreen.presenter.MainScreenPresenter

class MainScreenActivity : MvpAppCompatActivity(), MainScreenView {
    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainScreenActivity::class.java)
        }
    }

    lateinit var activityComponent: ActivityComponent

    @InjectPresenter
    lateinit var presenter: MainScreenPresenter

    @ProvidePresenter
    fun createPresenter(): MainScreenPresenter {
        return activityComponent.mainScreenPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MediaLibApplication).component.activityComponent(ActivityModule(this))
//        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_screen_activity)

        viewPager.adapter = MainScreenAdapter(supportFragmentManager, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.video_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            presenter.wannaUpdateVideoList()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}


