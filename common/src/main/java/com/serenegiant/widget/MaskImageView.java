/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.graphics.BlurMaskFilter;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.MaskFilter;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.PorterDuff;
/*     */ import android.graphics.PorterDuffXfermode;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.RectF;
/*     */ import android.graphics.Xfermode;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.util.AttributeSet;
/*     */ import android.widget.ImageView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MaskImageView
/*     */   extends ImageView
/*     */ {
/*  38 */   private final Paint mMaskedPaint = new Paint();
/*  39 */   private final Paint mCopyPaint = new Paint();
/*  40 */   private final Rect mMaskBounds = new Rect();
/*  41 */   private final RectF mViewBoundsF = new RectF();
/*     */   
/*     */   private Drawable mMaskDrawable;
/*     */   
/*  45 */   public MaskImageView(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public MaskImageView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */   
/*     */   public MaskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
/*  53 */     super(context, attrs, defStyleAttr);
/*  54 */     this.mMaskedPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
/*  55 */     this.mMaskDrawable = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setMaskDrawable(Drawable mask_drawable) {
/*  65 */     if (this.mMaskDrawable != mask_drawable) {
/*  66 */       this.mMaskDrawable = mask_drawable;
/*  67 */       if (this.mMaskDrawable != null) {
/*  68 */         this.mMaskDrawable.setBounds(this.mMaskBounds);
/*     */       }
/*  70 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void onSizeChanged(int width, int height, int old_width, int old_height) {
/*  76 */     super.onSizeChanged(width, height, old_width, old_height);
/*  77 */     if (width == 0 || height == 0)
/*     */       return; 
/*  79 */     int padding_left = getPaddingLeft();
/*  80 */     int padding_top = getPaddingTop();
/*  81 */     int sz = Math.min(width - padding_left - getPaddingRight(), height - padding_top - getPaddingBottom());
/*  82 */     int left = (width - sz) / 2 + padding_left;
/*  83 */     int top = (height - sz) / 2 + padding_top;
/*  84 */     this.mMaskBounds.set(left, top, left + sz, top + sz);
/*  85 */     if (sz > 3) {
/*  86 */       this.mMaskedPaint.setMaskFilter((MaskFilter)new BlurMaskFilter((sz * 2) / 3.0F, BlurMaskFilter.Blur.NORMAL));
/*     */     }
/*     */ 
/*     */     
/*  90 */     this.mViewBoundsF.set(0.0F, 0.0F, width, height);
/*  91 */     if (this.mMaskDrawable != null) {
/*  92 */       this.mMaskDrawable.setBounds(this.mMaskBounds);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void onDraw(Canvas canvas) {
/*  98 */     int saveCount = canvas.saveLayer(this.mViewBoundsF, this.mCopyPaint, 12);
/*     */     
/*     */     try {
/* 101 */       if (this.mMaskDrawable != null) {
/* 102 */         this.mMaskDrawable.draw(canvas);
/* 103 */         canvas.saveLayer(this.mViewBoundsF, this.mMaskedPaint, 0);
/*     */       } 
/* 105 */       super.onDraw(canvas);
/*     */     } finally {
/* 107 */       canvas.restoreToCount(saveCount);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\MaskImageView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */