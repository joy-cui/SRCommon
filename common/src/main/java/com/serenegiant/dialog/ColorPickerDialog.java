/*     */ package com.serenegiant.dialog;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.annotation.TargetApi;
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog;
/*     */ import android.app.Dialog;
/*     */ import android.app.DialogFragment;
/*     */ import android.app.Fragment;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.os.Bundle;
/*     */ import android.util.Log;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.FrameLayout;
/*     */ import com.serenegiant.common.R;
/*     */ import com.serenegiant.widget.ColorPickerView;
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
/*     */ @TargetApi(17)
/*     */ public class ColorPickerDialog
/*     */   extends DialogFragment
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "ColorPickerDialog";
/*     */   private static final String KEY_TITLE_ID = "title_id";
/*     */   private static final String KEY_COLOR_INIT = "initial_color";
/*     */   private static final String KEY_COLOR_CURRENT = "current_color";
/*     */   private static final int DEFAULT_COLOR = -1;
/*     */   private OnColorChangedListener mListener;
/*     */   private int mTitleResId;
/*  51 */   private int mInitialColor = -1;
/*  52 */   private int mCurrentColor = -1;
/*     */   
/*     */   private boolean isCanceled;
/*     */   
/*     */   private final ColorPickerView.ColorPickerListener mColorPickerListener;
/*     */   
/*     */   private final DialogInterface.OnClickListener mOnClickListner;
/*     */ 
/*     */   
/*     */   public static ColorPickerDialog show(Activity parent, int titleResId, int initialColor) {
/*  62 */     ColorPickerDialog dialog = newInstance(titleResId, initialColor);
/*  63 */     dialog.show(parent.getFragmentManager(), "ColorPickerDialog");
/*  64 */     return dialog;
/*     */   }
/*     */   
/*     */   public static ColorPickerDialog show(Fragment parent, int titleResId, int initialColor) {
/*  68 */     ColorPickerDialog dialog = newInstance(titleResId, initialColor);
/*  69 */     dialog.setTargetFragment(parent, 0);
/*  70 */     dialog.show(parent.getFragmentManager(), "ColorPickerDialog");
/*  71 */     return dialog;
/*     */   }
/*     */   
/*     */   public static ColorPickerDialog newInstance(int titleResId, int initialColor) {
/*  75 */     ColorPickerDialog dialog = new ColorPickerDialog();
/*  76 */     dialog.setArguments(titleResId, initialColor);
/*  77 */     return dialog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(int titleResId, int initialColor) {
/*  85 */     Bundle bundle = new Bundle();
/*  86 */     bundle.putInt("title_id", titleResId);
/*  87 */     bundle.putInt("initial_color", initialColor);
/*  88 */     bundle.remove("current_color");
/*  89 */     setArguments(bundle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCreate(Bundle savedInstanceState) {
/*  94 */     super.onCreate(savedInstanceState);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Bundle args = getArguments();
/* 100 */     if (args != null) {
/* 101 */       this.mTitleResId = args.getInt("title_id");
/* 102 */       this.mCurrentColor = this.mInitialColor = args.getInt("initial_color", -1);
/*     */     } 
/* 104 */     if (savedInstanceState != null) {
/* 105 */       this.mCurrentColor = savedInstanceState.getInt("current_color", this.mInitialColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSaveInstanceState(Bundle outState) {
/* 111 */     super.onSaveInstanceState(outState);
/*     */     
/* 113 */     if (outState != null) {
/* 114 */       outState.putInt("current_color", this.mCurrentColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAttach(Activity activity) {
/* 120 */     super.onAttach(activity);
/* 121 */     this.isCanceled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     try { this.mListener = (OnColorChangedListener)getTargetFragment(); }
/* 127 */     catch (NullPointerException nullPointerException) {  }
/* 128 */     catch (ClassCastException classCastException) {}
/*     */     
/* 130 */     if (this.mListener == null) {
/*     */ 
/*     */       
/* 133 */       try { this.mListener = (OnColorChangedListener)getParentFragment(); }
/* 134 */       catch (NullPointerException nullPointerException) {  }
/* 135 */       catch (ClassCastException classCastException) {}
/*     */     }
/* 137 */     if (this.mListener == null) {
/*     */ 
/*     */       
/* 140 */       try { this.mListener = (OnColorChangedListener)activity; }
/* 141 */       catch (ClassCastException classCastException) {  }
/* 142 */       catch (NullPointerException nullPointerException) {}
/*     */     }
/* 144 */     if (this.mListener == null)
/*     */     {
/*     */ 
/*     */       
/* 148 */       Log.w("ColorPickerDialog", "must implement OnColorChangedListener");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"InflateParams"})
/*     */   public Dialog onCreateDialog(Bundle savedInstanceState) {
/* 158 */     Activity activity = getActivity();
/* 159 */     FrameLayout rootView = (FrameLayout)LayoutInflater.from((Context)activity).inflate(R.layout.color_picker, null);
/* 160 */     FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
/*     */     
/* 162 */     ColorPickerView view = new ColorPickerView((Context)getActivity());
/* 163 */     view.setColor(this.mCurrentColor);
/* 164 */     view.setColorPickerListener(this.mColorPickerListener);
/* 165 */     rootView.addView((View)view, (ViewGroup.LayoutParams)params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     AlertDialog dialog = (new AlertDialog.Builder((Context)activity)).setPositiveButton(R.string.color_picker_select, this.mOnClickListner).setNegativeButton(R.string.color_picker_cancel, this.mOnClickListner).setTitle((this.mTitleResId != 0) ? this.mTitleResId : R.string.color_picker_default_title).setView((View)rootView).create();
/* 172 */     dialog.setCanceledOnTouchOutside(true);
/* 173 */     return (Dialog)dialog;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCancel(DialogInterface dialog) {
/* 178 */     super.onCancel(dialog);
/*     */     
/* 180 */     this.isCanceled = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDismiss(DialogInterface dialog) {
/* 185 */     super.onDismiss(dialog);
/* 186 */     if (this.mListener != null)
/* 187 */       if (this.isCanceled) {
/*     */         
/* 189 */         this.mListener.onCancel(this);
/*     */       } else {
/*     */         
/* 192 */         this.mListener.onDismiss(this, this.mCurrentColor);
/*     */       }  
/*     */   }
/*     */   
/*     */   public ColorPickerDialog() {
/* 197 */     this.mColorPickerListener = new ColorPickerView.ColorPickerListener()
/*     */       {
/*     */         public void onColorChanged(ColorPickerView view, int color) {
/* 200 */           if (ColorPickerDialog.this.mCurrentColor != color) {
/* 201 */             ColorPickerDialog.this.mCurrentColor = color;
/* 202 */             if (ColorPickerDialog.this.mListener != null) {
/* 203 */               ColorPickerDialog.this.mListener.onColorChanged(ColorPickerDialog.this, color);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 209 */     this.mOnClickListner = new DialogInterface.OnClickListener()
/*     */       {
/*     */         public void onClick(DialogInterface dialog, int which)
/*     */         {
/* 213 */           switch (which) {
/*     */             case -1:
/* 215 */               dialog.dismiss();
/*     */               break;
/*     */             case -2:
/* 218 */               dialog.cancel();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface OnColorChangedListener {
/*     */     void onColorChanged(ColorPickerDialog param1ColorPickerDialog, int param1Int);
/*     */     
/*     */     void onCancel(ColorPickerDialog param1ColorPickerDialog);
/*     */     
/*     */     void onDismiss(ColorPickerDialog param1ColorPickerDialog, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\dialog\ColorPickerDialog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */