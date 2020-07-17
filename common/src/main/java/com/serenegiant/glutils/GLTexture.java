/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.BitmapFactory;
/*     */ import android.graphics.Canvas;
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.GLUtils;
/*     */ import android.opengl.Matrix;
/*     */ import android.text.TextUtils;
/*     */ import java.io.IOException;
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
/*     */ public class GLTexture
/*     */   implements ITexture
/*     */ {
/*  38 */   int mTextureTarget = 3553;
/*  39 */   int mTextureUnit = 33984;
/*     */   int mTextureId;
/*  41 */   final float[] mTexMatrix = new float[16];
/*     */ 
/*     */   
/*     */   int mTexWidth;
/*     */ 
/*     */   
/*     */   int mTexHeight;
/*     */ 
/*     */   
/*     */   int mImageWidth;
/*     */   
/*     */   int mImageHeight;
/*     */ 
/*     */   
/*     */   public GLTexture(int width, int height, int filter_param) {
/*  56 */     int w = 32;
/*  57 */     for (; w < width; w <<= 1);
/*  58 */     int h = 32;
/*  59 */     for (; h < height; h <<= 1);
/*  60 */     if (this.mTexWidth != w || this.mTexHeight != h) {
/*  61 */       this.mTexWidth = w;
/*  62 */       this.mTexHeight = h;
/*     */     } 
/*     */     
/*  65 */     this.mTextureId = GLHelper.initTex(this.mTextureTarget, filter_param);
/*     */     
/*  67 */     GLES20.glTexImage2D(this.mTextureTarget, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/*  77 */     this.mTexMatrix[0] = width / this.mTexWidth;
/*  78 */     this.mTexMatrix[5] = height / this.mTexHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  84 */     release();
/*  85 */     super.finalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  95 */     if (this.mTextureId > 0) {
/*  96 */       GLHelper.deleteTex(this.mTextureId);
/*  97 */       this.mTextureId = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {
/* 107 */     GLES20.glActiveTexture(this.mTextureUnit);
/* 108 */     GLES20.glBindTexture(this.mTextureTarget, this.mTextureId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public void unbind() { GLES20.glBindTexture(this.mTextureTarget, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public int getTexTarget() { return this.mTextureTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public int getTexture() { return this.mTextureId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public float[] getTexMatrix() { return this.mTexMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public void getTexMatrix(float[] matrix, int offset) { System.arraycopy(this.mTexMatrix, 0, matrix, offset, this.mTexMatrix.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public int getTexWidth() { return this.mTexWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public int getTexHeight() { return this.mTexHeight; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadTexture(String filePath) throws NullPointerException, IOException {
/* 168 */     if (TextUtils.isEmpty(filePath))
/* 169 */       throw new NullPointerException("image file path should not be a null"); 
/* 170 */     BitmapFactory.Options options = new BitmapFactory.Options();
/* 171 */     options.inJustDecodeBounds = true;
/* 172 */     BitmapFactory.decodeFile(filePath, options);
/*     */     
/* 174 */     int imageWidth = options.outWidth;
/* 175 */     int imageHeight = options.outHeight;
/* 176 */     int inSampleSize = 1;
/* 177 */     if (imageHeight > this.mTexHeight || imageWidth > this.mTexWidth) {
/* 178 */       if (imageWidth > imageHeight) {
/* 179 */         inSampleSize = (int)Math.ceil((imageHeight / this.mTexHeight));
/*     */       } else {
/* 181 */         inSampleSize = (int)Math.ceil((imageWidth / this.mTexWidth));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 186 */     options.inSampleSize = inSampleSize;
/* 187 */     options.inJustDecodeBounds = false;
/* 188 */     Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
/* 189 */     this.mImageWidth = bitmap.getWidth();
/* 190 */     this.mImageHeight = bitmap.getHeight();
/* 191 */     Bitmap texture = Bitmap.createBitmap(this.mTexWidth, this.mTexHeight, Bitmap.Config.ARGB_8888);
/* 192 */     Canvas canvas = new Canvas(texture);
/* 193 */     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
/* 194 */     bitmap.recycle();
/* 195 */     bitmap = null;
/*     */     
/* 197 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/* 198 */     this.mTexMatrix[0] = this.mImageWidth / this.mTexWidth;
/* 199 */     this.mTexMatrix[5] = this.mImageHeight / this.mTexHeight;
/*     */     
/* 201 */     bind();
/* 202 */     GLUtils.texImage2D(this.mTextureTarget, 0, texture, 0);
/* 203 */     unbind();
/* 204 */     texture.recycle();
/* 205 */     texture = null;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\GLTexture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */