package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ru.roscha_akademii.medialib.video.model.remote.Video;
import ru.roscha_akademii.medialib.video.model.local.VideoDb;
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShowVideoPresenterImplTest {

    private VideoDb videoDb;
    private ShowVideoPresenterImpl presenter;
    private ShowVideoView view;

    @Before
    public void setUp() throws Exception {
        videoDb = mock(VideoDb.class);
        view = mock(ShowVideoView.class);

        presenter = new ShowVideoPresenterImpl(videoDb); // SUT
    }

    private static Video video1 = new Video();
    static {
        video1.setId(1111);
        video1.setDescription("description one");
        video1.setPictureUrl("picture url one");
        video1.setTitle("title one");
        video1.setVideoUrl("video url one");
    }

    @Test
    public void start() throws Exception {
        when(videoDb.getVideo(anyLong())).thenReturn(video1);

        presenter.attachView(view);
        presenter.start(video1.getId());

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Video> videoCaptor = ArgumentCaptor.forClass(Video.class);

        verify(videoDb, times(1)).getVideo(longCaptor.capture());
        assertEquals((Long) video1.getId(), longCaptor.getAllValues().get(0));

        verify(view, times(1)).showVideo(videoCaptor.capture());
        assertEquals((Long) video1.getId(), (Long) videoCaptor.getAllValues().get(0).getId());

    }

}