/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.os.Environment;
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiskUtils
/*    */ {
/*    */   public static String getCacheDir(Context context, String uniqueName) {
/* 42 */     String cachePath = ("mounted".equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
/* 43 */     return cachePath + File.separator + uniqueName;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\DiskUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */