//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.serenegiant.graphics.BitmapHelper;
import com.serenegiant.graphics.ShaderDrawable;

public class ColorPickerView extends View {
    private static final float SQRT2 = (float)Math.sqrt(2.0D);
    private static final float PI = 3.1415927F;
    private static final float BORDER_WIDTH_PX = 1.0F;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;
    private static final float RECTANGLE_TRACKER_OFFSET_DP = 2.0F;
    private static final int BORDER_COLOR = -9539986;
    private static final int TRACKER_COLOR = -14935012;
    private static final int DEFAULT_SELECTED_RADIUS = 8;
    private static final int STATE_IDLE = 0;
    private static final int STATE_COLOR = 1;
    private static final int STATE_ALPHA = 2;
    private static final int STATE_VAL = 3;
    private boolean mShowAlphaSlider;
    private boolean mShowValSlider;
    private boolean mShowSelectedColor;
    private final float RECTANGLE_TRACKER_OFFSET;
    private final float DENSITY;
    private final int[] COLORS;
    private final float[] HSV;
    private final RectF mDrawingRect;
    private final Paint mBorderPaint;
    private final Paint mTrackerPaint;
    private final ShaderDrawable mAlphaDrawable;
    private final Shader mAlphaShader;
    private final Paint mPaint;
    private final Paint mGradientPaint;
    private final Paint mSelectionPaint;
    private final RectF mSelectionRect;
    private final PointF mSelected;
    private final RectF mSliderRect;
    private final Paint mAlphaPaint;
    private final RectF mAlphaRect;
    private final Paint mValPaint;
    private final RectF mValRect;
    private int mState;
    private ColorPickerListener mColorPickerListener;
    private int slider_width;
    private int center_x;
    private int center_y;
    private final float SELECTED_RADIUS;
    private int radius;
    private int radius2;
    private int mColor;
    private int mAlpha;
    private float mVal;
    private float mHue;
    private float mSat;

    public ColorPickerView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mShowAlphaSlider = true;
        this.mShowValSlider = true;
        this.mShowSelectedColor = true;
        this.COLORS = new int[360];
        this.HSV = new float[3];
        this.mDrawingRect = new RectF();
        this.mBorderPaint = new Paint();
        this.mTrackerPaint = new Paint();
        this.mPaint = new Paint(1);
        this.mGradientPaint = new Paint(1);
        this.mSelectionPaint = new Paint(1);
        this.mSelectionRect = new RectF();
        this.mSelected = new PointF();
        this.mSliderRect = new RectF();
        this.mAlphaPaint = new Paint();
        this.mAlphaRect = new RectF();
        this.mValPaint = new Paint();
        this.mValRect = new RectF();
        this.mState = 0;
        this.slider_width = 32;
        this.mColor = -1;
        this.mAlpha = 255;
        this.mVal = 0.0F;
        this.mHue = 360.0F;
        this.mSat = 0.0F;
        this.DENSITY = context.getResources().getDisplayMetrics().density;
        this.RECTANGLE_TRACKER_OFFSET = 2.0F * this.DENSITY;
        this.SELECTED_RADIUS = 8.0F * this.DENSITY;
        this.mAlphaShader = new BitmapShader(BitmapHelper.makeCheckBitmap(), TileMode.REPEAT, TileMode.REPEAT);
        this.mAlphaDrawable = new ShaderDrawable(6, 0);
        this.mAlphaDrawable.setShader(this.mAlphaShader);
        this.radius = 0;
        this.internalSetColor(this.mColor, false);
        this.setHueColorArray(this.mAlpha, this.COLORS);
        this.mPaint.setShader(new SweepGradient(0.0F, 0.0F, this.COLORS, (float[])null));
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeWidth(0.0F);
        this.mSelectionPaint.setColor(this.mColor);
        this.mSelectionPaint.setStrokeWidth(5.0F);
        this.mTrackerPaint.setColor(-14935012);
        this.mTrackerPaint.setStyle(Style.STROKE);
        this.mTrackerPaint.setStrokeWidth(2.0F * this.DENSITY);
        this.mTrackerPaint.setAntiAlias(true);
    }

    protected void onDraw(Canvas canvas) {
        this.mSelectionPaint.setStyle(Style.FILL);
        if (this.mShowSelectedColor) {
            this.mSelectionPaint.setShader(this.mAlphaShader);
            canvas.drawArc(this.mSelectionRect, 0.0F, 90.0F, true, this.mSelectionPaint);
            this.mSelectionPaint.setShader((Shader)null);
            this.mSelectionPaint.setColor(this.mColor);
            canvas.drawArc(this.mSelectionRect, 0.0F, 90.0F, true, this.mSelectionPaint);
        }

        int count = canvas.save();

        try {
            canvas.translate((float)this.center_x, (float)this.center_y);
            this.mSelectionPaint.setShader(this.mAlphaShader);
            canvas.drawCircle(0.0F, 0.0F, (float)this.radius, this.mSelectionPaint);
            canvas.drawCircle(0.0F, 0.0F, (float)this.radius, this.mPaint);
            canvas.drawCircle(0.0F, 0.0F, (float)this.radius, this.mGradientPaint);
        } finally {
            canvas.restoreToCount(count);
        }

        this.mSelectionPaint.setShader((Shader)null);
        this.mSelectionPaint.setColor(~this.mColor | -16777216);
        this.mSelectionPaint.setStyle(Style.STROKE);
        canvas.drawCircle(this.mSelected.x, this.mSelected.y, this.SELECTED_RADIUS, this.mSelectionPaint);
        this.drawAlphaPanel(canvas);
        this.drawValPanel(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch(widthMode) {
            case 0:
                width = 100;
            case -2147483648:
            case 1073741824:
            default:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
                switch(heightMode) {
                    case 0:
                        height = 100;
                    case -2147483648:
                    case 1073741824:
                    default:
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
                        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
        }
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.getWidth() != 0 && this.getHeight() != 0) {
            int width = this.getWidth();
            int height = this.getHeight();
            this.mDrawingRect.set(0.0F, 0.0F, (float)width, (float)height);
            int dimeter = Math.min(width, height);
            this.slider_width = dimeter / 10;
            if (this.slider_width < 32) {
                this.slider_width = (int)(32.0F * this.DENSITY);
            }

            int space = this.slider_width + (int)(16.0F * this.DENSITY);
            dimeter -= space;
            this.radius = dimeter >>> 1;
            this.radius2 = this.radius * this.radius;
            this.center_x = width - (this.mShowValSlider ? this.slider_width : 0) >>> 1;
            this.center_y = height - (this.mShowAlphaSlider ? this.slider_width : 0) >>> 1;
            int selection_radius = (int)Math.sqrt((double)(this.center_x * this.center_x + this.center_y * this.center_y)) - this.radius;
            this.mSelectionRect.set((float)(-selection_radius), (float)(-selection_radius), (float)selection_radius, (float)selection_radius);
            this.mGradientPaint.setShader(new RadialGradient(0.0F, 0.0F, (float)this.radius, -1, 16777215, TileMode.CLAMP));
            this.setupAlphaRect();
            this.setUpValRect();
            this.setColor(this.mAlpha, this.mHue, this.mSat, this.mVal, true);
        }

    }

    public int getColor() {
        return this.mColor;
    }

    public float getHue() {
        return this.mHue;
    }

    public float getSat() {
        return this.mSat;
    }

    public float getVal() {
        return this.mVal;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = x - (float)this.center_x;
        float dy = y - (float)this.center_y;
        float d = dx * dx + dy * dy;
        boolean inColorCircle = d <= (float)this.radius2;
        switch(event.getAction()) {
            case 0:
                if (inColorCircle) {
                    this.mState = 1;
                } else if (this.mAlphaRect.contains(x, y)) {
                    this.mState = 2;
                } else {
                    if (!this.mValRect.contains(x, y)) {
                        return false;
                    }

                    this.mState = 3;
                }
            case 2:
                boolean modified = false;
                switch(this.mState) {
                    case 1:
                        float angle = (float)Math.atan2((double)dy, (double)dx);
                        float unit = angle / 6.2831855F;
                        if (unit < 0.0F) {
                            ++unit;
                        }

                        this.setColor(this.mAlpha, 360.0F - unit * 360.0F, (float)Math.sqrt((double)d) / (float)this.radius, this.mVal, true);
                        modified = true;
                        break;
                    case 2:
                        if (modified = this.trackAlpha(x, y)) {
                            this.setHueColorArray(this.mAlpha, this.COLORS);
                            this.mPaint.setShader(new SweepGradient(0.0F, 0.0F, this.COLORS, (float[])null));
                        }
                        break;
                    case 3:
                        if (modified = this.trackVal(x, y)) {
                            this.setHueColorArray(this.mAlpha, this.COLORS);
                            this.mPaint.setShader(new SweepGradient(0.0F, 0.0F, this.COLORS, (float[])null));
                        }
                }

                if (modified && this.mColorPickerListener != null) {
                    this.mColorPickerListener.onColorChanged(this, this.mColor);
                }
                break;
            case 1:
                if (this.mColorPickerListener != null) {
                    this.mColorPickerListener.onColorChanged(this, this.mColor);
                }

                this.mState = 0;
        }

        return true;
    }

    public void setColor(int cl) {
        this.internalSetColor(cl, true);
    }

    protected void internalSetColor(int cl, boolean force) {
        int alpha = Color.alpha(cl);
        int red = Color.red(cl);
        int blue = Color.blue(cl);
        int green = Color.green(cl);
        Color.RGBToHSV(red, green, blue, this.HSV);
        this.setColor(alpha, this.HSV[0], this.HSV[1], this.HSV[2], force);
    }

    protected void setColor(int alpha, float hue, float sat, float val, boolean force) {
        if (sat > 1.0F) {
            sat = 1.0F;
        }

        if (force || this.mAlpha != alpha || this.mHue != hue || this.mSat != sat || this.mVal != val) {
            this.mAlpha = alpha;
            this.mHue = hue;
            this.mSat = sat;
            this.mVal = val;
            this.mColor = this.HSVToColor(alpha, hue, sat, val);
            if (this.radius > 0) {
                float r = (float)this.radius * sat;
                float d = hue / 180.0F * 3.1415927F;
                this.mSelected.set((float)this.center_x + r * (float)Math.cos((double)d), (float)this.center_y - r * (float)Math.sin((double)d));
                this.postInvalidate();
            }
        }

    }

    public void setColorPickerListener(ColorPickerListener listener) {
        this.mColorPickerListener = listener;
    }

    public ColorPickerListener getColorPickerListener() {
        return this.mColorPickerListener;
    }

    public void setShowSelectedColor(boolean showSelectedColor) {
        this.mShowSelectedColor = showSelectedColor;
    }

    public boolean getShowSelectedColor() {
        return this.mShowSelectedColor;
    }

    public void showAlpha(boolean showAlpha) {
        if (this.mShowAlphaSlider != showAlpha) {
            this.mShowAlphaSlider = showAlpha;
            this.postInvalidate();
        }

    }

    public boolean isShowAlpha() {
        return this.mShowAlphaSlider;
    }

    public void setShowVal(boolean showVal) {
        if (this.mShowValSlider != showVal) {
            this.mShowValSlider = showVal;
            this.postInvalidate();
        }

    }

    public boolean isShowVal() {
        return this.mShowValSlider;
    }

    private final int HSVToColor(int alpha, float hue, float saturation, float value) {
        if (hue >= 360.0F) {
            hue = 359.99F;
        } else if (hue < 0.0F) {
            hue = 0.0F;
        }

        if (saturation > 1.0F) {
            saturation = 1.0F;
        } else if (saturation < 0.0F) {
            saturation = 0.0F;
        }

        if (value > 1.0F) {
            value = 1.0F;
        } else if (value < 0.0F) {
            value = 0.0F;
        }

        this.HSV[0] = hue;
        this.HSV[1] = saturation;
        this.HSV[2] = value;
        return Color.HSVToColor(alpha, this.HSV);
    }

    private final int[] setHueColorArray(int alpha, int[] colors) {
        int n = colors.length;
        float resolution = 360.0F / (float)n;
        this.HSV[1] = 1.0F;
        this.HSV[2] = this.mVal;
        int count = 0;

        for(float i = 360.0F; (double)i >= 0.0D && count < n; ++count) {
            this.HSV[0] = i;
            colors[count] = Color.HSVToColor(alpha, this.HSV);
            i -= resolution;
        }

        this.HSV[0] = 0.0F;
        colors[n - 1] = Color.HSVToColor(alpha, this.HSV);
        return colors;
    }

    private final void drawTrackerHorizontal(Canvas canvas, float x, float y, float height) {
        float width = 4.0F * this.DENSITY / 2.0F;
        this.mSliderRect.left = x - width;
        this.mSliderRect.right = x + width;
        this.mSliderRect.top = y - this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.bottom = y + height + this.RECTANGLE_TRACKER_OFFSET;
        this.mTrackerPaint.setColor(-1842205);
        this.mTrackerPaint.setStyle(Style.FILL);
        canvas.drawRoundRect(this.mSliderRect, 2.0F, 2.0F, this.mTrackerPaint);
        this.mTrackerPaint.setColor(-14935012);
        this.mTrackerPaint.setStyle(Style.STROKE);
        canvas.drawRoundRect(this.mSliderRect, 2.0F, 2.0F, this.mTrackerPaint);
    }

    private final void drawTrackerVertical(Canvas canvas, float x, float y, float width) {
        float height = 4.0F * this.DENSITY / 2.0F;
        this.mSliderRect.left = x - this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.right = x + width + this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.top = y - height;
        this.mSliderRect.bottom = y + height;
        this.mTrackerPaint.setColor(-1842205);
        this.mTrackerPaint.setStyle(Style.FILL);
        canvas.drawRoundRect(this.mSliderRect, 2.0F, 2.0F, this.mTrackerPaint);
        this.mTrackerPaint.setColor(-14935012);
        this.mTrackerPaint.setStyle(Style.STROKE);
        canvas.drawRoundRect(this.mSliderRect, 2.0F, 2.0F, this.mTrackerPaint);
    }

    private void setupAlphaRect() {
        if (this.mShowAlphaSlider) {
            RectF dRect = this.mDrawingRect;
            this.mAlphaRect.set(dRect.left + 1.0F, dRect.bottom - (float)this.slider_width + 1.0F, dRect.right - (float)this.slider_width - 1.0F, dRect.bottom - 1.0F - this.RECTANGLE_TRACKER_OFFSET);
        }
    }

    private final void drawAlphaPanel(Canvas canvas) {
        if (this.mShowAlphaSlider) {
            RectF rect = this.mAlphaRect;
            this.mBorderPaint.setColor(-9539986);
            canvas.drawRect(rect.left - 1.0F, rect.top - 1.0F, rect.right + 1.0F, rect.bottom + 1.0F, this.mBorderPaint);
            this.mAlphaPaint.setShader(this.mAlphaShader);
            canvas.drawRect(rect, this.mAlphaPaint);
            int color = this.HSVToColor(255, this.mHue, this.mSat, this.mVal);
            int acolor = this.HSVToColor(0, this.mHue, this.mSat, this.mVal);
            Shader alphaShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, color, acolor, TileMode.CLAMP);
            this.mAlphaPaint.setShader(alphaShader);
            canvas.drawRect(rect, this.mAlphaPaint);
            Point p = this.alphaToPoint(this.mAlpha);
            this.drawTrackerHorizontal(canvas, (float)p.x, (float)p.y, rect.height());
        }
    }

    private final boolean trackAlpha(float x, float y) {
        boolean result = false;
        if (this.mShowAlphaSlider) {
            int alpha = this.pointToAlpha((int)x);
            if (this.mAlpha != alpha) {
                this.setColor(alpha, this.mHue, this.mSat, this.mVal, true);
                result = true;
            }
        }

        return result;
    }

    private final Point alphaToPoint(int alpha) {
        RectF r = this.mAlphaRect;
        float w = r.width();
        return new Point((int)(w - (float)alpha * w / 255.0F + r.left), (int)r.top);
    }

    private final int pointToAlpha(int x) {
        RectF rect = this.mAlphaRect;
        int width = (int)rect.width();
        if ((float)x < rect.left) {
            x = 0;
        } else if ((float)x > rect.right) {
            x = width;
        } else {
            x -= (int)rect.left;
        }

        return 255 - x * 255 / width;
    }

    private final void setUpValRect() {
        if (this.mShowValSlider) {
            RectF dRect = this.mDrawingRect;
            this.mValRect.set(dRect.right - (float)this.slider_width + 1.0F, dRect.top + 1.0F + this.RECTANGLE_TRACKER_OFFSET, dRect.right - 1.0F - this.RECTANGLE_TRACKER_OFFSET, dRect.bottom - 1.0F - (float)(this.mShowAlphaSlider ? 16 + this.slider_width : 0));
        }
    }

    private final void drawValPanel(Canvas canvas) {
        if (this.mShowValSlider) {
            RectF rect = this.mValRect;
            this.mBorderPaint.setColor(-9539986);
            canvas.drawRect(rect.left - 1.0F, rect.top - 1.0F, rect.right + 1.0F, rect.bottom + 1.0F, this.mBorderPaint);
            int color = this.HSVToColor(255, this.mHue, this.mSat, 1.0F);
            int acolor = this.HSVToColor(255, this.mHue, this.mSat, 0.0F);
            Shader mValShader = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom, color, acolor, TileMode.CLAMP);
            this.mValPaint.setShader(mValShader);
            canvas.drawRect(rect, this.mValPaint);
            Point p = this.valToPoint(this.mVal);
            this.drawTrackerVertical(canvas, (float)p.x, (float)p.y, rect.width());
        }
    }

    private final boolean trackVal(float x, float y) {
        boolean result = false;
        float val = this.pointToVal(y);
        if (this.mVal != val) {
            this.setColor(this.mAlpha, this.mHue, this.mSat, val, true);
            result = true;
        }

        return result;
    }

    private final Point valToPoint(float val) {
        RectF rect = this.mValRect;
        float height = rect.height();
        Point p = new Point();
        p.y = (int)(height - val * height + rect.top);
        p.x = (int)rect.left;
        return p;
    }

    private final float pointToVal(float y) {
        RectF rect = this.mValRect;
        float height = rect.height();
        if (y < rect.top) {
            y = 0.0F;
        } else if (y > rect.bottom) {
            y = height;
        } else {
            y -= rect.top;
        }

        return 1.0F - y * 1.0F / height;
    }

    public interface ColorPickerListener {
        void onColorChanged(ColorPickerView var1, int var2);
    }
}
