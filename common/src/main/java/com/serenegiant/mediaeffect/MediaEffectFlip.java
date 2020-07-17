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
/*    */ 
/*    */ public class MediaEffectFlip
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectFlip(EffectContext effect_context, boolean flip_vertical, boolean flip_horizontal) {
/* 34 */     super(effect_context, "android.media.effect.effects.FlipEffect");
/* 35 */     setParameter(flip_vertical, flip_horizontal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectFlip setParameter(boolean flip_vertical, boolean flip_horizontal) {
/* 45 */     setParameter("vertical", Boolean.valueOf(flip_vertical));
/* 46 */     setParameter("horizontal", Boolean.valueOf(flip_horizontal));
/* 47 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectFlip.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */