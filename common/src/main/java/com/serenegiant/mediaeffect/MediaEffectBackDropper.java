/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.media.effect.EffectContext;
/*    */ import android.text.TextUtils;
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
/*    */ public class MediaEffectBackDropper
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectBackDropper(EffectContext effect_context, String source) {
/* 34 */     super(effect_context, "android.media.effect.effects.BackDropperEffect");
/* 35 */     setParameter(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectBackDropper setParameter(String source) {
/* 43 */     if (!TextUtils.isEmpty(source))
/* 44 */       setParameter("source", source); 
/* 45 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectBackDropper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */