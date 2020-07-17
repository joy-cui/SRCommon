/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class CollectionMap<K, V>
/*     */   implements Map<K, Collection<V>>
/*     */ {
/*  40 */   private final Map<K, Collection<V>> contents = createContentsMap();
/*     */ 
/*     */ 
/*     */   
/*  44 */   protected Map<K, Collection<V>> createContentsMap() { return new HashMap<>(); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected Collection<V> createCollection() { return new ArrayList<>(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public void clear() { this.contents.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public boolean containsKey(Object key) { return this.contents.containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public boolean containsValue(Object value) { return this.contents.containsValue(value); }
/*     */ 
/*     */   
/*     */   public boolean containsInValue(V value) {
/*  67 */     for (Collection<V> collection : this.contents.values()) {
/*  68 */       if (collection.contains(value)) {
/*  69 */         return true;
/*     */       }
/*     */     } 
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*  78 */   public Set<Entry<K, Collection<V>>> entrySet() { return this.contents.entrySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Collection<V> get(Object key) { return this.contents.get(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public boolean isEmpty() { return this.contents.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*  94 */   public Set<K> keySet() { return this.contents.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public Collection<V> put(K key, Collection<V> value) { return this.contents.put(key, value); }
/*     */ 
/*     */   
/*     */   public boolean add(K key, V value) {
/* 103 */     Collection<V> collection = this.contents.get(key);
/* 104 */     if (collection == null) {
/* 105 */       collection = createCollection();
/* 106 */       this.contents.put(key, collection);
/*     */     } 
/* 108 */     return collection.add(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public void putAll(@NonNull Map<? extends K, ? extends Collection<V>> m) { this.contents.putAll(m); }
/*     */ 
/*     */   
/*     */   public void addAll(Map<? extends K, ? extends Collection<V>> m) {
/* 117 */     for (Entry<? extends K, ? extends Collection<V>> entry : m.entrySet()) {
/* 118 */       addAll(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean addAll(K key, Collection<? extends V> values) {
/* 123 */     Collection<V> collection = this.contents.get(key);
/* 124 */     if (collection == null) {
/* 125 */       collection = createCollection();
/* 126 */       this.contents.put(key, collection);
/*     */     } 
/* 128 */     return collection.addAll(values);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public Collection<V> remove(Object key) { return this.contents.remove(key); }
/*     */ 
/*     */   
/*     */   public boolean remove(Object key, Object value) {
/* 137 */     Collection<?> collection = this.contents.get(key);
/* 138 */     return (collection != null && collection.remove(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public int size() { return this.contents.size(); }
/*     */ 
/*     */   
/*     */   public int size(K key) {
/* 147 */     Collection<V> collection = this.contents.containsKey(key) ? this.contents.get(key) : null;
/* 148 */     return (collection != null) ? collection.size() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/* 154 */   public Collection<Collection<V>> values() { return this.contents.values(); }
/*     */ 
/*     */   
/*     */   public Collection<V> valuesAll() {
/* 158 */     Collection<V> result = createCollection();
/* 159 */     for (Collection<V> v : values()) {
/* 160 */       result.addAll(v);
/*     */     }
/* 162 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\CollectionMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */