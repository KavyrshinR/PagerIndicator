package ru.kavyrshinr.pagerindicator;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class PagerIndicator extends View implements ViewPager.OnPageChangeListener {

    private int dotRadiusPx;
    private int dotPaddingPx;

    private int countIndicator;

    private int selectedColor;
    private int unselectedColor;

    private int currentPosition;
    private int selectingPosition;

    private int animationDuration = 350;
    private ValueAnimator valueAnimator;
    private int frameFrom;

    private Paint paint;

    public PagerIndicator(Context context) {
        super(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        countIndicator = typedArray.getInteger(R.styleable.PagerIndicator_pi_count, 0);
        dotRadiusPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_radius, Util.dpToPx(Const.DEFAULT_RADIUS));
        dotPaddingPx = typedArray.getInteger(R.styleable.PagerIndicator_pi_dotPadding, Util.dpToPx(Const.DEFAULT_PADDING));

        selectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_selectedColor, Color.parseColor(Const.COLOR_DEFAULT_SELECTED));
        unselectedColor = typedArray.getColor(R.styleable.PagerIndicator_pi_unselectedColor, Color.parseColor(Const.COLOR_DEFAULT_UNSELECTED));

        typedArray.recycle();

        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(animationDuration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frameFrom = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
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



        setMeasuredDimension(Math.max(0, widthResult), Math.max(0, heightResult));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawIndicator(canvas);
        drawIndicatorWithSwapAnimation(canvas);
    }

    private void drawIndicatorWithSwapAnimation(Canvas canvas) {
        paint.setColor(unselectedColor);

        int step = (2 * dotRadiusPx) + dotPaddingPx;

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < countIndicator; i++) {

            int selectingSrcX = getXCoordinate(selectingPosition);
            int currentSrcX = getXCoordinate(currentPosition);

            if (i == currentPosition) {
                paint.setColor(selectedColor);
                int x = frameFrom;
                canvas.drawCircle(x, height / 2, dotRadiusPx, paint);
            } else if(i == selectingPosition) {
                paint.setColor(unselectedColor);
                int x = selectingSrcX > frameFrom ? selectingSrcX - (frameFrom - currentSrcX) : selectingSrcX + (currentSrcX - frameFrom);
                canvas.drawCircle(x, height / 2, dotRadiusPx, paint);
            } else {
                paint.setColor(unselectedColor);
                int x = dotRadiusPx + (step * i);
                canvas.drawCircle(x, height / 2, dotRadiusPx, paint);
            }
        }
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

            int x = dotRadiusPx + (step * i); // Начинаем рисовать не с нуля, а с точки куда вместится первая "точка"
            canvas.drawCircle(x, height / 2, dotRadiusPx, paint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        boolean scrollToRight = position == currentPosition;

        if (!scrollToRight) {
            selectingPosition = position;
        } else {
            selectingPosition = currentPosition + 1;
        }

        int playTime = (int) (animationDuration * positionOffset);

        int valueFrom;
        int valueTo;

        if (scrollToRight) {
            valueFrom = getXCoordinate(currentPosition);
            valueTo = getXCoordinate(selectingPosition);
        } else {
            valueFrom = getXCoordinate(selectingPosition);
            valueTo = getXCoordinate(currentPosition);
        }


        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofInt("values", valueFrom, valueTo);
        propertyValuesHolder.setEvaluator(new IntEvaluator());
        valueAnimator.setValues(propertyValuesHolder);
        valueAnimator.setCurrentPlayTime(playTime);
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public int getCountIndicator() {
        return countIndicator;
    }

    public void setCountIndicator(int countIndicator) {
        this.countIndicator = countIndicator;
        requestLayout();
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

    private int getXCoordinate(int position) {
        return dotRadiusPx + (dotRadiusPx * 2 + dotPaddingPx) * position;
    }

    private int getYCoordinate(int position) {
        return dotRadiusPx / 2;
    }
}