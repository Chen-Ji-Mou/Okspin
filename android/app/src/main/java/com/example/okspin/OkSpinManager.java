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
                _return(result, null, null);
            }

            @Override
            public void onInitFailed(Error error)
            {
                super.onInitFailed(error);
                _return(result, error, "initSDK => onInitFailed");
            }
        });
        // 初始化SDK
        OkSpin.initSDK(context.getString(R.string.okspin_app_key));
        // 设置是否为debug模式
        OkSpin.debug(BuildConfig.DEBUG);
    }

    public static void getPlacement(Context context, MethodChannel.Result result, OkSpinPlacementFactory factory) {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onIconReady(String s)
            {
                super.onIconReady(s);
                factory.setPlacement(OkSpin.showIcon(placementId));
                _return(result, null, null);
            }

            @Override
            public void onIconLoadFailed(String s, Error error)
            {
                super.onIconLoadFailed(s, error);
                _return(result, error, "getPlacement => onIconLoadFailed");
            }

            @Override
            public void onIconShowFailed(String s, Error error)
            {
                super.onIconShowFailed(s, error);
                _return(result, error, "getPlacement => onIconShowFailed");
            }
        });
        // 加载placement
        OkSpin.loadIcon(placementId);
    }

    static void _return(MethodChannel.Result result, Error error, String errorMsg) {
        // 返回结果到flutter端
        if (error != null) {
            result.error(String.valueOf(error.getCode()), errorMsg, error.toString());
        } else {
            result.success(true);
        }
        // 取消监听
        OkSpin.setListener(null);
    }
}
