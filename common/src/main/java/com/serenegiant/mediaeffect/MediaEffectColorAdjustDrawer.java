/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.opengl.GLES20;
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
/*    */ public class MediaEffectColorAdjustDrawer
/*    */   extends MediaEffectDrawer
/*    */ {
/*    */   private int muColorAdjustLoc;
/*    */   private float mColorAdjust;
/*    */   
/* 30 */   public MediaEffectColorAdjustDrawer(String fss) { this(false, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public MediaEffectColorAdjustDrawer(boolean isOES, String fss) { this(isOES, "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", fss); }
/*    */ 
/*    */   
/*    */   public MediaEffectColorAdjustDrawer(boolean isOES, String vss, String fss) {
/* 38 */     super(isOES, vss, fss);
/* 39 */     this.muColorAdjustLoc = GLES20.glGetUniformLocation(getProgram(), "uColorAdjust");
/* 40 */     if (this.muColorAdjustLoc < 0) {
/* 41 */       this.muColorAdjustLoc = -1;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setColorAdjust(float adjust) {
/* 46 */     synchronized (this.mSync) {
/* 47 */       this.mColorAdjust = adjust;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preDraw(int tex_id, float[] tex_matrix, int offset) {
/* 53 */     super.preDraw(tex_id, tex_matrix, offset);
/*    */     
/* 55 */     if (this.muColorAdjustLoc >= 0)
/* 56 */       GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust); 
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectColorAdjustDrawer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */