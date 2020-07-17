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
/*    */ public class MediaEffectFillLight
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectFillLight(EffectContext effect_context, float strength) {
/* 33 */     super(effect_context, "android.media.effect.effects.FillLightEffect");
/* 34 */     setParameter(strength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectFillLight setParameter(float strength) {
/* 42 */     setParameter("strength", Float.valueOf(strength));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectFillLight.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */