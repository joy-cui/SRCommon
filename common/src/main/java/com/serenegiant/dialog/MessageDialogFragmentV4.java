/*     */ package com.serenegiant.dialog;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog;
/*     */ import android.app.Dialog;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.os.Bundle;
/*     */ import android.support.v4.app.DialogFragment;
/*     */ import android.support.v4.app.Fragment;
/*     */ import android.support.v4.app.FragmentActivity;
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
/*     */ public class MessageDialogFragmentV4
/*     */   extends DialogFragment
/*     */ {
/*  36 */   private static final String TAG = MessageDialogFragmentV4.class.getSimpleName();
/*     */ 
/*     */   
/*     */   private MessageDialogListener mDialogListener;
/*     */ 
/*     */   
/*     */   public static MessageDialogFragmentV4 showDialog(FragmentActivity parent, int requestCode, int id_title, int id_message, String[] permissions) {
/*  43 */     MessageDialogFragmentV4 dialog = newInstance(requestCode, id_title, id_message, permissions);
/*  44 */     dialog.show(parent.getSupportFragmentManager(), TAG);
/*  45 */     return dialog;
/*     */   }
/*     */   
/*     */   public static MessageDialogFragmentV4 showDialog(Fragment parent, int requestCode, int id_title, int id_message, String[] permissions) {
/*  49 */     MessageDialogFragmentV4 dialog = newInstance(requestCode, id_title, id_message, permissions);
/*  50 */     dialog.setTargetFragment(parent, parent.getId());
/*  51 */     dialog.show(parent.getFragmentManager(), TAG);
/*  52 */     return dialog;
/*     */   }
/*     */   
/*     */   public static MessageDialogFragmentV4 newInstance(int requestCode, int id_title, int id_message, String[] permissions) {
/*  56 */     MessageDialogFragmentV4 fragment = new MessageDialogFragmentV4();
/*  57 */     Bundle args = new Bundle();
/*     */     
/*  59 */     args.putInt("requestCode", requestCode);
/*  60 */     args.putInt("title", id_title);
/*  61 */     args.putInt("message", id_message);
/*  62 */     args.putStringArray("permissions", (permissions != null) ? permissions : new String[0]);
/*  63 */     fragment.setArguments(args);
/*  64 */     return fragment;
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
/*  77 */     super.onAttach(activity);
/*     */     
/*  79 */     if (activity instanceof MessageDialogListener) {
/*  80 */       this.mDialogListener = (MessageDialogListener)activity;
/*     */     }
/*  82 */     if (this.mDialogListener == null) {
/*  83 */       Fragment fragment = getTargetFragment();
/*  84 */       if (fragment instanceof MessageDialogListener) {
/*  85 */         this.mDialogListener = (MessageDialogListener)fragment;
/*     */       }
/*     */     } 
/*  88 */     if (this.mDialogListener == null && 
/*  89 */       BuildCheck.isAndroid4_2()) {
/*  90 */       Fragment target = getParentFragment();
/*  91 */       if (target instanceof MessageDialogListener) {
/*  92 */         this.mDialogListener = (MessageDialogListener)target;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     if (this.mDialogListener == null)
/*     */     {
/*  98 */       throw new ClassCastException(activity.toString());
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
/* 110 */     Bundle args = (savedInstanceState != null) ? savedInstanceState : getArguments();
/* 111 */     final int requestCode = getArguments().getInt("requestCode");
/* 112 */     int id_title = getArguments().getInt("title");
/* 113 */     int id_message = getArguments().getInt("message");
/* 114 */     final String[] permissions = args.getStringArray("permissions");
/*     */ 
/*     */     
/* 117 */     return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       (Dialog)(new AlertDialog.Builder((Context)getActivity())).setIcon(17301543).setTitle(id_title).setMessage(id_message).setPositiveButton(17039370, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) { try { MessageDialogFragmentV4.this.mDialogListener.onMessageDialogResult(MessageDialogFragmentV4.this, requestCode, permissions, true); } catch (Exception e) { Log.w(TAG, e); }  } }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) { try { MessageDialogFragmentV4.this.mDialogListener.onMessageDialogResult(MessageDialogFragmentV4.this, requestCode, permissions, false); } catch (Exception e) { Log.w(TAG, e); }  } }).create();
/*     */   }
/*     */   
/*     */   public static interface MessageDialogListener {
/*     */     void onMessageDialogResult(MessageDialogFragmentV4 param1MessageDialogFragmentV4, int param1Int, String[] param1ArrayOfString, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\dialog\MessageDialogFragmentV4.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */