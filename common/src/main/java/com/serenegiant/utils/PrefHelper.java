/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.content.SharedPreferences;
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
/*     */ public class PrefHelper
/*     */ {
/*     */   public static int getShort(SharedPreferences pref, String key, short defaultValue) {
/*  30 */     boolean success = false;
/*  31 */     short result = defaultValue;
/*     */     try {
/*  33 */       result = (short)pref.getInt(key, defaultValue);
/*  34 */       success = true;
/*  35 */     } catch (Exception exception) {}
/*     */     
/*  37 */     if (!success) {
/*     */       try {
/*  39 */         String v = pref.getString(key, Short.toString(defaultValue));
/*     */         try {
/*  41 */           result = (short)Integer.parseInt(v);
/*  42 */           success = true;
/*  43 */         } catch (Exception exception) {}
/*     */         
/*  45 */         if (!success) {
/*     */           
/*     */           try {
/*  48 */             result = (short)Integer.parseInt(v, 16);
/*  49 */             success = true;
/*  50 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/*  54 */         if (!success) {
/*     */           
/*     */           try {
/*  57 */             result = (short)Integer.parseInt("0x" + v, 16);
/*  58 */           } catch (Exception exception) {}
/*     */         
/*     */         }
/*     */       }
/*  62 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/*  66 */     return result;
/*     */   }
/*     */   
/*     */   public static int getInt(SharedPreferences pref, String key, int defaultValue) {
/*  70 */     boolean success = false;
/*  71 */     int result = defaultValue;
/*     */     try {
/*  73 */       result = (int)pref.getLong(key, defaultValue);
/*  74 */       success = true;
/*  75 */     } catch (Exception exception) {}
/*     */     
/*  77 */     if (!success) {
/*     */       try {
/*  79 */         String v = pref.getString(key, Integer.toString(defaultValue));
/*     */         try {
/*  81 */           result = (int)Long.parseLong(v);
/*  82 */           success = true;
/*  83 */         } catch (Exception exception) {}
/*     */         
/*  85 */         if (!success) {
/*     */           
/*     */           try {
/*  88 */             result = (int)Long.parseLong(v, 16);
/*  89 */             success = true;
/*  90 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/*  94 */         if (!success) {
/*     */           
/*     */           try {
/*  97 */             result = (int)Long.parseLong("0x" + v, 16);
/*  98 */           } catch (Exception exception) {}
/*     */         
/*     */         }
/*     */       }
/* 102 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   public static long getLong(SharedPreferences pref, String key, long defaultValue) {
/* 110 */     boolean success = false;
/* 111 */     long result = defaultValue;
/*     */     try {
/* 113 */       result = pref.getLong(key, defaultValue);
/* 114 */       success = true;
/* 115 */     } catch (Exception exception) {}
/*     */     
/* 117 */     if (!success) {
/*     */       try {
/* 119 */         String v = pref.getString(key, Long.toString(defaultValue));
/*     */         try {
/* 121 */           result = Long.parseLong(v);
/* 122 */           success = true;
/* 123 */         } catch (Exception exception) {}
/*     */         
/* 125 */         if (!success) {
/*     */           
/*     */           try {
/* 128 */             result = Long.parseLong(v, 16);
/* 129 */             success = true;
/* 130 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 134 */         if (!success) {
/*     */           
/*     */           try {
/* 137 */             result = Long.parseLong("0x" + v, 16);
/* 138 */           } catch (Exception exception) {}
/*     */         
/*     */         }
/*     */       }
/* 142 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 146 */     return result;
/*     */   }
/*     */   
/*     */   public static float getFloat(SharedPreferences pref, String key, float defaultValue) {
/* 150 */     float result = defaultValue;
/*     */     try {
/* 152 */       result = pref.getFloat(key, defaultValue);
/* 153 */     } catch (Exception e) {
/*     */       try {
/* 155 */         result = (float)Double.parseDouble(pref.getString(key, Float.toString(defaultValue)));
/* 156 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return result;
/*     */   }
/*     */   
/*     */   public static double getDouble(SharedPreferences pref, String key, double defaultValue) {
/* 164 */     double result = defaultValue;
/*     */     try {
/* 166 */       result = Double.parseDouble(pref.getString(key, Double.toString(defaultValue)));
/* 167 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 170 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\PrefHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */