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
/*    */ public class MediaEffectKernel
/*    */   extends MediaEffectGLESBase
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */   private static final String TAG = "MediaEffectKernel";
/*    */   
/* 28 */   public MediaEffectKernel() { super(new MediaEffectKernel3x3Drawer(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", MediaEffectKernel3x3Drawer.FRAGMENT_SHADER_2D)); }
/*    */ 
/*    */   
/*    */   public MediaEffectKernel(float[] kernel) {
/* 32 */     this();
/* 33 */     setParameter(kernel, 0.0F);
/*    */   }
/*    */   
/*    */   public MediaEffectKernel(float[] kernel, float color_adjust) {
/* 37 */     this();
/* 38 */     setParameter(kernel, color_adjust);
/*    */   }
/*    */ 
/*    */   
/*    */   public MediaEffectKernel resize(int width, int height) {
/* 43 */     super.resize(width, height);
/* 44 */     setTexSize(width, height);
/* 45 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public void setKernel(float[] values, float colorAdj) { ((MediaEffectKernel3x3Drawer)this.mDrawer).setKernel(values, colorAdj); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void setColorAdjust(float adjust) { ((MediaEffectKernel3x3Drawer)this.mDrawer).setColorAdjust(adjust); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public void setTexSize(int width, int height) { this.mDrawer.setTexSize(width, height); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectKernel setParameter(float[] kernel, float color_adjust) {
/* 70 */     setKernel(kernel, color_adjust);
/* 71 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectKernel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */