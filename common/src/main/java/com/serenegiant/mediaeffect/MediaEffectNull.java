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
/*    */ public class MediaEffectNull
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectNull(EffectContext effect_context) {
/* 32 */     super(effect_context, "android.media.effect.effects.AutoFixEffect");
/* 33 */     setParameter("scale", Float.valueOf(0.0F));
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectNull.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */