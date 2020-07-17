/*    */ package com.serenegiant.widget;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.preference.Preference;
/*    */ import android.util.AttributeSet;
/*    */ import android.view.View;
/*    */ import android.widget.TextView;
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
/*    */ public class MultilineLabelPreference
/*    */   extends Preference
/*    */ {
/* 29 */   public MultilineLabelPreference(Context context) { super(context); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public MultilineLabelPreference(Context context, AttributeSet attrs) { super(context, attrs); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public MultilineLabelPreference(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onBindView(View view) {
/* 42 */     super.onBindView(view);
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
/*    */     try {
/* 54 */       TextView summary = (TextView)view.findViewById(16908304);
/* 55 */       summary.setSingleLine(false);
/* 56 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\MultilineLabelPreference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */