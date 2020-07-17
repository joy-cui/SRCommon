package com.serenegiant.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface CustomRecycleViewListener<T> {
  void onItemClick(RecyclerView.Adapter<?> paramAdapter, View paramView, int paramInt, T paramT);
  
  boolean onItemLongClick(RecyclerView.Adapter<?> paramAdapter, View paramView, int paramInt, T paramT);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\CustomRecycleViewListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */