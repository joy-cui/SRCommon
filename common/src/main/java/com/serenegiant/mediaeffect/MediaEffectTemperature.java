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
/*    */ public class MediaEffectTemperature
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectTemperature(EffectContext effect_context, float scale) {
/* 33 */     super(effect_context, "android.media.effect.effects.ColorTemperatureEffect");
/* 34 */     setParameter(scale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectTemperature setParameter(float scale) {
/* 42 */     setParameter("scale", Float.valueOf(scale));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectTemperature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */