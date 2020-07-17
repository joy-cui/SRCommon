/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.graphics.Matrix;
/*     */ import android.graphics.Rect;
/*     */ import android.graphics.RectF;
/*     */ import android.graphics.SurfaceTexture;
/*     */ import android.os.SystemClock;
/*     */ import android.util.AttributeSet;
/*     */ import android.view.MotionEvent;
/*     */ import android.view.ViewConfiguration;
/*     */ import com.serenegiant.common.R;
/*     */ import com.serenegiant.glutils.IRendererCommon;
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
/*     */ public class ZoomAspectScaledTextureView
/*     */   extends AspectScaledTextureView
/*     */   implements IRendererCommon
/*     */ {
/*     */   private boolean mHandleTouchEvent = true;
/*     */   private static final int STATE_NON = 0;
/*     */   private static final int STATE_WAITING = 1;
/*     */   private static final int STATE_DRAGING = 2;
/*     */   private static final int STATE_CHECKING = 3;
/*     */   private static final int STATE_ZOOMING = 4;
/*     */   private static final int STATE_ROTATING = 5;
/*     */   private static final float DEFAULT_MAX_SCALE = 8.0F;
/*     */   private static final float DEFAULT_MIN_SCALE = 0.8F;
/*     */   private static final float DEFAULT_SCALE = 1.0F;
/*     */   private static final float MIN_DISTANCE = 15.0F;
/*     */   private static final float MIN_DISTANCE_SQUARE = 225.0F;
/*     */   private static final float MOVE_LIMIT_RATE = 0.2F;
/*  89 */   private static final int CHECK_TIMEOUT = ViewConfiguration.getTapTimeout() + ViewConfiguration.getLongPressTimeout();
/*  90 */   private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout() * 2;
/*  91 */   private static final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float TO_DEGREE = 57.29578F;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float EPS = 0.1F;
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected final Matrix mDefaultMatrix = new Matrix();
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean mImageMatrixChanged;
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected final float[] mMatrixCache = new float[9];
/*     */ 
/*     */ 
/*     */   
/* 116 */   private final Matrix mSavedImageMatrix = new Matrix();
/*     */ 
/*     */ 
/*     */   
/* 120 */   private final RectF mLimitRect = new RectF();
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final LineSegment[] mLimitSegments = new LineSegment[4];
/*     */ 
/*     */ 
/*     */   
/* 128 */   private final RectF mImageRect = new RectF();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   private final float[] mTrans = new float[8];
/*     */ 
/*     */   
/*     */   private int mPrimaryId;
/*     */ 
/*     */   
/*     */   private int mSecondaryId;
/*     */ 
/*     */   
/*     */   private float mPrimaryX;
/*     */ 
/*     */   
/*     */   private float mPrimaryY;
/*     */ 
/*     */   
/*     */   private float mSecondX;
/*     */   
/*     */   private float mSecondY;
/*     */   
/*     */   private float mPivotX;
/*     */   
/*     */   private float mPivotY;
/*     */   
/*     */   private float mTouchDistance;
/*     */   
/*     */   private float mCurrentDegrees;
/*     */   
/*     */   private boolean mIsRotating;
/*     */   
/* 162 */   protected final float mMaxScale = 8.0F;
/*     */ 
/*     */ 
/*     */   
/* 166 */   private float mMinScale = 0.8F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   private int mState = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private Runnable mWaitImageReset;
/*     */ 
/*     */   
/*     */   private Runnable mStartCheckRotate;
/*     */ 
/*     */   
/* 181 */   private int mMirrorMode = 0;
/*     */ 
/*     */ 
/*     */   
/* 185 */   public ZoomAspectScaledTextureView(Context context) { this(context, null, 0); }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public ZoomAspectScaledTextureView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */   
/*     */   public ZoomAspectScaledTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
/* 193 */     super(context, attrs, defStyleAttr);
/* 194 */     TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZoomAspectScaledTextureView, defStyleAttr, 0);
/*     */     try {
/* 196 */       this.mHandleTouchEvent = a.getBoolean(R.styleable.ZoomAspectScaledTextureView_handle_touch_event, true);
/*     */     } finally {
/* 198 */       a.recycle();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"ClickableViewAccessibility"})
/*     */   public boolean onTouchEvent(MotionEvent event) {
/* 206 */     if (!this.mHandleTouchEvent) {
/* 207 */       return super.onTouchEvent(event);
/*     */     }
/*     */     
/* 210 */     int actionCode = event.getActionMasked();
/*     */     
/* 212 */     switch (actionCode) {
/*     */       
/*     */       case 0:
/* 215 */         startWaiting(event);
/* 216 */         return true;
/*     */ 
/*     */       
/*     */       case 5:
/* 220 */         switch (this.mState) {
/*     */           case 1:
/* 222 */             removeCallbacks(this.mWaitImageReset);
/*     */           
/*     */           case 2:
/* 225 */             if (event.getPointerCount() > 1) {
/* 226 */               startCheck(event);
/* 227 */               return true;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */       
/*     */       case 2:
/* 236 */         switch (this.mState) {
/*     */           case 1:
/* 238 */             if (checkTouchMoved(event)) {
/* 239 */               removeCallbacks(this.mWaitImageReset);
/* 240 */               setState(2);
/* 241 */               return true;
/*     */             } 
/*     */             break;
/*     */           case 2:
/* 245 */             if (processDrag(event))
/* 246 */               return true; 
/*     */             break;
/*     */           case 3:
/* 249 */             if (checkTouchMoved(event)) {
/* 250 */               startZoom(event);
/* 251 */               return true;
/*     */             } 
/*     */             break;
/*     */           case 4:
/* 255 */             if (processZoom(event))
/* 256 */               return true; 
/*     */             break;
/*     */           case 5:
/* 259 */             if (processRotate(event)) {
/* 260 */               return true;
/*     */             }
/*     */             break;
/*     */         } 
/*     */         
/*     */         break;
/*     */       case 1:
/*     */       case 3:
/* 268 */         removeCallbacks(this.mWaitImageReset);
/* 269 */         removeCallbacks(this.mStartCheckRotate);
/* 270 */         if (actionCode == 1 && this.mState == 1) {
/* 271 */           long downTime = SystemClock.uptimeMillis() - event.getDownTime();
/* 272 */           if (downTime > LONG_PRESS_TIMEOUT) {
/* 273 */             performLongClick();
/* 274 */           } else if (downTime < TAP_TIMEOUT) {
/* 275 */             performClick();
/*     */           } 
/*     */         } 
/*     */       
/*     */       case 6:
/* 280 */         setState(0);
/*     */         break;
/*     */     } 
/* 283 */     return super.onTouchEvent(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
/* 292 */     super.onSurfaceTextureAvailable(surface, width, height);
/* 293 */     setMirror(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
/* 301 */     super.onSurfaceTextureSizeChanged(surface, width, height);
/* 302 */     applyMirrorMode();
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
/*     */   public void setMirror(int mirror) {
/* 320 */     if (this.mMirrorMode != mirror) {
/* 321 */       this.mMirrorMode = mirror;
/* 322 */       applyMirrorMode();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 329 */   public int getMirror() { return this.mMirrorMode; }
/*     */ 
/*     */ 
/*     */   
/* 333 */   public void setEnableHandleTouchEvent(boolean enabled) { this.mHandleTouchEvent = enabled; }
/*     */ 
/*     */ 
/*     */   
/* 337 */   public void reset() { init(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 344 */     this.mState = -1; setState(0);
/*     */     
/* 346 */     this.mMinScale = 0.8F;
/* 347 */     this.mCurrentDegrees = 0.0F;
/* 348 */     this.mIsRotating = (Math.abs((int)(this.mCurrentDegrees / 360.0F) * 360.0F - this.mCurrentDegrees) > 0.1F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     int view_width = getWidth();
/* 356 */     int view_height = getHeight();
/* 357 */     Rect tmp = new Rect();
/* 358 */     getDrawingRect(tmp);
/* 359 */     this.mLimitRect.set(tmp);
/* 360 */     this.mLimitRect.inset((int)(0.2F * view_width), (int)(0.2F * view_height));
/* 361 */     this.mLimitSegments[0] = null;
/* 362 */     this.mImageRect.set(0.0F, 0.0F, tmp.width(), tmp.height());
/* 363 */     super.init();
/* 364 */     this.mDefaultMatrix.set(this.mImageMatrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void setState(int state) {
/* 373 */     if (this.mState != state) {
/* 374 */       this.mState = state;
/*     */       
/* 376 */       getTransform(this.mSavedImageMatrix);
/* 377 */       if (!this.mImageMatrix.equals(this.mSavedImageMatrix)) {
/* 378 */         this.mImageMatrix.set(this.mSavedImageMatrix);
/* 379 */         this.mImageMatrixChanged = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void startWaiting(MotionEvent event) {
/* 389 */     this.mPrimaryId = 0;
/* 390 */     this.mSecondaryId = -1;
/* 391 */     this.mPrimaryX = this.mSecondX = event.getX();
/* 392 */     this.mPrimaryY = this.mSecondY = event.getY();
/* 393 */     if (this.mWaitImageReset == null) this.mWaitImageReset = new WaitImageReset(); 
/* 394 */     postDelayed(this.mWaitImageReset, CHECK_TIMEOUT);
/* 395 */     setState(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean processDrag(MotionEvent event) {
/* 403 */     float dx = event.getX() - this.mPrimaryX;
/* 404 */     float dy = event.getY() - this.mPrimaryY;
/*     */ 
/*     */ 
/*     */     
/* 408 */     this.mTrans[6] = this.mImageRect.left; this.mTrans[0] = this.mImageRect.left;
/* 409 */     this.mTrans[3] = this.mImageRect.top; this.mTrans[1] = this.mImageRect.top;
/* 410 */     this.mTrans[7] = this.mImageRect.bottom; this.mTrans[5] = this.mImageRect.bottom;
/* 411 */     this.mTrans[4] = this.mImageRect.right; this.mTrans[2] = this.mImageRect.right;
/* 412 */     this.mImageMatrix.mapPoints(this.mTrans);
/* 413 */     for (int i = 0; i < 8; i += 2) {
/* 414 */       this.mTrans[i] = this.mTrans[i] + dx;
/* 415 */       this.mTrans[i + 1] = this.mTrans[i + 1] + dy;
/*     */     } 
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
/* 429 */     boolean canMove = (this.mLimitRect.contains(this.mTrans[0], this.mTrans[1]) || this.mLimitRect.contains(this.mTrans[2], this.mTrans[3]) || this.mLimitRect.contains(this.mTrans[4], this.mTrans[5]) || this.mLimitRect.contains(this.mTrans[6], this.mTrans[7]) || ptInPoly(this.mLimitRect.left, this.mLimitRect.top, this.mTrans) || ptInPoly(this.mLimitRect.right, this.mLimitRect.top, this.mTrans) || ptInPoly(this.mLimitRect.right, this.mLimitRect.bottom, this.mTrans) || ptInPoly(this.mLimitRect.left, this.mLimitRect.bottom, this.mTrans));
/* 430 */     if (!canMove) {
/*     */ 
/*     */       
/* 433 */       if (this.mLimitSegments[0] == null) {
/* 434 */         this.mLimitSegments[0] = new LineSegment(this.mLimitRect.left, this.mLimitRect.top, this.mLimitRect.right, this.mLimitRect.top);
/* 435 */         this.mLimitSegments[1] = new LineSegment(this.mLimitRect.right, this.mLimitRect.top, this.mLimitRect.right, this.mLimitRect.bottom);
/* 436 */         this.mLimitSegments[2] = new LineSegment(this.mLimitRect.right, this.mLimitRect.bottom, this.mLimitRect.left, this.mLimitRect.bottom);
/* 437 */         this.mLimitSegments[3] = new LineSegment(this.mLimitRect.left, this.mLimitRect.bottom, this.mLimitRect.left, this.mLimitRect.top);
/*     */       } 
/* 439 */       LineSegment side = new LineSegment(this.mTrans[0], this.mTrans[1], this.mTrans[2], this.mTrans[3]);
/* 440 */       canMove = checkIntersect(side, this.mLimitSegments);
/* 441 */       if (!canMove) {
/* 442 */         side.set(this.mTrans[2], this.mTrans[3], this.mTrans[4], this.mTrans[5]);
/* 443 */         canMove = checkIntersect(side, this.mLimitSegments);
/* 444 */         if (!canMove) {
/* 445 */           side.set(this.mTrans[4], this.mTrans[5], this.mTrans[6], this.mTrans[7]);
/* 446 */           canMove = checkIntersect(side, this.mLimitSegments);
/* 447 */           if (!canMove) {
/* 448 */             side.set(this.mTrans[6], this.mTrans[7], this.mTrans[0], this.mTrans[1]);
/* 449 */             canMove = checkIntersect(side, this.mLimitSegments);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 454 */     if (canMove) {
/*     */ 
/*     */ 
/*     */       
/* 458 */       if (!this.mIsRotating) {
/* 459 */         float left = Math.min(Math.min(this.mTrans[0], this.mTrans[2]), Math.min(this.mTrans[4], this.mTrans[6]));
/* 460 */         float right = Math.max(Math.max(this.mTrans[0], this.mTrans[2]), Math.max(this.mTrans[4], this.mTrans[6]));
/* 461 */         float top = Math.min(Math.min(this.mTrans[1], this.mTrans[3]), Math.min(this.mTrans[5], this.mTrans[7]));
/* 462 */         float bottom = Math.max(Math.max(this.mTrans[1], this.mTrans[3]), Math.max(this.mTrans[5], this.mTrans[7]));
/*     */         
/* 464 */         if (right < this.mLimitRect.left) {
/* 465 */           dx = this.mLimitRect.left - right;
/* 466 */         } else if (left + 0.1F > this.mLimitRect.right) {
/* 467 */           dx = this.mLimitRect.right - left - 0.1F;
/*     */         } 
/* 469 */         if (bottom < this.mLimitRect.top) {
/* 470 */           dy = this.mLimitRect.top - bottom;
/* 471 */         } else if (top + 0.1F > this.mLimitRect.bottom) {
/* 472 */           dy = this.mLimitRect.bottom - top - 0.1F;
/*     */         } 
/*     */       } 
/* 475 */       if (dx != 0.0F || dy != 0.0F)
/*     */       {
/*     */         
/* 478 */         if (this.mImageMatrix.postTranslate(dx, dy)) {
/*     */           
/* 480 */           this.mImageMatrixChanged = true;
/*     */           
/* 482 */           setTransform(this.mImageMatrix);
/*     */         } 
/*     */       }
/*     */     } 
/* 486 */     this.mPrimaryX = event.getX();
/* 487 */     this.mPrimaryY = event.getY();
/* 488 */     return canMove;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void startCheck(MotionEvent event) {
/* 497 */     if (event.getPointerCount() > 1) {
/*     */       
/* 499 */       this.mPrimaryId = event.getPointerId(0);
/* 500 */       this.mPrimaryX = event.getX(0);
/* 501 */       this.mPrimaryY = event.getY(0);
/*     */       
/* 503 */       this.mSecondaryId = event.getPointerId(1);
/* 504 */       this.mSecondX = event.getX(1);
/* 505 */       this.mSecondY = event.getY(1);
/*     */       
/* 507 */       float dx = this.mSecondX - this.mPrimaryX;
/* 508 */       float dy = this.mSecondY - this.mPrimaryY;
/* 509 */       float distance = (float)Math.hypot(dx, dy);
/* 510 */       if (distance < 15.0F) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 515 */       this.mTouchDistance = distance;
/*     */       
/* 517 */       this.mPivotX = (this.mPrimaryX + this.mSecondX) / 2.0F;
/* 518 */       this.mPivotY = (this.mPrimaryY + this.mSecondY) / 2.0F;
/*     */       
/* 520 */       if (this.mStartCheckRotate == null)
/* 521 */         this.mStartCheckRotate = new StartCheckRotate(); 
/* 522 */       postDelayed(this.mStartCheckRotate, CHECK_TIMEOUT);
/* 523 */       setState(3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void startZoom(MotionEvent event) {
/* 534 */     removeCallbacks(this.mStartCheckRotate);
/* 535 */     setState(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean processZoom(MotionEvent event) {
/* 545 */     restoreMatrix();
/*     */     
/* 547 */     float currentScale = getMatrixScale();
/*     */     
/* 549 */     float scale = calcScale(event);
/*     */     
/* 551 */     float tmpScale = scale * currentScale;
/* 552 */     if (tmpScale < this.mMinScale)
/*     */     {
/* 554 */       return false; } 
/* 555 */     if (tmpScale > 8.0F)
/*     */     {
/* 557 */       return false;
/*     */     }
/*     */     
/* 560 */     if (this.mImageMatrix.postScale(scale, scale, this.mPivotX, this.mPivotY)) {
/*     */       
/* 562 */       this.mImageMatrixChanged = true;
/*     */       
/* 564 */       setTransform(this.mImageMatrix);
/*     */     } 
/* 566 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final float calcScale(MotionEvent event) {
/* 576 */     float dx = event.getX(0) - event.getX(1);
/* 577 */     float dy = event.getY(0) - event.getY(1);
/* 578 */     float distance = (float)Math.hypot(dx, dy);
/*     */     
/* 580 */     return distance / this.mTouchDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean checkTouchMoved(MotionEvent event) {
/* 589 */     boolean result = true;
/* 590 */     int ix0 = event.findPointerIndex(this.mPrimaryId);
/* 591 */     int ix1 = event.findPointerIndex(this.mSecondaryId);
/* 592 */     if (ix0 >= 0) {
/*     */       
/* 594 */       float x = event.getX(ix0) - this.mPrimaryX;
/* 595 */       float y = event.getY(ix0) - this.mPrimaryY;
/* 596 */       if (x * x + y * y < 225.0F)
/*     */       {
/* 598 */         if (ix1 >= 0) {
/*     */           
/* 600 */           x = event.getX(ix1) - this.mSecondX;
/* 601 */           y = event.getY(ix1) - this.mSecondY;
/* 602 */           if (x * x + y * y < 225.0F)
/*     */           {
/* 604 */             return false;
/*     */           }
/*     */         } else {
/* 607 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/* 611 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean processRotate(MotionEvent event) {
/* 620 */     if (checkTouchMoved(event)) {
/*     */       
/* 622 */       restoreMatrix();
/* 623 */       this.mCurrentDegrees = calcAngle(event);
/* 624 */       this.mIsRotating = (Math.abs((int)(this.mCurrentDegrees / 360.0F) * 360.0F - this.mCurrentDegrees) > 0.1F);
/* 625 */       if (this.mIsRotating && this.mImageMatrix.postRotate(this.mCurrentDegrees, this.mPivotX, this.mPivotY)) {
/*     */         
/* 627 */         this.mImageMatrixChanged = true;
/*     */         
/* 629 */         setTransform(this.mImageMatrix);
/* 630 */         return true;
/*     */       } 
/*     */     } 
/* 633 */     return false;
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
/*     */   private final float calcAngle(MotionEvent event) {
/* 648 */     int ix0 = event.findPointerIndex(this.mPrimaryId);
/* 649 */     int ix1 = event.findPointerIndex(this.mSecondaryId);
/* 650 */     float angle = 0.0F;
/* 651 */     if (ix0 >= 0 && ix1 >= 0) {
/*     */       
/* 653 */       float x0 = this.mSecondX - this.mPrimaryX;
/* 654 */       float y0 = this.mSecondY - this.mPrimaryY;
/*     */       
/* 656 */       float x1 = event.getX(ix1) - event.getX(ix0);
/* 657 */       float y1 = event.getY(ix1) - event.getY(ix0);
/*     */       
/* 659 */       double s = ((x0 * x0 + y0 * y0) * (x1 * x1 + y1 * y1));
/* 660 */       double cos = dotProduct(x0, y0, x1, y1) / Math.sqrt(s);
/* 661 */       angle = 57.29578F * (float)Math.acos(cos) * Math.signum(crossProduct(x0, y0, x1, y1));
/*     */     } 
/* 663 */     return angle;
/*     */   }
/*     */ 
/*     */   
/* 667 */   private static final float dotProduct(float x0, float y0, float x1, float y1) { return x0 * x1 + y0 * y1; }
/*     */ 
/*     */ 
/*     */   
/* 671 */   private static final float crossProduct(float x0, float y0, float x1, float y1) { return x0 * y1 - x1 * y0; }
/*     */ 
/*     */ 
/*     */   
/* 675 */   private static final float crossProduct(Vector v1, Vector v2) { return v1.x * v2.y - v2.x * v1.y; }
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
/*     */   private static final boolean ptInPoly(float x, float y, float[] poly) {
/* 687 */     int n = poly.length & Integer.MAX_VALUE;
/*     */     
/* 689 */     if (n < 6) return false; 
/* 690 */     boolean result = true;
/* 691 */     Vector v1 = new Vector();
/* 692 */     Vector v2 = new Vector();
/* 693 */     for (int i = 0; i < n; i += 2) {
/* 694 */       v1.set(x, y).dec(poly[i], poly[i + 1]);
/* 695 */       if (i + 2 < n) { v2.set(poly[i + 2], poly[i + 3]); }
/* 696 */       else { v2.set(poly[0], poly[1]); }
/* 697 */        v2.dec(poly[i], poly[i + 1]);
/* 698 */       if (crossProduct(v1, v2) > 0.0F) {
/*     */         
/* 700 */         result = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 704 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Vector
/*     */   {
/*     */     public float x;
/*     */     
/*     */     public float y;
/*     */ 
/*     */     
/*     */     public Vector() {}
/*     */ 
/*     */     
/* 718 */     public Vector(float x, float y) { set(x, y); }
/*     */     
/*     */     public Vector set(float x, float y) {
/* 721 */       this.x = x;
/* 722 */       this.y = y;
/* 723 */       return this;
/*     */     }
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
/* 747 */     public Vector sub(Vector other) { return new Vector(this.x - other.x, this.y - other.y); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Vector dec(float x, float y) {
/* 758 */       this.x -= x;
/* 759 */       this.y -= y;
/* 760 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LineSegment {
/*     */     public final Vector p1;
/*     */     public final Vector p2;
/*     */     
/*     */     public LineSegment(float x0, float y0, float x1, float y1) {
/* 769 */       this.p1 = new Vector(x0, y0);
/* 770 */       this.p2 = new Vector(x1, y1);
/*     */     }
/*     */     public LineSegment set(float x0, float y0, float x1, float y1) {
/* 773 */       this.p1.set(x0, y0);
/* 774 */       this.p2.set(x1, y1);
/* 775 */       return this;
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
/*     */   private static final boolean checkIntersect(LineSegment seg, LineSegment[] segs) {
/* 790 */     boolean result = false;
/* 791 */     int n = (segs != null) ? segs.length : 0;
/*     */     
/* 793 */     Vector a = seg.p2.sub(seg.p1);
/*     */     
/* 795 */     for (int i = 0; i < n; i++) {
/* 796 */       Vector c = (segs[i]).p1.sub(seg.p1);
/* 797 */       Vector d = (segs[i]).p2.sub(seg.p1);
/* 798 */       result = (crossProduct(a, c) * crossProduct(a, d) < 0.1F);
/* 799 */       if (result) {
/* 800 */         Vector b = (segs[i]).p2.sub((segs[i]).p1);
/* 801 */         c = seg.p1.sub((segs[i]).p1);
/* 802 */         d = seg.p2.sub((segs[i]).p1);
/* 803 */         result = (crossProduct(b, c) * crossProduct(b, d) < 0.1F);
/* 804 */         if (result) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 809 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final float getMatrixScale() {
/* 818 */     updateMatrixCache();
/* 819 */     float scale = Math.min(this.mMatrixCache[0], this.mMatrixCache[0]);
/* 820 */     if (scale <= 0.0F)
/*     */     {
/* 822 */       return 1.0F;
/*     */     }
/* 824 */     return scale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void restoreMatrix() {
/* 831 */     this.mImageMatrix.set(this.mSavedImageMatrix);
/* 832 */     this.mImageMatrixChanged = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean updateMatrixCache() {
/* 839 */     if (this.mImageMatrixChanged) {
/* 840 */       this.mImageMatrix.getValues(this.mMatrixCache);
/* 841 */       this.mImageMatrixChanged = false;
/* 842 */       return true;
/*     */     } 
/* 844 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyMirrorMode() {
/* 853 */     switch (this.mMirrorMode) {
/*     */       case 1:
/* 855 */         setScaleX(-1.0F);
/* 856 */         setScaleY(1.0F);
/*     */         return;
/*     */       case 2:
/* 859 */         setScaleX(1.0F);
/* 860 */         setScaleY(-1.0F);
/*     */         return;
/*     */       case 3:
/* 863 */         setScaleX(-1.0F);
/* 864 */         setScaleY(-1.0F);
/*     */         return;
/*     */     } 
/*     */     
/* 868 */     setScaleX(1.0F);
/* 869 */     setScaleY(1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class WaitImageReset
/*     */     implements Runnable
/*     */   {
/*     */     private WaitImageReset() {}
/*     */ 
/*     */     
/* 880 */     public void run() { ZoomAspectScaledTextureView.this.init(); }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class StartCheckRotate
/*     */     implements Runnable
/*     */   {
/*     */     private StartCheckRotate() {}
/*     */     
/*     */     public void run() {
/* 890 */       if (ZoomAspectScaledTextureView.this.mState == 3)
/* 891 */         ZoomAspectScaledTextureView.this.setState(5); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\ZoomAspectScaledTextureView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */