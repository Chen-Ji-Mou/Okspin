package com.example.okspin;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class OkSpinPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {
    public OkSpinEntryFactory factory;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        factory = new OkSpinEntryFactory(binding.getBinaryMessenger());
        binding.getPlatformViewRegistry().registerViewFactory("OkSpinEntryView", factory);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        factory = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {

    }
}
