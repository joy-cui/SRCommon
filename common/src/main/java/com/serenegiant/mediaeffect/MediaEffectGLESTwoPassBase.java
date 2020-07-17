/*     */ package com.serenegiant.mediaeffect;
/*     */ 
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
/*     */ public class MediaEffectGLESTwoPassBase
/*     */   extends MediaEffectGLESBase
/*     */ {
/*     */   protected final MediaEffectKernel3x3Drawer mDrawer2;
/*     */   protected TextureOffscreen mOutputOffscreen2;
/*     */   
/*     */   public MediaEffectGLESTwoPassBase(boolean isOES, String fss) {
/*  29 */     super(isOES, fss);
/*  30 */     this.mDrawer2 = null;
/*     */   }
/*     */   
/*     */   public MediaEffectGLESTwoPassBase(String vss, String fss) {
/*  34 */     super(false, vss, fss);
/*  35 */     this.mDrawer2 = null;
/*     */   }
/*     */   
/*     */   public MediaEffectGLESTwoPassBase(boolean isOES, String vss, String fss) {
/*  39 */     super(isOES, vss, fss);
/*  40 */     this.mDrawer2 = null;
/*     */   }
/*     */   
/*     */   public MediaEffectGLESTwoPassBase(boolean isOES, String vss1, String fss1, String vss2, String fss2) {
/*  44 */     super(isOES, vss1, fss1);
/*  45 */     if (!vss1.equals(vss2) || !fss1.equals(fss2)) {
/*  46 */       this.mDrawer2 = new MediaEffectKernel3x3Drawer(isOES, vss2, fss2);
/*     */     } else {
/*  48 */       this.mDrawer2 = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void release() {
/*  54 */     if (this.mDrawer2 != null) {
/*  55 */       this.mDrawer2.release();
/*     */     }
/*  57 */     if (this.mOutputOffscreen2 != null) {
/*  58 */       this.mOutputOffscreen2.release();
/*  59 */       this.mOutputOffscreen2 = null;
/*     */     } 
/*  61 */     super.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public MediaEffectGLESBase resize(int width, int height) {
/*  66 */     super.resize(width, height);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (this.mDrawer2 != null) {
/*  76 */       this.mDrawer2.setTexSize(width, height);
/*     */     }
/*  78 */     return this;
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
/*     */   public void apply(int[] src_tex_ids, int width, int height, int out_tex_id) {
/*  91 */     if (!this.mEnabled)
/*     */       return; 
/*  93 */     if (this.mOutputOffscreen == null) {
/*  94 */       this.mOutputOffscreen = new TextureOffscreen(width, height, false);
/*     */     }
/*  96 */     this.mOutputOffscreen.bind();
/*     */     try {
/*  98 */       this.mDrawer.apply(src_tex_ids[0], this.mOutputOffscreen.getTexMatrix(), 0);
/*     */     } finally {
/* 100 */       this.mOutputOffscreen.unbind();
/*     */     } 
/*     */     
/* 103 */     if (this.mOutputOffscreen2 == null) {
/* 104 */       this.mOutputOffscreen2 = new TextureOffscreen(width, height, false);
/*     */     }
/*     */     
/* 107 */     if (out_tex_id != this.mOutputOffscreen2.getTexture() || width != this.mOutputOffscreen2
/* 108 */       .getWidth() || height != this.mOutputOffscreen2
/* 109 */       .getHeight()) {
/* 110 */       this.mOutputOffscreen2.assignTexture(out_tex_id, width, height);
/*     */     }
/* 112 */     this.mOutputOffscreen2.bind();
/*     */     try {
/* 114 */       if (this.mDrawer2 != null) {
/* 115 */         this.mDrawer2.apply(this.mOutputOffscreen.getTexture(), this.mOutputOffscreen2.getTexMatrix(), 0);
/*     */       } else {
/* 117 */         this.mDrawer.apply(this.mOutputOffscreen.getTexture(), this.mOutputOffscreen2.getTexMatrix(), 0);
/*     */       } 
/*     */     } finally {
/* 120 */       this.mOutputOffscreen2.unbind();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(ISource src) {
/* 126 */     if (!this.mEnabled)
/* 127 */       return;  TextureOffscreen output_tex = src.getOutputTexture();
/* 128 */     int[] src_tex_ids = src.getSourceTexId();
/* 129 */     int width = src.getWidth();
/* 130 */     int height = src.getHeight();
/*     */     
/* 132 */     if (this.mOutputOffscreen == null) {
/* 133 */       this.mOutputOffscreen = new TextureOffscreen(width, height, false);
/*     */     }
/* 135 */     this.mOutputOffscreen.bind();
/*     */     try {
/* 137 */       this.mDrawer.apply(src_tex_ids[0], this.mOutputOffscreen.getTexMatrix(), 0);
/*     */     } finally {
/* 139 */       this.mOutputOffscreen.unbind();
/*     */     } 
/*     */     
/* 142 */     output_tex.bind();
/*     */     try {
/* 144 */       if (this.mDrawer2 != null) {
/* 145 */         this.mDrawer2.apply(this.mOutputOffscreen.getTexture(), output_tex.getTexMatrix(), 0);
/*     */       } else {
/* 147 */         this.mDrawer.apply(this.mOutputOffscreen.getTexture(), output_tex.getTexMatrix(), 0);
/*     */       } 
/*     */     } finally {
/* 150 */       output_tex.unbind();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectGLESTwoPassBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */