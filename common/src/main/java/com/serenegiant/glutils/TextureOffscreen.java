/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.graphics.Bitmap;
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.GLUtils;
/*     */ import android.opengl.Matrix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextureOffscreen
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "TextureOffscreen";
/*     */   private static final boolean DEFAULT_ADJUST_POWER2 = false;
/*     */   private final int TEX_TARGET;
/*     */   private final boolean mHasDepthBuffer;
/*     */   private final boolean mAdjustPower2;
/*     */   private int mWidth;
/*     */   private int mHeight;
/*     */   private int mTexWidth;
/*     */   private int mTexHeight;
/*  41 */   private int mFBOTextureName = -1;
/*  42 */   private int mDepthBufferObj = -1; private int mFrameBufferObj = -1;
/*  43 */   private final float[] mTexMatrix = new float[16];
/*     */ 
/*     */ 
/*     */   
/*     */   private final float[] mResultMatrix;
/*     */ 
/*     */ 
/*     */   
/*  51 */   public TextureOffscreen(int width, int height) { this(3553, width, height, false, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public TextureOffscreen(int width, int height, boolean use_depth_buffer) { this(3553, width, height, use_depth_buffer, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public TextureOffscreen(int width, int height, boolean use_depth_buffer, boolean adjust_power2) { this(3553, width, height, use_depth_buffer, adjust_power2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public TextureOffscreen(int tex_id, int width, int height) { this(3553, tex_id, width, height, false, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public TextureOffscreen(int tex_id, int width, int height, boolean use_depth_buffer) { this(3553, tex_id, width, height, use_depth_buffer, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public void release() { releaseFrameBuffer(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {
/* 148 */     GLES20.glBindFramebuffer(36160, this.mFrameBufferObj);
/* 149 */     GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public void unbind() { GLES20.glBindFramebuffer(36160, 0); }
/*     */   
/*     */   public TextureOffscreen(int tex_target, int width, int height, boolean use_depth_buffer, boolean adjust_power2) {
/* 160 */     this.mResultMatrix = new float[16]; this.TEX_TARGET = tex_target; this.mWidth = width; this.mHeight = height; this.mHasDepthBuffer = use_depth_buffer; this.mAdjustPower2 = adjust_power2; prepareFramebuffer(width, height); } public TextureOffscreen(int tex_target, int tex_id, int width, int height, boolean use_depth_buffer, boolean adjust_power2) { this.mResultMatrix = new float[16]; this.TEX_TARGET = tex_target;
/*     */     this.mWidth = width;
/*     */     this.mHeight = height;
/*     */     this.mHasDepthBuffer = use_depth_buffer;
/*     */     this.mAdjustPower2 = adjust_power2;
/*     */     createFrameBuffer(width, height);
/* 166 */     assignTexture(tex_id, width, height); } public float[] getTexMatrix() { System.arraycopy(this.mTexMatrix, 0, this.mResultMatrix, 0, 16);
/* 167 */     return this.mResultMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public float[] getRawTexMatrix() { return this.mTexMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public void getTexMatrix(float[] matrix, int offset) { System.arraycopy(this.mTexMatrix, 0, matrix, offset, this.mTexMatrix.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public int getTexture() { return this.mFBOTextureName; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignTexture(int texture_name, int width, int height) {
/* 198 */     if (width > this.mTexWidth || height > this.mTexHeight) {
/* 199 */       this.mWidth = width;
/* 200 */       this.mHeight = height;
/* 201 */       releaseFrameBuffer();
/* 202 */       createFrameBuffer(width, height);
/*     */     } 
/* 204 */     this.mFBOTextureName = texture_name;
/*     */     
/* 206 */     GLES20.glBindFramebuffer(36160, this.mFrameBufferObj);
/* 207 */     GLHelper.checkGlError("glBindFramebuffer " + this.mFrameBufferObj);
/*     */     
/* 209 */     GLES20.glFramebufferTexture2D(36160, 36064, this.TEX_TARGET, this.mFBOTextureName, 0);
/*     */     
/* 211 */     GLHelper.checkGlError("glFramebufferTexture2D");
/*     */     
/* 213 */     if (this.mHasDepthBuffer) {
/*     */       
/* 215 */       GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.mDepthBufferObj);
/* 216 */       GLHelper.checkGlError("glFramebufferRenderbuffer");
/*     */     } 
/*     */ 
/*     */     
/* 220 */     int status = GLES20.glCheckFramebufferStatus(36160);
/* 221 */     if (status != 36053) {
/* 222 */       throw new RuntimeException("Framebuffer not complete, status=" + status);
/*     */     }
/*     */ 
/*     */     
/* 226 */     GLES20.glBindFramebuffer(36160, 0);
/*     */ 
/*     */     
/* 229 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/* 230 */     this.mTexMatrix[0] = width / this.mTexWidth;
/* 231 */     this.mTexMatrix[5] = height / this.mTexHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadBitmap(Bitmap bitmap) {
/* 236 */     int width = bitmap.getWidth();
/* 237 */     int height = bitmap.getHeight();
/* 238 */     if (width > this.mTexWidth || height > this.mTexHeight) {
/* 239 */       this.mWidth = width;
/* 240 */       this.mHeight = height;
/* 241 */       releaseFrameBuffer();
/* 242 */       createFrameBuffer(width, height);
/*     */     } 
/* 244 */     GLES20.glBindTexture(this.TEX_TARGET, this.mFBOTextureName);
/* 245 */     GLUtils.texImage2D(this.TEX_TARGET, 0, bitmap, 0);
/* 246 */     GLES20.glBindTexture(this.TEX_TARGET, 0);
/*     */     
/* 248 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/* 249 */     this.mTexMatrix[0] = width / this.mTexWidth;
/* 250 */     this.mTexMatrix[5] = height / this.mTexHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void prepareFramebuffer(int width, int height) {
/* 258 */     GLHelper.checkGlError("prepareFramebuffer start");
/*     */     
/* 260 */     createFrameBuffer(width, height);
/*     */     
/* 262 */     int tex_name = GLHelper.initTex(this.TEX_TARGET, 33984, 9729, 9729, 33071);
/*     */     
/* 264 */     GLES20.glTexImage2D(this.TEX_TARGET, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
/*     */     
/* 266 */     GLHelper.checkGlError("glTexImage2D");
/*     */ 
/*     */     
/* 269 */     assignTexture(tex_name, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void createFrameBuffer(int width, int height) {
/* 274 */     int[] ids = new int[1];
/*     */     
/* 276 */     if (this.mAdjustPower2) {
/*     */       
/* 278 */       int w = 1;
/* 279 */       for (; w < width; w <<= 1);
/* 280 */       int h = 1;
/* 281 */       for (; h < height; h <<= 1);
/* 282 */       if (this.mTexWidth != w || this.mTexHeight != h) {
/* 283 */         this.mTexWidth = w;
/* 284 */         this.mTexHeight = h;
/*     */       } 
/*     */     } else {
/* 287 */       this.mTexWidth = width;
/* 288 */       this.mTexHeight = height;
/*     */     } 
/*     */     
/* 291 */     if (this.mHasDepthBuffer) {
/*     */       
/* 293 */       GLES20.glGenRenderbuffers(1, ids, 0);
/* 294 */       this.mDepthBufferObj = ids[0];
/* 295 */       GLES20.glBindRenderbuffer(36161, this.mDepthBufferObj);
/*     */       
/* 297 */       GLES20.glRenderbufferStorage(36161, 33189, this.mTexWidth, this.mTexHeight);
/*     */     } 
/*     */     
/* 300 */     GLES20.glGenFramebuffers(1, ids, 0);
/* 301 */     GLHelper.checkGlError("glGenFramebuffers");
/* 302 */     this.mFrameBufferObj = ids[0];
/* 303 */     GLES20.glBindFramebuffer(36160, this.mFrameBufferObj);
/* 304 */     GLHelper.checkGlError("glBindFramebuffer " + this.mFrameBufferObj);
/*     */ 
/*     */     
/* 307 */     GLES20.glBindFramebuffer(36160, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void releaseFrameBuffer() {
/* 313 */     int[] names = new int[1];
/*     */     
/* 315 */     if (this.mDepthBufferObj >= 0) {
/* 316 */       names[0] = this.mDepthBufferObj;
/* 317 */       GLES20.glDeleteRenderbuffers(1, names, 0);
/* 318 */       this.mDepthBufferObj = 0;
/*     */     } 
/*     */     
/* 321 */     if (this.mFBOTextureName >= 0) {
/* 322 */       names[0] = this.mFBOTextureName;
/* 323 */       GLES20.glDeleteTextures(1, names, 0);
/* 324 */       this.mFBOTextureName = -1;
/*     */     } 
/*     */     
/* 327 */     if (this.mFrameBufferObj >= 0) {
/* 328 */       names[0] = this.mFrameBufferObj;
/* 329 */       GLES20.glDeleteFramebuffers(1, names, 0);
/* 330 */       this.mFrameBufferObj = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 339 */   public int getWidth() { return this.mWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public int getHeight() { return this.mHeight; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 355 */   public int getTexWidth() { return this.mTexWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 363 */   public int getTexHeight() { return this.mTexHeight; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\TextureOffscreen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */