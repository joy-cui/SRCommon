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
/*    */ public class MediaEffectStraighten
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectStraighten(EffectContext effect_context, float angle) {
/* 33 */     super(effect_context, "android.media.effect.effects.StraightenEffect");
/* 34 */     setParameter(angle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectStraighten setParameter(float angle) {
/* 42 */     setParameter("angle", Float.valueOf(angle));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectStraighten.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */