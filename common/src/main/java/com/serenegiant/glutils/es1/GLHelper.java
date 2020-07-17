/*     */ package com.serenegiant.glutils.es1;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.Context;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.opengl.GLES10;
/*     */ import android.opengl.GLES30;
/*     */ import android.opengl.GLUtils;
/*     */ import android.opengl.Matrix;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import javax.microedition.khronos.opengles.GL10;
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
/*     */ public final class GLHelper
/*     */ {
/*     */   private static final String TAG = "GLHelper";
/*     */   
/*     */   public static void checkGlError(String op) {
/*  49 */     int error = GLES10.glGetError();
/*  50 */     if (error != 0) {
/*  51 */       String msg = op + ": glError 0x" + Integer.toHexString(error);
/*  52 */       Log.e("GLHelper", msg);
/*  53 */       (new Throwable(msg)).printStackTrace();
/*     */     } 
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
/*     */   public static void checkGlError(GL10 gl, String op) {
/*  66 */     int error = gl.glGetError();
/*  67 */     if (error != 0) {
/*  68 */       String msg = op + ": glError 0x" + Integer.toHexString(error);
/*  69 */       Log.e("GLHelper", msg);
/*  70 */       (new Throwable(msg)).printStackTrace();
/*     */     } 
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
/*  84 */   public static int initTex(int texTarget, int filter_param) { return initTex(texTarget, 33984, filter_param, filter_param, 33071); }
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
/*     */   public static int initTex(int texTarget, int texUnit, int min_filter, int mag_filter, int wrap) {
/*  98 */     int[] tex = new int[1];
/*  99 */     GLES10.glActiveTexture(texUnit);
/* 100 */     GLES10.glGenTextures(1, tex, 0);
/* 101 */     GLES10.glBindTexture(texTarget, tex[0]);
/* 102 */     GLES10.glTexParameterx(texTarget, 10242, wrap);
/* 103 */     GLES10.glTexParameterx(texTarget, 10243, wrap);
/* 104 */     GLES10.glTexParameterx(texTarget, 10241, min_filter);
/* 105 */     GLES10.glTexParameterx(texTarget, 10240, mag_filter);
/* 106 */     return tex[0];
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
/*     */   public static int initTex(GL10 gl, int texTarget, int filter_param) {
/* 118 */     int[] tex = new int[1];
/* 119 */     gl.glActiveTexture(33984);
/* 120 */     gl.glGenTextures(1, tex, 0);
/* 121 */     gl.glBindTexture(texTarget, tex[0]);
/* 122 */     gl.glTexParameterx(texTarget, 10242, 33071);
/* 123 */     gl.glTexParameterx(texTarget, 10243, 33071);
/* 124 */     gl.glTexParameterx(texTarget, 10241, filter_param);
/* 125 */     gl.glTexParameterx(texTarget, 10240, filter_param);
/* 126 */     return tex[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteTex(int hTex) {
/* 134 */     int[] tex = { hTex };
/* 135 */     GLES10.glDeleteTextures(1, tex, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteTex(GL10 gl, int hTex) {
/* 143 */     int[] tex = { hTex };
/* 144 */     gl.glDeleteTextures(1, tex, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int loadTextureFromResource(Context context, int resId) {
/* 149 */     Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
/*     */     
/* 151 */     Canvas canvas = new Canvas(bitmap);
/* 152 */     canvas.drawARGB(0, 0, 255, 0);
/*     */ 
/*     */ 
/*     */     
/* 156 */     Drawable background = context.getResources().getDrawable(resId);
/* 157 */     background.setBounds(0, 0, 256, 256);
/* 158 */     background.draw(canvas);
/*     */     
/* 160 */     int[] textures = new int[1];
/*     */ 
/*     */     
/* 163 */     GLES10.glGenTextures(1, textures, 0);
/*     */     
/* 165 */     GLES10.glBindTexture(3553, textures[0]);
/*     */ 
/*     */     
/* 168 */     GLES10.glTexParameterx(3553, 10241, 9728);
/* 169 */     GLES10.glTexParameterx(3553, 10240, 9729);
/*     */ 
/*     */     
/* 172 */     GLES10.glTexParameterx(3553, 10242, 10497);
/* 173 */     GLES10.glTexParameterx(3553, 10243, 10497);
/*     */ 
/*     */     
/* 176 */     GLUtils.texImage2D(3553, 0, bitmap, 0);
/*     */     
/* 178 */     bitmap.recycle();
/*     */     
/* 180 */     return textures[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int createTextureWithTextContent(String text) {
/* 185 */     Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
/*     */     
/* 187 */     Canvas canvas = new Canvas(bitmap);
/* 188 */     canvas.drawARGB(0, 0, 255, 0);
/*     */ 
/*     */     
/* 191 */     Paint textPaint = new Paint();
/* 192 */     textPaint.setTextSize(32.0F);
/* 193 */     textPaint.setAntiAlias(true);
/* 194 */     textPaint.setARGB(255, 255, 255, 255);
/*     */     
/* 196 */     canvas.drawText(text, 16.0F, 112.0F, textPaint);
/*     */     
/* 198 */     int texture = initTex(3553, 33984, 9728, 9729, 10497);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     GLUtils.texImage2D(3553, 0, bitmap, 0);
/*     */     
/* 207 */     bitmap.recycle();
/*     */     
/* 209 */     return texture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkLocation(int location, String label) {
/* 219 */     if (location < 0) {
/* 220 */       throw new RuntimeException("Unable to locate '" + label + "' in program");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"InlinedApi"})
/*     */   public static void logVersionInfo() {
/* 229 */     Log.i("GLHelper", "vendor  : " + GLES10.glGetString(7936));
/* 230 */     Log.i("GLHelper", "renderer: " + GLES10.glGetString(7937));
/* 231 */     Log.i("GLHelper", "version : " + GLES10.glGetString(7938));
/*     */     
/* 233 */     if (BuildCheck.isAndroid4_3()) {
/* 234 */       int[] values = new int[1];
/* 235 */       GLES30.glGetIntegerv(33307, values, 0);
/* 236 */       int majorVersion = values[0];
/* 237 */       GLES30.glGetIntegerv(33308, values, 0);
/* 238 */       int minorVersion = values[0];
/* 239 */       if (GLES30.glGetError() == 0) {
/* 240 */         Log.i("GLHelper", "version: " + majorVersion + "." + minorVersion);
/*     */       }
/*     */     } 
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
/*     */   public static String gluErrorString(int error) {
/* 254 */     switch (error) {
/*     */       case 0:
/* 256 */         return "no error";
/*     */       case 1280:
/* 258 */         return "invalid enum";
/*     */       case 1281:
/* 260 */         return "invalid value";
/*     */       case 1282:
/* 262 */         return "invalid operation";
/*     */       case 1283:
/* 264 */         return "stack overflow";
/*     */       case 1284:
/* 266 */         return "stack underflow";
/*     */       case 1285:
/* 268 */         return "out of memory";
/*     */     } 
/* 270 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void gluLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
/* 292 */     float[] scratch = sScratch;
/* 293 */     synchronized (scratch) {
/* 294 */       Matrix.setLookAtM(scratch, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
/*     */       
/* 296 */       GLES10.glMultMatrixf(scratch, 0);
/*     */     } 
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
/* 310 */   public static void gluOrtho2D(float left, float right, float bottom, float top) { GLES10.glOrthof(left, right, bottom, top, -1.0F, 1.0F); }
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
/*     */   public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
/* 329 */     float top = zNear * (float)Math.tan(fovy * 0.008726646259971648D);
/* 330 */     float bottom = -top;
/* 331 */     float left = bottom * aspect;
/* 332 */     float right = top * aspect;
/* 333 */     GLES10.glFrustumf(left, right, bottom, top, zNear, zFar);
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
/*     */   public static int gluProject(float objX, float objY, float objZ, float[] model, int modelOffset, float[] project, int projectOffset, int[] view, int viewOffset, float[] win, int winOffset) {
/* 367 */     float[] scratch = sScratch;
/* 368 */     synchronized (scratch) {
/* 369 */       int M_OFFSET = 0;
/* 370 */       int V_OFFSET = 16;
/* 371 */       int V2_OFFSET = 20;
/* 372 */       Matrix.multiplyMM(scratch, 0, project, projectOffset, model, modelOffset);
/*     */       
/* 374 */       scratch[16] = objX;
/* 375 */       scratch[17] = objY;
/* 376 */       scratch[18] = objZ;
/* 377 */       scratch[19] = 1.0F;
/*     */       
/* 379 */       Matrix.multiplyMV(scratch, 20, scratch, 0, scratch, 16);
/*     */       
/* 381 */       float w = scratch[23];
/* 382 */       if (w == 0.0F) {
/* 383 */         return 0;
/*     */       }
/*     */       
/* 386 */       float rw = 1.0F / w;
/*     */       
/* 388 */       win[winOffset] = view[viewOffset] + view[viewOffset + 2] * (scratch[20] * rw + 1.0F) * 0.5F;
/*     */ 
/*     */ 
/*     */       
/* 392 */       win[winOffset + 1] = view[viewOffset + 1] + view[viewOffset + 3] * (scratch[21] * rw + 1.0F) * 0.5F;
/*     */ 
/*     */       
/* 395 */       win[winOffset + 2] = (scratch[22] * rw + 1.0F) * 0.5F;
/*     */     } 
/*     */     
/* 398 */     return 1;
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
/*     */   public static int gluUnProject(float winX, float winY, float winZ, float[] model, int modelOffset, float[] project, int projectOffset, int[] view, int viewOffset, float[] obj, int objOffset) {
/* 432 */     float[] scratch = sScratch;
/* 433 */     synchronized (scratch) {
/* 434 */       int PM_OFFSET = 0;
/* 435 */       int INVPM_OFFSET = 16;
/* 436 */       int V_OFFSET = 0;
/* 437 */       Matrix.multiplyMM(scratch, 0, project, projectOffset, model, modelOffset);
/*     */       
/* 439 */       if (!Matrix.invertM(scratch, 16, scratch, 0)) {
/* 440 */         return 0;
/*     */       }
/*     */       
/* 443 */       scratch[0] = 2.0F * (winX - view[viewOffset + 0]) / view[viewOffset + 2] - 1.0F;
/*     */ 
/*     */       
/* 446 */       scratch[1] = 2.0F * (winY - view[viewOffset + 1]) / view[viewOffset + 3] - 1.0F;
/*     */ 
/*     */       
/* 449 */       scratch[2] = 2.0F * winZ - 1.0F;
/* 450 */       scratch[3] = 1.0F;
/*     */       
/* 452 */       Matrix.multiplyMV(obj, objOffset, scratch, 16, scratch, 0);
/*     */     } 
/*     */     
/* 455 */     return 1;
/*     */   }
/*     */   
/* 458 */   private static final float[] sScratch = new float[32];
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\es1\GLHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */