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
/*    */ public class MediaEffectTint
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectTint(EffectContext effect_context, int tint) {
/* 33 */     super(effect_context, "android.media.effect.effects.TintEffect");
/* 34 */     setParameter(tint);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectTint setParameter(int tint) {
/* 42 */     setParameter("tint", Integer.valueOf(tint));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectTint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */