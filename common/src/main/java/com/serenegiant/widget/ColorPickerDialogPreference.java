/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.preference.DialogPreference;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.View;
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
/*     */ public class ColorPickerDialogPreference
/*     */   extends DialogPreference
/*     */ {
/*  29 */   private static final String TAG = ColorPickerDialogPreference.class.getSimpleName();
/*     */   
/*  31 */   private int mColor = -65536;
/*     */   private boolean changed;
/*     */   private final ColorPickerView.ColorPickerListener mColorPickerListener;
/*     */   
/*  35 */   public ColorPickerDialogPreference(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public ColorPickerDialogPreference(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */   
/*     */   public ColorPickerDialogPreference(Context context, AttributeSet attrs, int defStyleAttr)
/*     */   {
/*  43 */     super(context, attrs, defStyleAttr);
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
/*  94 */     this.mColorPickerListener = new ColorPickerView.ColorPickerListener()
/*     */       {
/*     */         public void onColorChanged(ColorPickerView colorPickerView, int color)
/*     */         {
/*  98 */           if (ColorPickerDialogPreference.this.mColor != color) {
/*  99 */             ColorPickerDialogPreference.this.mColor = color;
/* 100 */             ColorPickerDialogPreference.this.changed = true;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 106 */   public int getValue() { return this.mColor; }
/*     */   
/*     */   protected void onBindView(View view) {
/*     */     super.onBindView(view);
/*     */     this.mColor = getPersistedInt(this.mColor);
/*     */   }
/*     */   
/*     */   protected View onCreateDialogView() {
/*     */     ColorPickerView view = new ColorPickerView(getContext());
/*     */     view.setColorPickerListener(this.mColorPickerListener);
/*     */     return view;
/*     */   }
/*     */   
/*     */   protected void onBindDialogView(View v) {
/*     */     super.onBindDialogView(v);
/*     */     this.mColor = getPersistedInt(this.mColor);
/*     */     this.changed = false;
/*     */     if (v instanceof ColorPickerView)
/*     */       ((ColorPickerView)v).setColor(this.mColor); 
/*     */   }
/*     */   
/*     */   protected void onDialogClosed(boolean positiveResult) {
/*     */     if (positiveResult || this.changed) {
/*     */       setSummary(getSummary());
/*     */       if (callChangeListener(Integer.valueOf(this.mColor))) {
/*     */         persistInt(this.mColor);
/*     */         notifyChanged();
/*     */       } 
/*     */     } 
/*     */     super.onDialogClosed((positiveResult || this.changed));
/*     */   }
/*     */   
/*     */   protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
/*     */     if (restorePersistedValue) {
/*     */       this.mColor = getPersistedInt(this.mColor);
/*     */     } else {
/*     */       this.mColor = ((Integer)defaultValue).intValue();
/*     */       persistInt(this.mColor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ColorPickerDialogPreference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */