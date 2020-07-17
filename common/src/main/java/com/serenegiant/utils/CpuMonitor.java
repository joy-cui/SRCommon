/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.util.Log;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
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
/*     */ public final class CpuMonitor
/*     */ {
/*     */   private static final String TAG = "CpuMonitor";
/*     */   private static final int SAMPLE_SAVE_NUMBER = 10;
/*  73 */   private int[] percentVec = new int[10];
/*  74 */   private int sum3 = 0;
/*  75 */   private int sum10 = 0;
/*     */   private long[] cpuFreq;
/*     */   private int cpusPresent;
/*  78 */   private double lastPercentFreq = -1.0D;
/*     */   private int cpuCurrent;
/*     */   private int cpuAvg3;
/*     */   private int cpuAvgAll;
/*     */   private boolean initialized = false;
/*     */   private String[] maxPath;
/*     */   private String[] curPath;
/*  85 */   private final ProcStat lastProcStat = new ProcStat(0L, 0L);
/*  86 */   private final Map<String, Integer> mCpuTemps = new HashMap<>();
/*  87 */   private int mTempNum = 0;
/*  88 */   private float tempAve = 0.0F;
/*     */   
/*     */   private static final class ProcStat {
/*     */     private long runTime;
/*     */     private long idleTime;
/*     */     
/*     */     private ProcStat(long aRunTime, long aIdleTime) {
/*  95 */       this.runTime = aRunTime;
/*  96 */       this.idleTime = aIdleTime;
/*     */     }
/*     */     
/*     */     private void set(long aRunTime, long aIdleTime) {
/* 100 */       this.runTime = aRunTime;
/* 101 */       this.idleTime = aIdleTime;
/*     */     }
/*     */     
/*     */     private void set(ProcStat other) {
/* 105 */       this.runTime = other.runTime;
/* 106 */       this.idleTime = other.idleTime;
/*     */     }
/*     */   }
/*     */   
/*     */   private void init() {
/*     */     try {
/* 112 */       FileReader fin = new FileReader("/sys/devices/system/cpu/present");
/*     */       try {
/* 114 */         BufferedReader rdr = new BufferedReader(fin);
/* 115 */         Scanner scanner = (new Scanner(rdr)).useDelimiter("[-\n]");
/* 116 */         scanner.nextInt();
/* 117 */         this.cpusPresent = 1 + scanner.nextInt();
/* 118 */         scanner.close();
/* 119 */       } catch (Exception e) {
/* 120 */         Log.e("CpuMonitor", "Cannot do CPU stats due to /sys/devices/system/cpu/present parsing problem");
/*     */       } finally {
/* 122 */         fin.close();
/*     */       } 
/* 124 */     } catch (FileNotFoundException e) {
/* 125 */       Log.e("CpuMonitor", "Cannot do CPU stats since /sys/devices/system/cpu/present is missing");
/* 126 */     } catch (IOException e) {
/* 127 */       Log.e("CpuMonitor", "Error closing file");
/*     */     } 
/*     */     
/* 130 */     this.cpuFreq = new long[this.cpusPresent];
/* 131 */     this.maxPath = new String[this.cpusPresent];
/* 132 */     this.curPath = new String[this.cpusPresent];
/* 133 */     for (int i = 0; i < this.cpusPresent; i++) {
/* 134 */       this.cpuFreq[i] = 0L;
/* 135 */       this.maxPath[i] = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq";
/* 136 */       this.curPath[i] = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq";
/*     */     } 
/*     */     
/* 139 */     this.lastProcStat.set(0L, 0L);
/*     */     
/* 141 */     this.mCpuTemps.clear();
/* 142 */     this.mTempNum = 0;
/* 143 */     for (int i = 0; i < 50; i++) {
/* 144 */       String path = "/sys/class/hwmon/hwmon" + i;
/* 145 */       File dir = new File(path);
/* 146 */       if (dir.exists() && dir.canRead()) {
/* 147 */         this.mCpuTemps.put(path, Integer.valueOf(0));
/* 148 */         this.mTempNum++;
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     this.initialized = true;
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
/*     */   public boolean sampleCpuUtilization() {
/* 165 */     long lastSeenMaxFreq = 0L;
/* 166 */     long cpufreqCurSum = 0L;
/* 167 */     long cpufreqMaxSum = 0L;
/*     */     
/* 169 */     if (!this.initialized) {
/* 170 */       init();
/*     */     }
/*     */     
/* 173 */     for (int i = 0; i < this.cpusPresent; i++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       if (this.cpuFreq[i] == 0L) {
/*     */         
/* 182 */         long cpufreqMax = readFreqFromFile(this.maxPath[i]);
/* 183 */         if (cpufreqMax > 0L) {
/* 184 */           lastSeenMaxFreq = cpufreqMax;
/* 185 */           this.cpuFreq[i] = cpufreqMax;
/* 186 */           this.maxPath[i] = null;
/*     */         } 
/*     */       } else {
/* 189 */         lastSeenMaxFreq = this.cpuFreq[i];
/*     */       } 
/*     */       
/* 192 */       long cpufreqCur = readFreqFromFile(this.curPath[i]);
/* 193 */       cpufreqCurSum += cpufreqCur;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       cpufreqMaxSum += lastSeenMaxFreq;
/*     */     } 
/*     */     
/* 204 */     if (cpufreqMaxSum == 0L) {
/* 205 */       Log.e("CpuMonitor", "Could not read max frequency for any CPU");
/* 206 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     double newPercentFreq = 100.0D * cpufreqCurSum / cpufreqMaxSum;
/* 217 */     double percentFreq = (this.lastPercentFreq > 0.0D) ? ((this.lastPercentFreq + newPercentFreq) * 0.5D) : newPercentFreq;
/* 218 */     this.lastPercentFreq = newPercentFreq;
/*     */     
/* 220 */     ProcStat procStat = readIdleAndRunTime();
/* 221 */     if (procStat == null) {
/* 222 */       return false;
/*     */     }
/*     */     
/* 225 */     long diffRunTime = procStat.runTime - this.lastProcStat.runTime;
/* 226 */     long diffIdleTime = procStat.idleTime - this.lastProcStat.idleTime;
/*     */ 
/*     */     
/* 229 */     this.lastProcStat.set(procStat);
/*     */     
/* 231 */     long allTime = diffRunTime + diffIdleTime;
/* 232 */     int percent = (allTime == 0L) ? 0 : (int)Math.round(percentFreq * diffRunTime / allTime);
/* 233 */     percent = Math.max(0, Math.min(percent, 100));
/*     */ 
/*     */     
/* 236 */     this.sum3 += percent - this.percentVec[2];
/*     */     
/* 238 */     this.sum10 += percent - this.percentVec[9];
/*     */ 
/*     */     
/* 241 */     for (int i = 9; i > 0; i--) {
/* 242 */       this.percentVec[i] = this.percentVec[i - 1];
/*     */     }
/* 244 */     this.percentVec[0] = percent;
/*     */     
/* 246 */     this.cpuCurrent = percent;
/* 247 */     this.cpuAvg3 = this.sum3 / 3;
/* 248 */     this.cpuAvgAll = this.sum10 / 10;
/*     */     
/* 250 */     this.tempAve = 0.0F;
/* 251 */     float tempCnt = 0.0F;
/* 252 */     for (String path : this.mCpuTemps.keySet()) {
/* 253 */       File dir = new File(path);
/* 254 */       if (dir.exists() && dir.canRead()) {
/* 255 */         File file = new File(dir, "temp1_input");
/* 256 */         if (file.exists() && file.canRead()) {
/* 257 */           int temp = (int)readFreqFromFile(file.getAbsolutePath());
/* 258 */           this.mCpuTemps.put(path, Integer.valueOf(temp));
/* 259 */           if (temp > 0) {
/* 260 */             tempCnt++;
/* 261 */             this.tempAve += (temp > 1000) ? (temp / 1000.0F) : temp;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 266 */     if (tempCnt > 0.0F) {
/* 267 */       this.tempAve /= tempCnt;
/*     */     }
/* 269 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 273 */   public int getCpuCurrent() { return this.cpuCurrent; }
/*     */ 
/*     */ 
/*     */   
/* 277 */   public int getCpuAvg3() { return this.cpuAvg3; }
/*     */ 
/*     */ 
/*     */   
/* 281 */   public int getCpuAvgAll() { return this.cpuAvgAll; }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public int getTempNum() { return this.mTempNum; }
/*     */ 
/*     */   
/*     */   public int getTemp(int ix) {
/* 289 */     int result = 0;
/* 290 */     if (ix >= 0 && ix < this.mTempNum) {
/* 291 */       String path = "/sys/class/hwmon/hwmon" + ix;
/* 292 */       if (this.mCpuTemps.containsKey(path)) {
/* 293 */         result = ((Integer)this.mCpuTemps.get(path)).intValue();
/*     */       }
/*     */     } 
/* 296 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 300 */   public float getTempAve() { return this.tempAve; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readFreqFromFile(String fileName) {
/* 308 */     long number = 0L;
/*     */     try {
/* 310 */       FileReader fin = new FileReader(fileName);
/*     */       try {
/* 312 */         BufferedReader rdr = new BufferedReader(fin);
/* 313 */         Scanner scannerC = new Scanner(rdr);
/* 314 */         number = scannerC.nextLong();
/* 315 */         scannerC.close();
/* 316 */       } catch (Exception exception) {
/*     */       
/*     */       } finally {
/* 319 */         fin.close();
/*     */       } 
/* 321 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */     
/* 323 */     } catch (IOException e) {
/* 324 */       Log.e("CpuMonitor", "Error closing file");
/*     */     } 
/* 326 */     return number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProcStat readIdleAndRunTime() {
/* 334 */     long runTime = 0L;
/* 335 */     long idleTime = 0L;
/*     */     try {
/* 337 */       FileReader fin = new FileReader("/proc/stat");
/*     */       try {
/* 339 */         BufferedReader rdr = new BufferedReader(fin);
/* 340 */         Scanner scanner = new Scanner(rdr);
/* 341 */         scanner.next();
/* 342 */         long user = scanner.nextLong();
/* 343 */         long nice = scanner.nextLong();
/* 344 */         long sys = scanner.nextLong();
/* 345 */         runTime = user + nice + sys;
/* 346 */         idleTime = scanner.nextLong();
/* 347 */         scanner.close();
/* 348 */       } catch (Exception e) {
/* 349 */         Log.e("CpuMonitor", "Problems parsing /proc/stat");
/* 350 */         return null;
/*     */       } finally {
/* 352 */         fin.close();
/*     */       } 
/* 354 */     } catch (FileNotFoundException e) {
/* 355 */       Log.e("CpuMonitor", "Cannot open /proc/stat for reading");
/* 356 */       return null;
/* 357 */     } catch (IOException e) {
/* 358 */       Log.e("CpuMonitor", "Problems reading /proc/stat");
/* 359 */       return null;
/*     */     } 
/* 361 */     return new ProcStat(runTime, idleTime);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\CpuMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */