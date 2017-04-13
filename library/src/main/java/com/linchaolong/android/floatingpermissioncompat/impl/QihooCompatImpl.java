package com.linchaolong.android.floatingpermissioncompat.impl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.linchaolong.android.floatingpermissioncompat.Utils;

/**
 *  360 悬浮窗权限兼容实现
 *
 * Created by linchaolong on 2016/12/26.
 */
public class QihooCompatImpl extends BelowApi23CompatImpl {

  private static final String TAG = "QihooCompatImpl";

  @Override public boolean isSupported() {
    return true;
  }

  @Override public boolean apply(Context context) {
    Intent intent = new Intent();
    intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.appEnterActivity");
      if (Utils.isIntentAvailable(context, intent)) {
        context.startActivity(intent);
        return true;
      } else {
        Log.e(TAG, "can't open permission page with particular name, please use " +
            "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
      }
    }
    return false;
  }

}
