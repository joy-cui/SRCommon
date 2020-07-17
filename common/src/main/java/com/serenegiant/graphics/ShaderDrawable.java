/*     */ package com.serenegiant.graphics;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.ColorFilter;
/*     */ import android.graphics.DrawFilter;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.PaintFlagsDrawFilter;
/*     */ import android.graphics.RectF;
/*     */ import android.graphics.Shader;
/*     */ import android.graphics.drawable.Drawable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderDrawable
/*     */   extends Drawable
/*     */ {
/*     */   private final Paint mPaint;
/*     */   private final DrawFilter mDrawFilter;
/*     */   private Shader mShader;
/*     */   
/*  38 */   public ShaderDrawable() { this(0, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public ShaderDrawable(int clearflags) { this(clearflags, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShaderDrawable(int clearflags, int setFlags) {
/*  54 */     this.mPaint = new Paint();
/*  55 */     this.mDrawFilter = (DrawFilter)new PaintFlagsDrawFilter(clearflags, setFlags);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Canvas canvas) {
/*  60 */     if (this.mShader != null) {
/*  61 */       int count = canvas.save();
/*  62 */       DrawFilter org = canvas.getDrawFilter();
/*  63 */       canvas.setDrawFilter(this.mDrawFilter);
/*  64 */       this.mPaint.setShader(this.mShader);
/*  65 */       canvas.drawPaint(this.mPaint);
/*  66 */       canvas.setDrawFilter(org);
/*  67 */       canvas.restoreToCount(count);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public void setAlpha(int alpha) { this.mPaint.setAlpha(alpha); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public void setColorFilter(ColorFilter cf) { this.mPaint.setColorFilter(cf); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"Override"})
/*  84 */   public ColorFilter getColorFilter() { return this.mPaint.getColorFilter(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public int getOpacity() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void setBounds(RectF bounds) { setBounds((int)bounds.left, (int)bounds.top, (int)bounds.right, (int)bounds.bottom); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void setBounds(float left, float top, float right, float bottom) { setBounds((int)left, (int)top, (int)right, (int)bottom); }
/*     */ 
/*     */   
/*     */   public Shader setShader(Shader shader) {
/* 101 */     if (this.mShader != shader) {
/* 102 */       this.mShader = shader;
/*     */     }
/* 104 */     return shader;
/*     */   }
/*     */ 
/*     */   
/* 108 */   public Shader getShader() { return this.mShader; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\graphics\ShaderDrawable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */