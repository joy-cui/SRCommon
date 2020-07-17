/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.support.v7.widget.LinearLayoutManager;
/*     */ import android.support.v7.widget.RecyclerView;
/*     */ import android.util.AttributeSet;
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
/*     */ public class RecycleViewWithEmptyView
/*     */   extends RecyclerView
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  35 */   private static final String TAG = RecycleViewWithEmptyView.class.getSimpleName();
/*     */   @Nullable
/*     */   private View mEmptyView;
/*     */   private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
/*     */   
/*  40 */   public RecycleViewWithEmptyView(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public RecycleViewWithEmptyView(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecycleViewWithEmptyView(Context context, @Nullable AttributeSet attrs, int defStyle) {
/*  52 */     super(context, attrs, defStyle);
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
/* 114 */     this.mAdapterDataObserver = new RecyclerView.AdapterDataObserver()
/*     */       {
/*     */         public void onChanged() {
/* 117 */           super.onChanged();
/* 118 */           RecycleViewWithEmptyView.this.updateEmptyView();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onItemRangeChanged(int positionStart, int itemCount) {
/* 123 */           super.onItemRangeChanged(positionStart, itemCount);
/* 124 */           RecycleViewWithEmptyView.this.updateEmptyView();
/*     */         }
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
/*     */         public void onItemRangeRemoved(int positionStart, int itemCount) {
/* 143 */           super.onItemRangeRemoved(positionStart, itemCount);
/* 144 */           RecycleViewWithEmptyView.this.updateEmptyView();
/*     */         }
/*     */       };
/*     */     Drawable divider = null;
/*     */     if (attrs != null) {
/*     */       int defStyleRes = 0;
/*     */       TypedArray attribs = context.obtainStyledAttributes(attrs, R.styleable.RecycleViewWithEmptyView, defStyle, defStyleRes);
/*     */       try {
/*     */         if (attribs.hasValue(R.styleable.RecycleViewWithEmptyView_listDivider))
/*     */           divider = attribs.getDrawable(R.styleable.RecycleViewWithEmptyView_listDivider); 
/*     */       } catch (Exception exception) {}
/*     */       attribs.recycle();
/*     */     } 
/*     */     int orientation = 1;
/*     */     if (getLayoutManager() instanceof LinearLayoutManager)
/*     */       orientation = ((LinearLayoutManager)getLayoutManager()).getOrientation(); 
/*     */     DividerItemDecoration deco = new DividerItemDecoration(context, divider);
/*     */     deco.setOrientation(orientation);
/*     */     addItemDecoration(deco);
/*     */   }
/*     */   
/*     */   public void setAdapter(RecyclerView.Adapter adapter) {
/*     */     if (getAdapter() != adapter) {
/*     */       try {
/*     */         if (getAdapter() != null)
/*     */           getAdapter().unregisterAdapterDataObserver(this.mAdapterDataObserver); 
/*     */       } catch (Exception exception) {}
/*     */       super.setAdapter(adapter);
/*     */       if (adapter != null)
/*     */         adapter.registerAdapterDataObserver(this.mAdapterDataObserver); 
/*     */     } 
/*     */     updateEmptyView();
/*     */   }
/*     */   
/*     */   public void setEmptyView(View empty_view) {
/*     */     if (this.mEmptyView != empty_view) {
/*     */       this.mEmptyView = empty_view;
/*     */       updateEmptyView();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateEmptyView() {
/*     */     if (this.mEmptyView != null) {
/*     */       final RecyclerView.Adapter adapter = getAdapter();
/*     */       post(new Runnable() {
/*     */             public void run() { RecycleViewWithEmptyView.this.mEmptyView.setVisibility((adapter == null || adapter.getItemCount() == 0) ? 0 : 8); }
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\RecycleViewWithEmptyView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */