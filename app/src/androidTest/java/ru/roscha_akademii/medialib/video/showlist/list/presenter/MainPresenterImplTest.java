package ru.roscha_akademii.medialib.video.showlist.list.presenter;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.roscha_akademii.medialib.common.ActivityNavigator;
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView;
import ru.roscha_akademii.medialib.video.model.remote.Video;
import ru.roscha_akademii.medialib.update.UpdateScheduler;
import ru.roscha_akademii.medialib.video.model.local.VideoDb;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterImplTest {

    private MainPresenterImpl presenter; // SUT

    private UpdateScheduler updateScheduler;
    private ListOfVideoView view;
    private VideoDb videoDb;
    private ActivityNavigator navigator;

    /*
test data
 */
    private static Video video1 = new Video(
            1111,
            "title one",
            "picture url one",
            "description one",
            "video url one",
            LocalDate.parse("2000-01-01"),
            "0:01");

    private static Video video2 = new Video(2222,
            "title two",
            "picture url two",
            "description two",
            "video url two",
            LocalDate.parse("2001-02-02"),
            "0:02");


    @Before
    public void setUp() throws Exception {
        updateScheduler = mock(UpdateScheduler.class);

        videoDb = mock(VideoDb.class);
        view = mock(ListOfVideoView.class);
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
        assertEquals(video1.getId(), dstList.get(0).getId());
        assertEquals(video2.getId(), dstList.get(1).getId());

    }

}