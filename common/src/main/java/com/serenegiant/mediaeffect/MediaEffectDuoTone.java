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
/*    */ public class MediaEffectDuoTone
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectDuoTone(EffectContext effect_context, int first_color, int second_color) {
/* 34 */     super(effect_context, "android.media.effect.effects.DuotoneEffect");
/* 35 */     setParameter(first_color, second_color);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectDuoTone setParameter(int first_color, int second_color) {
/* 44 */     setParameter("first_color", Integer.valueOf(first_color));
/* 45 */     setParameter("second_color", Integer.valueOf(second_color));
/* 46 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectDuoTone.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */