/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.Matrix;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GLDrawer2D
/*     */   implements IDrawer2dES2
/*     */ {
/*  37 */   private static final float[] VERTICES = new float[] { 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F };
/*  38 */   private static final float[] TEXCOORD = new float[] { 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
/*     */   
/*     */   private static final int FLOAT_SZ = 4;
/*     */   private final int VERTEX_NUM;
/*     */   private final int VERTEX_SZ;
/*     */   private final FloatBuffer pVertex;
/*     */   private final FloatBuffer pTexCoord;
/*     */   private final int mTexTarget;
/*     */   private int hProgram;
/*     */   int maPositionLoc;
/*     */   int maTextureCoordLoc;
/*     */   int muMVPMatrixLoc;
/*     */   int muTexMatrixLoc;
/*  51 */   private final float[] mMvpMatrix = new float[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public GLDrawer2D(boolean isOES) { this(VERTICES, TEXCOORD, isOES); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GLDrawer2D(float[] vertices, float[] texcoord, boolean isOES) {
/*  70 */     this.VERTEX_NUM = Math.min((vertices != null) ? vertices.length : 0, (texcoord != null) ? texcoord.length : 0) / 2;
/*  71 */     this.VERTEX_SZ = this.VERTEX_NUM * 2;
/*     */     
/*  73 */     this.mTexTarget = isOES ? 36197 : 3553;
/*  74 */     this
/*  75 */       .pVertex = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*  76 */     this.pVertex.put(vertices);
/*  77 */     this.pVertex.flip();
/*  78 */     this
/*  79 */       .pTexCoord = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*  80 */     this.pTexCoord.put(texcoord);
/*  81 */     this.pTexCoord.flip();
/*     */     
/*  83 */     if (isOES) {
/*  84 */       this.hProgram = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}");
/*     */     } else {
/*  86 */       this.hProgram = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\nprecision mediump float;\nuniform sampler2D sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}");
/*     */     } 
/*     */     
/*  89 */     Matrix.setIdentityM(this.mMvpMatrix, 0);
/*  90 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  98 */     if (this.hProgram >= 0) {
/*  99 */       GLES20.glDeleteProgram(this.hProgram);
/*     */     }
/* 101 */     this.hProgram = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public boolean isOES() { return (this.mTexTarget == 36197); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public float[] getMvpMatrix() { return this.mMvpMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IDrawer2D setMvpMatrix(float[] matrix, int offset) {
/* 129 */     System.arraycopy(matrix, offset, this.mMvpMatrix, 0, 16);
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
/* 140 */   public void getMvpMatrix(float[] matrix, int offset) { System.arraycopy(this.mMvpMatrix, 0, matrix, offset, 16); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void draw(int texId, float[] tex_matrix, int offset) {
/* 152 */     if (this.hProgram < 0)
/* 153 */       return;  GLES20.glUseProgram(this.hProgram);
/* 154 */     if (tex_matrix != null)
/*     */     {
/* 156 */       GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, tex_matrix, offset);
/*     */     }
/*     */     
/* 159 */     GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
/* 160 */     GLES20.glActiveTexture(33984);
/* 161 */     GLES20.glBindTexture(this.mTexTarget, texId);
/* 162 */     GLES20.glDrawArrays(5, 0, this.VERTEX_NUM);
/* 163 */     GLES20.glBindTexture(this.mTexTarget, 0);
/* 164 */     GLES20.glUseProgram(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public void draw(ITexture texture) { draw(texture.getTexture(), texture.getTexMatrix(), 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public void draw(TextureOffscreen offscreen) { draw(offscreen.getTexture(), offscreen.getTexMatrix(), 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public int initTex() { return GLHelper.initTex(this.mTexTarget, 9728); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public void deleteTex(int hTex) { GLHelper.deleteTex(hTex); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateShader(String vs, String fs) {
/* 212 */     release();
/* 213 */     this.hProgram = GLHelper.loadShader(vs, fs);
/* 214 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   public void updateShader(String fs) { updateShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fs); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetShader() {
/* 231 */     release();
/* 232 */     if (isOES()) {
/* 233 */       this.hProgram = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}");
/*     */     } else {
/* 235 */       this.hProgram = GLHelper.loadShader("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#version 100\nprecision mediump float;\nuniform sampler2D sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}");
/*     */     } 
/* 237 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int glGetAttribLocation(String name) {
/* 248 */     GLES20.glUseProgram(this.hProgram);
/* 249 */     return GLES20.glGetAttribLocation(this.hProgram, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int glGetUniformLocation(String name) {
/* 260 */     GLES20.glUseProgram(this.hProgram);
/* 261 */     return GLES20.glGetUniformLocation(this.hProgram, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   public void glUseProgram() { GLES20.glUseProgram(this.hProgram); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 277 */     GLES20.glUseProgram(this.hProgram);
/* 278 */     this.maPositionLoc = GLES20.glGetAttribLocation(this.hProgram, "aPosition");
/* 279 */     this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.hProgram, "aTextureCoord");
/* 280 */     this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uMVPMatrix");
/* 281 */     this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uTexMatrix");
/*     */     
/* 283 */     GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
/* 284 */     GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, this.mMvpMatrix, 0);
/* 285 */     GLES20.glVertexAttribPointer(this.maPositionLoc, 2, 5126, false, this.VERTEX_SZ, this.pVertex);
/* 286 */     GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, this.VERTEX_SZ, this.pTexCoord);
/* 287 */     GLES20.glEnableVertexAttribArray(this.maPositionLoc);
/* 288 */     GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\GLDrawer2D.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */