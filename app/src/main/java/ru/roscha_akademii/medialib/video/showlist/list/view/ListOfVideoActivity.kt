package ru.roscha_akademii.medialib.video.showlist.list.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityComponent
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl

class ListOfVideoActivity : MvpAppCompatActivity(), ListOfVideoView, VideoListAdapter.OnItemClickListener {
    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ListOfVideoActivity::class.java)
        }
    }

    lateinit var activityComponent: ActivityComponent

    @InjectPresenter
    lateinit var presenter: MainPresenterImpl

    @ProvidePresenter
    fun createPresenter(): MainPresenterImpl {
        return activityComponent.mainPresenter()
    }

    lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MediaLibApplication).component.activityComponent(ActivityModule(this))
//        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        adapter = VideoListAdapter(mvpDelegate, this)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
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

    override fun showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(id: Long) {
        presenter.wannaOpenVideo(id)
    }

    override fun showVideoList(list: List<Video>) {
        adapter.list = list
    }

}
