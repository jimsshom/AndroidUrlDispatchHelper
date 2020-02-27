package com.jimsshom.androidurldispatchhelper;

import android.graphics.drawable.Drawable;

public class RowData {
    private String appName;
    private String packageName;
    private Drawable appIcon;

    public RowData(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    @Override
    public String toString() {
        return "RowData{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
