package net.hvatum.simplewidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by hvatum on 01.02.19.
 */

public class BarGauge extends BaseGauge {

    public BarGauge(Context context) {
        super(context);
    }

    public BarGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - 100 + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - 100, heightMeasureSpec, 0);

        setMeasuredDimension(0, IntegerMax((int)strokeWidth*2,20));
    }

    private int IntegerMax(int i, int i1) {
        return i < i1 ? i1 : i;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = canvas.getClipBounds();
        Paint p = getBackgroundPaint();
        canvas.drawLine(bounds.left + margin, bounds.centerY(),bounds.right - margin, bounds.centerY(), p);
        p = getValuePaint();
        //canvas.drawLine((float) (centerX + sin * (radius - tickLength)), (float) (centerY - cos * (radius - tickLength)), (float) (centerX + sin * radius), (float) (centerY - cos * radius), getLabelTextPaint());
        canvas.drawLine(bounds.left + margin, bounds.centerY(),bounds.left + (int) ((value / (double) maxValue) * (bounds.right - bounds.left)) - margin, bounds.centerY(), p);
        if (drawText) {
            if (unit != null && !unit.isEmpty()) {
                canvas.drawText(value + " " + unit, bounds.centerX(), bounds.centerY(), p);
            } else {
                canvas.drawText(Float.toString(value), bounds.centerX(), bounds.centerY(), p);
            }
        }
    }
}
