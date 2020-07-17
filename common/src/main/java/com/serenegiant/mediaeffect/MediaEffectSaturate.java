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
/*    */ public class MediaEffectSaturate
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectSaturate(EffectContext effect_context, float scale) {
/* 33 */     super(effect_context, "android.media.effect.effects.SaturateEffect");
/* 34 */     setParameter(scale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectSaturate setParameter(float saturation) {
/* 42 */     setEnable((saturation != 0.0F));
/* 43 */     setParameter("scale", Float.valueOf(saturation));
/* 44 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectSaturate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */