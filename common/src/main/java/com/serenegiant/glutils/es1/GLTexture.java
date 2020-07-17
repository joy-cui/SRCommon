/*     */ package com.serenegiant.glutils.es1;
/*     */ 
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.BitmapFactory;
/*     */ import android.graphics.Canvas;
/*     */ import android.opengl.GLES10;
/*     */ import android.opengl.GLUtils;
/*     */ import android.opengl.Matrix;
/*     */ import android.text.TextUtils;
/*     */ import com.serenegiant.glutils.ITexture;
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
/*     */ 
/*     */ public class GLTexture
/*     */   implements ITexture
/*     */ {
/*  40 */   int mTextureTarget = 3553;
/*  41 */   int mTextureUnit = 33984;
/*     */   int mTextureId;
/*  43 */   final float[] mTexMatrix = new float[16];
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
/*  58 */     int w = 32;
/*  59 */     for (; w < width; w <<= 1);
/*  60 */     int h = 32;
/*  61 */     for (; h < height; h <<= 1);
/*  62 */     if (this.mTexWidth != w || this.mTexHeight != h) {
/*  63 */       this.mTexWidth = w;
/*  64 */       this.mTexHeight = h;
/*     */     } 
/*     */     
/*  67 */     this.mTextureId = GLHelper.initTex(this.mTextureTarget, filter_param);
/*     */     
/*  69 */     GLES10.glTexImage2D(this.mTextureTarget, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/*  79 */     this.mTexMatrix[0] = width / this.mTexWidth;
/*  80 */     this.mTexMatrix[5] = height / this.mTexHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  86 */     release();
/*  87 */     super.finalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  97 */     if (this.mTextureId > 0) {
/*  98 */       GLHelper.deleteTex(this.mTextureId);
/*  99 */       this.mTextureId = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {
/* 109 */     GLES10.glActiveTexture(this.mTextureUnit);
/* 110 */     GLES10.glBindTexture(this.mTextureTarget, this.mTextureId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void unbind() { GLES10.glBindTexture(this.mTextureTarget, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public int getTexTarget() { return this.mTextureTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public int getTexture() { return this.mTextureId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public float[] getTexMatrix() { return this.mTexMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public void getTexMatrix(float[] matrix, int offset) { System.arraycopy(this.mTexMatrix, 0, matrix, offset, this.mTexMatrix.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public int getTexWidth() { return this.mTexWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   public int getTexHeight() { return this.mTexHeight; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadTexture(String filePath) throws NullPointerException, IOException {
/* 170 */     if (TextUtils.isEmpty(filePath))
/* 171 */       throw new NullPointerException("image file path should not be a null"); 
/* 172 */     BitmapFactory.Options options = new BitmapFactory.Options();
/* 173 */     options.inJustDecodeBounds = true;
/* 174 */     BitmapFactory.decodeFile(filePath, options);
/*     */     
/* 176 */     int imageWidth = options.outWidth;
/* 177 */     int imageHeight = options.outHeight;
/* 178 */     int inSampleSize = 1;
/* 179 */     if (imageHeight > this.mTexHeight || imageWidth > this.mTexWidth) {
/* 180 */       if (imageWidth > imageHeight) {
/* 181 */         inSampleSize = (int)Math.ceil((imageHeight / this.mTexHeight));
/*     */       } else {
/* 183 */         inSampleSize = (int)Math.ceil((imageWidth / this.mTexWidth));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 188 */     options.inSampleSize = inSampleSize;
/* 189 */     options.inJustDecodeBounds = false;
/* 190 */     Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
/* 191 */     this.mImageWidth = bitmap.getWidth();
/* 192 */     this.mImageHeight = bitmap.getHeight();
/* 193 */     Bitmap texture = Bitmap.createBitmap(this.mTexWidth, this.mTexHeight, Bitmap.Config.ARGB_8888);
/* 194 */     Canvas canvas = new Canvas(texture);
/* 195 */     canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
/* 196 */     bitmap.recycle();
/* 197 */     bitmap = null;
/*     */     
/* 199 */     Matrix.setIdentityM(this.mTexMatrix, 0);
/* 200 */     this.mTexMatrix[0] = this.mImageWidth / this.mTexWidth;
/* 201 */     this.mTexMatrix[5] = this.mImageHeight / this.mTexHeight;
/*     */     
/* 203 */     bind();
/* 204 */     GLUtils.texImage2D(this.mTextureTarget, 0, texture, 0);
/* 205 */     unbind();
/* 206 */     texture.recycle();
/* 207 */     texture = null;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\es1\GLTexture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */