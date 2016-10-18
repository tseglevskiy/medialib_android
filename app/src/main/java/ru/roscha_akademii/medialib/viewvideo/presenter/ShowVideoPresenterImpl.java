package ru.roscha_akademii.medialib.viewvideo.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.video.VideoDb;
import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoView;

public class ShowVideoPresenterImpl
        extends MvpBasePresenter<ShowVideoView>
        implements ShowVideoPresenter
{

    private VideoDb videoDb;

    public ShowVideoPresenterImpl(VideoDb videoDb) {
        this.videoDb = videoDb;
    }

    @Override
    public void start(long videoId) {
        Video video = videoDb.getVideo(videoId);

        if (getView() != null) {
            getView().showVideo(video);
        }
    }
}
