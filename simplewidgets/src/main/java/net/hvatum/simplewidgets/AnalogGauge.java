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


    private boolean ticksAtBackgroundColorChange = false;
    Rect bounds = new Rect();
    private float value = 80;
    private float maxValue = 100;
    private int margin = 40;
    private float strokeWidth = 50.0f;
    private float gaugeStart;
    private float gaugeSweep;
    private boolean drawText = false;
    private int textSize = 48;
    private String unit = null;
    private float labelPos = 48f;
    private int numTicks = 6;
    private int tickLength = 40;
    private List<Pair<Float, Integer>> backgroundColors;
    private boolean drawTicks = true;

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
            setDrawValueText(a.getBoolean(R.styleable.AnalogGauge_showLabel, false));
            setLabelTextSize(a.getInteger(R.styleable.AnalogGauge_labelTextSize, 48));
            setLabelPostFromBottom(a.getDimension(R.styleable.AnalogGauge_labelPosFromBottom, 48f));
            setUnit(a.getString(R.styleable.AnalogGauge_unit));
            setValue(a.getInteger(R.styleable.AnalogGauge_value, 30));
            setMaxValue(a.getInteger(R.styleable.AnalogGauge_maxValue, 100));
            setArcDegrees(a.getInteger(R.styleable.AnalogGauge_arcSweep, 300));
            setNumTicks(a.getInteger(R.styleable.AnalogGauge_numTicks, 6));
            setTickLength(a.getInteger(R.styleable.AnalogGauge_tickLength, 40));
            setTicksAtBackgroundColorChange(a.getBoolean(R.styleable.AnalogGauge_ticksAtBackgroundColorChange, false));
        } finally {
            a.recycle();
        }
    }

    public void setNumTicks(int numTicks) {
        this.numTicks = numTicks;
    }

    public void setTickLength(int tickLength) {
        this.tickLength = tickLength;
    }

    public void setTicksAtBackgroundColorChange(boolean ticksAtBackgroundColorChange) {
        this.ticksAtBackgroundColorChange = ticksAtBackgroundColorChange;
    }

    public void setLabelTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setLabelPostFromBottom(float labelPos) {
        this.labelPos = labelPos;
    }

    private Paint getValuePaint() {
        Paint p = getCommonPaint();
        p.setColor(Color.blue(204));
        p.setStrokeWidth(strokeWidth/1.4f);
        p.setAlpha(190);
        return p;
    }

    private Paint getTextPaint() {
        Paint p = getCommonPaint();
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(4.0f);
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
        p.setAntiAlias(true);
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
                Log.i("Sweep", "Sweep from " + currentSweep + " to " + toSweep + " gets color " + floatColorPair.second);
            }
        }

        if (drawTicks && numTicks > 0) {
            float centerX = gaugeBounds.centerX();
            float centerY = gaugeBounds.centerY();



            float radius = gaugeBounds.right - gaugeBounds.centerX();

            int text_margin = tickLength + 60;
            for (int i = 0; i < numTicks; i++) {
                float angle = 90 + gaugeStart + i * (gaugeSweep/(numTicks-1));
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));
                canvas.drawLine((float) (centerX + sin * (radius - tickLength)), (float) (centerY - cos * (radius - tickLength)), (float) (centerX + sin * radius), (float) (centerY - cos * radius), getLabelTextPaint());
                canvas.drawText(String.valueOf((int)(i*maxValue/(numTicks-1))), (float) (centerX + sin * (radius - text_margin)), (float) (centerY - cos * (radius - text_margin)), getLabelTextPaint());
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
            }
        }

        canvas.restore();
    }

    private Paint getLabelTextPaint() {
        Paint p = getTextPaint();
        p.setTextSize(40);
        return p;
    }

    public void setBackgroundColors(List<Pair<Float, Integer>> backgroundColors) {
        this.backgroundColors = backgroundColors;
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
