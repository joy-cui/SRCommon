/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.drawable.ClipDrawable;
/*     */ import android.graphics.drawable.ColorDrawable;
/*     */ import android.graphics.drawable.Drawable;
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
/*     */ public class ProgressView
/*     */   extends View
/*     */ {
/*  32 */   private int mRotation = 90;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private int mMin = 0; private int mMax = 100;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private float mScale = 100.0F;
/*     */ 
/*     */ 
/*     */   
/*  46 */   private volatile int mProgress = 40;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private int mColor = -65536;
/*     */   private Drawable mDrawable; private ClipDrawable mClipDrawable; private final Runnable mUpdateProgressTask; protected static final int GRAVITY_TOP = 48; protected static final int GRAVITY_BOTTOM = 80; protected static final int GRAVITY_LEFT = 3; protected static final int GRAVITY_RIGHT = 5; protected static final int GRAVITY_CENTER_VERTICAL = 16; protected static final int GRAVITY_FILL_VERTICAL = 112;
/*     */   protected static final int GRAVITY_CENTER_HORIZONTAL = 1;
/*     */   protected static final int GRAVITY_FILL_HORIZONTAL = 7;
/*     */   protected static final int GRAVITY_CENTER = 17;
/*     */   protected static final int GRAVITY_FILL = 119;
/*     */   protected static final int GRAVITY_CLIP_VERTICAL = 128;
/*     */   protected static final int GRAVITY_CLIP_HORIZONTAL = 8;
/*     */   protected static final int GRAVITY_START = 8388611;
/*     */   protected static final int GRAVITY_END = 8388613;
/*     */   
/*  62 */   public ProgressView(Context context) { super(context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.mUpdateProgressTask = new Runnable()
/*     */       {
/*     */         public void run() {
/* 111 */           if (ProgressView.this.mClipDrawable != null) {
/* 112 */             int level = (int)(ProgressView.this.mProgress * ProgressView.this.mScale) + ProgressView.this.mMin;
/* 113 */             if (level < 0) level = 0; 
/* 114 */             if (level > 10000) level = 10000; 
/* 115 */             ProgressView.this.mClipDrawable.setLevel(level);
/*     */           } 
/* 117 */           ProgressView.this.invalidate(); } }; } public ProgressView(Context context, AttributeSet attrs) { super(context, attrs); this.mUpdateProgressTask = new Runnable() { public void run() { if (ProgressView.this.mClipDrawable != null) { int level = (int)(ProgressView.this.mProgress * ProgressView.this.mScale) + ProgressView.this.mMin; if (level < 0) level = 0;  if (level > 10000) level = 10000;  ProgressView.this.mClipDrawable.setLevel(level); }  ProgressView.this.invalidate(); } }; } public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); this.mUpdateProgressTask = new Runnable() { public void run() { if (ProgressView.this.mClipDrawable != null) { int level = (int)(ProgressView.this.mProgress * ProgressView.this.mScale) + ProgressView.this.mMin; if (level < 0) level = 0;  if (level > 10000) level = 10000;  ProgressView.this.mClipDrawable.setLevel(level); }  ProgressView.this.invalidate(); }
/*     */          }
/*     */       ; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onDraw(Canvas canvas) {
/*     */     super.onDraw(canvas);
/*     */     this.mClipDrawable.draw(canvas);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
/*     */     super.onLayout(changed, left, top, right, bottom);
/*     */     resize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinMax(int min, int max) {
/*     */     if ((this.mMin != min || this.mMax != max) && min != max) {
/*     */       this.mMin = Math.min(min, max);
/*     */       this.mMax = Math.max(min, max);
/*     */       resize();
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
/*     */   public void setProgress(int progress) {
/*     */     if (this.mProgress != progress) {
/*     */       this.mProgress = progress;
/*     */       removeCallbacks(this.mUpdateProgressTask);
/*     */       post(this.mUpdateProgressTask);
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
/*     */   public void setRotation(int rotation) {
/* 185 */     rotation = rotation / 90 * 90 % 360;
/* 186 */     if (this.mRotation != rotation) {
/* 187 */       this.mRotation = rotation;
/* 188 */       resize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(int color) {
/* 198 */     if (this.mColor != color) {
/* 199 */       this.mColor = color;
/* 200 */       refreshDrawable(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDrawable(Drawable drawable) {
/* 210 */     if (this.mDrawable != drawable) {
/* 211 */       refreshDrawable(drawable);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void resize() {
/* 216 */     float progress = this.mProgress * this.mScale + this.mMin;
/* 217 */     this.mScale = 10000.0F / (this.mMax - this.mMin);
/* 218 */     this.mProgress = (int)((progress - this.mMin) / this.mScale);
/* 219 */     refreshDrawable(this.mDrawable);
/*     */   }
/*     */   
/*     */   protected void refreshDrawable(Drawable drawable) {
/* 223 */     this.mDrawable = drawable;
/* 224 */     if (this.mDrawable == null) {
/* 225 */       this.mDrawable = (Drawable)new ColorDrawable(this.mColor);
/*     */     }
/* 227 */     int gravity = 115;
/* 228 */     int orientation = 1;
/* 229 */     switch (this.mRotation) {
/*     */       case 90:
/* 231 */         gravity = 87;
/* 232 */         orientation = 2;
/*     */         break;
/*     */       case 180:
/* 235 */         gravity = 117;
/* 236 */         orientation = 1;
/*     */         break;
/*     */       case 270:
/* 239 */         gravity = 55;
/* 240 */         orientation = 2;
/*     */         break;
/*     */     } 
/* 243 */     this.mClipDrawable = new ClipDrawable(this.mDrawable, gravity, orientation);
/* 244 */     Rect outRect = new Rect();
/* 245 */     getDrawingRect(outRect);
/* 246 */     this.mClipDrawable.setBounds(outRect);
/* 247 */     this.mClipDrawable.setLevel((int)(this.mProgress * this.mScale) + this.mMin);
/* 248 */     postInvalidate();
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ProgressView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */