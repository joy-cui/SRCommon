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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaEffectGLESBase
/*     */   implements IEffect
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEffectGLESBase";
/*     */   protected TextureOffscreen mOutputOffscreen;
/*     */   protected volatile boolean mEnabled = true;
/*     */   protected final MediaEffectDrawer mDrawer;
/*     */   
/*  44 */   public MediaEffectGLESBase(String shader) { this(new MediaEffectDrawer(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", shader)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public MediaEffectGLESBase(boolean isOES, String shader) { this(new MediaEffectDrawer(isOES, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", shader)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public MediaEffectGLESBase(boolean isOES, String vss, String fss) { this(new MediaEffectDrawer(isOES, vss, fss)); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public MediaEffectGLESBase(MediaEffectDrawer drawer) { this.mDrawer = drawer; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  72 */     this.mDrawer.release();
/*  73 */     if (this.mOutputOffscreen != null) {
/*  74 */       this.mOutputOffscreen.release();
/*  75 */       this.mOutputOffscreen = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public float[] getMvpMatrix() { return this.mDrawer.getMvpMatrix(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectGLESBase setMvpMatrix(float[] matrix, int offset) {
/*  94 */     this.mDrawer.setMvpMatrix(matrix, offset);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public void getMvpMatrix(float[] matrix, int offset) { this.mDrawer.getMvpMatrix(matrix, offset); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectGLESBase resize(int width, int height) {
/* 116 */     if (this.mDrawer != null) {
/* 117 */       this.mDrawer.setTexSize(width, height);
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean enabled() { return this.mEnabled; }
/*     */ 
/*     */ 
/*     */   
/*     */   public IEffect setEnable(boolean enable) {
/* 129 */     this.mEnabled = enable;
/* 130 */     return this;
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
/* 143 */     if (!this.mEnabled)
/* 144 */       return;  if (this.mOutputOffscreen == null) {
/* 145 */       this.mOutputOffscreen = new TextureOffscreen(width, height, false);
/*     */     }
/* 147 */     if (out_tex_id != this.mOutputOffscreen.getTexture() || width != this.mOutputOffscreen
/* 148 */       .getWidth() || height != this.mOutputOffscreen
/* 149 */       .getHeight()) {
/* 150 */       this.mOutputOffscreen.assignTexture(out_tex_id, width, height);
/*     */     }
/* 152 */     this.mOutputOffscreen.bind();
/*     */     try {
/* 154 */       this.mDrawer.apply(src_tex_ids[0], this.mOutputOffscreen.getTexMatrix(), 0);
/*     */     } finally {
/* 156 */       this.mOutputOffscreen.unbind();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(ISource src) {
/* 166 */     if (!this.mEnabled)
/* 167 */       return;  TextureOffscreen output_tex = src.getOutputTexture();
/* 168 */     int[] src_tex_ids = src.getSourceTexId();
/* 169 */     output_tex.bind();
/*     */     try {
/* 171 */       this.mDrawer.apply(src_tex_ids[0], output_tex.getTexMatrix(), 0);
/*     */     } finally {
/* 173 */       output_tex.unbind();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 178 */   protected int getProgram() { return this.mDrawer.getProgram(); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectGLESBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */