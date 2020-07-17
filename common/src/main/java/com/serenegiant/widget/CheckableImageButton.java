/*    */ package com.serenegiant.widget;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.AttributeSet;
/*    */ import android.widget.Checkable;
/*    */ import android.widget.ImageButton;
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
/*    */ public class CheckableImageButton
/*    */   extends ImageButton
/*    */   implements Checkable
/*    */ {
/*    */   private static final boolean DEBUG = false;
/* 28 */   private static final String TAG = CheckableImageButton.class.getSimpleName();
/*    */   
/*    */   private boolean mIsChecked;
/* 31 */   private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
/*    */ 
/*    */   
/* 34 */   public CheckableImageButton(Context context) { this(context, null, 0); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public CheckableImageButton(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public CheckableImageButton(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChecked(boolean checked) {
/* 47 */     if (this.mIsChecked != checked) {
/* 48 */       this.mIsChecked = checked;
/* 49 */       refreshDrawableState();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean isChecked() { return this.mIsChecked; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public void toggle() { setChecked(!this.mIsChecked); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] onCreateDrawableState(int extraSpace) {
/* 65 */     int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
/* 66 */     if (isChecked()) {
/* 67 */       mergeDrawableStates(drawableState, CHECKED_STATE_SET);
/*    */     }
/* 69 */     return drawableState;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\CheckableImageButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */