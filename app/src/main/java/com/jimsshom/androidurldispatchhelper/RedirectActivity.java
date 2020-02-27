package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class RedirectActivity extends AppCompatActivity {
    private String currentDefaultBrowser = "";
    private String currentSpecialBrowser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        loadFromPersistentStore();

        Intent intent = getIntent();
        Uri uri = intent.getData();

        String url = uri.toString();
        TextView textView = findViewById(R.id.urlPlaceHolderTextView);
        textView.setText(url);

        if (isSpecialUrl(uri)) {
            redirectUrl(currentSpecialBrowser, uri);
        } else {
            redirectUrl(currentDefaultBrowser, uri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    private void loadFromPersistentStore() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        currentDefaultBrowser = preferences.getString(Constants.KEY_DEFAULT_BROWSER, "");
        currentSpecialBrowser = preferences.getString(Constants.KEY_SPECIAL_BROWSER, "");
    }

    private void redirectUrl(String packageName, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(packageName);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //intent.setPackage(null);
            //startActivity(intent);
            System.out.println("Cannot find activity! packageName:" + packageName);
        }
    }

    private boolean isSpecialUrl(Uri uri) {
        if (uri.getHost().endsWith(".baidu.com")) {
            return false;
        }
        return true;
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

        super.onRestoreInstanceState(savedInstanceState);
    }
}
