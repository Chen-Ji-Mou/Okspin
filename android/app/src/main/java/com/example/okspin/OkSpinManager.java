package com.example.okspin;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.utils.Error;

public class OkSpinManager {
    public static final OkSpinManager instance = new OkSpinManager();

    private OkSpinManager() {
        // 初始化SDK
        OkSpin.initSDK(Strings.appKey);
        // debug模式
        OkSpin.debug(true);
        // 加载placement
        OkSpin.loadIcon(Strings.placementId);
    }

    public View getEntryView() {
        while (true) {
            if (OkSpin.isIconReady(Strings.placementId)) break;
        }
        return OkSpin.showIcon(Strings.placementId);
    }
}
