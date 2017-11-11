/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.android.floatwindowpermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.linchaolong.android.floatingpermissioncompat.FloatingPermissionCompat;

import org.jetbrains.annotations.NotNull;

import vip.frendy.kfloat.FloatView;
import vip.frendy.kfloat.interfaces.IFloatView;

/**
 * Description:
 *
 * @author zhaozp 2016-10-17
 * @modify frendy 2017/11/11
 */

public class FloatWindowActivity extends Activity implements View.OnClickListener, IFloatView {

    Activity context;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.btn_show_or_apply).setOnClickListener(this);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        FloatWindowManager.getInstance().setListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_show_or_apply) {
            // 检查是否已经授权
            if (FloatingPermissionCompat.get().check(context)) {
                FloatWindowManager.getInstance().show(context, R.layout.float_webview);
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
        } else if(v.getId() == R.id.btn_dismiss) {
            FloatWindowManager.getInstance().dismiss();
        }
    }

    @Override
    public void onFloatViewInit(@NotNull FloatView parent) {
        parent.findViewById(R.id.button).setVisibility(View.GONE);

        WebView webView = (WebView) parent.findViewById(R.id.webview);
    }

    @Override
    public void onFloatViewClick(@NotNull FloatView parent) {
        if(parent.findViewById(R.id.button).getVisibility() == View.GONE) {
            parent.findViewById(R.id.button).setVisibility(View.VISIBLE);
        } else {
            parent.findViewById(R.id.button).setVisibility(View.GONE);
        }
    }
}
