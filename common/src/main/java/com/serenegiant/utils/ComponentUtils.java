/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.content.ComponentName;
/*    */ import android.content.Context;
/*    */ import android.content.pm.PackageManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComponentUtils
/*    */ {
/* 27 */   public static void disable(Context context, Class<?> clazz) { setComponentState(context, clazz, false); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static void enable(Context context, Class<?> clazz) { setComponentState(context, clazz, true); }
/*    */ 
/*    */   
/*    */   public static void setComponentState(Context context, Class<?> clazz, boolean enabled) {
/* 35 */     int newState = enabled ? 1 : 2;
/* 36 */     ComponentName componentName = new ComponentName(context, clazz);
/* 37 */     PackageManager pm = context.getPackageManager();
/* 38 */     pm.setComponentEnabledSetting(componentName, newState, 1);
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\ComponentUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */