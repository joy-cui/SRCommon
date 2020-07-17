/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.app.ActivityManager;
/*     */ import android.content.AsyncQueryHandler;
/*     */ import android.content.ContentResolver;
/*     */ import android.content.Context;
/*     */ import android.database.Cursor;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.BitmapFactory;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.provider.MediaStore;
/*     */ import android.support.v4.util.LruCache;
/*     */ import android.support.v4.widget.CursorAdapter;
/*     */ import android.util.Log;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ImageView;
/*     */ import android.widget.TextView;
/*     */ import com.serenegiant.common.R;
/*     */ import com.serenegiant.utils.ThreadPool;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class MediaStoreAdapter
/*     */   extends CursorAdapter
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  54 */   private static final String TAG = MediaStoreAdapter.class.getSimpleName();
/*     */   
/*     */   private static final int CACHE_RATE = 8;
/*     */   
/*     */   private static LruCache<String, Bitmap> sThumbnailCache;
/*     */   
/*  60 */   private int mThumbnailWidth = 200; private int mThumbnailHeight = 200;
/*     */   
/*     */   private final LayoutInflater mInflater;
/*     */   private final ContentResolver mCr;
/*     */   private final int mMemClass;
/*     */   private final int mLayoutId;
/*     */   private final MyAsyncQueryHandler mQueryHandler;
/*  67 */   private final int mHashCode = hashCode();
/*     */   private Cursor mMediaInfoCursor;
/*     */   private String mSelection;
/*     */   private String[] mSelectionArgs;
/*     */   private boolean mShowTitle = false;
/*  72 */   private int mMediaType = 0;
/*  73 */   private final MediaStoreHelper.MediaInfo info = new MediaStoreHelper.MediaInfo();
/*     */ 
/*     */   
/*     */   public MediaStoreAdapter(Context context, int id_layout) {
/*  77 */     super(context, null, 2);
/*  78 */     this.mInflater = LayoutInflater.from(context);
/*  79 */     this.mCr = context.getContentResolver();
/*  80 */     this.mQueryHandler = new MyAsyncQueryHandler(this.mCr, this);
/*     */     
/*  82 */     this.mMemClass = ((ActivityManager)context.getSystemService("activity")).getMemoryClass();
/*  83 */     this.mLayoutId = id_layout;
/*  84 */     onContentChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public View newView(Context context, Cursor cursor, ViewGroup parent) {
/*  90 */     View view = this.mInflater.inflate(this.mLayoutId, parent, false);
/*  91 */     getViewHolder(view);
/*  92 */     return view;
/*     */   }
/*     */ 
/*     */   
/*  96 */   protected MediaStoreHelper.LoaderDrawable createLoaderDrawable(ContentResolver cr) { return new ThumbnailLoaderDrawable(cr, this.mThumbnailWidth, this.mThumbnailHeight); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindView(View view, Context context, Cursor cursor) {
/* 102 */     ViewHolder holder = getViewHolder(view);
/*     */     
/* 104 */     ImageView iv = holder.mImageView;
/* 105 */     TextView tv = holder.mTitleView;
/* 106 */     Drawable drawable = iv.getDrawable();
/* 107 */     if (drawable == null || !(drawable instanceof MediaStoreHelper.LoaderDrawable)) {
/* 108 */       drawable = createLoaderDrawable(this.mCr);
/* 109 */       iv.setImageDrawable(drawable);
/*     */     } 
/* 111 */     ((MediaStoreHelper.LoaderDrawable)drawable).startLoad(cursor.getInt(2), this.mHashCode, cursor.getLong(0));
/* 112 */     if (tv != null) {
/* 113 */       tv.setVisibility(this.mShowTitle ? 0 : 8);
/* 114 */       if (this.mShowTitle) {
/* 115 */         tv.setText(cursor.getString(1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ViewHolder getViewHolder(View view) {
/* 124 */     ViewHolder holder = (ViewHolder)view.getTag(R.id.mediastorephotoadapter);
/* 125 */     if (holder == null) {
/* 126 */       holder = new ViewHolder();
/* 127 */       if (view instanceof ImageView) {
/* 128 */         holder.mImageView = (ImageView)view;
/* 129 */         view.setTag(R.id.mediastorephotoadapter, holder);
/*     */       } else {
/* 131 */         View v = view.findViewById(R.id.thumbnail);
/* 132 */         if (v instanceof ImageView)
/* 133 */           holder.mImageView = (ImageView)v; 
/* 134 */         v = view.findViewById(R.id.title);
/* 135 */         if (v instanceof TextView)
/* 136 */           holder.mTitleView = (TextView)v; 
/* 137 */         view.setTag(R.id.mediastorephotoadapter, holder);
/*     */       } 
/*     */     } 
/* 140 */     return holder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 145 */     changeCursor(null);
/* 146 */     if (this.mMediaInfoCursor != null) {
/* 147 */       this.mMediaInfoCursor.close();
/* 148 */       this.mMediaInfoCursor = null;
/*     */     } 
/* 150 */     super.finalize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onContentChanged() {
/* 155 */     createBitmapCache(false);
/* 156 */     this.mQueryHandler.requery();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bitmap getItem(int position) {
/* 166 */     Bitmap result = null;
/*     */     
/* 168 */     getMediaInfo(position, this.info);
/* 169 */     if (this.info.mediaType == 1) {
/*     */       
/*     */       try {
/* 172 */         result = getImageThumbnail(this.mCr, this.mHashCode, getItemId(position), this.mThumbnailWidth, this.mThumbnailHeight);
/* 173 */       } catch (FileNotFoundException e) {
/* 174 */         Log.w(TAG, e);
/* 175 */       } catch (IOException e) {
/* 176 */         Log.w(TAG, e);
/*     */       } 
/* 178 */     } else if (this.info.mediaType == 3) {
/*     */       
/*     */       try {
/* 181 */         result = getVideoThumbnail(this.mCr, this.mHashCode, getItemId(position), this.mThumbnailWidth, this.mThumbnailHeight);
/* 182 */       } catch (FileNotFoundException e) {
/* 183 */         Log.w(TAG, e);
/* 184 */       } catch (IOException e) {
/* 185 */         Log.w(TAG, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 191 */     return result;
/*     */   }
/*     */   
/*     */   public int getPositionFromId(long id) {
/* 195 */     int result = -1;
/* 196 */     int n = getCount();
/* 197 */     MediaStoreHelper.MediaInfo info = new MediaStoreHelper.MediaInfo();
/* 198 */     for (int i = 0; i < n; i++) {
/* 199 */       getMediaInfo(i, info);
/* 200 */       if (info.id == id) {
/* 201 */         result = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 205 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public Bitmap getImage(int position, int width, int height) throws FileNotFoundException, IOException { return MediaStoreHelper.getImage(this.mCr, getItemId(position), width, height); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public MediaStoreHelper.MediaInfo getMediaInfo(int position) { return getMediaInfo(position, null); }
/*     */ 
/*     */   
/*     */   public synchronized MediaStoreHelper.MediaInfo getMediaInfo(int position, MediaStoreHelper.MediaInfo info) {
/* 236 */     MediaStoreHelper.MediaInfo _info = (info != null) ? info : new MediaStoreHelper.MediaInfo();
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
/* 252 */     if (this.mMediaInfoCursor == null) {
/* 253 */       this.mMediaInfoCursor = this.mCr.query(MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mSelection, this.mSelectionArgs, null);
/*     */     }
/*     */ 
/*     */     
/* 257 */     if (this.mMediaInfoCursor.moveToPosition(position)) {
/* 258 */       _info.loadFromCursor(this.mMediaInfoCursor);
/*     */     }
/* 260 */     return _info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThumbnailSize(int size) {
/* 268 */     if (this.mThumbnailWidth != size || this.mThumbnailHeight != size) {
/* 269 */       this.mThumbnailWidth = this.mThumbnailHeight = size;
/* 270 */       createBitmapCache(true);
/* 271 */       onContentChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThumbnailSize(int width, int height) {
/* 281 */     if (this.mThumbnailWidth != width || this.mThumbnailHeight != height) {
/* 282 */       this.mThumbnailWidth = width;
/* 283 */       this.mThumbnailHeight = height;
/* 284 */       createBitmapCache(true);
/* 285 */       onContentChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setShowTitle(boolean showTitle) {
/* 290 */     if (this.mShowTitle != showTitle) {
/* 291 */       this.mShowTitle = showTitle;
/* 292 */       onContentChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 297 */   public boolean getShowTitle() { return this.mShowTitle; }
/*     */ 
/*     */ 
/*     */   
/* 301 */   public int getMediaType() { return this.mMediaType % 3; }
/*     */ 
/*     */   
/*     */   public void setMediaType(int media_type) {
/* 305 */     if (this.mMediaType != media_type % 3) {
/* 306 */       this.mMediaType = media_type % 3;
/* 307 */       onContentChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroy() {
/* 316 */     if (sThumbnailCache != null) {
/* 317 */       sThumbnailCache.evictAll();
/* 318 */       sThumbnailCache = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MyAsyncQueryHandler extends AsyncQueryHandler { private final MediaStoreAdapter mAdapter;
/*     */     
/*     */     public MyAsyncQueryHandler(ContentResolver cr, MediaStoreAdapter adapter) {
/* 325 */       super(cr);
/* 326 */       this.mAdapter = adapter;
/*     */     }
/*     */     
/*     */     public void requery() {
/* 330 */       synchronized (this.mAdapter) {
/* 331 */         if (this.mAdapter.mMediaInfoCursor != null) {
/* 332 */           this.mAdapter.mMediaInfoCursor.close();
/* 333 */           this.mAdapter.mMediaInfoCursor = null;
/*     */         } 
/* 335 */         this.mAdapter.mSelection = MediaStoreHelper.SELECTIONS[this.mAdapter.mMediaType % 3];
/* 336 */         this.mAdapter.mSelectionArgs = null;
/* 337 */         startQuery(0, this.mAdapter, MediaStoreHelper.QUERY_URI, MediaStoreHelper.PROJ_MEDIA, this.mAdapter
/* 338 */             .mSelection, this.mAdapter.mSelectionArgs, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
/* 345 */       Cursor oldCursor = this.mAdapter.swapCursor(cursor);
/* 346 */       if (oldCursor != null && !oldCursor.isClosed()) {
/* 347 */         oldCursor.close();
/*     */       }
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"NewApi"})
/*     */   private final void createBitmapCache(boolean clear) {
/* 357 */     if (clear && sThumbnailCache != null) {
/* 358 */       clearBitmapCache(hashCode());
/*     */     }
/* 360 */     if (sThumbnailCache == null) {
/*     */       
/* 362 */       int cacheSize = 1048576 * this.mMemClass / 8;
/* 363 */       sThumbnailCache = new LruCache<String, Bitmap>(cacheSize)
/*     */         {
/*     */           protected int sizeOf(String key, Bitmap bitmap)
/*     */           {
/* 367 */             return bitmap.getRowBytes() * bitmap.getHeight();
/*     */           }
/*     */         };
/*     */     } 
/* 371 */     ThreadPool.preStartAllCoreThreads();
/*     */   }
/*     */   
/*     */   private static void clearBitmapCache(int hashCode) {
/* 375 */     if (sThumbnailCache != null) {
/* 376 */       if (hashCode != 0) {
/*     */         
/* 378 */         Map<String, Bitmap> snapshot = sThumbnailCache.snapshot();
/* 379 */         String key_prefix = String.format(Locale.US, "%d_", new Object[] { Integer.valueOf(hashCode) });
/* 380 */         Set<String> keys = snapshot.keySet();
/* 381 */         for (String key : keys) {
/* 382 */           if (key.startsWith(key_prefix))
/*     */           {
/* 384 */             sThumbnailCache.remove(key);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 389 */         sThumbnailCache.evictAll();
/*     */       } 
/* 391 */       System.gc();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 396 */   private static String getKey(long hashCode, long id) { return String.format(Locale.US, "%d_%d", new Object[] { Long.valueOf(hashCode), Long.valueOf(id) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Bitmap getImageThumbnail(ContentResolver cr, long hashCode, long id, int requestWidth, int requestHeight) throws FileNotFoundException, IOException {
/* 403 */     String key = getKey(hashCode, id);
/* 404 */     Bitmap result = (Bitmap)sThumbnailCache.get(key);
/* 405 */     if (result == null) {
/* 406 */       if (requestWidth <= 0 || requestHeight <= 0) {
/* 407 */         result = MediaStoreHelper.getImage(cr, id, requestWidth, requestHeight);
/*     */       } else {
/* 409 */         BitmapFactory.Options options = null;
/* 410 */         int kind = 3;
/* 411 */         if (requestWidth > 96 || requestHeight > 96 || requestWidth * requestHeight > 16384)
/* 412 */           kind = 1; 
/*     */         try {
/* 414 */           result = MediaStore.Images.Thumbnails.getThumbnail(cr, id, kind, options);
/* 415 */         } catch (Exception exception) {}
/*     */       } 
/*     */ 
/*     */       
/* 419 */       if (result != null)
/*     */       {
/*     */         
/* 422 */         sThumbnailCache.put(key, result);
/*     */       }
/*     */     } 
/*     */     
/* 426 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Bitmap getVideoThumbnail(ContentResolver cr, long hashCode, long id, int requestWidth, int requestHeight) throws FileNotFoundException, IOException {
/* 433 */     String key = getKey(hashCode, id);
/* 434 */     Bitmap result = (Bitmap)sThumbnailCache.get(key);
/* 435 */     if (result == null) {
/* 436 */       BitmapFactory.Options options = null;
/* 437 */       int kind = 3;
/* 438 */       if (requestWidth > 96 || requestHeight > 96 || requestWidth * requestHeight > 16384)
/* 439 */         kind = 1; 
/*     */       try {
/* 441 */         result = MediaStore.Video.Thumbnails.getThumbnail(cr, id, kind, options);
/* 442 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 445 */       if (result != null) {
/*     */ 
/*     */         
/* 448 */         sThumbnailCache.put(key, result);
/*     */       } else {
/* 450 */         Log.w(TAG, "failed to get video thumbnail ofr id=" + id);
/*     */       } 
/*     */     } 
/*     */     
/* 454 */     return result;
/*     */   }
/*     */   
/*     */   private static final class ViewHolder {
/*     */     TextView mTitleView;
/*     */     ImageView mImageView;
/*     */     
/*     */     private ViewHolder() {} }
/*     */   
/*     */   private static class ThumbnailLoaderDrawable extends MediaStoreHelper.LoaderDrawable {
/* 464 */     public ThumbnailLoaderDrawable(ContentResolver cr, int width, int height) { super(cr, width, height); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 469 */     protected MediaStoreHelper.ImageLoader createThumbnailLoader() { return new ThumbnailLoader(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     protected Bitmap checkBitmapCache(int hashCode, long id) { return (Bitmap)sThumbnailCache.get(MediaStoreAdapter.getKey(hashCode, id)); }
/*     */   }
/*     */   
/*     */   private static class ThumbnailLoader
/*     */     extends MediaStoreHelper.ImageLoader
/*     */   {
/* 480 */     public ThumbnailLoader(ThumbnailLoaderDrawable parent) { super(parent); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Bitmap loadBitmap(ContentResolver cr, int mediaType, int hashCode, long id, int requestWidth, int requestHeight) {
/* 485 */       Bitmap result = null;
/*     */       try {
/* 487 */         switch (mediaType) {
/*     */           case 1:
/* 489 */             result = MediaStoreAdapter.getImageThumbnail(cr, hashCode, id, requestWidth, requestHeight);
/*     */             break;
/*     */           case 3:
/* 492 */             result = MediaStoreAdapter.getVideoThumbnail(cr, hashCode, id, requestWidth, requestHeight);
/*     */             break;
/*     */         } 
/* 495 */       } catch (IOException e) {
/* 496 */         Log.w(TAG, e);
/*     */       } 
/* 498 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaStoreAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */