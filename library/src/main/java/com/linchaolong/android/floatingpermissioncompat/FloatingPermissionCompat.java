package com.linchaolong.android.floatingpermissioncompat;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import com.linchaolong.android.floatingpermissioncompat.impl.Api23CompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.BelowApi23CompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.HuaweiCompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.MeizuCompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.MiuiCompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.OppoCompatImpl;
import com.linchaolong.android.floatingpermissioncompat.impl.QihooCompatImpl;
import java.lang.reflect.Method;

/**
 * 悬浮窗权限兼容类
 *
 *  参考了该项目的代码：https://github.com/zhaozepeng/FloatWindowPermission
 *
 * Created by linchaolong on 2016/12/26.
 */
public class FloatingPermissionCompat {

  private static final String TAG = "FloatPermissionCompat";

  private static FloatingPermissionCompat sInstance;
  private CompatImpl compat;

  private FloatingPermissionCompat(){
    // 6.0 以下的处理
    if (Build.VERSION.SDK_INT < 23) {
      if (Utils.isMiui()) {
        compat = new MiuiCompatImpl();
      } else if (Utils.isMeizu()) {
        compat = new MeizuCompatImpl();
      } else if (Utils.isHuawei()) {
        compat = new HuaweiCompatImpl();
      } else if (Utils.isQihoo()) {
        compat = new QihooCompatImpl();
      } else if(Utils.isOppo()) {
        compat = new OppoCompatImpl();
      } else{
        // Android6.0以下未兼容机型默认实现
        compat = new BelowApi23CompatImpl() {
          @Override public boolean isSupported() {
            return false;
          }
          @Override public boolean apply(Context context) {
            return false;
          }
        };
      }
    } else {
      // 最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
      if(Utils.isMeizu()) {
        compat = new MeizuCompatImpl();
      } else {
        // 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        compat = new Api23CompatImpl();
      }
    }
  }

  /**
   * 获取悬浮窗兼容工具实例
   *
   * @return
   */
  public static FloatingPermissionCompat get(){
    if(sInstance == null){
      sInstance = new FloatingPermissionCompat();
    }
    return sInstance;
  }

  /**
   * 检查是否已开启悬浮窗权限
   *
   * @return
   */
  public boolean check(Context context){
    return compat.check(context);
  }

  /**
   * 是否支持打开悬浮窗授权界面
   *
   * @return
   */
  public boolean isSupported() {
    return compat.isSupported();
  }

  /**
   * 申请悬浮窗权限
   *
   * @return  是否成功打开授权界面
   */
  public boolean apply(Context context){
    if(!isSupported()){
      return false;
    }
    return compat.apply(context);
  }

  /**
   * 检测 op 值判断悬浮窗是否已授权
   *
   * @param context
   * @param op
   * @return
   */
  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static boolean checkOp(Context context, int op) {
    final int version = Build.VERSION.SDK_INT;
    if (version >= 19) {
      AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
      try {
        Class clazz = AppOpsManager.class;
        Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
        return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
      } catch (Exception e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
    } else {
      Log.e(TAG, "Below API 19 cannot invoke!");
    }
    return false;
  }

  public interface CompatImpl{
    /**
     * 检测是否已经权限
     *
     * @param context
     * @return
     */
    boolean check(Context context);

    /**
     * 对于该 ROM 是否已经做了悬浮窗授权的兼容支持
     *
     * @return
     */
    boolean isSupported();

    /**
     * 申请权限
     *
     * @param context
     * @return
     */
    boolean apply(Context context);
  }

}
