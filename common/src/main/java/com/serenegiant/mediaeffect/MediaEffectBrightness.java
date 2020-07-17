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
/*    */ public class MediaEffectBrightness
/*    */   extends MediaEffectGLESBase
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaEffectBrightness";
/*    */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb + vec3(uColorAdjust, uColorAdjust, uColorAdjust), tex.w);\n}\n";
/* 39 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb + vec3(uColorAdjust, uColorAdjust, uColorAdjust), tex.w);\n}\n", new Object[] { "", "sampler2D" });
/*    */   
/* 41 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb + vec3(uColorAdjust, uColorAdjust, uColorAdjust), tex.w);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*    */ 
/*    */   
/* 44 */   public MediaEffectBrightness() { this(0.0F); }
/*    */ 
/*    */   
/*    */   public MediaEffectBrightness(float brightness) {
/* 48 */     super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER));
/* 49 */     setParameter(brightness);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectBrightness setParameter(float brightness) {
/* 58 */     setEnable((brightness != 0.0F));
/* 59 */     ((MediaEffectColorAdjustDrawer)this.mDrawer).setColorAdjust(brightness);
/* 60 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectBrightness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */