/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.Context;
/*     */ import android.os.Environment;
/*     */ import android.text.TextUtils;
/*     */ import android.util.Log;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStreamReader;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtils
/*     */ {
/*  35 */   private static final String TAG = FileUtils.class.getSimpleName();
/*     */   
/*  37 */   public static String DIR_NAME = "UsbWebCamera";
/*  38 */   private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final File getCaptureFile(Context context, String type, String ext, int save_tree_id) { return getCaptureFile(context, type, null, ext, save_tree_id); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final File getCaptureFile(Context context, String type, String prefix, String ext, int save_tree_id) {
/*  52 */     File result = null;
/*  53 */     String file_name = (TextUtils.isEmpty(prefix) ? getDateTimeString() : (prefix + getDateTimeString())) + ext;
/*  54 */     if (save_tree_id > 0 && SDUtils.hasStorageAccess(context, save_tree_id)) {
/*     */       
/*  56 */       result = SDUtils.createStorageDir(context, save_tree_id);
/*  57 */       if (result == null || !result.canWrite()) {
/*  58 */         Log.w(TAG, "なんでか書き込めん");
/*  59 */         result = null;
/*     */       } 
/*     */     } 
/*  62 */     if (result == null) {
/*     */       
/*  64 */       File dir = getCaptureDir(context, type, 0);
/*  65 */       if (dir != null) {
/*  66 */         dir.mkdirs();
/*  67 */         if (dir.canWrite()) {
/*  68 */           result = dir;
/*     */         }
/*     */       } 
/*     */     } 
/*  72 */     if (result != null) {
/*  73 */       result = new File(result, file_name);
/*     */     }
/*     */     
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @SuppressLint({"NewApi"})
/*     */   public static final File getCaptureDir(Context context, String type, int save_tree_id) {
/*  82 */     File result = null;
/*  83 */     if (save_tree_id > 0 && SDUtils.hasStorageAccess(context, save_tree_id)) {
/*  84 */       result = SDUtils.createStorageDir(context, save_tree_id);
/*     */     }
/*     */     
/*  87 */     File dir = (result != null) ? result : new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
/*  88 */     dir.mkdirs();
/*     */     
/*  90 */     if (dir.canWrite()) {
/*  91 */       return dir;
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getDateTimeString() {
/* 100 */     GregorianCalendar now = new GregorianCalendar();
/* 101 */     return mDateTimeFormat.format(now.getTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getExternalMounts() {
/* 106 */     String externalpath = null;
/* 107 */     String internalpath = "";
/*     */     
/* 109 */     Runtime runtime = Runtime.getRuntime();
/*     */     
/*     */     try {
/* 112 */       Process proc = runtime.exec("mount");
/* 113 */       BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream())); String line;
/* 114 */       while ((line = br.readLine()) != null) {
/*     */         
/* 116 */         if (line.contains("secure") || 
/* 117 */           line.contains("asec"))
/*     */           continue; 
/* 119 */         if (line.contains("fat")) {
/* 120 */           String[] columns = line.split(" ");
/* 121 */           if (columns != null && columns.length > 1 && !TextUtils.isEmpty(columns[1])) {
/* 122 */             externalpath = columns[1];
/* 123 */             if (!externalpath.endsWith("/"))
/* 124 */               externalpath = externalpath + "/"; 
/*     */           }  continue;
/*     */         } 
/* 127 */         if (line.contains("fuse")) {
/* 128 */           String[] columns = line.split(" ");
/* 129 */           if (columns != null && columns.length > 1) {
/* 130 */             internalpath = internalpath.concat("[" + columns[1] + "]");
/*     */           }
/*     */         } 
/*     */       } 
/* 134 */     } catch (Exception e) {
/* 135 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 139 */     return externalpath;
/*     */   }
/*     */ 
/*     */   
/* 143 */   public static float FREE_RATIO = 0.03F;
/* 144 */   public static float FREE_SIZE_OFFSET = 2.097152E7F;
/* 145 */   public static float FREE_SIZE = 3.145728E8F;
/* 146 */   public static float FREE_SIZE_MINUTE = 4.194304E7F;
/* 147 */   public static long CHECK_INTERVAL = 45000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean checkFreeSpace(Context context, long max_duration, long start_time, int save_tree_id) {
/* 157 */     if (context == null) return false; 
/* 158 */     return checkFreeSpace(context, FREE_RATIO, (max_duration > 0L) ? (
/*     */         
/* 160 */         (float)(max_duration - System.currentTimeMillis() - start_time) / 60000.0F * FREE_SIZE_MINUTE + FREE_SIZE_OFFSET) : FREE_SIZE, save_tree_id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean checkFreeSpace(Context context, float ratio, float minFree, int save_tree_id) {
/* 173 */     if (context == null) return false; 
/* 174 */     boolean result = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 182 */       File dir = getCaptureDir(context, Environment.DIRECTORY_DCIM, save_tree_id);
/*     */       
/* 184 */       if (dir != null) {
/* 185 */         float freeSpace = dir.canWrite() ? (float)dir.getUsableSpace() : 0.0F;
/* 186 */         if (dir.getTotalSpace() > 0L) {
/* 187 */           result = (freeSpace / (float)dir.getTotalSpace() > ratio || freeSpace > minFree);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 194 */     catch (Exception e) {
/* 195 */       Log.w("checkFreeSpace:", e);
/*     */     } 
/* 197 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getAvailableFreeSpace(Context context, String type, int save_tree_id) {
/* 208 */     long result = 0L;
/* 209 */     if (context != null) {
/* 210 */       File dir = getCaptureDir(context, type, save_tree_id);
/* 211 */       if (dir != null) {
/* 212 */         result = dir.canWrite() ? dir.getUsableSpace() : 0L;
/*     */       }
/*     */     } 
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getFreeRatio(Context context, String type, int save_tree_id) {
/* 226 */     if (context != null) {
/* 227 */       File dir = getCaptureDir(context, type, save_tree_id);
/* 228 */       if (dir != null) {
/* 229 */         float freeSpace = dir.canWrite() ? (float)dir.getUsableSpace() : 0.0F;
/* 230 */         if (dir.getTotalSpace() > 0L) {
/* 231 */           return freeSpace / (float)dir.getTotalSpace();
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String removeFileExtension(String path) {
/* 244 */     int ix = !TextUtils.isEmpty(path) ? path.lastIndexOf(".") : -1;
/* 245 */     if (ix > 0) {
/* 246 */       return path.substring(0, ix);
/*     */     }
/* 248 */     return path;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\FileUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */