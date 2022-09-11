package com.example.okspin;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import io.flutter.plugin.platform.PlatformView;

public class OkSpinPlacement implements PlatformView {
    private RelativeLayout container;

    public OkSpinPlacement(@Nullable Context context
    ){
        container = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(layoutParams);
        container.setGravity(Gravity.CENTER);
    }

    @Nullable
    @Override
    public View getView() {
        return container;
    }

    @Override
    public void dispose() {
        container = null;
    }

    public void setPlacement(View placement) {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        if (placement.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) placement.getParent();
            viewGroup.removeView(placement);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80, 80);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(placement, layoutParams);
    }
}
