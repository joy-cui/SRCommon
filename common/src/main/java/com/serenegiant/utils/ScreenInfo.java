/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.annotation.SuppressLint;
/*    */ import android.app.Activity;
/*    */ import android.graphics.Point;
/*    */ import android.util.DisplayMetrics;
/*    */ import android.view.Display;
/*    */ import android.view.WindowManager;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
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
/*    */ 
/*    */ 
/*    */ public class ScreenInfo
/*    */ {
/*    */   @SuppressLint({"NewApi"})
/*    */   public static JSONObject get(Activity activity) throws JSONException {
/* 36 */     JSONObject result = new JSONObject();
/*    */     try {
/* 38 */       WindowManager wm = activity.getWindowManager();
/* 39 */       Display display = wm.getDefaultDisplay();
/* 40 */       DisplayMetrics metrics = new DisplayMetrics();
/* 41 */       display.getMetrics(metrics);
/*    */       try {
/* 43 */         result.put("widthPixels", metrics.widthPixels);
/* 44 */       } catch (Exception e) {
/* 45 */         result.put("widthPixels", e.getMessage());
/*    */       } 
/*    */       try {
/* 48 */         result.put("heightPixels", metrics.heightPixels);
/* 49 */       } catch (Exception e) {
/* 50 */         result.put("heightPixels", e.getMessage());
/*    */       } 
/*    */       try {
/* 53 */         result.put("density", metrics.density);
/* 54 */       } catch (Exception e) {
/* 55 */         result.put("density", e.getMessage());
/*    */       } 
/*    */       try {
/* 58 */         result.put("densityDpi", metrics.densityDpi);
/* 59 */       } catch (Exception e) {
/* 60 */         result.put("densityDpi", e.getMessage());
/*    */       } 
/*    */       try {
/* 63 */         result.put("scaledDensity", metrics.scaledDensity);
/* 64 */       } catch (Exception e) {
/* 65 */         result.put("scaledDensity", e.getMessage());
/*    */       } 
/*    */       try {
/* 68 */         result.put("xdpi", metrics.xdpi);
/* 69 */       } catch (Exception e) {
/* 70 */         result.put("xdpi", e.getMessage());
/*    */       } 
/*    */       try {
/* 73 */         result.put("ydpi", metrics.ydpi);
/* 74 */       } catch (Exception e) {
/* 75 */         result.put("ydpi", e.getMessage());
/*    */       } 
/*    */       try {
/* 78 */         Point size = new Point();
/* 79 */         if (BuildCheck.isAndroid4_2()) {
/* 80 */           display.getRealSize(size);
/* 81 */           result.put("width", size.x);
/* 82 */           result.put("height", size.y);
/*    */         } else {
/* 84 */           result.put("width", display.getWidth());
/* 85 */           result.put("height", display.getHeight());
/*    */         } 
/* 87 */       } catch (Exception e) {
/* 88 */         result.put("size", e.getMessage());
/*    */       } 
/* 90 */     } catch (Exception e) {
/* 91 */       result.put("EXCEPTION", e.getMessage());
/*    */     } 
/* 93 */     return result;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\ScreenInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */