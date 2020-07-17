/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.preference.Preference;
/*     */ import android.text.TextUtils;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.RelativeLayout;
/*     */ import android.widget.SeekBar;
/*     */ import android.widget.TextView;
/*     */ import com.serenegiant.common.R;
/*     */ import java.util.Locale;
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
/*     */ public final class SeekBarPreference
/*     */   extends Preference
/*     */ {
/*  40 */   private static int sDefaultValue = 1;
/*     */   
/*     */   private final int mSeekbarLayoutId;
/*     */   
/*     */   private final int mSeekbarId;
/*     */   
/*     */   private final int mLabelTvId;
/*     */   
/*     */   private final int mMinValue;
/*     */   private final int mMaxValue;
/*     */   private final int mDefaultValue;
/*     */   private final float mScaleValue;
/*     */   private final String mFmtStr;
/*     */   private int preferenceValue;
/*     */   private TextView mTextView;
/*     */   private final SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
/*     */   
/*  57 */   public SeekBarPreference(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */   
/*     */   public SeekBarPreference(Context context, AttributeSet attrs, int defStyle)
/*     */   {
/*  61 */     super(context, attrs, defStyle);
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
/* 143 */     this.mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
/*     */       {
/*     */ 
/*     */         
/*     */         public void onStopTrackingTouch(SeekBar seekBar)
/*     */         {
/* 149 */           int newValue = seekBar.getProgress();
/* 150 */           if (SeekBarPreference.this.callChangeListener(Integer.valueOf(newValue))) {
/* 151 */             SeekBarPreference.this.preferenceValue = newValue + SeekBarPreference.this.mMinValue;
/* 152 */             SeekBarPreference.this.persistInt(SeekBarPreference.this.preferenceValue);
/* 153 */             SeekBarPreference.this.setValueLabel(SeekBarPreference.this.preferenceValue, false);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void onStartTrackingTouch(SeekBar seekBar) {}
/*     */ 
/*     */ 
/*     */         
/* 164 */         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { SeekBarPreference.this.setValueLabel(progress + SeekBarPreference.this.mMinValue, fromUser); } }; TypedArray attribs = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, defStyle, 0); this.mSeekbarLayoutId = attribs.getResourceId(R.styleable.SeekBarPreference_seekbar_layout, R.layout.seekbar_preference); this.mSeekbarId = attribs.getResourceId(R.styleable.SeekBarPreference_seekbar_id, R.id.seekbar); this.mLabelTvId = attribs.getResourceId(R.styleable.SeekBarPreference_seekbar_label_id, R.id.seekbar_value_label); this.mMinValue = attribs.getInt(R.styleable.SeekBarPreference_min_value, 0); this.mMaxValue = attribs.getInt(R.styleable.SeekBarPreference_max_value, 100); this.mDefaultValue = attribs.getInt(R.styleable.SeekBarPreference_default_value, this.mMinValue); this.mScaleValue = attribs.getFloat(R.styleable.SeekBarPreference_scale_value, 1.0F); String fmt = attribs.getString(R.styleable.SeekBarPreference_value_format); try {
/*     */       String str = String.format(fmt, new Object[] { Float.valueOf(1.0F) });
/*     */     } catch (Exception e) {
/*     */       fmt = "%f";
/*     */     }  this.mFmtStr = !TextUtils.isEmpty(fmt) ? fmt : "%f";
/* 169 */     attribs.recycle(); } private void setValueLabel(int value, boolean fromUser) { if (this.mTextView != null)
/* 170 */       this.mTextView.setText(formatValueLabel(value, fromUser));  }
/*     */   protected void onBindView(View view) { super.onBindView(view); if (this.mSeekbarLayoutId == 0 || this.mSeekbarId == 0)
/*     */       return;  RelativeLayout parent = null; ViewGroup group = (ViewGroup)view; for (int i = group.getChildCount() - 1; i >= 0; i--) { View v = group.getChildAt(i); if (v instanceof RelativeLayout) { parent = (RelativeLayout)v; break; }  }  if (parent == null)
/*     */       return;  LayoutInflater layoutInflater = LayoutInflater.from(getContext()); View extraview = layoutInflater.inflate(this.mSeekbarLayoutId, group, false); if (extraview != null) { RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2); params.addRule(3, 16908304); parent.addView(extraview, (ViewGroup.LayoutParams)params); SeekBar seekBar = (SeekBar)extraview.findViewById(this.mSeekbarId); if (seekBar != null) { seekBar.setMax(this.mMaxValue - this.mMinValue); int progress = this.preferenceValue - this.mMinValue; seekBar.setProgress(progress); seekBar.setSecondaryProgress(progress); seekBar.setOnSeekBarChangeListener(this.mOnSeekBarChangeListener); seekBar.setEnabled(isEnabled()); }  this.mTextView = (TextView)extraview.findViewById(R.id.seekbar_value_label); if (this.mTextView != null) { setValueLabel(this.preferenceValue, false); this.mTextView.setEnabled(isEnabled()); }  }  }
/*     */   protected Object onGetDefaultValue(TypedArray a, int index) { return Integer.valueOf(a.getInt(index, this.mDefaultValue)); } protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) { try { this.preferenceValue = ((Integer)defaultValue).intValue(); } catch (Exception e) { this.preferenceValue = this.mDefaultValue; }
/*     */      if (restorePersistedValue)
/* 176 */       this.preferenceValue = getPersistedInt(this.preferenceValue);  persistInt(this.preferenceValue); } protected String formatValueLabel(int value, boolean fromUser) { try { return String.format(this.mFmtStr, new Object[] { Float.valueOf(value * this.mScaleValue) }); }
/* 177 */     catch (Exception e)
/* 178 */     { return String.format(Locale.US, "%f", new Object[] { Float.valueOf(value * this.mScaleValue) }); }
/*     */      }
/*     */ 
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\SeekBarPreference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */