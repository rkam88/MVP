package net.rusnet.sb.mvp.presentation.presenter;

import net.rusnet.sb.mvp.data.model.InstalledPackageModel;
import net.rusnet.sb.mvp.data.model.InstalledPackageModelComparator;
import net.rusnet.sb.mvp.data.repository.PackageInstalledRepository;
import net.rusnet.sb.mvp.presentation.view.IMainActivity;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class MainPresenter {
    private WeakReference<IMainActivity> mMainActivity;
    private PackageInstalledRepository mPackageInstalledRepository;

    private boolean mLoadSystem;

    private SortType mSortType;

    public MainPresenter(IMainActivity mainActivity, PackageInstalledRepository packageInstalledRepository, boolean loadSystem, SortType sortType) {
        mMainActivity = new WeakReference<>(mainActivity);
        mPackageInstalledRepository = packageInstalledRepository;
        mLoadSystem = loadSystem;
        mSortType = sortType;
    }

    public void loadData() {
        if (mMainActivity.get() != null) {
            mMainActivity.get().showProgress();
        }

        List<InstalledPackageModel> data = mPackageInstalledRepository.getData(mLoadSystem);

        if (mMainActivity.get() != null) {
            mMainActivity.get().hideProgress();
            mMainActivity.get().showData(data);
        }
    }

    public void setLoadSystemState(boolean loadSystem) {
        mLoadSystem = loadSystem;
    }

    public void setSortType(SortType sortType) {
        mSortType = sortType;
    }

    /**
     * Метод для загрузки данных в ассинхронном режиме.
     */
    public void loadDataAsync() {
        if (mMainActivity.get() != null) {
            mMainActivity.get().showProgress();
        }

        PackageInstalledRepository.OnLoadingFinishListener onLoadingFinishListener = new PackageInstalledRepository.OnLoadingFinishListener() {
            @Override
            public void onFinish(List<InstalledPackageModel> packageModels) {
                if (mMainActivity.get() != null) {
                    mMainActivity.get().hideProgress();
                    if (mSortType != MainPresenter.SortType.NO_SORT) {
                        InstalledPackageModelComparator comparator =
                                new InstalledPackageModelComparator(mSortType);
                        Collections.sort(packageModels, comparator);
                    }
                    mMainActivity.get().showData(packageModels);
                }
            }
        };

        mPackageInstalledRepository.loadDataAsync(mLoadSystem, onLoadingFinishListener);
    }

    /**
     * Метод для отвязки прикрепленной View.
     */
    public void detachView() {
        mMainActivity.clear();
    }

    public enum SortType {
        NO_SORT,
        BY_APP_NAME_ASC,
        BY_PACKAGE_NAME_ASC
    }

}


