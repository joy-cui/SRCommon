/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.hardware.Sensor;
/*     */ import android.hardware.SensorEvent;
/*     */ import android.hardware.SensorEventListener;
/*     */ import android.hardware.SensorManager;
/*     */ import android.util.Log;
/*     */ import java.lang.ref.WeakReference;
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
/*     */ public class GyroHelper
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  35 */   private static final String TAG = GyroHelper.class.getSimpleName();
/*     */   
/*  37 */   private static final int[] SENSOR_TYPES = new int[] { 2, 9, 1, 4 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   private final Object mSync = new Object();
/*     */   private final WeakReference<Context> mWeakContext;
/*     */   private SensorManager mSensorManager;
/*     */   private boolean mRegistered;
/*     */   private int mRotation;
/*  49 */   private final Object mSensorSync = new Object();
/*  50 */   private final float[] mMagnetValues = new float[3];
/*  51 */   private final float[] mGravityValues = new float[3];
/*  52 */   private final float[] mAzimuthValues = new float[3];
/*  53 */   private final float[] mAccelValues = new float[3];
/*  54 */   private final float[] mGyroValues = new float[3];
/*     */ 
/*     */ 
/*     */   
/*     */   private final SensorEventListener mSensorEventListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  64 */     synchronized (this.mSync) {
/*  65 */       this.mSensorManager = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  73 */     synchronized (this.mSync) {
/*  74 */       Context context = this.mWeakContext.get();
/*  75 */       if (this.mSensorManager == null || context == null) {
/*  76 */         throw new IllegalStateException("already released");
/*     */       }
/*     */       
/*  79 */       for (int i = 0; i < 3; i++) {
/*  80 */         this.mAzimuthValues[i] = 0.0F; this.mGravityValues[i] = 0.0F; this.mMagnetValues[i] = 0.0F;
/*  81 */         this.mGyroValues[i] = 0.0F; this.mAccelValues[i] = 0.0F;
/*     */       } 
/*     */       
/*  84 */       boolean hasGravity = false;
/*  85 */       this.mRegistered = true;
/*  86 */       for (int sensor_type : SENSOR_TYPES) {
/*  87 */         List<Sensor> sensors = this.mSensorManager.getSensorList(sensor_type);
/*  88 */         if (sensors != null && sensors.size() > 0) {
/*  89 */           if (sensor_type == 9) {
/*  90 */             Log.i(TAG, "hasGravity");
/*  91 */             hasGravity = true;
/*     */           } 
/*  93 */           if (!hasGravity || sensor_type != 1) {
/*  94 */             this.mSensorManager.registerListener(this.mSensorEventListener, sensors.get(0), 1);
/*     */           }
/*     */         } else {
/*     */           
/*  98 */           Log.i(TAG, String.format("no sensor for sensor type %d", new Object[] { Integer.valueOf(sensor_type) }));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 108 */     synchronized (this.mSync) {
/* 109 */       if (this.mRegistered && this.mSensorManager != null) {
/*     */         try {
/* 111 */           this.mSensorManager.unregisterListener(this.mSensorEventListener);
/* 112 */         } catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */       
/* 116 */       this.mRegistered = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 121 */   public void setScreenRotation(int rotation) { this.mRotation = rotation; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public float getAzimuth() { return this.mAzimuthValues[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public float getPan() { return this.mAzimuthValues[1]; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public float getTilt() { return this.mAzimuthValues[2]; }
/*     */   
/*     */   public GyroHelper(Context context) {
/* 148 */     this.mSensorEventListener = new SensorEventListener()
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         private void highPassFilter(float[] values, float[] new_values, float alpha)
/*     */         {
/* 156 */           values[0] = alpha * values[0] + (1.0F - alpha) * new_values[0];
/* 157 */           values[1] = alpha * values[1] + (1.0F - alpha) * new_values[1];
/* 158 */           values[2] = alpha * values[2] + (1.0F - alpha) * new_values[2];
/*     */         }
/*     */         
/* 161 */         private final float[] outR = new float[16];
/* 162 */         private final float[] outR2 = new float[16]; private static final float TO_DEGREE = 57.29578F;
/*     */         
/*     */         private void getOrientation(float[] rotateMatrix, float[] result) {
/* 165 */           switch (GyroHelper.this.mRotation) {
/*     */             case 0:
/* 167 */               SensorManager.getOrientation(rotateMatrix, result);
/*     */               return;
/*     */             case 1:
/* 170 */               SensorManager.remapCoordinateSystem(rotateMatrix, 2, 129, this.outR);
/*     */               break;
/*     */             
/*     */             case 2:
/* 174 */               SensorManager.remapCoordinateSystem(rotateMatrix, 2, 129, this.outR2);
/*     */               
/* 176 */               SensorManager.remapCoordinateSystem(this.outR2, 2, 129, this.outR);
/*     */               break;
/*     */             
/*     */             case 3:
/* 180 */               SensorManager.remapCoordinateSystem(this.outR, 130, 129, this.outR);
/*     */               break;
/*     */           } 
/*     */           
/* 184 */           SensorManager.getOrientation(this.outR, result);
/*     */         }
/*     */ 
/*     */         
/* 188 */         private final float[] mRotateMatrix = new float[16];
/* 189 */         private final float[] mInclinationMatrix = new float[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void onSensorChanged(SensorEvent event) {
/* 198 */           float[] values = event.values;
/* 199 */           int type = event.sensor.getType();
/* 200 */           switch (type) {
/*     */             case 2:
/* 202 */               synchronized (GyroHelper.this.mSensorSync) {
/*     */ 
/*     */                 
/* 205 */                 highPassFilter(GyroHelper.this.mMagnetValues, values, 0.8F);
/* 206 */                 System.arraycopy(values, 0, GyroHelper.this.mMagnetValues, 0, 3);
/*     */                 
/* 208 */                 SensorManager.getRotationMatrix(this.mRotateMatrix, this.mInclinationMatrix, GyroHelper.this.mGravityValues, GyroHelper.this.mMagnetValues);
/* 209 */                 getOrientation(this.mRotateMatrix, GyroHelper.this.mAzimuthValues);
/* 210 */                 GyroHelper.this.mAzimuthValues[0] = GyroHelper.this.mAzimuthValues[0] * 57.29578F;
/* 211 */                 GyroHelper.this.mAzimuthValues[1] = GyroHelper.this.mAzimuthValues[1] * 57.29578F;
/* 212 */                 GyroHelper.this.mAzimuthValues[2] = GyroHelper.this.mAzimuthValues[2] * 57.29578F;
/*     */               } 
/*     */               break;
/*     */             case 9:
/* 216 */               synchronized (GyroHelper.this.mSensorSync) {
/* 217 */                 System.arraycopy(values, 0, GyroHelper.this.mGravityValues, 0, 3);
/*     */               } 
/*     */               break;
/*     */             case 1:
/* 221 */               synchronized (GyroHelper.this.mSensorSync) {
/* 222 */                 System.arraycopy(values, 0, GyroHelper.this.mAccelValues, 0, 3);
/* 223 */                 System.arraycopy(values, 0, GyroHelper.this.mGravityValues, 0, 3);
/*     */               } 
/*     */               break;
/*     */             case 4:
/* 227 */               synchronized (GyroHelper.this.mSensorSync) {
/* 228 */                 System.arraycopy(values, 0, GyroHelper.this.mGyroValues, 0, 3);
/*     */               } 
/*     */               break;
/*     */           } 
/*     */         }
/*     */         
/*     */         public void onAccuracyChanged(Sensor sensor, int accuracy) {}
/*     */       };
/*     */     this.mWeakContext = new WeakReference<>(context);
/*     */     synchronized (this.mSync) {
/*     */       this.mSensorManager = (SensorManager)context.getSystemService("sensor");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\GyroHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */