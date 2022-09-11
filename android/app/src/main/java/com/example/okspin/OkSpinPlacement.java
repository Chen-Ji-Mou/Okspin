package com.example.okspin;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Map;

import androidx.annotation.Nullable;
import io.flutter.plugin.platform.PlatformView;

public class OkSpinPlacement implements PlatformView {
    private RelativeLayout container;
    private final int width;
    private final int height;

    public OkSpinPlacement(@Nullable Context context, Map<String, Integer> params) {
        container = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(layoutParams);
        container.setGravity(Gravity.CENTER);

        width = dp2px(params.get("width"));
        height = dp2px(params.get("height"));
    }

    @Nullable
    @Override
    public View getView() {
        return container;
    }

    @Override
    public void dispose() {}

    public void setPlacement(View placement) {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        if (placement.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) placement.getParent();
            viewGroup.removeView(placement);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(placement, layoutParams);
    }


    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
