package ru.roscha_akademii.medialib.video.showlist.list.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.hannesdorfmann.mosby.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenter
import ru.roscha_akademii.medialib.video.model.remote.Video
import javax.inject.Inject

class ListOfVideoActivity : MvpActivity<ListOfVideoView, MainPresenter>(), ListOfVideoView, VideoListAdapter.OnItemClickListener {
    @Inject
    lateinit var injectedPresenter: MainPresenter

    private val adapter = VideoListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MediaLibApplication).component.activityComponent(ActivityModule(this)).inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        (application as MediaLibApplication).refWatcher()!!.watch(presenter)
    }

    override fun onStart() {
        super.onStart()
        getPresenter().start()
    }

    override fun createPresenter(): MainPresenter {
        return injectedPresenter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.video_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            getPresenter().wannaUpdateVideoList()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun showHelloToast() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(id: Long) {
        getPresenter().wannaOpenVideo(id)
    }

    override fun showVideoList(list: List<Video>) {
        adapter.list = list
    }
}
