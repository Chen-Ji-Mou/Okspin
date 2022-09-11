package com.example.okspin;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import io.flutter.plugin.platform.PlatformView;

public class OkSpinEntryView implements PlatformView {
    private RelativeLayout container;

    public OkSpinEntryView(@Nullable Context context
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

    public void setEntryView(View entryView) {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        if (entryView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) entryView.getParent();
            viewGroup.removeView(entryView);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80, 80);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(entryView, layoutParams);
    }
}
