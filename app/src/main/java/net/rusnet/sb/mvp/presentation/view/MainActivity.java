package net.rusnet.sb.mvp.presentation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.mvp.R;
import net.rusnet.sb.mvp.data.model.InstalledPackageModel;
import net.rusnet.sb.mvp.data.repository.PackageInstalledRepository;
import net.rusnet.sb.mvp.presentation.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IMainActivity {

    private View mProgressFrameLayout;

    private CompoundButton mLoadSystemCheckBox;
    private Button mLoadButton;

    private Spinner mSortBySpinner;

    private RecyclerView mRecyclerView;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        providePresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void providePresenter() {
        PackageInstalledRepository packageInstalledRepository =
                new PackageInstalledRepository(this);
        mMainPresenter = new MainPresenter(this,
                packageInstalledRepository,
                mLoadSystemCheckBox.isChecked(),
                getSortTypeFromSpinner());
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        mProgressFrameLayout = findViewById(R.id.progress_frame_layout);

        mLoadSystemCheckBox = findViewById(R.id.checkbox_show_system);

        mSortBySpinner = findViewById(R.id.spinner_sort_by);
        ArrayList<String> sortTypes = new ArrayList<>();
        sortTypes.add(getString(R.string.no_sort));
        sortTypes.add(getString(R.string.by_app_name_asc));
        sortTypes.add(getString(R.string.by_package_name_asc));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_spinner_item, sortTypes
        );
        mSortBySpinner.setAdapter(adapter);

        mLoadButton = findViewById(R.id.button_load);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPresenter.setLoadSystemState(mLoadSystemCheckBox.isChecked());

                mMainPresenter.setSortType(getSortTypeFromSpinner());

                mMainPresenter.loadDataAsync();
            }
        });


    }

    @Override
    public void showProgress() {
        mProgressFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<InstalledPackageModel> modelList) {
        PackageInstalledRecyclerAdapter adapter = new PackageInstalledRecyclerAdapter(modelList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }

    private MainPresenter.SortType getSortTypeFromSpinner() {
        String spinnerText = mSortBySpinner.getSelectedItem().toString();
        if (spinnerText.equals(getString(R.string.by_app_name_asc)))
            return MainPresenter.SortType.BY_APP_NAME_ASC;
        if (spinnerText.equals(getString(R.string.by_package_name_asc)))
            return MainPresenter.SortType.BY_PACKAGE_NAME_ASC;
        return MainPresenter.SortType.NO_SORT;
    }
}
