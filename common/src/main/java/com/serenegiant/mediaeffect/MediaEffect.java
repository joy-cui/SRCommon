/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.media.effect.Effect;
/*    */ import android.media.effect.EffectContext;
/*    */ import android.media.effect.EffectFactory;
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
/*    */ public class MediaEffect
/*    */   implements IEffect
/*    */ {
/*    */   protected final EffectContext mEffectContext;
/*    */   protected Effect mEffect;
/*    */   protected boolean mEnabled = true;
/*    */   
/*    */   public MediaEffect(EffectContext effect_context, String effectName) {
/* 36 */     this.mEffectContext = effect_context;
/* 37 */     EffectFactory factory = effect_context.getFactory();
/* 38 */     if (TextUtils.isEmpty(effectName)) {
/* 39 */       this.mEffect = null;
/*    */     } else {
/* 41 */       this.mEffect = factory.createEffect(effectName);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(int[] src_tex_ids, int width, int height, int out_tex_id) {
/* 47 */     if (this.mEnabled && this.mEffect != null) {
/* 48 */       this.mEffect.apply(src_tex_ids[0], width, height, out_tex_id);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(ISource src) {
/* 54 */     if (this.mEnabled && this.mEffect != null) {
/* 55 */       this.mEffect.apply(src.getSourceTexId()[0], src.getWidth(), src.getHeight(), src.getOutputTexId());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void release() {
/* 61 */     if (this.mEffect != null) {
/* 62 */       this.mEffect.release();
/* 63 */       this.mEffect = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public MediaEffect resize(int width, int height) { return this; }
/*    */ 
/*    */   
/*    */   protected MediaEffect setParameter(String parameterKey, Object value) {
/* 74 */     if (this.mEffect != null && value != null) {
/* 75 */       this.mEffect.setParameter(parameterKey, value);
/*    */     }
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public boolean enabled() { return this.mEnabled; }
/*    */ 
/*    */ 
/*    */   
/*    */   public IEffect setEnable(boolean enable) {
/* 87 */     this.mEnabled = enable;
/* 88 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */