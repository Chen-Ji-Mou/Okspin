package com.example.okspin;

import android.content.Context;
import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.model.GSpaceReward;
import com.spin.ok.gp.utils.Error;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
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
                _return(result, true, null, null, true);
            }

            @Override
            public void onInitFailed(Error error)
            {
                super.onInitFailed(error);
                _return(result, false, error,
                        "initSDK => onInitFailed", true);
            }
        });
        // 初始化SDK
        OkSpin.initSDK(context.getString(R.string.okspin_app_key));
        // 设置是否为debug模式
        OkSpin.debug(BuildConfig.DEBUG);
    }

    public static void getPlacement(Context context, MethodChannel.Result result,
                                    OkSpinPlacementFactory factory)
    {
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
                    _return(result, true, null, null, true);
                } catch (NullPointerException exception) {
                    String errorMsg = "OkSpin.showIcon(placementId) return null";
                    _return(result, false, new Error(-1, errorMsg),
                            null, true);
                }
            }

            @Override
            public void onIconLoadFailed(String s, Error error)
            {
                super.onIconLoadFailed(s, error);
                _return(result, false, error,
                        "getPlacement => onIconLoadFailed", true);
            }

            @Override
            public void onIconShowFailed(String s, Error error)
            {
                super.onIconShowFailed(s, error);
                _return(result, false, error,
                        "getPlacement => onIconShowFailed", true);
            }
        });
        // 加载placement
        OkSpin.loadIcon(placementId);
    }

    public static void openGSpace(Context context, MethodChannel channel,
                                  MethodChannel.Result result, GSpaceRewardCallback callback)
    {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onGSpaceOpen(String s)
            {
                super.onGSpaceOpen(s);
                _return(result, true, null, null, false);
            }

            @Override
            public void onGSpaceOpenFailed(String s, Error error)
            {
                super.onGSpaceOpenFailed(s, error);
                _return(result, false, error,
                        "openGSpace => onGSpaceOpenFailed", false);
            }

            @Override
            public void onGSpaceClose(String s)
            {
                super.onGSpaceClose(s);
                channel.invokeMethod("onGSpaceClose", null);
                _return(null, true, null, null, true);
                // 查询用户在GSpace中已兑换的实物奖品的记录
                OkSpin.queryGSpaceRewards(new OkSpin.QueryGSpaceRewardsCallback()
                {
                    @Override
                    public void onGetGSRewards(GSpaceReward rewardRecord)
                    {
                        // 返回所兑换的实物奖品id到flutter端
                        List<String> records = new ArrayList<>();
                        if (rewardRecord != null) {
                            for (Object record : rewardRecord.getOrders()) {
                                if (record instanceof GSpaceReward.GSpaceOrder) {
                                    GSpaceReward.GSpaceOrder order = (GSpaceReward.GSpaceOrder) record;
                                    records.add(order.getRewardId());
                                }
                            }
                            callback.onGSpaceReward(rewardRecord);
                        }
                        channel.invokeMethod("returnGSpaceRewardIds", records);
                    }

                    @Override
                    public void onGetGSRewardsError(Error error) { /*do nothing*/ }
                });
            }
        });
        // GSpace是否可用
        if (OkSpin.isGSpaceReady(placementId)) {
            // 打开GSpace页面
            OkSpin.openGSpace(placementId);
        } else {
            _return(result, false, null, null, true);
        }
    }

    public static void notifyGSPubTaskPayout(MethodChannel.Result result,
                                             @NonNull GSpaceReward reward,
                                             @NonNull NotifyPayoutCallback callback)
    {
        // 发放GSpace实物奖励完成后通知OKSpin
        OkSpin.notifyGSPubTaskPayout(reward.getOrders(), new OkSpin.GSPubTaskPayoutCallback()
        {
            @Override
            public void onGSPubTaskPayoutSuccess()
            {
                callback.onCall();
                _return(result, true, null, null, false);
            }

            @Override
            public void onGSPubTaskPayoutError(Error error)
            {
                _return(result, false, error,
                        "notifyGSPubTaskPayout => onGSPubTaskPayoutError", false);
            }
        });
    }

    public static void openInteractiveAds(Context context, MethodChannel channel,
                                          MethodChannel.Result result)
    {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onInteractiveOpen(String s)
            {
                super.onInteractiveOpen(s);
                _return(result, true, null, null, false);
            }

            @Override
            public void onInteractiveOpenFailed(String s, Error error)
            {
                super.onInteractiveOpenFailed(s, error);
                _return(result, false, error,
                        "openInteractiveAds => onInteractiveOpenFailed", false);
            }

            @Override
            public void onInteractiveClose(String s)
            {
                super.onInteractiveClose(s);
                channel.invokeMethod("onInteractiveAdsClose", null);
                _return(null, true, null, null, true);
                // 回传sdk获取用户兑换的总奖励值
                OkSpin.payout(new OkSpin.PayoutCallback() {
                    /**
                     * 用户奖励兑换成功
                     * @param totalReward 兑换的总奖励值
                     */
                    @Override
                    public void onPayout(int totalReward) {
                        // 返回用户兑换的总奖励值到flutter端
                        channel.invokeMethod("returnInteractiveAdsTotalReward", totalReward);
                    }

                    @Override
                    public void onPayoutError(Error error) { /*do nothing*/ }
                });
            }
        });
        // Interactive Ads是否可用
        if (OkSpin.isInteractiveReady(placementId)) {
            // 打开Interactive Ads页面
            OkSpin.openInteractive(placementId);
        } else {
            _return(result, false, null, null, true);
        }
    }

    public static void setUserId(MethodChannel.Result result, String userId) {
        try {
            OkSpin.setUserId(userId);
            _return(result, true, null, null, false);
        } catch (Exception exception) {
            String errorMsg = "setUserId failed";
            _return(result, false, new Error(-1, errorMsg), null, true);
        }
    }

    public static void getUserId(MethodChannel.Result result) {
        result.success(OkSpin.getUserId());
    }

    public static void openOfferWall(Context context, MethodChannel channel,
                                     MethodChannel.Result result)
    {
        String placementId = context.getString(R.string.okspin_placement_id);
        // 注册监听
        OkSpin.setListener(new OkSpinListener()
        {
            @Override
            public void onOfferWallOpen(String s)
            {
                super.onOfferWallOpen(s);
                _return(result, true, null, null, false);
            }

            @Override
            public void onOfferWallOpenFailed(String s, Error error)
            {
                super.onOfferWallOpenFailed(s, error);
                _return(result, false, error,
                        "openOfferWall => onOfferWallOpenFailed", false);
            }

            @Override
            public void onOfferWallClose(String s)
            {
                super.onOfferWallClose(s);
                channel.invokeMethod("onOfferWallClose", null);
                _return(null, true, null, null, true);
                // 回传sdk获取用户兑换的总奖励值
                OkSpin.payout(new OkSpin.PayoutCallback() {
                    /**
                     * 用户奖励兑换成功
                     * @param totalReward 兑换的总奖励值
                     */
                    @Override
                    public void onPayout(int totalReward) {
                        // 返回用户兑换的总奖励值到flutter端
                        channel.invokeMethod("returnOfferWallTotalReward", totalReward);
                    }

                    @Override
                    public void onPayoutError(Error error) { /*do nothing*/ }
                });
            }
        });
        // OfferWall是否可用
        if (OkSpin.isOfferWallReady(placementId)) {
            // 打开OfferWall页面
            OkSpin.openOfferWall(placementId);
        } else {
            _return(result, false, null, null, true);
        }
    }

    private static void _return(MethodChannel.Result result, boolean success,
                                @Nullable Error error, @Nullable String errorMsg,
                                boolean cancelListener)
    {
        // 返回结果到flutter端
        if (result != null) {
            if (error != null) {
                result.error(String.valueOf(error.getCode()), errorMsg, error.toString());
            } else {
                result.success(success);
            }
        }
        // 取消监听
        if (cancelListener) {
            OkSpin.setListener(null);
        }
    }
}