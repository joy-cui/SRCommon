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
/*    */ public class MediaEffectContrast
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectContrast(EffectContext effect_context, float contrast) {
/* 33 */     super(effect_context, "android.media.effect.effects.ContrastEffect");
/* 34 */     setParameter(contrast);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectContrast setParameter(float contrast) {
/* 42 */     setParameter("contrast", Float.valueOf(contrast));
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectContrast.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */