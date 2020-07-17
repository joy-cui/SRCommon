/*     */ package com.serenegiant.graphics;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Outline;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.RectF;
/*     */ import android.graphics.drawable.shapes.Shape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseShape
/*     */   extends Shape
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  17 */   protected final String TAG = getClass().getSimpleName();
/*     */   
/*  19 */   protected final RectF mBoundsRect = new RectF();
/*     */ 
/*     */ 
/*     */   
/*     */   protected final float mStdWidth;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final float mStdHeight;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final float mStdWidth2;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final float mStdHeight2;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float mScaleX;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float mScaleY;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float mRotation;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float mPivotX;
/*     */ 
/*     */   
/*     */   protected float mPivotY;
/*     */ 
/*     */   
/*     */   final Paint debugPaint;
/*     */ 
/*     */ 
/*     */   
/*     */   @TargetApi(21)
/*     */   public void getOutline(Outline outline) {
/*  63 */     RectF rect = boundsRect();
/*  64 */     outline.setRect((int)Math.ceil(rect.left), (int)Math.ceil(rect.top), 
/*  65 */         (int)Math.floor(rect.right), (int)Math.floor(rect.bottom));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResize(float width, float height) {
/*  70 */     this.mBoundsRect.set(0.0F, 0.0F, width, height);
/*  71 */     this.mScaleX = width / this.mStdWidth;
/*  72 */     this.mScaleY = height / this.mStdHeight;
/*  73 */     this.mPivotX = width / 2.0F;
/*  74 */     this.mPivotY = height / 2.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected final RectF boundsRect() { return this.mBoundsRect; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseShape clone() throws CloneNotSupportedException {
/*  87 */     BaseShape shape = (BaseShape)super.clone();
/*  88 */     shape.mBoundsRect.set(this.mBoundsRect);
/*  89 */     return shape;
/*     */   }
/*     */ 
/*     */   
/*  93 */   public float getScaleX() { return this.mScaleX; }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public float getScaleY() { return this.mScaleY; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public void setRotation(float rotation) { this.mRotation = rotation; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public float getRotation() { return this.mRotation; }
/*     */   
/*     */   public BaseShape(float std_width, float std_height) {
/* 108 */     this.debugPaint = new Paint();
/*     */     this.mStdWidth = std_width;
/*     */     this.mStdHeight = std_height;
/*     */     this.mStdWidth2 = std_width / 2.0F;
/*     */     this.mStdHeight2 = this.mStdHeight / 2.0F;
/*     */   }
/*     */   public void draw(Canvas canvas, Paint paint) {
/* 115 */     int count = canvas.save();
/* 116 */     canvas.translate(this.mPivotX, this.mPivotY);
/* 117 */     canvas.rotate(this.mRotation);
/* 118 */     canvas.scale(this.mScaleX, this.mScaleY);
/* 119 */     canvas.translate(-this.mStdWidth2, -this.mStdHeight2);
/* 120 */     doDraw(canvas, paint);
/* 121 */     canvas.restoreToCount(count);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected void doDraw(Canvas canvas, Paint paint) { canvas.drawRect(this.mBoundsRect, paint); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\graphics\BaseShape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */