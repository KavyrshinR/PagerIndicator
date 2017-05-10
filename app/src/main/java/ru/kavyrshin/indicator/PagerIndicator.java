package ru.kavyrshin.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private int selectedColor;
    private int unselectedColor;

    private int currentPosition;

    private Paint paint;

    public PagerIndicator(Context context) {
        super(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        countIndicator = typedArray.getInteger(R.styleable.PagerIndicator_pi_count, 0);
        dotRadiusPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_radius, Util.dpToPx(DEFAULT_RADIUS));
        dotPaddingPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_dotPadding, Util.dpToPx(DEFAULT_PADDING));

        selectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_selectedColor, ContextCompat.getColor(getContext(), R.color.default_selected));
        unselectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_unselectedColor, ContextCompat.getColor(getContext(), R.color.default_unselected));

        typedArray.recycle();
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthResult = 0;
        int heightResult = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthResult = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightResult = MeasureSpec.getSize(heightMeasureSpec);

        int necessaryWidthPx = (dotRadiusPx * 2 * countIndicator) + (dotPaddingPx * (countIndicator - 1));
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

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        paint.setColor(unselectedColor);

        int step = (2 * dotRadiusPx) + dotPaddingPx;

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < countIndicator; i++) {

            if (i == currentPosition) {
                paint.setColor(selectedColor);
            } else {
                paint.setColor(unselectedColor);
            }

            int x = dotRadiusPx + (step * i);
            canvas.drawCircle(x, height / 2, dotRadiusPx, paint);
        }
    }

    public int getCountIndicator() {
        return countIndicator;
    }

    public void setCountIndicator(int countIndicator) {
        this.countIndicator = countIndicator;
        invalidate();
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        invalidate();
    }

    public int getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
        invalidate();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        invalidate();
    }
}
