/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.android.floatwindowpermission;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import vip.frendy.kfloat.FloatView;
import vip.frendy.kfloat.interfaces.IFloatView;

/**
 * Description:
 *
 * @author zhaozp 2016-10-17
 * @modify frendy 2017/11/11
 */

public class FloatWindowManager<T> {

	private static final String TAG = "FloatWindowManager";

	private static volatile FloatWindowManager instance;
	private boolean isWindowDismiss = true;
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private FloatView floatView;
	private IFloatView mListener;

	public static FloatWindowManager getInstance() {
		if (instance == null) {
			synchronized (FloatWindowManager.class) {
				if (instance == null) {
					instance = new FloatWindowManager();
				}
			}
		}
		return instance;
	}

	public void setListener(IFloatView listener) {
		mListener = listener;
	}

	/**
	 * 是否已经显示悬浮窗
	 *
	 * @return
	 */
	public boolean isShowing(){
		return floatView != null && floatView.isShowing();
	}

	/**
	 * 显示悬浮窗
	 */
	public void show(Context context, int resId, T args) {
		if (!isWindowDismiss) {
			Log.e(TAG, "view is already added here");
			return;
		}

		isWindowDismiss = false;
		if (windowManager == null) {
			windowManager =
					(WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		}

		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		params = new WindowManager.LayoutParams();
		params.packageName = context.getPackageName();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		params.format = PixelFormat.RGBA_8888;
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.x = screenWidth - dp2px(context, 100);
		params.y = screenHeight - dp2px(context, 171);

		floatView = new FloatView(context);
		floatView.initView(resId, mListener, args);
		floatView.setParams(params);
		floatView.setIsShowing(true);
		windowManager.addView(floatView, params);
	}

	/**
	 * 关闭悬浮窗
	 */
	public void dismiss() {
		if (isWindowDismiss) {
			Log.e(TAG, "window can not be dismiss cause it has not been added");
			return;
		}
		isWindowDismiss = true;
		floatView.setIsShowing(false);
		floatView.destroy();
		if (windowManager != null && floatView != null) {
			windowManager.removeViewImmediate(floatView);
		}
	}

	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

}
