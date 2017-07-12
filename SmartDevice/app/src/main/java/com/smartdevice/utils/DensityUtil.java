package com.smartdevice.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {

	private static final String TAG = DensityUtil.class.getSimpleName();  
    
    // 当前屏幕的densityDpi  
    private static float dmDensityDpi = 0.0f;  
    private static DisplayMetrics dm;  
    private static float scale = 0.0f;  
  
    /** 
     *  
     * 根据构造函数获得当前手机的屏幕系数 
     *  
     * */  
    public DensityUtil(Context context) {  
        // 获取当前屏幕  
        dm = new DisplayMetrics();  
        dm = context.getApplicationContext().getResources().getDisplayMetrics();  
        // 设置DensityDpi  
        setDmDensityDpi(dm.densityDpi);  
        // 密度因子  
        scale = getDmDensityDpi() / 160;  
        //Logger.i(TAG, toString());  
    }  
  
    /** 
     * 当前屏幕的density因子 
     *  
     * @param DmDensity 
     * @retrun DmDensity Getter 
     * */  
    public static float getDmDensityDpi() {  
        return dmDensityDpi;  
    }  
  
    /** 
     * 当前屏幕的density因子 
     *  
     * @param DmDensity 
     * @retrun DmDensity Setter 
     * */  
    public static void setDmDensityDpi(float dmDensityDpi) {  
        DensityUtil.dmDensityDpi = dmDensityDpi;  
    }  
  
    /** 
     * dip to pixel
     * */  
    public int dip2px(float dipValue) {  
  
        return (int) (dipValue * scale + 0.5f);  
  
    }  
  
    /** 
     * pixel to dip
     * */  
    public int px2dip(float pxValue) {  
        return (int) (pxValue / scale + 0.5f);  
    }  
  
    @Override  
    public String toString() {  
        return " dmDensityDpi:" + dmDensityDpi;  
    }  
}
