package net.rusnet.sb.mvp.data.model;

import net.rusnet.sb.mvp.presentation.presenter.MainPresenter;

import java.util.Comparator;

public class InstalledPackageModelComparator implements Comparator<InstalledPackageModel> {

    MainPresenter.SortType mSortType;

    public InstalledPackageModelComparator(MainPresenter.SortType sortType) {
        mSortType = sortType;
    }

    @Override
    public int compare(InstalledPackageModel o1, InstalledPackageModel o2) {
        switch (mSortType) {
            case BY_APP_NAME_ASC:
                return o1.getAppName().compareTo(o2.getAppName());
            case BY_PACKAGE_NAME_ASC:
                return o1.getAppPackage().compareTo(o2.getAppPackage());
            case NO_SORT:
            default:
                return 0;
        }
    }
}
