package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AppSelectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyDataAdapter myDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com"));
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);

        ArrayList<RowData> dataList = new ArrayList<RowData>();
        for (ResolveInfo resolveInfo : list) {
            ApplicationInfo applicationInfo = resolveInfo.activityInfo.applicationInfo;
            String label = packageManager.getApplicationLabel(applicationInfo).toString();
            String packageName = applicationInfo.packageName;
            if (Constants.PACKAGE_NAME.equals(packageName)) {
                continue;
            }
            Drawable icon = resolveInfo.loadIcon(packageManager);
            dataList.add(new RowData(label, packageName, icon));
        }

        RowData[] dataset = new RowData[dataList.size()];
        for(int i = 0; i < dataset.length; i++) {
            dataset[i] = dataList.get(i);
        }

        myDataAdapter = new MyDataAdapter(dataset);
        recyclerView.setAdapter(myDataAdapter);

        SelectionTracker tracker = new SelectionTracker.Builder(
                "my-uri-selection",
                recyclerView,
                new MyStableIdProvider(myDataAdapter),
                new MyDetailsLookup(recyclerView),
                StorageStrategy.createStringStorage())
                .withSelectionPredicate(SelectionPredicates.<String>createSelectSingleAnything())
                .build();

        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
                System.out.println("key: " + key + ", " + selected);

                if (selected) {
                    Intent result = new Intent();
                    result.putExtra(Constants.EXTRA_KEY_SELECTED_PACKAGE_NAME, key.toString());
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }
        });

        myDataAdapter.setSelectionTracker(tracker);
    }
}
