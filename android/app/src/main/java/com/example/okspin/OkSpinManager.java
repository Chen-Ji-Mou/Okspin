package com.example.okspin;

import android.content.Context;
import android.util.Log;

import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.utils.Error;

import androidx.annotation.Nullable;
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
                _return(result, null, null, true);
            }

            @Override
            public void onInitFailed(Error error)
            {
                super.onInitFailed(error);
                _return(result, error, "initSDK => onInitFailed", true);
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
                try {
                    factory.setPlacement(OkSpin.showIcon(placementId));
                    _return(result, null, null, true);
                } catch (NullPointerException exception) {
                    String errorMsg = "onIconReady => setPlacement failed";
                    Log.e(TAG, errorMsg, exception);
                    _return(result, new Error(-1, exception.toString()), errorMsg, true);
                }
            }

            @Override
            public void onIconLoadFailed(String s, Error error)
            {
                super.onIconLoadFailed(s, error);
                _return(result, error, "getPlacement => onIconLoadFailed", true);
            }

            @Override
            public void onIconShowFailed(String s, Error error)
            {
                super.onIconShowFailed(s, error);
                _return(result, error, "getPlacement => onIconShowFailed", true);
            }
        });
        // 加载placement
        OkSpin.loadIcon(placementId);
    }

    public static void openGSpace(Context context, MethodChannel channel, MethodChannel.Result result) {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onGSpaceOpen(String s)
            {
                super.onGSpaceOpen(s);
                _return(result, null, null, false);
            }

            @Override
            public void onGSpaceOpenFailed(String s, Error error)
            {
                super.onGSpaceOpenFailed(s, error);
                _return(result, error, "openGSpace => onGSpaceOpenFailed", false);
            }

            @Override
            public void onGSpaceClose(String s)
            {
                super.onGSpaceClose(s);
                channel.invokeMethod("onGSpaceClose", null);
                _return(null, null, null, true);
            }
        });
        // 打开GSpace页面
        OkSpin.openGSpace(placementId);
    }

    private static void _return(MethodChannel.Result result, @Nullable Error error,
            @Nullable String errorMsg, boolean cancelListener
    ){
        if (result != null) {
            // 返回结果到flutter端
            if (error != null) {
                result.error(String.valueOf(error.getCode()), errorMsg, error.toString());
            } else {
                result.success(true);
            }
        }
        if (cancelListener) {
            // 取消监听
            OkSpin.setListener(null);
        }
    }
}
