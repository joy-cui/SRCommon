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
/*    */ public class MediaEffectEmboss
/*    */   extends MediaEffectKernel
/*    */ {
/*    */   private float mIntensity;
/*    */   
/* 25 */   public MediaEffectEmboss() { this(1.0F); }
/*    */ 
/*    */   
/*    */   public MediaEffectEmboss(float intensity) {
/* 29 */     super(new float[] { intensity * -2.0F, -intensity, 0.0F, -intensity, 1.0F, intensity, 0.0F, intensity, intensity * 2.0F });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     this.mIntensity = intensity;
/*    */   }
/*    */   
/*    */   public MediaEffectEmboss setParameter(float intensity) {
/* 38 */     if (this.mIntensity != intensity) {
/* 39 */       this.mIntensity = intensity;
/* 40 */       setParameter(new float[] { intensity * -2.0F, -intensity, 0.0F, -intensity, 1.0F, intensity, 0.0F, intensity, intensity * 2.0F }, 0.0F);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectEmboss.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */