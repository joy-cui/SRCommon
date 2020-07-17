/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.support.annotation.LayoutRes;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.MotionEvent;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.TextView;
/*     */ import com.serenegiant.common.R;
/*     */ import java.util.ArrayList;
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
/*     */ public class ValueSelectorAdapter
/*     */   extends ArrayAdapter<ValueSelectorAdapter.ValueEntry>
/*     */ {
/*  38 */   private static final String TAG = ValueSelectorAdapter.class.getSimpleName();
/*     */   private final LayoutInflater mInflater;
/*     */   private final int mLayoutId;
/*     */   private final int mTitleId;
/*     */   private final ValueSelectorAdapterListener mListener;
/*     */   private View.OnTouchListener mOnTouchListener;
/*     */   
/*     */   public static class ValueEntry {
/*     */     public final String title;
/*     */     
/*     */     private ValueEntry(String title, String value) {
/*  49 */       this.title = title;
/*  50 */       this.value = value;
/*     */     }
/*     */     public final String value; }
/*     */   
/*     */   private static List<ValueEntry> createEntries(Context context, int entries_res, int values_res) {
/*  55 */     String[] entries = context.getResources().getStringArray(entries_res);
/*  56 */     String[] values = context.getResources().getStringArray(values_res);
/*  57 */     int n = Math.min((entries != null) ? entries.length : 0, (values != null) ? values.length : 0);
/*  58 */     List<ValueEntry> result = new ArrayList<>(n);
/*  59 */     for (int i = 0; i < n; i++) {
/*  60 */       result.add(new ValueEntry(entries[i], values[i]));
/*     */     }
/*     */     
/*  63 */     return result;
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
/*     */   public ValueSelectorAdapter(Context context, @LayoutRes int layout_resource, int title_id, int entries_resource, int values_resource, ValueSelectorAdapterListener listener) {
/*  76 */     super(context, layout_resource, createEntries(context, entries_resource, values_resource));
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
/* 138 */     this.mOnTouchListener = new View.OnTouchListener()
/*     */       {
/*     */         public boolean onTouch(View v, MotionEvent event) {
/* 141 */           if (ValueSelectorAdapter.this.mListener != null) {
/* 142 */             ViewHolder holder = (ViewHolder)v.getTag();
/* 143 */             int position = (holder != null) ? holder.position : -1;
/*     */             try {
/* 145 */               ValueSelectorAdapter.this.mListener.onTouch(v, event, position);
/* 146 */             } catch (Exception exception) {}
/*     */           } 
/*     */           
/* 149 */           return false;
/*     */         }
/*     */       };
/*     */     this.mInflater = LayoutInflater.from(context);
/*     */     this.mLayoutId = layout_resource;
/*     */     this.mTitleId = title_id;
/*     */     this.mListener = listener;
/*     */   }
/*     */   
/*     */   public View getView(int position, View convertView, ViewGroup parent) {
/*     */     View rootView = convertView;
/*     */     if (rootView == null) {
/*     */       rootView = this.mInflater.inflate(this.mLayoutId, parent, false);
/*     */       ViewHolder holder = new ViewHolder();
/*     */       if (rootView instanceof TextView) {
/*     */         holder.titleTv = (TextView)rootView;
/*     */       } else {
/*     */         try {
/*     */           holder.titleTv = (TextView)rootView.findViewById(this.mTitleId);
/*     */         } catch (Exception e) {
/*     */           holder.titleTv = null;
/*     */         } 
/*     */         if (holder.titleTv == null)
/*     */           try {
/*     */             holder.titleTv = (TextView)rootView.findViewById(R.id.title);
/*     */           } catch (Exception e) {
/*     */             holder.titleTv = null;
/*     */           }  
/*     */       } 
/*     */       rootView.setOnTouchListener(this.mOnTouchListener);
/*     */       rootView.setTag(holder);
/*     */     } 
/*     */     ViewHolder holder = (ViewHolder)rootView.getTag();
/*     */     ValueEntry item = (ValueEntry)getItem(position);
/*     */     if (item != null && holder.titleTv != null)
/*     */       holder.titleTv.setText(item.title); 
/*     */     holder.position = position;
/*     */     return rootView;
/*     */   }
/*     */   
/*     */   public View getDropDownView(int position, View convertView, ViewGroup parent) { return getView(position, convertView, parent); }
/*     */   
/*     */   public int getPosition(int value) {
/*     */     int position = -1;
/*     */     String _value = Integer.toString(value);
/*     */     int n = getCount();
/*     */     for (int i = 0; i < n; i++) {
/*     */       ValueEntry entry = (ValueEntry)getItem(i);
/*     */       if (_value.equals(entry.value)) {
/*     */         position = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     return position;
/*     */   }
/*     */   
/*     */   private static class ViewHolder {
/*     */     int position;
/*     */     TextView titleTv;
/*     */     
/*     */     private ViewHolder() {}
/*     */   }
/*     */   
/*     */   public static interface ValueSelectorAdapterListener {
/*     */     void onTouch(View param1View, MotionEvent param1MotionEvent, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ValueSelectorAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */