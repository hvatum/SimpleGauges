package net.hvatum.simplewidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

import java.util.List;

/**
 * Created by hvatum on 01.02.19.
 */

public class AnalogGauge extends BaseGauge {



    private float gaugeStart;
    private float gaugeSweep;

    public AnalogGauge(Context context) {
        super(context);
        initComponents();
    }


    public AnalogGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initComponents();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.AnalogGauge,
                0, 0);

        try {
            setArcDegrees(a.getFloat(R.styleable.AnalogGauge_arcSweep, 30));
        } finally {
            a.recycle();
        }
    }


    public void setArcDegrees(float degrees) {
        this.gaugeStart = 90.0f + (360.0f - degrees) / 2;
        this.gaugeSweep = degrees;
    }

    private void initComponents() {
        setArcDegrees(300.0f);
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
        Paint backgroundPaint = getBackgroundPaint();

        backgroundPath.addArc(new RectF(gaugeBounds), gaugeStart, gaugeSweep);
        canvas.drawPath(backgroundPath, backgroundPaint);
        if (backgroundColors != null) {
            float currentSweep = 0.0f;
            for (Pair<Float, Integer> floatColorPair : backgroundColors) {
                float toSweep = gaugeSweep * (floatColorPair.first / maxValue);
                float sweepLength = toSweep - currentSweep;
                backgroundPath.reset();
                backgroundPath.addArc(new RectF(gaugeBounds), gaugeStart + currentSweep, sweepLength);
                currentSweep += sweepLength;
                int c = floatColorPair.second;
                backgroundPaint.setColor(c);
                backgroundPaint.setStrokeCap(Paint.Cap.BUTT);
                canvas.drawPath(backgroundPath, backgroundPaint);
            }
        }

        if (drawTicks && numTicks > 0) {
            float centerX = gaugeBounds.centerX();
            float centerY = gaugeBounds.centerY();


            float radius = gaugeBounds.right - gaugeBounds.centerX();

            int text_margin = tickLength + 60;
            for (int i = 0; i < numTicks; i++) {
                float angle = 90 + gaugeStart + i * (gaugeSweep / (numTicks - 1));
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));
                canvas.drawLine((float) (centerX + sin * (radius - tickLength)), (float) (centerY - cos * (radius - tickLength)), (float) (centerX + sin * radius), (float) (centerY - cos * radius), getLabelTextPaint());
                canvas.drawText(String.valueOf((int) (i * maxValue / (numTicks - 1))), (float) (centerX + sin * (radius - text_margin)), (float) (centerY - cos * (radius - text_margin)), getLabelTextPaint());
            }
        }

        Path valuePath = new Path();
        float rValue = gaugeSweep * (value / maxValue);
        if (rValue > gaugeSweep) {
            rValue = gaugeSweep;
        }
        valuePath.addArc(new RectF(gaugeBounds), gaugeStart, rValue);
        canvas.drawPath(valuePath, getValuePaint());

        if (drawText) {
            if (unit != null) {
                canvas.drawText(value + " " + unit, gaugeBounds.centerX(), gaugeBounds.bottom - labelPos, getTextPaint());
            } else {
                canvas.drawText(Float.toString(value), gaugeBounds.centerX(), gaugeBounds.bottom - labelPos, getTextPaint());
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
