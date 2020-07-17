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
/*    */ public class MediaEffectExposure
/*    */   extends MediaEffectGLESBase
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaEffectExposure";
/*    */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb * pow(2.0, uColorAdjust), tex.w);\n}\n";
/* 41 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb * pow(2.0, uColorAdjust), tex.w);\n}\n", new Object[] { "", "sampler2D" });
/*    */   
/* 43 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(tex.rgb * pow(2.0, uColorAdjust), tex.w);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*    */ 
/*    */   
/* 46 */   public MediaEffectExposure() { super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectExposure(float exposure) {
/* 51 */     this();
/* 52 */     setParameter(exposure);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectExposure setParameter(float exposure) {
/* 61 */     setEnable((exposure != 0.0F));
/* 62 */     ((MediaEffectColorAdjustDrawer)this.mDrawer).setColorAdjust(exposure);
/* 63 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectExposure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */