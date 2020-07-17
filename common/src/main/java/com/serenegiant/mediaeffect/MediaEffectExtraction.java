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
/*     */ public class MediaEffectExtraction
/*     */   extends MediaEffectGLESBase
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEffectExtraction";
/*     */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    vec3 min = vec3(uKernel[0], uKernel[2], uKernel[4]);\n    vec3 max = vec3(uKernel[1], uKernel[3], uKernel[5]);\n    vec3 add = vec3(uKernel[6], uKernel[7], uKernel[8]);\n    float e = 1e-10;\n    vec3 eps = vec3(e, e, e);\n    vec3 v = hsv;\n    if (hsv.r < min.r || hsv.r > max.r || hsv.g < min.g || hsv.g > max.g || hsv.b < min.b || hsv.b > max.b) {\n        v = vec3(0.0);\n    }\n    hsv = v + add;\n    if (uColorAdjust > 0.0) {\n        hsv = step(vec3(1.0, 1.0, uColorAdjust), hsv);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
/*  73 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    vec3 min = vec3(uKernel[0], uKernel[2], uKernel[4]);\n    vec3 max = vec3(uKernel[1], uKernel[3], uKernel[5]);\n    vec3 add = vec3(uKernel[6], uKernel[7], uKernel[8]);\n    float e = 1e-10;\n    vec3 eps = vec3(e, e, e);\n    vec3 v = hsv;\n    if (hsv.r < min.r || hsv.r > max.r || hsv.g < min.g || hsv.g > max.g || hsv.b < min.b || hsv.b > max.b) {\n        v = vec3(0.0);\n    }\n    hsv = v + add;\n    if (uColorAdjust > 0.0) {\n        hsv = step(vec3(1.0, 1.0, uColorAdjust), hsv);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/*  75 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    vec3 min = vec3(uKernel[0], uKernel[2], uKernel[4]);\n    vec3 max = vec3(uKernel[1], uKernel[3], uKernel[5]);\n    vec3 add = vec3(uKernel[6], uKernel[7], uKernel[8]);\n    float e = 1e-10;\n    vec3 eps = vec3(e, e, e);\n    vec3 v = hsv;\n    if (hsv.r < min.r || hsv.r > max.r || hsv.g < min.g || hsv.g > max.g || hsv.b < min.b || hsv.b > max.b) {\n        v = vec3(0.0);\n    }\n    hsv = v + add;\n    if (uColorAdjust > 0.0) {\n        hsv = step(vec3(1.0, 1.0, uColorAdjust), hsv);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */   
/*  77 */   private final float[] mLimit = new float[9];
/*     */   
/*     */   public MediaEffectExtraction() {
/*  80 */     super(new MediaEffectKernel3x3Drawer(FRAGMENT_SHADER));
/*     */     
/*  82 */     this.mLimit[0] = 0.0F; this.mLimit[1] = 1.0F;
/*  83 */     this.mLimit[2] = 0.0F; this.mLimit[3] = 1.0F;
/*  84 */     this.mLimit[4] = 0.0F; this.mLimit[5] = 1.0F;
/*  85 */     this.mLimit[6] = 0.0F; this.mLimit[7] = 0.0F; this.mLimit[8] = 0.0F;
/*  86 */     ((MediaEffectKernel3x3Drawer)this.mDrawer).setKernel(this.mLimit, 0.0F);
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
/* 105 */   public MediaEffectExtraction setParameter(float lowerH, float upperH, float lowerS, float upperS, float lowerV, float upperV, float color_adjust) { return setParameter(lowerH, upperH, lowerS, upperS, lowerV, upperV, 0.0F, 0.0F, 0.0F, color_adjust); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectExtraction setParameter(float lowerH, float upperH, float lowerS, float upperS, float lowerV, float upperV, float addH, float addS, float addV, float color_adjust) {
/* 115 */     this.mLimit[0] = Math.min(lowerH, upperH);
/* 116 */     this.mLimit[1] = Math.max(lowerH, upperH);
/* 117 */     this.mLimit[2] = Math.min(lowerS, upperS);
/* 118 */     this.mLimit[3] = Math.max(lowerS, upperS);
/* 119 */     this.mLimit[4] = Math.min(lowerV, upperV);
/* 120 */     this.mLimit[5] = Math.max(lowerV, upperV);
/* 121 */     this.mLimit[6] = addH;
/* 122 */     this.mLimit[7] = addS;
/* 123 */     this.mLimit[8] = addV;
/* 124 */     ((MediaEffectKernel3x3Drawer)this.mDrawer).setKernel(this.mLimit, color_adjust);
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaEffectExtraction setParameter(float[] limit, float color_adjust) {
/* 135 */     if (limit == null || limit.length < 6) {
/* 136 */       throw new IllegalArgumentException("limit is null or short");
/*     */     }
/* 138 */     System.arraycopy(limit, 0, this.mLimit, 0, 6);
/* 139 */     ((MediaEffectKernel3x3Drawer)this.mDrawer).setKernel(this.mLimit, color_adjust);
/* 140 */     return this;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectExtraction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */