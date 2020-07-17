/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.view.accessibility.AccessibilityEvent;
/*     */ import android.view.accessibility.AccessibilityNodeInfo;
/*     */ import android.widget.CompoundButton;
/*     */ import android.widget.RadioButton;
/*     */ import android.widget.RelativeLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RelativeRadioGroup
/*     */   extends RelativeLayout
/*     */ {
/*  57 */   private int mCheckedId = -1;
/*     */   
/*     */   private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
/*     */   
/*     */   private boolean mProtectFromCheckedChange = false;
/*     */   
/*     */   private OnCheckedChangeListener mOnCheckedChangeListener;
/*     */   
/*     */   private PassThroughHierarchyChangeListener mPassThroughListener;
/*     */ 
/*     */   
/*     */   public RelativeRadioGroup(Context context) {
/*  69 */     super(context);
/*  70 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RelativeRadioGroup(Context context, AttributeSet attrs) {
/*  77 */     super(context, attrs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/*  94 */     this.mChildOnCheckedChangeListener = new CheckedStateTracker();
/*  95 */     this.mPassThroughListener = new PassThroughHierarchyChangeListener();
/*  96 */     super.setOnHierarchyChangeListener(this.mPassThroughListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) { this.mPassThroughListener.mOnHierarchyChangeListener = listener; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onFinishInflate() {
/* 113 */     super.onFinishInflate();
/*     */ 
/*     */     
/* 116 */     if (this.mCheckedId != -1) {
/* 117 */       this.mProtectFromCheckedChange = true;
/* 118 */       setCheckedStateForView(this.mCheckedId, true);
/* 119 */       this.mProtectFromCheckedChange = false;
/* 120 */       setCheckedId(this.mCheckedId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addView(View child, int index, ViewGroup.LayoutParams params) {
/* 126 */     if (child instanceof RadioButton) {
/* 127 */       RadioButton button = (RadioButton)child;
/* 128 */       if (button.isChecked()) {
/* 129 */         this.mProtectFromCheckedChange = true;
/* 130 */         if (this.mCheckedId != -1) {
/* 131 */           setCheckedStateForView(this.mCheckedId, false);
/*     */         }
/* 133 */         this.mProtectFromCheckedChange = false;
/* 134 */         setCheckedId(button.getId());
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     super.addView(child, index, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(int id) {
/* 153 */     if (id != -1 && id == this.mCheckedId) {
/*     */       return;
/*     */     }
/*     */     
/* 157 */     if (this.mCheckedId != -1) {
/* 158 */       setCheckedStateForView(this.mCheckedId, false);
/*     */     }
/*     */     
/* 161 */     if (id != -1) {
/* 162 */       setCheckedStateForView(id, true);
/*     */     }
/*     */     
/* 165 */     setCheckedId(id);
/*     */   }
/*     */   
/*     */   private void setCheckedId(int id) {
/* 169 */     this.mCheckedId = id;
/* 170 */     if (this.mOnCheckedChangeListener != null) {
/* 171 */       this.mOnCheckedChangeListener.onCheckedChanged(this, this.mCheckedId);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setCheckedStateForView(int viewId, boolean checked) {
/* 176 */     View checkedView = findViewById(viewId);
/* 177 */     if (checkedView != null && checkedView instanceof RadioButton) {
/* 178 */       ((RadioButton)checkedView).setChecked(checked);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public int getCheckedRadioButtonId() { return this.mCheckedId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public void clearCheck() { check(-1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   public void setOnCheckedChangeListener(OnCheckedChangeListener listener) { this.mOnCheckedChangeListener = listener; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   public LayoutParams generateLayoutParams(AttributeSet attrs) { return new LayoutParams(getContext(), attrs); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   protected boolean checkLayoutParams(ViewGroup.LayoutParams p) { return p instanceof LayoutParams; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   protected RelativeLayout.LayoutParams generateDefaultLayoutParams() { return new LayoutParams(-2, -2); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
/* 242 */     super.onInitializeAccessibilityEvent(event);
/* 243 */     event.setClassName(RelativeRadioGroup.class.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
/* 248 */     super.onInitializeAccessibilityNodeInfo(info);
/* 249 */     info.setClassName(RelativeRadioGroup.class.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LayoutParams
/*     */     extends RelativeLayout.LayoutParams
/*     */   {
/* 267 */     public LayoutParams(Context c, AttributeSet attrs) { super(c, attrs); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     public LayoutParams(int w, int h) { super(w, h); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     public LayoutParams(ViewGroup.LayoutParams p) { super(p); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     public LayoutParams(MarginLayoutParams source) { super(source); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
/* 305 */       if (a.hasValue(widthAttr)) {
/* 306 */         this.width = a.getLayoutDimension(widthAttr, "layout_width");
/*     */       } else {
/* 308 */         this.width = -2;
/*     */       } 
/*     */       
/* 311 */       if (a.hasValue(heightAttr)) {
/* 312 */         this.height = a.getLayoutDimension(heightAttr, "layout_height");
/*     */       } else {
/* 314 */         this.height = -2;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface OnCheckedChangeListener
/*     */   {
/*     */     void onCheckedChanged(RelativeRadioGroup param1RelativeRadioGroup, int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CheckedStateTracker
/*     */     implements CompoundButton.OnCheckedChangeListener
/*     */   {
/*     */     private CheckedStateTracker() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/* 337 */       if (RelativeRadioGroup.this.mProtectFromCheckedChange) {
/*     */         return;
/*     */       }
/*     */       
/* 341 */       RelativeRadioGroup.this.mProtectFromCheckedChange = true;
/* 342 */       if (RelativeRadioGroup.this.mCheckedId != -1) {
/* 343 */         RelativeRadioGroup.this.setCheckedStateForView(RelativeRadioGroup.this.mCheckedId, false);
/*     */       }
/* 345 */       RelativeRadioGroup.this.mProtectFromCheckedChange = false;
/*     */       
/* 347 */       int id = buttonView.getId();
/* 348 */       RelativeRadioGroup.this.setCheckedId(id);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PassThroughHierarchyChangeListener
/*     */     implements OnHierarchyChangeListener
/*     */   {
/*     */     private OnHierarchyChangeListener mOnHierarchyChangeListener;
/*     */ 
/*     */ 
/*     */     
/*     */     private PassThroughHierarchyChangeListener() {}
/*     */ 
/*     */     
/*     */     public void onChildViewAdded(View parent, View child) {
/* 365 */       if (parent == RelativeRadioGroup.this && child instanceof RadioButton) {
/* 366 */         int id = child.getId();
/*     */         
/* 368 */         if (id == -1) {
/* 369 */           id = child.hashCode();
/* 370 */           child.setId(id);
/*     */         } 
/* 372 */         ((RadioButton)child).setOnCheckedChangeListener(RelativeRadioGroup.this
/* 373 */             .mChildOnCheckedChangeListener);
/*     */       } 
/*     */       
/* 376 */       if (this.mOnHierarchyChangeListener != null) {
/* 377 */         this.mOnHierarchyChangeListener.onChildViewAdded(parent, child);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onChildViewRemoved(View parent, View child) {
/* 385 */       if (parent == RelativeRadioGroup.this && child instanceof RadioButton) {
/* 386 */         ((RadioButton)child).setOnCheckedChangeListener(null);
/*     */       }
/*     */       
/* 389 */       if (this.mOnHierarchyChangeListener != null)
/* 390 */         this.mOnHierarchyChangeListener.onChildViewRemoved(parent, child); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\RelativeRadioGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */