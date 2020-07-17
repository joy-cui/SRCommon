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
/*    */ public class MediaEffectAutoFix
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectAutoFix(EffectContext effect_context, float scale) {
/* 33 */     super(effect_context, "android.media.effect.effects.AutoFixEffect");
/* 34 */     setParameter(scale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectAutoFix setParameter(float scale) {
/* 42 */     setEnable((scale != 0.0F));
/* 43 */     setParameter("scale", Float.valueOf(scale));
/* 44 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectAutoFix.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */