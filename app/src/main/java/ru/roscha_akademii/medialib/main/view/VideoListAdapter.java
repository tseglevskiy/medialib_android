package ru.roscha_akademii.medialib.main.view;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.CommonListAdapter;
import ru.roscha_akademii.medialib.databinding.VideolistItemBinding;
import ru.roscha_akademii.medialib.net.model.Video;

class VideoListAdapter extends CommonListAdapter<Video, VideolistItemBinding> {

    VideoListAdapter(OnItemClickListener clickListener) {
        super(R.layout.videolist_item, clickListener);
    }

    @Override
    public void show(Video item, VideolistItemBinding binding, OnItemClickListener clickListener) {
        binding.title.setText(item.title);
        binding.description.setText(item.description);
        binding.getRoot().setOnClickListener(v -> clickListener.onItemClicked(item.id));
    }

}
