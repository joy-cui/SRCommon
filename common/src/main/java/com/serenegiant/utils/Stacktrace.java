/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.util.Log;
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
/*    */ public class Stacktrace
/*    */ {
/*    */   private static final String TAG = "Stacktrace";
/*    */   
/*    */   public static void print() {
/* 26 */     Throwable t = new Throwable();
/* 27 */     StringBuilder sb = new StringBuilder();
/* 28 */     StackTraceElement[] elms = t.getStackTrace();
/* 29 */     boolean top = true;
/* 30 */     if (elms != null) {
/* 31 */       for (StackTraceElement elm : elms) {
/* 32 */         if (!top && elm != null) {
/* 33 */           sb.append(elm.toString()).append("\n");
/*    */         } else {
/* 35 */           top = false;
/*    */         } 
/*    */       } 
/*    */     }
/* 39 */     Log.i("Stacktrace", sb.toString());
/*    */   }
/*    */ 
/*    */   
/* 43 */   public static String asString() { return asString(new Throwable()); }
/*    */ 
/*    */   
/*    */   public static String asString(Throwable t) {
/* 47 */     StringBuilder sb = new StringBuilder();
/* 48 */     StackTraceElement[] elms = t.getStackTrace();
/* 49 */     boolean top = true;
/* 50 */     if (elms != null) {
/* 51 */       for (StackTraceElement elm : elms) {
/* 52 */         if (!top && elm != null) {
/* 53 */           sb.append(elm.toString()).append("\n");
/*    */         } else {
/* 55 */           top = false;
/*    */         } 
/*    */       } 
/*    */     }
/* 59 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\Stacktrace.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */