/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.preference.DialogPreference;
/*     */ import android.text.format.DateFormat;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.View;
/*     */ import android.widget.TimePicker;
/*     */ import com.serenegiant.common.R;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
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
/*     */ public class TimePickerPreference
/*     */   extends DialogPreference
/*     */ {
/*     */   private final Calendar calendar;
/*     */   private final long mDefaultValue;
/*  39 */   private TimePicker picker = null;
/*     */ 
/*     */   
/*  42 */   public TimePickerPreference(Context ctxt) { this(ctxt, null); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public TimePickerPreference(Context ctxt, AttributeSet attrs) { this(ctxt, attrs, 16842897); }
/*     */ 
/*     */   
/*     */   public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
/*  50 */     super(context, attrs, defStyle);
/*     */     
/*  52 */     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyle, 0);
/*     */     
/*  54 */     this.mDefaultValue = (long)a.getFloat(R.styleable.TimePicker_TimePickerDefaultValue, -1.0F);
/*  55 */     a.recycle();
/*  56 */     a = null;
/*     */     
/*  58 */     setPositiveButtonText(17039370);
/*  59 */     setNegativeButtonText(17039360);
/*  60 */     this.calendar = new GregorianCalendar();
/*     */   }
/*     */ 
/*     */   
/*     */   protected View onCreateDialogView() {
/*  65 */     this.picker = new TimePicker(getContext());
/*  66 */     this.picker.setIs24HourView(Boolean.valueOf(true));
/*  67 */     return (View)this.picker;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onBindDialogView(View v) {
/*  72 */     super.onBindDialogView(v);
/*  73 */     this.picker.setCurrentHour(Integer.valueOf(this.calendar.get(11)));
/*  74 */     this.picker.setCurrentMinute(Integer.valueOf(this.calendar.get(12)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDialogClosed(boolean positiveResult) {
/*  79 */     super.onDialogClosed(positiveResult);
/*     */     
/*  81 */     if (positiveResult) {
/*  82 */       this.calendar.set(11, this.picker.getCurrentHour().intValue());
/*  83 */       this.calendar.set(12, this.picker.getCurrentMinute().intValue());
/*     */       
/*  85 */       setSummary(getSummary());
/*  86 */       if (callChangeListener(Long.valueOf(this.calendar.getTimeInMillis()))) {
/*  87 */         persistLong(this.calendar.getTimeInMillis());
/*  88 */         notifyChanged();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected Object onGetDefaultValue(TypedArray a, int index) { return a.getString(index); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
/* 100 */     long v = (this.mDefaultValue > 0L) ? this.mDefaultValue : System.currentTimeMillis();
/* 101 */     if (restoreValue) {
/*     */       long persistedValue;
/*     */       try {
/* 104 */         persistedValue = getPersistedLong(v);
/* 105 */       } catch (Exception e) {
/*     */         
/* 107 */         persistedValue = v;
/*     */       } 
/* 109 */       this.calendar.setTimeInMillis(persistedValue);
/* 110 */     } else if (defaultValue != null) {
/* 111 */       this.calendar.setTimeInMillis(Long.parseLong((String)defaultValue));
/*     */     } else {
/*     */       
/* 114 */       this.calendar.setTimeInMillis(v);
/*     */     } 
/*     */     
/* 117 */     setSummary(getSummary());
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence getSummary() {
/* 122 */     if (this.calendar == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     return DateFormat.getTimeFormat(getContext()).format(new Date(this.calendar
/* 126 */           .getTimeInMillis()));
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\TimePickerPreference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */