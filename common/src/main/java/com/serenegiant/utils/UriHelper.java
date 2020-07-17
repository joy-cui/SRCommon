/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.annotation.TargetApi;
/*     */ import android.content.ContentResolver;
/*     */ import android.content.ContentUris;
/*     */ import android.content.Context;
/*     */ import android.database.Cursor;
/*     */ import android.net.Uri;
/*     */ import android.os.Environment;
/*     */ import android.provider.DocumentsContract;
/*     */ import android.provider.MediaStore;
/*     */ import android.util.Log;
/*     */ import java.io.File;
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
/*     */ public final class UriHelper
/*     */ {
/*  38 */   private static final String TAG = UriHelper.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAbsolutePath(ContentResolver cr, Uri uri) {
/*  46 */     String path = null;
/*     */     try {
/*  48 */       String[] columns = { "_data" };
/*  49 */       Cursor cursor = cr.query(uri, columns, null, null, null);
/*  50 */       if (cursor != null)
/*     */         try {
/*  52 */           if (cursor.moveToFirst())
/*  53 */             path = cursor.getString(0); 
/*     */         } finally {
/*  55 */           cursor.close();
/*     */         }  
/*  57 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  61 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"NewApi"})
/*     */   @TargetApi(19)
/*     */   public static String getPath(Context context, Uri uri) {
/*  73 */     Log.i(TAG, "getPath:uri=" + uri);
/*     */     
/*  75 */     if (BuildCheck.isKitKat() && DocumentsContract.isDocumentUri(context, uri))
/*     */     
/*  77 */     { Log.i(TAG, "getPath:isDocumentUri,getAuthority=" + uri.getAuthority());
/*     */       
/*  79 */       if (isExternalStorageDocument(uri)) {
/*  80 */         String docId = DocumentsContract.getDocumentId(uri);
/*  81 */         Log.i(TAG, "getPath:isDocumentUri,docId=" + docId);
/*  82 */         if (BuildCheck.isLollipop()) {
/*  83 */           Log.i(TAG, "getPath:isDocumentUri,getTreeDocumentId=" + DocumentsContract.getTreeDocumentId(uri));
/*     */         }
/*  85 */         String[] split = docId.split(":");
/*  86 */         String type = split[0];
/*     */         
/*  88 */         Log.i(TAG, "getPath:type=" + type);
/*     */         
/*  90 */         if ("primary".equalsIgnoreCase(type)) {
/*  91 */           return Environment.getExternalStorageDirectory() + "/" + split[1];
/*     */         }
/*     */         
/*  94 */         String primary = Environment.getExternalStorageDirectory().getAbsolutePath();
/*  95 */         Log.i(TAG, "getPath:primary=" + primary);
/*  96 */         File[] dirs = context.getExternalFilesDirs(null);
/*  97 */         int n = (dirs != null) ? dirs.length : 0;
/*  98 */         StringBuilder sb = new StringBuilder();
/*  99 */         for (int i = 0; i < n; i++) {
/* 100 */           File dir = dirs[i];
/* 101 */           Log.i(TAG, "getPath:" + i + ")dir=" + dir);
/* 102 */           if (dir == null || !dir.getAbsolutePath().startsWith(primary)) {
/* 103 */             String dir_path = dir.getAbsolutePath();
/* 104 */             String[] dir_elements = dir_path.split("/");
/* 105 */             int m = (dir_elements != null) ? dir_elements.length : 0;
/* 106 */             if (m > 1 && "storage".equalsIgnoreCase(dir_elements[1])) {
/* 107 */               boolean found = false;
/* 108 */               sb.setLength(0);
/* 109 */               sb.append('/').append(dir_elements[1]);
/* 110 */               for (int j = 2; j < m; j++) {
/* 111 */                 if ("Android".equalsIgnoreCase(dir_elements[j])) {
/* 112 */                   found = true;
/*     */                   break;
/*     */                 } 
/* 115 */                 sb.append('/').append(dir_elements[j]);
/*     */               } 
/* 117 */               if (found) {
/* 118 */                 File path = new File(new File(sb.toString()), split[1]);
/* 119 */                 Log.i(TAG, "getPath:path=" + path);
/* 120 */                 if (path.exists() && path.canWrite())
/* 121 */                   return path.getAbsolutePath(); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 127 */         if (isDownloadsDocument(uri)) {
/*     */           
/* 129 */           String id = DocumentsContract.getDocumentId(uri);
/* 130 */           Uri contentUri = ContentUris.withAppendedId(
/* 131 */               Uri.parse("content://downloads/public_downloads"), Long.valueOf(id).longValue());
/*     */           
/* 133 */           return getDataColumn(context, contentUri, null, null);
/* 134 */         }  if (isMediaDocument(uri))
/*     */         
/* 136 */         { String docId = DocumentsContract.getDocumentId(uri);
/* 137 */           String[] split = docId.split(":");
/* 138 */           String type = split[0];
/*     */           
/* 140 */           Uri contentUri = null;
/* 141 */           if ("image".equals(type)) {
/* 142 */             contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
/* 143 */           } else if ("video".equals(type)) {
/* 144 */             contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
/* 145 */           } else if ("audio".equals(type)) {
/* 146 */             contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
/*     */           } 
/*     */           
/* 149 */           String selection = "_id=?";
/* 150 */           String[] selectionArgs = { split[1] };
/*     */           
/* 152 */           return getDataColumn(context, contentUri, "_id=?", selectionArgs); } 
/*     */       }  }
/* 154 */     else { if ("content".equalsIgnoreCase(uri.getScheme())) {
/*     */         
/* 156 */         if (isGooglePhotosUri(uri)) {
/* 157 */           return uri.getLastPathSegment();
/*     */         }
/* 159 */         return getDataColumn(context, uri, null, null);
/* 160 */       }  if ("file".equalsIgnoreCase(uri.getScheme()))
/*     */       {
/* 162 */         return uri.getPath();
/*     */       } }
/*     */     
/* 165 */     return null;
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
/*     */ 
/*     */   
/*     */   public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
/* 180 */     Cursor cursor = null;
/* 181 */     String column = "_data";
/* 182 */     String[] projection = { "_data" };
/*     */     
/*     */     try {
/* 185 */       cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
/* 186 */       if (cursor != null && cursor.moveToFirst()) {
/* 187 */         int column_index = cursor.getColumnIndexOrThrow("_data");
/* 188 */         return cursor.getString(column_index);
/*     */       } 
/*     */     } finally {
/* 191 */       if (cursor != null) {
/* 192 */         cursor.close();
/*     */       }
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public static boolean isExternalStorageDocument(Uri uri) { return "com.android.externalstorage.documents".equals(uri.getAuthority()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public static boolean isDownloadsDocument(Uri uri) { return "com.android.providers.downloads.documents".equals(uri.getAuthority()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   public static boolean isMediaDocument(Uri uri) { return "com.android.providers.media.documents".equals(uri.getAuthority()); }
/*     */ 
/*     */ 
/*     */   
/* 223 */   public static boolean isGooglePhotosUri(Uri uri) { return "com.google.android.apps.photos.content".equals(uri.getAuthority()); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\UriHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */