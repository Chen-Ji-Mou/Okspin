package com.example.okspin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class OkSpinEntryFactory extends PlatformViewFactory {
    private OkSpinEntryView view;

    public OkSpinEntryFactory() {
        super(StandardMessageCodec.INSTANCE);
    }

    @NonNull
    @Override
    public PlatformView create(@Nullable Context context, int viewId, @Nullable Object args) {
        view = new OkSpinEntryView(context);
        return view;
    }

    public void setEntryView(View entryView) {
        new Handler(Looper.getMainLooper()).post(() -> view.setEntryView(entryView));
    }
}
