/*     */ package com.serenegiant.mediaeffect;
/*     */ 
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
/*     */ 
/*     */ public class MediaEffectKernel3x3Drawer
/*     */   extends MediaEffectColorAdjustDrawer
/*     */ {
/*     */   public static final int KERNEL_SIZE = 9;
/*  30 */   public static final float[] KERNEL_NULL = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F };
/*  31 */   public static final float[] KERNEL_SOBEL_H = new float[] { 1.0F, 0.0F, -1.0F, 2.0F, 0.0F, -2.0F, 1.0F, 0.0F, -1.0F };
/*  32 */   public static final float[] KERNEL_SOBEL_V = new float[] { 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, -2.0F, -1.0F };
/*  33 */   public static final float[] KERNEL_SOBEL2_H = new float[] { 3.0F, 0.0F, -3.0F, 10.0F, 0.0F, -10.0F, 3.0F, 0.0F, -3.0F };
/*  34 */   public static final float[] KERNEL_SOBEL2_V = new float[] { 3.0F, 10.0F, 3.0F, 0.0F, 0.0F, 0.0F, -3.0F, -10.0F, -3.0F };
/*  35 */   public static final float[] KERNEL_SHARPNESS = new float[] { 0.0F, -1.0F, 0.0F, -1.0F, 5.0F, -1.0F, 0.0F, -1.0F, 0.0F };
/*  36 */   public static final float[] KERNEL_EDGE_DETECT = new float[] { -1.0F, -1.0F, -1.0F, -1.0F, 8.0F, -1.0F, -1.0F, -1.0F, -1.0F };
/*  37 */   public static final float[] KERNEL_EMBOSS = new float[] { 2.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0F };
/*  38 */   public static final float[] KERNEL_SMOOTH = new float[] { 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F };
/*  39 */   public static final float[] KERNEL_GAUSSIAN = new float[] { 0.0625F, 0.125F, 0.0625F, 0.125F, 0.25F, 0.125F, 0.0625F, 0.125F, 0.0625F };
/*  40 */   public static final float[] KERNEL_BRIGHTEN = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 1.0F };
/*  41 */   public static final float[] KERNEL_LAPLACIAN = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, -8.0F, 1.0F, 1.0F, 1.0F, 1.0F };
/*     */   
/*     */   private final int muKernelLoc;
/*     */   private final int muTexOffsetLoc;
/*  45 */   private final float[] mKernel = new float[18];
/*  46 */   private final float[] mTexOffset = new float[18];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float mTexWidth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float mTexHeight;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_FILT3x3_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final String FRAGMENT_SHADER_FILT3x3 = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/*  75 */   private static final String FRAGMENT_SHADER_EXT_FILT3x3 = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */   
/*  78 */   public MediaEffectKernel3x3Drawer(String fss) { this(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public MediaEffectKernel3x3Drawer(boolean isOES, String fss) { this(isOES, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*     */ 
/*     */   
/*     */   public MediaEffectKernel3x3Drawer(boolean isOES, String vss, String fss) {
/*  86 */     super(isOES, vss, fss);
/*  87 */     this.muKernelLoc = GLES20.glGetUniformLocation(getProgram(), "uKernel");
/*  88 */     if (this.muKernelLoc < 0) {
/*     */       
/*  90 */       this.muTexOffsetLoc = -1;
/*     */     } else {
/*     */       
/*  93 */       this.muTexOffsetLoc = GLES20.glGetUniformLocation(getProgram(), "uTexOffset");
/*     */ 
/*     */       
/*  96 */       setKernel(KERNEL_NULL, 0.0F);
/*  97 */       setTexSize(256, 256);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void preDraw(int tex_id, float[] tex_matrix, int offset) {
/* 105 */     super.preDraw(tex_id, tex_matrix, offset);
/*     */     
/* 107 */     if (this.muKernelLoc >= 0) {
/* 108 */       GLES20.glUniform1fv(this.muKernelLoc, 9, this.mKernel, 0);
/* 109 */       GLHelper.checkGlError("set kernel");
/*     */     } 
/*     */     
/* 112 */     if (this.muTexOffsetLoc >= 0) {
/* 113 */       GLES20.glUniform2fv(this.muTexOffsetLoc, 9, this.mTexOffset, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setKernel(float[] values, float colorAdj) {
/* 118 */     if (values == null || values.length < 9) {
/* 119 */       throw new IllegalArgumentException("Kernel size is " + ((values != null) ? values.length : 0) + " vs. " + '\t');
/*     */     }
/* 121 */     synchronized (this.mSync) {
/* 122 */       System.arraycopy(values, 0, this.mKernel, 0, 9);
/* 123 */       setColorAdjust(colorAdj);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTexSize(int width, int height) {
/* 131 */     synchronized (this.mSync) {
/* 132 */       if (this.mTexWidth != width || this.mTexHeight != height) {
/* 133 */         this.mTexHeight = height;
/* 134 */         this.mTexWidth = width;
/* 135 */         float rw = 1.0F / width;
/* 136 */         float rh = 1.0F / height;
/*     */         
/* 138 */         this.mTexOffset[0] = -rw; this.mTexOffset[1] = -rh;
/* 139 */         this.mTexOffset[2] = 0.0F; this.mTexOffset[3] = -rh;
/* 140 */         this.mTexOffset[4] = rw; this.mTexOffset[5] = -rh;
/*     */         
/* 142 */         this.mTexOffset[6] = -rw; this.mTexOffset[7] = 0.0F;
/* 143 */         this.mTexOffset[8] = 0.0F; this.mTexOffset[9] = 0.0F;
/* 144 */         this.mTexOffset[10] = rw; this.mTexOffset[11] = 0.0F;
/*     */         
/* 146 */         this.mTexOffset[12] = -rw; this.mTexOffset[13] = rh;
/* 147 */         this.mTexOffset[14] = 0.0F; this.mTexOffset[15] = rh;
/* 148 */         this.mTexOffset[16] = rw; this.mTexOffset[17] = rh;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectKernel3x3Drawer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */