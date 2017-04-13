/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.android.floatwindowpermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.linchaolong.android.floatingpermissioncompat.FloatingPermissionCompat;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowActivity extends Activity {

  Activity context;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    context = this;
    findViewById(R.id.btn_show_or_apply).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        // 检查是否已经授权
        if (FloatingPermissionCompat.get().check(context)) {
          FloatWindowManager.get().show(context);
        } else {
          // 授权提示
          new AlertDialog.Builder(context).setTitle("悬浮窗权限未开启")
              .setMessage("你的手机没有授权" + context.getString(R.string.app_name) + "获得悬浮窗权限，视频悬浮窗功能将无法正常使用")
              .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  // 显示授权界面
                  FloatingPermissionCompat.get().apply(context);
                }
              })
              .setNegativeButton("取消", null).show();
        }
      }
    });

    findViewById(R.id.btn_dismiss).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FloatWindowManager.get().dismiss();
      }
    });
  }
}
