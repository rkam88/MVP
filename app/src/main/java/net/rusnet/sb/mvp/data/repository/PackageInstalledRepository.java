package net.rusnet.sb.mvp.data.repository;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import net.rusnet.sb.mvp.R;
import net.rusnet.sb.mvp.data.model.InstalledPackageModel;

import java.util.ArrayList;
import java.util.List;

public class PackageInstalledRepository {

    private final Context mContext;
    private final PackageManager mPackageManager;

    public PackageInstalledRepository(@NonNull Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    public List<InstalledPackageModel> getData(boolean isSystem) {
        List<InstalledPackageModel> installedPackageModels = new ArrayList<>();

        for (String packageName : getInstalledPackages(isSystem)) {
            //getAppSize(packageName); // для замедления

            InstalledPackageModel installedPackageModel = new InstalledPackageModel(
                    getAppName(packageName), packageName, getAppIcon(packageName), isSystem && isSystemPackage(packageName));
            installedPackageModels.add(installedPackageModel);
        }

        return installedPackageModels;
    }

    public void loadDataAsync(boolean isSystem, @NonNull OnLoadingFinishListener onLoadingFinishListener) {
        LoadingPackagesAsyncTask loadingPackagesAsyncTask = new LoadingPackagesAsyncTask(onLoadingFinishListener);
        loadingPackagesAsyncTask.execute(isSystem);
    }

    private List<String> getInstalledPackages(boolean isSystem) {
        List<String> apkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (isSystem || !isSystemPackage(resolveInfo)) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                apkPackageName.add(activityInfo.applicationInfo.packageName);
            }
        }
        return apkPackageName;
    }

    private String getAppName(@NonNull String packageName) {
        String appName = "";
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = mPackageManager.getApplicationInfo(packageName, 0);
            appName = (String) mPackageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    private Drawable getAppIcon(@NonNull String packageName) {
        Drawable drawable;
        try {
            drawable = mPackageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    private boolean isSystemPackage(@NonNull ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private boolean isSystemPackage(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (isSystemPackage(resolveInfo) &&
                    packageName.equals(activityInfo.applicationInfo.packageName)) return true;
        }
        return false;
    }

    private int getAppSize(@NonNull String packageName) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private class LoadingPackagesAsyncTask extends AsyncTask<Boolean, Void, List<InstalledPackageModel>> {

        private final OnLoadingFinishListener mOnLoadingFinishListener;

        LoadingPackagesAsyncTask(@NonNull OnLoadingFinishListener onLoadingFinishListener) {
            mOnLoadingFinishListener = onLoadingFinishListener;
        }

        @Override
        protected void onPostExecute(List<InstalledPackageModel> installedPackageModels) {
            super.onPostExecute(installedPackageModels);

            mOnLoadingFinishListener.onFinish(installedPackageModels);
        }

        @Override
        protected List<InstalledPackageModel> doInBackground(Boolean... booleans) {
            return getData(booleans[0]);
        }
    }

    public interface OnLoadingFinishListener {
        void onFinish(List<InstalledPackageModel> packageModels);
    }
}
