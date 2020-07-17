/*    */ package com.serenegiant.widget;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.AttributeSet;
/*    */ import android.view.MotionEvent;
/*    */ import android.view.View;
/*    */ import android.widget.Checkable;
/*    */ import android.widget.LinearLayout;
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
/*    */ public class CheckableLinearLayout
/*    */   extends LinearLayout
/*    */   implements Checkable, Touchable
/*    */ {
/*    */   private boolean mChecked;
/* 35 */   private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
/*    */   private float mTouchX;
/*    */   
/* 38 */   public CheckableLinearLayout(Context context) { this(context, null); }
/*    */   
/*    */   private float mTouchY;
/*    */   
/* 42 */   public CheckableLinearLayout(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean isChecked() { return this.mChecked; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChecked(boolean checked) {
/* 57 */     if (this.mChecked != checked) {
/* 58 */       this.mChecked = checked;
/* 59 */       int n = getChildCount();
/*    */       
/* 61 */       for (int i = 0; i < n; i++) {
/* 62 */         View v = getChildAt(i);
/* 63 */         if (v instanceof Checkable)
/* 64 */           ((Checkable)v).setChecked(checked); 
/*    */       } 
/* 66 */       refreshDrawableState();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public void toggle() { setChecked(!this.mChecked); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] onCreateDrawableState(int extraSpace) {
/* 77 */     int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
/* 78 */     if (isChecked()) {
/* 79 */       mergeDrawableStates(drawableState, CHECKED_STATE_SET);
/*    */     }
/* 81 */     return drawableState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onInterceptTouchEvent(MotionEvent ev) {
/* 87 */     this.mTouchX = ev.getX();
/* 88 */     this.mTouchY = ev.getY();
/* 89 */     return super.onInterceptTouchEvent(ev);
/*    */   }
/*    */ 
/*    */   
/* 93 */   public float touchX() { return this.mTouchX; }
/*    */   
/* 95 */   public float touchY() { return this.mTouchY; }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\CheckableLinearLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */