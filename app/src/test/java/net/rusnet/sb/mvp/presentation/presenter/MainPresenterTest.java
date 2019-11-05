package net.rusnet.sb.mvp.presentation.presenter;

import net.rusnet.sb.mvp.data.model.InstalledPackageModel;
import net.rusnet.sb.mvp.data.repository.PackageInstalledRepository;
import net.rusnet.sb.mvp.presentation.view.IMainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    private IMainActivity mMainActivity;

    @Mock
    private PackageInstalledRepository mPackageInstalledRepository;

    private MainPresenter mMainPresenter;

    @Before
    public void setUp() {
        mMainPresenter = new MainPresenter(mMainActivity,
                mPackageInstalledRepository,
                true,
                MainPresenter.SortType.NO_SORT);
    }

    @Test
    public void testLoadData() {
        when(mPackageInstalledRepository.getData(anyBoolean())).thenReturn(createTestData());

        mMainPresenter.loadData();

        verify(mMainActivity).showProgress();
        verify(mMainActivity).hideProgress();
        verify(mMainActivity).showData(createTestData());
    }

    @Test
    public void testSetLoadSystemState() throws NoSuchFieldException, IllegalAccessException {
        Field field = MainPresenter.class.getDeclaredField("mLoadSystem");
        field.setAccessible(true);

        mMainPresenter.setLoadSystemState(true);
        Assert.assertEquals(true, field.getBoolean(mMainPresenter));

        mMainPresenter.setLoadSystemState(false);
        Assert.assertEquals(false, field.getBoolean(mMainPresenter));
    }

    @Test
    public void testSetSortType() throws NoSuchFieldException, IllegalAccessException {
        Field field = MainPresenter.class.getDeclaredField("mSortType");
        field.setAccessible(true);

        mMainPresenter.setSortType(MainPresenter.SortType.NO_SORT);
        Assert.assertEquals(MainPresenter.SortType.NO_SORT,
                field.get(mMainPresenter));

        mMainPresenter.setSortType(MainPresenter.SortType.BY_APP_NAME_ASC);
        Assert.assertEquals(MainPresenter.SortType.BY_APP_NAME_ASC,
                field.get(mMainPresenter));

        mMainPresenter.setSortType(MainPresenter.SortType.BY_PACKAGE_NAME_ASC);
        Assert.assertEquals(MainPresenter.SortType.BY_PACKAGE_NAME_ASC,
                field.get(mMainPresenter));
    }

    private List<InstalledPackageModel> createTestData() {
        List<InstalledPackageModel> testData = new ArrayList<>();
        testData.add(new InstalledPackageModel("Sberbank", "ru.sberbankmobile", null, false));
        testData.add(new InstalledPackageModel("Test", "TestPackage", null, false));
        return testData;
    }

    /**
     * Тестирование асинхронного метода получения данных в презентере.
     */
    @Test
    public void testLoadDataAsync() {
        //Здесь происходит магия. Нам нужно выдернуть аргмуент, переданный в mPackageInstalledRepository в качетсве слушателя и немедленно вернуть
        //какой-то результат. Ведь нам неважно, каким образом отработает mPackageInstalledRepository#loadDataAsync(), важно, что этот метод должен вернуть
        //в колбеке.
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                //получаем слушателя из метода loadDataAsync().
                PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener =
                        (PackageInstalledRepository.OnLoadingFinishListener) invocation.getArguments()[1];

                //кидаем в него ответи
                onLoadingFinishListener.onFinish(createTestData());

                return null;
            }
        }).when(mPackageInstalledRepository).loadDataAsync(anyBoolean(), Mockito.any(PackageInstalledRepository.OnLoadingFinishListener.class));

        mMainPresenter.loadDataAsync();

        //Далее просто проверяем, что все будет вызвано в нужном порядке.
        InOrder inOrder = Mockito.inOrder(mMainActivity);
        inOrder.verify(mMainActivity).showProgress();
        inOrder.verify(mMainActivity).hideProgress();
        inOrder.verify(mMainActivity).showData(createTestData());

        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Тестирование {@link MainPresenter#detachView()}.
     *
     * <p> после детача, все методы не будут ничего прокидывать в {@link IMainActivity}.
     */
    @Test
    public void testDetachView() {
        mMainPresenter.detachView();

        mMainPresenter.loadDataAsync();
        mMainPresenter.loadData();

        verifyNoMoreInteractions(mMainActivity);
    }
}