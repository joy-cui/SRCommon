//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.serenegiant.common.R.styleable;
import com.serenegiant.utils.BuildCheck;

public class TwoPainViewGroup extends FrameLayout {
    private static final String TAG = TwoPainViewGroup.class.getSimpleName();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int MODE_SPLIT = 0;
    public static final int MODE_SELECT_1 = 1;
    public static final int MODE_SELECT_2 = 2;
    public static final int MODE_SINGLE_1 = 3;
    public static final int MODE_SINGLE_2 = 4;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;
    private static final float DEFAULT_SUB_WINDOW_SCALE = 0.2F;
    private static final int DEFAULT_CHILD_GRAVITY = 17;
    private final Object mSync;
    private int mOrientation;
    private int mDisplayMode;
    private boolean mEnableSubWindow;
    private boolean mFlipChildPos;
    private float mSubWindowScale;
    private ObjectAnimator mAnimator;
    private View mChild1;
    private View mChild2;
    private final Rect mChildRect;
    private final AnimatorListener mAnimatorListener;

    public TwoPainViewGroup(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public TwoPainViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoPainViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSync = new Object();
        this.mChildRect = new Rect();
        this.mAnimatorListener = new AnimatorListener() {
            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                synchronized(TwoPainViewGroup.this.mSync) {
                    TwoPainViewGroup.this.mAnimator = null;
                }

                TwoPainViewGroup.this.requestLayout();
            }

            public void onAnimationCancel(Animator animator) {
                synchronized(TwoPainViewGroup.this.mSync) {
                    TwoPainViewGroup.this.mAnimator = null;
                }

                TwoPainViewGroup.this.requestLayout();
            }

            public void onAnimationRepeat(Animator animator) {
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.TwoPainViewGroup, defStyleAttr, 0);
        this.mOrientation = a.getInt(styleable.TwoPainViewGroup_srhuijianorientation, 0);
        this.mDisplayMode = a.getInt(styleable.TwoPainViewGroup_displayMode, 0);
        this.mEnableSubWindow = a.getBoolean(styleable.TwoPainViewGroup_enableSubWindow, true);
        this.mFlipChildPos = a.getBoolean(styleable.TwoPainViewGroup_flipChildPos, false);
        this.mSubWindowScale = a.getFloat(styleable.TwoPainViewGroup_subWindowScale, 0.2F);
        if (this.mSubWindowScale <= 0.0F || this.mSubWindowScale >= 1.0F) {
            this.mSubWindowScale = 0.2F;
        }

        a.recycle();
    }

    public void addView(View child, int index, LayoutParams params) {
        if (this.getChildCount() >= 2) {
            throw new IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        } else {
            super.addView(child, index, params);
            int n = this.getChildCount();
            if (n > 0 && this.mChild1 == null) {
                this.mChild1 = this.getChildAt(0);
            }

            if (n > 1 && this.mChild2 == null) {
                this.mChild2 = this.getChildAt(1);
            }

        }
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child == this.mChild1) {
            this.mChild1 = null;
        } else if (child == this.mChild2) {
            this.mChild2 = null;
        }

    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TwoPainViewGroup.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TwoPainViewGroup.class.getName());
    }

    public void setOrientation(int orientation) {
        synchronized(this.mSync) {
            if (this.mOrientation != orientation % 2) {
                this.mOrientation = orientation % 2;
                this.startLayout();
            }

        }
    }

    public int getOrientation() {
        synchronized(this.mSync) {
            return this.mOrientation;
        }
    }

    public void setEnableSubWindow(boolean enable) {
        synchronized(this.mSync) {
            if (this.mEnableSubWindow != enable) {
                this.mEnableSubWindow = enable;
                this.startLayout();
            }

        }
    }

    public boolean getEnableSubWindow() {
        synchronized(this.mSync) {
            return this.mEnableSubWindow;
        }
    }

    public void setDisplayMode(int mode) {
        synchronized(this.mSync) {
            if (this.mDisplayMode != mode) {
                this.mDisplayMode = mode;
                this.startLayout();
            }

        }
    }

    public int getDisplayMode() {
        synchronized(this.mSync) {
            return this.mDisplayMode;
        }
    }

    public void setSubWindowScale(float scale) {
        float _scale = scale;
        if (scale <= 0.0F || scale >= 1.0F) {
            _scale = 0.2F;
        }

        synchronized(this.mSync) {
            if (_scale != this.mSubWindowScale) {
                this.mSubWindowScale = _scale;
                this.startLayout();
            }

        }
    }

    public float getSubWindowScale() {
        synchronized(this.mSync) {
            return this.mSubWindowScale;
        }
    }

    public void setFlipChildPos(boolean flip) {
        synchronized(this.mSync) {
            if (flip != this.mFlipChildPos) {
                this.mFlipChildPos = flip;
                this.startLayout();
            }

        }
    }

    public boolean getFlipChildPos() {
        synchronized(this.mSync) {
            return this.mFlipChildPos;
        }
    }

    @SuppressLint("WrongConstant")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean var10000;
        if (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
            var10000 = false;
        } else {
            var10000 = true;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (this.mDisplayMode == 0) {
            if (this.mOrientation == 1) {
                height >>>= 1;
            } else {
                width >>>= 1;
            }
        }

        int childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
        int childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int count = this.getChildCount();

        for(int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                this.measureChildWithMargins(child, childWidthSpec, 0, childHeightSpec, 0);
                if (this.mDisplayMode == 0 || (this.mDisplayMode == 1 || this.mDisplayMode == 3) && child == this.mChild1 || (this.mDisplayMode == 2 || this.mDisplayMode == 3) && child == this.mChild2) {
                    MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                    maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                }

                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        if (this.mDisplayMode == 0) {
            if (this.mOrientation == 1) {
                maxHeight <<= 1;
            } else {
                maxWidth <<= 1;
            }
        }

        maxWidth += this.getPaddingLeft() + this.getPaddingRight();
        maxHeight += this.getPaddingTop() + this.getPaddingBottom();
        maxHeight = Math.max(maxHeight, this.getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, this.getSuggestedMinimumWidth());
        Drawable drawable = this.getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        this.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << 16));
        int maxChildWidth = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
        int maxChildHeight = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n = this.getChildCount();
        if (n == 1) {
            this.callChildMeasure(this.mChild1, maxChildWidth, maxChildHeight, widthMeasureSpec, heightMeasureSpec);
        } else if (n > 0) {
            switch(this.mDisplayMode) {
                case 1:
                case 3:
                    this.onMeasureSelect1(maxChildWidth, maxChildHeight, widthMeasureSpec, heightMeasureSpec);
                    break;
                case 2:
                case 4:
                    this.onMeasureSelect2(maxChildWidth, maxChildHeight, widthMeasureSpec, heightMeasureSpec);
                    break;
                default:
                    this.onMeasureSplit(maxChildWidth, maxChildHeight, widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void onMeasureSplit(int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        switch(this.mOrientation) {
            case 1:
                this.onMeasureVertical(maxWidth, maxHeight, widthMeasureSpec, heightMeasureSpec);
                break;
            default:
                this.onMeasureHorizontal(maxWidth, maxHeight, widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void onMeasureSelect1(int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        this.callChildMeasure(ch1, maxWidth, maxHeight, widthMeasureSpec, heightMeasureSpec);
        if (this.mEnableSubWindow) {
            this.callChildMeasure(ch2, (int)((float)maxWidth * this.mSubWindowScale), (int)((float)maxHeight * this.mSubWindowScale), widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void onMeasureSelect2(int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        this.callChildMeasure(ch2, maxWidth, maxHeight, widthMeasureSpec, heightMeasureSpec);
        if (this.mEnableSubWindow) {
            this.callChildMeasure(ch1, (int)((float)maxWidth * this.mSubWindowScale), (int)((float)maxHeight * this.mSubWindowScale), widthMeasureSpec, heightMeasureSpec);
        }

    }

    private void onMeasureHorizontal(int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        this.callChildMeasure(ch1, maxWidth >>> 1, maxHeight, widthMeasureSpec, heightMeasureSpec);
        this.callChildMeasure(ch2, maxWidth >>> 1, maxHeight, widthMeasureSpec, heightMeasureSpec);
    }

    private void onMeasureVertical(int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        this.callChildMeasure(ch1, maxWidth, maxHeight >>> 1, widthMeasureSpec, heightMeasureSpec);
        this.callChildMeasure(ch2, maxWidth, maxHeight >>> 1, widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("WrongConstant")
    private void callChildMeasure(View child, int maxWidth, int maxHeight, int widthMeasureSpec, int heightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        int height;
        if (lp.width == -1) {
            childHeightMeasureSpec = Math.min(maxWidth, this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - lp.leftMargin - lp.rightMargin);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightMeasureSpec, 1073741824);
        } else {
            childHeightMeasureSpec = getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            height = MeasureSpec.getSize(childHeightMeasureSpec);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(height, maxWidth), MeasureSpec.getMode(childHeightMeasureSpec));
        }

        if (lp.height == -1) {
            height = Math.min(maxHeight, this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - lp.topMargin - lp.bottomMargin);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
        } else {
            height = getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
            int h = MeasureSpec.getSize(height);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(h, maxHeight), MeasureSpec.getMode(height));
        }

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int _left = left + this.getPaddingLeft();
        int _top = top + this.getPaddingTop();
        int _right = right - this.getPaddingRight();
        int _bottom = bottom - this.getPaddingBottom();
        int n = this.getChildCount();
        if (n == 1) {
            this.callChildLayout(this.mChild1, changed, _left, _top, _right, _bottom);
        } else if (n > 0) {
            switch(this.mDisplayMode) {
                case 1:
                case 3:
                    this.onLayoutSelect1(changed, _left, _top, _right, _bottom);
                    break;
                case 2:
                case 4:
                    this.onLayoutSelect2(changed, _left, _top, _right, _bottom);
                    break;
                default:
                    this.onLayoutSplit(changed, _left, _top, _right, _bottom);
            }
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }

    }

    private void onLayoutSplit(boolean changed, int left, int top, int right, int bottom) {
        switch(this.mOrientation) {
            case 1:
                this.onLayoutVertical(changed, left, top, right, bottom);
                break;
            default:
                this.onLayoutHorizontal(changed, left, top, right, bottom);
        }

    }

    private void onLayoutSelect1(boolean changed, int left, int top, int right, int bottom) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        this.callChildLayout(ch1, changed, left - paddingLeft, top - paddingTop, right - paddingLeft, bottom - paddingTop);
        if (this.mEnableSubWindow) {
            int _bottom = ch1.getBottom();
            int _right = ch1.getRight();
            int w = ch2.getMeasuredWidth();
            int h = ch2.getMeasuredHeight();
            switch(this.mOrientation) {
                case 1:
                    this.callChildLayout(ch2, changed, _right - w, _bottom - h, _right, _bottom);
                    break;
                default:
                    this.callChildLayout(ch2, changed, _right - w, _bottom - h, _right, _bottom);
            }
        }

    }

    private void onLayoutSelect2(boolean changed, int left, int top, int right, int bottom) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        this.callChildLayout(ch2, changed, left - paddingLeft, top - paddingTop, right - paddingLeft, bottom - paddingTop);
        if (this.mEnableSubWindow) {
            int _left = ch2.getLeft();
            int _top = ch2.getTop();
            int _right = ch2.getRight();
            int _bottom = ch2.getBottom();
            int w = ch1.getMeasuredWidth();
            int h = ch1.getMeasuredHeight();
            switch(this.mOrientation) {
                case 1:
                    this.callChildLayout(ch1, changed, _right - w, _top, _right, _top + h);
                    break;
                default:
                    this.callChildLayout(ch1, changed, _left, _bottom - h, _left + w, _bottom);
            }
        }

    }

    private void onLayoutHorizontal(boolean changed, int left, int top, int right, int bottom) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        int w2 = right - left >>> 1;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        this.callChildLayout(ch1, changed, left - paddingLeft, top - paddingTop, left - paddingLeft + w2, bottom - paddingTop);
        this.callChildLayout(ch2, changed, left - paddingLeft + w2, top - paddingTop, right - paddingLeft, bottom - paddingTop);
    }

    private void onLayoutVertical(boolean changed, int left, int top, int right, int bottom) {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;
        int h2 = bottom - top >>> 1;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        this.callChildLayout(ch1, changed, left - paddingLeft, top - paddingTop, right - paddingLeft, top - paddingTop + h2);
        this.callChildLayout(ch2, changed, left - paddingLeft, top - paddingTop + h2, right - paddingLeft, bottom - paddingTop);
    }

    @SuppressLint({"NewApi"})
    private void callChildLayout(View child, boolean changed, int left, int top, int right, int bottom) {
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();
        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = 17;
        }

        int layoutDirection = BuildCheck.isAndroid4_2() ? this.getLayoutDirection() : 0;
        int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        int verticalGravity = gravity & 112;
        int childLeft;
        switch(absoluteGravity & 7) {
            case 1:
                childLeft = left + (right - left - width) / 2 + lp.leftMargin - lp.rightMargin;
                break;
            case 2:
            case 3:
            case 4:
            default:
                childLeft = left + lp.leftMargin;
                break;
            case 5:
                childLeft = right - width - lp.rightMargin;
        }

        int childTop;
        switch(verticalGravity) {
            case 16:
                childTop = top + (bottom - top - height) / 2 + lp.topMargin - lp.bottomMargin;
                break;
            case 48:
                childTop = top + lp.topMargin;
                break;
            case 80:
                childTop = bottom - height - lp.bottomMargin;
                break;
            default:
                childTop = top + lp.topMargin;
        }

        child.layout(childLeft, childTop, childLeft + width, childTop + height);
    }

    public void startLayout() {
        if (this.isInEditMode() || this.getChildCount() < 2) {
            this.requestLayout();
        }

        this.post(new Runnable() {
            public void run() {
                TwoPainViewGroup.this.startLayoutOnUI();
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void startLayoutOnUI() {
        View ch1 = this.mFlipChildPos ? this.mChild2 : this.mChild1;
        View ch2 = this.mFlipChildPos ? this.mChild1 : this.mChild2;

        try {
            switch(this.mDisplayMode) {
                case 0:
                    ch1.setVisibility(0);
                    ch2.setVisibility(0);
                    break;
                case 1:
                case 3:
                    this.removeView(ch1);
                    this.addView(ch1, 0);
                    ch1.setVisibility(0);
                    ch2.setVisibility(this.mEnableSubWindow && this.mDisplayMode != 3 ? 0 : 4);
                    break;
                case 2:
                case 4:
                    this.removeView(ch2);
                    this.addView(ch2, 0);
                    ch1.setVisibility(this.mEnableSubWindow && this.mDisplayMode != 4 ? 0 : 4);
                    ch2.setVisibility(0);
            }
        } finally {
            this.mChild1 = this.mFlipChildPos ? ch2 : ch1;
            this.mChild2 = this.mFlipChildPos ? ch1 : ch2;
        }

        this.requestLayout();
    }

    private void cancelAnimation() {
        synchronized(this.mSync) {
            if (this.mAnimator != null) {
                this.mAnimator.cancel();
                this.mAnimator = null;
            }

        }
    }
}
