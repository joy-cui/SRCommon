/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ public class ReentrantReadWriteMap<K, V>
/*     */ {
/*  34 */   private final ReentrantReadWriteLock mSensorLock = new ReentrantReadWriteLock();
/*  35 */   private final Lock mReadLock = this.mSensorLock.readLock();
/*  36 */   private final Lock mWriteLock = this.mSensorLock.writeLock();
/*     */   
/*  38 */   private final Map<K, V> mMap = new HashMap<>();
/*     */   
/*     */   @Nullable
/*     */   public V get(@NonNull K key) {
/*  42 */     this.mReadLock.lock();
/*     */     try {
/*  44 */       return this.mMap.containsKey(key) ? this.mMap.get(key) : null;
/*     */     } finally {
/*  46 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public V tryGet(@NonNull K key) {
/*  52 */     if (this.mReadLock.tryLock()) {
/*     */       try {
/*  54 */         return this.mMap.containsKey(key) ? this.mMap.get(key) : null;
/*     */       } finally {
/*  56 */         this.mReadLock.unlock();
/*     */       } 
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(@NonNull K key, @NonNull V value) {
/*     */     V prev;
/*  70 */     this.mWriteLock.lock();
/*     */     try {
/*  72 */       prev = this.mMap.remove(key);
/*  73 */       this.mMap.put(key, value);
/*     */     } finally {
/*  75 */       this.mWriteLock.unlock();
/*     */     } 
/*  77 */     return prev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V putIfAbsent(K key, V value) {
/*     */     V v;
/*  89 */     this.mWriteLock.lock();
/*     */     try {
/*  91 */       v = this.mMap.get(key);
/*  92 */       if (v == null) {
/*  93 */         v = this.mMap.put(key, value);
/*     */       }
/*     */     } finally {
/*  96 */       this.mWriteLock.unlock();
/*     */     } 
/*  98 */     return v;
/*     */   }
/*     */   
/*     */   public void putAll(@NonNull Map<? extends K, ? extends V> map) {
/* 102 */     this.mWriteLock.lock();
/*     */     try {
/* 104 */       this.mMap.putAll(map);
/*     */     } finally {
/* 106 */       this.mWriteLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public V remove(@NonNull K key) {
/* 111 */     this.mWriteLock.lock();
/*     */     try {
/* 113 */       return this.mMap.remove(key);
/*     */     } finally {
/* 115 */       this.mWriteLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(@NonNull K key, V value) {
/* 126 */     V v = null;
/* 127 */     this.mWriteLock.lock();
/*     */     try {
/* 129 */       if (this.mMap.containsKey(key) && isEquals(this.mMap.get(key), value)) {
/* 130 */         v = this.mMap.remove(key);
/*     */       }
/*     */     } finally {
/* 133 */       this.mWriteLock.unlock();
/*     */     } 
/* 135 */     return v;
/*     */   }
/*     */   
/*     */   public Collection<V> removeAll() {
/* 139 */     Collection<V> result = new ArrayList<>();
/* 140 */     this.mWriteLock.lock();
/*     */     try {
/* 142 */       result.addAll(this.mMap.values());
/* 143 */       this.mMap.clear();
/*     */     } finally {
/* 145 */       this.mWriteLock.unlock();
/*     */     } 
/* 147 */     return result;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 151 */     this.mWriteLock.lock();
/*     */     try {
/* 153 */       this.mMap.clear();
/*     */     } finally {
/* 155 */       this.mWriteLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/* 160 */     this.mReadLock.lock();
/*     */     try {
/* 162 */       return this.mMap.size();
/*     */     } finally {
/* 164 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsKey(K key) {
/* 169 */     this.mReadLock.lock();
/*     */     try {
/* 171 */       return this.mMap.containsKey(key);
/*     */     } finally {
/* 173 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 178 */     this.mReadLock.lock();
/*     */     try {
/* 180 */       return this.mMap.containsValue(value);
/*     */     } finally {
/* 182 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public V getOrDefault(K key, @Nullable V defaultValue) {
/* 187 */     this.mReadLock.lock();
/*     */     try {
/* 189 */       return this.mMap.containsKey(key) ? this.mMap.get(key) : defaultValue;
/*     */     } finally {
/* 191 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 196 */     this.mReadLock.lock();
/*     */     try {
/* 198 */       return this.mMap.isEmpty();
/*     */     } finally {
/* 200 */       this.mReadLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Collection<K> keys() {
/* 210 */     Collection<K> result = new ArrayList<>();
/* 211 */     this.mReadLock.lock();
/*     */     try {
/* 213 */       result.addAll(this.mMap.keySet());
/*     */     } finally {
/* 215 */       this.mReadLock.unlock();
/*     */     } 
/* 217 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Collection<V> values() {
/* 226 */     Collection<V> result = new ArrayList<>();
/* 227 */     this.mReadLock.lock();
/*     */     try {
/* 229 */       if (!this.mMap.isEmpty()) {
/* 230 */         result.addAll(this.mMap.values());
/*     */       }
/*     */     } finally {
/* 233 */       this.mReadLock.unlock();
/*     */     } 
/* 235 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 244 */     Set<Map.Entry<K, V>> result = new HashSet<>();
/* 245 */     this.mReadLock.lock();
/*     */     try {
/* 247 */       result.addAll(this.mMap.entrySet());
/*     */     } finally {
/* 249 */       this.mReadLock.unlock();
/*     */     } 
/* 251 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 255 */   private static final boolean isEquals(Object a, Object b) { return (a == b || (a != null && a.equals(b))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   protected void readLock() { this.mReadLock.lock(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 270 */   protected void readUnlock() { this.mReadLock.unlock(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   protected void writeLock() { this.mWriteLock.lock(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   protected void writeUnlock() { this.mWriteLock.unlock(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 294 */   protected Collection<V> valuesLocked() { return this.mMap.values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 303 */   protected Set<K> keysLocked() { return this.mMap.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 312 */   protected Map<K, V> mapLocked() { return this.mMap; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\ReentrantReadWriteMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */