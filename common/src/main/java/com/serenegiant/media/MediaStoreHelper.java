/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.content.ContentResolver;
/*     */ import android.content.ContentUris;
/*     */ import android.database.Cursor;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.BitmapFactory;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.ColorFilter;
/*     */ import android.graphics.Matrix;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.drawable.Drawable;
/*     */ import android.net.Uri;
/*     */ import android.os.ParcelFileDescriptor;
/*     */ import android.provider.MediaStore;
/*     */ import android.support.annotation.NonNull;
/*     */ import com.serenegiant.utils.ThreadPool;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.FutureTask;
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
/*     */ @TargetApi(16)
/*     */ public class MediaStoreHelper
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  50 */   private static final String TAG = MediaStoreHelper.class.getSimpleName();
/*     */   
/*     */   public static final int MEDIA_ALL = 0;
/*     */   
/*     */   public static final int MEDIA_IMAGE = 1;
/*     */   public static final int MEDIA_VIDEO = 2;
/*     */   protected static final int MEDIA_TYPE_NUM = 3;
/*  57 */   protected static final String[] PROJ_MEDIA = new String[] { "_id", "title", "media_type", "mime_type", "_data", "_display_name", "width", "height" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String SELECTION_MEDIA_ALL = "media_type=1 OR media_type=3";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String SELECTION_MEDIA_IMAGE = "media_type=1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String SELECTION_MEDIA_VIDEO = "media_type=3";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected static final String[] SELECTIONS = new String[] { "media_type=1 OR media_type=3", "media_type=1", "media_type=3" };
/*     */   
/*     */   protected static final int PROJ_INDEX_ID = 0;
/*     */   
/*     */   protected static final int PROJ_INDEX_TITLE = 1;
/*     */   
/*     */   protected static final int PROJ_INDEX_MEDIA_TYPE = 2;
/*     */   
/*     */   protected static final int PROJ_INDEX_MIME_TYPE = 3;
/*     */   
/*     */   protected static final int PROJ_INDEX_DATA = 4;
/*     */   protected static final int PROJ_INDEX_DISPLAY_NAME = 5;
/*     */   protected static final int PROJ_INDEX_WIDTH = 6;
/*     */   protected static final int PROJ_INDEX_HEIGHT = 7;
/* 101 */   protected static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
/*     */   
/*     */   public static class MediaInfo {
/*     */     public long id;
/*     */     public String data;
/*     */     public String title;
/*     */     public String mime;
/*     */     public String displayName;
/*     */     public int mediaType;
/*     */     public int width;
/*     */     public int height;
/*     */     
/*     */     protected MediaInfo loadFromCursor(Cursor cursor) {
/* 114 */       if (cursor != null) {
/* 115 */         this.id = cursor.getLong(0);
/* 116 */         this.data = cursor.getString(4);
/* 117 */         this.title = cursor.getString(1);
/* 118 */         this.mime = cursor.getString(3);
/* 119 */         this.displayName = cursor.getString(5);
/* 120 */         this.mediaType = cursor.getInt(2);
/*     */         try {
/* 122 */           this.width = cursor.getInt(6);
/* 123 */           this.height = cursor.getInt(7);
/* 124 */         } catch (Exception exception) {}
/*     */       } 
/*     */       
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     public String toString() { return String.format("MediaInfo(title=%s,displayName=%s, mediaType=%s,mime=%s,data=%s)", new Object[] { this.title, this.displayName, mediaType(), this.mime, this.data }); }
/*     */ 
/*     */     
/*     */     private String mediaType() {
/* 136 */       switch (this.mediaType) {
/*     */         case 0:
/* 138 */           return "none";
/*     */         case 1:
/* 140 */           return "image";
/*     */         case 2:
/* 142 */           return "audio";
/*     */         case 3:
/* 144 */           return "video";
/*     */         case 4:
/* 146 */           return "playlist";
/*     */       } 
/* 148 */       return String.format(Locale.US, "unknown:%d", new Object[] { Integer.valueOf(this.mediaType) });
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class LoaderDrawable
/*     */     extends Drawable
/*     */     implements Runnable
/*     */   {
/*     */     private final ContentResolver mContentResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     private final Paint mPaint = new Paint();
/* 177 */     private final Paint mDebugPaint = new Paint();
/* 178 */     private final Matrix mDrawMatrix = new Matrix();
/*     */     private Bitmap mBitmap;
/* 180 */     private int mRotation = 0;
/*     */     
/*     */     private ImageLoader mLoader;
/*     */     
/*     */     public LoaderDrawable(ContentResolver cr, int width, int height) {
/* 185 */       this.mContentResolver = cr;
/* 186 */       this.mDebugPaint.setColor(-65536);
/* 187 */       this.mDebugPaint.setTextSize(18.0F);
/* 188 */       this.mWidth = width;
/* 189 */       this.mHeight = height;
/*     */     }
/*     */     private final int mWidth; private final int mHeight;
/*     */     
/*     */     protected void onBoundsChange(Rect bounds) {
/* 194 */       super.onBoundsChange(bounds);
/* 195 */       updateDrawMatrix(getBounds());
/*     */     }
/*     */ 
/*     */     
/*     */     public void draw(@NonNull Canvas canvas) {
/* 200 */       Rect bounds = getBounds();
/* 201 */       if (this.mBitmap != null) {
/* 202 */         canvas.save();
/* 203 */         canvas.clipRect(bounds);
/* 204 */         canvas.concat(this.mDrawMatrix);
/* 205 */         canvas.rotate(this.mRotation, bounds.centerX(), bounds.centerY());
/* 206 */         canvas.drawBitmap(this.mBitmap, 0.0F, 0.0F, this.mPaint);
/* 207 */         canvas.restore();
/*     */       } else {
/* 209 */         this.mPaint.setColor(-3355444);
/* 210 */         canvas.drawRect(bounds, this.mPaint);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void updateDrawMatrix(Rect bounds) {
/*     */       float scale;
/* 219 */       if (this.mBitmap == null || bounds.isEmpty()) {
/* 220 */         this.mDrawMatrix.reset();
/*     */         
/*     */         return;
/*     */       } 
/* 224 */       float dwidth = this.mBitmap.getWidth();
/* 225 */       float dheight = this.mBitmap.getHeight();
/* 226 */       int vwidth = bounds.width();
/* 227 */       int vheight = bounds.height();
/*     */ 
/*     */       
/* 230 */       int dx = 0, dy = 0;
/*     */ 
/*     */       
/* 233 */       if (dwidth * vheight > vwidth * dheight) {
/* 234 */         scale = vheight / dheight;
/* 235 */         dx = (int)((vwidth - dwidth * scale) * 0.5F + 0.5F);
/*     */       } else {
/* 237 */         scale = vwidth / dwidth;
/* 238 */         dy = (int)((vheight - dheight * scale) * 0.5F + 0.5F);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 249 */       this.mDrawMatrix.setScale(scale, scale);
/* 250 */       this.mDrawMatrix.postTranslate(dx, dy);
/*     */       
/* 252 */       invalidateSelf();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAlpha(int alpha) {
/* 257 */       int oldAlpha = this.mPaint.getAlpha();
/* 258 */       if (alpha != oldAlpha) {
/* 259 */         this.mPaint.setAlpha(alpha);
/* 260 */         invalidateSelf();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setColorFilter(ColorFilter cf) {
/* 266 */       this.mPaint.setColorFilter(cf);
/* 267 */       invalidateSelf();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 272 */     public int getIntrinsicWidth() { return this.mWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     public int getIntrinsicHeight() { return this.mHeight; }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getOpacity() {
/* 282 */       Bitmap bm = this.mBitmap;
/* 283 */       return (bm == null || bm.hasAlpha() || this.mPaint.getAlpha() < 255) ? -3 : -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     public void run() { setBitmap(this.mLoader.getBitmap()); }
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
/*     */     public void startLoad(int media_type, int hashCode, long id) {
/* 305 */       if (this.mLoader != null) {
/* 306 */         this.mLoader.cancelLoad();
/*     */       }
/*     */       
/* 309 */       Bitmap newBitmap = checkBitmapCache(hashCode, id);
/* 310 */       if (newBitmap == null) {
/*     */         
/* 312 */         this.mBitmap = null;
/*     */         
/* 314 */         this.mLoader = createThumbnailLoader();
/* 315 */         this.mLoader.startLoad(media_type, hashCode, id);
/*     */       } else {
/* 317 */         setBitmap(newBitmap);
/*     */       } 
/* 319 */       invalidateSelf();
/*     */     }
/*     */     
/*     */     private void setBitmap(Bitmap bitmap) {
/* 323 */       if (bitmap != this.mBitmap) {
/* 324 */         this.mBitmap = bitmap;
/* 325 */         updateDrawMatrix(getBounds());
/*     */       } 
/*     */     }
/*     */     
/*     */     protected abstract ImageLoader createThumbnailLoader();
/*     */     
/*     */     protected abstract Bitmap checkBitmapCache(int param1Int, long param1Long); }
/*     */   
/*     */   protected static abstract class ImageLoader implements Runnable {
/*     */     protected final LoaderDrawable mParent;
/*     */     private final FutureTask<Bitmap> mTask;
/*     */     private int mMediaType;
/*     */     private int mHashCode;
/*     */     private long mId;
/*     */     private Bitmap mBitmap;
/*     */     
/*     */     public ImageLoader(LoaderDrawable parent) {
/* 342 */       this.mParent = parent;
/* 343 */       this.mTask = (FutureTask)new FutureTask<>(this, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void startLoad(int media_type, int hashCode, long id) {
/* 352 */       this.mMediaType = media_type;
/* 353 */       this.mHashCode = hashCode;
/* 354 */       this.mId = id;
/* 355 */       this.mBitmap = null;
/* 356 */       ThreadPool.queueEvent(this.mTask);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     public void cancelLoad() { this.mTask.cancel(true); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract Bitmap loadBitmap(ContentResolver param1ContentResolver, int param1Int1, int param1Int2, long param1Long, int param1Int3, int param1Int4);
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       long id;
/*     */       int hashCode, mediaType;
/* 373 */       synchronized (this) {
/* 374 */         mediaType = this.mMediaType;
/* 375 */         hashCode = this.mHashCode;
/* 376 */         id = this.mId;
/*     */       } 
/* 378 */       if (!this.mTask.isCancelled()) {
/* 379 */         this.mBitmap = loadBitmap(this.mParent.mContentResolver, mediaType, hashCode, id, this.mParent.mWidth, this.mParent.mHeight);
/*     */       }
/* 381 */       if (this.mTask.isCancelled() || id != this.mId || this.mBitmap == null) {
/*     */         return;
/*     */       }
/*     */       
/* 385 */       this.mParent.scheduleSelf(this.mParent, 0L);
/*     */     }
/*     */ 
/*     */     
/* 389 */     public Bitmap getBitmap() { return this.mBitmap; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final Bitmap getImage(ContentResolver cr, long id, int requestWidth, int requestHeight) throws IOException {
/* 396 */     Bitmap result = null;
/* 397 */     ParcelFileDescriptor pfd = cr.openFileDescriptor(
/* 398 */         ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id), "r");
/* 399 */     if (pfd != null) {
/*     */       try {
/* 401 */         BitmapFactory.Options options = new BitmapFactory.Options();
/*     */         
/* 403 */         options.inJustDecodeBounds = true;
/* 404 */         BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), null, options);
/*     */         
/* 406 */         options.inSampleSize = calcSampleSize(options, requestWidth, requestHeight);
/* 407 */         options.inJustDecodeBounds = false;
/* 408 */         result = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), null, options);
/*     */       } finally {
/* 410 */         pfd.close();
/*     */       } 
/*     */     }
/* 413 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int calcSampleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {
/* 424 */     int imageWidth = options.outWidth;
/* 425 */     int imageHeight = options.outHeight;
/* 426 */     int reqWidth = requestWidth, reqHeight = requestHeight;
/* 427 */     if (requestWidth <= 0)
/* 428 */       if (requestHeight > 0) {
/* 429 */         reqWidth = (int)((imageWidth * requestHeight) / imageHeight);
/*     */       } else {
/* 431 */         reqWidth = imageWidth;
/*     */       }  
/* 433 */     if (requestHeight <= 0)
/* 434 */       if (requestWidth > 0) {
/* 435 */         reqHeight = (int)((imageHeight * requestWidth) / imageHeight);
/*     */       } else {
/* 437 */         reqHeight = imageHeight;
/*     */       }  
/* 439 */     int inSampleSize = 1;
/* 440 */     if (imageHeight > reqHeight || imageWidth > reqWidth) {
/* 441 */       if (imageWidth > imageHeight) {
/* 442 */         inSampleSize = Math.round(imageHeight / reqHeight);
/*     */       } else {
/* 444 */         inSampleSize = Math.round(imageWidth / reqWidth);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 449 */     return inSampleSize;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaStoreHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */