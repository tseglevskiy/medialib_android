package ru.roscha_akademii.medialib.book.showlist.item.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.book_card.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.book.showlist.item.BookItemInterface
import ru.roscha_akademii.medialib.book.showlist.item.presenter.BookItemPresenter

class BookCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), BookItemView, BookItemInterface {


    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.book_card, this, true)
    }

    @InjectPresenter
    lateinit var presenter: BookItemPresenter

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
}
