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
/*    */ public class MediaEffectBlackWhite
/*    */   extends MediaEffect
/*    */ {
/* 32 */   public MediaEffectBlackWhite(EffectContext effect_context) { this(effect_context, 0.0F, 1.0F); }
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
/*    */   public MediaEffectBlackWhite(EffectContext effect_context, float black, float white) {
/* 44 */     super(effect_context, "android.media.effect.effects.BlackWhiteEffect");
/* 45 */     setParameter(black, white);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectBlackWhite setParameter(float black, float white) {
/* 54 */     setParameter("black", Float.valueOf(black));
/* 55 */     setParameter("white", Float.valueOf(white));
/* 56 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectBlackWhite.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */