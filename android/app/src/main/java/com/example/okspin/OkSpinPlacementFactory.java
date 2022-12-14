package com.example.okspin;

import android.content.Context;
import android.view.View;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class OkSpinPlacementFactory extends PlatformViewFactory {
    private OkSpinPlacement placement;

    public OkSpinPlacementFactory() {
        super(StandardMessageCodec.INSTANCE);
    }

    @NonNull
    @Override
    public PlatformView create(@Nullable Context context, int viewId, @Nullable Object args) {
        Map<String, Integer> params = (Map<String, Integer>) args;
        placement = new OkSpinPlacement(context, params);
        return placement;
    }

    public void setPlacement(View icon) throws NullPointerException {
        placement.setPlacement(icon);
    }
}
