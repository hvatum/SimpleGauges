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

    private int value = 80;
    private int maxValue = 100;

    public BarGauge(Context context) {
        super(context);
    }

    public BarGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
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

        setMeasuredDimension(100, 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bounds = canvas.getClipBounds();
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.GRAY);
        p.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRect(bounds, p);
        p.setColor(Color.BLACK);
        canvas.drawRect(new Rect(bounds.left, bounds.top, bounds.left + (int) ((value / (double) maxValue) * (bounds.right - bounds.left)), bounds.bottom), p);
        canvas.drawText("test", bounds.centerX(), bounds.centerY(), p);
    }
}
