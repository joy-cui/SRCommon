/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.os.Build;
/*     */ import android.text.TextUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
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
/*     */ 
/*     */ 
/*     */ public final class DeviceInfo
/*     */ {
/*     */   @SuppressLint({"NewApi"})
/*     */   public static JSONObject get() throws JSONException {
/*  35 */     JSONObject result = new JSONObject();
/*     */     try {
/*  37 */       result.put("BUILD", Build.ID);
/*  38 */     } catch (Exception e) {
/*  39 */       result.put("BUILD", e.getMessage());
/*     */     } 
/*     */     try {
/*  42 */       result.put("DISPLAY", Build.DISPLAY);
/*  43 */     } catch (Exception e) {
/*  44 */       result.put("DISPLAY", e.getMessage());
/*     */     } 
/*     */     try {
/*  47 */       result.put("PRODUCT", Build.PRODUCT);
/*  48 */     } catch (Exception e) {
/*  49 */       result.put("PRODUCT", e.getMessage());
/*     */     } 
/*     */     try {
/*  52 */       result.put("DEVICE", Build.DEVICE);
/*  53 */     } catch (Exception e) {
/*  54 */       result.put("DEVICE", e.getMessage());
/*     */     } 
/*     */     try {
/*  57 */       result.put("BOARD", Build.BOARD);
/*  58 */     } catch (Exception e) {
/*  59 */       result.put("BOARD", e.getMessage());
/*     */     } 
/*     */     try {
/*  62 */       result.put("MANUFACTURER", Build.MANUFACTURER);
/*  63 */     } catch (Exception e) {
/*  64 */       result.put("MANUFACTURER", e.getMessage());
/*     */     } 
/*     */     try {
/*  67 */       result.put("BRAND", Build.BRAND);
/*  68 */     } catch (Exception e) {
/*  69 */       result.put("BRAND", e.getMessage());
/*     */     } 
/*     */     try {
/*  72 */       result.put("MODEL", Build.MODEL);
/*  73 */     } catch (Exception e) {
/*  74 */       result.put("MODEL", e.getMessage());
/*     */     } 
/*     */     try {
/*  77 */       result.put("BOOTLOADER", Build.BOOTLOADER);
/*  78 */     } catch (Exception e) {
/*  79 */       result.put("BOOTLOADER", e.getMessage());
/*     */     } 
/*     */     try {
/*  82 */       result.put("HARDWARE", Build.HARDWARE);
/*  83 */     } catch (Exception e) {
/*  84 */       result.put("HARDWARE", e.getMessage());
/*     */     } 
/*  86 */     if (BuildCheck.isAndroid5()) {
/*     */       try {
/*  88 */         String[] supported_abis = Build.SUPPORTED_ABIS;
/*  89 */         if (supported_abis != null && supported_abis.length > 0) {
/*  90 */           JSONObject temp = new JSONObject();
/*  91 */           int n = supported_abis.length;
/*  92 */           for (int i = 0; i < n; i++)
/*  93 */             temp.put(Integer.toString(i), supported_abis[i]); 
/*  94 */           result.put("SUPPORTED_ABIS", temp);
/*     */         } 
/*  96 */       } catch (Exception e) {
/*  97 */         result.put("SUPPORTED_ABIS", e.getMessage());
/*     */       } 
/*     */       try {
/* 100 */         String[] supported_abis32 = Build.SUPPORTED_32_BIT_ABIS;
/* 101 */         if (supported_abis32 != null && supported_abis32.length > 0) {
/* 102 */           JSONObject temp = new JSONObject();
/* 103 */           int n = supported_abis32.length;
/* 104 */           for (int i = 0; i < n; i++)
/* 105 */             temp.put(Integer.toString(i), supported_abis32[i]); 
/* 106 */           result.put("SUPPORTED_32_BIT_ABIS", temp);
/*     */         } 
/* 108 */       } catch (Exception e) {
/* 109 */         result.put("SUPPORTED_32_BIT_ABIS", e.getMessage());
/*     */       } 
/*     */       try {
/* 112 */         String[] supported_abis64 = Build.SUPPORTED_64_BIT_ABIS;
/* 113 */         if (supported_abis64 != null && supported_abis64.length > 0) {
/* 114 */           JSONObject temp = new JSONObject();
/* 115 */           int n = supported_abis64.length;
/* 116 */           for (int i = 0; i < n; i++)
/* 117 */             temp.put(Integer.toString(i), supported_abis64[i]); 
/* 118 */           result.put("SUPPORTED_64_BIT_ABIS", temp);
/*     */         } 
/* 120 */       } catch (Exception e) {
/* 121 */         result.put("SUPPORTED_64_BIT_ABIS", e.getMessage());
/*     */       } 
/*     */     } else {
/*     */       try {
/* 125 */         JSONObject temp = new JSONObject();
/* 126 */         temp.put("0", Build.CPU_ABI);
/* 127 */         temp.put("1", Build.CPU_ABI2);
/* 128 */         result.put("SUPPORTED_ABIS", temp);
/* 129 */       } catch (Exception e) {
/* 130 */         result.put("SUPPORTED_ABIS", e.getMessage());
/*     */       } 
/*     */     } 
/*     */     try {
/* 134 */       result.put("RELEASE", Build.VERSION.RELEASE);
/* 135 */     } catch (Exception e) {
/* 136 */       result.put("RELEASE", e.getMessage());
/*     */     } 
/*     */     try {
/* 139 */       result.put("API_LEVEL", Build.VERSION.SDK_INT);
/* 140 */     } catch (Exception e) {
/* 141 */       result.put("API_LEVEL", e.getMessage());
/*     */     } 
/*     */     try {
/* 144 */       String proc_version = null;
/* 145 */       BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 512);
/* 146 */       proc_version = reader.readLine();
/* 147 */       reader.close();
/* 148 */       result.put("PROC_VERSION", proc_version);
/* 149 */     } catch (Exception e) {
/* 150 */       result.put("PROC_VERSION", e.getMessage());
/*     */     } 
/*     */     
/* 153 */     JSONObject cpu_info = new JSONObject();
/* 154 */     int i = 0;
/* 155 */     String proc_cpuinfo = null;
/*     */     try {
/* 157 */       BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"), 512);
/*     */       do {
/* 159 */         proc_cpuinfo = reader.readLine();
/* 160 */         if (proc_cpuinfo == null)
/* 161 */           break;  if (TextUtils.isEmpty(proc_cpuinfo))
/* 162 */           continue;  cpu_info.put(Integer.toString(i++), proc_cpuinfo);
/* 163 */       } while (proc_cpuinfo != null);
/* 164 */       reader.close();
/* 165 */       result.put("PROC_CPUINFO", cpu_info);
/* 166 */     } catch (Exception e) {
/* 167 */       result.put("PROC_CPUINFO", e.getMessage());
/*     */     } 
/* 169 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\DeviceInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */