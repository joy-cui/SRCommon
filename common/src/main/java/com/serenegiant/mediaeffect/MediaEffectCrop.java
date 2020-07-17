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
/*    */ 
/*    */ 
/*    */ public class MediaEffectCrop
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectCrop(EffectContext effect_context, int x, int y, int width, int height) {
/* 36 */     super(effect_context, "android.media.effect.effects.CropEffect");
/* 37 */     setParameter(x, y, width, height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectCrop setParameter(int x, int y, int width, int height) {
/* 48 */     setParameter("xorigin", Integer.valueOf(x));
/* 49 */     setParameter("yorigin", Integer.valueOf(y));
/* 50 */     setParameter("width", Integer.valueOf(width));
/* 51 */     setParameter("height", Integer.valueOf(height));
/* 52 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectCrop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */