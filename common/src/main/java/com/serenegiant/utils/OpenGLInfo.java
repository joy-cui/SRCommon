/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import com.serenegiant.glutils.EGLBase;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Arrays;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
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
/*     */ public class OpenGLInfo
/*     */ {
/*     */   private static final int EGL_CLIENT_APIS = 12429;
/*     */   
/*     */   public static JSONObject get() throws JSONException {
/*  37 */     JSONObject result = new JSONObject();
/*     */     try {
/*  39 */       EGLBase egl = EGLBase.createFrom(3, null, false, 0, false);
/*  40 */       EGLBase.IEglSurface dummy = egl.createOffscreen(1, 1);
/*  41 */       dummy.makeCurrent();
/*     */       try {
/*  43 */         IntBuffer val = IntBuffer.allocate(2);
/*  44 */         JSONObject glinfo = new JSONObject();
/*     */         try {
/*  46 */           glinfo.put("GL_VENDOR", GLES20.glGetString(7936));
/*  47 */         } catch (Exception e) {
/*  48 */           glinfo.put("GL_VENDOR", e.getMessage());
/*     */         } 
/*     */         try {
/*  51 */           glinfo.put("GL_VERSION", GLES20.glGetString(7938));
/*  52 */         } catch (Exception e) {
/*  53 */           glinfo.put("GL_VERSION", e.getMessage());
/*     */         } 
/*     */         try {
/*  56 */           glinfo.put("GL_RENDERER", GLES20.glGetString(7937));
/*  57 */         } catch (Exception e) {
/*  58 */           glinfo.put("GL_RENDERER", e.getMessage());
/*     */         } 
/*     */         try {
/*  61 */           GLES20.glGetIntegerv(34921, val);
/*  62 */           glinfo.put("GL_MAX_VERTEX_ATTRIBS", val.get(0));
/*  63 */         } catch (Exception e) {
/*  64 */           glinfo.put("GL_MAX_VERTEX_ATTRIBS", e.getMessage());
/*     */         } 
/*     */         try {
/*  67 */           GLES20.glGetIntegerv(36347, val);
/*  68 */           glinfo.put("GL_MAX_VERTEX_UNIFORM_VECTORS", val.get(0));
/*  69 */         } catch (Exception e) {
/*  70 */           glinfo.put("GL_MAX_VERTEX_UNIFORM_VECTORS", e.getMessage());
/*     */         } 
/*     */         try {
/*  73 */           GLES20.glGetIntegerv(36348, val);
/*  74 */           glinfo.put("GL_MAX_VARYING_VECTORS", val.get(0));
/*  75 */         } catch (Exception e) {
/*  76 */           glinfo.put("GL_MAX_VARYING_VECTORS", e.getMessage());
/*     */         } 
/*     */         try {
/*  79 */           GLES20.glGetIntegerv(35661, val);
/*  80 */           glinfo.put("GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS", val.get(0));
/*  81 */         } catch (Exception e) {
/*  82 */           glinfo.put("GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS", e.getMessage());
/*     */         } 
/*     */         try {
/*  85 */           GLES20.glGetIntegerv(35660, val);
/*  86 */           glinfo.put("GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS", val.get(0));
/*  87 */         } catch (Exception e) {
/*  88 */           glinfo.put("GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS", e.getMessage());
/*     */         } 
/*     */         try {
/*  91 */           GLES20.glGetIntegerv(34930, val);
/*  92 */           glinfo.put("GL_MAX_TEXTURE_IMAGE_UNITS", val.get(0));
/*  93 */         } catch (Exception e) {
/*  94 */           glinfo.put("GL_MAX_TEXTURE_IMAGE_UNITS", e.getMessage());
/*     */         } 
/*     */         try {
/*  97 */           GLES20.glGetIntegerv(36349, val);
/*  98 */           glinfo.put("GL_MAX_FRAGMENT_UNIFORM_VECTORS", val.get(0));
/*  99 */         } catch (Exception e) {
/* 100 */           glinfo.put("GL_MAX_FRAGMENT_UNIFORM_VECTORS", e.getMessage());
/*     */         } 
/*     */         try {
/* 103 */           GLES20.glGetIntegerv(34076, val);
/* 104 */           glinfo.put("GL_MAX_CUBE_MAP_TEXTURE_SIZE", val.get(0));
/* 105 */         } catch (Exception e) {
/* 106 */           glinfo.put("GL_MAX_CUBE_MAP_TEXTURE_SIZE", e.getMessage());
/*     */         } 
/*     */         try {
/* 109 */           GLES20.glGetIntegerv(34024, val);
/* 110 */           glinfo.put("GL_MAX_RENDERBUFFER_SIZE", val.get(0));
/* 111 */         } catch (Exception e) {
/* 112 */           glinfo.put("GL_MAX_RENDERBUFFER_SIZE", e.getMessage());
/*     */         } 
/*     */         try {
/* 115 */           GLES20.glGetIntegerv(3379, val);
/* 116 */           glinfo.put("GL_MAX_TEXTURE_SIZE", val.get(0));
/* 117 */         } catch (Exception e) {
/* 118 */           glinfo.put("GL_MAX_TEXTURE_SIZE", e.getMessage());
/*     */         } 
/*     */         try {
/* 121 */           GLES20.glGetIntegerv(3386, val);
/* 122 */           glinfo.put("GL_MAX_VIEWPORT_DIMS", String.format("%d x %d", new Object[] { Integer.valueOf(val.get(0)), Integer.valueOf(val.get(1)) }));
/* 123 */         } catch (Exception e) {
/* 124 */           glinfo.put("GL_MAX_VIEWPORT_DIMS", e.getMessage());
/*     */         } 
/*     */         try {
/* 127 */           glinfo.put("GL_EXTENSIONS", formatExtensions(GLES20.glGetString(7939)));
/* 128 */         } catch (Exception e) {
/* 129 */           glinfo.put("GL_EXTENSIONS", e.getMessage());
/*     */         } 
/* 131 */         result.put("GL_INFO", glinfo);
/* 132 */         JSONObject eglinfo = new JSONObject();
/*     */         try {
/* 134 */           eglinfo.put("EGL_VENDOR", egl.queryString(12371));
/* 135 */         } catch (Exception e) {
/* 136 */           glinfo.put("EGL_VENDOR", e.getMessage());
/*     */         } 
/*     */         try {
/* 139 */           eglinfo.put("EGL_VERSION", egl.queryString(12372));
/* 140 */         } catch (Exception e) {
/* 141 */           glinfo.put("EGL_VERSION", e.getMessage());
/*     */         } 
/*     */         try {
/* 144 */           eglinfo.put("EGL_CLIENT_APIS", egl.queryString(12429));
/* 145 */         } catch (Exception e) {
/* 146 */           glinfo.put("EGL_CLIENT_APIS", e.getMessage());
/*     */         } 
/*     */         try {
/* 149 */           eglinfo.put("EGL_EXTENSIONS:", formatExtensions(egl.queryString(12373)));
/* 150 */         } catch (Exception e) {
/* 151 */           glinfo.put("EGL_EXTENSIONS", e.getMessage());
/*     */         } 
/* 153 */         result.put("EGL_INFO", eglinfo);
/*     */       } finally {
/* 155 */         dummy.release();
/* 156 */         egl.release();
/*     */       } 
/* 158 */     } catch (Exception e) {
/* 159 */       result.put("EXCEPTION", e.getMessage());
/*     */     } 
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final JSONObject formatExtensions(String ext) throws JSONException {
/* 170 */     JSONObject result = new JSONObject();
/* 171 */     String[] values = ext.split(" ");
/* 172 */     Arrays.sort((Object[])values);
/* 173 */     for (int i = 0; i < values.length; i++) {
/* 174 */       result.put(Integer.toString(i), values[i]);
/*     */     }
/* 176 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\OpenGLInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */