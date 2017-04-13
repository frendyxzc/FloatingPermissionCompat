package com.linchaolong.android.floatingpermissioncompat.impl;

import android.content.Context;
import android.content.Intent;

/**
 * 魅族悬浮窗权限兼容实现
 *
 * Created by linchaolong on 2016/12/26.
 */
public class MeizuCompatImpl extends BelowApi23CompatImpl {

  @Override public boolean isSupported() {
    return true;
  }

  /**
   * 去魅族权限申请页面
   */
  @Override public boolean apply(Context context) {
    Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
    intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
    intent.putExtra("packageName", context.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
    return true;
  }
}
