package ru.roscha_akademii.medialib;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by tse on 09/09/16.
 */
public class MainPresenterTest {
    private MainView view;
    private MainPresenterImpl presenter;

    @Before
    public void setUp() {
        view = mock(MainView.class);
        presenter = new MainPresenterImpl();
    }

    @Test
    public void helloClicked_toastShow() {
        presenter.attachView(view);
        presenter.helloClicked();

        verify(view, times(1)).showHelloToast();
    }
}