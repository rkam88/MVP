package net.rusnet.sb.mvp.presentation.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.mvp.R;
import net.rusnet.sb.mvp.data.model.InstalledPackageModel;

import java.util.ArrayList;
import java.util.List;

public class PackageInstalledRecyclerAdapter extends RecyclerView.Adapter<PackageInstalledRecyclerAdapter.PackageInstalledViewHolder> {

    private final int USER = 0, SYSTEM = 1;

    private List<InstalledPackageModel> mInstalledPackageModelList;

    public PackageInstalledRecyclerAdapter(List<InstalledPackageModel> installedPackageModelList) {
        mInstalledPackageModelList = new ArrayList<>(installedPackageModelList);
    }

    @NonNull
    @Override
    public PackageInstalledViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PackageInstalledViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == USER) {
            View view = inflater.inflate(R.layout.package_installed_view_item, parent, false);
            holder = new PackageInstalledViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.system_package_installed_view_item, parent, false);
            holder = new PackageInstalledViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageInstalledViewHolder holder, int position) {
        holder.bindView(mInstalledPackageModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mInstalledPackageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mInstalledPackageModelList.get(position).isSystem()) {
            return SYSTEM;
        } else {
            return USER;
        }
    }

    static class PackageInstalledViewHolder extends RecyclerView.ViewHolder {

        private TextView mAppTextView;
        private TextView mPackageNameTextView;
        private ImageView mIconImageView;

        public PackageInstalledViewHolder(@NonNull View itemView) {
            super(itemView);

            mAppTextView = itemView.findViewById(R.id.app_name_text_view);
            mPackageNameTextView = itemView.findViewById(R.id.app_package_text_view);
            mIconImageView = itemView.findViewById(R.id.app_icon_image_view);
        }

        void bindView(InstalledPackageModel installedPackageModel) {
            mAppTextView.setText(installedPackageModel.getAppName());
            mPackageNameTextView.setText(installedPackageModel.getAppPackage());
            mIconImageView.setImageDrawable(installedPackageModel.getAppIcon());
        }
    }
}
