/*     */ package com.serenegiant.dialog;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog;
/*     */ import android.app.Dialog;
/*     */ import android.app.DialogFragment;
/*     */ import android.app.Fragment;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.os.Bundle;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.BuildCheck;
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
/*     */ public class MessageDialogFragment
/*     */   extends DialogFragment
/*     */ {
/*  38 */   private static final String TAG = MessageDialogFragment.class.getSimpleName();
/*     */ 
/*     */   
/*     */   private MessageDialogListener mDialogListener;
/*     */ 
/*     */   
/*     */   public static MessageDialogFragment showDialog(Activity parent, int requestCode, int id_title, int id_message, String[] permissions) {
/*  45 */     MessageDialogFragment dialog = newInstance(requestCode, id_title, id_message, permissions);
/*  46 */     dialog.show(parent.getFragmentManager(), TAG);
/*  47 */     return dialog;
/*     */   }
/*     */   
/*     */   public static MessageDialogFragment showDialog(Fragment parent, int requestCode, int id_title, int id_message, String[] permissions) {
/*  51 */     MessageDialogFragment dialog = newInstance(requestCode, id_title, id_message, permissions);
/*  52 */     dialog.setTargetFragment(parent, parent.getId());
/*  53 */     dialog.show(parent.getFragmentManager(), TAG);
/*  54 */     return dialog;
/*     */   }
/*     */   
/*     */   public static MessageDialogFragment newInstance(int requestCode, int id_title, int id_message, String[] permissions) {
/*  58 */     MessageDialogFragment fragment = new MessageDialogFragment();
/*  59 */     Bundle args = new Bundle();
/*     */     
/*  61 */     args.putInt("requestCode", requestCode);
/*  62 */     args.putInt("title", id_title);
/*  63 */     args.putInt("message", id_message);
/*  64 */     args.putStringArray("permissions", (permissions != null) ? permissions : new String[0]);
/*  65 */     fragment.setArguments(args);
/*  66 */     return fragment;
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
/*     */   @SuppressLint({"NewApi"})
/*     */   public void onAttach(Activity activity) {
/*  79 */     super.onAttach(activity);
/*     */     
/*  81 */     if (activity instanceof MessageDialogListener) {
/*  82 */       this.mDialogListener = (MessageDialogListener)activity;
/*     */     }
/*  84 */     if (this.mDialogListener == null) {
/*  85 */       Fragment fragment = getTargetFragment();
/*  86 */       if (fragment instanceof MessageDialogListener) {
/*  87 */         this.mDialogListener = (MessageDialogListener)fragment;
/*     */       }
/*     */     } 
/*  90 */     if (this.mDialogListener == null && 
/*  91 */       BuildCheck.isAndroid4_2()) {
/*  92 */       Fragment target = getParentFragment();
/*  93 */       if (target instanceof MessageDialogListener) {
/*  94 */         this.mDialogListener = (MessageDialogListener)target;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     if (this.mDialogListener == null)
/*     */     {
/* 100 */       throw new ClassCastException(activity.toString());
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
/*     */   public Dialog onCreateDialog(Bundle savedInstanceState) {
/* 112 */     Bundle args = (savedInstanceState != null) ? savedInstanceState : getArguments();
/* 113 */     final int requestCode = getArguments().getInt("requestCode");
/* 114 */     int id_title = getArguments().getInt("title");
/* 115 */     int id_message = getArguments().getInt("message");
/* 116 */     final String[] permissions = args.getStringArray("permissions");
/*     */ 
/*     */     
/* 119 */     return 
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
/* 149 */       (Dialog)(new AlertDialog.Builder((Context)getActivity())).setIcon(17301543).setTitle(id_title).setMessage(id_message).setPositiveButton(17039370, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) { try { MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, requestCode, permissions, true); } catch (Exception e) { Log.w(TAG, e); }  } }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) { try { MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, requestCode, permissions, false); } catch (Exception e) { Log.w(TAG, e); }  } }).create();
/*     */   }
/*     */   
/*     */   public static interface MessageDialogListener {
/*     */     void onMessageDialogResult(MessageDialogFragment param1MessageDialogFragment, int param1Int, String[] param1ArrayOfString, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\dialog\MessageDialogFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */