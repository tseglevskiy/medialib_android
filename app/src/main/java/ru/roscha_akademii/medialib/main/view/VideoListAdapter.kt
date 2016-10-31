package ru.roscha_akademii.medialib.main.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.net.model.Video
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.videolist_item.view.*

internal class VideoListAdapter(val clickListener: OnItemClickListener) : RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {
    var list: List<Video>? = null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.videolist_item, parent, false)

        return MyViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.show(list!![position])
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class MyViewHolder(view: View, val clickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        fun show(item: Video) {
            itemView.title.text = item.title
            itemView.description.text = item.description
            itemView.setOnClickListener { v -> clickListener.onItemClicked(item.id) }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: Long)
    }
}
