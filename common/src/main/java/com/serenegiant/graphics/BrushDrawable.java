//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.graphics;

import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

public class BrushDrawable extends ShapeDrawable {
    private static final boolean DEBUG = false;
    private static final String TAG = "BrushDrawable";
    public static final int ERASE_ELIPSE = -1;
    public static final int ERASE_LINE = -2;
    public static final int ERASE_TRIANGLE = -3;
    public static final int ERASE_RECTANGLE = -4;
    public static final int BRUSH_ELIPSE = 1;
    public static final int BRUSH_LINE = 2;
    public static final int BRUSH_TRIANGLE = 3;
    public static final int BRUSH_RECTANGLE = 4;
    private final PointF mPivot;
    private final Paint mPaint;
    private final DrawFilter mDrawFilter;
    private final Xfermode mClearXfermode;
    private Shader mShader;
    private float mRotation;

    public BrushDrawable(int type, int width, int height) {
        this(type, width, height, 0, 0);
    }

    public BrushDrawable(int type, int width, int height, int clearflags, int setFlags) {
        this.mPivot = new PointF();
        this.mRotation = 0.0F;
        this.mPaint = new Paint();
        this.mDrawFilter = new PaintFlagsDrawFilter(clearflags, setFlags);
        this.mClearXfermode = new PorterDuffXfermode(Mode.CLEAR);
        this.init(100, 100);
        this.setType(type, width, height);
    }

    private final void init(int width, int height) {
        this.setIntrinsicWidth(width > 0 ? width : 100);
        this.setIntrinsicHeight(height > 0 ? height : 100);
        this.mPivot.set((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
    }

    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        canvas.rotate(this.mRotation, this.mPivot.x, this.mPivot.y);
        int count = canvas.save();

        try {
            this.mPaint.setShader(this.mShader);
            super.onDraw(shape, canvas, paint);
        } finally {
            canvas.restoreToCount(count);
        }

    }

    public void setPivot(float pivotX, float pivotY) {
        if (this.mPivot.x != pivotX || this.mPivot.y != pivotY) {
            this.mPivot.set(pivotX, pivotY);
            this.invalidateSelf();
        }

    }

    public PointF getPivot() {
        return this.mPivot;
    }

    public float getPivotX() {
        return this.mPivot.x;
    }

    public float getPivotY() {
        return this.mPivot.y;
    }

    public float getRotation() {
        Shape shape = this.getShape();
        return shape instanceof BaseShape ? ((BaseShape)shape).getRotation() : this.mRotation;
    }

    public void setRotation(float rotation) {
        Shape shape = this.getShape();
        if (shape instanceof BaseShape) {
            ((BaseShape)shape).setRotation(rotation);
            this.mRotation = 0.0F;
        } else if (this.mRotation != rotation) {
            this.mRotation = rotation;
        }

        this.invalidateSelf();
    }

    public void setType(int type, int width, int height) {
        Shape shape = null;
        switch(type) {
            case 1:
                shape = new OvalShape();
            case 2:
            default:
                break;
            case 3:
                shape = new IsoscelesTriangleShape((float)width, (float)height);
                break;
            case 4:
                shape = new BaseShape((float)width, (float)height);
        }

        if (shape != null) {
            this.mRotation = 0.0F;
            ((Shape)shape).resize((float)width, (float)height);
            this.setShape((Shape)shape);
        }

    }

    public void setColor(int color) {
        Paint paint = this.getPaint();
        paint.setColor(color);
        this.invalidateSelf();
    }

    public void setPaintAlpha(int alpha) {
        this.getPaint().setAlpha(alpha);
    }

    public int getPaintAlpha() {
        return this.getPaint().getAlpha();
    }

    public Shader getShader() {
        return this.getPaint().getShader();
    }

    public void setShader(Shader shader) {
        this.getPaint().setShader(shader);
        this.invalidateSelf();
    }

    public void setPaintStyle(Style style) {
        this.getPaint().setStyle(style);
        this.invalidateSelf();
    }
}
