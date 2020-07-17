/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.content.AsyncQueryHandler;
/*     */ import android.content.ContentResolver;
/*     */ import android.content.Context;
/*     */ import android.database.ContentObserver;
/*     */ import android.database.Cursor;
/*     */ import android.database.DataSetObserver;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.os.Handler;
/*     */ import android.support.v4.view.PagerAdapter;
/*     */ import android.util.Log;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ImageView;
/*     */ import android.widget.TextView;
/*     */ import com.serenegiant.common.R;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaStoreImageAdapter
/*     */   extends PagerAdapter
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  47 */   private static final String TAG = MediaStoreImageAdapter.class.getSimpleName();
/*     */   
/*     */   private final LayoutInflater mInflater;
/*     */   private final int mLayoutId;
/*     */   private final ContentResolver mCr;
/*     */   private final MyAsyncQueryHandler mQueryHandler;
/*     */   protected boolean mDataValid;
/*     */   protected int mRowIDColumn;
/*     */   protected ChangeObserver mChangeObserver;
/*     */   protected DataSetObserver mDataSetObserver;
/*     */   private Cursor mCursor;
/*  58 */   private String mSelection = MediaStoreHelper.SELECTIONS[1];
/*  59 */   private String[] mSelectionArgs = null;
/*     */   
/*     */   private boolean mShowTitle;
/*     */ 
/*     */   
/*  64 */   public MediaStoreImageAdapter(Context context, int id_layout) { this(context, id_layout, true); }
/*     */ 
/*     */   
/*     */   public MediaStoreImageAdapter(Context context, int id_layout, boolean needQuery) {
/*  68 */     this.mInflater = LayoutInflater.from(context);
/*  69 */     this.mLayoutId = id_layout;
/*  70 */     this.mCr = context.getContentResolver();
/*  71 */     this.mQueryHandler = new MyAsyncQueryHandler(this.mCr, this);
/*  72 */     if (needQuery) {
/*  73 */       startQuery();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  79 */     changeCursor(null);
/*  80 */     super.finalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount() {
/*  85 */     if (this.mDataValid && this.mCursor != null) {
/*  86 */       return this.mCursor.getCount();
/*     */     }
/*  88 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiateItem(ViewGroup container, int position) {
/*  95 */     View view = this.mInflater.inflate(this.mLayoutId, container, false);
/*     */     
/*  97 */     if (view != null) {
/*  98 */       container.addView(view);
/*  99 */       ViewHolder holder = (ViewHolder)view.getTag();
/* 100 */       if (holder == null) {
/* 101 */         holder = new ViewHolder();
/*     */       }
/* 103 */       TextView tv = holder.mTitleView = (TextView)view.findViewById(R.id.title);
/* 104 */       ImageView iv = holder.mImageView = (ImageView)view.findViewById(R.id.thumbnail);
/* 105 */       if (holder.info == null) {
/* 106 */         holder.info = new MediaStoreHelper.MediaInfo();
/*     */       }
/* 108 */       holder.info.loadFromCursor(getCursor(position));
/*     */       
/* 110 */       Drawable drawable = iv.getDrawable();
/* 111 */       if (drawable == null || !(drawable instanceof MediaStoreHelper.LoaderDrawable)) {
/* 112 */         drawable = createLoaderDrawable(this.mCr, holder.info);
/* 113 */         iv.setImageDrawable(drawable);
/*     */       } 
/* 115 */       ((MediaStoreHelper.LoaderDrawable)drawable).startLoad(holder.info.mediaType, 0, holder.info.id);
/* 116 */       if (tv != null) {
/* 117 */         tv.setVisibility(this.mShowTitle ? 0 : 8);
/* 118 */         if (this.mShowTitle) {
/* 119 */           tv.setText(holder.info.title);
/*     */         }
/*     */       } 
/*     */     } 
/* 123 */     return view;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroyItem(ViewGroup container, int position, Object object) {
/* 129 */     if (object instanceof View) {
/* 130 */       container.removeView((View)object);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public int getItemPosition(Object object) { return super.getItemPosition(object); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItemPositionFromID(long id) {
/* 142 */     int result = -1;
/* 143 */     Cursor cursor = this.mCr.query(MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mSelection, this.mSelectionArgs, null);
/* 144 */     if (cursor != null) {
/*     */       try {
/* 146 */         if (cursor.moveToFirst())
/* 147 */           for (int ix = 0;; ix++) {
/* 148 */             if (cursor.getLong(0) == id) {
/*     */               
/* 150 */               result = ix;
/*     */               break;
/*     */             } 
/* 153 */             if (!cursor.moveToNext())
/*     */               break; 
/*     */           }  
/*     */       } finally {
/* 157 */         cursor.close();
/*     */       } 
/*     */     }
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public boolean isViewFromObject(View view, Object object) { return view.equals(object); }
/*     */ 
/*     */   
/*     */   protected void changeCursor(Cursor cursor) {
/* 169 */     Cursor old = swapCursor(cursor);
/* 170 */     if (old != null && !old.isClosed()) {
/* 171 */       old.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected Cursor getCursor(int position) {
/* 176 */     if (this.mDataValid && this.mCursor != null) {
/* 177 */       this.mCursor.moveToPosition(position);
/* 178 */       return this.mCursor;
/*     */     } 
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Cursor swapCursor(Cursor newCursor) {
/* 185 */     if (newCursor == this.mCursor) {
/* 186 */       return null;
/*     */     }
/* 188 */     Cursor oldCursor = this.mCursor;
/* 189 */     if (oldCursor != null) {
/* 190 */       if (this.mChangeObserver != null) oldCursor.unregisterContentObserver(this.mChangeObserver); 
/* 191 */       if (this.mDataSetObserver != null) oldCursor.unregisterDataSetObserver(this.mDataSetObserver); 
/*     */     } 
/* 193 */     this.mCursor = newCursor;
/* 194 */     if (newCursor != null) {
/* 195 */       if (this.mChangeObserver != null) newCursor.registerContentObserver(this.mChangeObserver); 
/* 196 */       if (this.mDataSetObserver != null) newCursor.registerDataSetObserver(this.mDataSetObserver); 
/* 197 */       this.mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
/* 198 */       this.mDataValid = true;
/*     */       
/* 200 */       notifyDataSetChanged();
/*     */     } else {
/* 202 */       this.mRowIDColumn = -1;
/* 203 */       this.mDataValid = false;
/*     */       
/* 205 */       notifyDataSetInvalidated();
/*     */     } 
/* 207 */     return oldCursor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyDataSetInvalidated() {}
/*     */ 
/*     */   
/* 215 */   public void startQuery() { this.mQueryHandler.requery(); }
/*     */ 
/*     */ 
/*     */   
/* 219 */   protected MediaStoreHelper.LoaderDrawable createLoaderDrawable(ContentResolver cr, MediaStoreHelper.MediaInfo info) { return new ImageLoaderDrawable(cr, info.width, info.height); }
/*     */   
/*     */   private static final class ViewHolder {
/*     */     TextView mTitleView;
/*     */     ImageView mImageView;
/*     */     MediaStoreHelper.MediaInfo info;
/*     */     
/*     */     private ViewHolder() {} }
/*     */   
/*     */   private static final class MyAsyncQueryHandler extends AsyncQueryHandler { private final MediaStoreImageAdapter mAdapter;
/*     */     
/*     */     public MyAsyncQueryHandler(ContentResolver cr, MediaStoreImageAdapter adapter) {
/* 231 */       super(cr);
/* 232 */       this.mAdapter = adapter;
/*     */     }
/*     */     
/*     */     public void requery() {
/* 236 */       synchronized (this.mAdapter) {
/* 237 */         startQuery(0, this.mAdapter, MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mAdapter
/* 238 */             .mSelection, this.mAdapter.mSelectionArgs, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
/* 245 */       Cursor oldCursor = this.mAdapter.swapCursor(cursor);
/* 246 */       if (oldCursor != null && !oldCursor.isClosed())
/* 247 */         oldCursor.close(); 
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ChangeObserver
/*     */     extends ContentObserver
/*     */   {
/* 254 */     public ChangeObserver() { super(new Handler()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     public boolean deliverSelfNotifications() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     public void onChange(boolean selfChange) { MediaStoreImageAdapter.this.startQuery(); }
/*     */   }
/*     */   
/*     */   private class MyDataSetObserver
/*     */     extends DataSetObserver
/*     */   {
/*     */     public void onChanged() {
/* 271 */       MediaStoreImageAdapter.this.mDataValid = true;
/* 272 */       MediaStoreImageAdapter.this.notifyDataSetChanged();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onInvalidated() {
/* 277 */       MediaStoreImageAdapter.this.mDataValid = false;
/* 278 */       MediaStoreImageAdapter.this.notifyDataSetInvalidated();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ImageLoaderDrawable
/*     */     extends MediaStoreHelper.LoaderDrawable {
/* 284 */     public ImageLoaderDrawable(ContentResolver cr, int width, int height) { super(cr, width, height); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     protected MediaStoreHelper.ImageLoader createThumbnailLoader() { return new MyImageLoader(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     protected Bitmap checkBitmapCache(int hashCode, long id) { return null; }
/*     */   }
/*     */   
/*     */   private static class MyImageLoader
/*     */     extends MediaStoreHelper.ImageLoader
/*     */   {
/* 300 */     public MyImageLoader(ImageLoaderDrawable parent) { super(parent); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Bitmap loadBitmap(ContentResolver cr, int mediaType, int hashCode, long id, int requestWidth, int requestHeight) {
/* 305 */       Bitmap result = null;
/*     */       try {
/* 307 */         result = MediaStoreHelper.getImage(cr, id, requestWidth, requestHeight);
/* 308 */         if (result != null) {
/* 309 */           int w = result.getWidth();
/* 310 */           int h = result.getHeight();
/* 311 */           Rect bounds = new Rect();
/* 312 */           this.mParent.copyBounds(bounds);
/* 313 */           int cx = bounds.centerX();
/* 314 */           int cy = bounds.centerY();
/* 315 */           bounds.set(cx - w / 2, cy - h / w, cx + w / 2, cy + h / 2);
/* 316 */           this.mParent.onBoundsChange(bounds);
/*     */         } 
/* 318 */       } catch (IOException e) {
/* 319 */         Log.w(TAG, e);
/*     */       } 
/* 321 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaStoreImageAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */