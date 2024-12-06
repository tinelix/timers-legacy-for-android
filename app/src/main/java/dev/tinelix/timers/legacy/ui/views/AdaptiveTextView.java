package dev.tinelix.timers.legacy.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AdaptiveTextView extends TextView {

    public AdaptiveTextView(Context context) {
        super(context);
    }

    public AdaptiveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoResizedText(CharSequence text) {
        super.setText(text);
        if(text.length() > 4) {
            setTextSize((float) (getTextSize() / (1.25 * text.length())));
        }
    }
}
