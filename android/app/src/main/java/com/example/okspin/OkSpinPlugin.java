package com.example.okspin;

import android.content.Context;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class OkSpinPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {
    private Context applicationContext;
    private MethodChannel channel;
    private OkSpinEntryFactory factory;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        applicationContext = binding.getApplicationContext();
        // 创建通道对象，通道名称与Flutter端通道的名称一致
        channel = new MethodChannel(binding.getBinaryMessenger(),"plugins.flutter.io/okspin_plugin");
        // 注册此通道的监听
        channel.setMethodCallHandler(this);
        // 创建PlatformView的工厂对象
        factory = new OkSpinEntryFactory();
        // 在Flutter引擎中注册PlatformView的工厂对象
        binding.getPlatformViewRegistry().registerViewFactory(
                "plugins.flutter.io/okspin_entry_view", factory);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        // 释放资源，取消监听
        applicationContext = null;
        channel.setMethodCallHandler(null);
        channel = null;
        factory = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "initSDK":
                OkSpinManager.initSDK(applicationContext, result);
                break;
            case "getEntryView":
                OkSpinManager.getEntryView(applicationContext, result, factory);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
