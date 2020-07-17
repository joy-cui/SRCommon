/*     */ package com.serenegiant.mediaeffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaEffectCanny
/*     */   extends MediaEffectGLESBase
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEffectCanny";
/*     */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nconst float lowerThreshold = 0.4;\nconst float upperThreshold = 0.8;\nvoid main() {\n    vec4 magdir = texture2D(sTexture, vTextureCoord);\n    vec2 offset = ((magdir.gb * 2.0) - 1.0) * uTexOffset[8];\n    float first = texture2D(sTexture, vTextureCoord + offset).r;\n    float second = texture2D(sTexture, vTextureCoord - offset).r;\n    float multiplier = step(first, magdir.r);\n    multiplier = multiplier * step(second, magdir.r);\n    float threshold = smoothstep(lowerThreshold, upperThreshold, magdir.r);\n    multiplier = multiplier * threshold;\n    gl_FragColor = vec4(multiplier, multiplier, multiplier, 1.0);\n}\n";
/* 115 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nconst float lowerThreshold = 0.4;\nconst float upperThreshold = 0.8;\nvoid main() {\n    vec4 magdir = texture2D(sTexture, vTextureCoord);\n    vec2 offset = ((magdir.gb * 2.0) - 1.0) * uTexOffset[8];\n    float first = texture2D(sTexture, vTextureCoord + offset).r;\n    float second = texture2D(sTexture, vTextureCoord - offset).r;\n    float multiplier = step(first, magdir.r);\n    multiplier = multiplier * step(second, magdir.r);\n    float threshold = smoothstep(lowerThreshold, upperThreshold, magdir.r);\n    multiplier = multiplier * threshold;\n    gl_FragColor = vec4(multiplier, multiplier, multiplier, 1.0);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 117 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nconst float lowerThreshold = 0.4;\nconst float upperThreshold = 0.8;\nvoid main() {\n    vec4 magdir = texture2D(sTexture, vTextureCoord);\n    vec2 offset = ((magdir.gb * 2.0) - 1.0) * uTexOffset[8];\n    float first = texture2D(sTexture, vTextureCoord + offset).r;\n    float second = texture2D(sTexture, vTextureCoord - offset).r;\n    float multiplier = step(first, magdir.r);\n    multiplier = multiplier * step(second, magdir.r);\n    float threshold = smoothstep(lowerThreshold, upperThreshold, magdir.r);\n    multiplier = multiplier * threshold;\n    gl_FragColor = vec4(multiplier, multiplier, multiplier, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */   
/* 120 */   public MediaEffectCanny() { super(new MediaEffectKernel3x3Drawer(false, FRAGMENT_SHADER)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectCanny(float threshold) {
/* 125 */     this();
/* 126 */     setParameter(threshold);
/*     */   }
/*     */   
/*     */   public MediaEffectCanny setParameter(float threshold) {
/* 130 */     ((MediaEffectKernel3x3Drawer)this.mDrawer).setColorAdjust(threshold);
/* 131 */     return this;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectCanny.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */