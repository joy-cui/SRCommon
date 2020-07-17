/*     */ package com.serenegiant.widget;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.TypedArray;
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Paint;
/*     */ import android.graphics.RectF;
/*     */ import android.util.AttributeSet;
/*     */ import android.util.DisplayMetrics;
/*     */ import android.view.View;
/*     */ import com.serenegiant.common.R;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrameView
/*     */   extends View
/*     */ {
/*  34 */   private static final String TAG = FrameView.class.getSimpleName();
/*     */   
/*     */   public static final float MAX_SCALE = 10.0F;
/*     */   
/*     */   public static final int FRAME_TYPE_NONE = 0;
/*     */   
/*     */   public static final int FRAME_TYPE_FRAME = 1;
/*     */   
/*     */   public static final int FRAME_TYPE_CROSS_FULL = 2;
/*     */   public static final int FRAME_TYPE_CROSS_QUARTER = 3;
/*     */   public static final int FRAME_TYPE_CIRCLE = 4;
/*     */   public static final int FRAME_TYPE_CROSS_CIRCLE = 5;
/*     */   public static final int FRAME_TYPE_CIRCLE_2 = 6;
/*     */   public static final int FRAME_TYPE_CROSS_CIRCLE2 = 7;
/*     */   public static final int FRAME_TYPE_NUMS = 8;
/*     */   public static final int SCALE_TYPE_NONE = 0;
/*     */   public static final int SCALE_TYPE_INCH = 1;
/*     */   public static final int SCALE_TYPE_MM = 2;
/*     */   public static final int SCALE_TYPE_NUMS = 3;
/*     */   private static final float DEFAULT_FRAME_WIDTH_DP = 3.0F;
/*  54 */   private final Paint mPaint = new Paint();
/*  55 */   private final RectF mBoundsRect = new RectF();
/*     */   
/*     */   private final DisplayMetrics metrics;
/*     */   
/*     */   private final float defaultFrameWidth;
/*     */   private int mFrameType;
/*     */   private int mFrameColor;
/*     */   private float mFrameWidth;
/*  63 */   private float mScale = 1.0F; private int mScaleType; private int mScaleColor; private int mTickColor; private float mScaleWidth; private float mRotation;
/*     */   private float mCenterX;
/*     */   private float mCenterY;
/*     */   private float mWidth;
/*     */   private float mHeight;
/*     */   
/*  69 */   public FrameView(Context context) { this(context, null, 0); }
/*     */   private float mRadius; private float mRadius2; private float mRadius4;
/*     */   private float mRadiusQ;
/*     */   
/*  73 */   public FrameView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
/*     */ 
/*     */   
/*     */   public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
/*  77 */     super(context, attrs, defStyleAttr);
/*  78 */     this.metrics = getContext().getResources().getDisplayMetrics();
/*  79 */     this.defaultFrameWidth = 3.0F * this.metrics.density;
/*  80 */     TypedArray attribs = context.obtainStyledAttributes(attrs, R.styleable.FrameView, defStyleAttr, 0);
/*  81 */     this.mFrameType = attribs.getInt(R.styleable.FrameView_frame_type, 0);
/*  82 */     this.mFrameWidth = attribs.getDimension(R.styleable.FrameView_frame_width, this.defaultFrameWidth);
/*  83 */     this.mFrameColor = attribs.getColor(R.styleable.FrameView_frame_color, -5131855);
/*  84 */     this.mScaleType = attribs.getInt(R.styleable.FrameView_scale_type, 0);
/*  85 */     this.mScaleWidth = attribs.getDimension(R.styleable.FrameView_scale_width, this.mFrameWidth);
/*  86 */     this.mScaleColor = attribs.getColor(R.styleable.FrameView_scale_color, this.mFrameColor);
/*  87 */     this.mTickColor = attribs.getColor(R.styleable.FrameView_tick_color, this.mScaleColor);
/*  88 */     this.mRotation = attribs.getFloat(R.styleable.FrameView_scale_rotation, 0.0F);
/*  89 */     this.mScale = attribs.getFloat(R.styleable.FrameView_scale_scale, 1.0F);
/*  90 */     attribs.recycle();
/*  91 */     attribs = null;
/*  92 */     this.mPaint.setStyle(Paint.Style.STROKE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { super.onMeasure(widthMeasureSpec, heightMeasureSpec); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
/* 103 */     super.onLayout(changed, left, top, right, bottom);
/*     */ 
/*     */     
/* 106 */     float w2 = this.mFrameWidth / 2.0F;
/* 107 */     this.mBoundsRect.set(getPaddingLeft() + w2, getPaddingTop() + w2, (
/* 108 */         getWidth() - getPaddingRight()) - w2, (getHeight() - getPaddingBottom()) - w2);
/*     */     
/* 110 */     this.mCenterX = this.mBoundsRect.centerX();
/* 111 */     this.mCenterY = this.mBoundsRect.centerY();
/* 112 */     this.mWidth = this.mBoundsRect.width();
/* 113 */     this.mHeight = this.mBoundsRect.height();
/* 114 */     this.mRadius = Math.min(this.mWidth, this.mHeight) * 0.9F;
/* 115 */     this.mRadius2 = this.mRadius / 2.0F;
/* 116 */     this.mRadius4 = this.mRadius / 4.0F;
/* 117 */     this.mRadiusQ = (float)(this.mRadius4 / Math.sqrt(2.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDraw(Canvas canvas) {
/* 122 */     super.onDraw(canvas);
/* 123 */     if (this.mFrameType != 0) {
/* 124 */       this.mPaint.setStrokeWidth(this.mFrameWidth);
/* 125 */       this.mPaint.setColor(this.mFrameColor);
/*     */       
/* 127 */       canvas.drawRect(this.mBoundsRect, this.mPaint);
/* 128 */       this.mPaint.setStrokeWidth(this.mScaleWidth);
/* 129 */       this.mPaint.setColor(this.mScaleColor);
/* 130 */       float centerX = this.mCenterX;
/* 131 */       float centerY = this.mCenterY;
/* 132 */       float r2 = this.mRadius2;
/* 133 */       float r4 = this.mRadius4;
/* 134 */       float rq = this.mRadiusQ;
/* 135 */       int saveCount = canvas.save();
/*     */       try {
/* 137 */         canvas.rotate(this.mRotation, centerX, centerY);
/* 138 */         canvas.scale(this.mScale, this.mScale, centerX, centerY);
/* 139 */         switch (this.mFrameType) {
/*     */           case 2:
/* 141 */             switch (this.mScaleType) {
/*     */               case 0:
/* 143 */                 canvas.drawLine(centerX, this.mBoundsRect.top, centerX, this.mBoundsRect.bottom, this.mPaint);
/* 144 */                 canvas.drawLine(this.mBoundsRect.left, centerY, this.mBoundsRect.right, centerY, this.mPaint);
/*     */                 break;
/*     */               case 1:
/* 147 */                 draw_scale_full(canvas, this.mWidth, this.mHeight, this.metrics.xdpi / 10.0F, this.metrics.ydpi / 10.0F, 10);
/*     */                 break;
/*     */               case 2:
/* 150 */                 draw_scale_full(canvas, this.mWidth, this.mHeight, this.metrics.xdpi / 12.7F, this.metrics.ydpi / 12.7F, 5);
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 3:
/* 155 */             switch (this.mScaleType) {
/*     */               case 0:
/* 157 */                 canvas.drawLine(centerX, centerY - r4, centerX, centerY + r4, this.mPaint);
/* 158 */                 canvas.drawLine(centerX - r4, centerY, centerX + r4, centerY, this.mPaint);
/*     */                 break;
/*     */               case 1:
/* 161 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 10.0F, this.metrics.ydpi / 10.0F, 10);
/*     */                 break;
/*     */               case 2:
/* 164 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 12.7F, this.metrics.ydpi / 12.7F, 5);
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 4:
/* 169 */             canvas.drawCircle(this.mCenterX, centerY, r4, this.mPaint);
/*     */             break;
/*     */           case 5:
/* 172 */             switch (this.mScaleType) {
/*     */               case 0:
/* 174 */                 canvas.drawLine(centerX, centerY - r4, centerX, centerY + r4, this.mPaint);
/* 175 */                 canvas.drawLine(centerX - r4, centerY, centerX + r4, centerY, this.mPaint);
/*     */                 break;
/*     */               case 1:
/* 178 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 10.0F, this.metrics.ydpi / 10.0F, 10);
/*     */                 break;
/*     */               case 2:
/* 181 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 12.7F, this.metrics.ydpi / 12.7F, 5);
/*     */                 break;
/*     */             } 
/* 184 */             canvas.drawCircle(centerX, centerY, r4, this.mPaint);
/*     */             break;
/*     */           case 6:
/* 187 */             canvas.drawCircle(centerX, centerY, r4 / 2.0F, this.mPaint);
/* 188 */             canvas.drawCircle(centerX, centerY, r4, this.mPaint);
/*     */             break;
/*     */           case 7:
/* 191 */             switch (this.mScaleType) {
/*     */               case 0:
/* 193 */                 canvas.drawLine(centerX, centerY - r4, centerX, centerY + r4, this.mPaint);
/* 194 */                 canvas.drawLine(centerX - r4, centerY, centerX + r4, centerY, this.mPaint);
/*     */                 break;
/*     */               case 1:
/* 197 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 10.0F, this.metrics.ydpi / 10.0F, 10);
/*     */                 break;
/*     */               case 2:
/* 200 */                 draw_scale_full(canvas, r2, r2, this.metrics.xdpi / 12.7F, this.metrics.ydpi / 12.7F, 5);
/*     */                 break;
/*     */             } 
/* 203 */             canvas.drawCircle(centerX, centerY, r4 / 2.0F, this.mPaint);
/* 204 */             canvas.drawCircle(centerX, centerY, r4, this.mPaint);
/*     */             break;
/*     */         } 
/*     */       } finally {
/* 208 */         canvas.restoreToCount(saveCount);
/*     */       } 
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
/*     */   private void draw_scale_full(Canvas canvas, float width, float height, float step_x, float step_y, int unit) {
/* 223 */     float centerX = this.mCenterX;
/* 224 */     float centerY = this.mCenterY;
/* 225 */     float len4 = (this.mScaleWidth > this.defaultFrameWidth) ? (this.mScaleWidth * 4.0F) : (this.defaultFrameWidth * 4.0F);
/* 226 */     float len2 = (this.mScaleWidth > this.defaultFrameWidth) ? (this.mScaleWidth * 2.0F) : (this.defaultFrameWidth * 2.0F);
/* 227 */     float w2 = width / 2.0F;
/* 228 */     float h2 = height / 2.0F;
/* 229 */     int nx = (int)(w2 / step_x);
/* 230 */     int ny = (int)(h2 / step_y);
/* 231 */     canvas.drawLine(centerX, centerY - h2, centerX, centerY + h2, this.mPaint);
/* 232 */     canvas.drawLine(centerX - w2, centerY, centerX + w2, centerY, this.mPaint);
/* 233 */     this.mPaint.setColor(this.mTickColor);
/* 234 */     for (int i = 0; i < nx; i++) {
/* 235 */       float l = (i % unit == 0) ? len4 : len2;
/* 236 */       float xp = centerX + i * step_x;
/* 237 */       canvas.drawLine(xp, centerY - l, xp, centerY + l, this.mPaint);
/* 238 */       float xm = centerX - i * step_x;
/* 239 */       canvas.drawLine(xm, centerY - l, xm, centerY + l, this.mPaint);
/*     */     } 
/* 241 */     for (int i = 0; i < ny; i++) {
/* 242 */       float l = (i % unit == 0) ? len4 : len2;
/* 243 */       float yp = centerY + i * step_y;
/* 244 */       canvas.drawLine(centerX - l, yp, centerX + l, yp, this.mPaint);
/* 245 */       float ym = centerY - i * step_y;
/* 246 */       canvas.drawLine(centerX - l, ym, centerX + l, ym, this.mPaint);
/*     */     } 
/* 248 */     this.mPaint.setColor(this.mScaleColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrameType(int type) {
/* 256 */     if (this.mFrameType != type && type >= 0 && type < 8) {
/* 257 */       this.mFrameType = type;
/* 258 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   public int getFrameType() { return this.mFrameType; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrameColor(int cl) {
/* 276 */     if (this.mFrameColor != cl) {
/* 277 */       if (this.mFrameColor == this.mScaleColor) {
/* 278 */         setScaleColor(cl);
/*     */       }
/* 280 */       this.mFrameColor = cl;
/* 281 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public int getFrameColor() { return this.mFrameColor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrameWidth(float width) {
/* 299 */     float w = (width <= 1.0F) ? 0.0F : width;
/* 300 */     if (this.mFrameWidth != w && w >= 0.0F) {
/* 301 */       if (this.mFrameWidth == this.mScaleWidth) {
/* 302 */         setScaleWidth(w);
/*     */       }
/* 304 */       this.mFrameWidth = w;
/* 305 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 314 */   public float getFrameWidth() { return this.mFrameWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleColor(int cl) {
/* 323 */     if (this.mScaleColor != cl) {
/* 324 */       if (this.mScaleColor == this.mTickColor) {
/* 325 */         setTickColor(cl);
/*     */       }
/* 327 */       this.mScaleColor = cl;
/* 328 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public int getScaleColor() { return this.mScaleColor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleType(int type) {
/* 345 */     if (this.mScaleType != type && type >= 0 && type < 3) {
/* 346 */       this.mScaleType = type;
/* 347 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public int getScaleType() { return this.mScaleType; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleWidth(float width) {
/* 364 */     float w = (width <= 1.0F) ? 0.0F : width;
/* 365 */     if (this.mScaleWidth != w) {
/* 366 */       this.mScaleWidth = w;
/* 367 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public float getScaleWidth() { return this.mScaleWidth; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTickColor(int cl) {
/* 384 */     if (this.mTickColor != cl) {
/* 385 */       this.mTickColor = cl;
/* 386 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 395 */   public int getTickColor() { return this.mTickColor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(float degree) {
/* 403 */     float d = degree;
/* 404 */     for (; d > 360.0F; d -= 360.0F);
/* 405 */     for (; d < -360.0F; d += 360.0F);
/* 406 */     if (this.mRotation != d) {
/* 407 */       this.mRotation = d;
/* 408 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 417 */   public float getRotation() { return this.mRotation; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(float scale) {
/* 425 */     if (this.mScale != scale && scale > 0.0F && scale <= 10.0F) {
/* 426 */       this.mScale = scale;
/* 427 */       postInvalidate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 436 */   public float getScale() { return this.mScale; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\FrameView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */