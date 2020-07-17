/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.AttributeSet;
/*     */ import android.util.Log;
/*     */ import android.util.SparseIntArray;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ImageButton;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.RadioGroup;
/*     */ import android.widget.SeekBar;
/*     */ import android.widget.TextView;
/*     */ import com.serenegiant.common.R;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrameSelectorView
/*     */   extends LinearLayout
/*     */ {
/*  38 */   private static final String TAG = FrameSelectorView.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private static final SparseIntArray sBUTTONS = new SparseIntArray();
/*     */   static  {
/*  80 */     sBUTTONS.put(R.id.color1_button, 0);
/*  81 */     sBUTTONS.put(R.id.color2_button, 1);
/*  82 */     sBUTTONS.put(R.id.color3_button, 2);
/*  83 */     sBUTTONS.put(R.id.color4_button, 3);
/*  84 */     sBUTTONS.put(R.id.color5_button, 4);
/*  85 */     sBUTTONS.put(R.id.color6_button, 5);
/*  86 */     sBUTTONS.put(R.id.color7_button, 6);
/*  87 */     sBUTTONS.put(R.id.color8_button, 7);
/*  88 */     sBUTTONS.put(R.id.color_select_button, -1);
/*     */     
/*  90 */     sBUTTONS.put(R.id.frame_frame_button, 1);
/*  91 */     sBUTTONS.put(R.id.frame_cross_button, 2);
/*  92 */     sBUTTONS.put(R.id.frame_cross_quarter_button, 3);
/*  93 */     sBUTTONS.put(R.id.frame_circle_button, 4);
/*  94 */     sBUTTONS.put(R.id.frame_circle2_button, 6);
/*  95 */     sBUTTONS.put(R.id.frame_cross_circle_button, 5);
/*  96 */     sBUTTONS.put(R.id.frame_cross_circle2_button, 7);
/*     */ 
/*     */     
/*  99 */     COLOR_BTN_IDS = new int[] { R.id.color1_button, R.id.color2_button, R.id.color3_button, R.id.color4_button, R.id.color5_button, R.id.color6_button, R.id.color7_button, R.id.color8_button };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int[] COLOR_BTN_IDS;
/*     */ 
/*     */ 
/*     */   
/* 110 */   private final int[] mColors = new int[] { -65536, -23296, -256, -16744448, -16776961, -1, -5131855, -16777216 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FrameSelectorViewCallback mCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private final ImageButton[] mFrameButtons = new ImageButton[7]; private RadioGroup mScaleTypeRadioGroup;
/*     */   private TextView mLineWidthTv;
/*     */   private SeekBar mSeekBar;
/*     */   private final OnClickListener mOnFrameClickListener;
/*     */   private final OnClickListener mOnColorClickListener;
/*     */   private final RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;
/*     */   private final SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
/*     */   
/* 130 */   public FrameSelectorView(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public FrameSelectorView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */   
/*     */   public FrameSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
/* 138 */     super(context, attrs, defStyleAttr);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     this.mOnFrameClickListener = new OnClickListener()
/*     */       {
/*     */         public void onClick(View view) {
/* 318 */           if (FrameSelectorView.this.mCallback != null) {
/*     */             try {
/* 320 */               FrameSelectorView.this.mCallback.onFrameSelected(FrameSelectorView.this, sBUTTONS.get(view.getId()));
/* 321 */             } catch (Exception e) {
/* 322 */               Log.w(TAG, e);
/*     */             } 
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.mOnColorClickListener = new OnClickListener()
/*     */       {
/*     */         public void onClick(View view) {
/* 334 */           if (FrameSelectorView.this.mCallback != null) {
/*     */             try {
/* 336 */               int ix = sBUTTONS.get(view.getId());
/* 337 */               if (ix >= 0 && ix < 8) {
/* 338 */                 FrameSelectorView.this.mCallback.onColorSelected(FrameSelectorView.this, ix, FrameSelectorView.this.mColors[ix]);
/*     */               } else {
/* 340 */                 FrameSelectorView.this.mCallback.onColorSelected(FrameSelectorView.this, -1, 0);
/*     */               } 
/* 342 */             } catch (Exception e) {
/* 343 */               Log.w(TAG, e);
/*     */             } 
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 349 */     this.mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
/*     */       {
/*     */         public void onCheckedChanged(RadioGroup group, int checkedId)
/*     */         {
/* 353 */           int scale_type = 0;
/* 354 */           if (checkedId == R.id.scale_type_inch_radiobutton) {
/* 355 */             scale_type = 1;
/* 356 */           } else if (checkedId == R.id.scale_type_mm_radiobutton) {
/* 357 */             scale_type = 2;
/*     */           } 
/* 359 */           if (FrameSelectorView.this.mCallback != null) {
/*     */             try {
/* 361 */               FrameSelectorView.this.mCallback.onScaleSelected(FrameSelectorView.this, scale_type);
/* 362 */             } catch (Exception e) {
/* 363 */               Log.w(TAG, e);
/*     */             } 
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 369 */     this.mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
/*     */       {
/*     */         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
/* 372 */           if (FrameSelectorView.this.mLineWidthTv != null) {
/* 373 */             FrameSelectorView.this.mLineWidthTv.setText(String.format("%4.1fpx", new Object[] { Float.valueOf(progress / 10.0F) }));
/*     */           }
/* 375 */           if (fromUser && FrameSelectorView.this.mCallback != null) {
/*     */             try {
/* 377 */               FrameSelectorView.this.mCallback.onLineWidthChanged(FrameSelectorView.this, seekBar.getProgress() / 10.0F);
/* 378 */             } catch (Exception e) {
/* 379 */               Log.w(TAG, e);
/*     */             } 
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void onStartTrackingTouch(SeekBar seekBar) {}
/*     */ 
/*     */         
/*     */         public void onStopTrackingTouch(SeekBar seekBar) {
/* 390 */           if (FrameSelectorView.this.mCallback != null)
/*     */             try {
/* 392 */               FrameSelectorView.this.mCallback.onLineWidthSelected(FrameSelectorView.this, seekBar.getProgress() / 10.0F);
/* 393 */             } catch (Exception e) {
/* 394 */               Log.w(TAG, e);
/*     */             }  
/*     */         }
/*     */       };
/*     */     setOrientation(1);
/*     */     LayoutInflater inflater = LayoutInflater.from(context);
/*     */     try {
/*     */       View rootView = inflater.inflate(R.layout.view_frame_selector, (ViewGroup)this, true);
/*     */       initView(rootView);
/*     */     } catch (Exception exception) {}
/*     */   }
/*     */   
/*     */   public void setCallback(FrameSelectorViewCallback callback) { this.mCallback = callback; }
/*     */   
/*     */   public FrameSelectorViewCallback getCallback() { return this.mCallback; }
/*     */   
/*     */   public void setFrameType(int frame_type) {
/*     */     if (frame_type > 0 && frame_type < 8) {
/*     */       for (int i = 0; i < 7; i++)
/*     */         this.mFrameButtons[i].setSelected(false); 
/*     */       this.mFrameButtons[frame_type - 1].setSelected(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setScaleType(int scale_type) {
/*     */     if (this.mScaleTypeRadioGroup != null) {
/*     */       switch (scale_type) {
/*     */         case 1:
/*     */           this.mScaleTypeRadioGroup.check(R.id.scale_type_inch_radiobutton);
/*     */           return;
/*     */         case 2:
/*     */           this.mScaleTypeRadioGroup.check(R.id.scale_type_mm_radiobutton);
/*     */           return;
/*     */       } 
/*     */       this.mScaleTypeRadioGroup.check(R.id.scale_type_non_radiobutton);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLineWidth(float width) {
/*     */     if (this.mSeekBar != null)
/*     */       this.mSeekBar.setProgress((int)(width * 10.0F)); 
/*     */   }
/*     */   
/*     */   public void setColors(int[] colors) {
/*     */     if (colors != null && colors.length >= 8) {
/*     */       System.arraycopy(this.mColors, 0, colors, 0, 8);
/*     */       updateColors(colors);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int[] getColors() { return this.mColors; }
/*     */   
/*     */   public void addColor(int color) {
/*     */     int ix = 0;
/*     */     for (int i = 0; i < 8; i++) {
/*     */       if (this.mColors[i] == color) {
/*     */         ix = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     for (int i = ix; i < 7; i++)
/*     */       this.mColors[i] = this.mColors[i + 1]; 
/*     */     this.mColors[7] = color;
/*     */     updateColors(this.mColors);
/*     */   }
/*     */   
/*     */   private void updateColors(final int[] colors) {
/*     */     post(new Runnable() {
/*     */           public void run() {
/*     */             if (colors != null && colors.length >= 8)
/*     */               for (int i = 0; i < 8; i++) {
/*     */                 int id = COLOR_BTN_IDS[i];
/*     */                 ImageButton button = (ImageButton)FrameSelectorView.this.findViewById(id);
/*     */                 if (button != null)
/*     */                   button.setBackgroundColor(colors[i]); 
/*     */               }  
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void initView(View rootView) {
/*     */     this.mFrameButtons[0] = (ImageButton)rootView.findViewById(R.id.frame_frame_button);
/*     */     this.mFrameButtons[0].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[1] = (ImageButton)rootView.findViewById(R.id.frame_cross_button);
/*     */     this.mFrameButtons[1].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[2] = (ImageButton)rootView.findViewById(R.id.frame_cross_quarter_button);
/*     */     this.mFrameButtons[2].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[3] = (ImageButton)rootView.findViewById(R.id.frame_circle_button);
/*     */     this.mFrameButtons[3].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[4] = (ImageButton)rootView.findViewById(R.id.frame_circle2_button);
/*     */     this.mFrameButtons[4].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[5] = (ImageButton)rootView.findViewById(R.id.frame_cross_circle_button);
/*     */     this.mFrameButtons[5].setOnClickListener(this.mOnFrameClickListener);
/*     */     this.mFrameButtons[6] = (ImageButton)rootView.findViewById(R.id.frame_cross_circle2_button);
/*     */     this.mFrameButtons[6].setOnClickListener(this.mOnFrameClickListener);
/*     */     ImageButton button = (ImageButton)rootView.findViewById(R.id.color1_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color2_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color3_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color4_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color5_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color6_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color7_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color8_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     button = (ImageButton)rootView.findViewById(R.id.color_select_button);
/*     */     button.setOnClickListener(this.mOnColorClickListener);
/*     */     this.mScaleTypeRadioGroup = (RadioGroup)rootView.findViewById(R.id.scale_type_radiogroup);
/*     */     this.mScaleTypeRadioGroup.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
/*     */     this.mLineWidthTv = (TextView)rootView.findViewById(R.id.line_width_textview);
/*     */     this.mSeekBar = (SeekBar)rootView.findViewById(R.id.line_width_seekbar);
/*     */     this.mSeekBar.setOnSeekBarChangeListener(this.mOnSeekBarChangeListener);
/*     */   }
/*     */   
/*     */   public static interface FrameSelectorViewCallback {
/*     */     void onFrameSelected(FrameSelectorView param1FrameSelectorView, int param1Int);
/*     */     
/*     */     void onColorSelected(FrameSelectorView param1FrameSelectorView, int param1Int1, int param1Int2);
/*     */     
/*     */     void onScaleSelected(FrameSelectorView param1FrameSelectorView, int param1Int);
/*     */     
/*     */     void onLineWidthChanged(FrameSelectorView param1FrameSelectorView, float param1Float);
/*     */     
/*     */     void onLineWidthSelected(FrameSelectorView param1FrameSelectorView, float param1Float);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\FrameSelectorView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */