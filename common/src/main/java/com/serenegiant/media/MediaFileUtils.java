/*    */ package com.serenegiant.media;
/*    */ 
/*    */ import android.os.Environment;
/*    */ import java.io.File;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.GregorianCalendar;
/*    */ import java.util.Locale;
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
/*    */ public class MediaFileUtils
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaFileUtils";
/* 35 */   private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final File getCaptureFile(String dir_name, String type, String ext) {
/* 44 */     File dir = new File(Environment.getExternalStoragePublicDirectory(type), dir_name);
/*    */     
/* 46 */     dir.mkdirs();
/* 47 */     if (dir.canWrite()) {
/* 48 */       return new File(dir, getDateTimeString() + ext);
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String getDateTimeString() {
/* 58 */     GregorianCalendar now = new GregorianCalendar();
/* 59 */     return mDateTimeFormat.format(now.getTime());
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaFileUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */