/*    */ package com.serenegiant.mediaeffect;
/*    */ 
/*    */ import android.graphics.Bitmap;
/*    */ import com.serenegiant.glutils.TextureOffscreen;
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
/*    */ public class MediaImageSource
/*    */   extends MediaSource
/*    */ {
/*    */   private TextureOffscreen mImageOffscreen;
/*    */   private boolean isReset;
/*    */   
/*    */   public MediaImageSource(Bitmap src) {
/* 33 */     super(src.getWidth(), src.getHeight());
/* 34 */     this.mImageOffscreen = new TextureOffscreen(this.mWidth, this.mHeight, false);
/* 35 */     setSource(src);
/*    */   }
/*    */   
/*    */   public ISource setSource(Bitmap bitmap) {
/* 39 */     this.mImageOffscreen.loadBitmap(bitmap);
/* 40 */     reset();
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ISource reset() {
/* 46 */     super.reset();
/* 47 */     this.isReset = true;
/* 48 */     this.mSrcTexIds[0] = this.mImageOffscreen.getTexture();
/* 49 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ISource apply(IEffect effect) {
/* 54 */     if (this.mSourceScreen != null) {
/* 55 */       if (this.isReset) {
/* 56 */         this.isReset = false;
/* 57 */         this.needSwap = true;
/*    */       } else {
/* 59 */         if (this.needSwap) {
/* 60 */           TextureOffscreen temp = this.mSourceScreen;
/* 61 */           this.mSourceScreen = this.mOutputScreen;
/* 62 */           this.mOutputScreen = temp;
/* 63 */           this.mSrcTexIds[0] = this.mSourceScreen.getTexture();
/*    */         } 
/* 65 */         this.needSwap = !this.needSwap;
/*    */       } 
/* 67 */       effect.apply(this.mSrcTexIds, this.mOutputScreen.getTexWidth(), this.mOutputScreen.getTexHeight(), this.mOutputScreen.getTexture());
/*    */     } 
/* 69 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\MediaImageSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */