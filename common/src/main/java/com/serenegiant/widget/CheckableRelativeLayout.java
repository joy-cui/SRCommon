/*    */ package com.serenegiant.widget;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.AttributeSet;
/*    */ import android.view.MotionEvent;
/*    */ import android.view.View;
/*    */ import android.view.ViewGroup;
/*    */ import android.widget.Checkable;
/*    */ import android.widget.RelativeLayout;
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
/*    */ public class CheckableRelativeLayout
/*    */   extends RelativeLayout
/*    */   implements Checkable, Touchable
/*    */ {
/*    */   private boolean mIsChecked;
/* 32 */   private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
/*    */   private float mTouchX;
/*    */   
/* 35 */   public CheckableRelativeLayout(Context context) { this(context, null); }
/*    */   
/*    */   private float mTouchY;
/*    */   
/* 39 */   public CheckableRelativeLayout(Context context, AttributeSet attrs) { super(context, attrs); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean isChecked() { return this.mIsChecked; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChecked(boolean checked) {
/* 50 */     if (this.mIsChecked != checked) {
/* 51 */       this.mIsChecked = checked;
/* 52 */       updateChildState((ViewGroup)this, checked);
/* 53 */       refreshDrawableState();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void updateChildState(ViewGroup group, boolean checked) {
/* 58 */     int n = group.getChildCount();
/* 59 */     for (int i = 0; i < n; i++) {
/* 60 */       View child = group.getChildAt(i);
/* 61 */       if (child instanceof Checkable) {
/* 62 */         ((Checkable)child).setChecked(checked);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void toggle() { setChecked(!this.mIsChecked); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int[] onCreateDrawableState(int extraSpace) {
/* 74 */     int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
/* 75 */     if (isChecked()) {
/* 76 */       mergeDrawableStates(drawableState, CHECKED_STATE_SET);
/*    */     }
/* 78 */     return drawableState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onInterceptTouchEvent(MotionEvent ev) {
/* 84 */     this.mTouchX = ev.getX();
/* 85 */     this.mTouchY = ev.getY();
/* 86 */     return super.onInterceptTouchEvent(ev);
/*    */   }
/*    */ 
/*    */   
/* 90 */   public float touchX() { return this.mTouchX; }
/*    */   
/* 92 */   public float touchY() { return this.mTouchY; }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\CheckableRelativeLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */