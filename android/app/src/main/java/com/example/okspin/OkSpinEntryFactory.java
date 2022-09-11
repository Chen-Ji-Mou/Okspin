package com.example.okspin;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class OkSpinEntryFactory extends PlatformViewFactory {
    private final BinaryMessenger messenger;
    public OkSpinEntryView entryView;

    public OkSpinEntryFactory(@Nullable BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @NonNull
    @Override
    public PlatformView create(@Nullable Context context, int viewId, @Nullable Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        entryView = new OkSpinEntryView(context, messenger, viewId, params);
        return entryView;
    }
}
