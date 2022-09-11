package com.example.okspin;

import android.content.Context;

import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.utils.Error;

import io.flutter.BuildConfig;
import io.flutter.plugin.common.MethodChannel;

public class OkSpinManager {
    public static void initSDK(Context context, MethodChannel.Result result) {
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onInitSuccess()
            {
                super.onInitSuccess();
                result.success("SDK初始化成功");
            }

            @Override
            public void onInitFailed(Error error)
            {
                super.onInitFailed(error);
                result.error(String.valueOf(error.getCode()), "SDK初始化失败", error.toString());
            }
        });
        // 初始化SDK
        OkSpin.initSDK(context.getString(R.string.okspin_app_key));
        // 设置是否为debug模式
        OkSpin.debug(BuildConfig.DEBUG);
    }

    public static void getEntryView(Context context, MethodChannel.Result result, OkSpinEntryFactory factory) {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onIconReady(String s)
            {
                super.onIconReady(s);
                factory.setEntryView(OkSpin.showIcon(placementId));
                result.success("EntryView加载成功");
            }

            @Override
            public void onIconLoadFailed(String s, Error error)
            {
                super.onIconLoadFailed(s, error);
                result.error(String.valueOf(error.getCode()), "EntryView加载失败", error.toString());
            }

            @Override
            public void onIconShowFailed(String s, Error error)
            {
                super.onIconShowFailed(s, error);
                result.error(String.valueOf(error.getCode()), "EntryView无法显示", error.toString());
            }
        });
        // 加载EntryView
        OkSpin.loadIcon(placementId);
    }
}
