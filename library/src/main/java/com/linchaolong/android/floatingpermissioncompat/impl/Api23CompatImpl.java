package com.linchaolong.android.floatingpermissioncompat.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.linchaolong.android.floatingpermissioncompat.FloatingPermissionCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  Android 6.0 以上悬浮窗权限申请实现
 *
 * Created by linchaolong on 2016/12/26.
 */
public class Api23CompatImpl implements FloatingPermissionCompat.CompatImpl{

  private static final String TAG = "Api23CompatImpl";

  @Override public boolean check(Context context) {
    boolean result = true;
    if (Build.VERSION.SDK_INT >= 23) {
      try {
        Class clazz = Settings.class;
        Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
        result = (Boolean) canDrawOverlays.invoke(null, context);
      } catch (Exception e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
    }
    return result;
  }

  @Override public boolean isSupported() {
    return true;
  }

  /**
   * 通用 rom 权限申请
   *
   * @param context
   * @return
   */
  @Override public boolean apply(Context context) {
    try {
      Class clazz = Settings.class;
      Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
      Intent intent = new Intent(field.get(null).toString());
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setData(Uri.parse("package:" + context.getPackageName()));
      context.startActivity(intent);
      return true;
    } catch (Exception e) {
      Log.e(TAG, Log.getStackTraceString(e));
    }
    return false;
  }

}
