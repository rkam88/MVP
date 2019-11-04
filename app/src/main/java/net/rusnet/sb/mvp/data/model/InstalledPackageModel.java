package net.rusnet.sb.mvp.data.model;


import android.graphics.drawable.Drawable;

import java.util.Objects;

public class InstalledPackageModel {
    private String mAppName;
    private String mAppPackage;
    private Drawable mAppIcon;
    private boolean mIsSystem;

    public InstalledPackageModel(String appName, String appPackage, Drawable appIcon, boolean isSystem) {
        mAppName = appName;
        mAppPackage = appPackage;
        mAppIcon = appIcon;
        mIsSystem = isSystem;
    }

    public String getAppName() {
        return mAppName;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public boolean isSystem() {
        return mIsSystem;
    }

    @Override
    public String toString() {
        return "InstalledPackageModel{" +
                "mAppName='" + mAppName + '\'' +
                ", mAppPackage='" + mAppPackage + '\'' +
                ", mAppIcon=" + mAppIcon +
                ", mIsSystem=" + mIsSystem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstalledPackageModel that = (InstalledPackageModel) o;
        return mIsSystem == that.mIsSystem &&
                Objects.equals(mAppName, that.mAppName) &&
                Objects.equals(mAppPackage, that.mAppPackage) &&
                Objects.equals(mAppIcon, that.mAppIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mAppName, mAppPackage, mAppIcon, mIsSystem);
    }
}
