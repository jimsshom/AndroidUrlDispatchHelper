package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainPageActivity extends AppCompatActivity {
    private static int REQUEST_CODE_DEFAULT_BROWSER = 1;
    private static int REQUEST_CODE_SPECIAL_BROWSER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
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
        System.out.println(data.getStringExtra(Constants.EXTRA_KEY_SELECTED_PACKAGE_NAME));

        if (resultCode != Activity.RESULT_OK) {
            System.out.println("AppSelectActivity fail!");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        String packageName = data.getStringExtra(Constants.EXTRA_KEY_SELECTED_PACKAGE_NAME);
        PackageManager packageManager = getPackageManager();
        Drawable newIcon;
        try {
            newIcon = packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == REQUEST_CODE_DEFAULT_BROWSER) {
            ImageView imageView = (ImageView) findViewById(R.id.defaultImageView);
            imageView.setImageDrawable(newIcon);
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.specialImageView);
            imageView.setImageDrawable(newIcon);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
