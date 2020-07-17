/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.Context;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.GLES30;
/*     */ import android.opengl.GLUtils;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.AssetsHelper;
/*     */ import com.serenegiant.utils.BuildCheck;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GLHelper
/*     */ {
/*     */   private static final String TAG = "GLHelper";
/*     */   
/*     */   public static void checkGlError(String op) {
/*  49 */     int error = GLES20.glGetError();
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
/*     */ 
/*     */   
/*  67 */   public static int initTex(int texTarget, int filter_param) { return initTex(texTarget, 33984, filter_param, filter_param, 33071); }
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
/*  81 */     int[] tex = new int[1];
/*  82 */     GLES20.glActiveTexture(texUnit);
/*  83 */     GLES20.glGenTextures(1, tex, 0);
/*  84 */     GLES20.glBindTexture(texTarget, tex[0]);
/*  85 */     GLES20.glTexParameteri(texTarget, 10242, wrap);
/*  86 */     GLES20.glTexParameteri(texTarget, 10243, wrap);
/*  87 */     GLES20.glTexParameteri(texTarget, 10241, min_filter);
/*  88 */     GLES20.glTexParameteri(texTarget, 10240, mag_filter);
/*  89 */     return tex[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteTex(int hTex) {
/*  97 */     int[] tex = { hTex };
/*  98 */     GLES20.glDeleteTextures(1, tex, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int loadTextureFromResource(Context context, int resId) {
/* 103 */     Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
/*     */     
/* 105 */     Canvas canvas = new Canvas(bitmap);
/* 106 */     canvas.drawARGB(0, 0, 255, 0);
/*     */ 
/*     */ 
/*     */     
/* 110 */     Drawable background = context.getResources().getDrawable(resId);
/* 111 */     background.setBounds(0, 0, 256, 256);
/* 112 */     background.draw(canvas);
/*     */     
/* 114 */     int[] textures = new int[1];
/*     */ 
/*     */     
/* 117 */     GLES20.glGenTextures(1, textures, 0);
/*     */     
/* 119 */     GLES20.glBindTexture(3553, textures[0]);
/*     */ 
/*     */     
/* 122 */     GLES20.glTexParameterf(3553, 10241, 9728.0F);
/* 123 */     GLES20.glTexParameterf(3553, 10240, 9729.0F);
/*     */ 
/*     */     
/* 126 */     GLES20.glTexParameterf(3553, 10242, 10497.0F);
/* 127 */     GLES20.glTexParameterf(3553, 10243, 10497.0F);
/*     */ 
/*     */     
/* 130 */     GLUtils.texImage2D(3553, 0, bitmap, 0);
/*     */     
/* 132 */     bitmap.recycle();
/*     */     
/* 134 */     return textures[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int createTextureWithTextContent(String text) {
/* 139 */     Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
/*     */     
/* 141 */     Canvas canvas = new Canvas(bitmap);
/* 142 */     canvas.drawARGB(0, 0, 255, 0);
/*     */ 
/*     */     
/* 145 */     Paint textPaint = new Paint();
/* 146 */     textPaint.setTextSize(32.0F);
/* 147 */     textPaint.setAntiAlias(true);
/* 148 */     textPaint.setARGB(255, 255, 255, 255);
/*     */     
/* 150 */     canvas.drawText(text, 16.0F, 112.0F, textPaint);
/*     */     
/* 152 */     int texture = initTex(3553, 33984, 9728, 9729, 10497);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     GLUtils.texImage2D(3553, 0, bitmap, 0);
/*     */     
/* 161 */     bitmap.recycle();
/*     */     
/* 163 */     return texture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int loadShader(Context context, String vss_asset, String fss_asset) {
/* 174 */     int program = 0;
/*     */     try {
/* 176 */       String vss = AssetsHelper.loadString(context.getAssets(), vss_asset);
/* 177 */       String fss = AssetsHelper.loadString(context.getAssets(), vss_asset);
/* 178 */       program = loadShader(vss, fss);
/* 179 */     } catch (IOException iOException) {}
/*     */     
/* 181 */     return program;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int loadShader(String vss, String fss) {
/* 192 */     int[] compiled = new int[1];
/*     */     
/* 194 */     int vs = loadShader(35633, vss);
/* 195 */     if (vs == 0) {
/* 196 */       return 0;
/*     */     }
/*     */     
/* 199 */     int fs = loadShader(35632, fss);
/* 200 */     if (fs == 0) {
/* 201 */       return 0;
/*     */     }
/*     */     
/* 204 */     int program = GLES20.glCreateProgram();
/* 205 */     checkGlError("glCreateProgram");
/* 206 */     if (program == 0) {
/* 207 */       Log.e("GLHelper", "Could not create program");
/*     */     }
/* 209 */     GLES20.glAttachShader(program, vs);
/* 210 */     checkGlError("glAttachShader");
/* 211 */     GLES20.glAttachShader(program, fs);
/* 212 */     checkGlError("glAttachShader");
/* 213 */     GLES20.glLinkProgram(program);
/* 214 */     int[] linkStatus = new int[1];
/* 215 */     GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
/* 216 */     if (linkStatus[0] != 1) {
/* 217 */       Log.e("GLHelper", "Could not link program: ");
/* 218 */       Log.e("GLHelper", GLES20.glGetProgramInfoLog(program));
/* 219 */       GLES20.glDeleteProgram(program);
/* 220 */       return 0;
/*     */     } 
/* 222 */     return program;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int loadShader(int shaderType, String source) {
/* 231 */     int shader = GLES20.glCreateShader(shaderType);
/* 232 */     checkGlError("glCreateShader type=" + shaderType);
/* 233 */     GLES20.glShaderSource(shader, source);
/* 234 */     GLES20.glCompileShader(shader);
/* 235 */     int[] compiled = new int[1];
/* 236 */     GLES20.glGetShaderiv(shader, 35713, compiled, 0);
/* 237 */     if (compiled[0] == 0) {
/* 238 */       Log.e("GLHelper", "Could not compile shader " + shaderType + ":");
/* 239 */       Log.e("GLHelper", " " + GLES20.glGetShaderInfoLog(shader));
/* 240 */       GLES20.glDeleteShader(shader);
/* 241 */       shader = 0;
/*     */     } 
/* 243 */     return shader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkLocation(int location, String label) {
/* 253 */     if (location < 0) {
/* 254 */       throw new RuntimeException("Unable to locate '" + label + "' in program");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"InlinedApi"})
/*     */   public static void logVersionInfo() {
/* 263 */     Log.i("GLHelper", "vendor  : " + GLES20.glGetString(7936));
/* 264 */     Log.i("GLHelper", "renderer: " + GLES20.glGetString(7937));
/* 265 */     Log.i("GLHelper", "version : " + GLES20.glGetString(7938));
/*     */     
/* 267 */     if (BuildCheck.isAndroid4_3()) {
/* 268 */       int[] values = new int[1];
/* 269 */       GLES30.glGetIntegerv(33307, values, 0);
/* 270 */       int majorVersion = values[0];
/* 271 */       GLES30.glGetIntegerv(33308, values, 0);
/* 272 */       int minorVersion = values[0];
/* 273 */       if (GLES30.glGetError() == 0)
/* 274 */         Log.i("GLHelper", "version: " + majorVersion + "." + minorVersion); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\GLHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */