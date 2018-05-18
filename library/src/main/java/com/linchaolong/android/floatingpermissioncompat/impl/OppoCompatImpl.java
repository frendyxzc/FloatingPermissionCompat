package com.linchaolong.android.floatingpermissioncompat.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by frendy on 2018/5/16.
 */

public class OppoCompatImpl extends BelowApi23CompatImpl {
    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean apply(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName componentName = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity");
            intent.setComponent(componentName);
            context.startActivity(intent);
            return true;
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
