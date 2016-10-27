package ru.roscha_akademii.medialib.viewvideo.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.video.VideoDb;
import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tse on 19/10/16.
 */
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

    static Video video1 = new Video();
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
        assertEquals((Long) video1.getId(), (Long)videoCaptor.getAllValues().get(0).id);

    }

}