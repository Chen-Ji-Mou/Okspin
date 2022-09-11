package com.example.okspin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class OkSpinPlacementFactory extends PlatformViewFactory {
    private OkSpinPlacement view;

    public OkSpinPlacementFactory() {
        super(StandardMessageCodec.INSTANCE);
    }

    @NonNull
    @Override
    public PlatformView create(@Nullable Context context, int viewId, Object args) {
        Map<String, Integer> params = (Map<String, Integer>) args;
        view = new OkSpinPlacement(context, params);
        return view;
    }

    public void setPlacement(View placement) {
        view.setPlacement(placement);
    }
}
