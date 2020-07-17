/*     */ package com.serenegiant.bluetooth;
/*     */ 
/*     */ import android.bluetooth.BluetoothClass;
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.os.Build;
/*     */ import android.os.Parcel;
/*     */ import android.os.Parcelable;
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
/*     */ public class BluetoothDeviceInfo
/*     */   implements Parcelable
/*     */ {
/*  32 */   public static final Creator<BluetoothDeviceInfo> CREATOR = new Creator<BluetoothDeviceInfo>()
/*     */     {
/*     */       public BluetoothDeviceInfo createFromParcel(Parcel in) {
/*  35 */         return new BluetoothDeviceInfo(in);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  40 */       public BluetoothDeviceInfo[] newArray(int size) { return new BluetoothDeviceInfo[size]; }
/*     */     };
/*     */ 
/*     */   
/*     */   public final String name;
/*     */   
/*     */   public final String address;
/*     */   
/*     */   public final int type;
/*     */   
/*     */   public final int deviceClass;
/*     */   
/*     */   public final int bondState;
/*     */   
/*     */   BluetoothDeviceInfo(BluetoothDevice device) {
/*  55 */     this.name = device.getName();
/*  56 */     this.address = device.getAddress();
/*  57 */     if (Build.VERSION.SDK_INT >= 18) {
/*  58 */       this.type = device.getType();
/*     */     } else {
/*  60 */       this.type = 0;
/*     */     } 
/*  62 */     BluetoothClass clazz = device.getBluetoothClass();
/*  63 */     this.deviceClass = (clazz != null) ? clazz.getDeviceClass() : 0;
/*  64 */     this.bondState = device.getBondState();
/*     */   }
/*     */   
/*     */   protected BluetoothDeviceInfo(Parcel in) {
/*  68 */     this.name = in.readString();
/*  69 */     this.address = in.readString();
/*  70 */     this.type = in.readInt();
/*  71 */     this.deviceClass = in.readInt();
/*  72 */     this.bondState = in.readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public boolean isPaired() { return (this.bondState == 12); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public int describeContents() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeToParcel(Parcel parcel, int flags) {
/*  91 */     parcel.writeString(this.name);
/*  92 */     parcel.writeString(this.address);
/*  93 */     parcel.writeInt(this.type);
/*  94 */     parcel.writeInt(this.deviceClass);
/*  95 */     parcel.writeInt(this.bondState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public String toString() { return String.format("BluetoothDeviceInfo(%s/%s)", new Object[] { this.name, this.address }); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\bluetooth\BluetoothDeviceInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */