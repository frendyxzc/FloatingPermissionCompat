package com.linchaolong.android.floatingpermissioncompat.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.linchaolong.android.floatingpermissioncompat.Utils;

/**
 * MIUI 悬浮窗权限兼容实现
 *
 * Created by linchaolong on 2016/12/26.
 */
public class MiuiCompatImpl extends BelowApi23CompatImpl {

  private static final String TAG = "MiuiCompatImpl";
  private int miuiVersion = -1;

  public MiuiCompatImpl() {
    miuiVersion = Utils.getMiuiVersion();
  }

  @Override public boolean isSupported() {
    return miuiVersion >= 5 && miuiVersion <= 8;
  }

  @Override public boolean apply(Context context) {
    if (miuiVersion == 5) {
      return applyV5(context);
    } else if (miuiVersion == 6) {
      return applyV6(context);
    } else if (miuiVersion == 7) {
      return applyV7(context);
    } else if (miuiVersion == 8) {
      return applyV8(context);
    } else {
      Log.e(TAG, "this is a special MIUI rom version, its version code " + miuiVersion);
    }
    return false;
  }

  /**
   * MIUI8下申请权限
   *
   * @param context
   */
  private boolean applyV8(Context context) {
    Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
    intent.putExtra("extra_pkgname", context.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    }

    intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    intent.setPackage("com.miui.securitycenter");
    intent.putExtra("extra_pkgname", context.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      // 小米平板2上没有安全中心，可以打开应用详情页开启权限
      //I/ActivityManager: START u0 {flg=0x14000000 cmp=com.android.settings/.applications.InstalledAppDetailsTop (has extras)}
      return applyV5(context);
    }
  }

  /**
   * MIUI7下申请权限
   *
   * @param context
   */
  private boolean applyV7(Context context) {
    Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
    intent.putExtra("extra_pkgname", context.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      Log.e(TAG, "applyV7 Intent is not available!");
    }
    return false;
  }

  /**
   * MIUI6下申请权限
   *
   * @param context
   */
  private boolean applyV6(Context context) {
    Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
    intent.putExtra("extra_pkgname", context.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      Log.e(TAG, "applyV6 Intent is not available!");
    }
    return false;
  }

  /**
   * MIUI5下申请权限
   *
   * @param context
   */
  private boolean applyV5(Context context) {
    Intent intent;
    String packageName = context.getPackageName();
    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.fromParts("package", packageName, null);
    intent.setData(uri);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Utils.isIntentAvailable(context, intent)) {
      context.startActivity(intent);
      return true;
    } else {
      Log.e(TAG, "applyV5 intent is not available!");
    }
    return false;
    //设置页面在应用详情页面
    //Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
    //PackageInfo pInfo = null;
    //try {
    //    pInfo = context.getPackageManager().getPackageInfo
    //            (HostInterfaceManager.getHostInterface().getApp().getPackageName(), 0);
    //} catch (PackageManager.NameNotFoundException e) {
    //    AVLogUtils.e(TAG, e.getMessage());
    //}
    //intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
    //intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //if (isIntentAvailable(intent, context)) {
    //    context.startActivity(intent);
    //} else {
    //    AVLogUtils.e(TAG, "Intent is not available!");
    //}
  }

}
