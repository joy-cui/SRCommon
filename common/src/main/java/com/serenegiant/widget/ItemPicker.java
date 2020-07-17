/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.os.Handler;
/*     */ import android.text.InputFilter;
/*     */ import android.text.Spanned;
/*     */ import android.text.TextUtils;
/*     */ import android.text.method.NumberKeyListener;
/*     */ import android.util.AttributeSet;
/*     */ import android.util.Log;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemPicker
/*     */   extends LinearLayout
/*     */ {
/*  82 */   private final Handler mHandler = new Handler();
/*  83 */   private final Runnable mRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  86 */         if (ItemPicker.this.mIncrement) {
/*  87 */           ItemPicker.this.changeCurrent(ItemPicker.this.mCurrentValue + 1);
/*  88 */           ItemPicker.this.mHandler.postDelayed(this, ItemPicker.this.mSpeed);
/*  89 */         } else if (ItemPicker.this.mDecrement) {
/*  90 */           ItemPicker.this.changeCurrent(ItemPicker.this.mCurrentValue - 1);
/*  91 */           ItemPicker.this.mHandler.postDelayed(this, ItemPicker.this.mSpeed);
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final EditText mText;
/*     */ 
/*     */   
/*     */   private final InputFilter mNumberInputFilter;
/*     */ 
/*     */   
/*     */   private String[] mDisplayedValues;
/*     */ 
/*     */   
/*     */   private int mMinValue;
/*     */ 
/*     */   
/*     */   private int mMaxValue;
/*     */ 
/*     */   
/*     */   private int mCurrentValue;
/*     */ 
/*     */   
/*     */   private int mPrevValue;
/*     */ 
/*     */   
/*     */   private OnChangedListener mListener;
/*     */ 
/*     */   
/*     */   private Formatter mFormatter;
/*     */   
/* 124 */   private long mSpeed = 300L;
/*     */ 
/*     */   
/*     */   private boolean mIncrement;
/*     */ 
/*     */   
/*     */   private boolean mDecrement;
/*     */ 
/*     */   
/* 133 */   public ItemPicker(Context context) { this(context, null); }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public ItemPicker(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemPicker(Context context, AttributeSet attrs, int defStyle) {
/* 147 */     super(context, attrs);
/* 148 */     setOrientation(0);
/* 149 */     setGravity(16);
/*     */     
/* 151 */     LayoutInflater inflater = LayoutInflater.from(context);
/* 152 */     inflater.inflate(R.layout.item_picker, (ViewGroup)this, true);
/*     */     
/* 154 */     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemPicker, defStyle, 0);
/*     */ 
/*     */     
/* 157 */     int minValue = a.getInt(R.styleable.ItemPicker_ItemPickerMinItemValue, -1);
/* 158 */     int maxValue = a.getInt(R.styleable.ItemPicker_ItemPickerMaxItemValue, -1);
/* 159 */     int displayedValueId = a.getResourceId(R.styleable.ItemPicker_ItemPickerDisplayedValue, -1);
/*     */     
/* 161 */     String[] displayedValue = (displayedValueId > -1) ? getResources().getStringArray(displayedValueId) : null;
/*     */     
/* 163 */     int incrementBackground = a.getResourceId(R.styleable.ItemPicker_ItemPickerIncrementBackground, -1);
/* 164 */     int decrementBackground = a.getResourceId(R.styleable.ItemPicker_ItemPickerDecrementBackground, -1);
/* 165 */     int incrementSrc = a.getResourceId(R.styleable.ItemPicker_ItemPickerIncrementSrc, -1);
/* 166 */     int decrementSrc = a.getResourceId(R.styleable.ItemPicker_ItemPickerDecrementSrc, -1);
/* 167 */     int editTextBackground = a.getResourceId(R.styleable.ItemPicker_ItemPickerEditTextBackground, -1);
/* 168 */     int currentValue = a.getInt(R.styleable.ItemPicker_ItemPickerCurrentItemValue, -1);
/* 169 */     int speed = a.getInt(R.styleable.ItemPicker_ItemPickerSpeed, -1);
/* 170 */     a.recycle();
/* 171 */     a = null;
/*     */     
/* 173 */     OnClickListener clickListener = new OnClickListener()
/*     */       {
/*     */         public void onClick(View v) {
/* 176 */           ItemPicker.this.validateInput((View)ItemPicker.this.mText);
/* 177 */           if (!ItemPicker.this.mText.hasFocus()) ItemPicker.this.mText.requestFocus();
/*     */ 
/*     */           
/* 180 */           if (R.id.increment == v.getId()) {
/* 181 */             ItemPicker.this.changeCurrent(ItemPicker.this.mCurrentValue + 1);
/* 182 */           } else if (R.id.decrement == v.getId()) {
/* 183 */             ItemPicker.this.changeCurrent(ItemPicker.this.mCurrentValue - 1);
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 188 */     OnFocusChangeListener focusListener = new OnFocusChangeListener()
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public void onFocusChange(View v, boolean hasFocus)
/*     */         {
/* 195 */           if (!hasFocus) {
/* 196 */             ItemPicker.this.validateInput(v);
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 201 */     OnLongClickListener longClickListener = new OnLongClickListener()
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean onLongClick(View v)
/*     */         {
/* 211 */           ItemPicker.this.mText.clearFocus();
/*     */           
/* 213 */           if (R.id.increment == v.getId()) {
/* 214 */             ItemPicker.this.mIncrement = true;
/* 215 */             ItemPicker.this.mHandler.post(ItemPicker.this.mRunnable);
/* 216 */           } else if (R.id.decrement == v.getId()) {
/* 217 */             ItemPicker.this.mDecrement = true;
/* 218 */             ItemPicker.this.mHandler.post(ItemPicker.this.mRunnable);
/*     */           } 
/* 220 */           return true;
/*     */         }
/*     */       };
/*     */     
/* 224 */     InputFilter inputFilter = new NumberPickerInputFilter();
/* 225 */     this.mNumberInputFilter = (InputFilter)new NumberRangeKeyListener();
/* 226 */     this.mIncrementButton = (ItemPickerButton)findViewById(R.id.increment);
/* 227 */     this.mIncrementButton.setOnClickListener(clickListener);
/* 228 */     this.mIncrementButton.setOnLongClickListener(longClickListener);
/* 229 */     this.mIncrementButton.setNumberPicker(this);
/* 230 */     if (incrementBackground != -1)
/* 231 */       this.mIncrementButton.setBackgroundResource(incrementBackground); 
/* 232 */     if (incrementSrc != -1) {
/* 233 */       this.mIncrementButton.setImageResource(incrementSrc);
/*     */     }
/* 235 */     this.mDecrementButton = (ItemPickerButton)findViewById(R.id.decrement);
/* 236 */     this.mDecrementButton.setOnClickListener(clickListener);
/* 237 */     this.mDecrementButton.setOnLongClickListener(longClickListener);
/* 238 */     this.mDecrementButton.setNumberPicker(this);
/* 239 */     if (decrementBackground != -1)
/* 240 */       this.mDecrementButton.setBackgroundResource(decrementBackground); 
/* 241 */     if (decrementSrc != -1) {
/* 242 */       this.mDecrementButton.setImageResource(decrementSrc);
/*     */     }
/* 244 */     this.mText = (EditText)findViewById(R.id.input);
/* 245 */     this.mText.setOnFocusChangeListener(focusListener);
/* 246 */     this.mText.setFilters(new InputFilter[] { inputFilter });
/* 247 */     this.mText.setRawInputType(2);
/* 248 */     if (editTextBackground != -1) {
/* 249 */       this.mText.setBackgroundResource(editTextBackground);
/*     */     }
/* 251 */     if (!isEnabled()) {
/* 252 */       setEnabled(false);
/*     */     }
/*     */     
/* 255 */     if (minValue > -1 && maxValue > -1) {
/* 256 */       if (displayedValue != null) {
/* 257 */         setRange(minValue, maxValue, displayedValue);
/*     */       } else {
/*     */         
/* 260 */         setRange(minValue, maxValue);
/*     */       } 
/*     */     }
/*     */     
/* 264 */     if (currentValue > -1) {
/* 265 */       setValue(currentValue);
/*     */     }
/* 267 */     if (speed > -1) {
/* 268 */       setSpeed(speed);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 279 */     super.setEnabled(enabled);
/* 280 */     this.mIncrementButton.setEnabled(enabled);
/* 281 */     this.mDecrementButton.setEnabled(enabled);
/* 282 */     this.mText.setEnabled(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnKeyListener(OnKeyListener listener) {
/* 288 */     super.setOnKeyListener(listener);
/* 289 */     this.mIncrementButton.setOnKeyListener(listener);
/* 290 */     this.mDecrementButton.setOnKeyListener(listener);
/* 291 */     this.mText.setOnKeyListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 299 */   public void setOnChangeListener(OnChangedListener listener) { this.mListener = listener; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public void setFormatter(Formatter formatter) { this.mFormatter = formatter; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 319 */   public void setRange(int min, int max) { setRange(min, max, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRange(int min, int max, String[] displayedValues) {
/* 332 */     this.mDisplayedValues = displayedValues;
/* 333 */     this.mMinValue = min;
/* 334 */     this.mMaxValue = max;
/* 335 */     if (this.mCurrentValue < min || this.mCurrentValue > max)
/* 336 */       this.mCurrentValue = min; 
/* 337 */     updateView();
/*     */     
/* 339 */     if (displayedValues != null)
/*     */     {
/* 341 */       this.mText.setRawInputType(524289);
/*     */     }
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
/*     */   public void setValue(int value) {
/* 354 */     if (value < this.mMinValue || value > this.mMaxValue) {
/*     */ 
/*     */ 
/*     */       
/* 358 */       Log.w("ItemPicker", String.format("current(%d) should be between min(%d) to max(%d) changed to min", new Object[] {
/* 359 */               Integer.valueOf(value), Integer.valueOf(this.mMinValue), Integer.valueOf(this.mMaxValue) }));
/* 360 */       value = this.mMinValue;
/*     */     } 
/* 362 */     this.mCurrentValue = value;
/* 363 */     updateView();
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
/* 374 */   public void setSpeed(long speed) { this.mSpeed = speed; }
/*     */ 
/*     */   
/*     */   private String formatNumber(int value) {
/* 378 */     return (this.mFormatter != null) ? this.mFormatter
/* 379 */       .toString(value) : 
/* 380 */       String.valueOf(value);
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
/*     */   
/*     */   protected void changeCurrent(int current) {
/* 394 */     if (current > this.mMaxValue) {
/* 395 */       current = this.mMinValue;
/* 396 */     } else if (current < this.mMinValue) {
/* 397 */       current = this.mMaxValue;
/*     */     } 
/* 399 */     this.mPrevValue = this.mCurrentValue;
/* 400 */     this.mCurrentValue = current;
/* 401 */     notifyChange();
/* 402 */     updateView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyChange() {
/* 410 */     if (this.mListener != null) {
/* 411 */       this.mListener.onChanged(this, this.mPrevValue, this.mCurrentValue);
/*     */     }
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
/*     */   
/*     */   private void updateView() {
/* 426 */     if (this.mDisplayedValues == null) {
/* 427 */       this.mText.setText(formatNumber(this.mCurrentValue));
/*     */     } else {
/* 429 */       this.mText.setText(this.mDisplayedValues[this.mCurrentValue - this.mMinValue]);
/*     */     } 
/* 431 */     this.mText.setSelection(this.mText.getText().length());
/*     */   }
/*     */   
/*     */   private void validateCurrentView(CharSequence str) {
/* 435 */     int val = getSelectedPos(str.toString());
/* 436 */     if (val >= this.mMinValue && val <= this.mMaxValue && 
/* 437 */       this.mCurrentValue != val) {
/* 438 */       this.mPrevValue = this.mCurrentValue;
/* 439 */       this.mCurrentValue = val;
/* 440 */       notifyChange();
/*     */     } 
/*     */     
/* 443 */     updateView();
/*     */   }
/*     */   
/*     */   private void validateInput(View v) {
/* 447 */     String str = String.valueOf(((TextView)v).getText());
/* 448 */     if (TextUtils.isEmpty(str)) {
/*     */ 
/*     */       
/* 451 */       updateView();
/*     */     }
/*     */     else {
/*     */       
/* 455 */       validateCurrentView(str);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 463 */   public void cancelIncrement() { this.mIncrement = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 470 */   public void cancelDecrement() { this.mDecrement = false; }
/*     */ 
/*     */   
/* 473 */   private static final char[] DIGIT_CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*     */   private ItemPickerButton mIncrementButton;
/*     */   private ItemPickerButton mDecrementButton;
/*     */   
/*     */   public static interface OnChangedListener {
/*     */     void onChanged(ItemPicker param1ItemPicker, int param1Int1, int param1Int2); }
/*     */   
/*     */   public static interface Formatter {
/*     */     String toString(int param1Int); }
/*     */   
/*     */   private class NumberPickerInputFilter implements InputFilter { public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
/* 484 */       if (ItemPicker.this.mDisplayedValues == null) {
/* 485 */         return ItemPicker.this.mNumberInputFilter.filter(source, start, end, dest, dstart, dend);
/*     */       }
/* 487 */       CharSequence filtered = String.valueOf(source.subSequence(start, end));
/*     */ 
/*     */       
/* 490 */       String result = String.valueOf(dest.subSequence(0, dstart)) + filtered + dest.subSequence(dend, dest.length());
/* 491 */       String str = String.valueOf(result).toLowerCase(Locale.US);
/* 492 */       for (String val : ItemPicker.this.mDisplayedValues) {
/* 493 */         val = val.toLowerCase(Locale.US);
/* 494 */         if (val.startsWith(str)) {
/* 495 */           return filtered;
/*     */         }
/*     */       } 
/* 498 */       return "";
/*     */     }
/*     */     
/*     */     private NumberPickerInputFilter() {} }
/*     */ 
/*     */   
/*     */   private class NumberRangeKeyListener
/*     */     extends NumberKeyListener {
/*     */     private NumberRangeKeyListener() {}
/*     */     
/* 508 */     public int getInputType() { return 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 513 */     protected char[] getAcceptedChars() { return DIGIT_CHARACTERS; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
/* 520 */       CharSequence filtered = super.filter(source, start, end, dest, dstart, dend);
/* 521 */       if (filtered == null) {
/* 522 */         filtered = source.subSequence(start, end);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 527 */       String result = String.valueOf(dest.subSequence(0, dstart)) + filtered + dest.subSequence(dend, dest.length());
/*     */       
/* 529 */       if ("".equals(result)) {
/* 530 */         return result;
/*     */       }
/* 532 */       int val = ItemPicker.this.getSelectedPos(result);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 539 */       if (val > ItemPicker.this.mMaxValue) {
/* 540 */         return "";
/*     */       }
/* 542 */       return filtered;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private int getSelectedPos(String str) {
/* 548 */     if (this.mDisplayedValues == null) {
/*     */       try {
/* 550 */         return Integer.parseInt(str);
/* 551 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */     else {
/*     */       
/* 555 */       for (int i = 0; i < this.mDisplayedValues.length; i++) {
/*     */         
/* 557 */         str = str.toLowerCase(Locale.US);
/* 558 */         if (this.mDisplayedValues[i].toLowerCase(Locale.US).startsWith(str)) {
/* 559 */           return this.mMinValue + i;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 567 */         return Integer.parseInt(str);
/* 568 */       } catch (NumberFormatException numberFormatException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 573 */     return this.mMinValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 581 */   public int getValue() { return this.mCurrentValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 589 */   protected int getEndRange() { return this.mMaxValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 597 */   protected int getBeginRange() { return this.mMinValue; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ItemPicker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */