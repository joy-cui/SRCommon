/*    */ package com.serenegiant.widget;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.AttributeSet;
/*    */ import android.view.KeyEvent;
/*    */ import android.view.MotionEvent;
/*    */ import android.widget.ImageButton;
/*    */ import com.serenegiant.common.R;
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
/*    */ public final class ItemPickerButton
/*    */   extends ImageButton
/*    */ {
/*    */   private ItemPicker mNumberPicker;
/*    */   
/* 37 */   public ItemPickerButton(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public ItemPickerButton(Context context, AttributeSet attrs) { super(context, attrs); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public ItemPickerButton(Context context) { super(context); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public void setNumberPicker(ItemPicker picker) { this.mNumberPicker = picker; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onTouchEvent(MotionEvent event) {
/* 54 */     cancelLongpressIfRequired(event);
/* 55 */     return super.onTouchEvent(event);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onTrackballEvent(MotionEvent event) {
/* 60 */     cancelLongpressIfRequired(event);
/* 61 */     return super.onTrackballEvent(event);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onKeyUp(int keyCode, KeyEvent event) {
/* 66 */     if (keyCode == 23 || keyCode == 66)
/*    */     {
/* 68 */       cancelLongpress();
/*    */     }
/* 70 */     return super.onKeyUp(keyCode, event);
/*    */   }
/*    */   
/*    */   private void cancelLongpressIfRequired(MotionEvent event) {
/* 74 */     if (event.getAction() == 3 || event
/* 75 */       .getAction() == 1) {
/* 76 */       cancelLongpress();
/*    */     }
/*    */   }
/*    */   
/*    */   private void cancelLongpress() {
/* 81 */     if (R.id.increment == getId()) {
/* 82 */       this.mNumberPicker.cancelIncrement();
/* 83 */     } else if (R.id.decrement == getId()) {
/* 84 */       this.mNumberPicker.cancelDecrement();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ItemPickerButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */