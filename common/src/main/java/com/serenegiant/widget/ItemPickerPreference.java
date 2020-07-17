/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.preference.Preference;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.RelativeLayout;
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
/*     */ public final class ItemPickerPreference
/*     */   extends Preference
/*     */ {
/*     */   private int preferenceValue;
/*  36 */   private int mMinValue = 1; private int mMaxValue = 100;
/*     */   private ItemPicker mItemPicker;
/*     */   private final ItemPicker.OnChangedListener mOnChangeListener;
/*     */   
/*  40 */   public ItemPickerPreference(Context context) { super(context);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     this.mOnChangeListener = new ItemPicker.OnChangedListener()
/*     */       {
/*     */         public void onChanged(ItemPicker picker, int oldVal, int newVal)
/*     */         {
/* 111 */           ItemPickerPreference.this.callChangeListener(Integer.valueOf(newVal));
/* 112 */           ItemPickerPreference.this.preferenceValue = newVal;
/* 113 */           ItemPickerPreference.this.persistInt(ItemPickerPreference.this.preferenceValue); } }; } public ItemPickerPreference(Context context, AttributeSet attrs) { super(context, attrs); this.mOnChangeListener = new ItemPicker.OnChangedListener() { public void onChanged(ItemPicker picker, int oldVal, int newVal) { ItemPickerPreference.this.callChangeListener(Integer.valueOf(newVal)); ItemPickerPreference.this.preferenceValue = newVal; ItemPickerPreference.this.persistInt(ItemPickerPreference.this.preferenceValue); } }; } public ItemPickerPreference(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); this.mOnChangeListener = new ItemPicker.OnChangedListener() { public void onChanged(ItemPicker picker, int oldVal, int newVal) { ItemPickerPreference.this.callChangeListener(Integer.valueOf(newVal)); ItemPickerPreference.this.preferenceValue = newVal; ItemPickerPreference.this.persistInt(ItemPickerPreference.this.preferenceValue); }
/*     */          }
/*     */       ; }
/*     */ 
/*     */   
/*     */   public void setRange(int min, int max) {
/* 119 */     if (min > max) {
/* 120 */       int w = min;
/* 121 */       min = max;
/* 122 */       max = w;
/*     */     } 
/* 124 */     if (this.mMinValue != min || this.mMaxValue != max) {
/* 125 */       this.mMaxValue = max;
/* 126 */       this.mMinValue = min;
/* 127 */       if (this.mItemPicker != null) {
/* 128 */         this.mItemPicker.setRange(this.mMinValue, this.mMaxValue);
/* 129 */         this.mItemPicker.setValue(this.preferenceValue);
/* 130 */         this.preferenceValue = this.mItemPicker.getValue();
/* 131 */         persistInt(this.preferenceValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onBindView(View view) {
/*     */     super.onBindView(view);
/*     */     RelativeLayout parent = null;
/*     */     ViewGroup group = (ViewGroup)view;
/*     */     for (int i = group.getChildCount() - 1; i >= 0; i--) {
/*     */       View v = group.getChildAt(i);
/*     */       if (v instanceof RelativeLayout) {
/*     */         parent = (RelativeLayout)v;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     this.mItemPicker = new ItemPicker(getContext());
/*     */     RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
/*     */     params.addRule(3, 16908304);
/*     */     parent.addView((View)this.mItemPicker, (ViewGroup.LayoutParams)params);
/*     */     this.mItemPicker.setRange(this.mMinValue, this.mMaxValue);
/*     */     this.mItemPicker.setValue(this.preferenceValue);
/*     */     this.preferenceValue = this.mItemPicker.getValue();
/*     */     persistInt(this.preferenceValue);
/*     */     this.mItemPicker.setOnChangeListener(this.mOnChangeListener);
/*     */   }
/*     */   
/*     */   protected Object onGetDefaultValue(TypedArray a, int index) { return Integer.valueOf(a.getInt(index, 0)); }
/*     */   
/*     */   protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
/*     */     int def = this.preferenceValue;
/*     */     if (defaultValue instanceof Integer) {
/*     */       def = ((Integer)defaultValue).intValue();
/*     */     } else if (defaultValue instanceof String) {
/*     */       try {
/*     */         def = Integer.parseInt((String)defaultValue);
/*     */       } catch (Exception exception) {}
/*     */     } 
/*     */     if (restorePersistedValue) {
/*     */       this.preferenceValue = getPersistedInt(def);
/*     */     } else {
/*     */       this.preferenceValue = def;
/*     */       persistInt(this.preferenceValue);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ItemPickerPreference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */