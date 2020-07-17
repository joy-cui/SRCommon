/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.support.annotation.DrawableRes;
/*     */ import android.support.v7.widget.RecyclerView;
/*     */ import android.view.View;
/*     */ import com.serenegiant.common.R;
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
/*     */ public class DividerItemDecoration
/*     */   extends RecyclerView.ItemDecoration
/*     */ {
/*     */   public static final int HORIZONTAL_LIST = 0;
/*     */   public static final int VERTICAL_LIST = 1;
/*  40 */   private static final int[] ATTRS = new int[] { 16843284 };
/*     */ 
/*     */   
/*     */   private Drawable mDivider;
/*     */   
/*  45 */   private int mOrientation = 1;
/*     */   
/*     */   public DividerItemDecoration(Context context) {
/*  48 */     Drawable divider = null;
/*  49 */     TypedArray a = context.obtainStyledAttributes(ATTRS);
/*     */     try {
/*  51 */       divider = a.getDrawable(0);
/*  52 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  55 */     a.recycle();
/*  56 */     init(divider);
/*     */   }
/*     */ 
/*     */   
/*  60 */   public DividerItemDecoration(Context context, @DrawableRes int divider) { init(context.getResources().getDrawable(divider)); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public DividerItemDecoration(Context context, Drawable divider) { init(divider); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   private void init(Drawable divider) { this.mDivider = divider; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
/*  75 */     if (this.mDivider == null)
/*  76 */       return;  if (this.mOrientation == 1) {
/*  77 */       drawVertical(canvas, parent);
/*     */     } else {
/*  79 */       drawHorizontal(canvas, parent);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawVertical(Canvas canvas, RecyclerView parent) {
/*  84 */     RecyclerView.LayoutManager manager = parent.getLayoutManager();
/*     */     
/*  86 */     int left = parent.getPaddingLeft();
/*  87 */     int right = parent.getWidth() - parent.getPaddingRight();
/*     */     
/*  89 */     int childCount = parent.getChildCount() - 1;
/*  90 */     for (int i = 0; i < childCount; i++) {
/*  91 */       View child = parent.getChildAt(i);
/*  92 */       if (hasDivider(child)) {
/*  93 */         int top = child.getBottom();
/*  94 */         int bottom = top + this.mDivider.getIntrinsicHeight();
/*  95 */         this.mDivider.setBounds(left, top, right, bottom);
/*  96 */         this.mDivider.draw(canvas);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawHorizontal(Canvas canvas, RecyclerView parent) {
/* 102 */     RecyclerView.LayoutManager manager = parent.getLayoutManager();
/*     */     
/* 104 */     int top = parent.getPaddingTop();
/* 105 */     int bottom = parent.getHeight() - parent.getPaddingBottom();
/*     */     
/* 107 */     int childCount = parent.getChildCount() - 1;
/* 108 */     for (int i = 0; i < childCount; i++) {
/* 109 */       View child = parent.getChildAt(i);
/* 110 */       if (hasDivider(child)) {
/* 111 */         int left = child.getLeft();
/* 112 */         int right = left + this.mDivider.getIntrinsicWidth();
/* 113 */         this.mDivider.setBounds(left, top, right, bottom);
/* 114 */         this.mDivider.draw(canvas);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
/* 123 */     int position = parent.getChildAdapterPosition(view);
/* 124 */     if (this.mDivider == null) {
/* 125 */       outRect.set(0, 0, 0, 0);
/*     */     }
/* 127 */     else if (hasDivider(view)) {
/* 128 */       if (this.mOrientation == 1) {
/* 129 */         outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
/*     */       } else {
/* 131 */         outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
/*     */       } 
/*     */     } else {
/* 134 */       outRect.set(0, 0, 0, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrientation(int orientation) {
/* 140 */     if (orientation != 0 && orientation != 1) {
/* 141 */       throw new IllegalArgumentException("invalid orientation");
/*     */     }
/* 143 */     this.mOrientation = orientation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasDivider(View view) {
/* 152 */     if (view instanceof Dividable) {
/* 153 */       return ((Dividable)view).hasDivider();
/*     */     }
/* 155 */     Boolean b = (Boolean)view.getTag(R.id.has_divider);
/* 156 */     return (b != null && b.booleanValue());
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\DividerItemDecoration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */