/*    */ package com.serenegiant.cameracommon;
/*    */ 
/*    */ import android.graphics.drawable.Drawable;
/*    */ import android.view.View;
/*    */ import android.view.ViewGroup;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViewHelper
/*    */ {
/*    */   public static void setBackgroundAll(ViewGroup vg, int color) {
/* 33 */     for (int i = 0, count = vg.getChildCount(); i < count; i++) {
/* 34 */       View child = vg.getChildAt(i);
/* 35 */       child.setBackgroundColor(color);
/* 36 */       if (child instanceof ViewGroup) {
/* 37 */         setBackgroundAll((ViewGroup)child, color);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setBackgroundAll(ViewGroup vg, Drawable dr) {
/* 48 */     for (int i = 0, count = vg.getChildCount(); i < count; i++) {
/* 49 */       View child = vg.getChildAt(i);
/* 50 */       child.setBackground(dr);
/* 51 */       if (child instanceof ViewGroup)
/* 52 */         setBackgroundAll((ViewGroup)child, dr); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\cameracommon\ViewHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */