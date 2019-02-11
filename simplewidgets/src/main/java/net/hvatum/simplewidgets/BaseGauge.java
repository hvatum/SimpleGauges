package net.hvatum.simplewidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;

import java.util.List;

/**
 * Created by hvatum on 01.02.19.
 */

abstract class BaseGauge extends android.support.v7.widget.AppCompatImageView {

    protected int numTicks = 6;
    protected int tickLength = 40;
    protected List<Pair<Float, Integer>> backgroundColors;
    protected boolean drawTicks = true;
    private int valueColor = Color.blue(204);

    private boolean ticksAtBackgroundColorChange = false;
    Rect bounds = new Rect();
    protected float value = 80;
    protected float maxValue = 100;
    protected int margin = 40;
    protected float strokeWidth = 50.0f;
    protected float labelPos = 48f;

    protected boolean drawText = false;
    private int textSize = 48;
    protected String unit = null;

    private Paint p = new Paint();
    private Paint.Cap strokeCap = Paint.Cap.ROUND;

    public static final int SQUARE = 0;
    public static final int ROUND = 1;
    public static final int BUTT = 2;


    public BaseGauge(Context context) {
        super(context);
    }

    public BaseGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BaseGauge,
                0, 0);

        try {
            setDrawValueText(a.getBoolean(R.styleable.BaseGauge_showLabel, false));
            setLabelTextSize(a.getInteger(R.styleable.BaseGauge_labelTextSize, 48));
            setUnit(a.getString(R.styleable.BaseGauge_unit));
            setValue(a.getInteger(R.styleable.BaseGauge_value, 30));
            setMaxValue(a.getInteger(R.styleable.BaseGauge_maxValue, 100));
            setNumTicks(a.getInteger(R.styleable.BaseGauge_numTicks, 6));
            setTickLength(a.getInteger(R.styleable.BaseGauge_tickLength, 40));
            setTicksAtBackgroundColorChange(a.getBoolean(R.styleable.BaseGauge_ticksAtBackgroundColorChange, false));
            setValueStrokeCaps(a.getInt(R.styleable.BaseGauge_valueStrokeCaps, ROUND));
            setValueColors(a.getColor(R.styleable.BaseGauge_valueColor, Color.blue(204)));
            setLabelPosFromBottom(a.getDimension(R.styleable.BaseGauge_labelPosFromBottom, 48f));
        } finally {
            a.recycle();
        }
    }


    public void setLabelPosFromBottom(float labelPos) {
        this.labelPos = labelPos;
    }

    public int colorFromArgb(int a, int r, int g, int b) {
            return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
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

    public void setValueStrokeCaps(int strokeCap) {
        switch (strokeCap) {
            case SQUARE:
                setValueStrokeCaps(Paint.Cap.SQUARE);
                break;
            case BUTT:
                setValueStrokeCaps(Paint.Cap.BUTT);
                break;
            case ROUND:
                setValueStrokeCaps(Paint.Cap.ROUND);
                break;
            default:
                setValueStrokeCaps(Paint.Cap.BUTT);
                break;
        }
    }

    public void setValueStrokeCaps(Paint.Cap strokeCap) {
        this.strokeCap = strokeCap;
    }

    public void setLabelTextSize(int textSize) {
        this.textSize = textSize;
    }

    private Paint getCommonPaint() {
        p.reset();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(strokeWidth);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setAntiAlias(true);
        return p;
    }

    protected Paint getValuePaint() {
        Paint p = getCommonPaint();
        p.setColor(valueColor);
        p.setStrokeWidth(strokeWidth/1.4f);
        p.setAlpha(190);
        p.setStrokeCap(strokeCap);
        return p;
    }

    protected Paint getTextPaint() {
        Paint p = getCommonPaint();
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(4.0f);
        return p;
    }

    protected Paint getBackgroundPaint() {
        Paint p = getCommonPaint();
        p.setColor(Color.GRAY);
        p.setAlpha(30);
        return p;
    }


    protected Paint getLabelTextPaint() {
        Paint p = getTextPaint();
        p.setTextSize(40);
        return p;
    }

    public void setBackgroundColors(List<Pair<Float, Integer>> backgroundColors) {
        this.backgroundColors = backgroundColors;
    }

    public void setValueColors(int r, int g, int b) {
        setValueColors(255, r, g, b);
    }

    public void setValueColors(int a, int r, int g, int b) {
        setValueColors(colorFromArgb(a, r, g, b));
    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    public void setValueColors(int valueColor) {
        this.valueColor= valueColor;
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

}
