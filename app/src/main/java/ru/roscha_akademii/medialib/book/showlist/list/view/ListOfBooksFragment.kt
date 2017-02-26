package ru.roscha_akademii.medialib.book.showlist.list.view

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
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.showlist.list.presenter.BookListPresenter
import ru.roscha_akademii.medialib.common.ActivityComponent
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication


class ListOfBooksFragment : MvpAppCompatFragment(), BookListView, BookListAdapter.OnItemClickListener {

    companion object {
        fun getInstance(): ListOfBooksFragment {
            return ListOfBooksFragment()
        }
    }

    lateinit var activityComponent: ActivityComponent

    @InjectPresenter
    lateinit var presenter: BookListPresenter

    @ProvidePresenter
    fun createPresenter(): BookListPresenter {
        return activityComponent.bookListPresenter()
    }

    lateinit var adapter: BookListAdapter

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity) {
        activityComponent = (activity.applicationContext as MediaLibApplication)
                .component
                .activityComponent(ActivityModule(activity))

        super.onAttach(activity)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.activity_main, container, false)

        adapter = BookListAdapter(mvpDelegate, this)

        view.list.layoutManager = LinearLayoutManager(container?.context)
        view.list.adapter = adapter

        return view
    }

    override fun showBooks(list: List<Book>) {
        adapter.list = list
    }

    override fun onItemClicked(id: Long) {
        presenter.wannaOpenBook(id)
    }


//    override fun showHelloToast() {
//        Toast.makeText(activity, "Hello!", Toast.LENGTH_LONG).show()
//    }
//
//    override fun onItemClicked(id: Long) {
//        presenter.wannaOpenVideo(id)
//    }
//
//    override fun showVideoList(list: List<Video>) {
//        adapter.list = list
//    }

}
