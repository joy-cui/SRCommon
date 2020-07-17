/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.os.Build;
/*     */ import android.preference.PreferenceManager;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Map;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
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
/*     */ public final class CrashExceptionHandler
/*     */   implements Thread.UncaughtExceptionHandler
/*     */ {
/*     */   static final String LOG_NAME = "crashrepo.txt";
/*     */   static final String MAIL_TO = "t_saki@serenegiant.com";
/*     */   private final WeakReference<Context> mWeakContext;
/*     */   private final WeakReference<PackageInfo> mWeakPackageInfo;
/*     */   private final Thread.UncaughtExceptionHandler mHandler;
/*     */   
/*  45 */   public static void registerCrashHandler(Context app_context) { Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(app_context)); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CrashExceptionHandler(Context context) {
/*  76 */     this.mWeakContext = new WeakReference<>(context);
/*     */     try {
/*  78 */       this
/*  79 */         .mWeakPackageInfo = new WeakReference<>(context.getPackageManager().getPackageInfo(context.getPackageName(), 0));
/*  80 */     } catch (PackageManager.NameNotFoundException e) {
/*  81 */       throw new RuntimeException((Throwable)e);
/*     */     } 
/*  83 */     this.mHandler = Thread.getDefaultUncaughtExceptionHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uncaughtException(Thread thread, Throwable throwable) {
/*  91 */     Context context = this.mWeakContext.get();
/*  92 */     if (context != null) {
/*  93 */       PrintWriter writer = null;
/*     */       try {
/*  95 */         FileOutputStream outputStream = context.openFileOutput("crashrepo.txt", 0);
/*  96 */         writer = new PrintWriter(outputStream);
/*  97 */         JSONObject json = new JSONObject();
/*  98 */         json.put("Build", getBuildInfo());
/*  99 */         json.put("PackageInfo", getPackageInfo());
/* 100 */         json.put("Exception", getExceptionInfo(throwable));
/* 101 */         json.put("SharedPreferences", getPreferencesInfo());
/* 102 */         writer.print(json.toString());
/* 103 */         writer.flush();
/* 104 */       } catch (FileNotFoundException e) {
/* 105 */         e.printStackTrace();
/* 106 */       } catch (JSONException e) {
/* 107 */         e.printStackTrace();
/*     */       } finally {
/* 109 */         if (writer != null) {
/* 110 */           writer.close();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     try {
/* 115 */       if (this.mHandler != null)
/* 116 */         this.mHandler.uncaughtException(thread, throwable); 
/* 117 */     } catch (Exception exception) {}
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
/*     */   private JSONObject getBuildInfo() throws JSONException {
/* 129 */     JSONObject buildJson = new JSONObject();
/* 130 */     buildJson.put("BRAND", Build.BRAND);
/* 131 */     buildJson.put("MODEL", Build.MODEL);
/* 132 */     buildJson.put("DEVICE", Build.DEVICE);
/* 133 */     buildJson.put("MANUFACTURER", Build.MANUFACTURER);
/* 134 */     buildJson.put("VERSION.SDK_INT", Build.VERSION.SDK_INT);
/* 135 */     buildJson.put("VERSION.RELEASE", Build.VERSION.RELEASE);
/* 136 */     return buildJson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONObject getPackageInfo() throws JSONException {
/* 146 */     PackageInfo info = this.mWeakPackageInfo.get();
/* 147 */     JSONObject packageInfoJson = new JSONObject();
/* 148 */     if (info != null) {
/* 149 */       packageInfoJson.put("packageName", info.packageName);
/* 150 */       packageInfoJson.put("versionCode", info.versionCode);
/* 151 */       packageInfoJson.put("versionName", info.versionName);
/*     */     } 
/* 153 */     return packageInfoJson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONObject getExceptionInfo(Throwable throwable) throws JSONException {
/* 164 */     JSONObject exceptionJson = new JSONObject();
/* 165 */     exceptionJson.put("name", throwable.getClass().getName());
/* 166 */     exceptionJson.put("cause", throwable.getCause());
/* 167 */     exceptionJson.put("message", throwable.getMessage());
/*     */     
/* 169 */     JSONArray stackTrace = new JSONArray();
/* 170 */     for (StackTraceElement element : throwable.getStackTrace()) {
/* 171 */       stackTrace.put("at " + LogUtils.getMetaInfo(element));
/*     */     }
/* 173 */     exceptionJson.put("stacktrace", stackTrace);
/* 174 */     return exceptionJson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONObject getPreferencesInfo() throws JSONException {
/* 184 */     SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mWeakContext.get());
/* 185 */     JSONObject preferencesJson = new JSONObject();
/* 186 */     Map<String, ?> map = preferences.getAll();
/* 187 */     for (Map.Entry<String, ?> entry : map.entrySet()) {
/* 188 */       preferencesJson.put(entry.getKey(), entry.getValue());
/*     */     }
/* 190 */     return preferencesJson;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\CrashExceptionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */