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
/*    */ public class MediaEffectGrain
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectGrain(EffectContext effect_context, float strength) {
/* 33 */     super(effect_context, "android.media.effect.effects.GrainEffect");
/* 34 */     setParameter(strength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectGrain setParameter(float strength) {
/* 42 */     if (strength >= 0.0F && strength <= 1.0F)
/* 43 */       setParameter("strength", Float.valueOf(strength)); 
/* 44 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectGrain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */