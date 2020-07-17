/*     */ package com.serenegiant.mediaeffect;
/*     */ 
/*     */ import android.graphics.Matrix;
/*     */ import android.opengl.GLES20;
/*     */ import com.serenegiant.glutils.GLHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaEffectTexProjection
/*     */   extends MediaEffectGLESBase
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEffectTexProjection";
/*     */   public static final String PROJ_VERTEX_SHADER = "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nuniform mat3 uTexMatrix2;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\ngl_Position = uMVPMatrix * aPosition;\nvec3 tex_coord = vec3((uTexMatrix * aTextureCoord).xy, 1.0);\nvec3 temp = uTexMatrix2 * tex_coord;\nvTextureCoord = temp.xy / temp.z;\n}\n";
/*     */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
/*     */   
/*     */   private static class MediaEffectTexProjectionDrawer
/*     */     extends MediaEffectDrawer
/*     */   {
/*  37 */     private float[] texMatrix2 = new float[9];
/*     */     private final int muTexMatrixLoc2;
/*     */     
/*     */     public MediaEffectTexProjectionDrawer(String vss, String fss) {
/*  41 */       super(false, vss, fss);
/*  42 */       this.muTexMatrixLoc2 = GLES20.glGetUniformLocation(getProgram(), "uTexMatrix2");
/*  43 */       GLHelper.checkLocation(this.muTexMatrixLoc2, "uTexMatrix2");
/*  44 */       reset();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void preDraw(int tex_id, float[] tex_matrix, int offset) {
/*  50 */       if (this.muTexMatrixLoc2 >= 0) {
/*  51 */         GLES20.glUniformMatrix3fv(this.muTexMatrixLoc2, 1, false, this.texMatrix2, 0);
/*  52 */         GLHelper.checkGlError("glUniformMatrix3fv");
/*     */       } 
/*  54 */       super.preDraw(tex_id, tex_matrix, offset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  59 */     public void reset() { setTexProjection(new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTexProjection(float[] matrix) {
/*  72 */       synchronized (this.mSync) {
/*     */       
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 156 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nvoid main() {\ngl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */   
/*     */   public MediaEffectTexProjection() {
/* 159 */     super(new MediaEffectTexProjectionDrawer("#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nuniform mat3 uTexMatrix2;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\ngl_Position = uMVPMatrix * aPosition;\nvec3 tex_coord = vec3((uTexMatrix * aTextureCoord).xy, 1.0);\nvec3 temp = uTexMatrix2 * tex_coord;\nvTextureCoord = temp.xy / temp.z;\n}\n", FRAGMENT_SHADER));
/*     */ 
/*     */ 
/*     */     
/* 163 */     this.mat = new Matrix();
/* 164 */     this.m = new float[9];
/*     */   }
/*     */ 
/*     */   
/*     */   private final Matrix mat;
/*     */   private final float[] m;
/*     */   
/*     */   public void calcPerspectiveTransform(float[] src, float[] dst) {
/* 172 */     this.mat.reset();
/* 173 */     this.mat.setPolyToPoly(src, 0, dst, 0, 4);
/* 174 */     this.mat.getValues(this.m);
/* 175 */     ((MediaEffectTexProjectionDrawer)this.mDrawer).setTexProjection(this.m);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectTexProjection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */