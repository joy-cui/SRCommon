/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.Signature;
/*     */ import android.text.TextUtils;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ public class SignatureHelper
/*     */ {
/*     */   public static boolean checkSignature(Context context, String key) throws IllegalArgumentException, PackageManager.NameNotFoundException {
/*  42 */     if (context == null || TextUtils.isEmpty(key)) {
/*  43 */       throw new IllegalArgumentException("context or key is null");
/*     */     }
/*  45 */     Signature expected = new Signature(key);
/*  46 */     boolean result = true;
/*  47 */     PackageManager pm = context.getPackageManager();
/*     */     
/*  49 */     PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 64);
/*     */     
/*  51 */     for (int i = 0; i < packageInfo.signatures.length; i++) {
/*  52 */       result &= expected.equals(packageInfo.signatures[i]);
/*     */     }
/*  54 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSignature(Context context) {
/*  63 */     if (context != null) {
/*  64 */       PackageManager pm = context.getPackageManager();
/*     */       
/*     */       try {
/*  67 */         PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 64);
/*     */ 
/*     */         
/*  70 */         int cnt = 0;
/*  71 */         StringBuilder sb = new StringBuilder();
/*  72 */         for (int i = 0; i < packageInfo.signatures.length; i++) {
/*  73 */           Signature signature = packageInfo.signatures[i];
/*  74 */           if (signature != null) {
/*  75 */             if (cnt != 0) {
/*  76 */               sb.append('/');
/*     */             }
/*  78 */             sb.append(signature.toCharsString());
/*     */           } 
/*     */         } 
/*  81 */         return sb.toString();
/*  82 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getSignatureBytes(Context context) {
/*  94 */     if (context != null) {
/*  95 */       PackageManager pm = context.getPackageManager();
/*     */       
/*     */       try {
/*  98 */         PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 64);
/*  99 */         ByteBuffer result = ByteBuffer.allocate(1024);
/*     */ 
/*     */         
/* 102 */         for (int i = 0; i < packageInfo.signatures.length; i++) {
/* 103 */           Signature signature = packageInfo.signatures[i];
/* 104 */           if (signature != null) {
/* 105 */             byte[] bytes = signature.toByteArray();
/* 106 */             int n = (bytes != null) ? bytes.length : 0;
/* 107 */             if (n > 0) {
/* 108 */               if (n > result.remaining()) {
/* 109 */                 result.flip();
/* 110 */                 ByteBuffer temp = ByteBuffer.allocate(result.capacity() + n * 2);
/* 111 */                 temp.put(result);
/* 112 */                 result = temp;
/*     */               } 
/* 114 */               result.put(bytes);
/*     */             } 
/*     */           } 
/*     */         } 
/* 118 */         result.flip();
/* 119 */         int n = result.limit();
/* 120 */         if (n > 0) {
/* 121 */           byte[] bytes = new byte[n];
/* 122 */           result.get(bytes);
/* 123 */           return bytes;
/*     */         } 
/* 125 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 128 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\SignatureHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */