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
/*    */ public class MediaEffectRotate
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectRotate(EffectContext effect_context, int angle) {
/* 33 */     super(effect_context, "android.media.effect.effects.RotateEffect");
/* 34 */     setParameter(angle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectRotate setParameter(int angle) {
/* 42 */     setParameter("angle", Integer.valueOf(angle));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectRotate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */