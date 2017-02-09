package ru.roscha_akademii.medialib.video.showlist.list.view

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.ActivityComponent
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.showlist.list.presenter.MainPresenterImpl

class ListOfVideoFragment : MvpAppCompatFragment(), ListOfVideoView, VideoListAdapter.OnItemClickListener {
    companion object {
        fun getInstance(): ListOfVideoFragment {
            return ListOfVideoFragment()
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

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity) {
        activityComponent = (activity.applicationContext as MediaLibApplication)
                .component
                .activityComponent(ActivityModule(activity))

        super.onAttach(activity)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.activity_main, container, false)

        adapter = VideoListAdapter(mvpDelegate, this)

        view.list.layoutManager = LinearLayoutManager(container?.context)
        view.list.adapter = adapter

        return view
    }

    override fun showHelloToast() {
        Toast.makeText(activity, "Hello!", Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(id: Long) {
        presenter.wannaOpenVideo(id)
    }

    override fun showVideoList(list: List<Video>) {
        adapter.list = list
    }

}
