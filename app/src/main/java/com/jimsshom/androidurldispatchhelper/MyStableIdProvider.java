package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

class MyStableIdProvider extends ItemKeyProvider<String> {
    private MyDataAdapter adapter;
    public MyStableIdProvider(MyDataAdapter adapter) {
        super(ItemKeyProvider.SCOPE_CACHED);
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        return adapter.getKeyByPosition(position);
    }

    @Override
    public int getPosition(@NonNull String key) {
        return adapter.getPositionByKey(key);
    }
}
