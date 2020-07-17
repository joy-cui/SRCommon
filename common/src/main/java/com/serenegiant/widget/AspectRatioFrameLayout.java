/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.View;
/*     */ import android.widget.FrameLayout;
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
/*     */ 
/*     */ 
/*     */ public class AspectRatioFrameLayout
/*     */   extends FrameLayout
/*     */   implements IAspectRatioView
/*     */ {
/*  34 */   private double mRequestedAspect = -1.0D;
/*     */ 
/*     */   
/*  37 */   public AspectRatioFrameLayout(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public AspectRatioFrameLayout(Context context, AttributeSet attrs) { super(context, attrs, 0); }
/*     */ 
/*     */   
/*     */   public AspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
/*  45 */     super(context, attrs, defStyleAttr);
/*  46 */     TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IAspectRatioView, defStyleAttr, 0);
/*     */     try {
/*  48 */       this.mRequestedAspect = a.getFloat(R.styleable.IAspectRatioView_aspect_ratio, -1.0F);
/*     */     } finally {
/*  50 */       a.recycle();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
/*  60 */     if (this.mRequestedAspect > 0.0D) {
/*  61 */       int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
/*  62 */       int initialHeight = MeasureSpec.getSize(heightMeasureSpec);
/*  63 */       int horizPadding = getPaddingLeft() + getPaddingRight();
/*  64 */       int vertPadding = getPaddingTop() + getPaddingBottom();
/*  65 */       initialWidth -= horizPadding;
/*  66 */       initialHeight -= vertPadding;
/*     */       
/*  68 */       double viewAspectRatio = initialWidth / initialHeight;
/*  69 */       double aspectDiff = this.mRequestedAspect / viewAspectRatio - 1.0D;
/*     */ 
/*     */       
/*  72 */       if (Math.abs(aspectDiff) > 0.01D) {
/*  73 */         if (aspectDiff > 0.0D) {
/*     */           
/*  75 */           initialHeight = (int)(initialWidth / this.mRequestedAspect);
/*     */         } else {
/*     */           
/*  78 */           initialWidth = (int)(initialHeight * this.mRequestedAspect);
/*     */         } 
/*  80 */         initialWidth += horizPadding;
/*  81 */         initialHeight += vertPadding;
/*  82 */         widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, 1073741824);
/*  83 */         heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, 1073741824);
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAspectRatio(double aspectRatio) {
/*  92 */     if (this.mRequestedAspect != aspectRatio) {
/*  93 */       this.mRequestedAspect = aspectRatio;
/*  94 */       requestLayout();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void setAspectRatio(int width, int height) { setAspectRatio(width / height); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public double getAspectRatio() { return this.mRequestedAspect; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\AspectRatioFrameLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */