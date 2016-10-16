package ru.roscha_akademii.medialib.main.view;

import ru.roscha_akademii.medialib.R;
import ru.roscha_akademii.medialib.common.CommonListAdapter;
import ru.roscha_akademii.medialib.databinding.VideolistItemBinding;
import ru.roscha_akademii.medialib.net.model.Video;

public class VideoListAdapter extends CommonListAdapter<Video, VideolistItemBinding> {

    public VideoListAdapter() {
        super(R.layout.videolist_item);
    }

    @Override
    public void show(Video item, VideolistItemBinding binding) {
        binding.title.setText(item.title);
        binding.description.setText(item.description);
    }
}
