package dev.tinelix.timers.legacy.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;

public class TahomaFont {

    public static Typeface getFont(Context ctx, int weight) {
        Typeface typeface;
        switch(weight) {
            case 200:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/tahoma_regular.ttf");
                break;
            case 700:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/tahoma_bold.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/tahoma_regular.ttf");
                break;
        }

        return typeface;
    }
}
