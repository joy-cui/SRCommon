/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.view.WindowManager;
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
/*    */ public class BrightnessHelper
/*    */ {
/*    */   public static void setBrightness(Activity activity, float brightness) {
/* 26 */     WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
/* 27 */     float _brightness = brightness;
/* 28 */     if (brightness > 1.0F) {
/* 29 */       _brightness = 1.0F;
/*    */     }
/* 31 */     lp.screenBrightness = _brightness;
/* 32 */     activity.getWindow().setAttributes(lp);
/*    */   }
/*    */   
/*    */   public float getBrightness(Activity activity) {
/* 36 */     WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
/* 37 */     return lp.screenBrightness;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\BrightnessHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */