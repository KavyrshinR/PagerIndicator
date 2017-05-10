package ru.kavyrshin.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class PagerIndicator extends View {

    private static final int DEFAULT_RADIUS = 6;
    private static final int DEFAULT_PADDING = 8;

    private int dotRadiusPx;
    private int dotPaddingPx;

    private int countIndicator;

    public PagerIndicator(Context context) {
        super(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        countIndicator = typedArray.getInteger(R.styleable.PagerIndicator_pi_count, 0);
        dotRadiusPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_radius, Util.dpToPx(DEFAULT_RADIUS));
        dotPaddingPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_dotPadding, Util.dpToPx(DEFAULT_PADDING));

        typedArray.recycle();
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthResult = 0;
        int heightResult = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthResult = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightResult = MeasureSpec.getSize(heightMeasureSpec);

        int necessaryWidthPx = (dotRadiusPx * 2 * countIndicator) + ((dotPaddingPx - 1) * countIndicator);
        int necessaryHeightPx = dotRadiusPx * 2;

        if (widthMode == MeasureSpec.AT_MOST) {
            widthResult = Math.min(widthResult, necessaryWidthPx);
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthResult = necessaryWidthPx;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightResult = Math.min(heightResult, necessaryHeightPx);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightResult = necessaryHeightPx;
        }


        setMeasuredDimension(widthResult, heightResult);
    }
}
