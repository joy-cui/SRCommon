/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.graphics.Bitmap;
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
/*    */ public class MediaEffectBitmapOverlay
/*    */   extends MediaEffect
/*    */ {
/*    */   public MediaEffectBitmapOverlay(EffectContext effect_context, Bitmap bitmap) {
/* 34 */     super(effect_context, "android.media.effect.effects.BitmapOverlayEffect");
/* 35 */     setParameter(bitmap);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MediaEffectBitmapOverlay setParameter(Bitmap bitmap) {
/* 43 */     setParameter("bitmap", bitmap);
/* 44 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffectBitmapOverlay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */