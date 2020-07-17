/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.media.effect.EffectContext;
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
/*    */ public class MediaEffectRedEye
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectRedEye(EffectContext effect_context, float[] centers) {
/* 33 */     super(effect_context, "android.media.effect.effects.RedEyeEffect");
/* 34 */     setParameter(centers);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectRedEye setParameter(float[] centers) {
/* 42 */     setParameter("centers", centers);
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectRedEye.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */