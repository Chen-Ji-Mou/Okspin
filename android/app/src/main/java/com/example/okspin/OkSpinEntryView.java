package com.example.okspin;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class OkSpinEntryView implements PlatformView {
    final FrameLayout layout;

    public OkSpinEntryView(Context context, BinaryMessenger messenger,
                           int id, Map<String, Object> params
    ){
        layout = new FrameLayout(context);
    }

    @Nullable
    @Override
    public View getView() {
        return layout;
    }

    @Override
    public void dispose() {}
}
