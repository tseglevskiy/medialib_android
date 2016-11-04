package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ru.roscha_akademii.medialib.video.model.local.VideoStorage;
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
    private VideoStorage videoStorage;

    @Before
    public void setUp() throws Exception {
        videoDb = mock(VideoDb.class);
        videoStorage = mock(VideoStorage.class);
        view = mock(ShowVideoView.class);

        presenter = new ShowVideoPresenterImpl(videoDb, videoStorage); // SUT
    }

    private static Video video1 = new Video(
            1111,
            "title one",
            "picture url one",
            "description one",
            "video url one");


    @Test
    public void start() throws Exception {
        when(videoDb.getVideo(anyLong())).thenReturn(video1);

        presenter.attachView(view);
        presenter.start(video1.getId());

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> videoCaptor = ArgumentCaptor.forClass(String.class);

        verify(videoDb, times(1)).getVideo(longCaptor.capture());
        assertEquals((Long) video1.getId(), longCaptor.getAllValues().get(0));

        verify(view, times(1)).showVideo(videoCaptor.capture());
        assertEquals(video1.getVideoUrl(), videoCaptor.getAllValues().get(0));

    }

}