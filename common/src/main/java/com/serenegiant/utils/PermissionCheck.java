/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PermissionGroupInfo;
/*     */ import android.net.Uri;
/*     */ import android.util.Log;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public final class PermissionCheck
/*     */ {
/*     */   public static final void dumpPermissions(Context context) {
/*  39 */     if (context == null)
/*     */       return;  try {
/*  41 */       PackageManager pm = context.getPackageManager();
/*  42 */       List<PermissionGroupInfo> list = pm.getAllPermissionGroups(128);
/*  43 */       for (PermissionGroupInfo info : list) {
/*  44 */         Log.d("PermissionCheck", info.name);
/*     */       }
/*  46 */     } catch (Exception e) {
/*  47 */       Log.w("", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"NewApi"})
/*     */   public static boolean hasPermission(Context context, String permissionName) {
/*  59 */     if (context == null) return false; 
/*  60 */     boolean result = false;
/*     */     try {
/*     */       int check;
/*  63 */       if (BuildCheck.isMarshmallow()) {
/*  64 */         check = context.checkSelfPermission(permissionName);
/*     */       } else {
/*  66 */         PackageManager pm = context.getPackageManager();
/*  67 */         check = pm.checkPermission(permissionName, context.getPackageName());
/*     */       } 
/*  69 */       switch (check) {
/*     */ 
/*     */         
/*     */         case 0:
/*  73 */           result = true;
/*     */           break;
/*     */       } 
/*  76 */     } catch (Exception e) {
/*  77 */       Log.w("", e);
/*     */     } 
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static boolean hasAudio(Context context) { return hasPermission(context, "android.permission.RECORD_AUDIO"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static boolean hasNetwork(Context context) { return hasPermission(context, "android.permission.INTERNET"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static boolean hasWriteExternalStorage(Context context) { return hasPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"InlinedApi"})
/*     */   public static boolean hasReadExternalStorage(Context context) {
/* 116 */     if (BuildCheck.isAndroid4()) {
/* 117 */       return hasPermission(context, "android.permission.READ_EXTERNAL_STORAGE");
/*     */     }
/* 119 */     return hasPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasAccessLocation(Context context) {
/* 128 */     return (hasPermission(context, "android.permission.ACCESS_COARSE_LOCATION") && 
/* 129 */       hasPermission(context, "android.permission.ACCESS_FINE_LOCATION"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static boolean hasAccessCoarseLocation(Context context) { return hasPermission(context, "android.permission.ACCESS_COARSE_LOCATION"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static boolean hasAccessFineLocation(Context context) { return hasPermission(context, "android.permission.ACCESS_FINE_LOCATION"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static boolean hasCamera(Context context) { return hasPermission(context, "android.permission.CAMERA"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void openSettings(Context context) {
/* 164 */     Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
/* 165 */     Uri uri = Uri.fromParts("package", context.getPackageName(), null);
/* 166 */     intent.setData(uri);
/* 167 */     context.startActivity(intent);
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
/* 179 */   public static List<String> missingPermissions(Context context, String[] expectations) throws IllegalArgumentException, PackageManager.NameNotFoundException { return missingPermissions(context, new ArrayList<>(Arrays.asList(expectations))); }
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
/*     */   public static List<String> missingPermissions(Context context, List<String> expectations) throws IllegalArgumentException, PackageManager.NameNotFoundException {
/* 191 */     if (context == null || expectations == null) {
/* 192 */       throw new IllegalArgumentException("context or expectations is null");
/*     */     }
/* 194 */     PackageManager pm = context.getPackageManager();
/* 195 */     PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 4096);
/* 196 */     String[] info = pi.requestedPermissions;
/* 197 */     if (info != null) {
/* 198 */       for (String i : info) {
/* 199 */         expectations.remove(i);
/*     */       }
/*     */     }
/* 202 */     return expectations;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\PermissionCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */