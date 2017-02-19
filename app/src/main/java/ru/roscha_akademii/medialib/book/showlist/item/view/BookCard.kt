package ru.roscha_akademii.medialib.book.showlist.item.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_card.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.showlist.item.BookItemInterface
import ru.roscha_akademii.medialib.book.showlist.item.presenter.BookItemPresenter
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.storage.Storage
import javax.inject.Inject

class BookCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), BookItemView, BookItemInterface {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var bookDb: BookDb

    @Inject
    lateinit var storage: Storage

    init {
        (context.applicationContext as MediaLibApplication)
                .component
                .inject(this)

        LayoutInflater
                .from(context)
                .inflate(R.layout.book_card, this, true)
    }

    @InjectPresenter
    lateinit var presenter: BookItemPresenter

    @ProvidePresenter
    fun providesPresenter(): BookItemPresenter {
        return BookItemPresenter(bookDb, storage)
    }

    override var bookId: Long? = null
        set(value) {
            field = value
            presenter.bookId = value
        }

    override var parentDeleagate: MvpDelegate<*>? = null
        set(value) {
            field = value

            val mvpDelegate = MvpDelegate<BookCard>(this)
            mvpDelegate.setParentDelegate(value, hashCode().toString())
            mvpDelegate.onCreate()
            mvpDelegate.onAttach()

//            statusField.parentDelegate = mvpDelegate
        }

    override fun showId(id: Long) {
        dummyId.text = id.toString()
    }

    override fun showTitle(title: String?) {
        titleField.text = title ?: "-"
    }

    override fun showImage(url: String?) {
        if (url != null && !url.isEmpty()) {
            picasso.load(url)
                    .fit()
                    .centerCrop().into(imageField)
            imageField.visibility = View.VISIBLE
        } else {
            imageField.visibility = View.GONE
        }
    }
}
