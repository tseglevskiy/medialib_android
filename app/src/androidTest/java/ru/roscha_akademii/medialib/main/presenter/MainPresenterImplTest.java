package ru.roscha_akademii.medialib.main.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import ru.roscha_akademii.medialib.common.ActivityNavigator;
import ru.roscha_akademii.medialib.main.view.MainView;
import ru.roscha_akademii.medialib.net.model.Video;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.video.VideoDb;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterImplTest {

    private MainPresenterImpl presenter; // SUT

    private UpdateScheduler updateScheduler;
    private MainView view;
    private VideoDb videoDb;
    private ActivityNavigator navigator;

    /*
test data
 */
    private static Video video1 = new Video();

    static {
        video1.id = 1111;
        video1.description = "description one";
        video1.pictureUrl = "picture url one";
        video1.title = "title one";
        video1.videoUrl = "video url one";
    }

    private static Video video2 = new Video();

    static {
        video2.id = 2222;
        video2.description = "description two";
        video2.pictureUrl = "picture url two";
        video2.title = "title two";
        video2.videoUrl = "video url two";
    }


    @Before
    public void setUp() throws Exception {
        updateScheduler = mock(UpdateScheduler.class);

        videoDb = mock(VideoDb.class);
        view = mock(MainView.class);
        navigator = mock(ActivityNavigator.class);

        presenter = new MainPresenterImpl(updateScheduler, videoDb, navigator);
    }

    @Test
    public void start() throws Exception {
        presenter.start();

        verify(updateScheduler, times(1)).startBySchedule();
    }

    @Test
    public void displayVidoeList() {
        ArrayList<Video> srcList = new ArrayList<>();
        srcList.add(video1);
        srcList.add(video2);

        when(videoDb.getAllVideo()).thenReturn(srcList);

        presenter.attachView(view);
        presenter.start();

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(view, times(1)).showVideoList(listCaptor.capture());

        ArrayList<Video> dstList = (ArrayList<Video>) listCaptor.getValue();
        assertEquals(2, dstList.size());
        assertEquals(video1.id, dstList.get(0).id);
        assertEquals(video2.id, dstList.get(1).id);

    }

}