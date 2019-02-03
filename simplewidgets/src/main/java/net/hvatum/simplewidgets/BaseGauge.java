package net.hvatum.simplewidgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by hvatum on 01.02.19.
 */

abstract class BaseGauge extends android.support.v7.widget.AppCompatImageView {
    public BaseGauge(Context context) {
        super(context);
    }

    public BaseGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
