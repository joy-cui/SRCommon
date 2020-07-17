/*     */ package com.serenegiant.mediaeffect;
/*     */ 
/*     */ import android.util.Log;
/*     */ import com.serenegiant.glutils.GLDrawer2D;
/*     */ import com.serenegiant.glutils.TextureOffscreen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaSource
/*     */   implements ISource
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaSource";
/*     */   protected TextureOffscreen mSourceScreen;
/*     */   protected TextureOffscreen mOutputScreen;
/*     */   protected int mWidth;
/*     */   protected int mHeight;
/*  33 */   protected int[] mSrcTexIds = new int[1];
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean needSwap;
/*     */ 
/*     */ 
/*     */   
/*  41 */   public MediaSource() { resize(1, 1); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public MediaSource(int width, int height) { resize(width, height); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ISource reset() {
/*  50 */     this.needSwap = false;
/*  51 */     this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ISource resize(int width, int height) {
/*  58 */     if (this.mWidth != width || this.mHeight != height) {
/*  59 */       if (this.mSourceScreen != null) {
/*  60 */         this.mSourceScreen.release();
/*  61 */         this.mSourceScreen = null;
/*     */       } 
/*  63 */       if (this.mOutputScreen != null) {
/*  64 */         this.mOutputScreen.release();
/*  65 */         this.mOutputScreen = null;
/*     */       } 
/*  67 */       if (width > 0 && height > 0) {
/*     */ 
/*     */         
/*  70 */         this.mSourceScreen = new TextureOffscreen(width, height, false, false);
/*  71 */         this.mOutputScreen = new TextureOffscreen(width, height, false, false);
/*  72 */         this.mWidth = width;
/*  73 */         this.mHeight = height;
/*  74 */         this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
/*     */       } 
/*     */     } 
/*  77 */     this.needSwap = false;
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ISource apply(IEffect effect) {
/*  83 */     if (this.mSourceScreen != null) {
/*  84 */       if (this.needSwap) {
/*  85 */         TextureOffscreen temp = this.mSourceScreen;
/*  86 */         this.mSourceScreen = this.mOutputScreen;
/*  87 */         this.mOutputScreen = temp;
/*  88 */         this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
/*     */       } 
/*  90 */       this.needSwap = !this.needSwap;
/*     */       
/*  92 */       effect.apply(this);
/*     */     } 
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public int getWidth() { return this.mWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public int getHeight() { return this.mHeight; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public int[] getSourceTexId() { return this.mSrcTexIds; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public int getOutputTexId() { return this.needSwap ? this.mOutputScreen.getTexture() : this.mSourceScreen.getTexture(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public float[] getTexMatrix() { return this.needSwap ? this.mOutputScreen.getTexMatrix() : this.mSourceScreen.getTexMatrix(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public TextureOffscreen getOutputTexture() { return this.needSwap ? this.mOutputScreen : this.mSourceScreen; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 129 */     this.mSrcTexIds[0] = -1;
/* 130 */     if (this.mSourceScreen != null) {
/* 131 */       this.mSourceScreen.release();
/* 132 */       this.mSourceScreen = null;
/*     */     } 
/* 134 */     if (this.mOutputScreen != null) {
/* 135 */       this.mOutputScreen.release();
/* 136 */       this.mOutputScreen = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public MediaSource bind() {
/* 141 */     this.mSourceScreen.bind();
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public MediaSource unbind() {
/* 146 */     this.mSourceScreen.unbind();
/* 147 */     reset();
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaSource setSource(GLDrawer2D drawer, int tex_id, float[] tex_matrix) {
/* 159 */     this.mSourceScreen.bind();
/*     */     try {
/* 161 */       drawer.draw(tex_id, tex_matrix, 0);
/* 162 */     } catch (RuntimeException e) {
/* 163 */       Log.w("MediaSource", e);
/*     */     } finally {
/* 165 */       this.mSourceScreen.unbind();
/*     */     } 
/* 167 */     reset();
/* 168 */     return this;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */