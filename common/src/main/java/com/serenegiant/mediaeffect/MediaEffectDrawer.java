/*     */ package com.serenegiant.mediaeffect;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.Matrix;
/*     */ import com.serenegiant.glutils.GLHelper;
/*     */ import com.serenegiant.glutils.TextureOffscreen;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaEffectDrawer
/*     */ {
/*     */   protected TextureOffscreen mOutputOffscreen;
/*     */   protected boolean mEnabled = true;
/*     */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
/*  66 */   protected static final String FRAGMENT_SHADER_2D = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/*  68 */   protected static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */   
/*  70 */   private static final float[] VERTICES = new float[] { 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F };
/*  71 */   private static final float[] TEXCOORD = new float[] { 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
/*     */   
/*     */   private static final int FLOAT_SZ = 4;
/*     */   
/*     */   private static final int VERTEX_NUM = 4;
/*     */   private static final int VERTEX_SZ = 8;
/*  77 */   protected final Object mSync = new Object();
/*     */   private final int mTexTarget;
/*     */   private final int muMVPMatrixLoc;
/*     */   private final int muTexMatrixLoc;
/*  81 */   private final float[] mMvpMatrix = new float[16];
/*     */   
/*     */   private int hProgram;
/*     */   
/*  85 */   public MediaEffectDrawer() { this(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", FRAGMENT_SHADER_2D); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public MediaEffectDrawer(String fss) { this(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public MediaEffectDrawer(boolean isOES, String fss) { this(isOES, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*     */ 
/*     */   
/*     */   public MediaEffectDrawer(boolean isOES, String vss, String fss) {
/*  97 */     this.mTexTarget = isOES ? 36197 : 3553;
/*     */     
/*  99 */     FloatBuffer pVertex = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
/* 100 */     pVertex.put(VERTICES);
/* 101 */     pVertex.flip();
/*     */     
/* 103 */     FloatBuffer pTexCoord = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
/* 104 */     pTexCoord.put(TEXCOORD);
/* 105 */     pTexCoord.flip();
/*     */     
/* 107 */     this.hProgram = GLHelper.loadShader(vss, fss);
/* 108 */     GLES20.glUseProgram(this.hProgram);
/* 109 */     int maPositionLoc = GLES20.glGetAttribLocation(this.hProgram, "aPosition");
/* 110 */     int maTextureCoordLoc = GLES20.glGetAttribLocation(this.hProgram, "aTextureCoord");
/* 111 */     this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uMVPMatrix");
/* 112 */     this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uTexMatrix");
/*     */     
/* 114 */     Matrix.setIdentityM(this.mMvpMatrix, 0);
/*     */     
/* 116 */     if (this.muMVPMatrixLoc >= 0) {
/* 117 */       GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
/*     */     }
/* 119 */     if (this.muTexMatrixLoc >= 0)
/*     */     {
/* 121 */       GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, this.mMvpMatrix, 0);
/*     */     }
/*     */     
/* 124 */     GLES20.glVertexAttribPointer(maPositionLoc, 2, 5126, false, 8, pVertex);
/* 125 */     GLES20.glEnableVertexAttribArray(maPositionLoc);
/*     */     
/* 127 */     GLES20.glVertexAttribPointer(maTextureCoordLoc, 2, 5126, false, 8, pTexCoord);
/* 128 */     GLES20.glEnableVertexAttribArray(maTextureCoordLoc);
/*     */   }
/*     */   
/*     */   public void release() {
/* 132 */     GLES20.glUseProgram(0);
/* 133 */     if (this.hProgram >= 0) {
/* 134 */       GLES20.glDeleteProgram(this.hProgram);
/*     */     }
/* 136 */     this.hProgram = -1;
/*     */   }
/*     */ 
/*     */   
/* 140 */   protected int getProgram() { return this.hProgram; }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public float[] getMvpMatrix() { return this.mMvpMatrix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTexSize(int width, int height) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMvpMatrix(float[] matrix, int offset) {
/* 162 */     synchronized (this.mSync) {
/* 163 */       System.arraycopy(matrix, offset, this.mMvpMatrix, 0, this.mMvpMatrix.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public void getMvpMatrix(float[] matrix, int offset) { System.arraycopy(this.mMvpMatrix, 0, matrix, offset, this.mMvpMatrix.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void apply(int tex_id, float[] tex_matrix, int offset) {
/* 183 */     synchronized (this.mSync) {
/* 184 */       GLES20.glUseProgram(this.hProgram);
/* 185 */       preDraw(tex_id, tex_matrix, offset);
/* 186 */       draw(tex_id, tex_matrix, offset);
/* 187 */       postDraw();
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
/*     */   protected void preDraw(int tex_id, float[] tex_matrix, int offset) {
/* 200 */     if (this.muTexMatrixLoc >= 0 && tex_matrix != null) {
/* 201 */       GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, tex_matrix, offset);
/*     */     }
/* 203 */     if (this.muMVPMatrixLoc >= 0) {
/* 204 */       GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
/*     */     }
/* 206 */     GLES20.glActiveTexture(33984);
/* 207 */     GLES20.glBindTexture(this.mTexTarget, tex_id);
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
/* 220 */   protected void draw(int tex_id, float[] tex_matrix, int offset) { GLES20.glDrawArrays(5, 0, 4); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postDraw() {
/* 228 */     GLES20.glBindTexture(this.mTexTarget, 0);
/* 229 */     GLES20.glUseProgram(0);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectDrawer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */