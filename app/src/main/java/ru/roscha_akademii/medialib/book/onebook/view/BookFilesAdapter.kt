package ru.roscha_akademii.medialib.book.onebook.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpDelegate
import kotlinx.android.synthetic.main.book_file_card.view.*
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile

class BookFilesAdapter(val parentDelegate: MvpDelegate<*>) : RecyclerView.Adapter<BookFileHolder>() {
    var list: List<BookFile>? = null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookFileHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_file_card, parent, false)

        view.downloadField.parentDelegate = parentDelegate

        return BookFileHolder(view)
    }

    override fun onBindViewHolder(holder: BookFileHolder, position: Int) {
        holder.showFile(list!![position])

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}

class BookFileHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun showFile(file: BookFile) {
        itemView.urlField.text = file.url
        itemView.downloadField.url = file.url
    }
}
