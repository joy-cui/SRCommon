/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.util.Log;
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
/*     */ 
/*     */ public class LogUtils
/*     */ {
/*     */   public static final int DEBUG_LEVEL_OFF = 0;
/*     */   public static final int DEBUG_LEVEL_ERROR = 1;
/*     */   public static final int DEBUG_LEVEL_WARNING = 2;
/*     */   public static final int DEBUG_LEVEL_INFO = 3;
/*     */   public static final int DEBUG_LEVEL_DEBUG = 4;
/*     */   public static final int DEBUG_LEVEL_VERBOSE = 5;
/*  32 */   private static String TAG = LogUtils.class.getSimpleName();
/*  33 */   private static int LOG_LEVEL = 0;
/*     */   
/*     */   public void tag(String tag) {
/*  36 */     if (tag != null) {
/*  37 */       TAG = tag;
/*     */     } else {
/*  39 */       TAG = LogUtils.class.getSimpleName();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  44 */   public static void logLevel(int level) { LOG_LEVEL = level; }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static int logLevel() { return LOG_LEVEL; }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static void v() { if (LOG_LEVEL >= 5) Log.v(TAG, getMetaInfo());
/*     */      }
/*     */ 
/*     */   
/*  56 */   public static void v(String message) { if (LOG_LEVEL >= 5) Log.v(TAG, getMetaInfo() + null2str(message));
/*     */      }
/*     */ 
/*     */   
/*  60 */   public static void d() { if (LOG_LEVEL >= 4) Log.d(TAG, getMetaInfo());
/*     */      }
/*     */ 
/*     */   
/*  64 */   public static void d(String message) { if (LOG_LEVEL >= 4) Log.d(TAG, getMetaInfo() + null2str(message));
/*     */      }
/*     */ 
/*     */   
/*  68 */   public static void i() { if (LOG_LEVEL >= 3) Log.i(TAG, getMetaInfo());
/*     */      }
/*     */ 
/*     */   
/*  72 */   public static void i(String message) { if (LOG_LEVEL >= 3) Log.i(TAG, getMetaInfo() + null2str(message));
/*     */      }
/*     */ 
/*     */   
/*  76 */   public static void w(String message) { if (LOG_LEVEL >= 2) Log.w(TAG, getMetaInfo() + null2str(message));
/*     */      }
/*     */   
/*     */   public static void w(String message, Throwable e) {
/*  80 */     if (LOG_LEVEL >= 2) {
/*  81 */       Log.w(TAG, getMetaInfo() + null2str(message), e);
/*  82 */       printThrowable(e);
/*  83 */       if (e.getCause() != null) {
/*  84 */         printThrowable(e.getCause());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  90 */   public static void e(String message) { if (LOG_LEVEL >= 1) Log.e(TAG, getMetaInfo() + null2str(message));
/*     */      }
/*     */   
/*     */   public static void e(String message, Throwable e) {
/*  94 */     if (LOG_LEVEL >= 1) {
/*  95 */       Log.e(TAG, getMetaInfo() + null2str(message), e);
/*  96 */       printThrowable(e);
/*  97 */       if (e.getCause() != null) {
/*  98 */         printThrowable(e.getCause());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void e(Throwable e) {
/* 104 */     if (LOG_LEVEL >= 1) {
/* 105 */       printThrowable(e);
/* 106 */       if (e.getCause() != null) {
/* 107 */         printThrowable(e.getCause());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String null2str(String string) {
/* 113 */     if (string == null) {
/* 114 */       return "(null)";
/*     */     }
/* 116 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void printThrowable(Throwable e) {
/* 125 */     Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
/* 126 */     for (StackTraceElement element : e.getStackTrace()) {
/* 127 */       Log.e(TAG, "  at " + getMetaInfo(element));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getMetaInfo() {
/* 139 */     StackTraceElement element = Thread.currentThread().getStackTrace()[4];
/* 140 */     return getMetaInfo(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMetaInfo(StackTraceElement element) {
/* 150 */     String fullClassName = element.getClassName();
/* 151 */     String simpleClassName = fullClassName.substring(fullClassName
/* 152 */         .lastIndexOf(".") + 1);
/* 153 */     String methodName = element.getMethodName();
/* 154 */     int lineNumber = element.getLineNumber();
/*     */     
/* 156 */     String metaInfo = "[" + simpleClassName + "#" + methodName + ":" + lineNumber + "]";
/*     */     
/* 158 */     return metaInfo;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\LogUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */