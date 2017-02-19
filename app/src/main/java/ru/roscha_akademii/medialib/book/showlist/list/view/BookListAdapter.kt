package ru.roscha_akademii.medialib.book.showlist.list.view

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.book.showlist.item.view.BookCard

class BookListAdapter(val parentDelegate: MvpDelegate<*>,
                      val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<BookListAdapter.MyViewHolder>() {

    var list: List<Book>? = null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = BookCard(parent.context)
        view.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        )
        Log.d("happy", "parent delegate " + parentDelegate)
        view.parentDeleagate = parentDelegate

        return MyViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.show(list!![position])
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class MyViewHolder(view: BookCard,
                       val clickListener: OnItemClickListener)
        : RecyclerView.ViewHolder(view) {
        fun show(item: Book) {
            (itemView as BookCard).let {
                it.bookId = item.id
                it.setOnClickListener { v -> clickListener.onItemClicked(item.id) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: Long)
    }
}
