/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.annotation.SuppressLint;
/*    */ import android.app.ActivityManager;
/*    */ import android.content.Context;
/*    */ import android.os.Debug;
/*    */ import android.text.TextUtils;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
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
/*    */ 
/*    */ 
/*    */ public class MemInfo
/*    */ {
/*    */   @SuppressLint({"NewApi"})
/*    */   public static JSONObject get(Context contex) throws JSONException {
/* 38 */     JSONObject result = new JSONObject();
/*    */     try {
/*    */       try {
/* 41 */         ActivityManager.MemoryInfo mem_info = new ActivityManager.MemoryInfo();
/* 42 */         ActivityManager am = (ActivityManager)contex.getSystemService("activity");
/* 43 */         am.getMemoryInfo(mem_info);
/* 44 */         JSONObject am_info = new JSONObject();
/* 45 */         am_info.put("availMem", mem_info.availMem);
/* 46 */         am_info.put("totalMem", mem_info.totalMem);
/* 47 */         am_info.put("threshold", mem_info.threshold);
/* 48 */         am_info.put("lowMemory", mem_info.lowMemory);
/* 49 */         result.put("ACTIVITYMANAGER_MEMORYINFO", am_info);
/* 50 */       } catch (Exception e) {
/* 51 */         result.put("ACTIVITYMANAGER_MEMORYINFO", e.getMessage());
/*    */       } 
/*    */       
/*    */       try {
/* 55 */         Debug.MemoryInfo dmeminfo = new Debug.MemoryInfo();
/* 56 */         Debug.getMemoryInfo(dmeminfo);
/* 57 */         JSONObject dm_info = new JSONObject();
/* 58 */         dm_info.put("TotalPss", dmeminfo.getTotalPss());
/* 59 */         dm_info.put("TotalPrivateDirty", dmeminfo.getTotalPrivateDirty());
/* 60 */         dm_info.put("TotalSharedDirty", dmeminfo.getTotalSharedDirty());
/* 61 */         if (BuildCheck.isAndroid4_4()) {
/* 62 */           dm_info.put("TotalPrivateClean", dmeminfo.getTotalPrivateClean());
/* 63 */           dm_info.put("TotalSharedClean", dmeminfo.getTotalSharedClean());
/* 64 */           dm_info.put("TotalSwappablePss", dmeminfo.getTotalSwappablePss());
/*    */         } 
/* 66 */         result.put("DEBUG_MEMORYINFO", dm_info);
/* 67 */       } catch (Exception e) {
/* 68 */         result.put("DEBUG_MEMORYINFO", e.getMessage());
/*    */       } 
/*    */       
/*    */       try {
/* 72 */         JSONObject pm_info = new JSONObject();
/* 73 */         int i = 0;
/* 74 */         String proc_meminfo = null;
/* 75 */         BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"), 512);
/*    */         do {
/* 77 */           proc_meminfo = reader.readLine();
/* 78 */           if (proc_meminfo == null)
/* 79 */             break;  if (TextUtils.isEmpty(proc_meminfo))
/* 80 */             continue;  pm_info.put(Integer.toString(i++), proc_meminfo);
/* 81 */         } while (proc_meminfo != null);
/* 82 */         reader.close();
/* 83 */         result.put("PROC_MEMINFO", pm_info);
/* 84 */       } catch (Exception e) {
/* 85 */         result.put("PROC_MEMINFO", e.getMessage());
/*    */       } 
/* 87 */     } catch (Exception e) {
/* 88 */       result.put("EXCEPTION", e.getMessage());
/*    */     } 
/* 90 */     return result;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\MemInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */