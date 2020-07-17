/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.support.annotation.LayoutRes;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.support.v7.widget.RecyclerView;
/*     */ import android.util.Log;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.Checkable;
/*     */ import com.serenegiant.common.R;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ public abstract class CustomRecycleViewAdapter<T>
/*     */   extends RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder<T>>
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  46 */   private static final String TAG = CustomRecycleViewAdapter.class.getSimpleName(); @LayoutRes
/*     */   private final int mItemViewId;
/*     */   @NonNull
/*     */   private final List<T> mItems;
/*     */   private LayoutInflater mLayoutInflater;
/*     */   private RecyclerView mRecycleView;
/*     */   private CustomRecycleViewListener<T> mCustomRecycleViewListener;
/*  53 */   private Handler mUIHandler = new Handler(Looper.getMainLooper());
/*     */ 
/*     */ 
/*     */   
/*     */   protected final View.OnClickListener mOnClickListener;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final View.OnLongClickListener mOnLongClickListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  67 */     synchronized (this.mItems) {
/*  68 */       unregisterDataSetObserver(this.mItems);
/*     */     } 
/*  70 */     super.finalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
/*  75 */     super.onAttachedToRecyclerView(recyclerView);
/*  76 */     this.mRecycleView = recyclerView;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
/*  81 */     this.mRecycleView = null;
/*  82 */     super.onDetachedFromRecyclerView(recyclerView);
/*     */   }
/*     */ 
/*     */   
/*     */   public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
/*  87 */     LayoutInflater inflater = getLayoutInflater(parent.getContext());
/*  88 */     View view = onCreateItemView(inflater, parent, viewType);
/*  89 */     view.setOnClickListener(this.mOnClickListener);
/*  90 */     view.setOnLongClickListener(this.mOnLongClickListener);
/*  91 */     return onCreateViewHolder(view);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected View onCreateItemView(LayoutInflater inflater, ViewGroup parent, int viewType) { return inflater.inflate(this.mItemViewId, parent, false); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected ViewHolder<T> onCreateViewHolder(View item) { return new ViewHolder<>(item); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public int getItemCount() { return this.mItems.size(); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public T getItem(int position) { return (position >= 0 && position < this.mItems.size()) ? this.mItems.get(position) : null; }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public void setOnItemClickListener(CustomRecycleViewListener<T> listener) { this.mCustomRecycleViewListener = listener; }
/*     */ 
/*     */   
/*     */   @Nullable
/* 118 */   public RecyclerView getParent() { return this.mRecycleView; }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 122 */     synchronized (this.mItems) {
/* 123 */       unregisterDataSetObserver(this.mItems);
/* 124 */       this.mItems.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(Collection<? extends T> collection) {
/* 130 */     synchronized (this.mItems) {
/* 131 */       unregisterDataSetObserver(this.mItems);
/* 132 */       this.mItems.clear();
/* 133 */       this.mItems.addAll(collection);
/* 134 */       registerDataSetObserver(this.mItems);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sort(Comparator<? super T> comparator) {
/* 140 */     synchronized (this.mItems) {
/* 141 */       Collections.sort(this.mItems, comparator);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected LayoutInflater getLayoutInflater(Context context) {
/* 146 */     if (this.mLayoutInflater == null) {
/* 147 */       this.mLayoutInflater = LayoutInflater.from(context);
/*     */     }
/* 149 */     return this.mLayoutInflater;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomRecycleViewAdapter(@LayoutRes int layout_id, @NonNull List<T> devices) {
/* 156 */     this.mOnClickListener = new View.OnClickListener()
/*     */       {
/*     */         public void onClick(final View v) {
/* 159 */           if (CustomRecycleViewAdapter.this.mRecycleView != null) {
/* 160 */             if (v instanceof Checkable) {
/* 161 */               ((Checkable)v).setChecked(true);
/* 162 */               CustomRecycleViewAdapter.this.mUIHandler.postDelayed(new Runnable()
/*     */                   {
/*     */                     public void run() {
/* 165 */                       ((Checkable)v).setChecked(false);
/*     */                     }
/*     */                   },  100L);
/*     */             } 
/* 169 */             if (CustomRecycleViewAdapter.this.mCustomRecycleViewListener != null) {
/* 170 */               Integer pos = (Integer)v.getTag(R.id.position);
/* 171 */               if (pos != null) {
/*     */                 try {
/* 173 */                   T item = CustomRecycleViewAdapter.this.getItem(pos.intValue());
/* 174 */                   CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemClick(CustomRecycleViewAdapter.this, v, pos
/* 175 */                       .intValue(), item);
/*     */                   return;
/* 177 */                 } catch (Exception e) {
/* 178 */                   Log.w(TAG, e);
/*     */                 } 
/*     */               }
/*     */               try {
/* 182 */                 int position = CustomRecycleViewAdapter.this.mRecycleView.getChildAdapterPosition(v);
/* 183 */                 T item = CustomRecycleViewAdapter.this.getItem(position);
/* 184 */                 CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemClick(CustomRecycleViewAdapter.this, v, position, item);
/*     */               }
/* 186 */               catch (Exception e) {
/* 187 */                 Log.w(TAG, e);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 194 */     this.mOnLongClickListener = new View.OnLongClickListener()
/*     */       {
/*     */         
/*     */         public boolean onLongClick(View v)
/*     */         {
/* 199 */           if (CustomRecycleViewAdapter.this.mRecycleView != null) {
/*     */             try {
/* 201 */               if (CustomRecycleViewAdapter.this.mCustomRecycleViewListener != null) {
/* 202 */                 int position = CustomRecycleViewAdapter.this.mRecycleView.getChildAdapterPosition(v);
/* 203 */                 T item = CustomRecycleViewAdapter.this.getItem(position);
/* 204 */                 return CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemLongClick(CustomRecycleViewAdapter.this, v, position, item);
/*     */               }
/*     */             
/* 207 */             } catch (Exception e) {
/* 208 */               Log.w(TAG, e);
/*     */             } 
/*     */           }
/* 211 */           return false;
/*     */         }
/*     */       };
/*     */     this.mItemViewId = layout_id;
/*     */     this.mItems = devices;
/*     */     synchronized (this.mItems) {
/*     */       registerDataSetObserver(this.mItems);
/*     */     } 
/*     */   } protected abstract void registerDataSetObserver(List<T> paramList); protected abstract void unregisterDataSetObserver(List<T> paramList); public static class ViewHolder<T> extends RecyclerView.ViewHolder { public ViewHolder(View view) {
/* 220 */       super(view);
/* 221 */       this.mView = view;
/*     */     }
/*     */     public final View mView;
/*     */     public T mItem;
/*     */     
/* 226 */     public String toString() { return super.toString() + " '" + this.mItem + "'"; }
/*     */ 
/*     */ 
/*     */     
/* 230 */     public void setEnable(boolean enable) { this.mView.setEnabled(enable); }
/*     */ 
/*     */     
/*     */     public void hasDivider(boolean hasDivider) {
/* 234 */       if (this.mView instanceof Dividable) {
/* 235 */         ((Dividable)this.mView).hasDivider(hasDivider);
/*     */       } else {
/* 237 */         this.mView.setTag(R.id.has_divider, Boolean.valueOf(hasDivider));
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasDivider() {
/* 242 */       if (this.mView instanceof Dividable) {
/* 243 */         return ((Dividable)this.mView).hasDivider();
/*     */       }
/* 245 */       Boolean b = (Boolean)this.mView.getTag(R.id.has_divider);
/* 246 */       return (b != null && b.booleanValue());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\CustomRecycleViewAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */