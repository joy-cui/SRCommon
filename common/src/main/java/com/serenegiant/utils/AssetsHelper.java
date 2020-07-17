/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.content.res.AssetManager;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
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
/*    */ public class AssetsHelper
/*    */ {
/*    */   public static String loadString(AssetManager assets, String name) throws IOException {
/* 30 */     StringBuffer sb = new StringBuffer();
/* 31 */     char[] buf = new char[1024];
/* 32 */     BufferedReader reader = new BufferedReader(new InputStreamReader(assets.open(name)));
/* 33 */     int r = reader.read(buf);
/* 34 */     while (r > 0) {
/* 35 */       sb.append(buf, 0, r);
/* 36 */       r = reader.read(buf);
/*    */     } 
/* 38 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\AssetsHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */