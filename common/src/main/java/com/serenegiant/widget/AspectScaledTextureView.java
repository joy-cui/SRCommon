/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.graphics.Matrix;
/*     */ import android.graphics.SurfaceTexture;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.TextureView;
/*     */ import android.view.View;
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
/*     */ public class AspectScaledTextureView
/*     */   extends TextureView
/*     */   implements TextureView.SurfaceTextureListener, IAspectRatioView, IScaledView
/*     */ {
/*  33 */   protected final Matrix mImageMatrix = new Matrix();
/*  34 */   private int mScaleMode = 0;
/*  35 */   private double mRequestedAspect = -1.0D; private volatile boolean mHasSurface;
/*     */   private int prevWidth;
/*     */   private int prevHeight;
/*     */   
/*  39 */   public AspectScaledTextureView(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public AspectScaledTextureView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */   
/*     */   public AspectScaledTextureView(Context context, AttributeSet attrs, int defStyleAttr)
/*     */   {
/*  47 */     super(context, attrs, defStyleAttr);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.prevWidth = -1;
/*  96 */     this.prevHeight = -1; TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectScaledTextureView, defStyleAttr, 0); try { this.mRequestedAspect = a.getFloat(R.styleable.AspectScaledTextureView_aspect_ratio, -1.0F); this.mScaleMode = a.getInt(R.styleable.AspectScaledTextureView_scale_mode, 0); }
/*     */     finally
/*     */     { a.recycle(); }
/*  99 */      setSurfaceTextureListener(this); } protected void onLayout(boolean changed, int left, int top, int right, int bottom) { super.onLayout(changed, left, top, right, bottom);
/*     */ 
/*     */     
/* 102 */     if (getWidth() == 0 || getHeight() == 0)
/* 103 */       return;  if (this.prevWidth != getWidth() || this.prevHeight != getHeight()) {
/* 104 */       this.prevWidth = getWidth();
/* 105 */       this.prevHeight = getHeight();
/* 106 */       onResize(this.prevWidth, this.prevHeight);
/*     */     } 
/* 108 */     init(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
/* 119 */     this.mHasSurface = true;
/* 120 */     init();
/*     */   } protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { if (this.mRequestedAspect > 0.0D && this.mScaleMode == 0) {
/*     */       int initialWidth = MeasureSpec.getSize(widthMeasureSpec); int initialHeight = MeasureSpec.getSize(heightMeasureSpec); int horizPadding = getPaddingLeft() + getPaddingRight(); int vertPadding = getPaddingTop() + getPaddingBottom(); initialWidth -= horizPadding; initialHeight -= vertPadding; double viewAspectRatio = initialWidth / initialHeight; double aspectDiff = this.mRequestedAspect / viewAspectRatio - 1.0D; if (Math.abs(aspectDiff) > 0.01D) {
/*     */         if (aspectDiff > 0.0D) {
/*     */           initialHeight = (int)(initialWidth / this.mRequestedAspect);
/*     */         } else {
/*     */           initialWidth = (int)(initialHeight * this.mRequestedAspect);
/*     */         }  initialWidth += horizPadding; initialHeight += vertPadding; widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, 1073741824); heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, 1073741824);
/*     */       } 
/*     */     } 
/* 130 */     super.onMeasure(widthMeasureSpec, heightMeasureSpec); } protected void onResize(int width, int height) {} public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {} public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public void onSurfaceTextureUpdated(SurfaceTexture surface) { this.mHasSurface = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAspectRatio(double aspectRatio) {
/* 147 */     if (this.mRequestedAspect != aspectRatio) {
/* 148 */       this.mRequestedAspect = aspectRatio;
/* 149 */       requestLayout();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public void setAspectRatio(int width, int height) { setAspectRatio(width / height); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   public double getAspectRatio() { return this.mRequestedAspect; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleMode(int scale_mode) {
/* 169 */     if (this.mScaleMode != scale_mode) {
/* 170 */       this.mScaleMode = scale_mode;
/* 171 */       requestLayout();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public int getScaleMode() { return this.mScaleMode; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/*     */     double height, width, scale, scale_y, scale_x, video_height, video_width;
/* 192 */     int view_width = getWidth();
/* 193 */     int view_height = getHeight();
/*     */     
/* 195 */     this.mImageMatrix.reset();
/* 196 */     switch (this.mScaleMode) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 202 */         video_width = this.mRequestedAspect * view_height;
/* 203 */         video_height = view_height;
/* 204 */         scale_x = view_width / video_width;
/* 205 */         scale_y = view_height / video_height;
/* 206 */         scale = Math.max(scale_x, scale_y);
/*     */         
/* 208 */         width = scale * video_width;
/* 209 */         height = scale * video_height;
/*     */ 
/*     */         
/* 212 */         this.mImageMatrix.postScale((float)(width / view_width), (float)(height / view_height), (view_width / 2), (view_height / 2));
/*     */         break;
/*     */     } 
/* 215 */     setTransform(this.mImageMatrix);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\AspectScaledTextureView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */