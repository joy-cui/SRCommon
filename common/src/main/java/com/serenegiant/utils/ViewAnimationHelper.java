//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.serenegiant.common.R.id;

public class ViewAnimationHelper {
    private static final String TAG = ViewAnimationHelper.class.getSimpleName();
    private static final long DEFAULT_DURATION_MS = 500L;
    public static final int ANIMATION_UNKNOWN = -1;
    public static final int ANIMATION_FADE_OUT = 0;
    public static final int ANIMATION_FADE_IN = 1;
    public static final int ANIMATION_ZOOM_IN = 2;
    public static final int ANIMATION_ZOOM_OUT = 3;
    private static final AnimatorListener mAnimatorListener = new AnimatorListener() {
        public void onAnimationStart(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 0);
        }

        public void onAnimationEnd(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 1);
        }

        public void onAnimationCancel(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 2);
        }

        public void onAnimationRepeat(Animator animation) {
        }
    };

    public ViewAnimationHelper() {
    }

    @SuppressLint({"NewApi"})
    public static void fadeIn(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
        if (target != null) {
            target.postDelayed(new Runnable() {
                @SuppressLint("WrongConstant")
                public void run() {
                    target.setVisibility(0);
                    target.setTag(id.anim_type, 1);
                    target.setTag(id.anim_listener, listener);
                    target.setScaleX(1.0F);
                    target.setScaleY(1.0F);
                    target.setAlpha(0.0F);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", new float[]{0.0F, 1.0F});
                    objectAnimator.addListener(ViewAnimationHelper.mAnimatorListener);
                    if (BuildCheck.isJellyBeanMR2()) {
                        objectAnimator.setAutoCancel(true);
                    }

                    objectAnimator.setDuration(duration > 0L ? duration : 500L);
                    objectAnimator.setStartDelay(startDelay > 0L ? startDelay : 0L);
                    objectAnimator.start();
                }
            }, 100L);
        }
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    public static void fadeOut(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
        if (target != null && target.getVisibility() == 0) {
            target.postDelayed(new Runnable() {
                public void run() {
                    target.setTag(id.anim_type, 0);
                    target.setTag(id.anim_listener, listener);
                    target.setScaleX(1.0F);
                    target.setScaleY(1.0F);
                    target.setAlpha(1.0F);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", new float[]{1.0F, 0.0F});
                    objectAnimator.addListener(ViewAnimationHelper.mAnimatorListener);
                    if (BuildCheck.isAndroid4_3()) {
                        objectAnimator.setAutoCancel(true);
                    }

                    objectAnimator.setDuration(duration > 0L ? duration : 500L);
                    objectAnimator.setStartDelay(startDelay > 0L ? startDelay : 0L);
                    objectAnimator.start();
                }
            }, 100L);
        }

    }

    @SuppressLint({"NewApi"})
    public static void zoomIn(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
        if (target != null) {
            target.postDelayed(new Runnable() {
                @SuppressLint("WrongConstant")
                public void run() {
                    target.setVisibility(0);
                    target.setTag(id.anim_type, 2);
                    target.setTag(id.anim_listener, listener);
                    target.setScaleX(0.0F);
                    target.setScaleY(0.0F);
                    target.setAlpha(1.0F);
                    PropertyValuesHolder scale_x = PropertyValuesHolder.ofFloat("scaleX", new float[]{0.01F, 1.0F});
                    PropertyValuesHolder scale_y = PropertyValuesHolder.ofFloat("scaleY", new float[]{0.01F, 1.0F});
                    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{scale_x, scale_y});
                    objectAnimator.addListener(ViewAnimationHelper.mAnimatorListener);
                    if (BuildCheck.isJellyBeanMR2()) {
                        objectAnimator.setAutoCancel(true);
                    }

                    objectAnimator.setDuration(duration > 0L ? duration : 500L);
                    objectAnimator.setStartDelay(startDelay > 0L ? startDelay : 0L);
                    objectAnimator.start();
                }
            }, 100L);
        }
    }

    @SuppressLint({"NewApi"})
    public static void zoomOut(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
        if (target != null) {
            target.postDelayed(new Runnable() {
                @SuppressLint("WrongConstant")
                public void run() {
                    target.setVisibility(0);
                    target.setTag(id.anim_type, 3);
                    target.setTag(id.anim_listener, listener);
                    target.setScaleX(1.0F);
                    target.setScaleY(1.0F);
                    target.setAlpha(1.0F);
                    PropertyValuesHolder scale_x = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0F, 0.0F});
                    PropertyValuesHolder scale_y = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0F, 0.0F});
                    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{scale_x, scale_y});
                    objectAnimator.addListener(ViewAnimationHelper.mAnimatorListener);
                    if (BuildCheck.isJellyBeanMR2()) {
                        objectAnimator.setAutoCancel(true);
                    }

                    objectAnimator.setDuration(duration > 0L ? duration : 500L);
                    objectAnimator.setStartDelay(startDelay > 0L ? startDelay : 0L);
                    objectAnimator.start();
                }
            }, 100L);
        }
    }

    private static void onAnimation(Animator animator, final int event) {
        if (animator instanceof ObjectAnimator) {
            final ObjectAnimator anim = (ObjectAnimator)animator;
            final View target = (View)anim.getTarget();
            if (target == null) {
                return;
            }

            final ViewAnimationListener listener = (ViewAnimationListener)target.getTag(id.anim_listener);

            final int animType;
            try {
                animType = (Integer)target.getTag(id.anim_type);
            } catch (Exception var7) {
                return;
            }

            if (listener != null) {
                target.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            switch(event) {
                                case 0:
                                    listener.onAnimationStart(anim, target, animType);
                                    break;
                                case 1:
                                    listener.onAnimationEnd(anim, target, animType);
                                    break;
                                case 2:
                                    listener.onAnimationCancel(anim, target, animType);
                            }
                        } catch (Exception var2) {
                            Log.w(ViewAnimationHelper.TAG, var2);
                        }

                    }
                }, 100L);
            }
        }

    }

    public interface ViewAnimationListener {
        void onAnimationStart(@NonNull Animator var1, @NonNull View var2, int var3);

        void onAnimationEnd(@NonNull Animator var1, @NonNull View var2, int var3);

        void onAnimationCancel(@NonNull Animator var1, @NonNull View var2, int var3);
    }
}
