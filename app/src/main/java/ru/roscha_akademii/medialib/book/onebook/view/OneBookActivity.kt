package ru.roscha_akademii.medialib.book.onebook.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.book_onebook_acitivity.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.book.onebook.presenter.OneBookPresenter
import ru.roscha_akademii.medialib.common.ActivityComponent
import ru.roscha_akademii.medialib.common.ActivityModule
import ru.roscha_akademii.medialib.common.MediaLibApplication

class OneBookActivity : MvpAppCompatActivity(), OneBookView {
    companion object {
        private val EXTRA_ID = "id"

        fun getStartIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, OneBookActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            return intent
        }
    }

    val bookId: Long
        get() = intent.getLongExtra(EXTRA_ID, -1)

    @InjectPresenter
    lateinit var presenter: OneBookPresenter

    @ProvidePresenter
    fun createPresenter(): OneBookPresenter {
        return activityComponent.oneBookPresenter()
    }

    lateinit var activityComponent: ActivityComponent
    lateinit var adapter: BookFilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MediaLibApplication).component.activityComponent(ActivityModule(this))
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.book_onebook_acitivity)

        adapter = BookFilesAdapter(mvpDelegate)
        filesListField.adapter = adapter
        filesListField.layoutManager = LinearLayoutManager(this)

        presenter.start(bookId)
    }

    override fun showTitle(title: String?) {
        titleField.text = title ?: "--"
    }

    override fun showDescription(description: String?) {
        descriptionField.post {
            if (description != null) {
                descriptionField.visibility = VISIBLE
                descriptionField.loadDataWithBaseURL("", description, "text/html", "utf-8", "")
            } else {
                descriptionField.visibility = GONE
            }
        }
    }

    override fun showFiles(files: List<BookFile>) {
        adapter.list = files
    }
}

