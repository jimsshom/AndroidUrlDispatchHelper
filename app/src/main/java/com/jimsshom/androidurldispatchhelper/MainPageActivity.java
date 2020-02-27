package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    private static int REQUEST_CODE_DEFAULT_BROWSER = 1;
    private static int REQUEST_CODE_SPECIAL_BROWSER = 2;

    private String currentDefaultBrowser = "";
    private String currentSpecialBrowser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentDefaultBrowser = savedInstanceState.getString(Constants.KEY_DEFAULT_BROWSER);
            currentSpecialBrowser = savedInstanceState.getString(Constants.KEY_SPECIAL_BROWSER);
        } else {
            loadFromPersistentStore();
        }
        if ("".equals(currentSpecialBrowser)) {
            currentSpecialBrowser = getDefaultBrowserOnCreate();
        }
        if ("".equals(currentDefaultBrowser)) {
            currentDefaultBrowser = getDefaultBrowserOnCreate();
        }
        setContentView(R.layout.activity_main_page);
        updateBrowserIcon();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClickDefaultBrowser(View view) {
        Intent intent = new Intent(this, AppSelectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DEFAULT_BROWSER);

        System.out.println("click Default");
    }

    public void onClickSpecialBrowser(View view) {
        System.out.println("click Special");
        Intent intent = new Intent(this, AppSelectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SPECIAL_BROWSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            System.out.println("AppSelectActivity fail!");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        String packageName = data.getStringExtra(Constants.EXTRA_KEY_SELECTED_PACKAGE_NAME);
        if (requestCode == REQUEST_CODE_DEFAULT_BROWSER) {
            currentDefaultBrowser = packageName;
        } else {
            currentSpecialBrowser = packageName;
        }
        savePersistently();
        updateBrowserIcon();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFromPersistentStore() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        currentDefaultBrowser = preferences.getString(Constants.KEY_DEFAULT_BROWSER, "");
        currentSpecialBrowser = preferences.getString(Constants.KEY_SPECIAL_BROWSER, "");
    }

    private void savePersistently() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Constants.KEY_DEFAULT_BROWSER, currentDefaultBrowser);
        edit.putString(Constants.KEY_SPECIAL_BROWSER, currentSpecialBrowser);
        edit.commit();
    }

    private String getDefaultBrowserOnCreate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com"));
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            return "";
        }
        return list.get(0).activityInfo.applicationInfo.packageName;
    }

    private void updateBrowserIcon() {
        System.out.println(currentDefaultBrowser);
        System.out.println(currentSpecialBrowser);

        PackageManager packageManager = getPackageManager();
        try {
            ImageView imageView = findViewById(R.id.defaultImageView);
            Drawable applicationIcon = packageManager.getApplicationIcon(currentDefaultBrowser);
            if (imageView != null && applicationIcon != null) {
                imageView.setImageDrawable(applicationIcon);
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("NameNotFoundException: " + e.getLocalizedMessage() + ", " + currentDefaultBrowser);
            return;
        }
        try {
            ImageView imageView = findViewById(R.id.specialImageView);
            Drawable applicationIcon = packageManager.getApplicationIcon(currentSpecialBrowser);
            if (imageView != null && applicationIcon != null) {
                imageView.setImageDrawable(applicationIcon);
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("NameNotFoundException" + e.getLocalizedMessage() + ", " + currentSpecialBrowser);
            return;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constants.KEY_DEFAULT_BROWSER, currentDefaultBrowser);
        outState.putString(Constants.KEY_SPECIAL_BROWSER, currentSpecialBrowser);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        currentDefaultBrowser = savedInstanceState.getString(Constants.KEY_DEFAULT_BROWSER);
        currentSpecialBrowser = savedInstanceState.getString(Constants.KEY_SPECIAL_BROWSER);
        updateBrowserIcon();

        super.onRestoreInstanceState(savedInstanceState);
    }

    public String getCurrentDefaultBrowser() {
        return currentDefaultBrowser;
    }

    public String getCurrentSpecialBrowser() {
        return currentSpecialBrowser;
    }
}
