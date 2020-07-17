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
/*    */ 
/*    */ public class MediaEffectSaturateGLES
/*    */   extends MediaEffectGLESBase
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaEffectBrightness";
/*    */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nconst highp vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\nhighp float getIntensity(vec3 c) {\nreturn dot(c.rgb, luminanceWeighting);\n}\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    highp float intensity = getIntensity(tex.rgb);\n    highp vec3 greyScaleColor = vec3(intensity, intensity, intensity);\n    gl_FragColor = vec4(mix(greyScaleColor, tex.rgb, uColorAdjust), tex.w);\n}\n";
/* 42 */   private static final String FRAGMENT_SHADER = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nconst highp vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\nhighp float getIntensity(vec3 c) {\nreturn dot(c.rgb, luminanceWeighting);\n}\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    highp float intensity = getIntensity(tex.rgb);\n    highp vec3 greyScaleColor = vec3(intensity, intensity, intensity);\n    gl_FragColor = vec4(mix(greyScaleColor, tex.rgb, uColorAdjust), tex.w);\n}\n", new Object[] { "", "sampler2D" });
/*    */   
/* 44 */   private static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uColorAdjust;\nconst highp vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\nhighp float getIntensity(vec3 c) {\nreturn dot(c.rgb, luminanceWeighting);\n}\nvoid main() {\n    highp vec4 tex = texture2D(sTexture, vTextureCoord);\n    highp float intensity = getIntensity(tex.rgb);\n    highp vec3 greyScaleColor = vec3(intensity, intensity, intensity);\n    gl_FragColor = vec4(mix(greyScaleColor, tex.rgb, uColorAdjust), tex.w);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*    */ 
/*    */   
/* 47 */   public MediaEffectSaturateGLES() { this(0.0F); }
/*    */ 
/*    */   
/*    */   public MediaEffectSaturateGLES(float saturation) {
/* 51 */     super(new MediaEffectColorAdjustDrawer(FRAGMENT_SHADER));
/* 52 */     setParameter(saturation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectSaturateGLES setParameter(float saturation) {
/* 61 */     ((MediaEffectColorAdjustDrawer)this.mDrawer).setColorAdjust(saturation + 1.0F);
/* 62 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectSaturateGLES.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */