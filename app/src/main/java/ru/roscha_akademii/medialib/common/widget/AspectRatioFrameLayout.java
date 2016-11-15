package ru.roscha_akademii.medialib.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/*
Copypasted from development branch of ExoPlayer
https://github.com/google/ExoPlayer/blob/release-v2/library/src/main/java/com/google/android/exoplayer2/ui/AspectRatioFrameLayout.java
 */

public final class AspectRatioFrameLayout extends FrameLayout {

    /**
     * Either the width or height is decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIT = 0;
    /**
     * The width is fixed and the height is increased or decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    /**
     * The height is fixed and the width is increased or decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;

    /**
     * The {@link FrameLayout} will not resize itself if the fractional difference between its natural
     * aspect ratio and the requested aspect ratio falls below this threshold.
     * <p>
     * This tolerance allows the view to occupy the whole of the screen when the requested aspect
     * ratio is very close, but not exactly equal to, the aspect ratio of the screen. This may reduce
     * the number of view layers that need to be composited by the underlying system, which can help
     * to reduce power consumption.
     */
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;

    private float videoAspectRatio;
    private int resizeMode;

    public AspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        resizeMode = RESIZE_MODE_FIT;
        /*
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.AspectRatioFrameLayout, 0, 0);
            try {
                resizeMode = a.getInt(R.styleable.AspectRatioFrameLayout_resize_mode, RESIZE_MODE_FIT);
            } finally {
                a.recycle();
            }
        }
        */
    }

    /**
     * Set the aspect ratio that this view should satisfy.
     *
     * @param widthHeightRatio The width to height ratio.
     */
    public void setAspectRatio(float widthHeightRatio) {
        if (this.videoAspectRatio != widthHeightRatio) {
            this.videoAspectRatio = widthHeightRatio;
            requestLayout();
        }
    }

    /**
     * Sets the resize mode which can be of value {@link #RESIZE_MODE_FIT},
     * {@link #RESIZE_MODE_FIXED_HEIGHT} or {@link #RESIZE_MODE_FIXED_WIDTH}.
     *
     * @param resizeMode The resize mode.
     */
    public void setResizeMode(int resizeMode) {
        if (this.resizeMode != resizeMode) {
            this.resizeMode = resizeMode;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (videoAspectRatio == 0) {
            // Aspect ratio not set.
            return;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float viewAspectRatio = (float) width / height;

        float aspectDeformation = videoAspectRatio / viewAspectRatio - 1;
        if (viewAspectRatio != Float.POSITIVE_INFINITY
                && Math.abs(aspectDeformation) <= MAX_ASPECT_RATIO_DEFORMATION_FRACTION)
        {
            // We're within the allowed tolerance.

            return;
        }

        switch (resizeMode) {
            case RESIZE_MODE_FIXED_WIDTH:
                height = (int) (width / videoAspectRatio);
                break;
            case RESIZE_MODE_FIXED_HEIGHT:
                width = (int) (height * videoAspectRatio);
                break;
            default:
                if (viewAspectRatio != Float.POSITIVE_INFINITY && aspectDeformation < 0) {
                    width = (int) (height * videoAspectRatio);
                } else {
                    height = (int) (width / videoAspectRatio);
                }
                break;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}

