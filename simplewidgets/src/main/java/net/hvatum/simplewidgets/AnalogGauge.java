package net.hvatum.simplewidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by hvatum on 01.02.19.
 */

public class AnalogGauge extends BaseGauge {

    Rect bounds = new Rect();
    private float value = 80;
    private float maxValue = 100;
    private int margin = 40;
    private float strokeWidth = 50.0f;
    private float gaugeStart;
    private float gaugeSweep;
    private boolean drawText = false;
    private String unit = null;

    public AnalogGauge(Context context) {
        super(context);
        initComponents();
    }


    public AnalogGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initComponents();

    }

    private Paint getValuePaint() {
        Paint p = getCommonPaint();
        p.setColor(Color.BLACK);
        return p;
    }

    private Paint getTextPaint() {
        Paint p = getCommonPaint();
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(96.0f);
        return p;
    }

    private Paint getBackgroundPaint() {
        Paint p = getCommonPaint();
        p.setColor(Color.GRAY);
        p.setAlpha(30);
        return p;
    }

    public void setArcDegrees(float degrees) {
        this.gaugeStart = 90.0f + (360.0f - degrees) / 2;
        this.gaugeSweep = degrees;
    }

    private Paint getCommonPaint() {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(strokeWidth);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        return p;
    }

    private void initComponents() {
        setArcDegrees(300.0f);
    }

    public void setValue(int value) {
        this.value = value;
        invalidate();
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setDrawValueText(boolean drawText) {
        this.drawText = drawText;
    }

    /*
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Try for a width based on our minimum
            int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
            int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

            // Whatever the width ends up being, ask for a height that would let the pie
            // get as big as it can
            int minh = MeasureSpec.getSize(w) - (int)100 + getPaddingBottom() + getPaddingTop();
            int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)100, heightMeasureSpec, 0);

            setMeasuredDimension(100, 20);
        }
    */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //canvas.rotate(45);

        canvas.getClipBounds(bounds);

        RectF gaugeBounds = getGaugeRect(bounds);
        Path backgroundPath = new Path();
        backgroundPath.addArc(new RectF(gaugeBounds), gaugeStart, gaugeSweep);
        canvas.drawPath(backgroundPath, getBackgroundPaint());

        Path valuePath = new Path();
        float rValue = gaugeSweep * (value / maxValue);
        if (rValue > gaugeSweep) {
            rValue = gaugeSweep;
        }
        valuePath.addArc(new RectF(gaugeBounds), gaugeStart, rValue);
        canvas.drawPath(valuePath, getValuePaint());

        if (drawText) {
            if (unit != null) {
                canvas.drawText(value + " " + unit, gaugeBounds.centerX(), gaugeBounds.bottom - margin, getTextPaint());
            }
        }

        canvas.restore();
    }


    @NonNull
    private RectF getGaugeRect(Rect bounds) {
        int diameter1 = bounds.right - bounds.left;
        int diameter2 = bounds.bottom - bounds.top;
        int diameter = diameter1 > diameter2 ? diameter2 : diameter1;
        int radius = diameter / 2;
        RectF gaugeBounds = new RectF(bounds.centerX() - radius, bounds.centerY() - radius, bounds.centerX() + radius, bounds.centerY() + radius);

        gaugeBounds.left = gaugeBounds.left + margin;
        gaugeBounds.right = gaugeBounds.right - margin;
        gaugeBounds.top = gaugeBounds.top + margin;
        gaugeBounds.bottom = gaugeBounds.bottom - margin;
        return gaugeBounds;
    }
}
