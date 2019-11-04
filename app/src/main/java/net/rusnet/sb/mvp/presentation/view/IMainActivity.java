package net.rusnet.sb.mvp.presentation.view;

import net.rusnet.sb.mvp.data.model.InstalledPackageModel;

import java.util.List;

public interface IMainActivity {

    void showProgress();

    void hideProgress();

    void showData(List<InstalledPackageModel> modelList);

}
