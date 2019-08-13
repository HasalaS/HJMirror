package com.hjess.mirror.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.view.Surface;

import java.lang.reflect.Method;

/**
 * 截屏类
 * Created by HalfmanG2 on 2018/2/13.
 */
public class HJSnap {
    // 屏幕宽度
    private int width;
    // 屏幕高度
    private int height;

    // 截屏方法
    private Method method = null;
    // 截屏参数
    private Object[] size = null;

    @SuppressLint("PrivateApi")
    public HJSnap() {
        String surfaceClassName;
        if (Build.VERSION.SDK_INT <= 17) {
            surfaceClassName = "android.view.Surface";
        } else {
            surfaceClassName = "android.view.SurfaceControl";
        }
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                // Android 9.0方法变更，https://github.com/wejoy/HJMirror/issues/4
                Class[] paramTypes = new Class[]{Rect.class, Integer.TYPE, Integer.TYPE, Integer.TYPE};
                method = Class.forName(surfaceClassName)
                            .getDeclaredMethod("screenshot", paramTypes);

            } else {
                // Android 9.0以前
                Class[] paramTypes = new Class[]{Integer.TYPE, Integer.TYPE};
                method = Class.forName(surfaceClassName)
                        .getDeclaredMethod("screenshot", paramTypes);
            }
        } catch (Exception e) {
            // 打印异常
            HJLog.e(e);
        }
    }

    /**
     * 设置截屏尺寸
     * @param width 宽度
     * @param height 高度
     */
    public void setSize(int width, int height) {
        if (Build.VERSION.SDK_INT >= 28) {
            // Android 9.0方法变更，https://github.com/wejoy/HJMirror/issues/4
            if (width != 0 && height != 0 && this.width != width && this.height != height) {
                size = new Object[]{new Rect(), width, height, Surface.ROTATION_0};
                this.width = width;
                this.height = height;
            }
        } else {
            if (width != 0 && height != 0 && this.width != width && this.height != height) {
                size = new Object[]{width, height};
                this.width = width;
                this.height = height;
            }
        }
    }

    /**
     * 获取屏幕截图
     * @return 截图
     * @throws Exception 截图异常
     */
    public Bitmap getShoot() {
        if (method != null && size != null) {
            try {
                return (Bitmap) method.invoke(null, size);
            } catch (Exception e) {
                // 打印异常
                HJLog.e(e);
                return null;
            }
        }
        return null;
    }
}
