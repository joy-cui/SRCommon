/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MediaEffectPosterize
/*    */   extends MediaEffectGLESBase
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaEffectBrightness";
/*    */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = floor((tex * uColorAdjust) + vec4(0.5)) / uColorAdjust;\n}\n";
/* 41 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = floor((tex * uColorAdjust) + vec4(0.5)) / uColorAdjust;\n}\n", new Object[] { "", "sampler2D" });
/*    */   
/* 43 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = floor((tex * uColorAdjust) + vec4(0.5)) / uColorAdjust;\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*    */ 
/*    */   
/* 46 */   public MediaEffectPosterize() { this(10.0F); }
/*    */ 
/*    */   
/*    */   public MediaEffectPosterize(float posterize) {
/* 50 */     super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER));
/* 51 */     setParameter(posterize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectPosterize setParameter(float posterize) {
/* 60 */     ((MediaEffectColorAdjustDrawer)this.mDrawer).setColorAdjust(posterize);
/* 61 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectPosterize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */