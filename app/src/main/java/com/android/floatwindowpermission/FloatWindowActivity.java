/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.android.floatwindowpermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.linchaolong.android.floatingpermissioncompat.FloatingPermissionCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vip.frendy.kfloat.FloatView;
import vip.frendy.kfloat.interfaces.IFloatView;
import vip.frendy.kwebviewext.KWebViewExt;

/**
 * Description:
 *
 * @author zhaozp 2016-10-17
 * @modify frendy 2017/11/11
 */

public class FloatWindowActivity extends Activity implements View.OnClickListener, IFloatView {

    Activity context;
    FloatView mFloatView;
    Button mFloatViewClose;
    KWebViewExt mFloatViewWeb;

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
                FloatWindowManager.getInstance().show(context, R.layout.float_webview, null);
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
        } else if(v.getId() == R.id.close && mFloatViewClose != null && mFloatViewClose.getVisibility() == View.VISIBLE) {
            mFloatViewClose.setVisibility(View.GONE);
            rollupFloatView();
        }
    }

    @Override
    public void onFloatViewCreate(@NotNull FloatView parent, String args) {
        mFloatView = parent;
        mFloatViewClose = parent.findViewById(R.id.close);
        mFloatViewWeb = parent.findViewById(R.id.webview);

        mFloatViewClose.setVisibility(View.GONE);
        mFloatViewClose.setOnClickListener(this);

        mFloatViewWeb.loadUrl("https://www.baidu.com/");
        mFloatViewWeb.setProceedTouchEvent(true);
    }

    @Override
    public void onFloatViewDestroy(@NotNull FloatView parent) {
        Log.i("", "** float view destroy");
    }

    @Override
    public void onFloatViewClick(@NotNull FloatView parent) {
        if(mFloatViewClose.getVisibility() == View.GONE) {
            mFloatViewClose.setVisibility(View.VISIBLE);
            rolloutFloatView();
        }
    }

    /**
     * 铺开悬浮窗
     */
    private void rolloutFloatView() {
        if(mFloatViewWeb == null) return;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFloatViewWeb.getLayoutParams();
        linearParams.width = width - FloatWindowManager.dp2px(this, 24);
        linearParams.height = linearParams.width / 16 * 9;
        mFloatViewWeb.setLayoutParams(linearParams);
    }

    /**
     * 卷起悬浮窗
     */
    private void rollupFloatView() {
        if(mFloatViewWeb == null) return;

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFloatViewWeb.getLayoutParams();
        linearParams.width = FloatWindowManager.dp2px(this, 160);
        linearParams.height = FloatWindowManager.dp2px(this, 90);
        mFloatViewWeb.setLayoutParams(linearParams);
    }
}
