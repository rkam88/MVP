package net.rusnet.sb.mvp.presentation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.mvp.R;
import net.rusnet.sb.mvp.data.model.InstalledPackageModel;
import net.rusnet.sb.mvp.data.repository.PackageInstalledRepository;
import net.rusnet.sb.mvp.presentation.presenter.MainPresenter;

import java.util.List;


public class MainActivity extends AppCompatActivity implements IMainActivity {

    private RecyclerView mRecyclerView;
    private View mProgressFrameLayout;

    private CompoundButton mLoadSystemCheckBox;
    private Button mLoadButton;

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
                mLoadSystemCheckBox.isChecked());
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        mProgressFrameLayout = findViewById(R.id.progress_frame_layout);

        mLoadSystemCheckBox = findViewById(R.id.checkbox_show_system);
        mLoadSystemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMainPresenter.setLoadSystemState(isChecked);
            }
        });

        mLoadButton = findViewById(R.id.button_load);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
