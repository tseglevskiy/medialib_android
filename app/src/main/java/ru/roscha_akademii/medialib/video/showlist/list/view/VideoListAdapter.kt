package ru.roscha_akademii.medialib.video.showlist.list.view

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCard

class VideoListAdapter(val parentDelegate: MvpDelegate<*>, val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {
    var list: List<Video>? = null
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = VideoCard(parent.context)
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

    class MyViewHolder(view: VideoCard, val clickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        fun show(item: Video) {
            (itemView as VideoCard).let {
                it.videoId = item.id
                it.setOnClickListener { v -> clickListener.onItemClicked(item.id) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: Long)
    }
}
