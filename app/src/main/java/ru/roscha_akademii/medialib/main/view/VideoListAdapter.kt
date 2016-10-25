package ru.roscha_akademii.medialib.main.view

import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.common.CommonListAdapter
import ru.roscha_akademii.medialib.databinding.VideolistItemBinding
import ru.roscha_akademii.medialib.net.model.Video

internal class VideoListAdapter(clickListener: CommonListAdapter.OnItemClickListener) : CommonListAdapter<Video, VideolistItemBinding>(R.layout.videolist_item, clickListener) {

    override fun show(item: Video, binding: VideolistItemBinding, clickListener: CommonListAdapter.OnItemClickListener) {
        binding.title.text = item.title
        binding.description.text = item.description
        binding.root.setOnClickListener { v -> clickListener.onItemClicked(item.id) }
    }

}
