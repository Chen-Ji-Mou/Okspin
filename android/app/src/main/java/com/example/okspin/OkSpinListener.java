package com.example.okspin;

import android.util.Log;

import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.utils.Error;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.FlutterEngine;

public abstract class OkSpinListener implements OkSpin.SpinListener
{
    public static final String TAG = "OkSpinListener";

    /**
     * SDK初始化成功
     */
    @Override
    public void onInitSuccess() {
        Log.d(TAG, "=> onInitSuccess");
    }

    /**
     * SDK初始化失败
     * @param error
     */
    @Override
    public void onInitFailed(Error error) {
        Log.e(TAG, "=> onInitFailed", new Exception(error.toString()));
    }

    /**
     * Placement 加载成功
     * @param s
     */
    @Override
    public void onIconReady(String s) {
        Log.d(TAG, "=> onIconReady: "+s);
    }

    /**
     * Placement 加载失败
     * @param s
     * @param error
     */
    @Override
    public void onIconLoadFailed(String s, Error error) {
        Log.e(TAG, "=> onIconLoadFailed: "+s, new Exception(error.toString()));
    }

    /**
     * Placement 素材展示失败
     * @param s
     * @param error
     */
    @Override
    public void onIconShowFailed(String s, Error error) {
        Log.e(TAG, "=> onIconShowFailed: "+s, new Exception(error.toString()));
    }

    /**
     * Placement 被点击
     * @param s
     */
    @Override
    public void onIconClick(String s) {
        Log.d(TAG, "=> onIconClick: "+s);
    }

    /**
     * GSpace - Interactive Ads 页面被打开
     * @param s
     */
    @Override
    public void onInteractiveOpen(String s) {
        Log.d(TAG, "=> onInteractiveOpen: "+s);
    }

    /**
     * GSpace - Interactive Ads 页面打开失败
     * @param s
     * @param error
     */
    @Override
    public void onInteractiveOpenFailed(String s, Error error) {
        Log.e(TAG, "=> onInteractiveOpenFailed: "+s, new Exception(error.toString()));
    }

    /**
     * GSpace - Interactive Ads 页面被关闭
     * @param s
     */
    @Override
    public void onInteractiveClose(String s) {
        Log.d(TAG, "=> onInteractiveClose: "+s);
    }

    /**
     * GSpace - Interactive Wall 页面被打开
     * @param s
     */
    @Override
    public void onOfferWallOpen(String s) {
        Log.d(TAG, "=> onOfferWallOpen: "+s);
    }

    /**
     * GSpace - Interactive Wall 页面打开失败
     * @param s
     * @param error
     */
    @Override
    public void onOfferWallOpenFailed(String s, Error error) {
        Log.e(TAG, "=> onOfferWallOpenFailed: "+s, new Exception(error.toString()));
    }

    /**
     * GSpace - Interactive Wall 页面关闭
     * @param s
     */
    @Override
    public void onOfferWallClose(String s) {
        Log.d(TAG, "=> onOfferWallClose: "+s);
    }

    /**
     * GSpace 页面打开
     * @param s
     */
    @Override
    public void onGSpaceOpen(String s) {
        Log.d(TAG, "=> onGSpaceOpen: "+s);
    }

    /**
     * GSpace 页面打开失败
     * @param s
     * @param error
     */
    @Override
    public void onGSpaceOpenFailed(String s, Error error) {
        Log.e(TAG, "=> onGSpaceOpenFailed: "+s, new Exception(error.toString()));
    }

    /**
     * GSpace 页面关闭
     * @param s
     */
    @Override
    public void onGSpaceClose(String s) {
        Log.d(TAG, "=> onGSpaceClose: "+s);
    }

    /**
     * 用户交互行为回调
     * INTERACTIVE_PLAY         GSpace - Interactive Ads 页面互动
     * INTERACTIVE_CLICK        GSpace - Interactive Ads 页面点击广告
     * OFFER_WALL_SHOW_DETAIL   GSpace - Interactive Wall 页面展示Offer详情
     * OFFER_WALL_GET_TASK      GSpace - Interactive Wall 页面领取Offer
     */
    @Override
    public void onUserInteraction(String s, String s1) {
        Log.d(TAG, "=> onInitSuccess "+s+" "+s1);
    }
}
